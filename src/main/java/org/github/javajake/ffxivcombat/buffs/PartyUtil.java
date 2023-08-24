package org.github.javajake.ffxivcombat.buffs;

import java.util.List;
import org.github.javajake.ffxivcombat.character.PlayableCharacter;

public final class PartyUtil {

  private PartyUtil() {
  }

  public static List<StatsMultiplier> computePartyBuffGiven(PlayableCharacter... partyMembers) {
    if (partyMembers.length > 8) {
      throw new IllegalArgumentException("Party members cannot exceed 8: " + partyMembers.length);
    }

    final int tank = 0;
    final int healer = 1;
    final int melee = 2;
    final int ranged = 3;
    final int caster = 4;
    final boolean[] has = new boolean[5];
    for (PlayableCharacter partyMember : partyMembers) {
      switch (partyMember.jobMod()) {
        case PLD, WAR, DRK, GNB -> has[tank] = true;
        case WHM, SCH, AST -> has[healer] = true;
        case MNK, DRG, NIN, SAM -> has[melee] = true;
        case BRD, MCH, DNC -> has[ranged] = true;
        case BLM, SMN, RDM -> has[caster] = true;
      }
    }
    int uniqueRoles = 0;
    for (boolean role : has) {
      if (role) {
        uniqueRoles++;
      }
    }

    if (partyMembers.length <= 1) {
      uniqueRoles = 0;
    }

    return List.of(
        new StatsMultiplier("Party Bonus", (uniqueRoles/100.0), StatsMultiplier.Stat.STRENGTH),
        new StatsMultiplier("Party Bonus", (uniqueRoles/100.0), StatsMultiplier.Stat.DEXTERITY),
        new StatsMultiplier("Party Bonus", (uniqueRoles/100.0), StatsMultiplier.Stat.VITALITY),
        new StatsMultiplier("Party Bonus", (uniqueRoles/100.0), StatsMultiplier.Stat.INTELLIGENCE),
        new StatsMultiplier("Party Bonus", (uniqueRoles/100.0), StatsMultiplier.Stat.MIND)
    );
  }
}
