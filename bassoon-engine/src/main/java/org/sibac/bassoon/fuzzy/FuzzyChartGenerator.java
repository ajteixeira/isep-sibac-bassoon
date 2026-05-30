package org.sibac.bassoon.fuzzy;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

import static java.util.Objects.requireNonNull;

/**
 * Opens the fuzzy membership function charts from {@code suitability.fcl}
 * using JFuzzyChart (same approach as the course instructor's example).
 *
 * <p>Run with: {@code mvn exec:java -pl bassoon-engine
 * -Dexec.mainClass="org.sibac.bassoon.fuzzy.FuzzyChartGenerator"}
 */
public class FuzzyChartGenerator {

  public static void main(String[] args) {
    String path = requireNonNull(FuzzyChartGenerator.class.getResource("/fuzzy/suitability.fcl")).getPath();
    FIS fis = FIS.load(path, true);
    JFuzzyChart.get().chart(fis);
  }
}
