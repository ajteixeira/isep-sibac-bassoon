package org.sibac.bassoon.model;

/** Work difficulty, 1 (easiest) to 6 (hardest). */
public enum DifficultyLevel {
  LEVEL_1(1),
  LEVEL_2(2),
  LEVEL_3(3),
  LEVEL_4(4),
  LEVEL_5(5),
  LEVEL_6(6);

  private final int value;

  DifficultyLevel(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
