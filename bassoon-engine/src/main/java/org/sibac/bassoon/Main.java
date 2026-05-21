package org.sibac.bassoon;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.sibac.bassoon.model.Evidence;
import org.sibac.bassoon.model.Hypothesis;
import org.sibac.bassoon.model.Obra;
import org.sibac.bassoon.model.Recommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Main {

    static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // --- inicializar Drools ---
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kContainer = kieServices.getKieClasspathContainer();
        Results verifyResults = kContainer.verify();
        for (Message m : verifyResults.getMessages()) {
            LOG.info("{}", m);
        }
        KieBase kieBase = kContainer.getKieBase("rulesBase");
        KieSession kSession = kContainer.newKieSession("rulesSession");


        // =====================================================================
        // SECAO 1 - BASE DE CONHECIMENTO (obras)
        // Inserir todas as obras do repertorio antes de disparar as regras.
        // As regras vao consultar estes factos para calcular candidaturas.
        //
        // TODO: criar um metodo separado (ex: KnowledgeBase.load()) para
        //       nao poluir o Main com 31 insercoes
        // =====================================================================

        // TODO: definir o mapa de competencias de cada obra
        // exemplo:
        // Map<String, Double> cfHopi = Map.of(
        //     "staccato",     0.20,
        //     "legato",       0.00,
        //     "tecnicas_contemp", 0.90,
        //     ...
        // );
        // kSession.insert(new Obra("Hopi", "Hersant", "contemporaneo", 5, 0.85, null, 0.0, cfHopi));
        // kSession.insert(new Obra("Sonata Fa menor", "Telemann", "barroco", 3, 0.90, null, 0.0, cfTelemann));
        // ... (31 obras no total)


        // =====================================================================
        // SECAO 2 - INPUT DO PROFESSOR
        // Inserir os factos fornecidos pelo professor para esta consulta.
        // Cada Evidence tem: descricao, valor, CF (certeza do professor).
        //
        // TODO: num sistema real isto viria de uma interface (UI) ou parametros
        // =====================================================================

        // nivel do aluno (obrigatorio)
        // TODO: kSession.insert(new Evidence("nivel_aluno", "intermedio", 0.80));
        // TODO: kSession.insert(new Evidence("nivel_incerteza", "avancado"));  // para qual lado e a duvida

        // competencias prioritarias (pelo menos 1, por ordem de prioridade)
        // TODO: kSession.insert(new Evidence("competencia_1", "staccato", 0.50));
        // TODO: kSession.insert(new Evidence("competencia_2", "legato",   0.40));

        // motivacao do aluno (opcional)
        // TODO: kSession.insert(new Evidence("motivacao", "neutra", 0.60));
        // TODO: kSession.insert(new Evidence("motivacao_incerteza", "alta")); // para qual lado e a duvida

        // ultimo periodo estudado (opcional - para penalizacao de repeticao)
        // TODO: kSession.insert(new Evidence("ultimo_periodo", "barroco"));


        // =====================================================================
        // SECAO 3 - MOTOR DE INFERENCIA
        // As regras DRL disparam aqui:
        //   R1 - mapeia nivel do aluno para faixa de dificuldade (com CF)
        //   R2 - ajusta faixa por motivacao (com CF)
        //   R6 - penaliza obras do mesmo periodo da ultima obra (com CF)
        //   R7 - verifica pre-requisitos pedagogicos (com CF)
        // =====================================================================

        kSession.fireAllRules();


        // =====================================================================
        // SECAO 4 - CALCULO DO SCORE FINAL (R4 + R5 em Java)
        // Apos as regras dispararem, recolher as Hypothesis de candidatura,
        // calcular o score ponderado pelas competencias e ordenar.
        //
        // Score de cada obra =
        //   CF_candidatura * SOMA( peso(i) * cfKb(competencia_i) * cfProfessor(i) )
        //   onde peso(i) = 1.0 / i  (1a competencia pesa mais)
        //
        // TODO: implementar este calculo
        // =====================================================================

        Collection<Hypothesis> candidaturas = (Collection<Hypothesis>)
            kSession.getObjects(new ClassObjectFilter(Hypothesis.class));

        List<Recommendation> recomendacoes = new ArrayList<>();

        // TODO: para cada candidatura, ir buscar a Obra correspondente,
        //       calcular o score final com as competencias do input,
        //       e criar um Recommendation


        // =====================================================================
        // SECAO 5 - APRESENTACAO DOS RESULTADOS
        // Ordenar por score e apresentar ao professor.
        //
        // TODO: aplicar R7 (pre-requisitos) antes de apresentar
        // =====================================================================

        recomendacoes.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        LOG.info("=== OBRAS RECOMENDADAS ===");
        for (Recommendation r : recomendacoes) {
            LOG.info(r.toString());
        }

        kSession.dispose();
    }
}
