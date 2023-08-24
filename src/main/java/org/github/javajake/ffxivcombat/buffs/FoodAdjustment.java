package org.github.javajake.ffxivcombat.buffs;

public record FoodAdjustment(String name, int adjustment, Stat stat) implements Buff {

  public enum Stat {
    STRENGTH,
    DEXTERITY,
    VITALITY,
    INTELLIGENCE,
    MIND,
    CRITICAL_HIT,
    DETERMINATION,
    DIRECT_HIT,
    SPELL_SPEED,
    SKILL_SPEED
  }
}
