package org.sibac.bassoon.model;

/**
 * Representa um facto de entrada fornecido pelo professor no momento da consulta.
 *
 * Exemplos de insercao no Main:
 *   new Evidence(EvidenceType.NIVEL_ALUNO,     NivelAluno.INTERMEDIO,  0.80)
 *   new Evidence(EvidenceType.NIVEL_INCERTEZA, NivelAluno.AVANCADO)         // sem CF - e so direcao
 *   new Evidence(EvidenceType.COMPETENCIA_1,   Competencia.STACCATO,   0.50)
 *   new Evidence(EvidenceType.COMPETENCIA_2,   Competencia.LEGATO,     0.40)
 *   new Evidence(EvidenceType.MOTIVACAO,       Motivacao.NEUTRA,       0.60)
 *   new Evidence(EvidenceType.MOTIVACAO_INCERTEZA, Motivacao.ALTA)          // sem CF - e so direcao
 *   new Evidence(EvidenceType.ULTIMO_PERIODO,  Epoca.BARROCO)               // sem CF - e um facto
 */
public class Evidence {

    private EvidenceType description;
    private Object value;   // valor tipado: NivelAluno, Competencia, Motivacao ou Epoca
    private double cf;

    public Evidence(EvidenceType description, Object value, double cf) {
        this.description = description;
        this.value = value;
        this.cf = cf;
    }

    // construtor sem CF para factos sem incerteza (direcoes e ultimo periodo)
    public Evidence(EvidenceType description, Object value) {
        this(description, value, 1.0);
    }

    public EvidenceType getDescription() { return description; }
    public Object getValue()             { return value; }
    public double getCf()                { return cf; }

    @Override
    public String toString() {
        return "Evidence[" + description + "=" + value + ", CF=" + cf + "]";
    }
}
