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

  /** Portuguese name of the skill (e.g. TRILLS → "Trilos"). */
  static String skill(Skill skill) {
    return switch (skill) {
      case LEGATO -> "Legato";
      case STACCATO -> "Staccato";
      case LOW_REGISTER -> "Registo grave";
      case MID_REGISTER -> "Registo medio";
      case HIGH_REGISTER -> "Registo agudo";
      case VERY_HIGH_REGISTER -> "Registo sobreagudo";
      case SLOW_TEMPO -> "Tempo lento";
      case MODERATE_TEMPO -> "Tempo moderado";
      case FAST_TEMPO -> "Tempo rapido";
      case VIRTUOSO_TEMPO -> "Tempo virtuoso";
      case ENDURANCE -> "Resistencia";
      case SOUND_QUALITY -> "Qualidade do som";
      case FLEXIBILITY -> "Flexibilidade";
      case INTONATION -> "Afinacao";
      case DYNAMICS -> "Dinamicas";
      case COORDINATION -> "Coordenacao";
      case FLICKING -> "Flicking";
      case TRILLS -> "Trilos";
      case ORNAMENTATION -> "Ornamentacao";
      case HALF_HOLE_TECHNIQUE -> "Tecnica de meio-buraco";
      case CONTEMPORARY_TECHNIQUES -> "Tecnicas contemporaneas";
      case RHYTHMIC_COMPLEXITY -> "Complexidade ritmica";
      case TECHNICAL_CHARACTER -> "Caracter tecnico";
      case EXPRESSIVE_CHARACTER -> "Caracter expressivo";
    };
  }

  /** Portuguese name of the era (e.g. BAROQUE → "Barroco"). */
  static String era(Era era) {
    if (era == null) {
      return "";
    }
    return switch (era) {
      case BAROQUE -> "Barroco";
      case CLASSICAL -> "Classico";
      case ROMANTIC -> "Romantico";
      case CONTEMPORARY -> "Contemporaneo";
      case OTHER -> "Outro";
    };
  }

  /** Portuguese name of the accompaniment type (e.g. BASSO_CONTINUO → "Baixo continuo"). */
  static String accompaniment(Accompaniment accompaniment) {
    if (accompaniment == null) {
      return "";
    }
    return switch (accompaniment) {
      case SOLO -> "Solo";
      case PIANO -> "Piano";
      case BASSO_CONTINUO -> "Baixo continuo";
      case ORCHESTRA -> "Orquestra";
    };
  }

  /** Portuguese label for difficulty (e.g. 3 → "moderada"). */
  static String difficulty(int level) {
    return switch (level) {
      case 1 -> "muito acessivel";
      case 2 -> "acessivel";
      case 3 -> "moderada";
      case 4 -> "exigente";
      case 5 -> "muito exigente";
      case 6 -> "extremamente exigente";
      default -> "";
    };
  }
}
