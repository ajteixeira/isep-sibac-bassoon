package org.sibac.bassoon;

import java.util.List;
import java.util.Map;
import org.sibac.bassoon.model.Accompaniment;
import org.sibac.bassoon.model.DifficultyLevel;
import org.sibac.bassoon.model.Era;
import org.sibac.bassoon.model.Skill;
import org.sibac.bassoon.model.SkillSuitability;
import org.sibac.bassoon.model.Work;

import static java.util.Map.entry;

/**
 * Stable test works for the engine integration test.
 *
 * <p>These works do NOT depend on the production KnowledgeBase and never change.
 * They cover all pipeline scenarios: fuzzy filter, skills, era penalty,
 * accompaniment match, and prerequisites.
 */
public final class TestWorks {

  private TestWorks() {}

  private static final double ID_BAROQUE_SOLO = 10.0;
  private static final double ID_CLASSICAL_PIANO = 20.0;
  private static final double ID_ROMANTIC_ORCH = 30.0;

  public static final String BAROQUE_SOLO = "Baroque Solo Piece";
  public static final String CLASSICAL_PIANO = "Classical Piano Piece";
  public static final String ROMANTIC_ORCH = "Romantic Orchestral Piece";

  /**
   * Catalog of 3 test works.
   *
   * <p>Baroque Solo Piece: dif 4, BAROQUE, SOLO, TRILLS=VERY_SUITABLE, no prereq.
   * Classical Piano Piece: dif 4, CLASSICAL, PIANO, LEGATO=VERY_SUITABLE, prereq = Baroque Solo.
   * Romantic Orchestral Piece: dif 4, ROMANTIC, ORCHESTRA, DYNAMICS=TOTALLY_UNSUITABLE, no prereq.
   */
  public static List<Work> catalog() {
    return List.of(
        // id=10: medium, baroque, solo -> passes fuzzy for intermediate and advanced
        new Work(
            ID_BAROQUE_SOLO,
            BAROQUE_SOLO,
            "Test Composer A",
            Era.BAROQUE,
            "Germany",
            Accompaniment.SOLO,
            DifficultyLevel.LEVEL_4,
            "",
            -1,
            Map.ofEntries(
                entry(Skill.TRILLS, SkillSuitability.VERY_SUITABLE),
                entry(Skill.LEGATO, SkillSuitability.MODERATE),
                entry(Skill.STACCATO, SkillSuitability.MODERATE))),

        // id=20: medium, classical, piano -> passes fuzzy for intermediate and advanced; prereq=10
        new Work(
            ID_CLASSICAL_PIANO,
            CLASSICAL_PIANO,
            "Test Composer B",
            Era.CLASSICAL,
            "Austria",
            Accompaniment.PIANO,
            DifficultyLevel.LEVEL_4,
            "",
            ID_BAROQUE_SOLO,
            Map.ofEntries(
                entry(Skill.TRILLS, SkillSuitability.MODERATE),
                entry(Skill.LEGATO, SkillSuitability.VERY_SUITABLE),
                entry(Skill.STACCATO, SkillSuitability.MODERATE))),

        // id=30: medium, romantic, orchestra -> passes fuzzy for intermediate and advanced
        new Work(
            ID_ROMANTIC_ORCH,
            ROMANTIC_ORCH,
            "Test Composer C",
            Era.ROMANTIC,
            "France",
            Accompaniment.ORCHESTRA,
            DifficultyLevel.LEVEL_4,
            "",
            -1,
            Map.ofEntries(
                entry(Skill.TRILLS, SkillSuitability.VERY_SUITABLE),
                entry(Skill.LEGATO, SkillSuitability.VERY_SUITABLE),
                entry(Skill.DYNAMICS, SkillSuitability.TOTALLY_UNSUITABLE))));
  }
}
