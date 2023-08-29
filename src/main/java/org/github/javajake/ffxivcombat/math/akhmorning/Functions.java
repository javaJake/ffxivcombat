package org.github.javajake.ffxivcombat.math.akhmorning;

import static org.github.javajake.ffxivcombat.math.akhmorning.FunctionsMathUtil.getAttackPowerStat;
import static org.github.javajake.ffxivcombat.math.akhmorning.FunctionsMathUtil.getHealingPowerStat;
import static org.github.javajake.ffxivcombat.math.akhmorning.FunctionsMathUtil.getSpeedStat;

import org.github.javajake.ffxivcombat.character.PlayableCharacter;
import org.github.javajake.ffxivcombat.constants.JobMod;
import org.github.javajake.ffxivcombat.constants.LevelMod;

/**
 * Codified versions of the math in the page
 * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/</a>.
 */
@SuppressWarnings({"checkstyle:AbbreviationAsWordInName", "checkstyle:MethodNameCheck"})
public class Functions {

  private final PlayableCharacter character;
  private final LevelMod levelMod;
  private final JobMod jobMod;

  /**
   * Creates new "Functions" math for a given character.
   *
   * @param character the character to apply the math
   */
  public Functions(PlayableCharacter character) {
    this.character = character;

    // Just for convenience
    this.levelMod = this.character.levelMod();
    this.jobMod = this.character.jobMod();
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#weapon-damage-fwd">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#weapon-damage-fwd</a>.
   *
   * @return the contribution of your weapon’s damage rating
   */
  public int fWD() {
    return (int)
        // f(WD) = ⌊ ( LevelMod[Lv, MAIN] · JobMod[Job, Attribute] / 1000 )
        (Math.floor(levelMod.main() * this.jobMod.mainStat() / 1000.0)
            // + WD ⌋
            + this.character.weaponDamage());
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#attack-power-fap--fmap">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#attack-power-fap--fmap</a>.
   *
   * @return the contribution of either your Attack Power (AP) or Magic Attack Potency (MAP)
   */
  public int fAP() {
    if (levelMod.level() <= LevelMod.ARR_LEVEL) {
      return (int)
          // f(AP) = ⌊ 75
          (Math.floor(75.0
              // · ( AP - Level[Lv, MAIN]) / Level[Lv, MAIN]⌋ + 100
              * (getAttackPowerStat(character) - levelMod.main()) / levelMod.main()) + 100);
    } else if (levelMod.level() <= LevelMod.SB_LEVEL) {
      return (int)
          // f(AP) = ⌊ ( Level - 50 ) · 2.5 + 75 ⌋
          (Math.floor(Math.floor((levelMod.level() - 50.0) * 2.5 + 75.0)
              // · ( AP - Level[Lv, MAIN]) / Level[Lv, MAIN]⌋ + 100
              * (getAttackPowerStat(character) - levelMod.main()) / levelMod.main()) + 100.0);
    } else if (levelMod.level() <= LevelMod.SHB_LEVEL) {
      switch (jobMod) {
        case PLD, WAR, DRK, GNB -> {
          return (int)
              // f(AP) = ⌊ 115
              (Math.floor(115.0
                  // The "340" is assumed to be the main constant from the levelmod
                  // · ( AP - 340 ) / 340 ⌋ + 100
                  * (getAttackPowerStat(character) - levelMod.main()) / levelMod.main()) + 100);
        }
        default -> {
          return (int)
              // f(AP) = ⌊ 165
              (Math.floor(165.0
                  // The "340" is assumed to be the main constant from the levelmod
                  // · ( AP - 340 ) / 340 ⌋ + 100
                  * (getAttackPowerStat(character) - levelMod.main()) / levelMod.main()) + 100);
        }
      }
    } else if (levelMod.level() <= LevelMod.EW_LEVEL) {
      switch (jobMod) {
        case PLD, WAR, DRK, GNB -> {
          return (int)
              // TODO: Undocumented; word of mouth
              (Math.floor(156.0
                  * (getAttackPowerStat(character) - levelMod.main()) / levelMod.main()) + 100);
        }
        default -> {
          return (int)
              // TODO: Undocumented; word of mouth
              (Math.floor(195.0
                  * (getAttackPowerStat(character) - levelMod.main()) / levelMod.main()) + 100);
        }
      }
    } else {
      throw new IllegalStateException(
          "BUG: missing attack power function for level: " + levelMod.level());
    }
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#determination-fdet">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#determination-fdet</a>.
   *
   * @return total Determination’s contribution to damage and healing output as a rational number
   *         (rather than percentage)
   */
  public int fDET() {
    return (int)
        // Endwalker changed the constant from 130 to 140.
        // f(DET) = ⌊ 130 · ( DET - LevelMod[Lv, Main] )
        (Math.floor(140.0 * (this.character.determination() - levelMod.main())
            // / LevelMod[Lv, DIV] + 1000 ⌋
            / levelMod.div() + 1000.0));
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#tenacity-ftnc">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#tenacity-ftnc</a>.
   *
   * @return the impact of your character’s total Tenacity towards damage, mitigation, and healing
   */
  public int fTEN() {
    switch (jobMod) {
      case PLD, WAR, DRK, GNB -> {
        return (int)
            // f(TNC) = ⌊ 100 · ( TNC - LevelMod[Lv, SUB] )
            (Math.floor(100.0 * (this.character.tenacity() - levelMod.sub())
                // / LevelMod[Lv, DIV] + 1000 ⌋
                / levelMod.div() + 1000.0));
      }
      default -> {
        // TODO: this shouldn't be necessary as the above formula should be 1000 for non-tanks
        return 1000;
      }
    }
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#speed-fspd>https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#speed-fspd</a>.
   *
   * @return the bonus granted by Skill Speed or Spell Speed in decimal form
   */
  public int fSPD() {
    return (int)
        // f(SPD) = ⌊ 130 · ( SPD - LevelMod[Lv, SUB] )/ LevelMod[Lv, DIV] + 1000 ⌋
        (Math.floor(130.0 * (getSpeedStat(character) - levelMod.sub()) / levelMod.div() + 1000.0));
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#critical-hit-damage-fcrit">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#critical-hit-damage-fcrit</a>.
   *
   * @return the bonus granted by landing a critical hit
   */
  public int fCRIT() {
    return (int)
        // f(CRIT) = ⌊ 200 · ( CRIT - LevelMod[Lv, SUB] )
        (Math.floor(200.0 * (this.character.criticalHit() - levelMod.sub())
            // / LevelMod[Lv, DIV] + 1400 ⌋
            / levelMod.div() + 1400.0));
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#defense-fdef--fmdef">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#defense-fdef--fmdef</a>.
   *
   * @return how much damage you mitigate through Defense and Magic Defense
   */
  public int fDEF() {
    return (int)
        // f(DEF) = ⌊ 15 · Defense / LevelMod[Lv, DIV] ⌋
        (Math.floor(15.0 * this.character.defense() / levelMod.div()));
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#defense-fdef--fmdef">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#defense-fdef--fmdef</a>.
   *
   * @return how much damage you mitigate through Defense and Magic Defense
   */
  public int fMDEF() {
    return (int)
        // f(MDEF) = ⌊ 15 · Magic Defense / LevelMod[Lv, DIV] ⌋
        (Math.floor(15.0 * this.character.magicDefense() / levelMod.div()));
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#block-strength-fblk">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#block-strength-fblk</a>.
   *
   * @return the percent of damage your block mitigates
   */
  public int fBLK() {
    return (int)
        // f(BLK) = ⌊ 15 · Block Strength / LevelMod[Lv, DIV] ⌋ + 10
        (Math.floor(15.0 * this.character.blockStrength() / levelMod.div()) + 10);
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#auto-attacks-fauto">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#auto-attacks-fauto</a>.
   *
   * @return a decimal multiplier, to either increase or decrease the damage dealt by auto-attacks
   */
  public int fAUTO() {
    return (int)
        // f(AUTO) = ⌊ ( ⌊ LevelMod[Lv, Main] · JobMod[Job, Attribute] / 1000 )
        (Math.floor(Math.floor((levelMod.main() * jobMod.mainStat() / 1000.0)
            // + WD ⌋ · ( Weapon Delay / 3 ) ⌋
            + this.character.weaponDamage()) * (this.character.weaponDelay() / 3.0)));
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#healing-magic-potency-fhmp">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/functions/#healing-magic-potency-fhmp</a>.
   *
   * @return the multiplier through which your Healing Magic Potency affects your healing output
   */
  public int fHMP() {
    if (levelMod.level() < LevelMod.EW_LEVEL) {
      throw new UnsupportedOperationException("HMP below level 90 is unknown");
    } else {
      return (int)
          // TODO: Undocumented; word of mouth
          (Math.floor(569.0
              * (getHealingPowerStat(character) - 390.0) / 1522.0) + 100);
    }
  }
}
