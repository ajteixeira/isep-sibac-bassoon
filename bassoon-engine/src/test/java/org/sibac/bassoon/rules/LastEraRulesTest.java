package org.sibac.bassoon.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.sibac.bassoon.cf.TrackingAgendaListener;
import org.sibac.bassoon.kb.KnowledgeBase;
import org.sibac.bassoon.model.Era;
import org.sibac.bassoon.model.Evidence;
import org.sibac.bassoon.model.EvidenceType;
import org.sibac.bassoon.model.Hypothesis;
import org.sibac.bassoon.model.Work;

/**
 * Tests the era repetition penalty rule on the test work (Telemann, Sonata in F minor,
 * which is BAROQUE). Verifies that the CF drops when the era repeats and stays unchanged
 * when the era is different.
 */
class LastEraRulesTest {

  private static final double EPS = 1e-6;

  private final KieContainer container = KieServices.Factory.get().getKieClasspathContainer();

  /**
   * Runs a consultation with a last-era evidence and returns the final candidate CF.
   *
   * @param lastEra era indicated by the teacher as the last studied
   * @param seedCf  initial CF of the Hypothesis before rules fire
   */
  private double candidateCf(Era lastEra, double seedCf) {
    KieSession session = container.newKieSession("rulesSession");
    session.addEventListener(new TrackingAgendaListener());

    Work work = KnowledgeBase.works().get(0);
    session.insert(work);

    Hypothesis candidate = new Hypothesis(Hypothesis.CANDIDATE, work.getName(), seedCf);
    session.insert(candidate);
    session.insert(new Evidence(EvidenceType.LAST_ERA, lastEra));

    session.fireAllRules();
    double cf = candidate.getCf();
    session.dispose();
    return cf;
  }

  @Test
  void sameEraPenalizesCandidate() {
    // work = BAROQUE, evidence = BAROQUE -> should drop
    double seed = 0.0;
    double cf = candidateCf(Era.BAROQUE, seed);
    assertTrue(cf < seed, "CF should drop when era repeats: " + cf + " < " + seed);
  }

  @Test
  void sameEraReducesPositiveCf() {
    // candidate with positive CF -> penalty should lower the CF
    double seed = 0.7;
    double cf = candidateCf(Era.BAROQUE, seed);
    assertTrue(cf < seed, "CF should drop from " + seed + " but was " + cf);
    assertTrue(cf > 0.0, "CF should remain positive after light penalty: " + cf);
  }

  @Test
  void sameEraDeepensNegativeCf() {
    // already penalised -> same era should worsen the CF further
    double seed = -0.2;
    double cf = candidateCf(Era.BAROQUE, seed);
    assertTrue(cf < seed, "CF should drop further below " + seed + " but was " + cf);
  }

  @Test
  void differentEraDoesNotChangeCandidate() {
    double seed = 0.5;
    double cf = candidateCf(Era.CLASSICAL, seed);
    assertEquals(seed, cf, EPS);
  }

  @Test
  void differentEraRomantic() {
    double seed = 0.35;
    double cf = candidateCf(Era.ROMANTIC, seed);
    assertEquals(seed, cf, EPS);
  }

  @Test
  void differentEraContemporary() {
    double seed = 0.8;
    double cf = candidateCf(Era.CONTEMPORARY, seed);
    assertEquals(seed, cf, EPS);
  }
}
