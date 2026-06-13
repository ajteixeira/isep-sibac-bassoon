package org.sibac.bassoon.api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import org.sibac.bassoon.model.Accompaniment;
import org.sibac.bassoon.model.Era;
import org.sibac.bassoon.model.Motivation;
import org.sibac.bassoon.model.Skill;

/** JSON request body for POST /recommend. */
@Data
public class RecommendationRequest {

  @NotNull
  @DecimalMin("1.5")
  @DecimalMax("5.5")
  private Double studentLevel;
  private List<SkillPreference> skills;
  private Motivation motivation;
  private List<AccompanimentPreference> accompaniments;
  private Era lastEra;

  @Data
  public static class SkillPreference {
    private Skill skill;
    private double cf;
  }

  @Data
  public static class AccompanimentPreference {
    private Accompaniment type;
    private double cf;
  }
}
