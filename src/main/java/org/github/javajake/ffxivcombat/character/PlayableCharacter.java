package org.github.javajake.ffxivcombat.character;

import org.github.javajake.ffxivcombat.constants.JobMod;
import org.github.javajake.ffxivcombat.constants.LevelMod;

/**
 * Represents all possible desirable metadata for a playable character.
 *
 * @param name             the human-readable name for the character
 * @param jobMod           the correct {@link JobMod} for this character
 * @param levelMod         the correct {@link LevelMod} given the character's level
 * @param strength         strength stat in the character window, solo and without buffs
 * @param dexterity        dexterity stat in the character window, solo and without buffs
 * @param vitality         vitality stat in the character window, solo and without buffs
 * @param intelligence     intelligence stat in the character window, solo and without buffs
 * @param mind             mind stat in the character window, solo and without buffs
 * @param criticalHit      critical hit stat in the character window, solo and without buffs
 * @param determination    determination stat in the character window, solo and without buffs
 * @param directHit        direct hit stat in the character window, solo and without buffs
 * @param defense          defense stat in the character window, solo and without buffs
 * @param magicDefense     magic defense stat in the character window, solo and without buffs
 * @param skillSpeed       skill speed stat in the character window, solo and without buffs
 * @param spellSpeed       spell speed stat in the character window, solo and without buffs
 * @param tenacity         tenacity stat in the character window, solo and without buffs
 * @param piety            piety stat in the character window, solo and without buffs
 * @param weaponDamage     the weapon damage in the equipped weapon's tooltip
 * @param weaponAutoAttack the weapon auto attack in the equipped weapon's tooltip
 * @param weaponDelay      the weapon delay in the equipped weapon's tooltip
 * @param blockStrength    the block strength in the equipped shield's tooltip, or 0 otherwise
 * @param blockRate        the block rate in the equipped shield's tooltip, or 0 otherwise
 * @param fps              the framerate the user's game is running at during combat
 */
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
    int fps) {

}
