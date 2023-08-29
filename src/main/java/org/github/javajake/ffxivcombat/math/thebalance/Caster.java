package org.github.javajake.ffxivcombat.math.thebalance;

import java.time.Duration;
import org.github.javajake.ffxivcombat.character.PlayableCharacter;

/**
 * Codified versions of the math described by The Balance for casters.
 */
public class Caster {

  private final PlayableCharacter character;

  public Caster(PlayableCharacter character) {
    this.character = character;
  }

  /**
   * Documented by
   * <a href="https://www.thebalanceffxiv.com/jobs/casters/black-mage/advanced-guide/#higher-level-blm-concepts">https://www.thebalanceffxiv.com/jobs/casters/black-mage/advanced-guide/#higher-level-blm-concepts</a>.
   *
   * @return on average the additional seconds to the listed cast time of a hard-casted spell or
   *         weaponskill.
   */
  public Duration getCasterTax() {
    /*
     * From the Black Mage guide
     * https://www.thebalanceffxiv.com/jobs/casters/black-mage/advanced-guide/#higher-level-blm-concepts
     * "Caster tax is on average an additional 0.1+2/FPS seconds to the listed cast time of a hard
     * casted spell due to animation lock at the end of the spell."
     *
     * From Kupo Bot (is there a better source?)
     * "The 0.1s are given by the server and stable. The 2/fps can vary a bit due to fps fluctuation
     * and how the animation lock timer of 0.1 is divisible by 1/fps"
     */
    return Duration.ofMillis((long) Math.ceil(100 * (0.1 + 2.0 / character.fps())));
  }
}
