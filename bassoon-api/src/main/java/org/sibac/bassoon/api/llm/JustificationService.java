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
        "O Telemann e acessivel e destaca-se pelo staccato. O baixo continuo"
        + "alinha com a preferencia do professor, embora o legato tenha"
        + "melhores alternativas e o flicking seja desaconselhado. A epoca"
        + "barroca repete a ultima estudada, o que lhe tira alguma prioridade."

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
          text != null && !text.isBlank() ? clean(text) : fallback()
      );
    }

    LOG.info("Justifications filled for {} works", works.size());
  }

  private String buildPrompt(
      List<RecommendationResponse.RecommendedWork> works, RecommendationRequest request) {

    StringBuilder sb = new StringBuilder();
    sb.append("Recomendei estas obras para um aluno de fagote. Preciso de uma justificacao\n");
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
          case REFERENCE -> facts.add("Excelente para " + label + " (obra de referencia).");
          case VERY_SUITABLE -> facts.add("Muito boa para " + label + ".");
          case SUITABLE -> facts.add("Boa para " + label + ".");
          case MODERATE -> facts.add("Razoavel para " + label + ".");
          case WEAK -> facts.add("Fraca para " + label + ".");
          case UNSUITABLE -> facts.add("Ma para " + label + ".");
          case TOTALLY_UNSUITABLE -> facts.add("Pessima para " + label + ".");
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
        facts.add("Estudar \"" + prereq.getName() + "\" (" + prereq.getComposer()
            + ") antes pode ser uma boa preparacao para esta obra.");
      }
    }

    for (var other : allWorks) {
      if (other == w) continue;
      Work otherWork = catalog.byId(other.getWorkId());
      if (otherWork != null && otherWork.getPrerequisiteId() == work.getId()) {
        facts.add("Boa preparacao para \"" + otherWork.getName() + "\" ("
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
    text = text.replaceAll("(?i)convem estudar com um professor[^.]*\\.?\\s*", "");
    text = text.replaceAll("(?i)estudar com um professor[^.]*\\.?\\s*", "");
    text = text.replaceAll("(?i)para aproveitar ao maximo[^.]*\\.?\\s*", "");
    text = text.replaceAll("(?i)sem duvida[^.]*\\.?\\s*", "");
    text = text.replaceAll("(?i)certamente[^.]*\\.?\\s*", "");
    text = text.replaceAll("(?i)com certeza[^.]*\\.?\\s*", "");
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
