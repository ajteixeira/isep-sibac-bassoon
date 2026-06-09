package org.sibac.bassoon.model;

/**
 * Contract for facts that carry a certainty factor (CF).
 *
 * <p>The CF engine ({@link org.sibac.bassoon.cf.TrackingAgendaListener}) needs to
 * inspect all facts that matched a rule's LHS and find the weakest CF. Instead of
 * storing values as strings, this minimal interface lets {@code Evidence} and
 * {@code Hypothesis} keep their own typed fields.
 *
 * <p>Certain facts (e.g. {@code Work}, which is catalog data) do NOT implement this
 * interface, so the listener ignores them when computing the minimum.
 */
public interface CfFact {

  /** Certainty factor of this fact, in [-1, +1]. */
  double getCf();
}
