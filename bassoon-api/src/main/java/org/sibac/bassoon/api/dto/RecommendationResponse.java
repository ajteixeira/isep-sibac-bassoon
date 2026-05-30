package org.sibac.bassoon.api.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sibac.bassoon.model.Accompaniment;
import org.sibac.bassoon.model.Era;
import org.sibac.bassoon.output.FiredRule;

/**
 * JSON response body for POST /recommend.
 *
 * <p>Each recommended work includes catalog metadata and an LLM-generated justification.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponse {

  private List<RecommendedWork> recommendations;

  @Data
  @NoArgsConstructor
  public static class RecommendedWork {
    private double workId;
    private String workName;
    private String composer;
    private Era era;
    private String country;
    private int difficulty;
    private Accompaniment accompaniment;
    private double score;
    private String videoLink;
    private String prerequisite;
    private double initialScore;
    private List<FiredRule> firedRules;
    private String justification;
  }
}
