package org.sibac.bassoon.model;

import java.util.Map;

/**
 * Representa uma obra do repertorio de fagote na base de conhecimento.
 *
 * Cada obra e inserida no Main antes do fireAllRules() e fica disponivel
 * para todas as regras consultarem.
 *
 * TODO: preencher todas as 31 obras com os valores validados pelo perito (Tabela 9)
 */
public class Obra {

    // --- meta-dados (Tabela 5 do relatorio) ---
    private String nome;
    private String compositor;
    private Epoca epoca;
    private String pais;
    private Acompanhamento acompanhamento;
    private int dificuldade;           // escala 1 a 6
    private double cfDificuldade;      // certeza do perito na classificacao de dificuldade

    // --- pre-requisito pedagogico ---
    private String nomePreRequisito;   // nome da obra pre-requisito, null se nao tem
    private double cfPreRequisito;     // certeza de que o pre-requisito e necessario

    // --- competencias (Tabela 7 do relatorio) ---
    // mapa Competencia -> NivelCompetencia
    // indica em que medida esta obra e util para trabalhar cada competencia
    private Map<Competencia, NivelCompetencia> competencias;

    public Obra(String nome, String compositor, Epoca epoca, String pais,
                Acompanhamento acompanhamento, int dificuldade, double cfDificuldade,
                String nomePreRequisito, double cfPreRequisito,
                Map<Competencia, NivelCompetencia> competencias) {
        this.nome = nome;
        this.compositor = compositor;
        this.epoca = epoca;
        this.pais = pais;
        this.acompanhamento = acompanhamento;
        this.dificuldade = dificuldade;
        this.cfDificuldade = cfDificuldade;
        this.nomePreRequisito = nomePreRequisito;
        this.cfPreRequisito = cfPreRequisito;
        this.competencias = competencias;
    }

    // devolve o nivel de adequacao desta obra para uma competencia especifica
    // se a competencia nao estiver no mapa devolve BAIXA (nao contribui)
    public NivelCompetencia getNivelCompetencia(Competencia competencia) {
        return competencias.getOrDefault(competencia, NivelCompetencia.BAIXA);
    }

    public String getNome()               { return nome; }
    public String getCompositor()         { return compositor; }
    public Epoca getEpoca()               { return epoca; }
    public String getPais()               { return pais; }
    public Acompanhamento getAcompanhamento() { return acompanhamento; }
    public int getDificuldade()           { return dificuldade; }
    public double getCfDificuldade()      { return cfDificuldade; }
    public String getNomePreRequisito()   { return nomePreRequisito; }
    public double getCfPreRequisito()     { return cfPreRequisito; }

    @Override
    public String toString() {
        return "Obra[" + nome + " (" + compositor + "), " +
               epoca + ", dif=" + dificuldade + ", cfDif=" + cfDificuldade + "]";
    }
}
