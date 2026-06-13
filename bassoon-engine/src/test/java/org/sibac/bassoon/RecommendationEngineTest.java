package org.sibac.bassoon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sibac.bassoon.model.Accompaniment;
import org.sibac.bassoon.model.DifficultyLevel;
import org.sibac.bassoon.model.Era;
import org.sibac.bassoon.model.Motivation;
import org.sibac.bassoon.model.Skill;
import org.sibac.bassoon.model.SkillSuitability;
import org.sibac.bassoon.model.StudentLevel;
import org.sibac.bassoon.model.Work;
import org.sibac.bassoon.output.Recommendation;

/**
 * Full-pipeline integration test.
 *
 * <p>Uses stable works from {@link TestWorks} and verifies engine behaviour end to end:
 * fuzzy filter, skills, era penalty, accompaniment match, and prerequisites.
 *
 * <p>Asserts check direction and order (not exact CF values), because the fuzzy system
 * produces continuous values that depend on the jFuzzyLogic implementation.
 */
class RecommendationEngineTest {

  private static RecommendationEngine engine;

  @BeforeAll
  static void setUp() {
    engine = new RecommendationEngine();
  }

  /** Builds a dif-4 work with a single skill at the given level. */
  private static Work work(double id, String name, Era era, Skill skill, SkillSuitability level,
      double prerequisiteId) {
    return new Work(id, name, "Composer", era, "XX", Accompaniment.SOLO,
        DifficultyLevel.LEVEL_4, "", prerequisiteId, Map.of(skill, level));
  }

  // -----------------------------------------------------------------------
  // fuzzy filter
  // -----------------------------------------------------------------------

