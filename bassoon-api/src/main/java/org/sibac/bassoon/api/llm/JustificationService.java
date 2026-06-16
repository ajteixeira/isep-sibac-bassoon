package org.sibac.bassoon.api.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sibac.bassoon.api.dto.RecommendationRequest;
import org.sibac.bassoon.api.dto.RecommendationResponse;
import org.sibac.bassoon.api.service.WorkCatalog;
import org.sibac.bassoon.model.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Generates pedagogical justifications via LLM (Groq).
 *
 * <p>Builds one prompt with all recommended works and their facts, calls the LLM in JSON
 * mode, and writes each justification into {@link RecommendationResponse.RecommendedWork}.
 */
@Service
public class JustificationService {

  private static final Logger LOG = LoggerFactory.getLogger(JustificationService.class);

  private final GroqClient groq;
  private final WorkCatalog catalog;
  private final ObjectMapper json;

  public JustificationService(
      @Value("${groq.api.key}") String apiKey, WorkCatalog catalog, ObjectMapper json) {
    this.groq = new GroqClient(apiKey);
    this.catalog = catalog;
    this.json = json;
    LOG.info("JustificationService initialized with Groq");
  }

  private static final String SYSTEM_INSTRUCTION =
      """
      És um professor de fagote. Para cada obra, recebes o que o professor
      pediu e o que a obra oferece. Escreve um parágrafo natural — como se
      falasses com um colega, não como um robot a listar factos.

      Cobre todos os pontos recebidos: skills, acompanhamento, época,
      pré-requisitos. Mostra o balanço entre pontos fortes e fracos.

      NÃO inventes informações, baseia-te apenas nos factos.
      NÃO uses adjetivos vagos.
      Escreve em português europeu, COM ACENTOS. Escreve "não", nunca "nao".

      Exemplo:
        Factos:
          Dificuldade muito acessível.
          Excelente para Staccato (obra de referência).
          Razoável para Legato.
          Pouco adequada para Flicking.
          Acompanhamento Baixo contínuo (preferido pelo professor).
          ATENÇÃO: Barroco — mesma época da última obra. Perdeu prioridade.
        Resposta: Obra acessível. O ponto forte é o staccato, e tem o acompanhamento
        de baixo contínuo, tal como pedido. Para o legato é razoável, mas há outras obras melhores;
        no entanto, para trabalhar o flicking não é adequada. Sendo do período barroco, está a repetir
        a época da última obra estudada, o que torna a obra menos interessante.

      Responde APENAS com JSON:
      {"justificacoes": [{"obra": <numero>, "texto": "<justificacao>"}]}
      """;

  public void fillJustifications(
      List<RecommendationResponse.RecommendedWork> works, RecommendationRequest request) {

    if (works == null || works.isEmpty()) {
      return;
    }

    String prompt = buildPrompt(works, request);
    LOG.debug("Calling Groq with prompt of {} chars", prompt.length());
    String response = groq.generateJson(SYSTEM_INSTRUCTION, prompt);
    Map<Integer, String> byNumber = parse(response);

    for (int i = 0; i < works.size(); i++) {
      String text = byNumber.get(i + 1);
      works.get(i).setJustification(
          text != null && !text.isBlank() ? text.trim() : fallback()
      );
    }

    LOG.info("Justifications filled for {} works", works.size());
  }

  private String buildPrompt(
      List<RecommendationResponse.RecommendedWork> works, RecommendationRequest request) {

    StringBuilder sb = new StringBuilder();
    sb.append("Recomendei estas obras para um aluno de fagote. Preciso de uma justificação\n");
    sb.append("curta para cada uma. Tom direto, natural. NADA de frases feitas.\n\n");

    for (int i = 0; i < works.size(); i++) {
      var w = works.get(i);
      sb.append("Obra ").append(i + 1).append(": ").append(w.getWorkName())
          .append(" (")
          .append(w.getComposer()).append(", ")
          .append(PtLabels.era(w.getEra())).append(", dificuldade ")
          .append(PtLabels.difficulty(w.getDifficulty())).append("/6, ")
          .append(PtLabels.accompaniment(w.getAccompaniment()))
          .append(")\n");

      for (String fact : factsFor(w, works, request)) {
        sb.append("  ").append(fact).append("\n");
      }
      sb.append("\n");
    }

    return sb.toString();
  }

