package org.sibac.bassoon.output;

/**
 * A Drools rule that fired for a work candidacy, enriched with context.
 *
 * <p>Carries the rule name, its {@code @CF} value, and the specific domain
 * object that triggered the match (e.g. which skill, accompaniment, or era).
 */
public class FiredRule {

  private final String name;
  private final double cf;
  private final String category;
  private final String detail;

  public FiredRule(String name, double cf, String category, String detail) {
    this.name = name;
    this.cf = cf;
    this.category = category;
    this.detail = detail;
  }

  public String getName() {
    return name;
  }

  public double getCf() {
    return cf;
  }

  public String getCategory() {
    return category;
  }

  public String getDetail() {
    return detail;
  }

  @Override
  public String toString() {
    return "FiredRule[" + name + " cf=" + String.format("%+.2f", cf)
        + " " + category + (detail != null ? ":" + detail : "") + "]";
  }
}
