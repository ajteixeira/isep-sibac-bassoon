package org.sibac.bassoon.model;

/**
 * A work's suitability level for a specific skill.
 *
 * <p>The CF for each level is defined in the DRL rules via {@code @CF}:
 *
 * <pre>
 *   REFERENCE    — reference work for this skill
 *   HIGH         — works the skill well
 *   MEDIUM_HIGH  — works the skill, with minor limitations
 *   MEDIUM       — works the skill, but better choices exist
 *   MEDIUM_LOW   — poorly suited; better choices elsewhere
 *   LOW          — advised against for this skill
 *   AVOID        — no presence; makes no sense to suggest
 * </pre>
 */
public enum SkillLevel {
  REFERENCE,
  HIGH,
  MEDIUM_HIGH,
  MEDIUM,
  MEDIUM_LOW,
  LOW,
  AVOID
}
