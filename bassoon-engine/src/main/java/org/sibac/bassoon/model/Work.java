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
  private final Map<Skill, SkillLevel> skills;

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
      Map<Skill, SkillLevel> skills) {
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

  // returns the skill level for a given skill; MEDIUM if not defined
  public SkillLevel getSkillLevel(Skill skill) {
    return skills.getOrDefault(skill, SkillLevel.MEDIUM);
  }

  // Drools-friendly overload: accepts Object (avoids casts in DRL constraints)
  public SkillLevel skillLevelFor(Object skill) {
    return getSkillLevel((Skill) skill);
  }

  @Override
  public String toString() {
    return "Work[" + name + " (" + composer + "), " + era + ", " + country + ", " + accompaniment;
  }
}
