package org.sibac.bassoon.model;

/**
 * Types of evidence the teacher can provide.
 *
 * <p>SKILL_1, SKILL_2, SKILL_3 — Skill (first required, others optional)
 * LAST_ERA — Era (optional, for stylistic variety penalty)
 * PREFERRED_ACCOMPANIMENT — Accompaniment (optional, teacher preference)
 *
 * <p>Note: student level and motivation are not Drools facts — they feed the
 * fuzzy front-end directly, before the session, so they are not listed here.
 */
public enum EvidenceType {
  SKILL_1,
  SKILL_2,
  SKILL_3,
  LAST_ERA,
  PREFERRED_ACCOMPANIMENT
}
