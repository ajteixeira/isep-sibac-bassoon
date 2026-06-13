package org.sibac.bassoon.fuzzy;

import java.io.InputStream;
import net.sourceforge.jFuzzyLogic.FIS;

/**
 * Runs the fuzzy logic system defined in {@code suitability.fcl}.
 *
 * <p>Takes a student level (already adjusted by motivation, see
 * {@link StudentLevelMapper}) and a work difficulty, and returns a suitability
 * value between 0 and 1. That value is the entry gate for the work (threshold in
 * {@link org.sibac.bassoon.RecommendationEngine}); the candidacy CF itself starts
 * neutral at 0.0 and is built by the rules.
 *
 * <p>The FCL is loaded once in the constructor. Evaluation is {@code synchronized}
 * because the FIS object holds state between {@code setVariable} and {@code evaluate}.
 */
public class FuzzySuitabilityService {

  private static final String FCL_RESOURCE = "/fuzzy/suitability.fcl";
  private static final String IN_STUDENT_LEVEL = "studentLevel";
  private static final String IN_WORK_DIFFICULTY = "workDifficulty";
  private static final String OUT_SUITABILITY = "suitability";

  private final FIS fis;

  public FuzzySuitabilityService() {
    try (InputStream in = getClass().getResourceAsStream(FCL_RESOURCE)) {
      if (in == null) {
        throw new IllegalStateException(
            "Resource " + FCL_RESOURCE + " was not found on the classpath");
      }
      this.fis = FIS.load(in, true);
      if (this.fis == null) {
        throw new IllegalStateException("Failed to parse FCL resource " + FCL_RESOURCE);
      }
    } catch (Exception e) {
      throw new IllegalStateException("Failed to load fuzzy suitability system", e);
    }
  }

  /**
   * Returns the suitability [0..1] of a work for a student.
   *
   * @param studentLevel student level on the 1..6 axis (see {@link StudentLevelMapper})
   * @param workDifficulty work difficulty (1..6)
   */
  public synchronized double suitability(double studentLevel, int workDifficulty) {
    fis.setVariable(IN_STUDENT_LEVEL, studentLevel);
    fis.setVariable(IN_WORK_DIFFICULTY, workDifficulty);
    fis.evaluate();
    return fis.getVariable(OUT_SUITABILITY).defuzzify();
  }
}
