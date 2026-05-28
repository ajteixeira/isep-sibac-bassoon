package org.sibac.bassoon.api.dto;

import java.util.List;
import lombok.Data;
import org.sibac.bassoon.model.Accompaniment;
import org.sibac.bassoon.model.Era;
import org.sibac.bassoon.model.Motivation;
import org.sibac.bassoon.model.Skill;
import org.sibac.bassoon.model.StudentLevel;

/** JSON request body for POST /recommend. */
@Data
public class RecommendationRequest {

  private StudentLevel studentLevel;
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
