package org.sibac.bassoon.cf;

/**
 * MYCIN certainty factor combination formula.
 *
 * <p>Combines two CFs that support the same conclusion. Commutative and associative,
 * so rule firing order does not affect the final result.
 *
 * <pre>
 *   both &gt;= 0:        oldCf + newCf * (1 - oldCf)
 *   both &lt;= 0:        oldCf + newCf * (1 + oldCf)
 *   opposite signs:   (oldCf + newCf) / (1 - min(|oldCf|, |newCf|))
 * </pre>
 */
public final class Mycin {

  private Mycin() {}

  public static double combine(double oldCf, double newCf) {
    if (oldCf >= 0 && newCf >= 0) {
      return oldCf + newCf * (1 - oldCf);
    }
    if (oldCf <= 0 && newCf <= 0) {
      return oldCf + newCf * (1 + oldCf);
    }
    return (oldCf + newCf) / (1 - Math.min(Math.abs(oldCf), Math.abs(newCf)));
  }
}
