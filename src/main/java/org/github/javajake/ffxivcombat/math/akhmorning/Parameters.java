package org.github.javajake.ffxivcombat.math.akhmorning;

import java.util.List;
import org.github.javajake.ffxivcombat.buffs.RateModifier;
import org.github.javajake.ffxivcombat.character.PlayableCharacter;
import org.github.javajake.ffxivcombat.constants.LevelMod;

public class Parameters {

  private final PlayableCharacter character;
  private final double criticalHitRateChange;
  private final double directHitRateChange;
  private final LevelMod levelMod;

  public Parameters(
      final PlayableCharacter character,
      final List<RateModifier> rateModifiers) {
    this.character = character;

    // Just for convenience
    this.levelMod = this.character.levelMod();

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

    // Akhmorning functions return percentages as integer values
    this.criticalHitRateChange = criticalHitRateChange * 100;
    this.directHitRateChange = directHitRateChange * 100;
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/parameters/#direct-hit-probability">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/parameters/#direct-hit-probability</a>
   *
   * @return the probability of a Direct Hit
   */
  public double pDHR() {
    return
        // p(DHR) = ⌊ 550 · ( DHR - LevelMod[Lv, SUB] )/ LevelMod[Lv, DIV] ⌋ / 10
        (Math.floor(550.0 * (this.character.directHit() - levelMod.sub()) / levelMod.div()) / 10.0)
            + this.directHitRateChange;
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/parameters/#critical-hit-probability">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/parameters/#critical-hit-probability</a>
   *
   * @return the probability of a Critical Hit
   */
  public double pCHR() {
    return
        // p(CHR) = ⌊ 200 · ( CHR - LevelModLv, SUB )/ LevelModLv, DIV + 50 ⌋ / 10
        (Math.floor(200.0 * (this.character.criticalHit() - levelMod.sub()) / levelMod.div() + 50) / 10.0)
            + this.criticalHitRateChange;
  }
}
