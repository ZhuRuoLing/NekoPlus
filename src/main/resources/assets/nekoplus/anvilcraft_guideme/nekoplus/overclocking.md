---
navigation:
  title: "猫+ | 超频"
  icon: "minecraft:soul_torch"
  parent: "nekoplus.md"
  position: 301
item_ids:
- minecraft:soul_torch
---
# 超频机制

<ItemImage id="minecraft:soul_torch" scale="8" />

可超频的机器在电网的功率余量足够进行1倍超频，且机器**处于可超频状态**时，该机器会进行超频，超频是无损的。

每一等级的超频额外使用一份当前状态下的供电功率，并将工作时长缩短为原先的 1/2 倍，向下取整，超频倍率带来的工作时长缩短可以叠加。

超频计算器会使所有可超频的机器尽可能的超频。

> 部分机器，如充电器，支持使用<ItemLink id="nekoplus:charged_levitation_powder" />切换超频开关。

## 支持超频的机器

- <ItemLink id="anvilcraft:charger" />
  - 在对物品充电时会超频
  - 超频倍率上限为100倍
- <ItemLink id="nekoplus:particle_stabilizer" />
  - 在 <NeoColor id="#55ffff">*冷却中* </NeoColor> 状态下会超频
  - 超频倍率上限为100倍