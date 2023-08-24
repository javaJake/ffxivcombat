package org.github.javajake.ffxivcombat.actions;

public record DamageAction (Type type, int potency) {
  public enum Type {
    PHYSICAL,
    MAGICAL
  }
}
