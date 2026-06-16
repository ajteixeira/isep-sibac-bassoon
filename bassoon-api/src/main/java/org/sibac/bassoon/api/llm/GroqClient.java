package org.sibac.bassoon.api.llm;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Minimal HTTP client for the Groq API (OpenAI-compatible).
 *
 * <p>Uses the built-in {@link java.net.http.HttpClient} (Java 11+). The API key is set via
 * {@code application.properties} ({@code groq.api.key}).
 */
public class GroqClient {

  private static final Logger LOG = LoggerFactory.getLogger(GroqClient.class);

  private static final URI ENDPOINT =
      URI.create("https://api.groq.com/openai/v1/chat/completions");
  private static final String MODEL = "llama-3.3-70b-versatile";

  private final String apiKey;
  private final HttpClient http;
  private final ObjectMapper json;

  public GroqClient(String apiKey) {
    this.apiKey = apiKey;
    this.http = HttpClient.newHttpClient();
    this.json = new ObjectMapper();
  }

  /**
   * Calls the Groq API and returns the response content as a String.
   */
  public String generateJson(String systemInstruction, String userPrompt) {
    return call(systemInstruction, userPrompt);
  }

  private String call(String systemInstruction, String userPrompt) {
    try {
      Map<String, Object> body = new HashMap<>();
      body.put("model", MODEL);
      body.put("temperature", 0.3);
      body.put("response_format", Map.of("type", "json_object"));
      body.put(
          "messages",
          List.of(
              Map.of("role", "system", "content", systemInstruction),
              Map.of("role", "user", "content", userPrompt)));

      String bodyJson = json.writeValueAsString(body);

      HttpRequest request = HttpRequest.newBuilder()
          .uri(ENDPOINT)
          .header("Content-Type", "application/json")
          .header("Authorization", "Bearer " + apiKey)
          .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
          .build();

      HttpResponse<String> response = http.send(request,
          HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 200) {
        LOG.error("Groq API error {}: {}", response.statusCode(), response.body());
        return "Error calling Groq API (HTTP " + response.statusCode() + ")";
      }

      return extractText(response.body());

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      LOG.error("Groq API call interrupted", e);
      return "Justification could not be generated: interrupted";
    } catch (Exception e) {
      LOG.error("Groq API call failed", e);
      return "Justification could not be generated: " + e.getMessage();
    }
  }

  /**
   * Extracts the {@code content} field from an OpenAI-style chat completion response:
   * <pre>{@code { "choices": [{ "message": { "content": "..." } }] }}</pre>
   */
  @SuppressWarnings("unchecked")
  private String extractText(String responseBody) {
    try {
      Map<String, Object> root = json.readValue(responseBody, Map.class);
      List<Map<String, Object>> choices =
          (List<Map<String, Object>>) root.get("choices");
      if (choices == null || choices.isEmpty()) {
        return "No choices in Groq response";
      }
      Map<String, Object> message =
          (Map<String, Object>) choices.get(0).get("message");
      if (message == null) {
        return "No message in Groq response";
      }
      String content = (String) message.get("content");
      return content != null ? content : "No content in Groq response";
    } catch (Exception e) {
      LOG.error("Failed to parse Groq response", e);
      return "Failed to parse Groq response";
    }
  }
}
