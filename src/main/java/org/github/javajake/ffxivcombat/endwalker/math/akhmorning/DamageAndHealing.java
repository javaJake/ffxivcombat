package org.github.javajake.ffxivcombat.endwalker.math.akhmorning;

import java.util.List;
import org.github.javajake.ffxivcombat.endwalker.actions.DamageAction;
import org.github.javajake.ffxivcombat.endwalker.buffs.DamageMultiplier;
import org.github.javajake.ffxivcombat.endwalker.constants.JobMod;
import org.github.javajake.ffxivcombat.endwalker.math.VarianceOutOfRangeException;

/**
 * Codified versions of the math in the page
 * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/</a>.
 */
public class DamageAndHealing {

  private final Functions functions;
  private final Parameters parameters;
  private final JobMod jobMod;
  private final List<DamageMultiplier> damageMultipliers;

  /**
   * Creates new "Damage and Healing" math for a given character.
   *
   * @param functions         the "Functions" Ahkmorning math
   * @param parameters        the "Parameters" Ahkmorning math
   * @param jobMod            the JobMod constants of the current character
   * @param damageMultipliers damage multiplier active on the current character
   */
  public DamageAndHealing(
      Functions functions,
      Parameters parameters,
      JobMod jobMod,
      List<DamageMultiplier> damageMultipliers) {
    this.functions = functions;
    this.parameters = parameters;
    this.jobMod = jobMod;
    this.damageMultipliers = damageMultipliers;
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#direct-damage-d">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#direct-damage-d</a>
   * <br><br>
   * This version of the function simply calculates damage done.
   *
   * @param action      the action used to deal damage
   * @param isCrit      whether the damage was a critical hit
   * @param isDirectHit whether the damage was a direct hit
   * @param variance    how "hard" the damage hit this time; valid range is 0 - 10
   * @return the numerical amount of damage done
   */
  public int getDirectDamage(
      DamageAction action,
      boolean isCrit,
      boolean isDirectHit,
      double variance) {
    return getDirectDamage(
        action,
        isCrit ? 1.0 : 0.0,
        isDirectHit ? 1.0 : 0.0,
        variance);
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#direct-damage-d">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#direct-damage-d</a>
   * <br><br>
   * This version of the function calculates average damage done by the action as if you sampled
   * infinitely, to average the effect of critical and direct hits.
   *
   * @param action   the action used to deal damage
   * @param variance how "hard" the damage hit this time; valid range is 0 - 10
   * @return the numerical amount of damage done
   */
  public int getDirectDamage(
      DamageAction action,
      double variance) {
    return getDirectDamage(
        action,
        parameters.pCHR() / 100.0,
        parameters.pDHR() / 100.0,
        variance);
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#direct-damage-d">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#direct-damage-d</a>
   * <br><br>
   * This version of the function allows customization of the critical and direct hit rate to
   * simulate being "lucky" or "unlucky". The function calculates average damage done by the
   * action as if you sampled infinitely, to average the effect of critical and direct hits.
   *
   * @param action          the action used to deal damage
   * @param criticalHitRate the critical hit rate as a fraction (ex: 0.2 is 20%)
   * @param directHitRate   the direct hit rate as a fraction (ex: 0.2 is 20%)
   * @param variance        how "hard" the damage hit this time; valid range is 0 - 10
   * @return the numerical amount of damage done
   */
  public int getDirectDamage(
      DamageAction action,
      double criticalHitRate,
      double directHitRate,
      double variance) {
    final int criticalHitModifier = (int) ((functions.fCRIT() - 1000) * criticalHitRate + 1000);
    final int directHitModifier = (int) ((25 * directHitRate) + 100);

    if (variance < 0 || variance > 10) {
      throw new VarianceOutOfRangeException(variance);
    }
    final double variancePercent = variance + 95;

    int damage1 = (int)
        (Math.floor(Math.floor(Math.floor(
        // D1 = ⌊ Potency × f(ATK) × f(DET) ⌋
        action.potency() * functions.fAP() * functions.fDET())
            //  /100 ⌋ /1000 ⌋
            / 100.0) / 1000.0));
    int damage2 = (int)
        (Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(
        // D2 = ⌊ D1 × f(TNC) ⌋ /1000 ⌋ × f(WD) ⌋ /100 ⌋
        damage1 * functions.fTEN()) / 1000.0) * functions.fWD()) / 100.0)
            // × Trait ⌋ /100 ⌋
            * jobMod.trait()) / 100.0);
    int damage3 = (int)
        (Math.floor(Math.floor(Math.floor(
        // D3 = ⌊ D2 × CRIT? ⌋ /1000 ⌋ × DH? ⌋ /100 ⌋
        damage2 * criticalHitModifier) / 1000.0) * directHitModifier) / 100.0);
    double damage4 =
        (Math.floor(
        // D = ⌊ D3 × rand[95,105] ⌋ /100 ⌋
        damage3 * variancePercent) / 100.0);

