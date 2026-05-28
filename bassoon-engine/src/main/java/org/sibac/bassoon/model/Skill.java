package org.sibac.bassoon.model;

/**
 * Skills identified by the domain expert. Grouped by the categories used during
 * knowledge acquisition.
 */
public enum Skill {
  // --- Articulation ---
  LEGATO,
  STACCATO,

  // --- Register ---
  LOW_REGISTER,
  MID_REGISTER,
  HIGH_REGISTER,
  VERY_HIGH_REGISTER,

  // --- Tempo ---
  SLOW_TEMPO,
  MODERATE_TEMPO,
  FAST_TEMPO,
  VIRTUOSO_TEMPO,

  // --- Sound control ---
  ENDURANCE,
  SOUND_QUALITY,
  FLEXIBILITY,
  INTONATION,
  DYNAMICS,

  // --- Technical challenges ---
  COORDINATION,
  FLICKING,
  TRILLS,
  ORNAMENTATION,
  HALF_HOLE_TECHNIQUE,
  CONTEMPORARY_TECHNIQUES,

  // --- Rhythm ---
  RHYTHMIC_COMPLEXITY,

  // --- Character ---
  TECHNICAL_CHARACTER,
  EXPRESSIVE_CHARACTER
}
