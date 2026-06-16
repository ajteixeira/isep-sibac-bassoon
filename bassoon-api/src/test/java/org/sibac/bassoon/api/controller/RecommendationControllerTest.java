package org.sibac.bassoon.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
 * <p>JustificationService is mocked so the tests never call the Groq API
 */
@SpringBootTest
@AutoConfigureMockMvc
class RecommendationControllerTest {

  @Autowired
  private MockMvc mockMvc;

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
  void aboveRangeStudentLevelReturns400() throws Exception {
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
  void belowRangeStudentLevelReturns400() throws Exception {
    // below the @DecimalMin("1.5") lower bound
    String body = """
        {
          "studentLevel": 1.0,
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
  void invalidMotivationReturns400() throws Exception {
    String body = """
        {
          "studentLevel": 3.5,
          "motivation": "XPTO",
          "skills": [{"skill": "LEGATO", "cf": 0.9}]
        }
        """;
    mockMvc.perform(post("/recommend")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest());
  }

  @Test
  void validRequestReturnsRankedRecommendations() throws Exception {
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
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.recommendations").isArray())
        .andExpect(jsonPath("$.recommendations").isNotEmpty())
        .andExpect(jsonPath("$.recommendations[0].workName").exists())
        .andExpect(jsonPath("$.recommendations[0].score").isNumber());

    verify(justificationService).fillJustifications(any(), any());
  }
}
