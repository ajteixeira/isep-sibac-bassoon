package org.sibac.bassoon.api.service;

import org.sibac.bassoon.api.dto.RecommendationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servico que gera justificacao em linguagem natural via LLM.
 *
 * Recebe as obras recomendadas e as regras que dispararam,
 * monta um prompt estruturado, e chama a API do LLM escolhido.
 *
 * TODO: decidir entre Claude (Anthropic SDK) ou OpenAI SDK
 *       e adicionar a dependencia correspondente no pom.xml
 */
@Service
public class JustificationService {

    // TODO: injectar chave da API via @Value("${llm.api.key}")
    // private String apiKey;

    /**
     * Gera uma justificacao pedagogica para as obras recomendadas.
     *
     * Exemplo de prompt montado:
     *   "O sistema de recomendacao de repertorio de fagote sugeriu as seguintes obras:
     *    1. Hopi (Hersant, Contemporaneo, dif=6, score=0.84)
     *       Regras aplicadas: nivel intermedio -> faixa 3-4 (R1), competencia STACCATO
     *       nivel REFERENCIA (R4), sem penalizacao de periodo (R6)
     *    2. ...
     *    Com base nestas informacoes, gera uma justificacao clara e pedagogica
     *    para um professor de fagote do ensino superior."
     *
     * @param recomendacoes lista ordenada de obras recomendadas com regras fired
     * @return texto de justificacao gerado pelo LLM
     */
    public String generateJustification(List<RecommendationResponse.ObraRecomendada> recomendacoes) {

        // TODO: implementar
        // 1. montar prompt com obras e regras que dispararam
        // 2. chamar API do LLM
        // 3. devolver texto gerado
        throw new UnsupportedOperationException("JustificationService.generateJustification() - a implementar");
    }

    // TODO: private String buildPrompt(List<ObraRecomendada> recomendacoes)
}
