package org.sibac.bassoon;

import java.util.Collections;
import java.util.List;
import org.sibac.bassoon.kb.KnowledgeBase;
import org.sibac.bassoon.model.Accompaniment;
import org.sibac.bassoon.model.Era;
import org.sibac.bassoon.model.Motivation;
import org.sibac.bassoon.model.Skill;
import org.sibac.bassoon.model.StudentLevel;
import org.sibac.bassoon.output.Recommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Standalone entry point for testing the engine directly (no REST API).
 *
 * <p>Sets up a hardcoded input and prints the recommendations to the log.
 */
public class Main {

  static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {

    StudentLevel studentLevel = StudentLevel.INTERMEDIATE;
    Motivation motivation = Motivation.HIGH;

    List<RecommendationEngine.SkillInput> skills = List.of(
        new RecommendationEngine.SkillInput(Skill.TRILLS, 0.9),
        new RecommendationEngine.SkillInput(Skill.LEGATO, 0.6));

    Era lastEra = Era.BAROQUE;
    List<RecommendationEngine.AccompanimentInput> accompaniments = List.of(
        new RecommendationEngine.AccompanimentInput(Accompaniment.PIANO, 0.8));

    RecommendationEngine engine = new RecommendationEngine();
    List<Recommendation> recommendations = engine.run(
        KnowledgeBase.works(),
        studentLevel, motivation, skills,
        lastEra, accompaniments);

    LOG.info("=== RECOMMENDED WORKS ===");
    for (int i = 0; i < recommendations.size(); i++) {
      Recommendation r = recommendations.get(i);
      LOG.info("  #{}. {} (score={})  rules={}", i + 1, r.getWorkName(),
          String.format("%.3f", r.getScore()), r.getFiredRules());
    }
  }
}
