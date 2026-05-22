---
name: migrate-titanium-alloy-anvil-blockstate
overview: 参照 NPBlockStateDispatches.java 的新写法，将 NPBlocks.java 中 titanium_alloy_anvil 的旧 blockstate 代码（使用 getVariantBuilder/forAllStates）迁移为 PropertyDispatchWrap + MultiVariantGenerator 的新写法
todos:
  - id: add-titanium-alloy-anvil-dispatch
    content: 在 NPBlockStateDispatches.java 中新增 titaniumAlloyAnvitil() 方法，实现基于 AnvilBlock.FACING 的单属性方向分发
    status: completed
  - id: update-npblocks-reference
    content: 将 NPBlocks.java 中 TITANIUM_ALLOY_ANVIL 的 blockstate 回调替换为 NPBlockStateDispatches::titaniumAlloyAnvil 方法引用
    status: completed
    dependencies:
      - add-titanium-alloy-anvil-dispatch
---

## 产品概述

将 `TitaniumAlloyAnvilBlock`（钛合金铁砧）的方块状态数据生成代码从旧的 `getVariantBuilder/forAllStates` 模式迁移到新的 `PropertyDispatchWrap/MultiVariantGenerator.dispatch` 模式，参照 `NPBlockStateDispatches.java` 中已有的写法。

## 核心功能

- 在 `NPBlockStateDispatches.java` 中新增 `titaniumAlloyAnvil()` 静态方法，返回 `NonNullBiConsumer<DataGenContext<Block, TitaniumAlloyAnvilBlock>, RegistrumBlockModelGenerator>`
- 该方法使用 `PropertyDispatchWrap.C1<MultiVariant, Direction>` 处理 `AnvilBlock.FACING` 属性（4 个水平方向）
- 对每个水平方向计算 Y 轴旋转角度，通过 `VariantMutator.Y_ROT` + `Quadrant.parseJson(yRot)` 设置旋转
- 使用已存在的模型文件 `block/titanium_alloy_anvil`，无需创建新模板或纹理
- 通过 `MultiVariantGenerator.dispatch(block).with(dispatch.dispatch())` 输出 blockstate
- 将 `NPBlocks.java` 中 TITANIUM_ALLOY_ANVIL 的 `.blockstate((ctx, prov) -> {...})` 替换为 `.blockstate(NPBlockStateDispatches::titaniumAlloyAnvil)`

## 技术栈

- Minecraft Mod 开发（NeoForge），Java 17+
- Registrum v2 框架：`dev.anvilcraft.lib.v2.registrum`
- 数据生成 API：`net.minecraft.client.data.models.blockstates.MultiVariantGenerator`
- 方块状态分发：`PropertyDispatchWrap`、`VariantMutator`、`Quadrant`

## 实现方案

### 策略：单属性方向分发模式

旧代码使用 `prov.getVariantBuilder().forAllStates()` 回调方式，需要迁移到新 API 的声明式分发模式。核心差异：

1. **旧 API**：`ConfiguredModel.builder().rotationY(yRot).modelFile(modelFile).build()` —— 运行时回调遍历所有状态
2. **新 API**：`PropertyDispatchWrap.initial(FACING).select(direction, variant).dispatch()` —— 声明式属性映射

### 关键技术决策

- **C1 泛型选择**：铁砧只有 `AnvilBlock.FACING` 一个属性（Direction），因此使用 `PropertyDispatchWrap.C1<MultiVariant, Direction>`（参考 C3 用法推断 C1 存在且用法一致）
- **方向范围**：`AnvilBlock.FACING` 是 `Direction.Plane.HORIZONTAL`（N/S/E/W），不需要处理 UP/DOWN，因此只需 Y 轴旋转
- **Y 转角计算**：直接使用 `(int) value.toYRot() % 360`（与旧代码一致）
- **模型引用**：使用 `NekoPlus.location("block/titanium_alloy_anvil")` 作为已有模型的 Identifier，通过 `BlockModelGenerators.plainVariant(modelId)` 创建 variant

### 代码结构（NPBlockStateDispatches.java 新增方法）

```java
public static NonNullBiConsumer<DataGenContext<Block, TitaniumAlloyAnvilBlock>, RegistrumBlockModelGenerator> titaniumAlloyAnvil() {
    return new NonNullBiConsumer<>() {
        @Override
        public void accept(
            @NonNull DataGenContext<Block, TitaniumAlloyAnvilBlock> context,
            @NonNull RegistrumBlockModelGenerator generator
        ) {
            Identifier modelId = NekoPlus.location("block/titanium_alloy_anvil");
            
            PropertyDispatchWrap.C1<MultiVariant, Direction> dispatch = PropertyDispatchWrap.initial(
                AnvilBlock.FACING
            );
            
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                int yRot = ((int) direction.toYRot()) % 360;
                dispatch.select(
                    direction,
                    BlockModelGenerators.plainVariant(modelId)
                        .with(VariantMutator.Y_ROT.withValue(Quadrant.parseJson(yRot)))
                );
            }
            
            generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(context.get())
                    .with(dispatch.dispatch())
            );
        }
    };
}
```

### NPBlocks.java 变更

将第 293-303 行的 `.blockstate((ctx, prov) -> {...})` 整个替换为 `.blockstate(NPBlockStateDispatches::titaniumAlloyAnvil)`。

## 目录结构

```
src/main/java/icu/takeneko/nekoplus/
├── data/
│   └── NPBlockStateDispatches.java    # [MODIFY] 新增 titaniumAlloyAnvil() 方法 + 导入 AnvilBlock/TitaniumAlloyAnvilBlock
└── all/
    └── NPBlocks.java                   # [MODIFY] 替换 TITANIUM_ALLOY_ANVIL 的 .blockstate() 为方法引用
```

## 注意事项

- 需要在 `NPBlockStateDispatches.java` 中新增导入：`AnvilBlock`、`TitaniumAlloyAnvilBlock`
- `NPBlocks.java` 已有 `NPBlockStateDispatches` 和 `TitaniumAlloyAnvilBlock` 的导入，无需额外添加
- 旧代码中使用的 `ModelFile`、`ConfiguredModel`、`getVariantBuilder` 等 import 可在替换后移除（如果无其他地方使用的话——检查后发现 PROGRAMMABLE_LOGIC_GATE 和 FUSION_REACTOR_CONTROLLER、hatch 方法仍在使用这些旧 API，因此不能删除这些 import）

## Agent Extensions

### SubAgent

- **code-explorer**
- Purpose: 探索 `PropertyDispatchWrap.C1` 类型的具体定义和用法签名，确认 C1 泛型参数的正确顺序和 select 方法的确切签名
- Expected outcome: 获取 C1 的精确定义，确保生成的代码类型安全