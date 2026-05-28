package org.sibac.bassoon.model;

/**
 * A work's suitability level for a specific skill.
 *
 * <p>The CF for each level is defined in the DRL rules via {@code @CF}:
 *
 * <pre>
 *   REFERENCE  — reference work for this skill
 *   HIGH       — works the skill well
 *   MEDIUM     — works the skill, but better choices exist
 *   LOW        — (neutral, does not contribute)
 *   NONE       — advised against for this skill
 * </pre>
 */
public enum SkillLevel {
  REFERENCE,
  HIGH,
  MEDIUM,
  LOW,
  NONE
}
