package org.github.javajake.ffxivcombat.math.akhmorning;

/**
 * Codified versions of the math in the page
 * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/speed/">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/speed/</a>.
 */
public class Speed {

  private final Functions functions;

  /**
   * Creates new "Speed" math for a given character.
   *
   * @param functions     the "Functions" Ahkmorning math
   */
  public Speed(Functions functions) {
    this.functions = functions;
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/speed/#gcds--cast-times">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/speed/#gcds--cast-times</a>
   * <br><br>
   * See
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/speed/#type-y--type-z-variables">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/speed/#type-y--type-z-variables</a>
   * for the appropriate type Y and type Z variables for a given speed bonus.
   *
   * @param typeY the type Y variable for a certain speed bonus
   * @param typeZ the type Z variable for a certain speed bonus
   * @return the default GCD of a character in milliseconds, given speed bonuses
   */
  public int gcd(int typeY, int typeZ) {
    return this.gcd(2500, typeY, typeZ);
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/speed/#gcds--cast-times">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/speed/#gcds--cast-times</a>
   * <br><br>
   * See
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/speed/#type-y--type-z-variables">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/speed/#type-y--type-z-variables</a>
   * for the appropriate type Y and type Z variables for a given speed bonus.
   * <br><br>
   * This version of the function assumes no haste or astral/umbral bonuses.
   *
   * @param actionDelay the default, unmodified action delay (i.e. "cast time") without any bonuses
   * @param typeY the type Y variable for a certain speed bonus
   * @param typeZ the type Z variable for a certain speed bonus
   * @return the modified action delay in milliseconds, given speed bonuses
   */
  public int gcd(int actionDelay, int typeY, int typeZ) {
    return this.gcd(actionDelay, typeY, typeZ, 0, 100);
  }

  /**
   * Documentation:
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/speed/#gcds--cast-times">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/speed/#gcds--cast-times</a>
   * <br><br>
   * See
   * <a href="https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/speed/#type-y--type-z-variables">https://www.akhmorning.com/allagan-studies/how-to-be-a-math-wizard/shadowbringers/speed/#type-y--type-z-variables</a>
   * for the appropriate type Y and type Z variables for a given speed bonus.
   *
   * @param actionDelay the default, unmodified action delay (i.e. "cast time") without any bonuses
   * @param typeY the type Y variable for a certain speed bonus
   * @param typeZ the type Z variable for a certain speed bonus
   * @param haste the haste bonus percentage as an integer (i.e. 10% = 10)
   * @param astralUmbral the astral/umbral bonus percentage as an integer, or 100 if none
   * @return the modified action delay in milliseconds, given speed bonuses
   */
  public int gcd(int actionDelay, int typeY, int typeZ, int haste, int astralUmbral) {
    int gcd1 = (int)
        // GCD1 = ⌊ ( 2000 - f(SPD) ) × Action Delay / 1000 ⌋
        Math.floor((2000.0 - functions.fSPD()) * actionDelay / 1000.0);
    int gcd2 = (int)
        // GCD2 = ⌊ ( 100 - Type Y ) × ( 100 - Haste ) / 100 ⌋
        Math.floor((100.0 - typeY) * (100.0 - haste) / 100.0);
    double gcd3 =
        // GCD3 = ( 100 - Type Z ) / 100
        (100.0 - typeZ) / 100.0;
    int gcd4 = (int)
        // GCD4 = ⌊ ⌈ GCD2 × GCD3 ⌉ × GCD1 / 1000 ⌋ × Astral_Umbral / 100 ⌋
        Math.floor(Math.floor(Math.ceil(gcd2 * gcd3) * gcd1 / 1000.0) * astralUmbral / 100.0);

    return gcd4 * 10;
  }

}
