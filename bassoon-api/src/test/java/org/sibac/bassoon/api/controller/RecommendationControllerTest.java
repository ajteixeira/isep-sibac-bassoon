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
 * <p>JustificationService is mocked to avoid real Groq API calls.
 */
@SpringBootTest
@AutoConfigureMockMvc
class RecommendationControllerTest {

  @Autowired
  private MockMvc mockMvc;

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
