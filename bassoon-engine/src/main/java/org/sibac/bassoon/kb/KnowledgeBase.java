package org.sibac.bassoon.kb;

import static java.util.Map.entry;

import java.util.List;
import java.util.Map;
import org.sibac.bassoon.model.*;

public final class KnowledgeBase {

  private KnowledgeBase() {}

  public static List<Work> works() {
    return List.of(sonataInFMinor(), mozartConcerto());
  }

  private static Work sonataInFMinor() {
    return new Work(
        1.0,
        "Sonata em Fá menor",
        "G. P. Telemann",
        Era.BAROQUE,
        "Alemanha",
        Accompaniment.BASSO_CONTINUO,
        DifficultyLevel.LEVEL_1,
        "G4k2wWjV6gY",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillLevel.MEDIUM),
            entry(Skill.STACCATO, SkillLevel.LOW),
            entry(Skill.LOW_REGISTER, SkillLevel.LOW),
            entry(Skill.MID_REGISTER, SkillLevel.HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.MEDIUM),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.NONE),
            entry(Skill.SLOW_TEMPO, SkillLevel.HIGH),
            entry(Skill.MODERATE_TEMPO, SkillLevel.MEDIUM),
            entry(Skill.FAST_TEMPO, SkillLevel.MEDIUM),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.LOW),
            entry(Skill.ENDURANCE, SkillLevel.MEDIUM),
            entry(Skill.SOUND_QUALITY, SkillLevel.MEDIUM),
            entry(Skill.FLEXIBILITY, SkillLevel.MEDIUM),
            entry(Skill.INTONATION, SkillLevel.HIGH),
            entry(Skill.DYNAMICS, SkillLevel.NONE),
            entry(Skill.COORDINATION, SkillLevel.MEDIUM),
            entry(Skill.FLICKING, SkillLevel.LOW),
            entry(Skill.TRILLS, SkillLevel.HIGH),
            entry(Skill.ORNAMENTATION, SkillLevel.HIGH),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.LOW),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.NONE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.NONE),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.MEDIUM),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.HIGH)));
  }

  private static Work mozartConcerto() {
    return new Work(
        2.0,
        "Concerto in B♭, K. 191",
        "W. A. Mozart",
        Era.CLASSICAL,
        "Austria",
        Accompaniment.ORCHESTRA,
        DifficultyLevel.LEVEL_5,
        "QfhxZMUy9DU",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillLevel.REFERENCE),
            entry(Skill.STACCATO, SkillLevel.MEDIUM),
            entry(Skill.LOW_REGISTER, SkillLevel.LOW),
            entry(Skill.MID_REGISTER, SkillLevel.HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.HIGH),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.MEDIUM),
            entry(Skill.SLOW_TEMPO, SkillLevel.LOW),
            entry(Skill.MODERATE_TEMPO, SkillLevel.HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.MEDIUM),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.MEDIUM),
            entry(Skill.ENDURANCE, SkillLevel.HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.HIGH),
            entry(Skill.FLEXIBILITY, SkillLevel.HIGH),
            entry(Skill.INTONATION, SkillLevel.REFERENCE),
            entry(Skill.DYNAMICS, SkillLevel.HIGH),
            entry(Skill.COORDINATION, SkillLevel.HIGH),
            entry(Skill.FLICKING, SkillLevel.LOW),
            entry(Skill.TRILLS, SkillLevel.MEDIUM),
            entry(Skill.ORNAMENTATION, SkillLevel.HIGH),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.NONE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.NONE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.MEDIUM),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.REFERENCE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.REFERENCE)));
  }
}
