package org.sibac.bassoon.model;

import java.util.Map;

/**
 * A work from the bassoon repertoire.
 *
 * <p>Each work is inserted into the Drools session before {@code fireAllRules()}.
 */
public class Work {

  private final double id;
  private final String name;
  private final String composer;
  private final Era era;
  private final String country;
  private final Accompaniment accompaniment;
  private final DifficultyLevel difficultyLevel;
  private final String videoLink;
  private final double prerequisiteId;
  private final Map<Skill, SkillSuitability> skills;

  public Work(
      double id,
      String name,
      String composer,
      Era era,
      String country,
      Accompaniment accompaniment,
      DifficultyLevel difficultyLevel,
      String videoLink,
      double prerequisiteId,
      Map<Skill, SkillSuitability> skills) {
    this.id = id;
    this.name = name;
    this.composer = composer;
    this.era = era;
    this.country = country;
    this.accompaniment = accompaniment;
    this.difficultyLevel = difficultyLevel;
    this.videoLink = videoLink;
    this.prerequisiteId = prerequisiteId;
    this.skills = skills;
  }

  public double getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getComposer() {
    return composer;
  }

  public Era getEra() {
    return era;
  }

  public String getCountry() {
    return country;
  }

  public Accompaniment getAccompaniment() {
    return accompaniment;
  }

  public DifficultyLevel getDifficultyLevel() {
    return difficultyLevel;
  }

  public String getVideoLink() {
    return videoLink;
  }

  public double getPrerequisiteId() {
    return prerequisiteId;
  }

  public SkillSuitability getSkillSuitability(Skill skill) {
    return skills.getOrDefault(skill, SkillSuitability.MODERATE);
  }

  public SkillSuitability skillSuitabilityFor(Object skill) {
    return getSkillSuitability((Skill) skill);
  }

  @Override
  public String toString() {
    return "Work[" + name + " (" + composer + "), " + era + ", " + country + ", " + accompaniment;
  }
}
