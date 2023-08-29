package org.github.javajake.ffxivcombat.endwalker.constants;

/**
 * Holds constants for different races.
 */
public enum RacialMod {
  Raen("Au Ra", -1, 2, -1, 0, 3),
  Xaela("Au Ra", 3, 0, 2, 0, -2),
  Duskwight("Elezen", 0, 0, -1, 3, 1),
  Wildwood("Elezen", 0, 3, -1, 2, -1),
  Helion("Hrothgar", 3, -3, 3, -3, 3),
  TheLost("Hrothgar", 3, -3, 3, -3, 3),
  Highlander("Hyur", 3, 0, 2, -2, 0),
  Midlander("Hyur", 2, -1, 0, 3, -1),
  Dunesfolk("Lalafell", -1, 1, -2, 2, 3),
  Plainsfolk("Lalafell", -1, 3, -1, 2, 0),
  Sun("Miqote", 2, 3, 0, -1, -1),
  Moon("Miqote", -1, 2, -2, 1, 3),
  Hellsguard("Roegadyn", 0, -2, 2, 0, 2),
  SeaWolves("Roegadyn", 2, -1, 3, -2, 1),
  Rava("Viera", 0, 3, -2, 1, 1),
  Veena("Viera", -1, 0, -1, 3, 2);

  private final String race;
  private final int str;
  private final int dex;
  private final int vit;
  private final int intl;
  private final int mnd;

  RacialMod(String race, int str, int dex, int vit, int intl, int mnd) {
    this.race = race;
    this.str = str;
    this.dex = dex;
    this.vit = vit;
    this.intl = intl;
    this.mnd = mnd;
  }
}
