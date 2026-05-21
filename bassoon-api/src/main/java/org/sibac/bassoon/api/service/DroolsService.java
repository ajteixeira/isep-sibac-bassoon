package org.sibac.bassoon.api.service;

import org.sibac.bassoon.api.dto.RecommendationRequest;
import org.sibac.bassoon.api.dto.RecommendationResponse;
import org.sibac.bassoon.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servico que encapsula o motor de inferencia Drools.
 *
 * Responsabilidades:
 *   1. Carregar a KB de obras (uma vez, no arranque)
 *   2. Para cada pedido, criar uma sessao Drools, inserir os factos,
 *      correr as regras, e devolver os resultados
 *   3. Aplicar R5 (ordenacao) e R7 (pre-requisitos) em Java
 */
@Service
public class DroolsService {

    // TODO: injectar KieContainer via Spring Bean
    // @Autowired KieContainer kieContainer;

    // TODO: carregar lista de obras da KB no arranque (@PostConstruct)
    // private List<Obra> knowledgeBase;

    /**
     * Corre o motor de inferencia para um dado pedido do professor.
     *
     * Fluxo:
     *   1. Criar nova KieSession
     *   2. Inserir todas as obras da KB
     *   3. Inserir uma Hypothesis("candidatura", nome, 0.0) por obra
     *   4. Converter RecommendationRequest em Evidence e inserir
     *   5. fireAllRules()
     *   6. Recolher Hypothesis da working memory
     *   7. R5 - ordenar por score
     *   8. R7 - verificar pre-requisitos da obra topo
     *   9. Devolver lista de ObraRecomendada com regras que dispararam
     */
    public List<RecommendationResponse.ObraRecomendada> recommend(RecommendationRequest request) {

        // TODO: implementar
        throw new UnsupportedOperationException("DroolsService.recommend() - a implementar");
    }

    // --- metodos auxiliares ---

    // TODO: private List<Evidence> toEvidence(RecommendationRequest request)
    //   converte o DTO em factos Evidence para inserir na sessao

    // TODO: private Obra buscarObra(String nome)
    //   vai buscar uma obra da KB pelo nome (para R7)

    // TODO: private List<ObraRecomendada> aplicarR7(List<ObraRecomendada> ordenadas)
    //   verifica se a obra topo tem pre-requisito e ajusta a lista
}
