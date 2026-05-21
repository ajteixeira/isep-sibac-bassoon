package org.sibac.bassoon.model;

/**
 * Nivel de adequacao de uma obra para trabalhar uma competencia especifica.
 *
 * Corresponde à escala definida pelo perito (Tabela 6 do relatorio):
 *
 *   REFERENCIA  ->  valor original:  1.0
 *   ALTA        ->  valor original:  0.5 a 0.9
 *   MEDIA       ->  valor original:  0.1 a 0.4
 *   BAIXA       ->  valor original: -0.4 a  0.0
 *   NENHUMA     ->  valor original: -0.5 a -1.0  (penaliza)
 *
 * O CF correspondente a cada nivel e definido nas regras DRL via @CF,
 * nao neste enum. Este enum e apenas uma classificacao.
 */
public enum NivelCompetencia {
    REFERENCIA,  // obra de referencia para esta competencia       -> regra com @CF(1.00)
    ALTA,        // muito recomendada para trabalhar a competencia -> regra com @CF(0.70)
    MEDIA,       // trabalha a competencia mas ha escolhas melhores -> regra com @CF(0.30)
    BAIXA,       // neutra ou pouco adequada                       -> sem regra (nao contribui)
    NENHUMA      // desaconselhada ou sem presenca da competencia   -> regra com @CF(-0.50)
}
