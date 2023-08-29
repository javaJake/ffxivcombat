package org.github.javajake.ffxivcombat.endwalker.constants;

/**
 * Holds constants for each job.
 */
@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName") // intentionally mimicking Java records
public enum JobMod {
  PLD(19, 140, 100, 100, 110, 95, 60, 100, 100, MainStat.STR),
  WAR(21, 145, 100, 105, 110, 95, 40, 55, 100, MainStat.STR),
  DRK(32, 140, 100, 105, 110, 95, 60, 40, 100, MainStat.STR),
  DRK_LIVING_SHADOW(32, 140, 100, 100, 110, 95, 60, 40, 100, MainStat.STR),
  GNB(37, 120, 100, 100, 110, 95, 60, 100, 100, MainStat.STR),

  WHM(24, 105, 100, 55, 100, 105, 105, 115, 130, MainStat.MND),
  SCH(28, 105, 100, 90, 100, 100, 105, 115, 130, MainStat.MND),
  AST(33, 105, 100, 50, 100, 100, 105, 115, 130, MainStat.MND),
  AST_EARTHLY_STAR(33, 105, 100, 50, 100, 100, 105, 100, 130, MainStat.MND),

  MNK(20, 110, 100, 110, 100, 105, 50, 90, 100, MainStat.STR),
  DRG(22, 115, 100, 115, 105, 100, 45, 65, 100, MainStat.STR),
  NIN(30, 108, 100, 85, 100, 110, 65, 75, 100, MainStat.DEX),
  NIN_BUNSHIN(30, 108, 100, 85, 100, 100, 65, 75, 100, MainStat.DEX),
  SAM(34, 109, 100, 112, 100, 108, 60, 50, 100, MainStat.STR),

  BRD(23, 105, 100, 90, 100, 115, 85, 80, 120, MainStat.DEX),
  MCH(31, 105, 100, 85, 100, 115, 80, 85, 120, MainStat.DEX),
  MCH_AUTOMATON_QUEEN(31, 105, 100, 85, 100, 100, 80, 85, 120, MainStat.DEX),
  DNC(38, 105, 100, 90, 100, 115, 85, 80, 120, MainStat.DEX),

  BLM(25, 105, 100, 45, 100, 100, 115, 75, 130, MainStat.INT),
  SMN(27, 105, 100, 90, 100, 100, 115, 80, 130, MainStat.INT),
  RDM(35, 105, 100, 55, 100, 105, 115, 110, 130, MainStat.INT);

  private final int id;
  private final int hp;
  private final int mp;
  private final int str;
  private final int vit;
  private final int dex;
  private final int intl;
  private final int mnd;
  private final int trait;
  private final MainStat mainStat;

  JobMod(
      int id,
      int hp,
      int mp,
      int str,
      int vit,
      int dex,
      int intl,
      int mnd,
      int trait,
      MainStat mainStat) {
    this.id = id;
    this.hp = hp;
    this.mp = mp;
    this.str = str;
    this.vit = vit;
    this.dex = dex;
    this.intl = intl;
    this.mnd = mnd;
    this.trait = trait;
    this.mainStat = mainStat;
  }

  public int id() {
    return id;
  }

  public int hp() {
    return hp;
  }

  public int mp() {
    return mp;
  }

  public int str() {
    return str;
  }

  public int vit() {
    return vit;
  }

  public int dex() {
    return dex;
  }

  public int intl() {
    return intl;
  }

  public int mnd() {
    return mnd;
  }

  public int trait() {
    return trait;
  }

  public MainStat mainStatName() {
    return mainStat;
  }

  /**
   * Retrieves the constant for the stat that is the job's main stat.
   *
   * @return the value of the main stat for the job
   */
  public int mainStat() {
    return switch (this.mainStat) {
      case STR -> this.str;
      case VIT -> this.vit;
      case DEX -> this.dex;
      case INT -> this.intl;
      case MND -> this.mnd;
    };
  }

  /**
   * The type of stat that can be a main stat.
   */
  public enum MainStat {
    STR,
    VIT,
    DEX,
    INT,
    MND
  }
}
