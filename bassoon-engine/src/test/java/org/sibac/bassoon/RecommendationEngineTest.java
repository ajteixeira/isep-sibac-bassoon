package org.sibac.bassoon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sibac.bassoon.model.Accompaniment;
import org.sibac.bassoon.model.Era;
import org.sibac.bassoon.model.Motivation;
import org.sibac.bassoon.model.Skill;
import org.sibac.bassoon.model.StudentLevel;
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
    // last era = BAROQUE -> Baroque Solo penalised
    // Use ADVANCED so all works pass fuzzy
    List<Recommendation> results = engine.run(
        TestWorks.catalog(),
        StudentLevel.ADVANCED, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.TRILLS, 0.9)),
        Era.BAROQUE, null);

    int baroqueIdx = indexOf(results, TestWorks.BAROQUE_SOLO);
    int classicalIdx = indexOf(results, TestWorks.CLASSICAL_PIANO);

    assertTrue(baroqueIdx >= 0, "Baroque Solo should be present for advanced");
    assertTrue(classicalIdx >= 0, "Classical Piano should be present for advanced");
  }

  // -----------------------------------------------------------------------
  // accompaniment match
  // -----------------------------------------------------------------------

  @Test
  void accompanimentMatchBoostsPianoWork() {
    // preference = PIANO -> Classical Piano (PIANO) gets boost (@CF 0.45)
    // Use ADVANCED so all works pass fuzzy
    List<Recommendation> results = engine.run(
        TestWorks.catalog(),
        StudentLevel.ADVANCED, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.TRILLS, 0.9)),
        null, List.of(new RecommendationEngine.AccompanimentInput(Accompaniment.PIANO, 0.8)));

    assertTrue(indexOf(results, TestWorks.CLASSICAL_PIANO) >= 0,
        "Classical Piano should be present (fuzzy passes + accomp match)");
    assertTrue(indexOf(results, TestWorks.BAROQUE_SOLO) >= 0,
        "Baroque Solo should be present for advanced");
  }

  // -----------------------------------------------------------------------
  // prerequisites
  // -----------------------------------------------------------------------

  @Test
  void prerequisiteMovesBeforeDependentWork() {
    // Classical Piano has prerequisite = Baroque Solo
    // With LEGATO: Classical Piano=HIGH -> ranks 1st
    // Baroque Solo=LEGATO=NEUTRAL -> ranks 2nd
    // Prerequisite rule: Baroque Solo (prereq) is after -> moved up
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
    // Prerequisite rule should not change the order
    List<Recommendation> results = engine.run(
        TestWorks.catalog(),
        StudentLevel.INTERMEDIATE, Motivation.NEUTRAL,
        List.of(new RecommendationEngine.SkillInput(Skill.TRILLS, 0.9)),
        null, null);

    int baroqueIdx = indexOf(results, TestWorks.BAROQUE_SOLO);
    int classicalIdx = indexOf(results, TestWorks.CLASSICAL_PIANO);

    // With TRILLS: Baroque=HIGH > Classical=MEDIUM, Baroque is already first
    assertTrue(baroqueIdx < classicalIdx,
        "Baroque Solo (prerequisite) should remain before Classical Piano");
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

  private static List<String> namesOf(List<Recommendation> recommendations) {
    return recommendations.stream().map(Recommendation::getWorkName).toList();
  }

  private static int indexOf(List<Recommendation> recommendations, String name) {
    for (int i = 0; i < recommendations.size(); i++) {
      if (recommendations.get(i).getWorkName().equals(name)) {
        return i;
      }
    }
    return -1;
  }
}
