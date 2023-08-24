package org.github.javajake.ffxivcombat.buffs;

public record RateModifier (String name, double rateChange, Rate rate) implements Buff {

  public static final RateModifier ARMYS_PAEON = new RateModifier("Army's Paeon", 0.03, Rate.DIRECT_HIT_RATE);
  public static final RateModifier BATTLE_VOICE = new RateModifier("Battle Voice", 0.2, Rate.DIRECT_HIT_RATE);
  public static final RateModifier CHAIN = new RateModifier("Chain Stratagem", 0.1, Rate.CRITICAL_RATE);
  public static final RateModifier DEVILMENT = new RateModifier("Devilment", 0.2, Rate.ALL);
  public static final RateModifier WANDERERS_MINUET = new RateModifier("Wanderer's Minuet", 0.02, Rate.CRITICAL_RATE);

  public enum Rate {
    ALL,
    CRITICAL_RATE,
    DIRECT_HIT_RATE
  }
}
