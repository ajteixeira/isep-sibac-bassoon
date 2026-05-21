package org.sibac.bassoon.model;

/**
 * Competencias identificadas pelo perito (Tabela 7 do relatorio).
 * Organizadas pelos mesmos grupos definidos nas sessoes de aquisicao
 * de conhecimento.
 */
public enum Competencia {
    // --- Articulacao ---
    LEGATO,
    STACCATO,

    // --- Registo ---
    REGISTO_GRAVE,
    REGISTO_MEDIO,
    REGISTO_AGUDO,
    REGISTO_SOBREAGUDO,

    // --- Tempo de execucao ---
    TEMPO_LENTO,
    TEMPO_MODERADO,
    TEMPO_RAPIDO,
    TEMPO_VIRTUOSO,

    // --- Controlo do som ---
    RESISTENCIA,
    QUALIDADE_SOM,
    FLEXIBILIDADE,
    AFINACAO,
    DINAMICAS,

    // --- Desafios tecnicos ---
    COORDENACAO,
    FLICKING,
    TRILOS,
    ORNAMENTACAO,
    TECNICA_MEIO_BURACO,
    TECNICAS_CONTEMPORANEAS,

    // --- Ritmo ---
    COMPLEXIDADE_RITMICA,

    // --- Caracter ---
    CARACTER_TECNICO,
    CARACTER_EXPRESSIVO
}
