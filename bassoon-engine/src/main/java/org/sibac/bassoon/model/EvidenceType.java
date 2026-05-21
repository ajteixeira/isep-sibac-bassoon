package org.sibac.bassoon.model;

/**
 * Tipos de Evidence que o professor pode fornecer como input.
 *
 * Cada tipo indica que campo da consulta esta a ser especificado.
 * O valor correspondente deve usar o enum adequado:
 *
 *   NIVEL_ALUNO         -> valor: NivelAluno      (obrigatorio)
 *   NIVEL_INCERTEZA     -> valor: NivelAluno      (opcional - para que lado aponta a duvida)
 *   COMPETENCIA_1       -> valor: Competencia     (obrigatorio - competencia prioritaria)
 *   COMPETENCIA_2       -> valor: Competencia     (opcional)
 *   COMPETENCIA_3       -> valor: Competencia     (opcional)
 *   MOTIVACAO           -> valor: Motivacao       (opcional)
 *   MOTIVACAO_INCERTEZA -> valor: Motivacao       (opcional - para que lado aponta a duvida)
 *   ULTIMO_PERIODO      -> valor: Epoca           (opcional - para penalizacao R6)
 */
public enum EvidenceType {
    NIVEL_ALUNO,
    NIVEL_INCERTEZA,
    COMPETENCIA_1,
    COMPETENCIA_2,
    COMPETENCIA_3,
    MOTIVACAO,
    MOTIVACAO_INCERTEZA,
    ULTIMO_PERIODO
}
