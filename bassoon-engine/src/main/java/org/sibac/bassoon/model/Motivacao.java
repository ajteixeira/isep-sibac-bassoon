package org.sibac.bassoon.model;

/**
 * Estado motivacional do aluno.
 * Influencia a faixa de dificuldade das obras recomendadas (regra R2):
 *   ALTA   -> desloca a faixa +1 (obras mais exigentes)
 *   NEUTRA -> nao altera a faixa
 *   BAIXA  -> desloca a faixa -1 (obras mais acessiveis)
 */
public enum Motivacao {
    ALTA,
    NEUTRA,
    BAIXA
}
