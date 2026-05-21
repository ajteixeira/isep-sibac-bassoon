package org.sibac.bassoon.model;

/**
 * Representa uma obra recomendada como resultado final do sistema.
 *
 * E produzida em Java (nao por regras) apos o fireAllRules(),
 * quando se recolhem as Hypothesis de candidatura e se calcula
 * o score final ponderado pelas competencias.
 *
 * TODO: score e calculado no Main combinando:
 *   - CF de candidatura da obra (vem das regras)
 *   - CF das competencias da obra na KB
 *   - CF das competencias indicadas pelo professor
 *   - peso por posicao de prioridade (1a competencia pesa mais)
 */
public class Recommendation {

    private String nomeObra;
    private double score;       // score final calculado em Java
    private String justificacao; // texto explicativo para o professor

    // TODO: adicionar flag de pre-requisito se necessario

    public Recommendation(String nomeObra, double score, String justificacao) {
        this.nomeObra = nomeObra;
        this.score = score;
        this.justificacao = justificacao;
    }

    public String getNomeObra()      { return nomeObra; }
    public double getScore()         { return score; }
    public String getJustificacao()  { return justificacao; }

    @Override
    public String toString() {
        return "Recomendacao[" + nomeObra + ", score=" + String.format("%.3f", score) + "]\n  " + justificacao;
    }
}
