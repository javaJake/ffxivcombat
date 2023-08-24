package org.github.javajake.ffxivcombat.buffs;

public record DamageMultiplier(String name, double multiplier, Type type) implements Buff {

  public enum Type {
    ALL,
    PHYSICAL,
    MAGICAL
  }

  public DamageMultiplier(String name, double multiplier) {
    this(name, multiplier, Type.ALL);
  }

  public static final DamageMultiplier ARCANA_CARD = new DamageMultiplier("Minor Arcana", 1.08);
  public static final DamageMultiplier BROTHERHOOD = new DamageMultiplier("Brotherhood", 1.05, Type.PHYSICAL);
  public static final DamageMultiplier DEVOTION = new DamageMultiplier("Devotion", 1.05);
  public static final DamageMultiplier DIVINATION = new DamageMultiplier("Divination", 1.06);
  public static final DamageMultiplier EMBOLDEN = new DamageMultiplier("Embolden", 1.10, Type.MAGICAL);
  public static final DamageMultiplier MAGE_BALLAD = new DamageMultiplier("Mage's Ballad", 1.01);
  public static final DamageMultiplier SEAL_CARD = new DamageMultiplier("Card", 1.06);
  public static final DamageMultiplier STANDARD_FINISH = new DamageMultiplier("Standard Finish", 1.05);
  public static final DamageMultiplier TECHNICAL_FINISH = new DamageMultiplier("Technical Finish", 1.05);
  public static final DamageMultiplier MUG = new DamageMultiplier("Mug", 1.05);
  public static final DamageMultiplier TRICK_ATTACK = new DamageMultiplier("Trick Attack", 1.10);
}
