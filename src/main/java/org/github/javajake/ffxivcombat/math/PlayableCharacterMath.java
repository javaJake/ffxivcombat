package org.github.javajake.ffxivcombat.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;
import org.github.javajake.ffxivcombat.actions.DamageAction;
import org.github.javajake.ffxivcombat.buffs.DamageMultiplier;
import org.github.javajake.ffxivcombat.buffs.RateModifier;
import org.github.javajake.ffxivcombat.character.PlayableCharacter;
import org.github.javajake.ffxivcombat.constants.JobMod;
import org.github.javajake.ffxivcombat.constants.LevelMod;
import org.github.javajake.ffxivcombat.math.akhmorning.FunctionsMath;

/**
 * Capable of performing
 */
@SuppressWarnings("PMD.DataflowAnomalyAnalysis") // allow pseudo-constants so functions are clear
public class PlayableCharacterMath {
  public static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
  public static final BigDecimal THOUSAND = BigDecimal.valueOf(1000);

  private final LevelMod levelMod;
  private final JobMod jobMod;

  /* *** Stat-modified character *** */
  // only stat that can't be modified is the main stat; see mainStat for that
  private final PlayableCharacter character;
  private final FunctionsMath functions;
  /* *** Damage modifiers *** */
  private final List<DamageMultiplier> damageMultipliers;
  /* *** Rate modifiers *** */
  private final double criticalHitRateChange;
  private final double directHitRateChange;

  public PlayableCharacterMath(
      PlayableCharacter character,
      List<DamageMultiplier> damageMultipliers,
      List<RateModifier> rateModifiers) {
    this.character = character;
    this.functions = new FunctionsMath(character);
    this.damageMultipliers = damageMultipliers;

    // Just for convenience
    this.levelMod = this.character.levelMod();
    this.jobMod = this.character.jobMod();

    double criticalHitRateChange = 0;
    double directHitRateChange = 0;
    for (RateModifier rateModifier : rateModifiers) {
      switch (rateModifier.rate()) {
        case ALL -> {
          criticalHitRateChange += rateModifier.amount();
          directHitRateChange += rateModifier.amount();
        }
        case CRITICAL_RATE -> criticalHitRateChange += rateModifier.amount();
        case DIRECT_HIT_RATE -> directHitRateChange += rateModifier.amount();
      }
    }
    this.criticalHitRateChange = criticalHitRateChange;
    this.directHitRateChange = directHitRateChange;
  }

  public int getHP() {
    switch (jobMod) {
      case PLD, WAR, DRK, GNB -> {
        // TODO: not accurate for tanks
        return (int)
            (Math.floor(levelMod.hp() * (jobMod.hp() / 100.0)) + Math.floor((this.character.vitality() - levelMod.main()) * 34.6));
      }
      default -> {
        return (int)
            (Math.floor(levelMod.hp() * (jobMod.hp() / 100.0)) + Math.floor((this.character.vitality() - levelMod.main()) * 24.3));
      }
    }
  }

  public int getGcdMillis(int typeY, int typeZ) {
    return this.getCastTimeMillis(2500, typeY, typeZ);
  }

  public int getCastTimeMillis(int actionCastTimeMillis, int typeY, int typeZ) {
    final int haste = 0;
    final int astralUmbral = 100;

    int gcd1 = (int)
        Math.floor((2000.0 - functions.fSPD()) * actionCastTimeMillis / 1000.0);
    int gcd2 = (int)
        Math.floor((100.0 - typeY) * (100.0 - haste) / 100.0);
    double gcd3 = (100.0 - typeZ) / 100.0;
    int gcd4 = (int)
        Math.floor(Math.floor(Math.ceil(gcd2 * gcd3) * gcd1 / 1000.0) * astralUmbral / 100.0);

    return gcd4 * 10;
  }

  public double getCriticalHitRate() {
    return
        (Math.floor(200.0 * (this.character.criticalHit() - levelMod.sub()) / levelMod.div() + 50) / 1000.0)
        + this.criticalHitRateChange;
  }

  public double getDirectHitRate() {
    return
        (Math.floor(550.0 * (this.character.directHit() - levelMod.sub()) / levelMod.div()) / 1000.0)
        + this.directHitRateChange;
  }

  public int getBlockProbability() {
    return (int)
        (Math.floor(30.0 * this.character.blockRate() / levelMod.div()) + 10);
  }

  public int getHealing(int healPotency, double variance) {
    return getHealing(healPotency, this.getCriticalHitRate(), variance);
  }

