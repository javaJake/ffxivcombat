# FFXIV Combat Library

The **primary goal** of this library is to provide the foundational math for theorycrafting in Final
Fantasy 14, to make it far easier to start new theorycrafting projects. It is incredibly difficult
and time-consuming to get the math correct. The hope is that a library such as this will make it easier
by letting sim authors skip this step, or at least the bulk of it.

The **secondary goal** of this library is to contribute back to the community that helped me. The
math package is purposefully aligned with the resources that were used to figure the math out. In
this way, it should be easy to cross-reference against the authoritative sources, so that this can
potentially be used as a secondary reference, a teaching tool, etc., by the community. However I can
help, I'd like to!

The functions here have been cross-checked enough times that confidence has grown in their
usefulness; nevertheless, feedback and testing is absolutely welcome! A proper suite of unit tests
is a work in progress at the moment.

# How to Use
The library is not yet published to Maven Central. When it is, there will be documentation here for
how to add the library to a Java project, using either Maven or Gradle.

Here's a basic example where a ninja character is created and the direct damage is calculated for
the first action in their basic combo, "Spinning Edge," assuming no party, food, or potion buffs.
```java
// Not all the stats are required; you can zero out selectively if you know.
// Here I zero out the main stats that don't apply to Ninja, and block stats which only apply to PLD
final PlayableCharacter ninja = new PlayableCharacter(
    "Ninja", // name
    JobMod.NIN, // job
    LevelMod.BY_LEVEL[90], // level
    0, // strength
    2930, // dexterity
    3089, // vitality
    0, // intelligence
    0, // mind
    2339, // critical hit
    1407, // determination
    1547, // direct hit
    2619, // defense
    2619, // magic defense
    536, // skill speed
    400, // spell speed
    400, // tenacity
    390, // piety
    126, // weapon damage
    107.52, // weapon auto attack
    2.56, // weapon delay
    0, // block strength
    0, // block rate
    60); // game client FPS

DamageAndHealing damageAndHealing = new DamageAndHealing(
    ninja,
    Collections.emptyList(), // no damage multipliers
    Collections.emptyList()); // no stat multipliers

final int directDamage = damageAndHealing.getDirectDamage(
    new DamageAction(DamageAction.Type.PHYSICAL, 220), // Spinning Edge
    true, // guaranteed crit
    true, // guaranteed direct hit
    1.0);
```

You can use the `src/test/java` source code locally to mess around with the math.

## Special Thanks
This library was developed while I was on the sleepocat team, and it was with their help and
observations that I grew a love for the theorycrafting side of the game. The sleepocat team has a
strong culture of contributing back to the community, and I'd like to think this library is a part
of that.

The Allagan Studies Discord community was also instrumental in answering the toughest questions.
The work they've done to collect and develop the math is the source of most, if not all, the math
contained in this library.