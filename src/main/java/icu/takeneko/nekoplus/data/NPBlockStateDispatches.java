package icu.takeneko.nekoplus.data;

import com.mojang.math.Quadrant;
import dev.anvilcraft.lib.v2.registrum.providers.DataGenContext;
import dev.anvilcraft.lib.v2.registrum.providers.generators.RegistrumBlockModelGenerator;
import dev.anvilcraft.lib.v2.registrum.providers.generators.model.PropertyDispatchWrap;
import dev.anvilcraft.lib.v2.util.nullness.NonNullBiConsumer;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.block.CatAnvilBlock;
import icu.takeneko.nekoplus.block.FusionReactorControllerBlock;
import icu.takeneko.nekoplus.block.HighEnergyLaserBlock;
import icu.takeneko.nekoplus.block.ParticleStabilizerBlock;
import icu.takeneko.nekoplus.block.ProgrammableLogicGateBlock;
import icu.takeneko.nekoplus.block.ShulkerHatchBlock;
import icu.takeneko.nekoplus.block.FatAnvilBlock;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.ConditionBuilder;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

@SuppressWarnings({"Convert2Lambda", "deprecation"})
public class NPBlockStateDispatches {
    public static final TextureSlot SLOT_1 = TextureSlot.create("1");
    public static final TextureSlot SLOT_3 = TextureSlot.create("3");
    public static final TextureSlot SLOT_ALL = TextureSlot.create("all");
    public static final TextureSlot SLOT_OVERLAY = TextureSlot.create("overlay");

    public static final ModelTemplate PARTICLE_STABILIZER_MODEL = new ModelTemplate(
        Optional.of(NekoPlus.location("block/particle_stabilizer_template")),
        Optional.empty(),
        SLOT_1
    );

    public static final ModelTemplate HIGH_ENERGY_LASER_MODEL = new ModelTemplate(
        Optional.of(NekoPlus.location("block/high_energy_laser_template")),
        Optional.empty(),
        SLOT_3
    );

    public static final ModelTemplate HATCH_BASE_MODEL = new ModelTemplate(
        Optional.of(NekoPlus.location("block/hatch_template")),
        Optional.empty(),
        SLOT_ALL,
        SLOT_OVERLAY
    );

