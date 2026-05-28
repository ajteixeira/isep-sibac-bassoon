package org.sibac.bassoon.fuzzy;

import org.sibac.bassoon.model.Motivation;
import org.sibac.bassoon.model.StudentLevel;

/**
 * Maps the student level (categorical) and motivation to a continuous value on the
 * 1–6 axis used by the fuzzy system ({@code suitability.fcl}).
 *
 * <p>Motivation shifts the level:
 * <ul>
 *   <li>HIGH — +1.0 (student can handle more challenging works)
 *   <li>LOW  — −1.0 (student needs more accessible works)
 *   <li>NEUTRAL — no shift
 * </ul>
 * The result is clamped to [0.5, 6.5].
 */
public final class StudentLevelMapper {

  // base values (centre of each band)
  private static final double BEGINNER_BASE = 1.5; // band 1-2
  private static final double INTERMEDIATE_BASE = 3.5; // band 3-4
  private static final double ADVANCED_BASE = 5.5; // band 5-6

  // shift amount (one difficulty level)
  private static final double MOTIVATION_SHIFT = 1.0;

  // safety bounds
  private static final double MIN_LEVEL = 0.5;
  private static final double MAX_LEVEL = 6.5;

  private StudentLevelMapper() {} // utility class

  /**
   * Converts level + motivation to a continuous value on the 1–6 axis.
   *
   * @param level student level (required)
   * @param motivation motivation (null treated as NEUTRAL)
   * @return continuous value for the fuzzy system
   */
  public static double toStudentLevel(StudentLevel level, Motivation motivation) {
    if (level == null) {
      throw new IllegalArgumentException("Student level is required");
    }

    double value = baseValue(level);
    value += motivationShift(motivation);

    return clamp(value);
  }

  private static double baseValue(StudentLevel level) {
      return switch (level) {
          case BEGINNER -> BEGINNER_BASE;
          case INTERMEDIATE -> INTERMEDIATE_BASE;
          case ADVANCED -> ADVANCED_BASE;
      };
  }

  private static double motivationShift(Motivation motivation) {
    if (motivation == null) {
      return 0.0;
    }
      return switch (motivation) {
          case HIGH -> +MOTIVATION_SHIFT;
          case LOW -> -MOTIVATION_SHIFT;
          default -> 0.0;
      };
  }

  private static double clamp(double value) {
    return Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, value));
  }
}
