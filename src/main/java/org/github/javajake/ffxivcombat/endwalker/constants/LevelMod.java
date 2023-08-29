package org.github.javajake.ffxivcombat.endwalker.constants;

/**
 * Holds constants for each level.
 *
 * @param level  the level of the character
 * @param mp     the mp constant
 * @param main   the main constant
 * @param sub    the sub constant
 * @param div    the div constant
 * @param hp     the hp constant
 * @param elmt   the elmt constant
 * @param threat the threat constant
 */
public record LevelMod(
    int level,
    int mp,
    int main,
    int sub,
    int div,
    int hp,
    int elmt,
    int threat
) {

  public static final int ARR_LEVEL = 50;
  public static final int HW_LEVEL = 60;
  public static final int SB_LEVEL = 70;
  public static final int SHB_LEVEL = 80;
  public static final int EW_LEVEL = 90;

  public static final LevelMod[] BY_LEVEL = new LevelMod[]{
      null, // level 0 doesn't exist
      new LevelMod(1, 10000, 20, 56, 56, 86, 52, 2),
      new LevelMod(2, 10000, 21, 57, 57, 101, 54, 2),
      new LevelMod(3, 10000, 22, 60, 60, 109, 56, 3),
      new LevelMod(4, 10000, 24, 62, 62, 116, 58, 3),
      new LevelMod(5, 10000, 26, 65, 65, 123, 60, 3),
      new LevelMod(6, 10000, 27, 68, 68, 131, 62, 3),
      new LevelMod(7, 10000, 29, 70, 70, 138, 64, 4),
      new LevelMod(8, 10000, 31, 73, 73, 145, 66, 4),
      new LevelMod(9, 10000, 33, 76, 76, 153, 68, 4),
      new LevelMod(10, 10000, 35, 78, 78, 160, 70, 5),
      new LevelMod(11, 10000, 36, 82, 82, 174, 73, 5),
      new LevelMod(12, 10000, 38, 85, 85, 188, 75, 5),
      new LevelMod(13, 10000, 41, 89, 89, 202, 78, 6),
      new LevelMod(14, 10000, 44, 93, 93, 216, 81, 6),
      new LevelMod(15, 10000, 46, 96, 96, 230, 84, 7),
      new LevelMod(16, 10000, 49, 100, 100, 244, 86, 7),
      new LevelMod(17, 10000, 52, 104, 104, 258, 89, 8),
      new LevelMod(18, 10000, 54, 109, 109, 272, 93, 9),
      new LevelMod(19, 10000, 57, 113, 113, 286, 95, 9),
      new LevelMod(20, 10000, 60, 116, 116, 300, 98, 10),
      new LevelMod(21, 10000, 63, 122, 122, 0, 102, 10),
      new LevelMod(22, 10000, 67, 127, 127, 366, 105, 11),
      new LevelMod(23, 10000, 71, 133, 133, 399, 109, 12),
      new LevelMod(24, 10000, 74, 138, 138, 432, 113, 13),
      new LevelMod(25, 10000, 78, 144, 144, 465, 117, 14),
      new LevelMod(26, 10000, 81, 150, 150, 0, 121, 15),
      new LevelMod(27, 10000, 85, 155, 155, 531, 125, 16),
      new LevelMod(28, 10000, 89, 162, 162, 564, 129, 17),
      new LevelMod(29, 10000, 92, 168, 168, 0, 133, 18),
      new LevelMod(30, 10000, 97, 173, 173, 630, 137, 19),
      new LevelMod(31, 10000, 101, 181, 181, 669, 143, 20),
      new LevelMod(32, 10000, 106, 188, 188, 708, 148, 22),
      new LevelMod(33, 10000, 110, 194, 194, 747, 153, 23),
      new LevelMod(34, 10000, 115, 202, 202, 786, 159, 25),
      new LevelMod(35, 10000, 119, 209, 209, 825, 165, 27),
      new LevelMod(36, 10000, 124, 215, 215, 864, 170, 29),
      new LevelMod(37, 10000, 128, 223, 223, 903, 176, 31),
      new LevelMod(38, 10000, 134, 229, 229, 942, 181, 33),
      new LevelMod(39, 10000, 139, 236, 236, 981, 186, 35),
      new LevelMod(40, 10000, 144, 244, 244, 1020, 192, 38),
      new LevelMod(41, 10000, 150, 253, 253, 1088, 200, 40),
      new LevelMod(42, 10000, 155, 263, 263, 1156, 207, 43),
      new LevelMod(43, 10000, 161, 272, 272, 1224, 215, 46),
      new LevelMod(44, 10000, 166, 283, 283, 1292, 223, 49),
      new LevelMod(45, 10000, 171, 292, 292, 0, 231, 52),
      new LevelMod(46, 10000, 177, 302, 302, 0, 238, 55),
      new LevelMod(47, 10000, 183, 311, 311, 1496, 246, 58),
      new LevelMod(48, 10000, 189, 322, 322, 0, 254, 62),
      new LevelMod(49, 10000, 196, 331, 331, 1632, 261, 66),
      new LevelMod(50, 10000, 202, 341, 341, 1700, 269, 70),
      new LevelMod(51, 10000, 204, 342, 366, 0, 0, 0),
      new LevelMod(52, 10000, 205, 344, 392, 0, 0, 0),
      new LevelMod(53, 10000, 207, 345, 418, 0, 0, 0),
      new LevelMod(54, 10000, 209, 346, 444, 0, 0, 0),
      new LevelMod(55, 10000, 210, 347, 470, 0, 0, 0),
      new LevelMod(56, 10000, 212, 349, 496, 0, 0, 0),
      new LevelMod(57, 10000, 214, 350, 522, 0, 0, 0),
      new LevelMod(58, 10000, 215, 351, 548, 0, 0, 0),
      new LevelMod(59, 10000, 217, 352, 574, 0, 0, 0),
      new LevelMod(60, 10000, 218, 354, 600, 0, 0, 0),
      new LevelMod(61, 10000, 224, 355, 630, 0, 0, 0),
      new LevelMod(62, 10000, 228, 356, 660, 0, 0, 0),
      new LevelMod(63, 10000, 236, 357, 690, 0, 0, 0),
      new LevelMod(64, 10000, 244, 358, 720, 0, 0, 0),
      new LevelMod(65, 10000, 252, 359, 750, 0, 0, 0),
      new LevelMod(66, 10000, 260, 360, 780, 0, 0, 0),
      new LevelMod(67, 10000, 268, 361, 810, 0, 0, 0),
      new LevelMod(68, 10000, 276, 362, 840, 0, 0, 0),
      new LevelMod(69, 10000, 284, 363, 870, 0, 0, 0),
      new LevelMod(70, 10000, 292, 364, 900, 0, 0, 0),
      new LevelMod(71, 10000, 296, 365, 940, 0, 0, 0),
      new LevelMod(72, 10000, 300, 366, 980, 0, 0, 0),
      new LevelMod(73, 10000, 305, 367, 1020, 0, 0, 0),
      new LevelMod(74, 10000, 310, 368, 1060, 0, 0, 0),
      new LevelMod(75, 10000, 315, 370, 1100, 0, 0, 0),
      new LevelMod(76, 10000, 320, 372, 1140, 0, 0, 0),
      new LevelMod(77, 10000, 325, 374, 1180, 0, 0, 0),
      new LevelMod(78, 10000, 330, 376, 1220, 0, 0, 0),
      new LevelMod(79, 10000, 335, 378, 1260, 0, 0, 0),
      new LevelMod(80, 10000, 340, 380, 1300, 0, 0, 0),
      new LevelMod(81, 10000, 345, 382, 1360, 0, 0, 0),
      new LevelMod(82, 10000, 350, 384, 1420, 0, 0, 0),
      new LevelMod(83, 10000, 355, 386, 1480, 0, 0, 0),
      new LevelMod(84, 10000, 360, 388, 1540, 0, 0, 0),
      new LevelMod(85, 10000, 365, 390, 1600, 0, 0, 0),
      new LevelMod(86, 10000, 370, 392, 1660, 0, 0, 0),
      new LevelMod(87, 10000, 375, 394, 1720, 0, 0, 0),
      new LevelMod(88, 10000, 380, 396, 1780, 0, 0, 0),
      new LevelMod(89, 10000, 385, 398, 1840, 0, 0, 0),
      new LevelMod(90, 10000, 390, 400, 1900, 3000, 0, 0),
  };
}
