package org.sibac.bassoon.cf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.AgendaGroupPoppedEvent;
import org.kie.api.event.rule.AgendaGroupPushedEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;
import org.kie.api.event.rule.RuleFlowGroupActivatedEvent;
import org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieSession;
import org.sibac.bassoon.model.CfFact;
import org.sibac.bassoon.model.Hypothesis;

/**
 * Certainty factor engine — intercepts rule firings to propagate CFs.
 *
 * <p>Before each rule's RHS runs, the current LHS facts, rule CF ({@code @CF}), and
 * rule name are captured. The RHS calls {@code Hypothesis.update()}, which queries
 * this listener for the minimum LHS CF and the rule CF to compute the contribution.
 *
 * <p>Facts without a CF (e.g. {@code Work}) are skipped when computing the minimum.
 *
 * <p>State is static (single-request safe). The API creates a fresh {@code KieSession}
 * per request and calls {@code dispose()} afterwards.
 */
public class TrackingAgendaListener implements AgendaEventListener {

  private static KieSession kieSession;
  private static List<Object> activations;
  private static String ruleName;
  private static double ruleCF;

  public TrackingAgendaListener() {
    super();
    TrackingAgendaListener.kieSession = null;
    TrackingAgendaListener.activations = new ArrayList<>();
    TrackingAgendaListener.ruleName = null;
    TrackingAgendaListener.ruleCF = 0;
  }

  public static KieSession getKieSession() {
    return kieSession;
  }

  public static List<Object> getActivations() {
    return activations;
  }

  public static double getRuleCF() {
    return ruleCF;
  }

  public static String getRuleName() {
    return ruleName;
  }

  /**
   * Finds the Hypothesis with the given description and value in working memory.
   * Used by rule RHS to obtain the candidacy for a given work name.
   */
  public static Hypothesis getFactRef(Class<?> c, String description, String value) {
    Collection<?> facts = kieSession.getObjects(new ClassObjectFilter(c));
    for (Object fact : facts) {
      if (fact instanceof Hypothesis hypothesis
          && hypothesis.getDescription().equals(description)
          && hypothesis.getValue().equals(value)) {
        return hypothesis;
      }
    }
    return null;
  }

  /**
   * Returns the minimum CF among the LHS facts that carry a CF (weakest-link rule).
   * Ignores the conclusion itself and facts without CF (e.g. {@code Work}).
   * Returns 1.0 if no CF-carrying facts are present (deterministic rule).
   */
  public static double getLHSminimumCF(Object conclusion) {
    activations.remove(conclusion);
    double minimum = 1.0;
    for (Object fact : activations) {
      if (fact instanceof CfFact cfFact) {
        minimum = Math.min(minimum, cfFact.getCf());
      }
    }
    return minimum;
  }

  @Override
  public void beforeMatchFired(BeforeMatchFiredEvent event) {
    TrackingAgendaListener.kieSession =
        (KieSession) event.getKieRuntime().getKieBase().getKieSessions().toArray()[0];
    TrackingAgendaListener.activations.clear();
    TrackingAgendaListener.activations.addAll(event.getMatch().getObjects());
    Map<String, Object> metaData = event.getMatch().getRule().getMetaData();
    Object cf = metaData.get("CF");
    TrackingAgendaListener.ruleCF = (cf instanceof Double value) ? value : 1.0; // no @CF defaults to 1.0
    TrackingAgendaListener.ruleName = event.getMatch().getRule().getName();
  }

  @Override
  public void afterMatchFired(AfterMatchFiredEvent event) {
    TrackingAgendaListener.activations.clear();
  }

  // remaining interface methods are unused in this engine

  @Override
  public void matchCreated(MatchCreatedEvent event) {}

  @Override
  public void matchCancelled(MatchCancelledEvent event) {}

  @Override
  public void agendaGroupPopped(AgendaGroupPoppedEvent event) {}

  @Override
  public void agendaGroupPushed(AgendaGroupPushedEvent event) {}

  @Override
  public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {}

  @Override
  public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {}

  @Override
  public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {}

  @Override
  public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {}
}
