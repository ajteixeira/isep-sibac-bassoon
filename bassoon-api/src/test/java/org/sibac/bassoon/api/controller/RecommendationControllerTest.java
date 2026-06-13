package org.sibac.bassoon.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.sibac.bassoon.api.llm.JustificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for POST /recommend.
 *
 * <p>JustificationService is mocked so the tests never call the real Groq API
 * (no network, no key needed) — they exercise the controller + validation + engine only.
 */
@SpringBootTest
@AutoConfigureMockMvc
class RecommendationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  // mocked: avoids a real Groq call; fillJustifications becomes a no-op
  @MockBean
  private JustificationService justificationService;

  @Test
  void missingStudentLevelReturns400() throws Exception {
    String body = """
        {"skills": [], "motivation": "NEUTRAL"}
        """;
    mockMvc.perform(post("/recommend")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest());
  }

  @Test
  void outOfRangeStudentLevelReturns400() throws Exception {
    // studentLevel must be within [1.5, 5.5] (@DecimalMin/@DecimalMax on the DTO)
    String body = """
        {
          "studentLevel": 9.0,
          "motivation": "NEUTRAL",
          "skills": [{"skill": "LEGATO", "cf": 0.9}]
        }
        """;
    mockMvc.perform(post("/recommend")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest());
  }

  @Test
  void validRequestReturns200() throws Exception {
    String body = """
        {
          "studentLevel": 3.5,
          "motivation": "NEUTRAL",
          "skills": [{"skill": "LEGATO", "cf": 0.9}]
        }
        """;
    mockMvc.perform(post("/recommend")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk());
  }
}
