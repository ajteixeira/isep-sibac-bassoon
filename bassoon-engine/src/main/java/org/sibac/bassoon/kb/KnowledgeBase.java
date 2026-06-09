package org.sibac.bassoon.kb;

import static java.util.Map.entry;

import java.util.List;
import java.util.Map;
import org.sibac.bassoon.model.*;

/**
 * Bassoon repertoire knowledge base.
 *
 * <p>Populated from {@code obras_fagote_v5.xlsx} (31 works, 7-level scale),
 * validated against domain expert interviews (Prof. Carolino Carreira).
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
            entry(Skill.LEGATO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.STACCATO, SkillLevel.MEDIUM_LOW),
            entry(Skill.LOW_REGISTER, SkillLevel.MEDIUM_LOW),
            entry(Skill.MID_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.AVOID),
            entry(Skill.SLOW_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.LOW),
            entry(Skill.SOUND_QUALITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.INTONATION, SkillLevel.MEDIUM_HIGH),
            entry(Skill.DYNAMICS, SkillLevel.LOW),
            entry(Skill.FLICKING, SkillLevel.MEDIUM_LOW),
            entry(Skill.TRILLS, SkillLevel.HIGH),
            entry(Skill.ORNAMENTATION, SkillLevel.REFERENCE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.MEDIUM_LOW),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.AVOID),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.MEDIUM_HIGH)));
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
            entry(Skill.MID_REGISTER, SkillLevel.HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.MEDIUM_LOW),
            entry(Skill.SLOW_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.MODERATE_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.MEDIUM_LOW),
            entry(Skill.ENDURANCE, SkillLevel.HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.HIGH),
            entry(Skill.FLEXIBILITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.INTONATION, SkillLevel.HIGH),
            entry(Skill.DYNAMICS, SkillLevel.MEDIUM_LOW),
            entry(Skill.TRILLS, SkillLevel.HIGH),
            entry(Skill.ORNAMENTATION, SkillLevel.REFERENCE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.MEDIUM_LOW),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.MEDIUM_HIGH)));
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
            entry(Skill.LEGATO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.STACCATO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.LOW_REGISTER, SkillLevel.MEDIUM_LOW),
            entry(Skill.MID_REGISTER, SkillLevel.HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.AVOID),
            entry(Skill.SLOW_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.MODERATE_TEMPO, SkillLevel.LOW),
            entry(Skill.FAST_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.ENDURANCE, SkillLevel.MEDIUM_HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FLEXIBILITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.INTONATION, SkillLevel.MEDIUM_HIGH),
            entry(Skill.DYNAMICS, SkillLevel.LOW),
            entry(Skill.COORDINATION, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FLICKING, SkillLevel.HIGH),
            entry(Skill.TRILLS, SkillLevel.HIGH),
            entry(Skill.ORNAMENTATION, SkillLevel.REFERENCE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.MEDIUM_HIGH),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.AVOID),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.MEDIUM_HIGH)));
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
            entry(Skill.LEGATO, SkillLevel.HIGH),
            entry(Skill.STACCATO, SkillLevel.REFERENCE),
            entry(Skill.LOW_REGISTER, SkillLevel.LOW),
            entry(Skill.MID_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.AVOID),
            entry(Skill.SLOW_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.MODERATE_TEMPO, SkillLevel.LOW),
            entry(Skill.FAST_TEMPO, SkillLevel.REFERENCE),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.ENDURANCE, SkillLevel.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillLevel.HIGH),
            entry(Skill.DYNAMICS, SkillLevel.LOW),
            entry(Skill.COORDINATION, SkillLevel.REFERENCE),
            entry(Skill.FLICKING, SkillLevel.REFERENCE),
            entry(Skill.TRILLS, SkillLevel.HIGH),
            entry(Skill.ORNAMENTATION, SkillLevel.REFERENCE),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.REFERENCE),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.AVOID),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.REFERENCE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.HIGH)));
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
            entry(Skill.LEGATO, SkillLevel.HIGH),
            entry(Skill.STACCATO, SkillLevel.HIGH),
            entry(Skill.MID_REGISTER, SkillLevel.HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.LOW),
            entry(Skill.SLOW_TEMPO, SkillLevel.HIGH),
            entry(Skill.MODERATE_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.ENDURANCE, SkillLevel.MEDIUM_HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillLevel.HIGH),
            entry(Skill.INTONATION, SkillLevel.REFERENCE),
            entry(Skill.DYNAMICS, SkillLevel.MEDIUM_HIGH),
            entry(Skill.COORDINATION, SkillLevel.HIGH),
            entry(Skill.FLICKING, SkillLevel.REFERENCE),
            entry(Skill.TRILLS, SkillLevel.REFERENCE),
            entry(Skill.ORNAMENTATION, SkillLevel.HIGH),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.LOW),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.REFERENCE)));
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
            entry(Skill.LEGATO, SkillLevel.REFERENCE),
            entry(Skill.STACCATO, SkillLevel.HIGH),
            entry(Skill.LOW_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.MID_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.HIGH_REGISTER, SkillLevel.HIGH),
            entry(Skill.MODERATE_TEMPO, SkillLevel.HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.REFERENCE),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.REFERENCE),
            entry(Skill.ENDURANCE, SkillLevel.REFERENCE),
            entry(Skill.SOUND_QUALITY, SkillLevel.HIGH),
            entry(Skill.FLEXIBILITY, SkillLevel.REFERENCE),
            entry(Skill.INTONATION, SkillLevel.HIGH),
            entry(Skill.DYNAMICS, SkillLevel.HIGH),
            entry(Skill.COORDINATION, SkillLevel.REFERENCE),
            entry(Skill.FLICKING, SkillLevel.HIGH),
            entry(Skill.TRILLS, SkillLevel.HIGH),
            entry(Skill.ORNAMENTATION, SkillLevel.HIGH),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.MEDIUM_HIGH),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.REFERENCE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.MEDIUM_HIGH)));
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
            entry(Skill.LEGATO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.STACCATO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.MID_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.HIGH),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.MEDIUM_LOW),
            entry(Skill.SLOW_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.HIGH),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.ENDURANCE, SkillLevel.HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FLEXIBILITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.INTONATION, SkillLevel.MEDIUM_HIGH),
            entry(Skill.DYNAMICS, SkillLevel.MEDIUM_HIGH),
            entry(Skill.COORDINATION, SkillLevel.REFERENCE),
            entry(Skill.FLICKING, SkillLevel.HIGH),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.LOW),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.HIGH),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.REFERENCE)));
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
            entry(Skill.STACCATO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.MID_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.HIGH),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.MEDIUM_LOW),
            entry(Skill.SLOW_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.HIGH),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.HIGH),
            entry(Skill.ENDURANCE, SkillLevel.MEDIUM_HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.HIGH),
            entry(Skill.FLEXIBILITY, SkillLevel.HIGH),
            entry(Skill.INTONATION, SkillLevel.MEDIUM_HIGH),
            entry(Skill.DYNAMICS, SkillLevel.MEDIUM_HIGH),
            entry(Skill.COORDINATION, SkillLevel.HIGH),
            entry(Skill.FLICKING, SkillLevel.HIGH),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.MEDIUM_HIGH),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.MEDIUM_LOW),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.HIGH)));
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
            entry(Skill.LEGATO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.STACCATO, SkillLevel.HIGH),
            entry(Skill.MID_REGISTER, SkillLevel.HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.SLOW_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.REFERENCE),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.REFERENCE),
            entry(Skill.ENDURANCE, SkillLevel.REFERENCE),
            entry(Skill.SOUND_QUALITY, SkillLevel.HIGH),
            entry(Skill.FLEXIBILITY, SkillLevel.REFERENCE),
            entry(Skill.INTONATION, SkillLevel.HIGH),
            entry(Skill.DYNAMICS, SkillLevel.HIGH),
            entry(Skill.COORDINATION, SkillLevel.REFERENCE),
            entry(Skill.FLICKING, SkillLevel.REFERENCE),
            entry(Skill.TRILLS, SkillLevel.HIGH),
            entry(Skill.ORNAMENTATION, SkillLevel.HIGH),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.HIGH),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.REFERENCE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.HIGH)));
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
            entry(Skill.STACCATO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.LOW_REGISTER, SkillLevel.LOW),
            entry(Skill.HIGH_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.HIGH),
            entry(Skill.DYNAMICS, SkillLevel.MEDIUM_HIGH),
            entry(Skill.COORDINATION, SkillLevel.HIGH),
            entry(Skill.ORNAMENTATION, SkillLevel.MEDIUM_LOW),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.LOW),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.MEDIUM_LOW),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.REFERENCE)));
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
            entry(Skill.STACCATO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.LOW_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.MID_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.MODERATE_TEMPO, SkillLevel.MEDIUM_LOW),
            entry(Skill.FAST_TEMPO, SkillLevel.REFERENCE),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.REFERENCE),
            entry(Skill.ENDURANCE, SkillLevel.REFERENCE),
            entry(Skill.SOUND_QUALITY, SkillLevel.HIGH),
            entry(Skill.FLEXIBILITY, SkillLevel.REFERENCE),
            entry(Skill.INTONATION, SkillLevel.HIGH),
            entry(Skill.DYNAMICS, SkillLevel.HIGH),
            entry(Skill.COORDINATION, SkillLevel.REFERENCE),
            entry(Skill.FLICKING, SkillLevel.HIGH),
            entry(Skill.TRILLS, SkillLevel.REFERENCE),
            entry(Skill.ORNAMENTATION, SkillLevel.MEDIUM_HIGH),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.MEDIUM_HIGH),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.REFERENCE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.HIGH)));
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
            entry(Skill.LEGATO, SkillLevel.REFERENCE),
            entry(Skill.LOW_REGISTER, SkillLevel.MEDIUM_LOW),
            entry(Skill.MID_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.HIGH),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.SLOW_TEMPO, SkillLevel.REFERENCE),
            entry(Skill.FAST_TEMPO, SkillLevel.LOW),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.LOW),
            entry(Skill.ENDURANCE, SkillLevel.MEDIUM_HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.REFERENCE),
            entry(Skill.INTONATION, SkillLevel.HIGH),
            entry(Skill.DYNAMICS, SkillLevel.MEDIUM_HIGH),
            entry(Skill.COORDINATION, SkillLevel.LOW),
            entry(Skill.TRILLS, SkillLevel.LOW),
            entry(Skill.ORNAMENTATION, SkillLevel.LOW),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.MEDIUM_LOW),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.MEDIUM_LOW),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.MEDIUM_LOW),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.HIGH)));
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
            entry(Skill.LEGATO, SkillLevel.HIGH),
            entry(Skill.STACCATO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.MID_REGISTER, SkillLevel.HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.HIGH),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.SLOW_TEMPO, SkillLevel.HIGH),
            entry(Skill.MODERATE_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.HIGH),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.ENDURANCE, SkillLevel.HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillLevel.HIGH),
            entry(Skill.INTONATION, SkillLevel.REFERENCE),
            entry(Skill.DYNAMICS, SkillLevel.HIGH),
            entry(Skill.COORDINATION, SkillLevel.HIGH),
            entry(Skill.FLICKING, SkillLevel.HIGH),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.MEDIUM_HIGH),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.HIGH),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.REFERENCE)));
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
            entry(Skill.LEGATO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.STACCATO, SkillLevel.HIGH),
            entry(Skill.MID_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.HIGH),
            entry(Skill.SLOW_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FLEXIBILITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.INTONATION, SkillLevel.MEDIUM_HIGH),
            entry(Skill.DYNAMICS, SkillLevel.MEDIUM_HIGH),
            entry(Skill.COORDINATION, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FLICKING, SkillLevel.MEDIUM_HIGH),
            entry(Skill.TRILLS, SkillLevel.LOW),
            entry(Skill.ORNAMENTATION, SkillLevel.MEDIUM_LOW),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.MEDIUM_HIGH)));
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
            entry(Skill.LEGATO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.STACCATO, SkillLevel.REFERENCE),
            entry(Skill.LOW_REGISTER, SkillLevel.LOW),
            entry(Skill.MID_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.SLOW_TEMPO, SkillLevel.HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.ENDURANCE, SkillLevel.REFERENCE),
            entry(Skill.SOUND_QUALITY, SkillLevel.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillLevel.HIGH),
            entry(Skill.INTONATION, SkillLevel.REFERENCE),
            entry(Skill.DYNAMICS, SkillLevel.HIGH),
            entry(Skill.COORDINATION, SkillLevel.HIGH),
            entry(Skill.FLICKING, SkillLevel.MEDIUM_HIGH),
            entry(Skill.TRILLS, SkillLevel.MEDIUM_LOW),
            entry(Skill.ORNAMENTATION, SkillLevel.MEDIUM_LOW),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.REFERENCE)));
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
            entry(Skill.LEGATO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.STACCATO, SkillLevel.REFERENCE),
            entry(Skill.LOW_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.MID_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.SLOW_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.MODERATE_TEMPO, SkillLevel.HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.REFERENCE),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.ENDURANCE, SkillLevel.HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.HIGH),
            entry(Skill.FLEXIBILITY, SkillLevel.REFERENCE),
            entry(Skill.INTONATION, SkillLevel.HIGH),
            entry(Skill.DYNAMICS, SkillLevel.HIGH),
            entry(Skill.COORDINATION, SkillLevel.HIGH),
            entry(Skill.FLICKING, SkillLevel.HIGH),
            entry(Skill.TRILLS, SkillLevel.LOW),
            entry(Skill.ORNAMENTATION, SkillLevel.MEDIUM_LOW),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.MEDIUM_HIGH),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.HIGH),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.REFERENCE)));
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
            entry(Skill.LEGATO, SkillLevel.REFERENCE),
            entry(Skill.STACCATO, SkillLevel.HIGH),
            entry(Skill.LOW_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.MID_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.HIGH_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.SLOW_TEMPO, SkillLevel.REFERENCE),
            entry(Skill.MODERATE_TEMPO, SkillLevel.REFERENCE),
            entry(Skill.FAST_TEMPO, SkillLevel.HIGH),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.ENDURANCE, SkillLevel.MEDIUM_HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillLevel.REFERENCE),
            entry(Skill.INTONATION, SkillLevel.REFERENCE),
            entry(Skill.DYNAMICS, SkillLevel.HIGH),
            entry(Skill.COORDINATION, SkillLevel.HIGH),
            entry(Skill.FLICKING, SkillLevel.HIGH),
            entry(Skill.ORNAMENTATION, SkillLevel.MEDIUM_LOW),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.MEDIUM_LOW),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.REFERENCE),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.REFERENCE)));
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
            entry(Skill.LEGATO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.LOW_REGISTER, SkillLevel.MEDIUM_LOW),
            entry(Skill.MID_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.HIGH),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.SLOW_TEMPO, SkillLevel.MEDIUM_LOW),
            entry(Skill.FAST_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.HIGH),
            entry(Skill.ENDURANCE, SkillLevel.HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FLEXIBILITY, SkillLevel.HIGH),
            entry(Skill.INTONATION, SkillLevel.MEDIUM_HIGH),
            entry(Skill.DYNAMICS, SkillLevel.MEDIUM_HIGH),
            entry(Skill.COORDINATION, SkillLevel.REFERENCE),
            entry(Skill.FLICKING, SkillLevel.MEDIUM_HIGH),
            entry(Skill.TRILLS, SkillLevel.MEDIUM_LOW),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.MEDIUM_HIGH),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.REFERENCE),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.REFERENCE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.MEDIUM_HIGH)));
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
            entry(Skill.STACCATO, SkillLevel.REFERENCE),
            entry(Skill.LOW_REGISTER, SkillLevel.LOW),
            entry(Skill.MID_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.HIGH),
            entry(Skill.SLOW_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.HIGH),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.REFERENCE),
            entry(Skill.ENDURANCE, SkillLevel.HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.HIGH),
            entry(Skill.FLEXIBILITY, SkillLevel.HIGH),
            entry(Skill.INTONATION, SkillLevel.MEDIUM_HIGH),
            entry(Skill.DYNAMICS, SkillLevel.REFERENCE),
            entry(Skill.COORDINATION, SkillLevel.REFERENCE),
            entry(Skill.FLICKING, SkillLevel.HIGH),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.HIGH),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.HIGH),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.MEDIUM_HIGH)));
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
            entry(Skill.STACCATO, SkillLevel.HIGH),
            entry(Skill.LOW_REGISTER, SkillLevel.LOW),
            entry(Skill.HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.ENDURANCE, SkillLevel.HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.INTONATION, SkillLevel.MEDIUM_HIGH),
            entry(Skill.DYNAMICS, SkillLevel.HIGH),
            entry(Skill.COORDINATION, SkillLevel.HIGH),
            entry(Skill.FLICKING, SkillLevel.MEDIUM_HIGH),
            entry(Skill.TRILLS, SkillLevel.LOW),
            entry(Skill.ORNAMENTATION, SkillLevel.MEDIUM_LOW),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.MEDIUM_HIGH),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.MEDIUM_LOW),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.MEDIUM_HIGH)));
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
            entry(Skill.LOW_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.MID_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.HIGH),
            entry(Skill.ENDURANCE, SkillLevel.HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.HIGH),
            entry(Skill.FLEXIBILITY, SkillLevel.REFERENCE),
            entry(Skill.INTONATION, SkillLevel.HIGH),
            entry(Skill.DYNAMICS, SkillLevel.HIGH),
            entry(Skill.COORDINATION, SkillLevel.REFERENCE),
            entry(Skill.FLICKING, SkillLevel.MEDIUM_HIGH),
            entry(Skill.TRILLS, SkillLevel.MEDIUM_LOW),
            entry(Skill.ORNAMENTATION, SkillLevel.MEDIUM_LOW),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.REFERENCE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.HIGH)));
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
            entry(Skill.LEGATO, SkillLevel.REFERENCE),
            entry(Skill.STACCATO, SkillLevel.MEDIUM_LOW),
            entry(Skill.HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.SLOW_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.HIGH),
            entry(Skill.FLEXIBILITY, SkillLevel.HIGH),
            entry(Skill.INTONATION, SkillLevel.HIGH),
            entry(Skill.DYNAMICS, SkillLevel.REFERENCE),
            entry(Skill.COORDINATION, SkillLevel.MEDIUM_HIGH),
            entry(Skill.TRILLS, SkillLevel.LOW),
            entry(Skill.ORNAMENTATION, SkillLevel.LOW),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.MEDIUM_LOW),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.HIGH),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.REFERENCE),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.REFERENCE)));
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
            entry(Skill.LEGATO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.LOW_REGISTER, SkillLevel.MEDIUM_LOW),
            entry(Skill.MID_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.MODERATE_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.MEDIUM_LOW),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.LOW),
            entry(Skill.FLEXIBILITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.INTONATION, SkillLevel.MEDIUM_HIGH),
            entry(Skill.COORDINATION, SkillLevel.MEDIUM_HIGH),
            entry(Skill.TRILLS, SkillLevel.AVOID),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.MEDIUM_LOW),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.REFERENCE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.HIGH)));
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
            entry(Skill.LEGATO, SkillLevel.MEDIUM_LOW),
            entry(Skill.STACCATO, SkillLevel.MEDIUM_LOW),
            entry(Skill.LOW_REGISTER, SkillLevel.MEDIUM_LOW),
            entry(Skill.HIGH_REGISTER, SkillLevel.HIGH),
            entry(Skill.MODERATE_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.LOW),
            entry(Skill.ENDURANCE, SkillLevel.MEDIUM_HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.INTONATION, SkillLevel.REFERENCE),
            entry(Skill.DYNAMICS, SkillLevel.REFERENCE),
            entry(Skill.COORDINATION, SkillLevel.MEDIUM_HIGH),
            entry(Skill.TRILLS, SkillLevel.AVOID),
            entry(Skill.ORNAMENTATION, SkillLevel.LOW),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.REFERENCE),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.HIGH),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.HIGH)));
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
            entry(Skill.LEGATO, SkillLevel.HIGH),
            entry(Skill.MID_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.LOW),
            entry(Skill.SLOW_TEMPO, SkillLevel.HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.MEDIUM_LOW),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.AVOID),
            entry(Skill.SOUND_QUALITY, SkillLevel.HIGH),
            entry(Skill.FLEXIBILITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.INTONATION, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FLICKING, SkillLevel.MEDIUM_LOW),
            entry(Skill.TRILLS, SkillLevel.HIGH),
            entry(Skill.ORNAMENTATION, SkillLevel.HIGH),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.MEDIUM_LOW),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.LOW),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.MEDIUM_LOW),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.MEDIUM_HIGH)));
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
            entry(Skill.LEGATO, SkillLevel.HIGH),
            entry(Skill.STACCATO, SkillLevel.HIGH),
            entry(Skill.MID_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.HIGH_REGISTER, SkillLevel.HIGH),
            entry(Skill.MODERATE_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.HIGH),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.REFERENCE),
            entry(Skill.ENDURANCE, SkillLevel.HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.HIGH),
            entry(Skill.FLEXIBILITY, SkillLevel.REFERENCE),
            entry(Skill.INTONATION, SkillLevel.HIGH),
            entry(Skill.DYNAMICS, SkillLevel.MEDIUM_HIGH),
            entry(Skill.COORDINATION, SkillLevel.REFERENCE),
            entry(Skill.FLICKING, SkillLevel.HIGH),
            entry(Skill.TRILLS, SkillLevel.MEDIUM_HIGH),
            entry(Skill.ORNAMENTATION, SkillLevel.HIGH),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.HIGH),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.REFERENCE),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.HIGH)));
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
            entry(Skill.LEGATO, SkillLevel.HIGH),
            entry(Skill.STACCATO, SkillLevel.HIGH),
            entry(Skill.MID_REGISTER, SkillLevel.HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.FAST_TEMPO, SkillLevel.HIGH),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.ENDURANCE, SkillLevel.REFERENCE),
            entry(Skill.SOUND_QUALITY, SkillLevel.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillLevel.HIGH),
            entry(Skill.INTONATION, SkillLevel.REFERENCE),
            entry(Skill.DYNAMICS, SkillLevel.HIGH),
            entry(Skill.COORDINATION, SkillLevel.HIGH),
            entry(Skill.FLICKING, SkillLevel.MEDIUM_HIGH),
            entry(Skill.TRILLS, SkillLevel.LOW),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.MEDIUM_LOW),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.HIGH),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.REFERENCE)));
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
            entry(Skill.LEGATO, SkillLevel.REFERENCE),
            entry(Skill.STACCATO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.MID_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.HIGH_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.MODERATE_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.REFERENCE),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.HIGH),
            entry(Skill.ENDURANCE, SkillLevel.REFERENCE),
            entry(Skill.SOUND_QUALITY, SkillLevel.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillLevel.REFERENCE),
            entry(Skill.INTONATION, SkillLevel.HIGH),
            entry(Skill.DYNAMICS, SkillLevel.REFERENCE),
            entry(Skill.COORDINATION, SkillLevel.HIGH),
            entry(Skill.FLICKING, SkillLevel.MEDIUM_HIGH),
            entry(Skill.TRILLS, SkillLevel.LOW),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.MEDIUM_LOW),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.HIGH),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.REFERENCE)));
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
            entry(Skill.LEGATO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.STACCATO, SkillLevel.HIGH),
            entry(Skill.LOW_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.MID_REGISTER, SkillLevel.HIGH),
            entry(Skill.HIGH_REGISTER, SkillLevel.REFERENCE),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.HIGH),
            entry(Skill.SLOW_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.REFERENCE),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.HIGH),
            entry(Skill.ENDURANCE, SkillLevel.REFERENCE),
            entry(Skill.SOUND_QUALITY, SkillLevel.REFERENCE),
            entry(Skill.FLEXIBILITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.INTONATION, SkillLevel.HIGH),
            entry(Skill.DYNAMICS, SkillLevel.REFERENCE),
            entry(Skill.COORDINATION, SkillLevel.REFERENCE),
            entry(Skill.FLICKING, SkillLevel.MEDIUM_HIGH),
            entry(Skill.TRILLS, SkillLevel.REFERENCE),
            entry(Skill.ORNAMENTATION, SkillLevel.HIGH),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.HIGH),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.REFERENCE)));
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
            entry(Skill.LEGATO, SkillLevel.REFERENCE),
            entry(Skill.STACCATO, SkillLevel.LOW),
            entry(Skill.LOW_REGISTER, SkillLevel.AVOID),
            entry(Skill.HIGH_REGISTER, SkillLevel.HIGH),
            entry(Skill.SLOW_TEMPO, SkillLevel.MEDIUM_LOW),
            entry(Skill.MODERATE_TEMPO, SkillLevel.REFERENCE),
            entry(Skill.FAST_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VIRTUOSO_TEMPO, SkillLevel.MEDIUM_LOW),
            entry(Skill.ENDURANCE, SkillLevel.HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.HIGH),
            entry(Skill.INTONATION, SkillLevel.HIGH),
            entry(Skill.DYNAMICS, SkillLevel.HIGH),
            entry(Skill.TRILLS, SkillLevel.LOW),
            entry(Skill.ORNAMENTATION, SkillLevel.MEDIUM_LOW),
            entry(Skill.HALF_HOLE_TECHNIQUE, SkillLevel.LOW),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.RHYTHMIC_COMPLEXITY, SkillLevel.LOW),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.MEDIUM_LOW),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.REFERENCE)));
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
            entry(Skill.LOW_REGISTER, SkillLevel.LOW),
            entry(Skill.HIGH_REGISTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.VERY_HIGH_REGISTER, SkillLevel.LOW),
            entry(Skill.MODERATE_TEMPO, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FAST_TEMPO, SkillLevel.HIGH),
            entry(Skill.SOUND_QUALITY, SkillLevel.MEDIUM_HIGH),
            entry(Skill.FLEXIBILITY, SkillLevel.REFERENCE),
            entry(Skill.INTONATION, SkillLevel.HIGH),
            entry(Skill.DYNAMICS, SkillLevel.MEDIUM_HIGH),
            entry(Skill.COORDINATION, SkillLevel.REFERENCE),
            entry(Skill.FLICKING, SkillLevel.MEDIUM_HIGH),
            entry(Skill.TRILLS, SkillLevel.MEDIUM_LOW),
            entry(Skill.CONTEMPORARY_TECHNIQUES, SkillLevel.AVOID),
            entry(Skill.TECHNICAL_CHARACTER, SkillLevel.MEDIUM_HIGH),
            entry(Skill.EXPRESSIVE_CHARACTER, SkillLevel.HIGH)));
  }

}