package org.github.javajake.ffxivcombat.math.akhmorning;

import java.util.List;
import org.github.javajake.ffxivcombat.buffs.RateModifier;
import org.github.javajake.ffxivcombat.character.PlayableCharacter;
import org.github.javajake.ffxivcombat.constants.JobMod;
import org.github.javajake.ffxivcombat.constants.LevelMod;

/**
 * Codified versions of the math in the page
 * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/parameters/">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/parameters/</a>.
 */
public class Parameters {

  private final PlayableCharacter character;
  private final double criticalHitRateChange;
  private final double directHitRateChange;
  private final LevelMod levelMod;
  private final JobMod jobMod;

  /**
   * Creates new "Parameters" math for a given character.
   *
   * @param character     the character to apply the math
   * @param rateModifiers rate modifiers active on the current character
   */
  public Parameters(
      final PlayableCharacter character,
      final List<RateModifier> rateModifiers) {
    this.character = character;

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
        default ->
            throw new UnsupportedOperationException(
                "BUG: unknown rate modifier: " + rateModifier.rate());
      }
    }

    // Akhmorning functions return percentages as integer values
    this.criticalHitRateChange = criticalHitRateChange * 100;
    this.directHitRateChange = directHitRateChange * 100;
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/parameters/#total-hp">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/parameters/#total-hp</a>.
   *
   * @return the probability of a Direct Hit
   */
  public int totalHp() {
    if (levelMod.level() < LevelMod.EW_LEVEL) {
      throw new UnsupportedOperationException("Total HP below level 90 is unknown");
    } else {
      switch (jobMod) {
        case PLD, WAR, DRK, GNB -> {
          throw new UnsupportedOperationException("Total HP for tanks is unknown");
        }
        default -> {
          // TODO: Undocumented; word of mouth
          return (int)
              // ⌊ LevelMod[Lv, HP] · ( JobMod[Job, HP] /100) ⌋
              (Math.floor(levelMod.hp() * (jobMod.hp() / 100.0))
                  //  + ⌊ ( VIT - LevelMod[Lv, MAIN] ) · 22.1 ⌋
                  + Math.floor((this.character.vitality() - levelMod.main()) * 24.3));
        }
      }
    }
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/parameters/#block-probability">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/parameters/#block-probability</a>.
   *
   * @return the probability of blocking
   */
  @SuppressWarnings({"checkstyle:AbbreviationAsWordInName", "checkstyle:MethodNameCheck"})
  public int pBLK() {
    return (int)
        // ⌊ ( 30 · Block Rate ) / LevelMod[Lv, DIV] + 10 ⌋
        (Math.floor(30.0 * this.character.blockRate() / levelMod.div()) + 10);
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/parameters/#direct-hit-probability">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/parameters/#direct-hit-probability</a>.
   *
   * @return the probability of a Direct Hit
   */
  @SuppressWarnings({"checkstyle:AbbreviationAsWordInName", "checkstyle:MethodNameCheck"})
  public double pDHR() {
    return
        // p(DHR) = ⌊ 550 · ( DHR - LevelMod[Lv, SUB] )/ LevelMod[Lv, DIV] ⌋ / 10
        (Math.floor(550.0 * (this.character.directHit() - levelMod.sub()) / levelMod.div()) / 10.0)
            // Custom convenience for dealing with buffs
            + this.directHitRateChange;
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/parameters/#critical-hit-probability">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/parameters/#critical-hit-probability</a>.
   *
   * @return the probability of a Critical Hit
   */
  @SuppressWarnings({"checkstyle:AbbreviationAsWordInName", "checkstyle:MethodNameCheck"})
  public double pCHR() {
    return
        // p(CHR) = ⌊ 200 · ( CHR - LevelModLv, SUB )/ LevelModLv, DIV + 50 ⌋ / 10
        (Math.floor(200.0 * (this.character.criticalHit() - levelMod.sub())
            // / LevelModLv, DIV + 50 ⌋ / 10
            / levelMod.div() + 50) / 10.0)
            // Custom convenience for dealing with buffs
            + this.criticalHitRateChange;
  }
}
