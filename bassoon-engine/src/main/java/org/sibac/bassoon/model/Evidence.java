package org.sibac.bassoon.model;

/**
 * An input fact provided by the teacher at query time.
 *
 * <p>Examples:
 * <pre>
 *   new Evidence(EvidenceType.STUDENT_LEVEL, StudentLevel.INTERMEDIATE)
 *   new Evidence(EvidenceType.SKILL_1, Skill.STACCATO, 0.50)
 *   new Evidence(EvidenceType.MOTIVATION, Motivation.HIGH)
 *   new Evidence(EvidenceType.LAST_ERA, Era.BAROQUE)
 *   new Evidence(EvidenceType.PREFERRED_ACCOMPANIMENT, Accompaniment.PIANO, 0.80)
 * </pre>
 */
public class Evidence implements CfFact {

  private final EvidenceType description;
  private final Object value; // typed value: StudentLevel, Skill, Motivation, or Era
  private final double cf;

  public Evidence(EvidenceType description, Object value, double cf) {
    this.description = description;
    this.value = value;
    this.cf = cf;
  }

  // constructor without CF for deterministic facts (level, motivation, last era)
  public Evidence(EvidenceType description, Object value) {
    this(description, value, 1.0);
  }

  public EvidenceType getDescription() {
    return description;
  }

  public Object getValue() {
    return value;
  }

  public double getCf() {
    return cf;
  }

  @Override
  public String toString() {
    return "Evidence[" + description + "=" + value + ", CF=" + cf + "]";
  }
}
