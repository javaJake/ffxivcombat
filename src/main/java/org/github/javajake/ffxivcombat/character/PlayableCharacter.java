package org.github.javajake.ffxivcombat.character;

import java.time.Duration;
import org.github.javajake.ffxivcombat.constants.JobMod;
import org.github.javajake.ffxivcombat.constants.LevelMod;

public record PlayableCharacter(
    String name,
    JobMod jobMod,
    LevelMod levelMod,
    int strength,
    int dexterity,
    int vitality,
    int intelligence,
    int mind,
    int criticalHit,
    int determination,
    int directHit,
    int defense,
    int magicDefense,
    int skillSpeed,
    int spellSpeed,
    int tenacity,
    int piety,
    int weaponDamage,
    double weaponAutoAttack,
    double weaponDelay,
    int blockStrength,
    int blockRate,
    int totalHP,
    int fps) {

}
