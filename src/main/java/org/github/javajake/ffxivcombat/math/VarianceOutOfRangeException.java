package org.github.javajake.ffxivcombat.math;

import java.io.Serial;

/**
 * Thrown when variance is out of range (0 - 10).
 */
public class VarianceOutOfRangeException extends IllegalArgumentException {

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * Creates a new exception given a bad variance.
   *
   * @param outOfRangeVariance the bad variance input that caused the exception
   */
  public VarianceOutOfRangeException(final double outOfRangeVariance) {
    super("Variance range is 0 - 10. This is out of range: " + outOfRangeVariance);
  }
}
