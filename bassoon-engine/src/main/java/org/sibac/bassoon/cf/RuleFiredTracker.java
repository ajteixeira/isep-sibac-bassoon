package org.sibac.bassoon.cf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.sibac.bassoon.output.FiredRule;

/**
 * Tracks which Drools rules fired for each Hypothesis.
 *
 * <p>Called from {@link org.sibac.bassoon.model.Hypothesis#update()}. Results are
 * collected after {@code fireAllRules()} and cleared between requests.
 *
 * <p>State is static (one request at a time), same pattern as
 * {@link TrackingAgendaListener}.
 */
public final class RuleFiredTracker {

  // hypothesis value (work name) -> list of fired rules with context
  private static final Map<String, List<FiredRule>> registry = new ConcurrentHashMap<>();

  private RuleFiredTracker() {}

  /**
   * Records a rule firing for a hypothesis, with the context extracted from the LHS
   * (which skill, accompaniment, or era triggered the match).
   */
  public static void record(
      org.sibac.bassoon.model.Hypothesis hypothesis,
      String ruleName,
      double ruleCf,
      String detail) {
    String category = deriveCategory(ruleName);
    registry
        .computeIfAbsent(hypothesis.getValue(), k -> new ArrayList<>())
        .add(new FiredRule(ruleName, ruleCf, category, detail));
  }

  /** Returns the rules that fired for a hypothesis (or an empty list). */
  public static List<FiredRule> getRules(
      org.sibac.bassoon.model.Hypothesis hypothesis) {
    return registry.getOrDefault(
        hypothesis.getValue(), Collections.emptyList());
  }

  /** Clears the registry between requests. */
  public static void clear() {
    registry.clear();
  }

  private static String deriveCategory(String ruleName) {
    if (ruleName == null) {
      return "UNKNOWN";
    }
    if (ruleName.startsWith("skill")) {
      return "SKILL";
    }
    if (ruleName.equals("accompaniment match")) {
      return "ACCOMPANIMENT";
    }
    if (ruleName.equals("era penalty")) {
      return "ERA";
    }
    return "OTHER";
  }
}
