package org.github.javajake.ffxivcombat.buffs;

/**
 * A type of buff that is a multiplier on the damage dealt.
 *
 * @param name       a human-readable name of the buff
 * @param multiplier how much the damage is increased or decreased (example: 1.1 = 10%, 0.9 = -10%)
 * @param type       the type of damage that the multiplier applies to
 */
public record DamageMultiplier(String name, double multiplier, Type type) implements Buff {
  /**
   * The type of damage that the multiplier can apply to.
   */
  public enum Type {
    ALL,
    PHYSICAL,
    MAGICAL
  }
}
