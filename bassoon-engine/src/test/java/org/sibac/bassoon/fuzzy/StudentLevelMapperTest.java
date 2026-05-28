package org.sibac.bassoon.fuzzy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.sibac.bassoon.model.Motivation;
import org.sibac.bassoon.model.StudentLevel;

class StudentLevelMapperTest {

  @Test
  void mapsStudentLevelsToBandCenters() {
    assertEquals(1.5, StudentLevelMapper.toStudentLevel(StudentLevel.BEGINNER, Motivation.NEUTRAL));
    assertEquals(
        3.5, StudentLevelMapper.toStudentLevel(StudentLevel.INTERMEDIATE, Motivation.NEUTRAL));
    assertEquals(5.5, StudentLevelMapper.toStudentLevel(StudentLevel.ADVANCED, Motivation.NEUTRAL));
  }

  @Test
  void adjustsLevelWithMotivation() {
    assertEquals(
        4.5, StudentLevelMapper.toStudentLevel(StudentLevel.INTERMEDIATE, Motivation.HIGH));
    assertEquals(2.5, StudentLevelMapper.toStudentLevel(StudentLevel.INTERMEDIATE, Motivation.LOW));
  }

  @Test
  void clampsValuesToSafeRange() {
    // BEGINNER (1.5) + LOW (-1.0) = 0.5 (lower bound)
    assertEquals(0.5, StudentLevelMapper.toStudentLevel(StudentLevel.BEGINNER, Motivation.LOW));
    // ADVANCED (5.5) + HIGH (+1.0) = 6.5 (upper bound)
    assertEquals(6.5, StudentLevelMapper.toStudentLevel(StudentLevel.ADVANCED, Motivation.HIGH));
  }

  @Test
  void rejectsMissingStudentLevel() {
    assertThrows(
        IllegalArgumentException.class,
        () -> StudentLevelMapper.toStudentLevel(null, Motivation.NEUTRAL));
  }
}
