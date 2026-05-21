package org.sibac.bassoon.model;

/**
 * Epoca/periodo estilistico da obra.
 * Usada na regra R6 para penalizar obras do mesmo periodo
 * da ultima obra estudada pelo aluno, promovendo diversidade estilistica.
 */
public enum Epoca {
    BARROCO,
    CLASSICO,
    ROMANTICO,
    CONTEMPORANEO,
    OUTRO
}
