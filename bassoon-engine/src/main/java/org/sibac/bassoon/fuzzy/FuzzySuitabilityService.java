package org.sibac.bassoon.fuzzy;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import org.sibac.bassoon.output.Justification;

/**
 * Runs the fuzzy logic system defined in {@code suitability.fcl}.
 *
 * <p>Takes a student level (already adjusted by motivation, see
 * {@link StudentLevelMapper}) and a work difficulty, and returns a suitability
 * value between 0 and 1. That value becomes the initial CF of the work's candidacy.
 *
 * <p>The FCL is loaded once in the constructor. Evaluation is {@code synchronized}
 * because the FIS object holds state between {@code setVariable} and {@code evaluate}.
 */
public class FuzzySuitabilityService {

  private static final String JUSTIFICATION_RULE = "FUZZY.suitability";

  // path to the .fcl on the classpath
  private static final String FCL_RESOURCE = "/fuzzy/suitability.fcl";

  // variable names as declared in the .fcl
  private static final String IN_STUDENT_LEVEL = "studentLevel";
  private static final String IN_WORK_DIFFICULTY = "workDifficulty";
  private static final String OUT_SUITABILITY = "suitability";

  private final FIS fis;

  public FuzzySuitabilityService() {
    // load the fuzzy system from the classpath
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
    return explain(studentLevel, workDifficulty).suitability();
  }

  /**
   * Same as {@link #suitability}, but also returns the membership values used
   * for the result, following the "how" pattern for justifications.
   */
  public synchronized FuzzySuitabilityResult explain(double studentLevel, int workDifficulty) {
    fis.setVariable(IN_STUDENT_LEVEL, studentLevel);
    fis.setVariable(IN_WORK_DIFFICULTY, workDifficulty);
    fis.evaluate();

    double suitability = fis.getVariable(OUT_SUITABILITY).defuzzify();
    Map<String, Double> studentMemberships =
        memberships(fis.getVariable(IN_STUDENT_LEVEL), "beginner", "intermediate", "advanced");
    Map<String, Double> difficultyMemberships =
        memberships(fis.getVariable(IN_WORK_DIFFICULTY), "easy", "medium", "hard");

    Justification justification =
        new Justification(
            JUSTIFICATION_RULE,
            List.of(
                "studentLevel=" + studentLevel,
                "workDifficulty=" + workDifficulty,
                "studentLevelMemberships=" + studentMemberships,
                "workDifficultyMemberships=" + difficultyMemberships),
            "suitability=" + suitability);

    return new FuzzySuitabilityResult(
        studentLevel,
        workDifficulty,
        suitability,
        studentMemberships,
        difficultyMemberships,
        justification);
  }

  private Map<String, Double> memberships(Variable variable, String... terms) {
    Map<String, Double> result = new LinkedHashMap<>();
    for (String term : terms) {
      result.put(term, variable.getMembership(term));
    }
    return result;
  }
}