  public int getHealing(int healPotency, double criticalHitRate, double variance) {
    int criticalHitModifier = (int) ((functions.fCRIT() - 1000) * criticalHitRate + 1000);
    if (variance < 0 || variance > 6) {
      throw new IllegalArgumentException("Variance range is 0 - 6. This is out of range: " + variance);
    }
    variance += 97;

    BigDecimal bigPotency = BigDecimal.valueOf(healPotency);
    BigDecimal bigHealingMagicPotency = BigDecimal.valueOf(functions.fHMP());
    BigDecimal bigDetermination = BigDecimal.valueOf(functions.fDET());

    int heal1 =
        bigPotency
            .multiply(bigHealingMagicPotency)
            .multiply(bigDetermination)
            .divide(HUNDRED, RoundingMode.FLOOR)
            .divide(THOUSAND, RoundingMode.FLOOR)
            .intValue();
    int heal2 = (int)
        (Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(heal1 * functions.fTEN()) / 1000.0) * functions.fWD()) / 100.0) * jobMod.trait()) / 100.0);
    int heal3 = (int)
        (Math.floor(Math.floor(heal2 * criticalHitModifier) / 1000.0));
    double heal4 =
        (Math.floor(heal3 * variance) / 100.0);

    for (DamageMultiplier buff : this.damageMultipliers) {
      heal4 =
          (Math.floor(heal4 * buff.multiplier()));
    }

    return (int) heal4;
  }

  public int getDirectDamage(DamageAction action, double variance) {
    return getDirectDamage(action, this.getCriticalHitRate(), this.getDirectHitRate(), variance);
  }

  public int getDirectDamage(DamageAction action, double criticalHitRate, double directHitRate, double variance) {
    int criticalHitModifier = (int) ((functions.fCRIT() - 1000) * criticalHitRate + 1000);
    int directHitModifier = (int) ((25 * directHitRate) + 100);

    if (variance < 0 || variance > 10) {
      throw new VarianceOutOfRangeException(variance);
    }
    variance += 95;

    BigDecimal bigPotency = BigDecimal.valueOf(action.potency());
    BigDecimal bigAttackPower = BigDecimal.valueOf(functions.fAP());
    BigDecimal bigDetermination = BigDecimal.valueOf(functions.fDET());

    int damage1 =
        bigPotency
            .multiply(bigAttackPower)
            .multiply(bigDetermination)
            .divide(HUNDRED, RoundingMode.FLOOR)
            .divide(THOUSAND, RoundingMode.FLOOR)
            .intValue();
    int damage2 = (int)
        (Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(damage1 * functions.fTEN()) / 1000.0) * functions.fWD()) / 100.0) * jobMod.trait()) / 100.0);
    int damage3 = (int)
        (Math.floor(Math.floor(Math.floor(damage2 * criticalHitModifier) / 1000.0) * directHitModifier) / 100.0);
    double damage4 =
        (Math.floor(damage3 * variance) / 100.0);

    for (DamageMultiplier buff : this.damageMultipliers) {
      damage4 =
          (Math.floor(damage4 * buff.multiplier()));
    }

    return (int) damage4;
  }

  public int getDamageOverTime(DamageAction action, double variance) {
    return getDamageOverTime(action, this.getCriticalHitRate(), this.getDirectHitRate(), variance);
  }

  public int getDamageOverTime(DamageAction action, double criticalHitRate, double directHitRate, double variance) {
    int criticalHitModifier = (int) ((functions.fCRIT() - 1000) * criticalHitRate + 1000);
    int directHitModifier = (int) ((25 * directHitRate) + 100);
    if (variance < 0 || variance > 10) {
      throw new IllegalArgumentException("Variance range is 0 - 10. This is out of range: " + variance);
    }
    variance += 95;

    BigDecimal bigPotency = BigDecimal.valueOf(action.potency());
    BigDecimal bigAttackPower = BigDecimal.valueOf(functions.fAP());
    BigDecimal bigDetermination = BigDecimal.valueOf(functions.fDET());

    double finalDamage;
    switch (action.type()) {
      case PHYSICAL -> {

        int damage1 =
            bigPotency
                .multiply(bigAttackPower)
                .multiply(bigDetermination)
                .divide(HUNDRED, RoundingMode.FLOOR)
                .divide(THOUSAND, RoundingMode.FLOOR)
                .intValue();
        int damage2 = (int)
            (Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(damage1 * functions.fTEN()) / 1000.0) * functions.fSPD()) / 1000.0) * functions.fWD()) / 100.0) * jobMod.trait()) / 100.0) + 1;
        int damage3 = (int)
            (Math.floor(damage2 * variance) / 100.0);
        finalDamage =
            (Math.floor(Math.floor(Math.floor(damage3 * criticalHitModifier) / 1000.0) * directHitModifier) / 100.0);

        for (DamageMultiplier buff : this.damageMultipliers) {
          finalDamage =
              (Math.floor(finalDamage * buff.multiplier()));
        }

        return (int) finalDamage;
      }
      case MAGICAL -> {
        BigDecimal bigWeaponDamage = BigDecimal.valueOf(functions.fWD());
        BigDecimal bigSpeed = BigDecimal.valueOf(functions.fSPD());

        int damage1 =
            bigPotency
                .multiply(bigWeaponDamage)
                .divide(HUNDRED, RoundingMode.FLOOR)
                .multiply(bigAttackPower)
                .divide(HUNDRED, RoundingMode.FLOOR)
                .multiply(bigSpeed)
                .divide(THOUSAND, RoundingMode.FLOOR)
                .intValue();
        int damage2 = (int)
            (Math.floor(Math.floor(Math.floor(Math.floor(damage1 * functions.fDET()) / 1000.0) * functions.fTEN() / 1000.0) * jobMod.trait()) / 100.0) + 1;
        int damage3 = (int)
            (Math.floor(damage2 * variance) / 100.0);
        finalDamage =
            (Math.floor(Math.floor(Math.floor(damage3 * criticalHitModifier) / 1000.0) * directHitModifier) / 100.0);

        for (DamageMultiplier buff : this.damageMultipliers) {
          finalDamage =
              (Math.floor(finalDamage * buff.multiplier()));
        }

        return (int) finalDamage;
      }
    }
    throw new IllegalStateException("BUG: missing potency type for dot calculation: " + action.type());
  }

  public int getAutoAttackDamage(double variance) {
    return getAutoAttackDamage(this.getCriticalHitRate(), this.getDirectHitRate(), variance);
  }

  public int getAutoAttackDamage(double criticalHitRate, double directHitRate, double variance) {
    int criticalHitModifier = (int) ((functions.fCRIT() - 1000) * criticalHitRate + 1000);
    int directHitModifier = (int) ((25 * directHitRate) + 100);
    if (variance < 0 || variance > 10) {
      throw new IllegalArgumentException("Variance range is 0 - 10. This is out of range: " + variance);
    }
    variance += 95;

    BigDecimal bigPotency;
    switch (jobMod) {
      case BRD, MCH -> bigPotency = BigDecimal.valueOf(100);
      default -> bigPotency = BigDecimal.valueOf(110);
    }

    BigDecimal bigAttackPower = BigDecimal.valueOf(functions.fAP());
    BigDecimal bigDetermination = BigDecimal.valueOf(functions.fDET());

    int damage1 =
        bigPotency
            .multiply(bigAttackPower)
            .multiply(bigDetermination)
            .divide(HUNDRED, RoundingMode.FLOOR)
            .divide(THOUSAND, RoundingMode.FLOOR)
            .intValue();
    int damage2 = (int)
        (Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(Math.floor(damage1 * functions.fTEN()) / 1000.0) * functions.fSPD()) / 1000.0) * functions.fAUTO() / 100.0) * jobMod.trait()) / 100.0);
    int damage3 = (int)
        (Math.floor(Math.floor(Math.floor(damage2 * criticalHitModifier) / 1000.0) * directHitModifier) / 100.0);
    double damage4 =
        (Math.floor(damage3 * variance) / 100.0);

    for (DamageMultiplier buff : this.damageMultipliers) {
      damage4 =
          (Math.floor(damage4 * buff.multiplier()));
    }

    return (int) damage4;
  }

  public int getDamageTaken(int rawDamage, boolean isMagic, double variance) {
    if (variance < 0 || variance > 10) {
      throw new IllegalArgumentException("Variance range is 0 - 10. This is out of range: " + variance);
    }
    variance += 95;

    int damage1 = (int)
        (Math.floor(rawDamage * Math.floor(100 - (isMagic ? functions.fMDEF() : functions.fDEF()))) / 100.0);
    int damage2 = (int)
        (Math.floor(damage1 * (2000.0 - functions.fTEN())) / 1000.0);
    int damage3 = (int)
        (Math.floor(damage2 * (100.0 - functions.fBLK())) / 100.0);
    return (int) // damage4
        (Math.floor(damage3 * variance) / 100.0);
  }

  public Duration getCasterTax() {
    return Duration.ofMillis((long) Math.ceil(100*(0.1+2.0/character.fps())));
  }
}
