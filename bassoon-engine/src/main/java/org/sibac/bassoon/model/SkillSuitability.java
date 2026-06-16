package org.sibac.bassoon.model;

/**
 * How suitable a work is for developing a specific skill.
 *
 * <p>This is an ordinal <b>suitability</b> rating: it answers
 * "how good is this work for training skill X?". The matching CF for each level lives in
 * the DRL skill rules ({@code skills_rules.drl}) via {@code @CF}. Note the scale crosses
 * from positive to negative between {@code MODERATE} and {@code WEAK} - {@code MODERATE}
 * is still a (small) positive contribution.
 */
public enum SkillSuitability {
  REFERENCE,
  VERY_SUITABLE,
  SUITABLE,
  MODERATE,
  WEAK,
  UNSUITABLE,
  TOTALLY_UNSUITABLE
}
