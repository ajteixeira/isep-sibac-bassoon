package org.sibac.bassoon.rules;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.sibac.bassoon.cf.TrackingAgendaListener;
import org.sibac.bassoon.TestWorks;
import org.sibac.bassoon.model.Accompaniment;
import org.sibac.bassoon.model.DifficultyLevel;
import org.sibac.bassoon.model.Era;
import org.sibac.bassoon.model.Evidence;
import org.sibac.bassoon.model.EvidenceType;
import org.sibac.bassoon.model.Hypothesis;
import org.sibac.bassoon.model.Skill;
import org.sibac.bassoon.model.SkillSuitability;
import org.sibac.bassoon.model.Work;

/**
 * Tests the skill rules on the example work (Telemann, Sonata in F minor).
 * Verifies the direction of each SkillSuitability level's effect on the candidate CF.
 *
 * <p>Suitability on the test work: TRILLS=VERY_SUITABLE, LEGATO=MODERATE,
 * DYNAMICS=TOTALLY_UNSUITABLE, STACCATO=MODERATE. REFERENCE is not tested because the
 * current work has no skill at that level.
 */
class SkillRulesTest {

    private final KieContainer container = KieServices.Factory.get().getKieClasspathContainer();

  /** Runs a consultation with a single priority skill and returns the final candidate CF. */
  private double candidateCf(Work work, Skill skill, double evidenceCf, double seedCf) {
    KieSession session = container.newKieSession("rulesSession");
    session.addEventListener(new TrackingAgendaListener());

    session.insert(work);

    Hypothesis candidate = new Hypothesis(Hypothesis.CANDIDATE, work.getName(), seedCf);
    session.insert(candidate);
    session.insert(new Evidence(EvidenceType.SKILL_1, skill, evidenceCf));

    session.fireAllRules();
    double cf = candidate.getCf();
    session.dispose();
    return cf;
  }

  @Test
  void verySuitableRaisesCandidate() {
    // TRILLS is VERY_SUITABLE on the work (positive) -> should rise
    double seed = 0.0;
    double cf = candidateCf(TestWorks.catalog().get(0), Skill.TRILLS, 0.9, seed);
    assertTrue(cf > seed, "VERY_SUITABLE should raise CF above " + seed + " but was " + cf);
  }

  @Test
  void moderateRaisesCandidateLess() {
    // LEGATO is MODERATE (@CF 0.1) -> rises, but less than VERY_SUITABLE
    double seed = 0.0;
    double cf = candidateCf(TestWorks.catalog().get(0), Skill.LEGATO, 0.9, seed);
    assertTrue(cf > seed, "MODERATE should raise CF above " + seed + " but was " + cf);
  }

  @Test
  void verySuitableBeatsModerate() {
    // VERY_SUITABLE (@CF 0.5) should contribute more than MODERATE (@CF 0.1)
    double cfVerySuitable = candidateCf(TestWorks.catalog().get(0), Skill.TRILLS, 0.9, 0.0);
    double cfModerate = candidateCf(TestWorks.catalog().get(0), Skill.LEGATO, 0.9, 0.0);
    assertTrue(cfVerySuitable > cfModerate,
        "VERY_SUITABLE (" + cfVerySuitable + ") should outrank MODERATE (" + cfModerate + ")");
  }

  @Test
  void totallyUnsuitablePenalizesCandidate() {
    // DYNAMICS is TOTALLY_UNSUITABLE (negative) -> should drop
    double seed = 0.0;
    double cf = candidateCf(TestWorks.catalog().get(2), Skill.DYNAMICS, 0.9, seed);
    assertTrue(cf < seed, "TOTALLY_UNSUITABLE should drop CF below " + seed + " but was " + cf);
  }

  @Test
  void moderateChangesCandidateSlightly() {
    // STACCATO is MODERATE (positive) -> small contribution
    double seed = 0.5;
    double cf = candidateCf(TestWorks.catalog().get(0), Skill.STACCATO, 0.9, seed);
    assertTrue(cf > seed, "MODERATE should raise CF slightly above " + seed + " but was " + cf);
    assertTrue(cf < seed + 0.1, "MODERATE contribution should be small");
  }

  @Test
  void totallyUnsuitablePenalizesEvenWithPositiveSeed() {
    // candidate already positive -> TOTALLY_UNSUITABLE should reduce the CF
    double seed = 0.6;
    double cf = candidateCf(TestWorks.catalog().get(2), Skill.DYNAMICS, 0.9, seed);
    assertTrue(cf < seed,
        "TOTALLY_UNSUITABLE should reduce CF from " + seed + " but was " + cf);
  }

  @Test
  void verySuitableOnTopOfExistingCf() {
    // VERY_SUITABLE with positive seed -> CF should rise further
    double seed = 0.4;
    double cf = candidateCf(TestWorks.catalog().get(0), Skill.TRILLS, 0.9, seed);
    assertTrue(cf > seed,
        "VERY_SUITABLE should raise CF above seed " + seed + " but was " + cf);
  }

  @Test
  void referenceRaisesMoreThanVerySuitable() {
    // REFERENCE (@CF 0.8) contributes more than VERY_SUITABLE (@CF 0.5).
    // Classical Piano has LEGATO=VERY_SUITABLE; the reference work has LEGATO=REFERENCE.
    Work referenceWork = new Work(99.0, "Reference Work", "Composer",
        Era.BAROQUE, "DE", Accompaniment.SOLO, DifficultyLevel.LEVEL_4, "", -1,
        Map.of(Skill.LEGATO, SkillSuitability.REFERENCE));

    double cfRef = candidateCf(referenceWork, Skill.LEGATO, 0.9, 0.0);
    double cfVerySuitable = candidateCf(TestWorks.catalog().get(1), Skill.LEGATO, 0.9, 0.0);

    assertTrue(cfRef > cfVerySuitable,
        "REFERENCE (" + cfRef + ") should raise CF more than VERY_SUITABLE (" + cfVerySuitable + ")");
  }
}
