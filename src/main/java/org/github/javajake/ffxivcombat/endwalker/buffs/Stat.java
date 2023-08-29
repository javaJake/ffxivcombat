package org.github.javajake.ffxivcombat.endwalker.buffs;

import org.github.javajake.ffxivcombat.endwalker.character.PlayableCharacter;

/**
 * The types of stats that can be affected by stat buffs.
 */
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
  SKILL_SPEED,
  DEFENSE,
  MAGIC_DEFENSE,
  TENACITY,
  PIETY;

  /**
   * Retrieves the current stat of a player.
   *
   * @param character the character whose stats are being referenced
   * @param stat      the stat to retrieve
   * @return the current stat of the character
   */
  public static int getCharacterStat(
      final PlayableCharacter character,
      final Stat stat) {
    return switch (stat) {
      case STRENGTH -> character.strength();
      case DEXTERITY -> character.dexterity();
      case VITALITY -> character.vitality();
      case INTELLIGENCE -> character.intelligence();
      case MIND -> character.mind();
      case CRITICAL_HIT -> character.criticalHit();
      case DETERMINATION -> character.determination();
      case DIRECT_HIT -> character.directHit();
      case SPELL_SPEED -> character.spellSpeed();
      case SKILL_SPEED -> character.skillSpeed();
      case DEFENSE -> character.defense();
      case MAGIC_DEFENSE -> character.magicDefense();
      case TENACITY -> character.tenacity();
      case PIETY -> character.piety();
    };
  }
}
