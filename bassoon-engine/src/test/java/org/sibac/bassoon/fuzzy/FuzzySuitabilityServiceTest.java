package org.sibac.bassoon.fuzzy;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.sibac.bassoon.model.Motivation;

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
    double beginner = 1.5; // start of the level axis

    double easy = service.suitability(beginner, 1);
    double medium = service.suitability(beginner, 3);
    double hard = service.suitability(beginner, 6);

    assertTrue(easy > medium);
    assertTrue(medium > hard);
  }

  @Test
  void intermediatePrefersMediumWorks() {
    double intermediate = 3.5; // middle of the level axis

    double easy = service.suitability(intermediate, 1);
    double medium = service.suitability(intermediate, 3);
    double hard = service.suitability(intermediate, 6);

    assertTrue(medium > easy);
    assertTrue(easy > hard);
  }

  @Test
  void advancedPrefersHardWorks() {
    double advanced = 5.5; // end of the level axis

    double easy = service.suitability(advanced, 1);
    double medium = service.suitability(advanced, 3);
    double hard = service.suitability(advanced, 6);

    assertTrue(hard > medium);
    assertTrue(medium > easy);
  }

  @Test
  void highMotivationMakesHarderWorksMoreSuitable() {
    double neutral = StudentLevelMapper.toStudentLevel(3.5, Motivation.NEUTRAL);
    double motivated = StudentLevelMapper.toStudentLevel(3.5, Motivation.HIGH);

    assertTrue(service.suitability(motivated, 5) > service.suitability(neutral, 5));
  }
}
