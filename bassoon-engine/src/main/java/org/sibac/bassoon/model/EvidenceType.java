package org.sibac.bassoon.model;

/**
 * Types of evidence the teacher can provide.
 *
 * <p>SKILL_1, SKILL_2, SKILL_3 — Skill (first required, others optional)
 * LAST_ERA — Era (optional)
 * PREFERRED_ACCOMPANIMENT — Accompaniment (optional)
 */
public enum EvidenceType {
  SKILL_1,
  SKILL_2,
  SKILL_3,
  LAST_ERA,
  PREFERRED_ACCOMPANIMENT
}
