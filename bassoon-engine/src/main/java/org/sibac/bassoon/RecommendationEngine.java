package org.sibac.bassoon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import org.sibac.bassoon.model.StudentLevel;
import org.sibac.bassoon.model.Work;
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
 * <p>Used by {@code Main} (standalone) and {@code DroolsService} (REST API).
 */
public class RecommendationEngine {

  private static final Logger LOG = LoggerFactory.getLogger(RecommendationEngine.class);

  private static final double SUITABILITY_THRESHOLD = 0.4;

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
   * @param level student level
   * @param motivation student motivation (may be null)
   * @param skills list of (skill, cf) in priority order (1 to 3)
   * @param lastEra last studied era (may be null)
   * @param accompaniments preferred accompaniments (may be null or empty)
   */
  public List<Recommendation> run(
      List<Work> catalog,
      StudentLevel level,
      Motivation motivation,
      List<SkillInput> skills,
      Era lastEra,
      List<AccompanimentInput> accompaniments) {

    // --- fresh Drools session ---
    KieSession kSession = kieContainer.newKieSession("rulesSession");
    kSession.addEventListener(new TrackingAgendaListener());
    kSession.addEventListener(new FactListener());

    Map<String, Work> worksByName = indexByName(catalog);

    // --- fuzzy front-end ---
    double fuzzyLevel = StudentLevelMapper.toStudentLevel(level, motivation);
    LOG.debug("Fuzzy studentLevel = {} (level={}, motivation={})", fuzzyLevel, level, motivation);

    int inserted = 0;
    int filtered = 0;

    for (Work work : catalog) {
      double suitability = fuzzy.suitability(fuzzyLevel, work.getDifficultyLevel().getValue());

      if (suitability >= SUITABILITY_THRESHOLD) {
        kSession.insert(work);
        kSession.insert(new Hypothesis("candidate", work.getName(), suitability));
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
    kSession.insert(new Evidence(EvidenceType.STUDENT_LEVEL, level));

    if (motivation != null) {
      kSession.insert(new Evidence(EvidenceType.MOTIVATION, motivation));
    }

    for (int i = 0; i < skills.size() && i < 3; i++) {
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
            a.tipo(), a.cf()));
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
      if (!"candidate".equals(h.getDescription())) {
        continue;
      }
      List<String> firedRules = RuleFiredTracker.getRules(h);
      recommendations.add(new Recommendation(h.getValue(), h.getCf(),
          "CF=" + String.format("%.3f", h.getCf()), firedRules));
    }

    RuleFiredTracker.clear();

    recommendations.sort(Comparator.comparingDouble(Recommendation::getScore).reversed());

    // --- prerequisites ---
    recommendations = applyPrerequisites(recommendations, worksByName);

    kSession.dispose();
    return recommendations;
  }

  // -----------------------------------------------------------------------
  // prerequisites
  // -----------------------------------------------------------------------

  private List<Recommendation> applyPrerequisites(
      List<Recommendation> ordered, Map<String, Work> catalog) {

    List<Recommendation> result = new ArrayList<>(ordered);

    for (int i = result.size() - 1; i >= 0; i--) {
      Recommendation rec = result.get(i);
      Work work = catalog.get(rec.getWorkName());

      if (work == null || work.getPrerequisiteId() < 0) {
        continue;
      }

      Work prereq = findWorkById(catalog, work.getPrerequisiteId());
      if (prereq == null) {
        LOG.warn("Prerequisite not found: id={} for work {}", work.getPrerequisiteId(),
            work.getName());
        continue;
      }

      Recommendation prereqRec = findByName(result, prereq.getName());
      if (prereqRec != null && result.indexOf(prereqRec) > i) {
        result.remove(prereqRec);
        result.add(i, prereqRec);
        LOG.info("R7: {} (prerequisite of {}) moved up", prereq.getName(), work.getName());
      }
    }

    return result;
  }

  // -----------------------------------------------------------------------
  // helpers
  // -----------------------------------------------------------------------

  private static Map<String, Work> indexByName(List<Work> works) {
    Map<String, Work> map = new HashMap<>();
    for (Work w : works) {
      map.put(w.getName(), w);
    }
    return map;
  }

  private static Work findWorkById(Map<String, Work> catalog, double id) {
    for (Work w : catalog.values()) {
      if (w.getId() == id) {
        return w;
      }
    }
    return null;
  }

  private static Recommendation findByName(List<Recommendation> list, String name) {
    for (Recommendation r : list) {
      if (r.getWorkName().equals(name)) {
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
  public record AccompanimentInput(Accompaniment tipo, double cf) {}
}
