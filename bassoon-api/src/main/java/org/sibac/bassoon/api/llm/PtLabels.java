package org.sibac.bassoon.api.llm;

import org.sibac.bassoon.model.Accompaniment;
import org.sibac.bassoon.model.Era;
import org.sibac.bassoon.model.Skill;

/**
 * Portuguese labels for domain enums, used when building the LLM prompt.
 *
 * <p>All methods take an enum constant (never a raw string), so the compiler enforces
 * exhaustive coverage and there are no magic strings to chase.
 */
final class PtLabels {

  private PtLabels() {}

  static String skill(Skill skill) {
    return switch (skill) {
      case LEGATO -> "Legato";
      case STACCATO -> "Staccato";
      case LOW_REGISTER -> "Registo grave";
      case MID_REGISTER -> "Registo médio";
      case HIGH_REGISTER -> "Registo agudo";
      case VERY_HIGH_REGISTER -> "Registo sobreagudo";
      case SLOW_TEMPO -> "Tempo lento";
      case MODERATE_TEMPO -> "Tempo moderado";
      case FAST_TEMPO -> "Tempo rápido";
      case VIRTUOSO_TEMPO -> "Tempo virtuoso";
      case ENDURANCE -> "Resistência";
      case SOUND_QUALITY -> "Qualidade do som";
      case FLEXIBILITY -> "Flexibilidade";
      case INTONATION -> "Afinação";
      case DYNAMICS -> "Dinâmicas";
      case COORDINATION -> "Coordenação";
      case FLICKING -> "Flicking";
      case TRILLS -> "Trilos";
      case ORNAMENTATION -> "Ornamentação";
      case HALF_HOLE_TECHNIQUE -> "Técnica de meio-buraco";
      case CONTEMPORARY_TECHNIQUES -> "Técnicas contemporâneas";
      case RHYTHMIC_COMPLEXITY -> "Complexidade rítmica";
      case TECHNICAL_CHARACTER -> "Caráter técnico";
      case EXPRESSIVE_CHARACTER -> "Caráter expressivo";
    };
  }

  static String era(Era era) {
    if (era == null) {
      return "";
    }
    return switch (era) {
      case BAROQUE -> "Barroco";
      case CLASSICAL -> "Clássico";
      case ROMANTIC -> "Romântico";
      case CONTEMPORARY -> "Contemporâneo";
      case OTHER -> "Outro";
    };
  }

  static String accompaniment(Accompaniment accompaniment) {
    if (accompaniment == null) {
      return "";
    }
    return switch (accompaniment) {
      case SOLO -> "Solo";
      case PIANO -> "Piano";
      case BASSO_CONTINUO -> "Baixo contínuo";
      case ORCHESTRA -> "Orquestra";
    };
  }

  static String difficulty(int level) {
    return switch (level) {
      case 1 -> "muito acessível";
      case 2 -> "acessível";
      case 3 -> "moderada";
      case 4 -> "exigente";
      case 5 -> "muito exigente";
      case 6 -> "extremamente exigente";
      default -> "";
    };
  }
}