  @Test
  void allWorksPassForIntermediate() {
    // intermediate+neutral (3.5) with all dif 4: intermediate+medium=high -> ~0.63.
    // Request TRILLS+LEGATO so every work has at least one VERY_SUITABLE skill and clears MIN.
    List<Recommendation> results = engine.run(
        TestWorks.catalog(),
        StudentLevel.INTERMEDIATE, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.TRILLS, 0.9),
                new RecommendationEngine.SkillInput(Skill.LEGATO, 0.9)),
        null, null);

    assertEquals(3, results.size(),
        "All 3 works (dif 4) should pass for intermediate");
  }

  @Test
  void beginnerFiltersAllMediumWorks() {
    // beginner (1.5): all dif 4 -> beginner+medium=low -> ~0.30 -> all filtered
    List<Recommendation> results = engine.run(
        TestWorks.catalog(),
        StudentLevel.BEGINNER, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.TRILLS, 0.9)),
        null, null);

    assertEquals(0, results.size(),
        "Dif 4 works should be filtered for beginner");
  }

  // -----------------------------------------------------------------------
  // skill discrimination
  // -----------------------------------------------------------------------

  @Test
  void higherSkillSuitabilityRanksHigher() {
    // REFERENCE (@CF 0.8) must outrank VERY_SUITABLE (@CF 0.5). Both survive the threshold.
    // (Catalog works top out at VERY_SUITABLE, so purpose-built works are used here.)
    Work ref = work(101.0, "Reference Work", Era.BAROQUE, Skill.TRILLS, SkillSuitability.REFERENCE, -1);
    Work high = work(102.0, "High Work", Era.CLASSICAL, Skill.TRILLS, SkillSuitability.VERY_SUITABLE, -1);

    List<Recommendation> results = engine.run(
        List.of(ref, high),
        StudentLevel.ADVANCED, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.TRILLS, 0.9)),
        null, null);

    int refIdx = indexOf(results, "Reference Work");
    int highIdx = indexOf(results, "High Work");
    assertTrue(refIdx >= 0 && highIdx >= 0, "Both works should survive the threshold");
    assertTrue(refIdx < highIdx,
        "REFERENCE should outrank VERY_SUITABLE but got Ref@" + refIdx + ", High@" + highIdx);
  }

  @Test
  void weakSkillWorkDroppedBelowThreshold() {
    // Classical Piece is only MODERATE for TRILLS -> contribution 0.09, below MIN 0.30.
    // With the neutral CF seed it is dropped; the VERY_SUITABLE works survive.
    List<Recommendation> results = engine.run(
        TestWorks.catalog(),
        StudentLevel.ADVANCED, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.TRILLS, 0.9)),
        null, null);

    assertTrue(indexOf(results, TestWorks.CLASSICAL_PIANO) < 0,
        "MODERATE-skill work should fall below MIN and be dropped");
    assertTrue(indexOf(results, TestWorks.ROMANTIC_ORCH) >= 0,
        "VERY_SUITABLE-skill work should survive");
  }

  // -----------------------------------------------------------------------
  // era penalty
  // -----------------------------------------------------------------------

  @Test
  void eraPenaltyLowersBaroqueWork() {
    // Both works are REFERENCE for TRILLS -> same skill score. With last era = BAROQUE,
    // the Baroque work is penalised (and still survives) -> Romantic should rank first.
    Work baroque = work(201.0, "Baroque Ref", Era.BAROQUE, Skill.TRILLS, SkillSuitability.REFERENCE, -1);
    Work romantic = work(202.0, "Romantic Ref", Era.ROMANTIC, Skill.TRILLS, SkillSuitability.REFERENCE, -1);

    List<Recommendation> results = engine.run(
        List.of(baroque, romantic),
        StudentLevel.ADVANCED, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.TRILLS, 0.9)),
        Era.BAROQUE, null);

    int baroqueIdx = indexOf(results, "Baroque Ref");
    int romanticIdx = indexOf(results, "Romantic Ref");

    assertTrue(baroqueIdx >= 0, "Baroque work should still survive the penalty");
    assertTrue(romanticIdx >= 0, "Romantic work should be present");
    assertTrue(romanticIdx < baroqueIdx,
        "Romantic should rank above penalised Baroque"
            + " but got Romantic@" + romanticIdx + ", Baroque@" + baroqueIdx);
  }

  // -----------------------------------------------------------------------
  // accompaniment match
  // -----------------------------------------------------------------------

  @Test
  void accompanimentMatchBoostsPianoWork() {
    // Classical Piano has PIANO accompaniment and LEGATO=VERY_SUITABLE (survives without preference).
    // With PIANO preference its score should be strictly higher.
    List<Recommendation> without = engine.run(
        TestWorks.catalog(),
        StudentLevel.ADVANCED, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.LEGATO, 0.9)),
        null, null);

    List<Recommendation> with = engine.run(
        TestWorks.catalog(),
        StudentLevel.ADVANCED, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.LEGATO, 0.9)),
        null, List.of(new RecommendationEngine.AccompanimentInput(Accompaniment.PIANO, 0.8)));

    double scoreBefore = scoreOf(without, TestWorks.CLASSICAL_PIANO);
    double scoreAfter  = scoreOf(with,    TestWorks.CLASSICAL_PIANO);

    assertTrue(scoreBefore >= 0, "Classical Piano should be present without preference");
    assertTrue(scoreAfter  >= 0, "Classical Piano should be present with preference");
    assertTrue(scoreAfter > scoreBefore,
        "PIANO preference should raise Classical Piano score: before=" + scoreBefore
            + " after=" + scoreAfter);
  }

  // -----------------------------------------------------------------------
  // prerequisites — 2-level chain
  // -----------------------------------------------------------------------

  @Test
  void prerequisiteMovesBeforeDependentWork() {
    // Dependent (REFERENCE, higher score) would come first; the prereq rule must move
    // the lower-scored prerequisite (VERY_SUITABLE) up before it.
    Work prereq = work(301.0, "Prereq", Era.BAROQUE, Skill.LEGATO, SkillSuitability.VERY_SUITABLE, -1);
    Work dependent = work(302.0, "Dependent", Era.CLASSICAL, Skill.LEGATO, SkillSuitability.REFERENCE,
        301.0);

    List<Recommendation> results = engine.run(
        List.of(prereq, dependent),
        StudentLevel.ADVANCED, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.LEGATO, 0.9)),
        null, null);

    int prereqIdx = indexOf(results, "Prereq");
    int dependentIdx = indexOf(results, "Dependent");

    assertTrue(prereqIdx >= 0 && dependentIdx >= 0, "Both works should be present");
    assertTrue(prereqIdx < dependentIdx,
        "Prereq should be moved before Dependent"
            + " but got Prereq@" + prereqIdx + ", Dependent@" + dependentIdx);
  }

  @Test
  void prerequisiteAlreadyBeforeDoesNotMove() {
    // Prereq (REFERENCE) already outscores its dependent (VERY_SUITABLE), so it is already first.
    Work prereq = work(311.0, "Prereq2", Era.BAROQUE, Skill.LEGATO, SkillSuitability.REFERENCE, -1);
    Work dependent = work(312.0, "Dependent2", Era.CLASSICAL, Skill.LEGATO, SkillSuitability.VERY_SUITABLE,
        311.0);

    List<Recommendation> results = engine.run(
        List.of(prereq, dependent),
        StudentLevel.ADVANCED, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.LEGATO, 0.9)),
        null, null);

    int prereqIdx = indexOf(results, "Prereq2");
    int dependentIdx = indexOf(results, "Dependent2");

    assertTrue(prereqIdx < dependentIdx,
        "Prereq should remain before its dependent");
  }

  // -----------------------------------------------------------------------
  // prerequisites — 3-level chain (tests the do-while fix)
  // -----------------------------------------------------------------------

  @Test
  void threeChainPrerequisitesOrdered() {
    // Chain: C requires B, B requires A.
    // Skill scores: C (LEGATO=REFERENCE) > B (LEGATO=VERY_SUITABLE) > A (LEGATO=SUITABLE).
    // Evidence CF 1.0 keeps A at the 0.30 threshold (inclusive) so it survives.
    // Without the do-while fix a single backward pass would leave B before C
    // but A still after B — incorrect. The do-while iterates until stable.
    Work a = new Work(1.0, "Work A", "Composer", Era.BAROQUE, "DE",
        Accompaniment.SOLO, DifficultyLevel.LEVEL_4, "", -1,
        Map.of(Skill.LEGATO, SkillSuitability.SUITABLE));
    Work b = new Work(2.0, "Work B", "Composer", Era.CLASSICAL, "AT",
        Accompaniment.SOLO, DifficultyLevel.LEVEL_4, "", 1.0,
        Map.of(Skill.LEGATO, SkillSuitability.VERY_SUITABLE));
    Work c = new Work(3.0, "Work C", "Composer", Era.ROMANTIC, "FR",
        Accompaniment.SOLO, DifficultyLevel.LEVEL_4, "", 2.0,
        Map.of(Skill.LEGATO, SkillSuitability.REFERENCE));

    List<Recommendation> results = engine.run(
        List.of(a, b, c),
        StudentLevel.ADVANCED, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.LEGATO, 1.0)),
        null, null);

    int idxA = indexOf(results, "Work A");
    int idxB = indexOf(results, "Work B");
    int idxC = indexOf(results, "Work C");

    assertTrue(idxA >= 0 && idxB >= 0 && idxC >= 0, "All three works should be present");
    assertTrue(idxA < idxB,
        "Work A (prereq of B) must come before Work B but got A@" + idxA + ", B@" + idxB);
    assertTrue(idxB < idxC,
        "Work B (prereq of C) must come before Work C but got B@" + idxB + ", C@" + idxC);
  }

  // -----------------------------------------------------------------------
  // motivation
  // -----------------------------------------------------------------------

  @Test
  void highMotivationLetsMoreWorksPass() {
    // HIGH motivation pushes fuzzy level up -> more works may pass
    List<Recommendation> results = engine.run(
        TestWorks.catalog(),
        StudentLevel.INTERMEDIATE, Motivation.HIGH,
        List.of(new RecommendationEngine.SkillInput(Skill.TRILLS, 0.9)),
        null, null);

    assertFalse(results.isEmpty(), "At least one work should be recommended");
  }

  // -----------------------------------------------------------------------
  // helpers
  // -----------------------------------------------------------------------

  private static int indexOf(List<Recommendation> recommendations, String name) {
    for (int i = 0; i < recommendations.size(); i++) {
      if (recommendations.get(i).getWorkName().equals(name)) {
        return i;
      }
    }
    return -1;
  }

  private static double scoreOf(List<Recommendation> recommendations, String name) {
    for (Recommendation r : recommendations) {
      if (r.getWorkName().equals(name)) return r.getScore();
    }
    return -1;
  }
}
