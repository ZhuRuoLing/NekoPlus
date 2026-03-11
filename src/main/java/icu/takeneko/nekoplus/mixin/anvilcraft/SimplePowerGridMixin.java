package icu.takeneko.nekoplus.mixin.anvilcraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import dev.dubhe.anvilcraft.api.power.IPowerComponent;
import dev.dubhe.anvilcraft.api.power.IPowerConsumer;
import dev.dubhe.anvilcraft.api.power.IPowerProducer;
import dev.dubhe.anvilcraft.api.power.IPowerStorage;
import dev.dubhe.anvilcraft.api.power.IPowerTransmitter;
import dev.dubhe.anvilcraft.api.power.PowerComponentInfo;
import dev.dubhe.anvilcraft.api.power.PowerComponentType;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import dev.dubhe.anvilcraft.api.power.SimplePowerGrid;
import icu.takeneko.nekoplus.foundation.grid.OffCenterPowerComponent;
import icu.takeneko.nekoplus.foundation.grid.OffCenterPowerComponentInfo;
import icu.takeneko.nekoplus.internal.SimplePowerGridInternals;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(SimplePowerGrid.class)
public class SimplePowerGridMixin implements SimplePowerGridInternals.Access {

    @Final
    @Shadow
    @Mutable
    public static Codec<SimplePowerGrid> CODEC;

    @Unique
    private final List<OffCenterPowerComponentInfo> he$offCenterPowerComponents = new ArrayList<>();

    @Unique
    private final Set<BlockPos> he$offCenterPowerComponentsIndex = new HashSet<>();

    @WrapOperation(
        method = "<clinit>",
        at = @At(value = "FIELD", target = "Ldev/dubhe/anvilcraft/api/power/SimplePowerGrid;CODEC:Lcom/mojang/serialization/Codec;")
    )
    private static void modifyCodec(Codec<SimplePowerGrid> value, Operation<Void> original) {
        original.call(SimplePowerGridInternals.CODEC);
    }

    @Inject(
        method = "<init>(Ldev/dubhe/anvilcraft/api/power/PowerGrid;)V",
        at = @At(value = "INVOKE", target = "Ljava/util/Set;addAll(Ljava/util/Collection;)Z", ordinal = 3)
    )
    private void addOffCenterComponents(
        PowerGrid grid,
        CallbackInfo ci,
        @Local Set<IPowerComponent> powerComponents
    ) {
        for (IPowerComponent component : grid.getComponents()) {
            if (component instanceof OffCenterPowerComponent) {
                switch (component.getComponentType()) {
                    case STORAGE -> {
                        IPowerStorage it = (IPowerStorage) component;
                        he$offCenterPowerComponents.add(
                            new OffCenterPowerComponentInfo(
                                it.getPos(),
                                0,
                                0,
                                it.getPowerAmount(),
                                it.getCapacity(),
                                it.getShape(),
                                PowerComponentType.STORAGE
                            )
                        );
                    }

                    case CONSUMER -> {
                        IPowerConsumer it = (IPowerConsumer) component;
                        he$offCenterPowerComponents.add(
                            new OffCenterPowerComponentInfo(
                                it.getPos(),
                                it.getInputPower(),
                                0,
                                0,
                                0,
                                it.getShape(),
                                PowerComponentType.CONSUMER
                            )
                        );
                    }
                    case PRODUCER -> {
                        IPowerProducer it = (IPowerProducer) component;
                        he$offCenterPowerComponents.add(
                            new OffCenterPowerComponentInfo(
                                it.getPos(),
                                0,
                                it.getOutputPower(),
                                0,
                                0,
                                it.getShape(),
                                PowerComponentType.PRODUCER
                            )
                        );
                    }
                    case TRANSMITTER -> {
                        IPowerTransmitter it = (IPowerTransmitter) component;
                        he$offCenterPowerComponents.add(
                            new OffCenterPowerComponentInfo(
                                it.getPos(),
                                0,
                                0,
                                0,
                                0,
                                it.getShape(),
                                PowerComponentType.TRANSMITTER
                            )
                        );
                    }
                    default -> he$offCenterPowerComponents.add(
                        new OffCenterPowerComponentInfo(
                            component.getPos(),
                            0,
                            0,
                            0,
                            0,
                            component.getShape(),
                            PowerComponentType.INVALID
                        )
                    );
                }
            }
        }
    }

    @WrapOperation(
        method = "lambda$createMergedOutlineShape$12",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
        )
    )
    <E> boolean avoidAddOffCenterToMerge(
        List<VoxelShape> instance,
        E e,
        Operation<Boolean> original,
        @Local(index = 3) PowerComponentInfo it
    ) {
        if (he$offCenterPowerComponentsIndex.contains(it.pos())) {
            return false;
        }
        return original.call(instance, e);
    }

    @WrapOperation(
        method = "lambda$createMergedOutlineShape$12",
        at = @At(
            value = "INVOKE",
            target = "Ldev/dubhe/anvilcraft/util/ShapeUtil;threadedJoin(Ljava/util/List;Lnet/minecraft/world/phys/shapes/BooleanOp;Ljava/util/concurrent/ExecutorService;)Ljava/util/concurrent/Future;"
        )
    )
    Future<VoxelShape> handleOffCenterInputs(
        List<VoxelShape> shapes,
        BooleanOp function,
        ExecutorService executor,
        Operation<Future<VoxelShape>> original
    ) {
        for (OffCenterPowerComponentInfo info : he$offCenterPowerComponents) {
            shapes.add(Shapes.create(info.range()));
        }
        return original.call(shapes, function, executor);
    }

    @Override
    public List<OffCenterPowerComponentInfo> getOffCenterComponents() {
        return he$offCenterPowerComponents;
    }

    @Override
    public void setOffCenterComponents(List<OffCenterPowerComponentInfo> value) {
        he$offCenterPowerComponents.addAll(value);
        for (OffCenterPowerComponentInfo info : value) {
            he$offCenterPowerComponentsIndex.add(info.pos());
        }
    }
}
