package org.sibac.bassoon.kb;

import static java.util.Map.entry;

import java.util.List;
import java.util.Map;
import org.sibac.bassoon.model.*;

/**
 * Bassoon repertoire knowledge base.
 *
 * <p>Curated bassoon repertoire: 31 works on a 7-level scale, validated against
 * domain expert interviews (Prof. Carolino Carreira).
 */
public final class KnowledgeBase {

  private KnowledgeBase() {}

  public static List<Work> works() {
    return List.of(
        sonataFaMenorTelemann(),
        fantasiasTelemann(),
        concertoReMenorVivaldi(),
        concertoMiMenorVivaldi(),
        concertoSibMozart(),
        concertinoCrusell(),
        concertoFaOp75Weber(),
        andanteRondoWeber(),
        concertoFaHummel(),
        concertinoOp12David(),
        konzertstuckBerwald(),
        romanceOp36SaintSaens(),
        sonataOp168SaintSaens(),
        suiteTansman(),
        sonatineTansman(),
        recitSicilienneBozza(),
        sarabandeCortegeDutilleux(),
        concertinoBitsch(),
        divertissementFrancaix(),
        partitaOp75Jacob(),
        fantasyOp86Arnold(),
        parableOp110Persichetti(),
        niggunHersant(),
        hopiHersant(),
        concertinoSibHaydn(),
        variationsKreutzer(),
        donPasqualeTamplini(),
        luciaTorriani(),
        concertoEsperimentoRossini(),
        fantasiestuckeOp73Schumann(),
        stuckeOp102Schumann());
  }

  // =========================================================================
  // ID 1 - Sonata em Fá menor, G. P. Telemann
  // =========================================================================

