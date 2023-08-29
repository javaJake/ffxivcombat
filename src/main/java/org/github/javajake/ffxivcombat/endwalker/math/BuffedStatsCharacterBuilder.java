package org.github.javajake.ffxivcombat.endwalker.math;

import java.util.ArrayList;
import java.util.List;
import org.github.javajake.ffxivcombat.endwalker.buffs.StatAdjustment;
import org.github.javajake.ffxivcombat.endwalker.buffs.StatMultiplier;
import org.github.javajake.ffxivcombat.endwalker.character.PlayableCharacter;

/**
 * Creates a new character that's been affected by certain buffs.
 */
public class BuffedStatsCharacterBuilder {

  private final PlayableCharacter character;

  private final List<StatMultiplier> statMultipliers;
  private final List<StatAdjustment> statAdjustments;

  /**
   * Starts a new buffed character.
   *
   * @param character the original character whose stats will be affected.
   */
  public BuffedStatsCharacterBuilder(PlayableCharacter character) {
    this.character = character;
    this.statMultipliers = new ArrayList<>();
    this.statAdjustments = new ArrayList<>();
  }

  /**
   * Adds a stat multiplier to the list of buffs affecting the character.
   *
   * @param buff the stat multiplier to include
   * @return this builder for fluent usage
   */
  public BuffedStatsCharacterBuilder addBuff(StatMultiplier buff) {
    this.statMultipliers.add(buff);
    return this;
  }

  /**
   * Adds a stat adjustment to the list of buffs affecting the character.
   *
   * @param buff the stat adjustment to include
   * @return this builder for fluent usage
   */
  public BuffedStatsCharacterBuilder addBuff(StatAdjustment buff) {
    this.statAdjustments.add(buff);
    return this;
  }

  /**
   * Builds a new character that's had its stats recalculated given the added buffs.
   *
   * @return the new character with updated stats
   */
  public PlayableCharacter build() {
    double strengthMultiplier = 1;
    double dexterityMultiplier = 1;
    double vitalityMultiplier = 1;
    double intelligenceMultiplier = 1;
    double mindMultiplier = 1;
    double criticalHitMultiplier = 1;
    double determinationMultiplier = 1;
    double directHitMultiplier = 1;
    double skillSpeedMultiplier = 1;
    double spellSpeedMultiplier = 1;
    double defenseMultiplier = 1;
    double magicDefenseMultiplier = 1;
    double tenacityMultiplier = 1;
    double pietyMultiplier = 1;

    int strengthAdjustment = 0;
    int dexterityAdjustment = 0;
    int vitalityAdjustment = 0;
    int intelligenceAdjustment = 0;
    int mindAdjustment = 0;
    int criticalHitAdjustment = 0;
    int determinationAdjustment = 0;
    int directHitAdjustment = 0;
    int spellSpeedAdjustment = 0;
    int skillSpeedAdjustment = 0;

    for (StatMultiplier statMultiplier : this.statMultipliers) {
      switch (statMultiplier.stat()) {
        case STRENGTH -> strengthMultiplier += statMultiplier.multiplier();
        case DEXTERITY -> dexterityMultiplier += statMultiplier.multiplier();
        case VITALITY -> vitalityMultiplier += statMultiplier.multiplier();
        case INTELLIGENCE -> intelligenceMultiplier += statMultiplier.multiplier();
        case MIND -> mindMultiplier += statMultiplier.multiplier();
        case CRITICAL_HIT -> criticalHitMultiplier += statMultiplier.multiplier();
        case DETERMINATION -> determinationMultiplier += statMultiplier.multiplier();
        case DIRECT_HIT -> directHitMultiplier += statMultiplier.multiplier();
        case SKILL_SPEED -> skillSpeedMultiplier += statMultiplier.multiplier();
        case SPELL_SPEED -> spellSpeedMultiplier += statMultiplier.multiplier();
        case DEFENSE -> defenseMultiplier += statMultiplier.multiplier();
        case MAGIC_DEFENSE -> magicDefenseMultiplier += statMultiplier.multiplier();
        case TENACITY -> tenacityMultiplier += statMultiplier.multiplier();
        case PIETY -> pietyMultiplier += statMultiplier.multiplier();
        default -> throw new UnsupportedOperationException(
            "BUG: stat not understood: " + statMultiplier.stat());
      }
    }
    for (StatAdjustment statAdjustment : this.statAdjustments) {
      switch (statAdjustment.stat()) {
        case STRENGTH -> strengthAdjustment += statAdjustment.amount();
        case DEXTERITY -> dexterityAdjustment += statAdjustment.amount();
        case VITALITY -> vitalityAdjustment += statAdjustment.amount();
        case INTELLIGENCE -> intelligenceAdjustment += statAdjustment.amount();
        case MIND -> mindAdjustment += statAdjustment.amount();
        case CRITICAL_HIT -> criticalHitAdjustment += statAdjustment.amount();
        case DETERMINATION -> determinationAdjustment += statAdjustment.amount();
        case DIRECT_HIT -> directHitAdjustment += statAdjustment.amount();
        case SPELL_SPEED -> spellSpeedAdjustment += statAdjustment.amount();
        case SKILL_SPEED -> skillSpeedAdjustment += statAdjustment.amount();
        default -> throw new UnsupportedOperationException(
            "BUG: stat not understood: " + statAdjustment.stat());
      }
    }

    return new PlayableCharacter(
        this.character.name(),
        this.character.jobMod(),
        this.character.levelMod(),
        (int) (this.character.strength() * strengthMultiplier) + strengthAdjustment,
        (int) (this.character.dexterity() * dexterityMultiplier) + dexterityAdjustment,
        (int) (this.character.vitality() * vitalityMultiplier) + vitalityAdjustment,
        (int) (this.character.intelligence() * intelligenceMultiplier) + intelligenceAdjustment,
        (int) (this.character.mind() * mindMultiplier) + mindAdjustment,
        (int) (this.character.criticalHit() * criticalHitMultiplier) + criticalHitAdjustment,
        (int) (this.character.determination() * determinationMultiplier) + determinationAdjustment,
        (int) (this.character.directHit() * directHitMultiplier) + directHitAdjustment,
        (int) (this.character.defense() * defenseMultiplier),
        (int) (this.character.magicDefense() * magicDefenseMultiplier),
        (int) (this.character.skillSpeed() * skillSpeedMultiplier) + skillSpeedAdjustment,
        (int) (this.character.spellSpeed() * spellSpeedMultiplier) + spellSpeedAdjustment,
        (int) (this.character.tenacity() * tenacityMultiplier),
        (int) (this.character.piety() * pietyMultiplier),
        this.character.weaponDamage(),
        this.character.weaponAutoAttack(),
        this.character.weaponDelay(),
        this.character.blockStrength(),
        this.character.blockRate(),
        this.character.fps());
  }

}
