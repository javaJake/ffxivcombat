package org.github.javajake.ffxivcombat.dawntrail.buffs.util;

import java.util.List;
import org.github.javajake.ffxivcombat.dawntrail.buffs.Stat;
import org.github.javajake.ffxivcombat.dawntrail.character.PlayableCharacter;
import org.github.javajake.ffxivcombat.dawntrail.constants.JobMod;
import org.github.javajake.ffxivcombat.dawntrail.buffs.StatMultiplier;

/**
 * Provides convenient tools for dealing with the parties of characters.
 */
public final class PartyUtil {

  private PartyUtil() {
  }

  /**
   * Computes the correct stat multipliers that should be applied given a particular party.
   *
   * @param partyMembers the list of characters that are in the party
   * @return the stat multipliers that take effect for being in this party
   */
  public static List<StatMultiplier> computePartyBuffGiven(PlayableCharacter... partyMembers) {
    final int pet = -1;
    final int tank = 0;
    final int healer = 1;
    final int melee = 2;
    final int ranged = 3;
    final int caster = 4;
    final boolean[] has = new boolean[5];
    for (PlayableCharacter partyMember : partyMembers) {
      int job = switch (partyMember.jobMod()) {
        case JobMod.DRK_LIVING_SHADOW -> 0;
        case JobMod.PLD, JobMod.WAR, JobMod.DRK, JobMod.GNB -> tank;
        case JobMod.WHM, JobMod.SCH, JobMod.AST -> healer;
        case JobMod.MNK, JobMod.DRG, JobMod.NIN, JobMod.SAM -> melee;
        case JobMod.BRD, JobMod.MCH, JobMod.DNC -> ranged;
        case JobMod.BLM, JobMod.SMN, JobMod.RDM -> caster;
        // Pets are ignored
        case JobMod.MCH_AUTOMATON_QUEEN,
             JobMod.AST_EARTHLY_STAR,
             JobMod.NIN_BUNSHIN -> pet;
      };
      // If job is not a pet
      if (job != pet) {
        has[job] = true;
      }
    }
    int uniqueRoles = 0;
    for (boolean hasRole : has) {
      if (hasRole) {
        uniqueRoles++;
      }
    }

    if (partyMembers.length <= 1) { // NOPMD: 1 as a constant is fine
      uniqueRoles = 0;
    }

    return computePartyBuffGivenUniqueRoles(uniqueRoles);
  }

  /**
   * Computes the correct stat multipliers that should be applied given a set of unique roles.
   *
   * @param uniqueRoles the count of unique roles that are in the party (0 if solo)
   * @return the stat multipliers that take effect for being in this party
   */
  public static List<StatMultiplier> computePartyBuffGivenUniqueRoles(int uniqueRoles) {
    if (uniqueRoles < 0 || uniqueRoles > 5) {
      throw new IllegalArgumentException(
          "uniqueRoles range is 0 - 5. This is out of range: " + uniqueRoles);
    }

    final String buffName = "Party Bonus";
    return List.of(
        new StatMultiplier(buffName, (uniqueRoles / 100.0), Stat.STRENGTH),
        new StatMultiplier(buffName, (uniqueRoles / 100.0), Stat.DEXTERITY),
        new StatMultiplier(buffName, (uniqueRoles / 100.0), Stat.VITALITY),
        new StatMultiplier(buffName, (uniqueRoles / 100.0), Stat.INTELLIGENCE),
        new StatMultiplier(buffName, (uniqueRoles / 100.0), Stat.MIND)
    );
  }
}