    for (DamageMultiplier buff : damageMultipliers) {
      damage4 = (Math.floor(
          //  × buff_1 ⌋ × buff_2 ⌋
          damage4 * buff.multiplier()));
    }

    return (int) damage4;
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#damage-over-time">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#damage-over-time</a>
   * <br><br>
   * This version of the function simply calculates damage done by a "damage over time" tick.
   *
   * @param action      the action used to apply damage over time
   * @param isCrit      whether the "damage over time" tick was a critical hit
   * @param isDirectHit whether the "damage over time" tick was a direct hit
   * @param variance    how "hard" the "damage over time" tick hit this time; valid range is 0 - 10
   * @return the numerical amount of damage done by a single "damage over time" tick
   */
  public int getDamageOverTime(
      DamageAction action,
      boolean isCrit,
      boolean isDirectHit,
      double variance) {
    return getDamageOverTime(
        action,
        isCrit ? 1.0 : 0.0,
        isDirectHit ? 1.0 : 0.0,
        variance);
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#damage-over-time">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#damage-over-time</a>
   * <br><br>
   * This version of the function calculates average damage done by a "damage over time" debuff
   * sampled infinitely, to average the effect of critical and direct hits.
   *
   * @param action   the action used to apply damage over time
   * @param variance how "hard" the "damage over time" tick hit this time; valid range is 0 - 10
   * @return the numerical amount of damage done by a single "damage over time" tick
   */
  public int getDamageOverTime(DamageAction action, double variance) {
    return getDamageOverTime(
        action,
        parameters.pCHR() / 100.0,
        parameters.pDHR() / 100.0,
        variance);
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#damage-over-time">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#damage-over-time</a>
   * <br><br>
   * This version of the function allows customization of the critical and direct hit rate to
   * simulate being "lucky" or "unlucky". The function calculates average damage done by the
   * action as if you sampled infinitely, to average the effect of critical and direct hits.
   *
   * @param action          the action used to apply damage over time
   * @param criticalHitRate the critical hit rate as a fraction (ex: 0.2 is 20%)
   * @param directHitRate   the direct hit rate as a fraction (ex: 0.2 is 20%)
   * @param variance        how "hard" the tick hit this time; valid range is 0 - 10
   * @return the numerical amount of damage done by a single "damage over time" tick
   */
  public int getDamageOverTime(
      DamageAction action, double criticalHitRate, double directHitRate, double variance) {
    final int criticalHitModifier = (int) ((functions.fCRIT() - 1000) * criticalHitRate + 1000);
    final int directHitModifier = (int) ((25 * directHitRate) + 100);
    if (variance < 0 || variance > 10) {
      throw new IllegalArgumentException(
          "Variance range is 0 - 10. This is out of range: " + variance);
    }
    final double variancePercent = variance + 95;

    double finalDamage;
    switch (action.type()) {
      case PHYSICAL -> {

        int damage1 = (int)
            (Math.floor(Math.floor(Math.floor(
            // D1 = ⌊ Potency × f(ATK) × f(DET) ⌋ /100 ⌋ /1000 ⌋
            action.potency() * functions.fAP() * functions.fDET()) / 100.0) / 100.0));
        int damage2 = (int)
            (Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(
            // D2 = ⌊ D1 × f(TNC) ⌋ /1000 ⌋ × f(SPD) ⌋ /1000 ⌋
            damage1 * functions.fTEN()) / 1000.0) * functions.fSPD()) / 1000.0)
                // × f(WD) ⌋ /100 ⌋ × Trait ⌋ /100 ⌋ + 1
                * functions.fWD()) / 100.0) * jobMod.trait()) / 100.0) + 1;
        int damage3 = (int)
            (Math.floor(
            // D3 = ⌊ D2 × rand[95,105] ⌋ /100 ⌋
            damage2 * variancePercent) / 100.0);
        finalDamage = (Math.floor(Math.floor(Math.floor(
            // D = ⌊ D3 × CRIT? ⌋ /1000 ⌋ × DH? ⌋ /100 ⌋
            damage3 * criticalHitModifier) / 1000.0) * directHitModifier) / 100.0);

        for (DamageMultiplier buff : this.damageMultipliers) {
          finalDamage = (Math.floor(
              //  × buff_1 ⌋ × buff_2 ⌋
              finalDamage * buff.multiplier()));
        }

        return (int) finalDamage;
      }
      case MAGICAL -> {

        int damage1 = (int)
            (Math.floor(Math.floor(Math.floor(Math.floor(
            // D1 = ⌊ Potency × f(WD) ⌋ /100 ⌋ × f(ATK) ⌋ /100 ⌋
            action.potency() * functions.fWD()) / 100.0) * functions.fAP() / 100.0)
                // × f(SPD) ⌋ /1000 ⌋
                * functions.fSPD() / 1000.0));
        int damage2 = (int)
            (Math.floor(Math.floor(Math.floor(Math.floor(
            // D2 = ⌊ D1 × f(DET) ⌋ /1000 ⌋ × f(TNC) ⌋ /1000 ⌋
            damage1 * functions.fDET()) / 1000.0) * functions.fTEN() / 1000.0)
                // × Trait ⌋ /100 ⌋ + 1
                * jobMod.trait()) / 100.0) + 1;
        int damage3 = (int)
            (Math.floor(
            // D3 = ⌊ D2 × rand[95,105] ⌋ /100 ⌋
            damage2 * variancePercent) / 100.0);
        finalDamage =
            (Math.floor(Math.floor(Math.floor(
            // D = ⌊ D3 × CRIT? ⌋ /1000 ⌋ × DH? ⌋ /100 ⌋
            damage3 * criticalHitModifier) / 1000.0) * directHitModifier) / 100.0);

        for (DamageMultiplier buff : this.damageMultipliers) {
          finalDamage = (Math.floor(
              // × buff_1 ⌋ × buff_2 ⌋
              finalDamage * buff.multiplier()));
        }

        return (int) finalDamage;
      }
      default ->
        throw new IllegalStateException(
            "BUG: missing potency type for dot calculation: " + action.type());
    }
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#auto-attacks">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#auto-attacks</a>
   * <br><br>
   * This version of the function calculates average damage done by an auto attack as if you sampled
   * infinitely, to average the effect of critical and direct hits.
   *
   * @param variance how "hard" the auto attack hit this time; valid range is 0 - 10
   * @return the numerical amount of damage done
   */
  public int getAutoAttackDamage(double variance) {
    return getAutoAttackDamage(
        parameters.pCHR() / 100.0,
        parameters.pDHR() / 100.0,
        variance);
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#auto-attacks">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#auto-attacks</a>.
   * <br><br>
   * This version of the function simply calculates damage done by an auto attack
   *
   * @param isCrit      whether the auto attack was a critical hit
   * @param isDirectHit whether the auto attack was a direct hit
   * @param variance    how "hard" the auto attack hit this time; valid range is 0 - 10
   * @return the numerical amount of damage done
   */
  public int getAutoAttackDamage(
      boolean isCrit,
      boolean isDirectHit,
      double variance) {
    return getAutoAttackDamage(
        isCrit ? 1.0 : 0.0,
        isDirectHit ? 1.0 : 0.0,
        variance);
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#auto-attacks">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#auto-attacks</a>
   * <br><br>
   * This version of the function allows customization of the critical and direct hit rate to
   * simulate being "lucky" or "unlucky". The function calculates average damage done by the
   * auto attack as if you sampled infinitely, to average the effect of critical and direct hits.
   *
   * @param criticalHitRate the critical hit rate as a fraction (ex: 0.2 is 20%)
   * @param directHitRate   the direct hit rate as a fraction (ex: 0.2 is 20%)
   * @param variance        how "hard" the auto attack hit this time; valid range is 0 - 10
   * @return the numerical amount of damage done
   */
  public int getAutoAttackDamage(double criticalHitRate, double directHitRate, double variance) {
    final int criticalHitModifier = (int) ((functions.fCRIT() - 1000) * criticalHitRate + 1000);
    final int directHitModifier = (int) ((25 * directHitRate) + 100);
    if (variance < 0 || variance > 10) {
      throw new IllegalArgumentException(
          "Variance range is 0 - 10. This is out of range: " + variance);
    }
    final double variancePercent = variance + 95;

    /*
     * The potency of melee “Attack” autos is 110 potency. The potency of Physical Ranged “Shot”
     * autos is 100 potency. Dancers use melee “Attack” autos instead of Physical Ranged “Shot”
     * autos.
     */
    final int potency;
    switch (jobMod) {
      case BRD, MCH -> potency = 100;
      default -> potency = 110;
    }

    int damage1 = (int)
        (Math.floor(Math.floor(Math.floor(
        // D1 = ⌊ Potency × f(ATK) × f(DET) ⌋ /100 ⌋ /1000 ⌋
        potency * functions.fAP() * functions.fDET()) / 100.0) / 1000.0));
    int damage2 = (int)
        (Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(
        // D2 = ⌊ D1 × f(TNC) ⌋ /1000 ⌋ × f(SPD) ⌋ /1000 ⌋
        damage1 * functions.fTEN()) / 1000.0) * functions.fSPD()) / 1000.0)
            // × f(AUTO) ⌋ /100 ⌋ × Trait ⌋ /100 ⌋
            * functions.fAUTO() / 100.0) * jobMod.trait()) / 100.0);
    int damage3 = (int)
        (Math.floor(Math.floor(Math.floor(
        // D3 = ⌊ D2 × CRIT? ⌋ /1000 ⌋ × DH? ⌋ /100 ⌋
        damage2 * criticalHitModifier) / 1000.0) * directHitModifier) / 100.0);
    double damage4 =
        (Math.floor(
        // D = ⌊ D3 × rand[95,105] ⌋ /100 ⌋
        damage3 * variancePercent) / 100.0);

    for (DamageMultiplier buff : this.damageMultipliers) {
      damage4 = (Math.floor(
          // × buff_1 ⌋ × buff_2 ⌋
          damage4 * buff.multiplier()));
    }

    return (int) damage4;
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#damage-taken">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#damage-taken</a>.
   * <br><br>
   *
   * @param rawDamage      amount of damage the boss is dealing, before affected by buffs
   * @param isMagic        true if the damage is magical; false otherwise
   * @param variance       how "hard" the boss hit this time; valid range is 0 - 10
   * @param defensiveBuffs how much the damage taken is decreased (example: 0.1 = 10%, -0.1 = -10%)
   * @return the numerical amount of damage taken
   */
  public int getDamageTaken(
      int rawDamage, boolean isMagic, double variance, double... defensiveBuffs) {
    if (variance < 0 || variance > 10) {
      throw new IllegalArgumentException(
          "Variance range is 0 - 10. This is out of range: " + variance);
    }
    final double variancePercent = variance + 95;

    int damage1 = (int)
        (Math.floor(
            // MDT1 = ⌊ Raw Damage × (
            // PDT1 = ⌊ Raw Damage × (
            rawDamage * (
                //  100 -
                100 - (isMagic
                    // f(MDEF)
                    ? functions.fMDEF()
                    //  f(DEF)
                    : functions.fDEF()))
        // ) ⌋ /100 ⌋
        ) / 100.0);
    int damage2 = (int)
        (Math.floor(
            // PDT2 = ⌊ PDT1 × ( 2000 - f(TNC) ) ⌋ /1000 ⌋
            damage1 * (2000.0 - functions.fTEN())) / 1000.0);
    int damage3 = (int)
        (Math.floor(
            // PDT3 = ⌊ PDT2 × ( 100 - f(BLK) ) ⌋ /100 ⌋
            damage2 * (100.0 - functions.fBLK())) / 100.0);
    double damage4 =
        (Math.floor(
            // PDT = ⌊ PDT3 × rand[95,105] ⌋ /100 ⌋
            damage3 * variancePercent) / 100.0);

    for (double defensiveBuff : defensiveBuffs) {
      damage4 = (Math.floor(
          // × ( 1 - buff_1 ) ⌋ × ( 1 - buff_2 ) ⌋
          damage4 * (1.0 - defensiveBuff)));
    }

    return (int) damage4;
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#direct-heals">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#direct-heals</a>
   * <br><br>
   * This version of the function simply calculates healing done.
   *
   * @param healPotency the default, unmodified heal potency of the action
   * @param isCrit      whether the action was a critical hit
   * @param variance    how "hard" the heal hit this time; valid range is 0 - 6
   * @return the numerical amount of healing done
   */
  public int getHealing(int healPotency, boolean isCrit, double variance) {
    return getHealing(
        healPotency,
        isCrit ? 1.0 : 0.0,
        variance);
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#direct-heals">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#direct-heals</a>
   * <br><br>
   * This version of the function calculates average healing done by the action as if you sampled
   * infinitely, to average the effect of critical hits.
   *
   * @param healPotency the default, unmodified heal potency of the action
   * @param variance    how "hard" the heal hit this time; valid range is 0 - 6
   * @return the numerical amount of healing done
   */
  public int getHealing(int healPotency, double variance) {
    return getHealing(
        healPotency,
        parameters.pCHR() / 100.0,
        variance);
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#direct-heals">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/damage-and-healing/#direct-heals</a>
   * <br><br>
   * This version of the function allows customization of the hit rate to simulate being "lucky"
   * or "unlucky". The function calculates average damage done by the action as if you sampled
   * infinitely, to average the effect of critical hits.
   *
   * @param healPotency     the default, unmodified heal potency of the action
   * @param criticalHitRate the critical hit rate as a fraction (ex: 0.2 is 20%)
   * @param variance        how "hard" the heal hit this time; valid range is 0 - 6
   * @return the numerical amount of healing done
   */
  public int getHealing(int healPotency, double criticalHitRate, double variance) {
    final int criticalHitModifier = (int) ((functions.fCRIT() - 1000) * criticalHitRate + 1000);
    if (variance < 0 || variance > 6) {
      throw new IllegalArgumentException(
          "Variance range is 0 - 6. This is out of range: " + variance);
    }
    final double variancePercent = variance + 97;

    int heal1 = (int)
        (Math.floor(Math.floor(Math.floor(
        // H1 = ⌊ Potency × f(HMP) × f(DET) ⌋ /100 ⌋ /1000 ⌋
        healPotency * functions.fHMP() * functions.fDET()) / 100.0) / 1000.0));
    int heal2 = (int)
        (Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(
        // H2 = ⌊ H1 × f(TNC) ⌋ /1000 ⌋ × f(WD) ⌋ /100 ⌋ × Trait ⌋ /100 ⌋
        heal1 * functions.fTEN()) / 1000.0) * functions.fWD()) / 100.0) * jobMod.trait()) / 100.0);
    int heal3 = (int)
        (Math.floor(Math.floor(
        // H3 = ⌊ H2 × CRIT? ⌋ /1000 ⌋
        heal2 * criticalHitModifier) / 1000.0));
    double heal4 =
        (Math.floor(
        // H = ⌊ H3 × rand[97,103] ⌋ /100 ⌋
        heal3 * variancePercent) / 100.0);

    for (DamageMultiplier buff : this.damageMultipliers) {
      heal4 =
          (Math.floor(
          //  × buff_1 ⌋ × buff_2 ⌋
          heal4 * buff.multiplier()));
    }

    return (int) heal4;
  }
}
