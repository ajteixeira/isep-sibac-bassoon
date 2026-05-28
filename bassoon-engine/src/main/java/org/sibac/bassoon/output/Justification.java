package org.sibac.bassoon.output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Structured explanation record.
 *
 * <p>Stores a conclusion, the rule that produced it, and the supporting facts.
 * The final user-facing text can be generated later by an LLM from this data.
 */
public class Justification {

  private final String ruleName;
  private final List<String> lhs;
  private final String conclusion;

  public Justification(String ruleName, List<String> lhs, String conclusion) {
    this.ruleName = ruleName;
    this.lhs = new ArrayList<>(lhs);
    this.conclusion = conclusion;
  }

  public String getRuleName() {
    return ruleName;
  }

  public List<String> getLhs() {
    return Collections.unmodifiableList(lhs);
  }

  public String getConclusion() {
    return conclusion;
  }
}
