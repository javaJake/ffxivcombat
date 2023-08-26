package org.github.javajake.ffxivcombat.math.akhmorning;

import java.util.List;
import org.github.javajake.ffxivcombat.actions.DamageAction;
import org.github.javajake.ffxivcombat.buffs.DamageMultiplier;
import org.github.javajake.ffxivcombat.constants.JobMod;
import org.github.javajake.ffxivcombat.math.VarianceOutOfRangeException;

public class DamageAndHealing {

  private final Functions functions;
  private final Parameters parameters;
  private final JobMod jobMod;
  private final List<DamageMultiplier> damageMultipliers;

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
   * @param action            the action used to deal damage
   * @param isCrit            whether the damage was a critical hit
   * @param isDirectHit       whether the damage was a direct hit
   * @param variance          how "hard" the damage hit this time; valid range is 0 - 10
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
   * infinitely, to average the effect of crit and direct hit.
   *
   * @param action            the action used to deal damage
   * @param variance          how "hard" the damage hit this time; valid range is 0 - 10
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
   * action as if you sampled infinitely, to average the effect of crit and direct hit.
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
    int criticalHitModifier = (int) ((functions.fCRIT() - 1000) * criticalHitRate + 1000);
    int directHitModifier = (int) ((25 * directHitRate) + 100);

    if (variance < 0 || variance > 10) {
      throw new VarianceOutOfRangeException(variance);
    }
    variance += 95;

    int damage1 = (int) Math.floor(Math.floor(Math.floor(
        // D1 = ⌊ Potency × f(ATK) × f(DET) ⌋
        action.potency() * functions.fAP() * functions.fDET())
            //  /100 ⌋ /1000 ⌋
            / 100.0 ) / 1000.0 );
    int damage2 = (int) (Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(
        // D2 = ⌊ D1 × f(TNC) ⌋ /1000 ⌋ × f(WD) ⌋ /100 ⌋ × Trait ⌋ /100 ⌋
        damage1 * functions.fTEN()) / 1000.0) * functions.fWD()) / 100.0) * jobMod.trait()) / 100.0);
    int damage3 = (int) (Math.floor(Math.floor(Math.floor(
        // D3 = ⌊ D2 × CRIT? ⌋ /1000 ⌋ × DH? ⌋ /100 ⌋
        damage2 * criticalHitModifier) / 1000.0) * directHitModifier) / 100.0);
    double damage4 =
        // D = ⌊ D3 × rand[95,105] ⌋ /100 ⌋ × buff_1 ⌋ × buff_2 ⌋
        (Math.floor(damage3 * variance) / 100.0);

    for (DamageMultiplier buff : damageMultipliers) {
      damage4 =
          (Math.floor(damage4 * buff.multiplier()));
    }

    return (int) damage4;
  }
}
