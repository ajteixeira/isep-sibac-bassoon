package org.sibac.bassoon.fuzzy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.sibac.bassoon.model.Motivation;
import org.sibac.bassoon.model.StudentLevel;
import org.sibac.bassoon.output.Justification;

class FuzzySuitabilityServiceTest {

  private final FuzzySuitabilityService service = new FuzzySuitabilityService();

  @Test
  void returnsSuitabilityBetweenZeroAndOne() {
    double suitability = service.suitability(3.5, 3);

    assertTrue(suitability >= 0.0);
    assertTrue(suitability <= 1.0);
  }

  @Test
  void beginnerPrefersEasyWorks() {
    double beginner = StudentLevelMapper.toStudentLevel(StudentLevel.BEGINNER, Motivation.NEUTRAL);

    double easy = service.suitability(beginner, 1);
    double medium = service.suitability(beginner, 3);
    double hard = service.suitability(beginner, 6);

    assertTrue(easy > medium);
    assertTrue(medium > hard);
  }

  @Test
  void intermediatePrefersMediumWorks() {
    double intermediate =
        StudentLevelMapper.toStudentLevel(StudentLevel.INTERMEDIATE, Motivation.NEUTRAL);

    double easy = service.suitability(intermediate, 1);
    double medium = service.suitability(intermediate, 3);
    double hard = service.suitability(intermediate, 6);

    assertTrue(medium > easy);
    assertTrue(easy > hard);
  }

  @Test
  void advancedPrefersHardWorks() {
    double advanced = StudentLevelMapper.toStudentLevel(StudentLevel.ADVANCED, Motivation.NEUTRAL);

    double easy = service.suitability(advanced, 1);
    double medium = service.suitability(advanced, 3);
    double hard = service.suitability(advanced, 6);

    assertTrue(hard > medium);
    assertTrue(medium > easy);
  }

  @Test
  void highMotivationMakesHarderWorksMoreSuitable() {
    double neutral =
        StudentLevelMapper.toStudentLevel(StudentLevel.INTERMEDIATE, Motivation.NEUTRAL);
    double motivated =
        StudentLevelMapper.toStudentLevel(StudentLevel.INTERMEDIATE, Motivation.HIGH);

    assertTrue(service.suitability(motivated, 5) > service.suitability(neutral, 5));
  }

  @Test
  void explainReturnsSameSuitabilityAsSimpleMethod() {
    double studentLevel =
        StudentLevelMapper.toStudentLevel(StudentLevel.INTERMEDIATE, Motivation.HIGH);

    FuzzySuitabilityResult result = service.explain(studentLevel, 5);

    assertEquals(service.suitability(studentLevel, 5), result.suitability());
  }

  @Test
  void explainIncludesHowStyleJustification() {
    FuzzySuitabilityResult result = service.explain(4.5, 5);
    Justification justification = result.justification();

    assertEquals("FUZZY.suitability", justification.getRuleName());
    assertFalse(justification.getLhs().isEmpty());
    assertTrue(justification.getLhs().stream().anyMatch(fact -> fact.contains("studentLevel=4.5")));
    assertTrue(justification.getLhs().stream().anyMatch(fact -> fact.contains("workDifficulty=5")));
    assertTrue(justification.getConclusion().startsWith("suitability="));
  }

  @Test
  void explainIncludesMembershipsForInputs() {
    FuzzySuitabilityResult result = service.explain(4.5, 5);

    assertTrue(result.studentLevelMemberships().containsKey("beginner"));
    assertTrue(result.studentLevelMemberships().containsKey("intermediate"));
    assertTrue(result.studentLevelMemberships().containsKey("advanced"));
    assertTrue(result.workDifficultyMemberships().containsKey("easy"));
    assertTrue(result.workDifficultyMemberships().containsKey("medium"));
    assertTrue(result.workDifficultyMemberships().containsKey("hard"));
  }
}
