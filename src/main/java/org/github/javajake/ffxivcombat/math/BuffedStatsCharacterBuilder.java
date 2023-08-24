package org.github.javajake.ffxivcombat.math;

import java.util.ArrayList;
import java.util.List;
import org.github.javajake.ffxivcombat.buffs.FoodAdjustment;
import org.github.javajake.ffxivcombat.buffs.StatsMultiplier;
import org.github.javajake.ffxivcombat.character.PlayableCharacter;

public class BuffedStatsCharacterBuilder {

  private final PlayableCharacter character;

  private final List<StatsMultiplier> statBuffs;
  private final List<FoodAdjustment> foodBuffs;

  public BuffedStatsCharacterBuilder(PlayableCharacter character) {
    this.character = character;
    this.statBuffs = new ArrayList<>();
    this.foodBuffs = new ArrayList<>();
  }

  public BuffedStatsCharacterBuilder addBuff(StatsMultiplier buff) {
    this.statBuffs.add(buff);
    return this;
  }

  public BuffedStatsCharacterBuilder addBuff(FoodAdjustment buff) {
    this.foodBuffs.add(buff);
    return this;
  }

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

    for (StatsMultiplier statsMultiplier : this.statBuffs) {
      switch (statsMultiplier.stat()) {
        case STRENGTH -> strengthMultiplier += statsMultiplier.multiplier();
        case DEXTERITY -> dexterityMultiplier += statsMultiplier.multiplier();
        case VITALITY -> vitalityMultiplier += statsMultiplier.multiplier();
        case INTELLIGENCE -> intelligenceMultiplier += statsMultiplier.multiplier();
        case MIND -> mindMultiplier += statsMultiplier.multiplier();
        case CRITICAL_HIT -> criticalHitMultiplier += statsMultiplier.multiplier();
        case DETERMINATION -> determinationMultiplier += statsMultiplier.multiplier();
        case DIRECT_HIT -> directHitMultiplier += statsMultiplier.multiplier();
        case SKILL_SPEED -> skillSpeedMultiplier += statsMultiplier.multiplier();
        case SPELL_SPEED -> spellSpeedMultiplier += statsMultiplier.multiplier();
        case DEFENSE -> defenseMultiplier += statsMultiplier.multiplier();
        case MAGIC_DEFENSE -> magicDefenseMultiplier += statsMultiplier.multiplier();
        case TENACITY -> tenacityMultiplier += statsMultiplier.multiplier();
        case PIETY -> pietyMultiplier += statsMultiplier.multiplier();
      }
    }
    for (FoodAdjustment foodAdjustment : this.foodBuffs) {
      switch (foodAdjustment.stat()) {
        case STRENGTH -> strengthAdjustment += foodAdjustment.adjustment();
        case DEXTERITY -> dexterityAdjustment += foodAdjustment.adjustment();
        case VITALITY -> vitalityAdjustment += foodAdjustment.adjustment();
        case INTELLIGENCE -> intelligenceAdjustment += foodAdjustment.adjustment();
        case MIND -> mindAdjustment += foodAdjustment.adjustment();
        case CRITICAL_HIT -> criticalHitAdjustment += foodAdjustment.adjustment();
        case DETERMINATION -> determinationAdjustment += foodAdjustment.adjustment();
        case DIRECT_HIT -> directHitAdjustment += foodAdjustment.adjustment();
        case SPELL_SPEED -> spellSpeedAdjustment += foodAdjustment.adjustment();
        case SKILL_SPEED -> skillSpeedAdjustment += foodAdjustment.adjustment();
      }
    }

    return new PlayableCharacter(
        this.character.name(),
        this.character.jobMod(),
        this.character.levelMod(),
        (int)(this.character.strength() * strengthMultiplier) + strengthAdjustment,
        (int)(this.character.dexterity() * dexterityMultiplier) + dexterityAdjustment,
        (int)(this.character.vitality() * vitalityMultiplier) + vitalityAdjustment,
        (int)(this.character.intelligence() * intelligenceMultiplier) + intelligenceAdjustment,
        (int)(this.character.mind() * mindMultiplier) + mindAdjustment,
        (int)(this.character.criticalHit() * criticalHitMultiplier) + criticalHitAdjustment,
        (int)(this.character.determination() * determinationMultiplier) + determinationAdjustment,
        (int)(this.character.directHit() * directHitMultiplier) + directHitAdjustment,
        (int)(this.character.defense() * defenseMultiplier),
        (int)(this.character.magicDefense() * magicDefenseMultiplier),
        (int)(this.character.skillSpeed() * skillSpeedMultiplier) + skillSpeedAdjustment,
        (int)(this.character.spellSpeed() * spellSpeedMultiplier) + spellSpeedAdjustment,
        (int)(this.character.tenacity() * tenacityMultiplier),
        (int)(this.character.piety() * pietyMultiplier),
        this.character.weaponDamage(),
        this.character.weaponAutoAttack(),
        this.character.weaponDelay(),
        this.character.blockStrength(),
        this.character.blockRate(),
        this.character.totalHP(),
        this.character.fps());
  }

}
