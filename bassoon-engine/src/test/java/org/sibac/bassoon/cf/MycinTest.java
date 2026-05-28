package org.sibac.bassoon.cf;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Tests the MYCIN combination formula in isolation (no Drools). */
class MycinTest {

  private static final double TOLERANCE = 1e-9;

  @Test
  void bothPositive() {
    // 0.8 + 0.5 * (1 - 0.8) = 0.9
    assertEquals(0.9, Mycin.combine(0.8, 0.5), TOLERANCE);
  }

  @Test
  void bothNegative() {
    // -0.6 + (-0.4) * (1 + (-0.6)) = -0.6 - 0.16 = -0.76
    assertEquals(-0.76, Mycin.combine(-0.6, -0.4), TOLERANCE);
  }

  @Test
  void oppositeSigns() {
    // (0.8 + (-0.5)) / (1 - min(0.8, 0.5)) = 0.3 / 0.5 = 0.6
    assertEquals(0.6, Mycin.combine(0.8, -0.5), TOLERANCE);
  }

  @Test
  void zeroIsIdentity() {
    assertEquals(0.63, Mycin.combine(0.0, 0.63), TOLERANCE);
    assertEquals(-0.45, Mycin.combine(0.0, -0.45), TOLERANCE);
  }

  @Test
  void isCommutative() {
    assertEquals(Mycin.combine(0.7, 0.4), Mycin.combine(0.4, 0.7), TOLERANCE);
  }
}
