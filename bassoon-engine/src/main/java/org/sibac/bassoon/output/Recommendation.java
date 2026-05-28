package org.sibac.bassoon.output;

import java.util.List;

/**
 * A recommended work produced by the engine.
 *
 * <p>Built from {@code Hypothesis} candidacies after {@code fireAllRules()},
 * with combined CFs and the rules that fired.
 */
public class Recommendation {

  private final String workName;
  private final double score;
  private final String justification;
  private final List<String> firedRules;

  public Recommendation(
      String workName, double score, String justification, List<String> firedRules) {
    this.workName = workName;
    this.score = score;
    this.justification = justification;
    this.firedRules = List.copyOf(firedRules);
  }

  public String getWorkName() {
    return workName;
  }

  public double getScore() {
    return score;
  }

  public String getJustification() {
    return justification;
  }

  public List<String> getFiredRules() {
    return firedRules;
  }

  @Override
  public String toString() {
    return "Recommendation["
        + workName
        + ", score="
        + String.format("%.3f", score)
        + "]\n  "
        + justification;
  }
}
