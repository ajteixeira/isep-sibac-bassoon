package org.sibac.bassoon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.kie.api.KieServices;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.sibac.bassoon.cf.FactListener;
import org.sibac.bassoon.cf.RuleFiredTracker;
import org.sibac.bassoon.cf.TrackingAgendaListener;
import org.sibac.bassoon.fuzzy.FuzzySuitabilityService;
import org.sibac.bassoon.fuzzy.StudentLevelMapper;
import org.sibac.bassoon.model.Accompaniment;
import org.sibac.bassoon.model.Era;
import org.sibac.bassoon.model.Evidence;
import org.sibac.bassoon.model.EvidenceType;
import org.sibac.bassoon.model.Hypothesis;
import org.sibac.bassoon.model.Motivation;
import org.sibac.bassoon.model.Skill;
import org.sibac.bassoon.model.Work;
import org.sibac.bassoon.output.FiredRule;
import org.sibac.bassoon.output.Recommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Core recommendation pipeline.
 *
 * <ol>
 *   <li>Map student level + motivation to continuous value
 *   <li>Fuzzy: suitability of each work for the student (threshold filters)
 *   <li>Insert Works + Hypotheses + Evidences into a Drools session
 *   <li>fireAllRules() — skills, accompaniment, and era rules adjust each CF
 *   <li>Collect candidacies, sort by CF descending
 *   <li>Reorder to satisfy prerequisites
 * </ol>
 *
 * <p>Used by {@code DroolsService} (the REST API service).
 */
public class RecommendationEngine {

  private static final Logger LOG = LoggerFactory.getLogger(RecommendationEngine.class);

  private static final double SUITABILITY_THRESHOLD = 0.5;
  private static final double MIN_RECOMMENDATION_SCORE = 0.30;

  private final KieContainer kieContainer;
  private final FuzzySuitabilityService fuzzy;

  public RecommendationEngine() {
    this.kieContainer = KieServices.Factory.get().getKieClasspathContainer();
    this.fuzzy = new FuzzySuitabilityService();
  }

  /**
   * Runs the full pipeline and returns recommendations ordered by score,
   * with prerequisites applied.
   *
   * @param catalog work catalog
   * @param studentLevel continuous student level on the 1.5–5.5 axis
   * @param motivation student motivation (may be null)
   * @param skills list of (skill, cf) in priority order (1 to 3)
   * @param lastEra last studied era (may be null)
   * @param accompaniments preferred accompaniments (may be null or empty)
   */
  public List<Recommendation> run(
      List<Work> catalog,
      double studentLevel,
      Motivation motivation,
      List<SkillInput> skills,
      Era lastEra,
      List<AccompanimentInput> accompaniments) {

    // --- fresh Drools session ---
    KieSession kSession = kieContainer.newKieSession("rulesSession");
    kSession.addEventListener(new TrackingAgendaListener());
    kSession.addEventListener(new FactListener());

    Map<Double, Work> worksById = indexById(catalog);
    Map<String, Work> worksByName = indexByName(catalog);

    // --- fuzzy front-end ---
    double fuzzyLevel = StudentLevelMapper.toStudentLevel(studentLevel, motivation);
    LOG.debug("Fuzzy studentLevel = {} (input={}, motivation={})", fuzzyLevel, studentLevel,
        motivation);

    int inserted = 0;
    int filtered = 0;
    Map<String, Double> initialScores = new HashMap<>();

    for (Work work : catalog) {
      double suitability = fuzzy.suitability(fuzzyLevel, work.getDifficultyLevel().getValue());

      if (suitability >= SUITABILITY_THRESHOLD) {
        kSession.insert(work);
        kSession.insert(new Hypothesis(Hypothesis.CANDIDATE, work.getName(), 0.0));
        initialScores.put(work.getName(), suitability);
        inserted++;
      } else {
        filtered++;
      }

      LOG.debug("  {} (dif {}) -> suitability {} -> {}",
          work.getName(), work.getDifficultyLevel().getValue(),
          String.format("%.3f", suitability),
          suitability >= SUITABILITY_THRESHOLD ? "INSERTED" : "FILTERED");
    }

    LOG.info("Fuzzy: {} works passed, {} filtered (threshold={})", inserted, filtered,
        SUITABILITY_THRESHOLD);

    if (inserted == 0) {
      kSession.dispose();
      return List.of();
    }

    // --- insert evidences ---
    for (int i = 0; skills != null && i < skills.size() && i < 3; i++) {
      SkillInput si = skills.get(i);
      EvidenceType type = switch (i) {
        case 0 -> EvidenceType.SKILL_1;
        case 1 -> EvidenceType.SKILL_2;
        default -> EvidenceType.SKILL_3;
      };
      kSession.insert(new Evidence(type, si.skill(), si.cf()));
    }

    if (lastEra != null) {
      kSession.insert(new Evidence(EvidenceType.LAST_ERA, lastEra));
    }

    if (accompaniments != null) {
      for (var a : accompaniments) {
        kSession.insert(new Evidence(EvidenceType.PREFERRED_ACCOMPANIMENT,
            a.accompaniment(), a.cf()));
      }
    }

    // --- Drools ---
    kSession.fireAllRules();

    // --- collect and sort by CF ---
    @SuppressWarnings("unchecked")
    Collection<Hypothesis> candidates =
        (Collection<Hypothesis>) kSession.getObjects(new ClassObjectFilter(Hypothesis.class));

    List<Recommendation> recommendations = new ArrayList<>();
    for (Hypothesis h : candidates) {
      if (!Hypothesis.CANDIDATE.equals(h.getDescription())) {
        continue;
      }
      List<FiredRule> firedRules = RuleFiredTracker.getRules(h);
      double initialScore = initialScores.getOrDefault(h.getValue(), h.getCf());
      Work hWork = worksByName.get(h.getValue());
      double wid = (hWork != null) ? hWork.getId() : -1;
      recommendations.add(
          new Recommendation(wid, h.getValue(), h.getCf(), initialScore, firedRules));
    }

    RuleFiredTracker.clear();

    recommendations.sort(Comparator.comparingDouble(Recommendation::getScore).reversed());

    // --- prerequisites ---
    recommendations = applyPrerequisites(recommendations, worksById);

    // --- drop works below minimum score ---
    recommendations.removeIf(r -> r.getScore() < MIN_RECOMMENDATION_SCORE);

    kSession.dispose();
    return recommendations;
  }

