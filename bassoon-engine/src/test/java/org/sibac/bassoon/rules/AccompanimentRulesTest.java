package org.sibac.bassoon.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.sibac.bassoon.cf.TrackingAgendaListener;
import org.sibac.bassoon.kb.KnowledgeBase;
import org.sibac.bassoon.model.Accompaniment;
import org.sibac.bassoon.model.Evidence;
import org.sibac.bassoon.model.EvidenceType;
import org.sibac.bassoon.model.Hypothesis;
import org.sibac.bassoon.model.Work;

/**
 * Tests the accompaniment preference rule on the test work (Telemann, Sonata in F minor,
 * which has BASSO_CONTINUO). Verifies that the CF rises when the accompaniment matches
 * the teacher's preference and stays unchanged otherwise.
 */
class AccompanimentRulesTest {

  private static final double TOLERANCE = 1e-6;

  private final KieContainer container = KieServices.Factory.get().getKieClasspathContainer();

  /**
   * Runs a consultation with an accompaniment preference and returns the final candidate CF.
   *
   * @param preferred   accompaniment type requested by the teacher
   * @param evidenceCf  teacher's certainty in that preference [0..1]
   * @param seedCf      initial CF of the Hypothesis before rules fire
   */
  private double candidateCf(Accompaniment preferred, double evidenceCf, double seedCf) {
    KieSession session = container.newKieSession("rulesSession");
    session.addEventListener(new TrackingAgendaListener());

    Work work = KnowledgeBase.works().get(0);
    session.insert(work);

    Hypothesis candidate = new Hypothesis(Hypothesis.CANDIDATE, work.getName(), seedCf);
    session.insert(candidate);
    session.insert(new Evidence(EvidenceType.PREFERRED_ACCOMPANIMENT, preferred, evidenceCf));

    session.fireAllRules();
    double cf = candidate.getCf();
    session.dispose();
    return cf;
  }

  @Test
  void matchingAccompanimentRaisesCandidate() {
    // work = BASSO_CONTINUO, evidence = BASSO_CONTINUO -> should rise
    double seed = 0.4;
    double cf = candidateCf(Accompaniment.BASSO_CONTINUO, 0.9, seed);
    assertTrue(cf > seed, "CF should rise when accompaniment matches: " + cf + " > " + seed);
  }

  @Test
  void matchingAccompanimentFromZero() {
    // even with zero initial CF, the match should push it positive
    double cf = candidateCf(Accompaniment.BASSO_CONTINUO, 0.9, 0.0);
    assertTrue(cf > 0.0, "CF should be positive when accompaniment matches: " + cf);
  }

  @Test
  void nonMatchingAccompanimentDoesNotChangeCandidate() {
    // work = BASSO_CONTINUO, evidence = PIANO -> should not change
    double seed = 0.5;
    double cf = candidateCf(Accompaniment.PIANO, 0.9, seed);
    assertEquals(seed, cf, TOLERANCE);
  }

  @Test
  void matchingWithWeakerEvidenceStillRaises() {
    // teacher less certain of the preference, but it should still raise the CF
    double seed = 0.0;
    double cf = candidateCf(Accompaniment.BASSO_CONTINUO, 0.3, seed);
    assertTrue(cf > 0.0, "Even weak preference should raise CF above zero: " + cf);
  }

  @Test
  void nonMatchingSolo() {
    double seed = 0.35;
    double cf = candidateCf(Accompaniment.SOLO, 0.9, seed);
    assertEquals(seed, cf, TOLERANCE);
  }

  @Test
  void nonMatchingOrchestra() {
    double seed = 0.42;
    double cf = candidateCf(Accompaniment.ORCHESTRA, 0.7, seed);
    assertEquals(seed, cf, TOLERANCE);
  }
}
