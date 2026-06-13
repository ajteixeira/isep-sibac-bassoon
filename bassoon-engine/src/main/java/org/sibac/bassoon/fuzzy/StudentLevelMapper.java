package org.sibac.bassoon.fuzzy;

import org.sibac.bassoon.model.Motivation;

/**
 * Adjusts the teacher's continuous student-level input by motivation, producing the
 * value fed to the fuzzy system ({@code suitability.fcl}).
 *
 * <p>The level runs on a 1.5–5.5 axis: 1.5 = beginner, 3.5 = intermediate, 5.5 = advanced.
 * The teacher sets it on a labelled slider, so any point in between is valid (e.g. 4.3 is
 * "between intermediate and advanced"). Motivation then shifts it:
 * <ul>
 *   <li>HIGH — +1.0 (student can handle more challenging works)
 *   <li>LOW  — −1.0 (student needs more accessible works)
 *   <li>NEUTRAL — no shift
 * </ul>
 *
 * <p>With the slider range [1.5, 5.5] and a ±1.0 shift, the result lands in [0.5, 6.5] —
 * exactly the fuzzy axis (the beginner/advanced membership plateaus reserve [0.5, 1.5] and
 * [5.5, 6.5] as the motivation headroom). The clamp is a defensive guard.
 */
public final class StudentLevelMapper {

  // shift amount (one difficulty level)
  private static final double MOTIVATION_SHIFT = 1.0;

  // safety bounds (the fuzzy axis)
  private static final double MIN_LEVEL = 0.5;
  private static final double MAX_LEVEL = 6.5;

  private StudentLevelMapper() {} // utility class

  /**
   * Adjusts the continuous student level by motivation.
   *
   * @param level continuous student level on the 1.5–5.5 axis
   * @param motivation motivation (null treated as NEUTRAL)
   * @return value for the fuzzy system, clamped to [0.5, 6.5]
   */
  public static double toStudentLevel(double level, Motivation motivation) {
    return clamp(level + motivationShift(motivation));
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
