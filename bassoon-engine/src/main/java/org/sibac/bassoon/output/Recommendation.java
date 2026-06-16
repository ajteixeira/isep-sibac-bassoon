package org.sibac.bassoon.output;

import java.util.List;

/**
 * A recommended work produced by the engine.
 *
 * <p>Built from {@code Hypothesis} candidacies after {@code fireAllRules()},
 * with combined CFs and the rules that fired.
 */
public class Recommendation {

  private final double workId;
  private final String workName;
  private final double score;
  private final double initialScore;
  private final List<FiredRule> firedRules;

  public Recommendation(
      double workId, String workName, double score, double initialScore,
      List<FiredRule> firedRules) {
    this.workId = workId;
    this.workName = workName;
    this.score = score;
    this.initialScore = initialScore;
    this.firedRules = List.copyOf(firedRules);
  }

  public double getWorkId() {
    return workId;
  }

  public String getWorkName() {
    return workName;
  }

  public double getScore() {
    return score;
  }

  public double getInitialScore() {
    return initialScore;
  }

  public List<FiredRule> getFiredRules() {
    return firedRules;
  }

  @Override
  public String toString() {
    return "Recommendation[" + workName + ", score=" + String.format("%.3f", score) + "]";
  }
}
