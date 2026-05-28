package org.sibac.bassoon.cf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

  // hypothesis value (work name) -> list of rules that fired
  private static final Map<String, List<String>> registry = new ConcurrentHashMap<>();

  private RuleFiredTracker() {}

  /** Records that a rule fired for a hypothesis. Called from {@code Hypothesis.update()}. */
  public static void record(
      org.sibac.bassoon.model.Hypothesis hypothesis, String ruleName) {
    registry
        .computeIfAbsent(hypothesis.getValue(), k -> new ArrayList<>())
        .add(ruleName);
  }

  /** Returns the rules that fired for a hypothesis (or an empty list). */
  public static List<String> getRules(
      org.sibac.bassoon.model.Hypothesis hypothesis) {
    return registry.getOrDefault(
        hypothesis.getValue(), Collections.emptyList());
  }

  /** Clears the registry between requests. */
  public static void clear() {
    registry.clear();
  }
}
