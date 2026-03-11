package icu.takeneko.nekoplus.mixin.anvilcraft;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import dev.dubhe.anvilcraft.network.LaserEmitPacket;
import icu.takeneko.nekoplus.block.tile.HighEnergyLaserBlockEntity;
import icu.takeneko.nekoplus.block.tile.StampingPlatformBlockEntity;
import icu.takeneko.nekoplus.foundation.block.tile.NPInspectionSupported;
import icu.takeneko.nekoplus.internal.LaserRendererInternals;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.UnknownNullability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Mixin(BaseLaserBlockEntity.class)
public abstract class BaseLaserBlockEntityMixin
    extends BlockEntity
    implements LaserRendererInternals.Extension, NPInspectionSupported {

    @Shadow
    @UnknownNullability
    protected BlockPos irradiateBlockPos;

    @Shadow
    public abstract void markChanged();

    @Shadow
    protected abstract int getBaseLaserLevel();

    @Shadow
    protected int laserLevel;

    @Unique
    private final HashMap<BaseLaserBlockEntity, Boolean> he$dataMap = new HashMap<>();

    @Unique
    private boolean he$isPureHELaser = false;

    public BaseLaserBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public boolean hasPureHELaserSource() {
        return he$isPureHELaser;
    }

    @WrapMethod(
        method = "emitLaser"
    )
    void wrapEmitLaser(Direction direction, Operation<Void> original) {
        BlockPos oldValue = this.irradiateBlockPos;
        original.call(direction);
        if (oldValue != null
            && !oldValue.equals(irradiateBlockPos)
            && level.getBlockEntity(oldValue) instanceof StampingPlatformBlockEntity blockEntity
        ) {
            blockEntity.setLaserEmitterPosition(null);
            blockEntity.setLaserTarget(false);
        }
    }

    @Inject(
        method = "tick",
        at = @At("RETURN")
    )
    void updateTargetBlock(Level level, CallbackInfo ci) {
        if (irradiateBlockPos != null && level.getBlockEntity(irradiateBlockPos) instanceof StampingPlatformBlockEntity blockEntity) {
            if (this.laserLevel >= 64) {
                blockEntity.setLaserEmitterPosition(getBlockPos());
                blockEntity.setLaserTarget(true);
            } else {
                blockEntity.setLaserEmitterPosition(null);
                blockEntity.setLaserTarget(false);
            }
        }
    }


    @Override
    public void updateFromSource(BaseLaserBlockEntity blockEntity, boolean value, Set<BaseLaserBlockEntity> context) {
        if (context.contains(this)) return;
//        if (!he$dataMap.containsKey(blockEntity) || !this.irradiateSelfLaserBlockSet.contains(blockEntity)) {
//            return;
//        }
        he$dataMap.put(blockEntity, value);
        flushPureHESourceState(context);
    }

    @Override
    public void setPureHELaserSourceDirect(boolean value) {
        he$isPureHELaser = value;
    }

    @Unique
    private void flushPureHESourceState(Set<BaseLaserBlockEntity> context) {
        boolean newValue = !he$dataMap.isEmpty();
        for (boolean b : he$dataMap.values()) {
            if (!b) {
                newValue = false;
                break;
            }
        }
        setPureHELaserValue(newValue, context);
    }

    @Unique
    public void setPureHELaserValue(boolean value, Set<BaseLaserBlockEntity> context) {
        if (he$isPureHELaser != value) {
            this.markChanged();
        }
        he$isPureHELaser = value;

        if (irradiateBlockPos != null) {
            if (level.getBlockEntity(irradiateBlockPos) instanceof LaserRendererInternals.Extension extension) {
                context.add((BaseLaserBlockEntity) (Object) this);
                extension.updateFromSource((BaseLaserBlockEntity) (Object) this, he$isPureHELaser, context);
            }
        }
    }

    @WrapOperation(
        method = "emitLaser",
        at = @At(value = "INVOKE", target = "Ldev/dubhe/anvilcraft/block/entity/BaseLaserBlockEntity;onIrradiated(Ldev/dubhe/anvilcraft/block/entity/BaseLaserBlockEntity;)V")
    )
    void updateFromPureSource(BaseLaserBlockEntity instance, BaseLaserBlockEntity baseLaserBlockEntity, Operation<Void> original) {
        original.call(instance, baseLaserBlockEntity);
        if (instance instanceof LaserRendererInternals.Extension extension && (Object) this instanceof HighEnergyLaserBlockEntity heLaser) {
            extension.updateFromSource(heLaser, he$isPureHELaser, new HashSet<>());
        }
    }

    @WrapOperation(
        method = "syncTo",
        at = @At(value = "NEW", target = "(ILnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)Ldev/dubhe/anvilcraft/network/LaserEmitPacket;")
    )
    LaserEmitPacket fillCustomValue1(int laserLevel, BlockPos laserBlockPos, BlockPos irradiateBlockPos, Operation<LaserEmitPacket> original) {
        LaserEmitPacket packet = original.call(laserLevel, laserBlockPos, irradiateBlockPos);
        LaserRendererInternals.PacketAccess packetAccess = (LaserRendererInternals.PacketAccess) packet;
        packetAccess.setPureHELaserSource(this.he$isPureHELaser);
        return packet;
    }

    @WrapOperation(
        method = "tick",
        at = @At(value = "NEW", target = "(ILnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)Ldev/dubhe/anvilcraft/network/LaserEmitPacket;")
    )
    LaserEmitPacket fillCustomValue2(int laserLevel, BlockPos laserBlockPos, BlockPos irradiateBlockPos, Operation<LaserEmitPacket> original) {
        LaserEmitPacket packet = original.call(laserLevel, laserBlockPos, irradiateBlockPos);
        LaserRendererInternals.PacketAccess packetAccess = (LaserRendererInternals.PacketAccess) packet;
        packetAccess.setPureHELaserSource(this.he$isPureHELaser);
        return packet;
    }

    @WrapOperation(
        method = "emitLaser",
        at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I")
    )
    int modifyMaxHurt(int a, int b, Operation<Integer> original) {
        return original.call(he$isPureHELaser ? a * 64 : a, b);
    }

    @Inject(
        method = "onIrradiated",
        at = @At("RETURN")
    )
    void handleNewSource(BaseLaserBlockEntity baseLaserBlockEntity, CallbackInfo ci) {
        boolean value = false;
        if (baseLaserBlockEntity instanceof LaserRendererInternals.Extension extension) {
            value = extension.hasPureHELaserSource();
        }
        he$dataMap.put(baseLaserBlockEntity, value);
        Set<BaseLaserBlockEntity> set = new HashSet<>();
        set.add((BaseLaserBlockEntity) (Object) this);
        //updateFromSource(baseLaserBlockEntity, value, set);
    }

    @Inject(
        method = "onCancelingIrradiation",
        at = @At("RETURN")
    )
    void handleSourceRemoval(BaseLaserBlockEntity baseLaserBlockEntity, CallbackInfo ci) {
        he$dataMap.remove(baseLaserBlockEntity);
        flushPureHESourceState(new HashSet<>());
    }

    @Override
    public void onTurnOff() {
        if (irradiateBlockPos != null && level.getBlockEntity(irradiateBlockPos) instanceof StampingPlatformBlockEntity irradiateBlockEntity) {
            irradiateBlockEntity.setLaserEmitterPosition(null);
            irradiateBlockEntity.setLaserTarget(false);
        }
    }

    @Override
    public void echo(CommandSourceStack source) {
        StringBuilder sb = new StringBuilder();
        sb.append("BaseLaserBlockEntity[");
        sb.append("he$isPureHELaser=").append(he$isPureHELaser);
        sb.append("]");
        source.sendSystemMessage(Component.literal(sb.toString()));
    }
}
