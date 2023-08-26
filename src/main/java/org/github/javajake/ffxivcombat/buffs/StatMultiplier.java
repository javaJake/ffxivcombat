package org.github.javajake.ffxivcombat.buffs;

/**
 * A type of buff that is a simple positive or negative multiplier to the stats of a character.
 *
 * @param name       a human-readable name of the buff
 * @param multiplier how much the stat is increased or decreased (example: 1.1 = 10%, 0.9 = -10%)
 * @param stat       the particular stat that is being adjusted
 */
public record StatMultiplier(String name, double multiplier, Stat stat) implements Buff {
}
