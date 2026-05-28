package org.sibac.bassoon.model;

/**
 * Types of evidence the teacher can provide.
 *
 * <p>STUDENT_LEVEL — StudentLevel (required)
 * SKILL_1, SKILL_2, SKILL_3 — Skill (first required, others optional)
 * MOTIVATION — Motivation (optional)
 * LAST_ERA — Era (optional, for stylistic variety penalty)
 * PREFERRED_ACCOMPANIMENT — Accompaniment (optional, teacher preference)
 */
public enum EvidenceType {
  STUDENT_LEVEL,
  SKILL_1,
  SKILL_2,
  SKILL_3,
  MOTIVATION,
  LAST_ERA,
  PREFERRED_ACCOMPANIMENT
}
