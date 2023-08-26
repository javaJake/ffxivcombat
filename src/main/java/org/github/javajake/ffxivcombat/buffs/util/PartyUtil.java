package org.github.javajake.ffxivcombat.buffs.util;

import java.util.List;
import org.github.javajake.ffxivcombat.buffs.Stat;
import org.github.javajake.ffxivcombat.buffs.StatMultiplier;
import org.github.javajake.ffxivcombat.character.PlayableCharacter;

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
  @SuppressWarnings("PMD.DataflowAnomalyAnalysis") // has array is triggering this deprecated check
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
        case DRK_LIVING_SHADOW -> 0;
        case PLD, WAR, DRK, GNB -> tank;
        case WHM, SCH, AST -> healer;
        case MNK, DRG, NIN, SAM -> melee;
        case BRD, MCH, DNC -> ranged;
        case BLM, SMN, RDM -> caster;
        // Pets are ignored
        case MCH_AUTOMATON_QUEEN,
            AST_EARTHLY_STAR,
            NIN_BUNSHIN -> pet;
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