  private List<String> factsFor(
      RecommendationResponse.RecommendedWork w,
      List<RecommendationResponse.RecommendedWork> allWorks,
      RecommendationRequest request) {

    Work work = catalog.byId(w.getWorkId());
    if (work == null) {
      return List.of();
    }

    List<String> facts = new ArrayList<>();

    facts.add("Dificuldade " + PtLabels.difficulty(work.getDifficultyLevel().getValue()) + ".");

    if (request.getSkills() != null) {
      for (var s : request.getSkills()) {
        String label = PtLabels.skill(s.getSkill());
        switch (work.getSkillSuitability(s.getSkill())) {
          case REFERENCE -> facts.add("Excelente para " + label + " (obra de referência).");
          case VERY_SUITABLE -> facts.add("Muito adequada para " + label + ".");
          case SUITABLE -> facts.add("Adequada para " + label + ".");
          case MODERATE -> facts.add("Razoável para " + label + ".");
          case WEAK -> facts.add("Fraca para " + label + ".");
          case UNSUITABLE -> facts.add("Pouco adequada para " + label + ".");
          case TOTALLY_UNSUITABLE -> facts.add("Muito desadequada para " + label + ".");
        }
      }
    }

    if (request.getAccompaniments() != null) {
      boolean preferred = request.getAccompaniments().stream()
          .anyMatch(a -> a.getType() == work.getAccompaniment());
      if (preferred) {
        facts.add("Acompanhamento " + PtLabels.accompaniment(work.getAccompaniment())
            + " (preferido pelo professor).");
      } else {
        String preferredLabel = request.getAccompaniments().stream()
            .map(a -> PtLabels.accompaniment(a.getType()))
            .collect(java.util.stream.Collectors.joining(" ou "));
        facts.add("Acompanhamento " + PtLabels.accompaniment(work.getAccompaniment())
            + " — diferente do preferido (" + preferredLabel + ").");
      }
    }

    if (request.getLastEra() != null) {
      if (request.getLastEra() == work.getEra()) {
        facts.add("ATENÇÃO: " + PtLabels.era(work.getEra())
            + " — mesma época da última obra. Perdeu prioridade.");
      } else {
        facts.add("Época " + PtLabels.era(work.getEra())
            + " — diferente da última estudada. Favorece variedade.");
      }
    }

    if (work.getPrerequisiteId() >= 0) {
      Work prereq = catalog.byId(work.getPrerequisiteId());
      if (prereq != null) {
        facts.add("Estudar \"" + prereq.getName() + "\" (" + prereq.getComposer()
            + ") antes pode ser uma boa preparação para esta obra.");
      }
    }

    for (var other : allWorks) {
      if (other == w) continue;
      Work otherWork = catalog.byId(other.getWorkId());
      if (otherWork != null && otherWork.getPrerequisiteId() == work.getId()) {
        facts.add("Boa preparação para \"" + otherWork.getName() + "\" ("
            + otherWork.getComposer() + ") — por isso aparece primeiro.");
      }
    }

    return facts;
  }

  private Map<Integer, String> parse(String response) {
    Map<Integer, String> result = new HashMap<>();
    try {
      JsonNode root = json.readTree(response);
      JsonNode array = root.get("justificacoes");
      if (array != null && array.isArray()) {
        for (JsonNode item : array) {
          JsonNode number = item.get("obra");
          JsonNode text = item.get("texto");
          if (number != null && text != null) {
            result.put(number.asInt(), text.asText());
          }
        }
      }
    } catch (Exception e) {
      LOG.error("Failed to parse Groq JSON response: {}", response, e);
    }
    return result;
  }

  private String fallback() {
    return "Obra recomendada pelo sistema. A justificação automática não está disponível de momento.";
  }
}
