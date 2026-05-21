package org.sibac.bassoon.api.controller;

import org.sibac.bassoon.api.dto.RecommendationRequest;
import org.sibac.bassoon.api.dto.RecommendationResponse;
import org.sibac.bassoon.api.service.DroolsService;
import org.sibac.bassoon.api.service.JustificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoint REST consumido pelo Vue.
 *
 * POST /recommend
 *   Body: RecommendationRequest (JSON)
 *   Response: RecommendationResponse (JSON)
 */
@RestController
@RequestMapping("/recommend")
@CrossOrigin(origins = "http://localhost:5173")  // Vue dev server
public class RecommendationController {

    private final DroolsService droolsService;
    private final JustificationService justificationService;

    public RecommendationController(DroolsService droolsService,
                                    JustificationService justificationService) {
        this.droolsService = droolsService;
        this.justificationService = justificationService;
    }

    @PostMapping
    public RecommendationResponse recommend(@RequestBody RecommendationRequest request) {

        // 1. correr motor Drools (R1, R2, R4, R6) + R5 + R7 em Java
        List<RecommendationResponse.ObraRecomendada> recomendacoes =
                droolsService.recommend(request);

        // 2. gerar justificacao via LLM
        String justificacao = justificationService.generateJustification(recomendacoes);

        return new RecommendationResponse(recomendacoes, justificacao);
    }
}
