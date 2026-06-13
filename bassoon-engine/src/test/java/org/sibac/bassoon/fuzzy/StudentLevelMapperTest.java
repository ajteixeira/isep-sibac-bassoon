package org.sibac.bassoon.fuzzy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.sibac.bassoon.model.Motivation;

class StudentLevelMapperTest {

  @Test
  void neutralMotivationKeepsLevel() {
    assertEquals(1.5, StudentLevelMapper.toStudentLevel(1.5, Motivation.NEUTRAL));
    assertEquals(3.5, StudentLevelMapper.toStudentLevel(3.5, Motivation.NEUTRAL));
    assertEquals(5.5, StudentLevelMapper.toStudentLevel(5.5, Motivation.NEUTRAL));
  }

  @Test
  void nullMotivationTreatedAsNeutral() {
    assertEquals(4.2, StudentLevelMapper.toStudentLevel(4.2, null));
  }

  @Test
  void adjustsLevelWithMotivation() {
    assertEquals(4.5, StudentLevelMapper.toStudentLevel(3.5, Motivation.HIGH));
    assertEquals(2.5, StudentLevelMapper.toStudentLevel(3.5, Motivation.LOW));
  }

  @Test
  void clampsValuesToFuzzyAxis() {
    // 1.5 (beginner) + LOW (-1.0) = 0.5 (lower bound)
    assertEquals(0.5, StudentLevelMapper.toStudentLevel(1.5, Motivation.LOW));
    // 5.5 (advanced) + HIGH (+1.0) = 6.5 (upper bound)
    assertEquals(6.5, StudentLevelMapper.toStudentLevel(5.5, Motivation.HIGH));
  }
}
