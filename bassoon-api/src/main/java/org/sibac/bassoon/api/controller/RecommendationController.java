package org.sibac.bassoon.api.controller;

import org.sibac.bassoon.api.dto.RecommendationRequest;
import org.sibac.bassoon.api.dto.RecommendationResponse;
import org.sibac.bassoon.api.service.DroolsService;
import org.sibac.bassoon.api.llm.JustificationService;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for the recommendation endpoint.
 *
 * <p>Accepts a POST with the student profile and returns a ranked list of works
 * with pedagogical justifications.
 */
@RestController
@RequestMapping("/recommend")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class RecommendationController {

  private final DroolsService droolsService;
  private final JustificationService justificationService;

  public RecommendationController(
      DroolsService droolsService, JustificationService justificationService) {
    this.droolsService = droolsService;
    this.justificationService = justificationService;
  }

  @PostMapping
  public RecommendationResponse recommend(@RequestBody RecommendationRequest request) {
    var works = droolsService.recommend(request);
    justificationService.fillJustifications(works, request);
    return new RecommendationResponse(works);
  }
}
