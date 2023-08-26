package org.github.javajake.ffxivcombat.buffs;

/**
 * A type of buff that is a simple positive or negative amount to the stats of a character.
 *
 * @param name   a human-readable name of the buff
 * @param amount how much the stat is increased or decreased (10 increases by 10, -10 decreases
 *               by 10)
 * @param stat   the particular stat that is being adjusted
 */
public record StatAdjustment(String name, int amount, Stat stat) implements Buff {
}
