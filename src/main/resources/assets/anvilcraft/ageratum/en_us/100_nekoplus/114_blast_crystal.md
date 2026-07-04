---
navigation:
  title: "NekoPlus | Blast Crystal"
  icon: "nekoplus:blast_crystal"
items:
  - nekoplus:blast_crystal
---

# Blast Crystal

<item id="nekoplus:blast_crystal"/>

The Blast Crystal is a crystal that turns explosion impact into charge. It does not work on its own; it must be hit by an explosion, then it converts that explosion into charge that AnvilCraft charge collectors can receive.

## Charge Generation

When a Blast Crystal is hit by an explosion, it reads the explosion radius and the distance from the explosion center to the center of its own block, then generates charge at its own position. With the default config, larger and closer explosions generate more charge:

- Near the edge of the explosion effect, it generates about `18 * explosion radius` charge.
- When it is close enough to the explosion center, the distance bonus can reach up to 4x, or about `72 * explosion radius` charge.
- The distance calculation subtracts 1 block from both the explosion radius and the distance, so explosions close to the crystal are more likely to receive the full bonus.

This charge is sent directly to the charge collector system, and it is also added to the crystal's own internal accumulated charge.

## Detonation and Cracking

A Blast Crystal remembers its internal accumulated charge. When the accumulated charge reaches the detonation threshold for its current stage, the crystal starts a short countdown. With the default config, the countdown is set to `15 - random 1~6 ticks`; it then decreases on server ticks and detonates when the countdown reaches 1.

The detonation creates a TNT-type explosion with power 6 at the crystal center, and clears the accumulated charge. After the detonation condition has been reached, later charge decay does not cancel the countdown.

After detonating, the crystal has a chance to move to the next state:

- Normal Blast Crystal: defaults to a 50% chance to become a Cracked Blast Crystal, with a small random variation in the actual roll.
- Cracked Blast Crystal: defaults to a 90% chance to disappear.
- Damaged Blast Crystal: defaults to a 70% chance to trigger degradation, but the current implementation keeps it in the damaged state.

## Accumulated Charge Decay

Accumulated charge below 10 is cleared directly. Once it reaches 10, it decays by the default multiplier of 0.95 each tick until it falls below 10 again and is cleared.

Each stage also uses a different detonation threshold:

- Normal Blast Crystal: base threshold 128. The higher the accumulated charge is, the lower the threshold becomes, reduced by `accumulated charge * 0.5`.
- Damaged Blast Crystal: base threshold 96. The higher the accumulated charge is, the lower the threshold becomes, reduced by `accumulated charge * 0.1`.
- Cracked Blast Crystal: fixed threshold 32.
