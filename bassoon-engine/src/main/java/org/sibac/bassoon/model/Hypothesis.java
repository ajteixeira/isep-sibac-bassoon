package org.sibac.bassoon.model;

/**
 * Representa uma conclusao intermédia do motor de inferência.
 *
 * As regras produzem Hypothesis que outras regras podem depois usar.
 *
 * Exemplos de Hypothesis que as regras vao inserir:
 *   - faixa de dificuldade calculada para o aluno
 *     (ex: "faixa_alvo", "3-4", CF=0.72)
 *   - candidatura de uma obra a recomendacao
 *     (ex: "candidatura", "Hopi", CF=0.65)
 *
 * TODO: adicionar outros tipos de Hypothesis conforme as regras forem definidas
 */
public class Hypothesis {

    private String description;  // que conclusao e esta (ex: "faixa_alvo")
    private String value;        // o valor da conclusao (ex: "3-4")
    private double cf;           // certeza desta conclusao, calculada pelo motor

    public Hypothesis(String description, String value, double cf) {
        this.description = description;
        this.value = value;
        this.cf = cf;
    }

    public String getDescription() { return description; }
    public String getValue()       { return value; }
    public double getCf()          { return cf; }
    public void setCf(double cf)   { this.cf = cf; }

    @Override
    public String toString() {
        return "Hypothesis[" + description + "=" + value + ", CF=" + cf + "]";
    }
}
