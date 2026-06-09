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
import org.sibac.bassoon.model.SkillLevel;
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

  // -----------------------------------------------------------------------
  // fuzzy filter
  // -----------------------------------------------------------------------

  @Test
  void allWorksPassForIntermediate() {
    // intermediate+neutral (3.5) with all dif 4: intermediate+medium=high -> ~0.63
    List<Recommendation> results = engine.run(
        TestWorks.catalog(),
        StudentLevel.INTERMEDIATE, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.TRILLS, 0.9)),
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
  // skill ordering
  // -----------------------------------------------------------------------

  @Test
  void highSkillOutranksMediumSkill() {
    // TRILLS: Romantic Orch=HIGH, Classical Piano=MEDIUM
    // Use ADVANCED so all works pass fuzzy
    List<Recommendation> results = engine.run(
        TestWorks.catalog(),
        StudentLevel.ADVANCED, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.TRILLS, 0.9)),
        null, null);

    int romanticIdx = indexOf(results, TestWorks.ROMANTIC_ORCH);
    int classicalIdx = indexOf(results, TestWorks.CLASSICAL_PIANO);
    assertTrue(romanticIdx >= 0, "Romantic Orch should be present");
    assertTrue(classicalIdx >= 0, "Classical Piano should be present");
    assertTrue(romanticIdx < classicalIdx,
        "Romantic Orch (TRILLS=HIGH) should outrank Classical Piano (TRILLS=MEDIUM)");
  }

  // -----------------------------------------------------------------------
  // era penalty
  // -----------------------------------------------------------------------

  @Test
  void eraPenaltyLowersBaroqueWork() {
    // Baroque Solo and Romantic Orch both have TRILLS=HIGH — same skill score.
    // With last era = BAROQUE, Baroque Solo is penalised -> Romantic Orch should rank first.
    List<Recommendation> results = engine.run(
        TestWorks.catalog(),
        StudentLevel.ADVANCED, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.TRILLS, 0.9)),
        Era.BAROQUE, null);

    int baroqueIdx = indexOf(results, TestWorks.BAROQUE_SOLO);
    int romanticIdx = indexOf(results, TestWorks.ROMANTIC_ORCH);

    assertTrue(baroqueIdx >= 0, "Baroque Solo should still be present for advanced");
    assertTrue(romanticIdx >= 0, "Romantic Orch should be present");
    assertTrue(romanticIdx < baroqueIdx,
        "Romantic Orch should rank above Baroque Solo (era penalty applied)"
            + " but got Romantic@" + romanticIdx + ", Baroque@" + baroqueIdx);
  }

  // -----------------------------------------------------------------------
  // accompaniment match
  // -----------------------------------------------------------------------

  @Test
  void accompanimentMatchBoostsPianoWork() {
    // Classical Piano has PIANO accompaniment. With PIANO preference its score should
    // be strictly higher than without the preference.
    List<Recommendation> without = engine.run(
        TestWorks.catalog(),
        StudentLevel.ADVANCED, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.TRILLS, 0.9)),
        null, null);

    List<Recommendation> with = engine.run(
        TestWorks.catalog(),
        StudentLevel.ADVANCED, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.TRILLS, 0.9)),
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
    // Classical Piano has prerequisite = Baroque Solo
    // With LEGATO: Classical Piano=HIGH -> ranks 1st without prereq ordering
    // Prerequisite rule: Baroque Solo (prereq) is moved before Classical Piano
    List<Recommendation> results = engine.run(
        TestWorks.catalog(),
        StudentLevel.INTERMEDIATE, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.LEGATO, 0.9)),
        null, null);

    int baroqueIdx = indexOf(results, TestWorks.BAROQUE_SOLO);
    int classicalIdx = indexOf(results, TestWorks.CLASSICAL_PIANO);

    assertTrue(baroqueIdx < classicalIdx,
        "Baroque Solo (prerequisite) should come before Classical Piano (dependent)"
            + " but got Baroque@" + baroqueIdx + ", Classical@" + classicalIdx);
  }

  @Test
  void prerequisiteAlreadyBeforeDoesNotMove() {
    // Baroque Solo (prereq) is already before Classical Piano with TRILLS
    List<Recommendation> results = engine.run(
        TestWorks.catalog(),
        StudentLevel.INTERMEDIATE, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.TRILLS, 0.9)),
        null, null);

    int baroqueIdx = indexOf(results, TestWorks.BAROQUE_SOLO);
    int classicalIdx = indexOf(results, TestWorks.CLASSICAL_PIANO);

    assertTrue(baroqueIdx < classicalIdx,
        "Baroque Solo (prerequisite) should remain before Classical Piano");
  }

  // -----------------------------------------------------------------------
  // prerequisites — 3-level chain (tests the do-while fix)
  // -----------------------------------------------------------------------

  @Test
  void threeChainPrerequisitesOrdered() {
    // Chain: C requires B, B requires A.
    // Skill scores: C (LEGATO=HIGH) > B (LEGATO=MEDIUM_HIGH) > A (LEGATO=MEDIUM).
    // Without the do-while fix a single backward pass would leave B before C
    // but A still after B — incorrect. The do-while iterates until stable.
    Work a = new Work(1.0, "Work A", "Composer", Era.BAROQUE, "DE",
        Accompaniment.SOLO, DifficultyLevel.LEVEL_4, "", -1,
        Map.of(Skill.LEGATO, SkillLevel.MEDIUM));
    Work b = new Work(2.0, "Work B", "Composer", Era.CLASSICAL, "AT",
        Accompaniment.SOLO, DifficultyLevel.LEVEL_4, "", 1.0,
        Map.of(Skill.LEGATO, SkillLevel.MEDIUM_HIGH));
    Work c = new Work(3.0, "Work C", "Composer", Era.ROMANTIC, "FR",
        Accompaniment.SOLO, DifficultyLevel.LEVEL_4, "", 2.0,
        Map.of(Skill.LEGATO, SkillLevel.HIGH));

    List<Recommendation> results = engine.run(
        List.of(a, b, c),
        StudentLevel.ADVANCED, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.LEGATO, 0.9)),
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
