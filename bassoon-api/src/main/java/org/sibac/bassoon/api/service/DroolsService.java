package org.sibac.bassoon.api.service;

import java.util.List;
import org.sibac.bassoon.RecommendationEngine;
import org.sibac.bassoon.api.dto.RecommendationRequest;
import org.sibac.bassoon.api.dto.RecommendationResponse;
import org.sibac.bassoon.model.Work;
import org.sibac.bassoon.output.Recommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Runs the inference engine and enriches results with catalog metadata.
 *
 * <p>Converts the DTO into engine inputs, runs the pipeline (fuzzy + Drools + ordering +
 * prerequisites), and wraps each raw {@link Recommendation} with composer, era, video link,
 * etc. from the catalog.
 */
@Service
public class DroolsService {

  private static final Logger LOG = LoggerFactory.getLogger(DroolsService.class);

  private final RecommendationEngine engine;
  private final WorkCatalog catalog;

  public DroolsService(WorkCatalog catalog) {
    this.engine = new RecommendationEngine();
    this.catalog = catalog;
    LOG.info("DroolsService initialized with {} works in catalog", catalog.all().size());
  }

  public List<RecommendationResponse.RecommendedWork> recommend(RecommendationRequest request) {

    List<RecommendationEngine.SkillInput> skills = request.getSkills() == null ? List.of() :
        request.getSkills().stream()
            .map(s -> new RecommendationEngine.SkillInput(s.getSkill(), s.getCf()))
            .toList();

    List<RecommendationEngine.AccompanimentInput> accompaniments = request.getAccompaniments() == null ? List.of() :
        request.getAccompaniments().stream()
            .map(a -> new RecommendationEngine.AccompanimentInput(a.getType(), a.getCf()))
            .toList();

    List<Recommendation> results = engine.run(
        catalog.all(),
        request.getStudentLevel(),
        request.getMotivation(),
        skills,
        request.getLastEra(),
        accompaniments);

    List<RecommendationResponse.RecommendedWork> recommendations =
        results.stream().map(rec -> {
          RecommendationResponse.RecommendedWork dto =
              new RecommendationResponse.RecommendedWork();
          dto.setWorkId(rec.getWorkId());
          dto.setWorkName(rec.getWorkName());
          dto.setScore(rec.getScore());
          dto.setInitialScore(rec.getInitialScore());
          dto.setFiredRules(rec.getFiredRules());

          Work work = catalog.byId(rec.getWorkId());
          if (work != null) {
            dto.setComposer(work.getComposer());
            dto.setEra(work.getEra());
            dto.setCountry(work.getCountry());
            dto.setDifficulty(work.getDifficultyLevel().getValue());
            dto.setAccompaniment(work.getAccompaniment());
            if (work.getVideoLink() != null && !work.getVideoLink().isEmpty()) {
              dto.setVideoLink(work.getVideoLink());
            }
            if (work.getPrerequisiteId() >= 0) {
              Work prereq = catalog.byId(work.getPrerequisiteId());
              if (prereq != null) {
                dto.setPrerequisite(prereq.getName());
              }
            }
          }
          return dto;
        }).toList();

    LOG.info("Recommendation generated: {} works", recommendations.size());
    return recommendations;
  }
}
