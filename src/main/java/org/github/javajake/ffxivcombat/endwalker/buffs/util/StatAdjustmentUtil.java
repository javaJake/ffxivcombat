package org.github.javajake.ffxivcombat.endwalker.buffs.util;

import org.github.javajake.ffxivcombat.endwalker.buffs.Stat;
import org.github.javajake.ffxivcombat.endwalker.buffs.StatAdjustment;
import org.github.javajake.ffxivcombat.endwalker.buffs.StatMultiplier;
import org.github.javajake.ffxivcombat.endwalker.character.PlayableCharacter;

/**
 * Provides convenient tools for dealing with stat adjustment buffs.
 */
public final class StatAdjustmentUtil {
  private StatAdjustmentUtil() {
  }

  /**
   * Create a new stat adjustment buff that represents the way that a food stat adjustment works.
   * <ol>
   *   <li>The stat in question (say, strength) is retrieved from the character.</li>
   *   <li>The food's stat multiplier is applied to find the maximum adjustment.</li>
   *   <li>The maximum adjustment is compared against the capped adjustment. Whichever is lower
   *   wins.</li>
   *   <li>A new stat adjustment buff with this new capped amount.</li>
   * </ol>
   *
   * @param character     the character whose stats are being buffed
   * @param multiplier    the food buff at its maximum potential
   * @param adjustmentCap the cap on how much the food buff can
   * @return the stat adjustment calculated from the capped stat multiplier
   */
  public static StatAdjustment capFoodBuff(
      final PlayableCharacter character,
      final StatMultiplier multiplier,
      final int adjustmentCap) {

    final int characterStatToAdjust = Stat.getCharacterStat(
        character,
        multiplier.stat());

    final int maximumAmount = (int) (characterStatToAdjust * multiplier.multiplier());

    return new StatAdjustment(multiplier.name(),
        Math.min(adjustmentCap, maximumAmount),
        multiplier.stat());
  }

}
