package org.sibac.bassoon.api.dto;

import java.util.List;

/**
 * Output devolvido ao Vue com as obras recomendadas e justificacao.
 *
 * Exemplo de JSON devolvido:
 * {
 *   "recomendacoes": [
 *     {
 *       "nomeObra": "Hopi",
 *       "compositor": "P. Hersant",
 *       "epoca": "CONTEMPORANEO",
 *       "dificuldade": 6,
 *       "score": 0.842,
 *       "preRequisito": "Niggun",
 *       "regrasFiredSummary": ["R1.2", "R4.REFERENCIA", "R6"]
 *     },
 *     ...
 *   ],
 *   "justificacao": "Texto gerado pelo LLM explicando as recomendacoes..."
 * }
 */
public class RecommendationResponse {

    private List<ObraRecomendada> recomendacoes;
    private String justificacao;  // gerada pelo JustificationService via LLM

    public RecommendationResponse(List<ObraRecomendada> recomendacoes, String justificacao) {
        this.recomendacoes = recomendacoes;
        this.justificacao = justificacao;
    }

    public List<ObraRecomendada> getRecomendacoes() { return recomendacoes; }
    public String getJustificacao() { return justificacao; }

    public static class ObraRecomendada {
        private String nomeObra;
        private String compositor;
        private String epoca;
        private int dificuldade;
        private double score;
        private String preRequisito;          // null se nao tem
        private List<String> regrasFired;     // lista de regras que contribuiram

        // TODO: gerar com IDE ou Lombok

        public String getNomeObra() { return nomeObra; }
        public void setNomeObra(String nomeObra) { this.nomeObra = nomeObra; }
        public String getCompositor() { return compositor; }
        public void setCompositor(String compositor) { this.compositor = compositor; }
        public String getEpoca() { return epoca; }
        public void setEpoca(String epoca) { this.epoca = epoca; }
        public int getDificuldade() { return dificuldade; }
        public void setDificuldade(int dificuldade) { this.dificuldade = dificuldade; }
        public double getScore() { return score; }
        public void setScore(double score) { this.score = score; }
        public String getPreRequisito() { return preRequisito; }
        public void setPreRequisito(String preRequisito) { this.preRequisito = preRequisito; }
        public List<String> getRegrasFired() { return regrasFired; }
        public void setRegrasFired(List<String> regrasFired) { this.regrasFired = regrasFired; }
    }
}