  private static Work sonataFaMenorTelemann() {
    return new Work(
        1.0,
        "Sonata em Fá menor",
        "G. P. Telemann",
        Era.BAROQUE,
        "Alemanha",
        Accompaniment.BASSO_CONTINUO,
        DifficultyLevel.LEVEL_1,
        "afudKfX_4Ys",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.SUITABLE),
            entry(Skill.STACCATO, SkillSuitability.WEAK),
            entry(Skill.LOW_REGISTER, SkillSuitability.WEAK),
            entry(Skill.MID_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.FAST_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.UNSUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.MODERATE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.MODERATE),
            entry(Skill.INTONATION, SkillSuitability.SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.UNSUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.MODERATE),
            entry(Skill.FLICKING, SkillSuitability.WEAK),
            entry(Skill.TRILLS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.REFERENCE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.WEAK),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.MODERATE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.SUITABLE)));
  }

  // =========================================================================
  // ID 2 - Fantasias para Flauta (adapt.), G. P. Telemann // Adaptação das 12 Fantasias originalmente para flauta solo
  // =========================================================================

  private static Work fantasiasTelemann() {
    return new Work(
        2.0,
        "Fantasias para Flauta (adapt.)",
        "G. P. Telemann",
        Era.BAROQUE,
        "Alemanha",
        Accompaniment.SOLO,
        DifficultyLevel.LEVEL_2,
        "Pt9C2F0FLrI",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.MODERATE),
            entry(Skill.STACCATO, SkillSuitability.MODERATE),
            entry(Skill.LOW_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.MID_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.WEAK),
            entry(Skill.SLOW_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.FAST_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.WEAK),
            entry(Skill.ENDURANCE, SkillSuitability.VERY_SUITABLE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.SUITABLE),
            entry(Skill.INTONATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.WEAK),
            entry(Skill.COORDINATION, SkillSuitability.MODERATE),
            entry(Skill.FLICKING, SkillSuitability.MODERATE),
            entry(Skill.TRILLS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.REFERENCE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.MODERATE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.WEAK),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.MODERATE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.SUITABLE)));
  }

  // =========================================================================
  // ID 3 - Concerto em Ré menor, A. Vivaldi
  // =========================================================================

  private static Work concertoReMenorVivaldi() {
    return new Work(
        3.0,
        "Concerto em Ré menor",
        "A. Vivaldi",
        Era.BAROQUE,
        "Itália",
        Accompaniment.ORCHESTRA,
        DifficultyLevel.LEVEL_1,
        "enm_73jT3NA",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.SUITABLE),
            entry(Skill.STACCATO, SkillSuitability.SUITABLE),
            entry(Skill.LOW_REGISTER, SkillSuitability.WEAK),
            entry(Skill.MID_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.UNSUITABLE),
            entry(Skill.FAST_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.ENDURANCE, SkillSuitability.SUITABLE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.SUITABLE),
            entry(Skill.INTONATION, SkillSuitability.SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.UNSUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.SUITABLE),
            entry(Skill.FLICKING, SkillSuitability.VERY_SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.REFERENCE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.SUITABLE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.SUITABLE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.SUITABLE)));
  }

  // =========================================================================
  // ID 4 - Concerto em Mi menor, A. Vivaldi
  // =========================================================================

  private static Work concertoMiMenorVivaldi() {
    return new Work(
        4.0,
        "Concerto em Mi menor",
        "A. Vivaldi",
        Era.BAROQUE,
        "Itália",
        Accompaniment.ORCHESTRA,
        DifficultyLevel.LEVEL_2,
        "UJ9PPFLQIDY",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.STACCATO, SkillSuitability.REFERENCE),
            entry(Skill.LOW_REGISTER, SkillSuitability.UNSUITABLE),
            entry(Skill.MID_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.UNSUITABLE),
            entry(Skill.FAST_TEMPO, SkillSuitability.REFERENCE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.REFERENCE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.MODERATE),
            entry(Skill.FLEXIBILITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.INTONATION, SkillSuitability.MODERATE),
            entry(Skill.DYNAMICS, SkillSuitability.UNSUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.REFERENCE),
            entry(Skill.FLICKING, SkillSuitability.REFERENCE),
            entry(Skill.TRILLS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.REFERENCE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.REFERENCE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.REFERENCE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.VERY_SUITABLE)));
  }

  // =========================================================================
  // ID 5 - Concerto em Sib M, W. A. Mozart
  // =========================================================================

  private static Work concertoSibMozart() {
    return new Work(
        5.0,
        "Concerto em Sib M",
        "W. A. Mozart",
        Era.CLASSICAL,
        "Áustria",
        Accompaniment.ORCHESTRA,
        DifficultyLevel.LEVEL_4,
        "QfhxZMUy9DU",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.STACCATO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.LOW_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.MID_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.UNSUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.FAST_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.ENDURANCE, SkillSuitability.SUITABLE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.INTONATION, SkillSuitability.REFERENCE),
            entry(Skill.DYNAMICS, SkillSuitability.SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLICKING, SkillSuitability.REFERENCE),
            entry(Skill.TRILLS, SkillSuitability.REFERENCE),
            entry(Skill.ORNAMENTATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.MODERATE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.UNSUITABLE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.SUITABLE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.REFERENCE)));
  }

  // =========================================================================
  // ID 6 - Concertino, B. H. Crusell
  // =========================================================================

  private static Work concertinoCrusell() {
    return new Work(
        6.0,
        "Concertino",
        "B. H. Crusell",
        Era.CLASSICAL,
        "Suécia/Finlândia",
        Accompaniment.ORCHESTRA,
        DifficultyLevel.LEVEL_5,
        "B0T2FKmAdKg",
        26,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.REFERENCE),
            entry(Skill.STACCATO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.LOW_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.MID_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.WEAK),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FAST_TEMPO, SkillSuitability.REFERENCE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.REFERENCE),
            entry(Skill.ENDURANCE, SkillSuitability.REFERENCE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.REFERENCE),
            entry(Skill.INTONATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.REFERENCE),
            entry(Skill.FLICKING, SkillSuitability.VERY_SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.SUITABLE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.REFERENCE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.REFERENCE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.SUITABLE)));
  }

  // =========================================================================
  // ID 7 - Concerto em Fá M Op. 75, C. M. von Weber
  // =========================================================================

  private static Work concertoFaOp75Weber() {
    return new Work(
        7.0,
        "Concerto em Fá M Op. 75",
        "C. M. von Weber",
        Era.ROMANTIC,
        "Alemanha",
        Accompaniment.ORCHESTRA,
        DifficultyLevel.LEVEL_3,
        "45uZ6LHeP7Y",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.SUITABLE),
            entry(Skill.STACCATO, SkillSuitability.SUITABLE),
            entry(Skill.LOW_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.MID_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.WEAK),
            entry(Skill.SLOW_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.FAST_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.VERY_SUITABLE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.SUITABLE),
            entry(Skill.INTONATION, SkillSuitability.SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.REFERENCE),
            entry(Skill.FLICKING, SkillSuitability.VERY_SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.MODERATE),
            entry(Skill.ORNAMENTATION, SkillSuitability.MODERATE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.MODERATE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.UNSUITABLE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.REFERENCE)));
  }

  // =========================================================================
  // ID 8 - Andante e Rondo Ongarese Op. 35, C. M. von Weber
  // =========================================================================

  private static Work andanteRondoWeber() {
    return new Work(
        8.0,
        "Andante e Rondo Ongarese Op. 35",
        "C. M. von Weber",
        Era.ROMANTIC,
        "Alemanha",
        Accompaniment.ORCHESTRA,
        DifficultyLevel.LEVEL_4,
        "zDpEHLtXzJI",
        7,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.MODERATE),
            entry(Skill.STACCATO, SkillSuitability.SUITABLE),
            entry(Skill.LOW_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.MID_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.WEAK),
            entry(Skill.SLOW_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.FAST_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.SUITABLE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.INTONATION, SkillSuitability.SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLICKING, SkillSuitability.VERY_SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.MODERATE),
            entry(Skill.ORNAMENTATION, SkillSuitability.MODERATE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.SUITABLE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.WEAK),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.SUITABLE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.VERY_SUITABLE)));
  }

  // =========================================================================
  // ID 9 - Concerto em Fá M, J. N. Hummel
  // =========================================================================

  private static Work concertoFaHummel() {
    return new Work(
        9.0,
        "Concerto em Fá M",
        "J. N. Hummel",
        Era.CLASSICAL,
        "Áustria",
        Accompaniment.ORCHESTRA,
        DifficultyLevel.LEVEL_5,
        "OvtRg1DQO7g",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.SUITABLE),
            entry(Skill.STACCATO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.LOW_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.MID_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.FAST_TEMPO, SkillSuitability.REFERENCE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.REFERENCE),
            entry(Skill.ENDURANCE, SkillSuitability.REFERENCE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.REFERENCE),
            entry(Skill.INTONATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.REFERENCE),
            entry(Skill.FLICKING, SkillSuitability.REFERENCE),
            entry(Skill.TRILLS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.VERY_SUITABLE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.MODERATE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.REFERENCE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.VERY_SUITABLE)));
  }

  // =========================================================================
  // ID 10 - Concertino Op. 12, F. David
  // =========================================================================

  private static Work concertinoOp12David() {
    return new Work(
        10.0,
        "Concertino Op. 12",
        "F. David",
        Era.ROMANTIC,
        "Alemanha",
        Accompaniment.ORCHESTRA,
        DifficultyLevel.LEVEL_4,
        "b3Q7mrImZFI",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.MODERATE),
            entry(Skill.STACCATO, SkillSuitability.SUITABLE),
            entry(Skill.LOW_REGISTER, SkillSuitability.UNSUITABLE),
            entry(Skill.MID_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.FAST_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.MODERATE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.MODERATE),
            entry(Skill.FLEXIBILITY, SkillSuitability.MODERATE),
            entry(Skill.INTONATION, SkillSuitability.MODERATE),
            entry(Skill.DYNAMICS, SkillSuitability.SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLICKING, SkillSuitability.MODERATE),
            entry(Skill.TRILLS, SkillSuitability.MODERATE),
            entry(Skill.ORNAMENTATION, SkillSuitability.WEAK),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.UNSUITABLE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.WEAK),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.REFERENCE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.MODERATE)));
  }

  // =========================================================================
  // ID 11 - Konzertstück Op. 2, F. Berwald
  // =========================================================================

  private static Work konzertstuckBerwald() {
    return new Work(
        11.0,
        "Konzertstück Op. 2",
        "F. Berwald",
        Era.ROMANTIC,
        "Suécia",
        Accompaniment.ORCHESTRA,
        DifficultyLevel.LEVEL_5,
        "KWd8hpab1-c",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.MODERATE),
            entry(Skill.STACCATO, SkillSuitability.SUITABLE),
            entry(Skill.LOW_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.MID_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.WEAK),
            entry(Skill.FAST_TEMPO, SkillSuitability.REFERENCE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.REFERENCE),
            entry(Skill.ENDURANCE, SkillSuitability.REFERENCE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.REFERENCE),
            entry(Skill.INTONATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.REFERENCE),
            entry(Skill.FLICKING, SkillSuitability.VERY_SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.REFERENCE),
            entry(Skill.ORNAMENTATION, SkillSuitability.SUITABLE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.SUITABLE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.SUITABLE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.REFERENCE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.VERY_SUITABLE)));
  }

  // =========================================================================
  // ID 12 - Romance Op. 36 (adapt.), C. Saint-Saëns // Adaptação da Romance originalmente para trompa
  // =========================================================================

  private static Work romanceOp36SaintSaens() {
    return new Work(
        12.0,
        "Romance Op. 36 (adapt.)",
        "C. Saint-Saëns",
        Era.ROMANTIC,
        "França",
        Accompaniment.ORCHESTRA,
        DifficultyLevel.LEVEL_1,
        "-cp0-y7EvI0",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.REFERENCE),
            entry(Skill.STACCATO, SkillSuitability.MODERATE),
            entry(Skill.LOW_REGISTER, SkillSuitability.WEAK),
            entry(Skill.MID_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.REFERENCE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.FAST_TEMPO, SkillSuitability.UNSUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.UNSUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.SUITABLE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillSuitability.MODERATE),
            entry(Skill.INTONATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.UNSUITABLE),
            entry(Skill.FLICKING, SkillSuitability.MODERATE),
            entry(Skill.TRILLS, SkillSuitability.UNSUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.UNSUITABLE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.WEAK),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.WEAK),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.WEAK),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.VERY_SUITABLE)));
  }

  // =========================================================================
  // ID 13 - Sonata Op. 168, C. Saint-Saëns
  // =========================================================================

  private static Work sonataOp168SaintSaens() {
    return new Work(
        13.0,
        "Sonata Op. 168",
        "C. Saint-Saëns",
        Era.ROMANTIC,
        "França",
        Accompaniment.PIANO,
        DifficultyLevel.LEVEL_3,
        "JLRWLkugiXQ",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.STACCATO, SkillSuitability.SUITABLE),
            entry(Skill.LOW_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.MID_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.FAST_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.VERY_SUITABLE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.INTONATION, SkillSuitability.REFERENCE),
            entry(Skill.DYNAMICS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLICKING, SkillSuitability.VERY_SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.MODERATE),
            entry(Skill.ORNAMENTATION, SkillSuitability.MODERATE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.SUITABLE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.MODERATE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.REFERENCE)));
  }

  // =========================================================================
  // ID 14 - Suite pour Basson et Piano, A. Tansman
  // =========================================================================

  private static Work suiteTansman() {
    return new Work(
        14.0,
        "Suite pour Basson et Piano",
        "A. Tansman",
        Era.CONTEMPORARY,
        "Polónia/França",
        Accompaniment.PIANO,
        DifficultyLevel.LEVEL_2,
        "SZbvBMDC8qU",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.SUITABLE),
            entry(Skill.STACCATO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.LOW_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.MID_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.FAST_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.ENDURANCE, SkillSuitability.MODERATE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.SUITABLE),
            entry(Skill.INTONATION, SkillSuitability.SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.SUITABLE),
            entry(Skill.FLICKING, SkillSuitability.SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.UNSUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.WEAK),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.MODERATE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.MODERATE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.SUITABLE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.SUITABLE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.SUITABLE)));
  }

  // =========================================================================
  // ID 15 - Sonatine, A. Tansman
  // =========================================================================

  private static Work sonatineTansman() {
    return new Work(
        15.0,
        "Sonatine",
        "A. Tansman",
        Era.CONTEMPORARY,
        "Polónia/França",
        Accompaniment.PIANO,
        DifficultyLevel.LEVEL_4,
        "ir_inPNELRA",
        14,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.SUITABLE),
            entry(Skill.STACCATO, SkillSuitability.REFERENCE),
            entry(Skill.LOW_REGISTER, SkillSuitability.UNSUITABLE),
            entry(Skill.MID_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.FAST_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.REFERENCE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.INTONATION, SkillSuitability.REFERENCE),
            entry(Skill.DYNAMICS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLICKING, SkillSuitability.SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.WEAK),
            entry(Skill.ORNAMENTATION, SkillSuitability.WEAK),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.MODERATE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.MODERATE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.SUITABLE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.SUITABLE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.REFERENCE)));
  }

  // =========================================================================
  // ID 16 - Récit, Sicilienne et Rondo, E. Bozza
  // =========================================================================

  private static Work recitSicilienneBozza() {
    return new Work(
        16.0,
        "Récit, Sicilienne et Rondo",
        "E. Bozza",
        Era.CONTEMPORARY,
        "França",
        Accompaniment.PIANO,
        DifficultyLevel.LEVEL_5,
        "PPekDKzjv7w",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.SUITABLE),
            entry(Skill.STACCATO, SkillSuitability.REFERENCE),
            entry(Skill.LOW_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.MID_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FAST_TEMPO, SkillSuitability.REFERENCE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.VERY_SUITABLE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.REFERENCE),
            entry(Skill.INTONATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLICKING, SkillSuitability.VERY_SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.UNSUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.WEAK),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.SUITABLE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.MODERATE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.SUITABLE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.REFERENCE)));
  }

  // =========================================================================
  // ID 17 - Sarabande et Cortège, H. Dutilleux
  // =========================================================================

  private static Work sarabandeCortegeDutilleux() {
    return new Work(
        17.0,
        "Sarabande et Cortège",
        "H. Dutilleux",
        Era.CONTEMPORARY,
        "França",
        Accompaniment.PIANO,
        DifficultyLevel.LEVEL_6,
        "7sQV869kDSU",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.REFERENCE),
            entry(Skill.STACCATO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.LOW_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.MID_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.REFERENCE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.REFERENCE),
            entry(Skill.FAST_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.SUITABLE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillSuitability.REFERENCE),
            entry(Skill.INTONATION, SkillSuitability.REFERENCE),
            entry(Skill.DYNAMICS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLICKING, SkillSuitability.VERY_SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.MODERATE),
            entry(Skill.ORNAMENTATION, SkillSuitability.WEAK),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.WEAK),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.MODERATE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.REFERENCE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.SUITABLE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.REFERENCE)));
  }

  // =========================================================================
  // ID 18 - Concertino, M. Bitsch
  // =========================================================================

  private static Work concertinoBitsch() {
    return new Work(
        18.0,
        "Concertino",
        "M. Bitsch",
        Era.CONTEMPORARY,
        "França",
        Accompaniment.ORCHESTRA,
        DifficultyLevel.LEVEL_6,
        "dvUg9nikMfM",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.SUITABLE),
            entry(Skill.STACCATO, SkillSuitability.MODERATE),
            entry(Skill.LOW_REGISTER, SkillSuitability.WEAK),
            entry(Skill.MID_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.WEAK),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.FAST_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.VERY_SUITABLE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.INTONATION, SkillSuitability.SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.REFERENCE),
            entry(Skill.FLICKING, SkillSuitability.SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.WEAK),
            entry(Skill.ORNAMENTATION, SkillSuitability.MODERATE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.SUITABLE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.MODERATE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.REFERENCE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.REFERENCE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.SUITABLE)));
  }

  // =========================================================================
  // ID 19 - Divertissement, J. Françaix
  // =========================================================================

  private static Work divertissementFrancaix() {
    return new Work(
        19.0,
        "Divertissement",
        "J. Françaix",
        Era.CONTEMPORARY,
        "França",
        Accompaniment.ORCHESTRA,
        DifficultyLevel.LEVEL_6,
        "cptrH-LEAwE",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.MODERATE),
            entry(Skill.STACCATO, SkillSuitability.REFERENCE),
            entry(Skill.LOW_REGISTER, SkillSuitability.UNSUITABLE),
            entry(Skill.MID_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.FAST_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.REFERENCE),
            entry(Skill.ENDURANCE, SkillSuitability.VERY_SUITABLE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.INTONATION, SkillSuitability.SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.REFERENCE),
            entry(Skill.COORDINATION, SkillSuitability.REFERENCE),
            entry(Skill.FLICKING, SkillSuitability.VERY_SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.MODERATE),
            entry(Skill.ORNAMENTATION, SkillSuitability.MODERATE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.MODERATE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.MODERATE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.SUITABLE)));
  }

  // =========================================================================
  // ID 20 - Partita Op. 75, G. Jacob
  // =========================================================================

  private static Work partitaOp75Jacob() {
    return new Work(
        20.0,
        "Partita Op. 75",
        "G. Jacob",
        Era.CONTEMPORARY,
        "Reino Unido",
        Accompaniment.SOLO,
        DifficultyLevel.LEVEL_3,
        "QB-171Izh18",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.MODERATE),
            entry(Skill.STACCATO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.LOW_REGISTER, SkillSuitability.UNSUITABLE),
            entry(Skill.MID_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.FAST_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.VERY_SUITABLE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.MODERATE),
            entry(Skill.INTONATION, SkillSuitability.SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLICKING, SkillSuitability.SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.UNSUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.WEAK),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.SUITABLE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.WEAK),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.MODERATE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.SUITABLE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.MODERATE)));
  }

  // =========================================================================
  // ID 21 - Fantasy Op. 86, M. Arnold
  // =========================================================================

  private static Work fantasyOp86Arnold() {
    return new Work(
        21.0,
        "Fantasy Op. 86",
        "M. Arnold",
        Era.CONTEMPORARY,
        "Reino Unido",
        Accompaniment.SOLO,
        DifficultyLevel.LEVEL_3,
        "Qyeoblfy4f8",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.MODERATE),
            entry(Skill.STACCATO, SkillSuitability.MODERATE),
            entry(Skill.LOW_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.MID_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.FAST_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.VERY_SUITABLE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.REFERENCE),
            entry(Skill.INTONATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.REFERENCE),
            entry(Skill.FLICKING, SkillSuitability.SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.WEAK),
            entry(Skill.ORNAMENTATION, SkillSuitability.WEAK),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.MODERATE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.MODERATE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.REFERENCE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.MODERATE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.VERY_SUITABLE)));
  }

  // =========================================================================
  // ID 22 - Parable Op. 110, V. Persichetti
  // =========================================================================

  private static Work parableOp110Persichetti() {
    return new Work(
        22.0,
        "Parable Op. 110",
        "V. Persichetti",
        Era.CONTEMPORARY,
        "EUA",
        Accompaniment.SOLO,
        DifficultyLevel.LEVEL_2,
        "5SCGjuRB7x0",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.REFERENCE),
            entry(Skill.STACCATO, SkillSuitability.WEAK),
            entry(Skill.LOW_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.MID_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.FAST_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.MODERATE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.INTONATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.REFERENCE),
            entry(Skill.COORDINATION, SkillSuitability.SUITABLE),
            entry(Skill.FLICKING, SkillSuitability.MODERATE),
            entry(Skill.TRILLS, SkillSuitability.UNSUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.UNSUITABLE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.WEAK),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.VERY_SUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.REFERENCE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.SUITABLE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.REFERENCE)));
  }

  // =========================================================================
  // ID 23 - Niggun, P. Hersant // Dedicada a Pascal Gallois; uso de multifónicos e harmónicos
  // =========================================================================

  private static Work niggunHersant() {
    return new Work(
        23.0,
        "Niggun",
        "P. Hersant",
        Era.CONTEMPORARY,
        "França",
        Accompaniment.SOLO,
        DifficultyLevel.LEVEL_5,
        "eri7ID-GUa8",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.SUITABLE),
            entry(Skill.STACCATO, SkillSuitability.MODERATE),
            entry(Skill.LOW_REGISTER, SkillSuitability.WEAK),
            entry(Skill.MID_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.FAST_TEMPO, SkillSuitability.WEAK),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.UNSUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.MODERATE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.MODERATE),
            entry(Skill.FLEXIBILITY, SkillSuitability.SUITABLE),
            entry(Skill.INTONATION, SkillSuitability.SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.MODERATE),
            entry(Skill.COORDINATION, SkillSuitability.SUITABLE),
            entry(Skill.FLICKING, SkillSuitability.MODERATE),
            entry(Skill.TRILLS, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.MODERATE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.WEAK),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.REFERENCE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.SUITABLE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.MODERATE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.VERY_SUITABLE)));
  }

  // =========================================================================
  // ID 24 - Hopi, P. Hersant // Dedicada a Alexandre Ouzounoff; técnicas estendidas extensivas
  // =========================================================================

  private static Work hopiHersant() {
    return new Work(
        24.0,
        "Hopi",
        "P. Hersant",
        Era.CONTEMPORARY,
        "França",
        Accompaniment.SOLO,
        DifficultyLevel.LEVEL_6,
        "HyGI35dG3qE",
        23,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.WEAK),
            entry(Skill.STACCATO, SkillSuitability.WEAK),
            entry(Skill.LOW_REGISTER, SkillSuitability.WEAK),
            entry(Skill.MID_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.FAST_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.UNSUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.SUITABLE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillSuitability.SUITABLE),
            entry(Skill.INTONATION, SkillSuitability.REFERENCE),
            entry(Skill.DYNAMICS, SkillSuitability.REFERENCE),
            entry(Skill.COORDINATION, SkillSuitability.SUITABLE),
            entry(Skill.FLICKING, SkillSuitability.MODERATE),
            entry(Skill.TRILLS, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.UNSUITABLE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.MODERATE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.REFERENCE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.MODERATE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.VERY_SUITABLE)));
  }

  // =========================================================================
  // ID 25 - Concertino em Sib M, Perger 52/5, M. Haydn // Originalmente movimento da Sinfonia n.º 14 MH 133 (Concertino per il Fagotto)
  // =========================================================================

  private static Work concertinoSibHaydn() {
    return new Work(
        25.0,
        "Concertino em Sib M, Perger 52/5",
        "M. Haydn",
        Era.CLASSICAL,
        "Áustria",
        Accompaniment.ORCHESTRA,
        DifficultyLevel.LEVEL_1,
        "hhhdi024Vxo",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.STACCATO, SkillSuitability.MODERATE),
            entry(Skill.LOW_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.MID_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.UNSUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.FAST_TEMPO, SkillSuitability.WEAK),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.MODERATE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.SUITABLE),
            entry(Skill.INTONATION, SkillSuitability.SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.MODERATE),
            entry(Skill.COORDINATION, SkillSuitability.MODERATE),
            entry(Skill.FLICKING, SkillSuitability.WEAK),
            entry(Skill.TRILLS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.WEAK),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.UNSUITABLE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.WEAK),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.SUITABLE)));
  }

  // =========================================================================
  // ID 26 - Variations KWV 4202, C. Kreutzer
  // =========================================================================

  private static Work variationsKreutzer() {
    return new Work(
        26.0,
        "Variations KWV 4202",
        "C. Kreutzer",
        Era.ROMANTIC,
        "Alemanha",
        Accompaniment.ORCHESTRA,
        DifficultyLevel.LEVEL_4,
        "sBa4uxxS6-s",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.STACCATO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.LOW_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.MID_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.FAST_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.REFERENCE),
            entry(Skill.ENDURANCE, SkillSuitability.VERY_SUITABLE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.REFERENCE),
            entry(Skill.INTONATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.REFERENCE),
            entry(Skill.FLICKING, SkillSuitability.VERY_SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.SUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.VERY_SUITABLE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.MODERATE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.REFERENCE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.VERY_SUITABLE)));
  }

  // =========================================================================
  // ID 27 - Fantasia sobre Don Pasquale de Donizetti, G. Tamplini // Paráfrase operática belcantística
  // =========================================================================

  private static Work donPasqualeTamplini() {
    return new Work(
        27.0,
        "Fantasia sobre Don Pasquale de Donizetti",
        "G. Tamplini",
        Era.ROMANTIC,
        "Itália",
        Accompaniment.PIANO,
        DifficultyLevel.LEVEL_4,
        "SeNa4O-PwO0",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.STACCATO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.LOW_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.MID_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.FAST_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.REFERENCE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.INTONATION, SkillSuitability.REFERENCE),
            entry(Skill.DYNAMICS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLICKING, SkillSuitability.SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.UNSUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.MODERATE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.WEAK),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.MODERATE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.REFERENCE)));
  }

  // =========================================================================
  // ID 28 - Divertimento sobre Lucia di Lammermoor de Donizetti, A. Torriani // Paráfrase operática belcantística
  // =========================================================================

  private static Work luciaTorriani() {
    return new Work(
        28.0,
        "Divertimento sobre Lucia di Lammermoor de Donizetti",
        "A. Torriani",
        Era.ROMANTIC,
        "Itália",
        Accompaniment.PIANO,
        DifficultyLevel.LEVEL_5,
        "XTFKg5yjtPo",
        27,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.REFERENCE),
            entry(Skill.STACCATO, SkillSuitability.SUITABLE),
            entry(Skill.LOW_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.MID_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.FAST_TEMPO, SkillSuitability.REFERENCE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.REFERENCE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillSuitability.REFERENCE),
            entry(Skill.INTONATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.REFERENCE),
            entry(Skill.COORDINATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLICKING, SkillSuitability.SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.UNSUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.MODERATE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.WEAK),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.MODERATE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.REFERENCE)));
  }

  // =========================================================================
  // ID 29 - Concerto em Sib M (Concerto da Esperimento), G. Rossini
  // =========================================================================

  private static Work concertoEsperimentoRossini() {
    return new Work(
        29.0,
        "Concerto em Sib M (Concerto da Esperimento)",
        "G. Rossini",
        Era.ROMANTIC,
        "Itália",
        Accompaniment.ORCHESTRA,
        DifficultyLevel.LEVEL_5,
        "VW01SG8eVFI",
        28,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.SUITABLE),
            entry(Skill.STACCATO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.LOW_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.MID_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.FAST_TEMPO, SkillSuitability.REFERENCE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.ENDURANCE, SkillSuitability.REFERENCE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillSuitability.SUITABLE),
            entry(Skill.INTONATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.REFERENCE),
            entry(Skill.COORDINATION, SkillSuitability.REFERENCE),
            entry(Skill.FLICKING, SkillSuitability.SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.REFERENCE),
            entry(Skill.ORNAMENTATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.MODERATE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.MODERATE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.REFERENCE)));
  }

  // =========================================================================
  // ID 30 - Fantasiestücke Op. 73 (adapt.), R. Schumann // Originalmente para clarinete e piano (1849)
  // =========================================================================

  private static Work fantasiestuckeOp73Schumann() {
    return new Work(
        30.0,
        "Fantasiestücke Op. 73 (adapt.)",
        "R. Schumann",
        Era.ROMANTIC,
        "Alemanha",
        Accompaniment.PIANO,
        DifficultyLevel.LEVEL_3,
        "ed_r6Grpc9o",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.REFERENCE),
            entry(Skill.STACCATO, SkillSuitability.UNSUITABLE),
            entry(Skill.LOW_REGISTER, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.MID_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.WEAK),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.REFERENCE),
            entry(Skill.FAST_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.WEAK),
            entry(Skill.ENDURANCE, SkillSuitability.VERY_SUITABLE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.VERY_SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.MODERATE),
            entry(Skill.INTONATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.VERY_SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.MODERATE),
            entry(Skill.FLICKING, SkillSuitability.MODERATE),
            entry(Skill.TRILLS, SkillSuitability.UNSUITABLE),
            entry(Skill.ORNAMENTATION, SkillSuitability.WEAK),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.UNSUITABLE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.UNSUITABLE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.WEAK),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.REFERENCE)));
  }

  // =========================================================================
  // ID 31 - 5 Stücke im Volkston Op. 102 (adapt.), R. Schumann // Originalmente para violoncelo e piano (1849); carácter folclórico
  // =========================================================================

  private static Work stuckeOp102Schumann() {
    return new Work(
        31.0,
        "5 Stücke im Volkston Op. 102 (adapt.)",
        "R. Schumann",
        Era.ROMANTIC,
        "Alemanha",
        Accompaniment.PIANO,
        DifficultyLevel.LEVEL_2,
        "9bt88I_8K0c",
        -1,
        Map.ofEntries(
            entry(Skill.LEGATO, SkillSuitability.MODERATE),
            entry(Skill.STACCATO, SkillSuitability.MODERATE),
            entry(Skill.LOW_REGISTER, SkillSuitability.UNSUITABLE),
            entry(Skill.MID_REGISTER, SkillSuitability.MODERATE),
            entry(Skill.HIGH_REGISTER, SkillSuitability.SUITABLE),
            entry(Skill.VERY_HIGH_REGISTER, SkillSuitability.UNSUITABLE),
            entry(Skill.SLOW_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.MODERATE_TEMPO, SkillSuitability.SUITABLE),
            entry(Skill.FAST_TEMPO, SkillSuitability.VERY_SUITABLE),
            entry(Skill.VIRTUOSO_TEMPO, SkillSuitability.MODERATE),
            entry(Skill.ENDURANCE, SkillSuitability.MODERATE),
            entry(Skill.SOUND_QUALITY, SkillSuitability.SUITABLE),
            entry(Skill.FLEXIBILITY, SkillSuitability.REFERENCE),
            entry(Skill.INTONATION, SkillSuitability.VERY_SUITABLE),
            entry(Skill.DYNAMICS, SkillSuitability.SUITABLE),
            entry(Skill.COORDINATION, SkillSuitability.REFERENCE),
            entry(Skill.FLICKING, SkillSuitability.SUITABLE),
            entry(Skill.TRILLS, SkillSuitability.WEAK),
            entry(Skill.ORNAMENTATION, SkillSuitability.MODERATE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillSuitability.MODERATE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillSuitability.TOTALLY_UNSUITABLE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillSuitability.MODERATE),
            entry(Skill.TECHNICAL_CHARACTER, SkillSuitability.SUITABLE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillSuitability.VERY_SUITABLE)));
  }

}