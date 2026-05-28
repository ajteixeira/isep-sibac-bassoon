package org.sibac.bassoon.fuzzy;

import java.util.Map;
import org.sibac.bassoon.output.Justification;

/**
 * Explainable fuzzy suitability result.
 *
 * <p>The {@code suitability} field is used by the engine. The remaining fields provide
 * an audit trail for justifications.
 */
public record FuzzySuitabilityResult(
    double studentLevel,
    int workDifficulty,
    double suitability,
    Map<String, Double> studentLevelMemberships,
    Map<String, Double> workDifficultyMemberships,
    Justification justification) {}
