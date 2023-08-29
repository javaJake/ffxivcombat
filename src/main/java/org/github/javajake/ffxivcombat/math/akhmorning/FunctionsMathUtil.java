package org.github.javajake.ffxivcombat.math.akhmorning;

import org.github.javajake.ffxivcombat.character.PlayableCharacter;
import org.github.javajake.ffxivcombat.constants.JobMod;

/**
 * Utility math functions for {@link Functions} that makes it easier to run the math.
 */
public class FunctionsMathUtil {

  private FunctionsMathUtil() {
  }

  /**
   * Retrieves the correct "attack power" stat given the character's job.
   *
   * @param character the character to get the correct attack power stat for
   * @return the attack power stat's value
   */
  public static int getAttackPowerStat(PlayableCharacter character) {
    final JobMod.MainStat mainStat = character.jobMod().mainStatName();
    return switch (mainStat) {
      case STR -> character.strength();
      case DEX -> character.dexterity();
      case INT -> character.intelligence();
      case MND -> character.mind();
      default -> throw new IllegalStateException("BUG: Unknown power stat for: " + mainStat);
    };
  }

  /**
   * Retrieves the correct "speed" stat given the character's job.
   *
   * @param character the character to get the correct speed stat for
   * @return the speed stat's value
   */
  public static int getSpeedStat(PlayableCharacter character) {
    final JobMod.MainStat mainStat = character.jobMod().mainStatName();
    return switch (mainStat) {
      case STR, DEX -> character.skillSpeed();
      case INT, MND -> character.spellSpeed();
      default -> throw new IllegalStateException("BUG: Unknown speed stat for: " + mainStat);
    };
  }

  /**
   * Retireves the correct "healing power" stat given the character's job.
   *
   * @param character the character to get the correct healing power stat for
   * @return the healing power stat's value
   */
  public static int getHealingPowerStat(PlayableCharacter character) {
    if (character.jobMod() == JobMod.SMN) {
      // Yea, SMN gets the short end on this one
      return character.mind();
    } else {
      return getAttackPowerStat(character);
    }
  }
}
