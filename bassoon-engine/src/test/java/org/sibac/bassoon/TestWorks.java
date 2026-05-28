package org.sibac.bassoon;

import java.util.List;
import java.util.Map;
import org.sibac.bassoon.model.Accompaniment;
import org.sibac.bassoon.model.DifficultyLevel;
import org.sibac.bassoon.model.Era;
import org.sibac.bassoon.model.Skill;
import org.sibac.bassoon.model.SkillLevel;
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
   * <p>Baroque Solo Piece: dif 1, BAROQUE, SOLO, TRILLS=HIGH, no prereq.
   * Classical Piano Piece: dif 3, CLASSICAL, PIANO, LEGATO=HIGH, prereq = Baroque Solo.
   * Romantic Orchestral Piece: dif 6, ROMANTIC, ORCHESTRA, DYNAMICS=NONE, no prereq.
   */
  public static List<Work> catalog() {
    return List.of(
        // id=10: easy, baroque, solo -> passes fuzzy for intermediate (intermediate+easy=medium)
        new Work(
            ID_BAROQUE_SOLO,
            BAROQUE_SOLO,
            "Test Composer A",
            Era.BAROQUE,
            "Germany",
            Accompaniment.SOLO,
            DifficultyLevel.LEVEL_1,
            "",
            -1,
            Map.ofEntries(
                entry(Skill.TRILLS, SkillLevel.HIGH),
                entry(Skill.LEGATO, SkillLevel.LOW),
                entry(Skill.STACCATO, SkillLevel.LOW))),

        // id=20: medium, classical, piano -> passes fuzzy (intermediate+medium=high); prereq=10
        new Work(
            ID_CLASSICAL_PIANO,
            CLASSICAL_PIANO,
            "Test Composer B",
            Era.CLASSICAL,
            "Austria",
            Accompaniment.PIANO,
            DifficultyLevel.LEVEL_3,
            "",
            ID_BAROQUE_SOLO,
            Map.ofEntries(
                entry(Skill.TRILLS, SkillLevel.MEDIUM),
                entry(Skill.LEGATO, SkillLevel.HIGH),
                entry(Skill.STACCATO, SkillLevel.MEDIUM))),

        // id=30: hard, romantic, orchestra -> does NOT pass fuzzy (intermediate+hard=low < 0.4)
        new Work(
            ID_ROMANTIC_ORCH,
            ROMANTIC_ORCH,
            "Test Composer C",
            Era.ROMANTIC,
            "France",
            Accompaniment.ORCHESTRA,
            DifficultyLevel.LEVEL_6,
            "",
            -1,
            Map.ofEntries(
                entry(Skill.TRILLS, SkillLevel.HIGH),
                entry(Skill.LEGATO, SkillLevel.HIGH),
                entry(Skill.DYNAMICS, SkillLevel.NONE))));
  }
}
