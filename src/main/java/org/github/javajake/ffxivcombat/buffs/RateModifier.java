package org.github.javajake.ffxivcombat.buffs;

/**
 * Changes a rate by a given amount, such as critical hit rate.
 *
 * @param name   a human-readable name of the buff
 * @param amount how much the rate is increased or decreased (example: 0.1 = +10%, -0.1 = -10%)
 * @param rate   the particular rate that is being adjusted
 */
public record RateModifier(String name, double amount, Rate rate) implements Buff {
  /**
   * The types of rates that can be adjusted.
   */
  public enum Rate {
    ALL,
    CRITICAL_RATE,
    DIRECT_HIT_RATE
  }
}
