package org.github.javajake.ffxivcombat.endwalker.math.akhmorning;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import org.github.javajake.ffxivcombat.endwalker.actions.DamageAction;
import org.github.javajake.ffxivcombat.endwalker.character.PlayableCharacter;
import org.github.javajake.ffxivcombat.endwalker.constants.JobMod;
import org.github.javajake.ffxivcombat.endwalker.constants.LevelMod;
import org.junit.jupiter.api.Test;

class DamageAndHealingTest {

  @Test
  void getDirectDamage() {
    // This is primarily a demo of the math.
    final PlayableCharacter ninja = new PlayableCharacter(
        "Ninja",
        JobMod.NIN,
        LevelMod.BY_LEVEL[90],
        0,
        2930,
        3089,
        0,
        0,
        2339,
        1407,
        1547,
        2619,
        2619,
        536,
        400,
        400,
        390,
        126,
        107.52,
        2.56,
        0,
        0,
        60);

    DamageAndHealing damageAndHealing = new DamageAndHealing(
        ninja,
        Collections.emptyList(),
        Collections.emptyList());

    final int directDamage = damageAndHealing.getDirectDamage(
        new DamageAction(DamageAction.Type.PHYSICAL, 220), // Spinning Edge
        true,
        true,
        1.0);

    assertEquals(10465, directDamage,
        "Damage should match expected value");
  }
}