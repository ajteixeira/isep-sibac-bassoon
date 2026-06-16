package org.sibac.bassoon.fuzzy;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

import static java.util.Objects.requireNonNull;

/**
 * Opens the fuzzy membership function charts from {@code suitability.fcl}
 * using JFuzzyChart (same approach as the course instructor's example).
 */
public class FuzzyChartGenerator {

  public static void main(String[] args) {
    String path = requireNonNull(FuzzyChartGenerator.class.getResource("/fuzzy/suitability.fcl")).getPath();
    FIS fis = FIS.load(path, true);
    JFuzzyChart.get().chart(fis);
  }
}
