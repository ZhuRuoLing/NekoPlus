---
navigation:
  title: "Neko+ | Foundry Sand"
  icon: "nekoplus:foundry_sand"
items:
  - nekoplus:foundry_sand

---

# Foundry Sand

<item id="nekoplus:foundry_sand"/>

A standard molding sand used in precision casting. Foundry sand itself possesses excellent refractoriness and thermal insulation properties; when directly surrounding a high-temperature block, it provides stable heat retention for the target block, significantly slowing down its cooling and temperature drop rate.

## Heat Retention

The thermal insulation function of foundry sand requires it to be in direct contact with the high-temperature block. Each foundry sand block directly contacting the high-temperature block provides one level of thermal insulation, with each level slowing down the cooling countdown rate of the high-temperature block to 1/2 of its original speed.

Thermal insulation supports up to 4 foundry sand blocks functioning simultaneously. The cooling speeds corresponding to 0 to 4 foundry sand blocks are as follows:

- None placed (0 blocks): Maintains the default cooling countdown speed;
- Surrounded by 1: Cooling speed is reduced to `1/2` of the original;
- Surrounded by 2: Cooling speed is reduced to `1/4` of the original;
- Surrounded by 3: Cooling speed is reduced to `1/8` of the original;
- Surrounded by 4: Reaches the maximum heat retention limit, with cooling speed reduced to `1/16` of the original.