package org.github.javajake.ffxivcombat.actions;

/**
 * An action that does DPS.
 *
 * @param type    whether it's physical or magical
 * @param potency the tooltip's described potency of the action
 */
public record DamageAction(Type type, int potency) {
  /**
   * The type of damage that can be dealt.
   */
  public enum Type {
    PHYSICAL,
    MAGICAL
  }
}
