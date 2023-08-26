package org.github.javajake.ffxivcombat.math.akhmorning;

import org.github.javajake.ffxivcombat.character.PlayableCharacter;
import org.github.javajake.ffxivcombat.constants.JobMod;

public class FunctionsMathUtil {

  private FunctionsMathUtil() {
  }

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

  public static int getSpeedStat(PlayableCharacter character) {
    final JobMod.MainStat mainStat = character.jobMod().mainStatName();
    return switch (mainStat) {
      case STR, DEX -> character.skillSpeed();
      case INT, MND -> character.spellSpeed();
      default -> throw new IllegalStateException("BUG: Unknown speed stat for: " + mainStat);
    };
  }

  public static int getHealingPowerStat(PlayableCharacter character) {
    if (character.jobMod() == JobMod.SMN) {
      // Yea, SMN gets the short end on this one
      return character.mind();
    } else {
      return getAttackPowerStat(character);
    }
  }
}
