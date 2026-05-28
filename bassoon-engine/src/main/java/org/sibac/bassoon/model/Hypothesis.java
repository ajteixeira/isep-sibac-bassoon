package org.sibac.bassoon.model;

import org.kie.api.runtime.rule.FactHandle;
import org.sibac.bassoon.cf.Mycin;
import org.sibac.bassoon.cf.RuleFiredTracker;
import org.sibac.bassoon.cf.TrackingAgendaListener;

/**
 * An intermediate conclusion produced by the inference engine.
 *
 * <p>Work candidacies are represented as {@code Hypothesis("candidate", workName, cf)}.
 * Calling {@link #update()} propagates the CF according to the MYCIN formula,
 * using the current rule's CF and the weakest CF among LHS facts.
 */
public class Hypothesis implements CfFact {

  public static final String CANDIDATE = "candidate";
  private final String description; // what kind of conclusion (e.g. "candidate")
  private final String value; // the conclusion's value (e.g. work name)
  private double cf; // certainty of this conclusion, computed by the engine

  public Hypothesis(String description, String value, double cf) {
    this.description = description;
    this.value = value;
    this.cf = cf;
  }

  public String getDescription() {
    return description;
  }

  public String getValue() {
    return value;
  }

  public double getCf() {
    return cf;
  }

  public void setCf(double cf) {
    this.cf = cf;
  }

  /**
   * Combines another CF contribution into this hypothesis using the MYCIN formula.
   *
   * <p>Called from rule RHS (e.g. {@code $h.update()}). Reads the current rule's CF
   * and the minimum LHS CF from {@link org.sibac.bassoon.cf.TrackingAgendaListener},
   * computes the contribution, and updates the fact in working memory.
   */
  public void update() {
    FactHandle handle = TrackingAgendaListener.getKieSession().getFactHandle(this);
    double lhsMinimum = TrackingAgendaListener.getLHSminimumCF(this);
    double contribution = lhsMinimum * TrackingAgendaListener.getRuleCF();
    this.cf = Mycin.combine(this.cf, contribution);
    RuleFiredTracker.record(this, TrackingAgendaListener.getRuleName());
    TrackingAgendaListener.getKieSession().update(handle, this);
  }

  @Override
  public String toString() {
    return "Hypothesis[" + description + "=" + value + ", CF=" + cf + "]";
  }
}