  // -----------------------------------------------------------------------
  // prerequisites
  // -----------------------------------------------------------------------

  private List<Recommendation> applyPrerequisites(
      List<Recommendation> ordered, Map<Double, Work> catalog) {

    List<Recommendation> result = new ArrayList<>(ordered);
    boolean moved;
    do {
      moved = false;
      for (int i = result.size() - 1; i >= 0; i--) {
        Recommendation rec = result.get(i);
        Work work = catalog.get(rec.getWorkId());

        if (work == null || work.getPrerequisiteId() < 0) {
          continue;
        }

        Work prereq = catalog.get(work.getPrerequisiteId());
        if (prereq == null) {
          LOG.warn("Prerequisite not found: id={} for work {}", work.getPrerequisiteId(),
              work.getName());
          continue;
        }

        Recommendation prereqRec = findById(result, prereq.getId());
        if (prereqRec != null && result.indexOf(prereqRec) > i) {
          result.remove(prereqRec);
          result.add(i, prereqRec);
          moved = true;
          LOG.info("Prerequisite reorder: {} moved up before {}", prereq.getName(),
              work.getName());
        }
      }
    } while (moved);

    return result;
  }

  // -----------------------------------------------------------------------
  // helpers
  // -----------------------------------------------------------------------

  private static Map<Double, Work> indexById(List<Work> works) {
    Map<Double, Work> map = new HashMap<>();
    for (Work w : works) {
      map.put(w.getId(), w);
    }
    return map;
  }

  private static Map<String, Work> indexByName(List<Work> works) {
    Map<String, Work> map = new HashMap<>();
    for (Work w : works) {
      map.put(w.getName(), w);
    }
    return map;
  }

  private static Recommendation findById(List<Recommendation> list, double id) {
    for (Recommendation r : list) {
      if (r.getWorkId() == id) {
        return r;
      }
    }
    return null;
  }

  // -----------------------------------------------------------------------
  // input value objects
  // -----------------------------------------------------------------------

  /** A skill requested by the teacher, with its CF. */
  public record SkillInput(Skill skill, double cf) {}

  /** An accompaniment preference, with its CF. */
  public record AccompanimentInput(Accompaniment accompaniment, double cf) {}
}
