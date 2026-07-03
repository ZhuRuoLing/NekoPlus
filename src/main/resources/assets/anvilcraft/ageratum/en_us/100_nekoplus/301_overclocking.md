---
navigation:
  title: "NekoPlus | Overclocking"
  icon: "minecraft:soul_torch"
items:
  - minecraft:soul_torch
---

# Overclocking

<item id="minecraft:soul_torch"/>

When the power grid has enough spare power for 1x overclocking and a machine **is in an overclockable state**, that machine will overclock. Overclocking is lossless.

Each overclocking level uses one additional share of the machine's current power cost and reduces the work time to half of the previous duration, rounded down. Work-time reductions from overclocking levels can stack.

The overclocking calculator makes every overclockable machine overclock as much as possible.

> Some machines, such as the Charger, support using <ref item="nekoplus:charged_levitation_powder"/> to toggle overclocking.

## Machines That Support Overclocking

- <ref item="anvilcraft:charger"/>
  - Overclocks while charging items
  - Maximum overclocking multiplier: 100x
- <ref item="nekoplus:particle_stabilizer"/>
  - Overclocks while in the <color=#55ffff>*cooling down*</color> state
  - Maximum overclocking multiplier: 100x