    public static NonNullBiConsumer<DataGenContext<Block, HighEnergyLaserBlock>, RegistrumBlockModelGenerator> highEnergyLaser1() {
        return (context, generator) -> {
            Identifier textureOff = NekoPlus.location("block/high_energy_laser_off");
            Identifier textureOverload = NekoPlus.location("block/high_energy_laser_overload");
            Identifier texture = NekoPlus.location("block/high_energy_laser");

            Identifier normalModel = generator.withParent(HIGH_ENERGY_LASER_MODEL)
                .suffix("")
                .texture(SLOT_3, texture)
                .build(context.get());

            Identifier offModel = generator.withParent(HIGH_ENERGY_LASER_MODEL)
                .suffix("_off")
                .texture(SLOT_3, textureOff)
                .build(context.get());

            Identifier overloadModel = generator.withParent(HIGH_ENERGY_LASER_MODEL)
                .suffix("_overload")
                .texture(SLOT_3, textureOverload)
                .build(context.get());

            PropertyDispatchWrap.C3<MultiVariant, Boolean, Boolean, Direction> dispatch = PropertyDispatchWrap.initial(
                HighEnergyLaserBlock.POWERED,
                HighEnergyLaserBlock.OVERLOAD,
                HighEnergyLaserBlock.FACING
            );
            for (Direction direction : Direction.values()) {
                int yRot = direction.getAxis() != Direction.Axis.Y ? ((int) direction.toYRot() + 180) % 360 : 0;
                int xRot = 0;
                if (direction.getAxis() == Direction.Axis.Y) {
                    if (direction == Direction.DOWN) {
                        xRot = 180;
                    }
                } else {
                    xRot = 90;
                }

                VariantMutator mutator = VariantMutator.X_ROT
                    .withValue(Quadrant.parseJson(xRot))
                    .then(
                        VariantMutator.Y_ROT
                            .withValue(Quadrant.parseJson(yRot))
                    );

                dispatch.select(
                    true,
                    false,
                    direction,
                    BlockModelGenerators.plainVariant(offModel).with(mutator)
                ).select(
                    true,
                    true,
                    direction,
                    BlockModelGenerators.plainVariant(offModel).with(mutator)
                ).select(
                    false,
                    true,
                    direction,
                    BlockModelGenerators.plainVariant(overloadModel).with(mutator)
                ).select(
                    false,
                    false,
                    direction,
                    BlockModelGenerators.plainVariant(normalModel).with(mutator)
                );
            }

            generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(context.get())
                    .with(dispatch.dispatch())
            );
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, HighEnergyLaserBlock>, RegistrumBlockModelGenerator> highEnergyLaser() {
        return new NonNullBiConsumer<>() {
            @Override
            public void accept(
                @NonNull DataGenContext<Block, HighEnergyLaserBlock> context,
                @NonNull RegistrumBlockModelGenerator generator
            ) {
                Identifier textureOff = NekoPlus.location("block/high_energy_laser_off");
                Identifier textureOverload = NekoPlus.location("block/high_energy_laser_overload");
                Identifier texture = NekoPlus.location("block/high_energy_laser");

                Identifier normalModel = generator.withParent(HIGH_ENERGY_LASER_MODEL)
                    .suffix("")
                    .texture(SLOT_3, texture)
                    .build(context.get());

                Identifier offModel = generator.withParent(HIGH_ENERGY_LASER_MODEL)
                    .suffix("_off")
                    .texture(SLOT_3, textureOff)
                    .build(context.get());

                Identifier overloadModel = generator.withParent(HIGH_ENERGY_LASER_MODEL)
                    .suffix("_overload")
                    .texture(SLOT_3, textureOverload)
                    .build(context.get());

                PropertyDispatchWrap.C3<MultiVariant, Boolean, Boolean, Direction> dispatch = PropertyDispatchWrap.initial(
                    HighEnergyLaserBlock.POWERED,
                    HighEnergyLaserBlock.OVERLOAD,
                    HighEnergyLaserBlock.FACING
                );
                for (Direction direction : Direction.values()) {
                    int yRot = direction.getAxis() != Direction.Axis.Y ? ((int) direction.toYRot() + 180) % 360 : 0;
                    int xRot = 0;
                    if (direction.getAxis() == Direction.Axis.Y) {
                        if (direction == Direction.DOWN) {
                            xRot = 180;
                        }
                    } else {
                        xRot = 90;
                    }

                    VariantMutator mutator = VariantMutator.X_ROT
                        .withValue(Quadrant.parseJson(xRot))
                        .then(
                            VariantMutator.Y_ROT
                                .withValue(Quadrant.parseJson(yRot))
                        );

                    dispatch.select(
                        true,
                        false,
                        direction,
                        BlockModelGenerators.plainVariant(offModel).with(mutator)
                    ).select(
                        true,
                        true,
                        direction,
                        BlockModelGenerators.plainVariant(offModel).with(mutator)
                    ).select(
                        false,
                        true,
                        direction,
                        BlockModelGenerators.plainVariant(overloadModel).with(mutator)
                    ).select(
                        false,
                        false,
                        direction,
                        BlockModelGenerators.plainVariant(normalModel).with(mutator)
                    );
                }

                generator.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(context.get())
                        .with(dispatch.dispatch())
                );
            }
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, ParticleStabilizerBlock>, RegistrumBlockModelGenerator> particleStabilizer() {
        return new NonNullBiConsumer<>() {
            @Override
            public void accept(
                @NonNull DataGenContext<Block, ParticleStabilizerBlock> context,
                @NonNull RegistrumBlockModelGenerator generator
            ) {
                Identifier normalModel = generator.withParent(PARTICLE_STABILIZER_MODEL)
                    .suffix("")
                    .texture(SLOT_1, NekoPlus.location("block/particle_stabilizer"))
                    .build(context.get());

                Identifier overloadModel = generator.withParent(PARTICLE_STABILIZER_MODEL)
                    .suffix("_overload")
                    .texture(SLOT_1, NekoPlus.location("block/particle_stabilizer_overload"))
                    .build(context.get());

                Identifier freezingModel = generator.withParent(PARTICLE_STABILIZER_MODEL)
                    .suffix("_freezing")
                    .texture(SLOT_1, NekoPlus.location("block/particle_stabilizer_freezing"))
                    .build(context.get());

                MultiVariantGenerator variantGenerator = MultiVariantGenerator.dispatch(context.get())
                    .with(
                        PropertyDispatchWrap.initial(ParticleStabilizerBlock.OVERLOAD, ParticleStabilizerBlock.COOLING)
                            .select(
                                true,
                                false,
                                BlockModelGenerators.plainVariant(overloadModel)
                            )
                            .select(
                                true,
                                true,
                                BlockModelGenerators.plainVariant(overloadModel)
                            )
                            .select(
                                false,
                                true,
                                BlockModelGenerators.plainVariant(freezingModel)
                            )
                            .select(
                                false,
                                false,
                                BlockModelGenerators.plainVariant(normalModel)
                            )
                            .dispatch()
                    );
                generator.blockStateOutput.accept(variantGenerator);
            }
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, FusionReactorControllerBlock>, RegistrumBlockModelGenerator> fusionReactorController() {
        return new NonNullBiConsumer<>() {
            @Override
            public void accept(
                @NonNull DataGenContext<Block, FusionReactorControllerBlock> context,
                @NonNull RegistrumBlockModelGenerator generator
            ) {
                Identifier modelId = generator.withParent(HATCH_BASE_MODEL)
                    .texture(SLOT_ALL, NekoPlus.location("block/royal_steel_casing"))
                    .texture(SLOT_OVERLAY, NekoPlus.location("block/laser_confinement_fusion"))
                    .build(context.get());

                PropertyDispatchWrap.C1<MultiVariant, Direction> dispatch = PropertyDispatchWrap.initial(
                    FusionReactorControllerBlock.FACING
                );

                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    int yRot = ((int) direction.toYRot() + 180) % 360;
                    int xRot = 90;

                    VariantMutator mutator = VariantMutator.X_ROT
                        .withValue(Quadrant.parseJson(xRot))
                        .then(VariantMutator.Y_ROT.withValue(Quadrant.parseJson(yRot)))
                        .then(VariantMutator.UV_LOCK.withValue(true));

                    dispatch.select(
                        direction,
                        BlockModelGenerators.plainVariant(modelId).with(mutator)
                    );
                }

                generator.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(context.get())
                        .with(dispatch.dispatch())
                );
            }
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, ProgrammableLogicGateBlock>, RegistrumBlockModelGenerator> programmableLogicGate() {
        return new NonNullBiConsumer<>() {
            @Override
            public void accept(
                @NonNull DataGenContext<Block, ProgrammableLogicGateBlock> context,
                @NonNull RegistrumBlockModelGenerator generator
            ) {
                Identifier gateModel = NekoPlus.location("block/programmable_logic_gate");
                Identifier torchOnModel = NekoPlus.location("block/programmable_logic_gate_torch_on");
                Identifier torchOffModel = NekoPlus.location("block/programmable_logic_gate_torch_off");

                MultiPartGenerator gen = MultiPartGenerator.multiPart(context.get());

                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    int yRot = ((int) direction.toYRot() + 180) % 360;
                    MultiVariant gateVariant = BlockModelGenerators.plainVariant(gateModel)
                        .with(VariantMutator.Y_ROT.withValue(Quadrant.parseJson(yRot)));
                    gen.with(
                        condition().term(ProgrammableLogicGateBlock.FACING, direction),
                        gateVariant
                    );

                    int yRotE = 0;
                    for (BooleanProperty p : ProgrammableLogicGateBlock.PROPERTIES_ENABLE) {
                        int yR = yRotE + yRot;
                        MultiVariant torchOnVariant = BlockModelGenerators.plainVariant(torchOnModel)
                            .with(VariantMutator.Y_ROT.withValue(Quadrant.parseJson(yR)));
                        MultiVariant torchOffVariant = BlockModelGenerators.plainVariant(torchOffModel)
                            .with(VariantMutator.Y_ROT.withValue(Quadrant.parseJson(yR)));

                        gen.with(
                            condition().term(p, true).term(ProgrammableLogicGateBlock.FACING, direction),
                            torchOnVariant
                        ).with(
                            condition().term(p, false).term(ProgrammableLogicGateBlock.FACING, direction),
                            torchOffVariant
                        );

                        yRotE += 90;
                    }
                }
                generator.blockStateOutput.accept(gen);
            }
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, FatAnvilBlock>, RegistrumBlockModelGenerator> titaniumAlloyAnvil() {
        return new NonNullBiConsumer<>() {
            @Override
            public void accept(
                @NonNull DataGenContext<Block, FatAnvilBlock> context,
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

    public static NonNullBiConsumer<DataGenContext<Block, CatAnvilBlock>, RegistrumBlockModelGenerator> catAnvil() {
        return new NonNullBiConsumer<>() {
            @Override
            public void accept(
                @NonNull DataGenContext<Block, CatAnvilBlock> ctx,
                @NonNull RegistrumBlockModelGenerator gen
            ) {
                var modelId = NekoPlus.location("block/cat_anvil");

                var dispatchWrap = PropertyDispatchWrap.initial(
                    AnvilBlock.FACING
                );

                for (var direction : Direction.Plane.HORIZONTAL) {
                    int yRot = ((int) direction.toYRot()) % 360;
                    dispatchWrap.select(
                        direction,
                        BlockModelGenerators.plainVariant(modelId)
                            .with(VariantMutator.Y_ROT.withValue(Quadrant.parseJson(yRot)))
                    );
                }

                gen.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(ctx.get())
                        .with(dispatchWrap.dispatch())
                );
            }
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, ShulkerHatchBlock>, RegistrumBlockModelGenerator> shulkerHatch() {
        return new NonNullBiConsumer<>() {
            @Override
            public void accept(
                @NonNull DataGenContext<Block, ShulkerHatchBlock> ctx,
                @NonNull RegistrumBlockModelGenerator gen
            ) {
                var modelId = NekoPlus.location("block/shulker_hatch");

                var dispatchWrap = PropertyDispatchWrap.initial(
                    ShulkerHatchBlock.FACING
                );

                for (var direction : Direction.Plane.HORIZONTAL) {
                    int yRot = ((int) direction.toYRot()) % 360;
                    dispatchWrap.select(
                        direction,
                        BlockModelGenerators.plainVariant(modelId)
                            .with(VariantMutator.Y_ROT.withValue(Quadrant.parseJson(yRot)))
                    );
                }

                gen.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(ctx.get())
                        .with(dispatchWrap.dispatch())
                );
            }
        };
    }

    public static ConditionBuilder condition() {
        return new ConditionBuilder();
    }
}
