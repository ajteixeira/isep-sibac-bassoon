package org.sibac.bassoon.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.sibac.bassoon.cf.TrackingAgendaListener;
import org.sibac.bassoon.kb.KnowledgeBase;
import org.sibac.bassoon.model.Evidence;
import org.sibac.bassoon.model.EvidenceType;
import org.sibac.bassoon.model.Hypothesis;
import org.sibac.bassoon.model.Skill;
import org.sibac.bassoon.model.Work;

/**
 * Tests the skill rules on the example work (Telemann, Sonata in F minor).
 * Verifies the direction of each SkillLevel's effect on the candidate CF.
 *
 * <p>Skill levels on the test work: TRILLS=HIGH, LEGATO=MEDIUM, DYNAMICS=NONE,
 * STACCATO=LOW. REFERENCE is not tested because the current work has no skill at that level.
 */
class SkillRulesTest {

  private static final double TOLERANCE = 1e-6;

  private final KieContainer container = KieServices.Factory.get().getKieClasspathContainer();

  /** Runs a consultation with a single priority skill and returns the final candidate CF. */
  private double candidateCf(Skill skill, double evidenceCf, double seedCf) {
    KieSession session = container.newKieSession("rulesSession");
    session.addEventListener(new TrackingAgendaListener());

    Work work = KnowledgeBase.works().get(0);
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
  void highLevelRaisesCandidate() {
    // TRILLS is HIGH on the work (@CF 0.7) -> should rise
    double seed = 0.0;
    double cf = candidateCf(Skill.TRILLS, 0.9, seed);
    assertTrue(cf > seed, "HIGH skill should raise CF above " + seed + " but was " + cf);
  }

  @Test
  void mediumLevelRaisesCandidateLess() {
    // LEGATO is MEDIUM (@CF 0.3) -> rises, but less than HIGH
    double seed = 0.0;
    double cf = candidateCf(Skill.LEGATO, 0.9, seed);
    assertTrue(cf > seed, "MEDIUM skill should raise CF above " + seed + " but was " + cf);
  }

  @Test
  void highBeatsMedium() {
    // HIGH (@CF 0.7) should contribute more than MEDIUM (@CF 0.3)
    double cfHigh = candidateCf(Skill.TRILLS, 0.9, 0.0);
    double cfMedium = candidateCf(Skill.LEGATO, 0.9, 0.0);
    assertTrue(cfHigh > cfMedium,
        "HIGH (" + cfHigh + ") should outrank MEDIUM (" + cfMedium + ")");
  }

  @Test
  void noneLevelPenalizesCandidate() {
    // DYNAMICS is NONE (@CF -0.5) -> should drop
    double seed = 0.0;
    double cf = candidateCf(Skill.DYNAMICS, 0.9, seed);
    assertTrue(cf < seed, "NONE skill should drop CF below " + seed + " but was " + cf);
  }

  @Test
  void lowLevelDoesNotChangeCandidate() {
    // STACCATO is LOW -> no rule, CF stays unchanged
    double seed = 0.5;
    double cf = candidateCf(Skill.STACCATO, 0.9, seed);
    assertEquals(seed, cf, TOLERANCE);
  }

  @Test
  void nonePenalizesEvenWithPositiveSeed() {
    // candidate already positive -> NONE should reduce the CF
    double seed = 0.6;
    double cf = candidateCf(Skill.DYNAMICS, 0.9, seed);
    assertTrue(cf < seed,
        "NONE should reduce CF from " + seed + " but was " + cf);
  }

  @Test
  void highOnTopOfExistingCf() {
    // HIGH with positive seed -> CF should rise further
    double seed = 0.4;
    double cf = candidateCf(Skill.TRILLS, 0.9, seed);
    assertTrue(cf > seed,
        "HIGH should raise CF above seed " + seed + " but was " + cf);
  }
}
