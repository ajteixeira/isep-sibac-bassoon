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
      Es um professor de fagote. Para cada obra, recebes o que o professor
      pediu e o que a obra oferece. Escreve um paragrafo natural — como se
      falasses com um colega, nao como um robo a listar factos.

      Cobre todos os pontos recebidos: skills, acompanhamento, epoca,
      pre-requisitos. Mostra o balanco entre pontos fortes e fracos.

      NAO inventes. NAO uses frases feitas ("e uma obra que", "permite
      desenvolver", "ajuda a"). NAO uses adjetivos vagos.
      Portugues europeu, COM ACENTOS. Escreve "não", nunca "nao".

      Exemplo:
        Factos: Dificuldade muito acessivel. Ponto forte: Staccato. Trabalha Legato mas
        ha melhores. Desaconselhada para Flicking. Acompanhamento Baixo
        continuo (preferido). ATENCAO: Barroco — perdeu prioridade.
      Resposta:
        "O Telemann e acessivel e destaca-se pelo staccato. O baixo continuo\n"
        + "alinha com a preferencia do professor, embora o legato tenha\n"
        + "melhores alternativas e o flicking seja desaconselhado. A epoca\n"
        + "barroca repete a ultima estudada, o que lhe tira alguma prioridade."

      Responde APENAS com JSON:
      {"justificacoes": [{"obra": <numero>, "texto": "<justificacao>"}]}
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
    addReversePrereqFacts(factsByWork, works, catalog);
    String userPrompt = buildPrompt(works, factsByWork);
    LOG.debug("Calling Groq with prompt of {} chars", userPrompt.length());

    String response = groq.generateJson(SYSTEM_INSTRUCTION, userPrompt);
    Map<Integer, String> byNumber = parse(response);

    for (int i = 0; i < works.size(); i++) {
      String text = byNumber.get(i + 1);
      if (text != null && !text.isBlank()) {
        works.get(i).setJustification(clean(text));
      } else {
        works.get(i).setJustification(fallback());
      }
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
      Work work = catalog.byId(w.getWorkId());
      if (work != null) {
        result.put(w.getWorkName(), buildFacts(work, request));
      }
    }
    return result;
  }

  private List<String> buildFacts(Work work, RecommendationRequest request) {
    List<String> facts = new ArrayList<>();

    facts.add("Dificuldade " + PtLabels.difficulty(work.getDifficultyLevel().getValue()) + ".");

    if (request.getSkills() != null) {
      for (var s : request.getSkills()) {
        var level = work.getSkillLevel(s.getSkill());
        String label = PtLabels.skill(s.getSkill());
        switch (level) {
          case REFERENCE -> facts.add("Excelente para " + label + " (obra de referencia).");
          case HIGH -> facts.add("Muito boa para " + label + ".");
          case MEDIUM_HIGH -> facts.add("Boa para " + label + ".");
          case MEDIUM -> facts.add("Razoavel para " + label + ".");
          case MEDIUM_LOW -> facts.add("Fraca para " + label + ".");
          case LOW -> facts.add("Ma para " + label + ".");
          case AVOID -> facts.add("Pessima para " + label + ".");
          default -> {}
        }
      }
    }

    if (request.getAccompaniments() != null) {
      boolean preferred =
          request.getAccompaniments().stream()
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
        facts.add("ATENCAO: " + PtLabels.era(work.getEra())
            + " — mesma epoca da ultima obra. Perdeu prioridade.");
      } else {
        facts.add("Epoca " + PtLabels.era(work.getEra())
            + " — diferente da ultima estudada. Favorece variedade.");
      }
    }

    if (work.getPrerequisiteId() >= 0) {
      Work prereq = catalog.byId(work.getPrerequisiteId());
      if (prereq != null) {
        facts.add("Estudar \"" + prereq.getName()
            + "\" (" + prereq.getComposer() + ") antes pode ser uma boa preparacao para esta obra.");
      }
    }

    return facts;
  }

  /**
   * Adds reverse prerequisite facts: if this work is a prerequisite for another
   * recommended work, mention it (explains why it appears first in the list).
   */
  private void addReversePrereqFacts(
      Map<String, List<String>> factsByWork,
      List<RecommendationResponse.RecommendedWork> works,
      WorkCatalog catalog) {

    for (var w : works) {
      Work work = catalog.byId(w.getWorkId());
      if (work == null) continue;

      // Find which recommended works depend on this one
      for (var other : works) {
        if (other == w) continue;
        Work otherWork = catalog.byId(other.getWorkId());
        if (otherWork != null && otherWork.getPrerequisiteId() == work.getId()) {
          List<String> facts = factsByWork.get(w.getWorkName());
          if (facts != null) {
            facts.add("Boa preparacao para \"" + otherWork.getName()
                + "\" (" + otherWork.getComposer() + ") — por isso aparece primeiro.");
          }
        }
      }
    }
  }

  /** Builds the prompt with each work and its facts in a natural, conversational format. */
  private String buildPrompt(
      List<RecommendationResponse.RecommendedWork> works, Map<String, List<String>> factsByWork) {

    StringBuilder sb = new StringBuilder();
    sb.append("Recomendei estas obras para um aluno de fagote. Preciso de uma justificacao\n");
    sb.append("curta para cada uma. Tom direto, natural. NADA de frases feitas.\n\n");

    for (int i = 0; i < works.size(); i++) {
      var w = works.get(i);
      sb.append("Obra ").append(i + 1).append(": ").append(w.getWorkName());
      sb.append(" (")
          .append(w.getComposer())
          .append(", ")
          .append(PtLabels.era(w.getEra()))
          .append(", dificuldade ")
          .append(PtLabels.difficulty(w.getDifficulty()))
          .append("/6, ")
          .append(PtLabels.accompaniment(w.getAccompaniment()))
          .append(")\n");

      List<String> facts = factsByWork.getOrDefault(w.getWorkName(), List.of());
      for (String fact : facts) {
        sb.append("  ").append(fact).append("\n");
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

  /** Removes forbidden cliches and generic padding the model insists on using. */
  private static String clean(String text) {
    text = text
        .replace("e uma obra que ", "")
        .replace("permite desenvolver ", "trabalha ")
        .replace("ajuda a ", "")
        .replace("fornece ", "oferece ")
        .replace("constitui ", "")
        .replace("alem disso, ", "")
        .replace("no entanto, ", "")
        .replace("por outro lado, ", "")
        .replace("interessante", "util")
        .replace("  ", " ");
    // Remove generic padding phrases
    text = text.replaceAll("(?i)convem estudar com um professor[^.]*\\.?\\s*", "");
    text = text.replaceAll("(?i)estudar com um professor[^.]*\\.?\\s*", "");
    text = text.replaceAll("(?i)para aproveitar ao maximo[^.]*\\.?\\s*", "");
    text = text.replaceAll("(?i)sem duvida[^.]*\\.?\\s*", "");
    text = text.replaceAll("(?i)certamente[^.]*\\.?\\s*", "");
    text = text.replaceAll("(?i)com certeza[^.]*\\.?\\s*", "");
    // Fix missing Portuguese accents
    text = text.replace("acao", "ação").replace("cao ", "ção ")
        .replace("Nao ", "Não ").replace("nao ", "não ")
        .replace(" so ", " só ").replace(" So ", " Só ")
        .replace("epoca", "época").replace("Epoca", "Época");
    return text.trim();
  }

  private String fallback() {
    return "Obra recomendada pelo sistema. A justificacao automatica nao esta disponivel de momento.";
  }
}
