package org.sibac.bassoon.api.dto;

import org.sibac.bassoon.model.Competencia;
import org.sibac.bassoon.model.Epoca;
import org.sibac.bassoon.model.Motivacao;
import org.sibac.bassoon.model.NivelAluno;

import java.util.List;

/**
 * Input do professor enviado pelo Vue via POST /recommend.
 *
 * Exemplo de JSON:
 * {
 *   "nivelAluno": "INTERMEDIO",
 *   "cfNivelAluno": 0.80,
 *   "nivelIncerteza": "AVANCADO",
 *   "competencias": [
 *     { "competencia": "STACCATO", "cf": 0.50 },
 *     { "competencia": "LEGATO",   "cf": 0.40 }
 *   ],
 *   "motivacao": "NEUTRA",
 *   "cfMotivacao": 0.60,
 *   "motivacaoIncerteza": "ALTA",
 *   "ultimoPeriodo": "BARROCO"
 * }
 */
public class RecommendationRequest {

    // --- obrigatorio ---
    private NivelAluno nivelAluno;
    private double cfNivelAluno;

    // --- opcional: direcao da incerteza do nivel ---
    private NivelAluno nivelIncerteza;

    // --- obrigatorio: pelo menos 1 competencia ---
    private List<CompetenciaInput> competencias;

    // --- opcional ---
    private Motivacao motivacao;
    private double cfMotivacao;
    private Motivacao motivacaoIncerteza;
    private Epoca ultimoPeriodo;

    // --- getters e setters ---
    // TODO: gerar com IDE ou Lombok

    public static class CompetenciaInput {
        private Competencia competencia;
        private double cf;

        public Competencia getCompetencia() { return competencia; }
        public void setCompetencia(Competencia competencia) { this.competencia = competencia; }
        public double getCf() { return cf; }
        public void setCf(double cf) { this.cf = cf; }
    }

    public NivelAluno getNivelAluno() { return nivelAluno; }
    public void setNivelAluno(NivelAluno nivelAluno) { this.nivelAluno = nivelAluno; }
    public double getCfNivelAluno() { return cfNivelAluno; }
    public void setCfNivelAluno(double cfNivelAluno) { this.cfNivelAluno = cfNivelAluno; }
    public NivelAluno getNivelIncerteza() { return nivelIncerteza; }
    public void setNivelIncerteza(NivelAluno nivelIncerteza) { this.nivelIncerteza = nivelIncerteza; }
    public List<CompetenciaInput> getCompetencias() { return competencias; }
    public void setCompetencias(List<CompetenciaInput> competencias) { this.competencias = competencias; }
    public Motivacao getMotivacao() { return motivacao; }
    public void setMotivacao(Motivacao motivacao) { this.motivacao = motivacao; }
    public double getCfMotivacao() { return cfMotivacao; }
    public void setCfMotivacao(double cfMotivacao) { this.cfMotivacao = cfMotivacao; }
    public Motivacao getMotivacaoIncerteza() { return motivacaoIncerteza; }
    public void setMotivacaoIncerteza(Motivacao motivacaoIncerteza) { this.motivacaoIncerteza = motivacaoIncerteza; }
    public Epoca getUltimoPeriodo() { return ultimoPeriodo; }
    public void setUltimoPeriodo(Epoca ultimoPeriodo) { this.ultimoPeriodo = ultimoPeriodo; }
}
