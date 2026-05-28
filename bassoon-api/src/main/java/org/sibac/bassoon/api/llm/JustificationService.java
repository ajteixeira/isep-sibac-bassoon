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
 *
 * <p>A single call (rather than one per work) lets the model relate works to each other
 * (e.g. moving from basso continuo to orchestra) and is cheaper.
 */
@Service
public class JustificationService {

  private static final Logger LOG = LoggerFactory.getLogger(JustificationService.class);

  private final GroqClient groq;
  private final WorkCatalog catalog;
  private final ObjectMapper json = new ObjectMapper();

  public JustificationService(@Value("${groq.api.key}") String apiKey, WorkCatalog catalog) {
    this.groq = new GroqClient(apiKey);
    this.catalog = catalog;
    LOG.info("JustificationService initialized with Groq");
  }

  private static final String SYSTEM_INSTRUCTION =
      """
      Es um assistente de um professor de fagote do ensino superior em Portugal. Escreves
      justificacoes pedagogicas curtas para obras que um sistema pericial recomendou.

      Regras de escrita:
      - Escreve em portugues europeu (PT-PT), nao portugues do Brasil.
      - Texto corrido, 2 a 3 frases por obra. Sem titulos, sem markdown, sem listas, sem negrito.
      - Refere-te a cada obra pelo nome. Nao numeres as obras nem escrevas "Obra 1" ou "Trabalho 1".
      - Baseia-te SO nos factos dados de cada obra. Nao inventes competencias, niveis nem dados.
      - Nunca escrevas codigos internos do sistema (por exemplo "skill HIGH", "era penalty" ou
        "accompaniment match"); explica por palavras tuas o que significam.
      - Usa sempre os termos em portugues que aparecem nos factos (por exemplo "baixo continuo",
        nunca "basso continuo").
      - Quando um facto mencionar "penalizacao", "desvantagem" ou "desaconselhada",
        explica-o como um ponto negativo ou uma limitacao. Nunca o transformes em algo positivo
        nem uses palavras como "continuidade" ou "interessante" para descrever uma penalizacao.

      Responde APENAS com um objeto JSON valido, sem texto antes nem depois, com este formato:
      {"justificacoes": [{"obra": <numero>, "texto": "<justificacao>"}]}
      onde <numero> e o numero da obra tal como aparece no pedido.
      """;

  /**
   * Generates and writes the justification for each recommended work.
   *
   * @param works the recommended works, in order
   * @param request the original recommendation request (skills, accompaniments, era, etc.)
   */
  public void fillJustifications(
      List<RecommendationResponse.RecommendedWork> works, RecommendationRequest request) {

    if (works == null || works.isEmpty()) {
      return;
    }

    Map<String, List<String>> factsByWork = buildFactsByWork(works, request);
    String userPrompt = buildPrompt(works, factsByWork);
    LOG.debug("Calling Groq with prompt of {} chars", userPrompt.length());

    String response = groq.generateJson(SYSTEM_INSTRUCTION, userPrompt);
    Map<Integer, String> byNumber = parse(response);

    for (int i = 0; i < works.size(); i++) {
      String text = byNumber.get(i + 1);
      works.get(i).setJustification(text != null && !text.isBlank() ? text : fallback());
    }

    LOG.info("Justifications filled for {} works", works.size());
  }

  /**
   * Builds a list of facts (in Portuguese) for each recommended work, translating internal
   * rule codes into concrete phrases about skills, accompaniment, era, and prerequisites.
   */
  private Map<String, List<String>> buildFactsByWork(
      List<RecommendationResponse.RecommendedWork> works, RecommendationRequest request) {

    Map<String, List<String>> result = new HashMap<>();
    for (var w : works) {
      Work work = catalog.byName(w.getWorkName());
      if (work != null) {
        result.put(w.getWorkName(), buildFacts(work, request));
      }
    }
    return result;
  }

  private List<String> buildFacts(Work work, RecommendationRequest request) {
    List<String> facts = new ArrayList<>();

    facts.add("Dificuldade " + work.getDifficultyLevel().getValue() + " em 6.");

    if (request.getSkills() != null) {
      for (var s : request.getSkills()) {
        var level = work.getSkillLevel(s.getSkill());
        facts.add(
            "A obra "
                + PtLabels.skillLevelPhrase(level)
                + " a competencia "
                + PtLabels.skill(s.getSkill())
                + ".");
      }
    }

    if (request.getAccompaniments() != null) {
      boolean preferred =
          request.getAccompaniments().stream()
              .anyMatch(a -> a.getType() == work.getAccompaniment());
      if (preferred) {
        facts.add(
            "Usa um acompanhamento preferido pelo professor: "
                + PtLabels.accompaniment(work.getAccompaniment())
                + ".");
      }
    }

    if (request.getLastEra() != null && request.getLastEra() == work.getEra()) {
      facts.add(
          "E do mesmo periodo ("
              + PtLabels.era(work.getEra())
              + ") da ultima obra estudada, por isso recebeu uma ligeira penalizacao para"
              + " favorecer a variedade de epocas.");
    }

    if (work.getPrerequisiteId() >= 0) {
      Work prereq = catalog.byId(work.getPrerequisiteId());
      if (prereq != null) {
        facts.add(
            "Tem como pre-requisito a obra \""
                + prereq.getName()
                + "\", que convem estudar antes.");
      }
    }

    return facts;
  }

  /** Builds the prompt with each work numbered and its facts listed. */
  private String buildPrompt(
      List<RecommendationResponse.RecommendedWork> works, Map<String, List<String>> factsByWork) {

    StringBuilder sb = new StringBuilder();
    sb.append("O sistema pericial recomendou estas obras de fagote, por ordem de adequacao.\n");
    sb.append("Escreve uma justificacao pedagogica curta para cada uma, em JSON.\n\n");

    for (int i = 0; i < works.size(); i++) {
      var w = works.get(i);
      sb.append("Obra ").append(i + 1).append(": ").append(w.getWorkName());
      sb.append(" (")
          .append(w.getComposer())
          .append(", ")
          .append(PtLabels.era(w.getEra()))
          .append(", dificuldade ")
          .append(w.getDifficulty())
          .append(" em 6, acompanhamento ")
          .append(PtLabels.accompaniment(w.getAccompaniment()))
          .append(")\n");

      sb.append("Factos:\n");
      List<String> facts = factsByWork.getOrDefault(w.getWorkName(), List.of());
      if (facts.isEmpty()) {
        sb.append("- (sem factos adicionais)\n");
      } else {
        for (String fact : facts) {
          sb.append("- ").append(fact).append("\n");
        }
      }
      sb.append("\n");
    }

    return sb.toString();
  }

  /** Parses the LLM JSON response into a map of work-number to justification text. */
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
    return "Obra recomendada pelo sistema. A justificacao automatica nao esta disponivel de momento.";
  }
}
