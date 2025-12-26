package icu.takeneko.highenergyanvilology.mixin.anvilcraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import dev.dubhe.anvilcraft.network.LaserEmitPacket;
import icu.takeneko.highenergyanvilology.block.tile.HighEnergyLaserBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.block.entity.HEInspectionSupported;
import icu.takeneko.highenergyanvilology.internal.LaserRendererInternals;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
    implements LaserRendererInternals.Access, HEInspectionSupported {

    @Shadow
    @UnknownNullability
    protected BlockPos irradiateBlockPos;

    @Shadow
    public abstract void markChanged();

    @Shadow protected boolean changed;
    @Unique
    private final HashMap<BaseLaserBlockEntity, Boolean> he$dataMap = new HashMap<>();

    @Unique
    private boolean he$isPureHELaser = false;

    public BaseLaserBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(
        method = "<init>",
        at = @At("RETURN")
    )
    void reInitBlockSet(BlockEntityType<?> type, BlockPos pos, BlockState blockState, CallbackInfo ci) {
    }

    @Override
    public boolean hasPureHELaserSource() {
        return he$isPureHELaser;
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
            if (level.getBlockEntity(irradiateBlockPos) instanceof LaserRendererInternals.Access access) {
                context.add((BaseLaserBlockEntity) (Object) this);
                access.updateFromSource((BaseLaserBlockEntity) (Object) this, he$isPureHELaser, context);
            }
        }
    }

    @WrapOperation(
        method = "emitLaser",
        at = @At(value = "INVOKE", target = "Ldev/dubhe/anvilcraft/block/entity/BaseLaserBlockEntity;onIrradiated(Ldev/dubhe/anvilcraft/block/entity/BaseLaserBlockEntity;)V")
    )
    void updateFromPureSource(BaseLaserBlockEntity instance, BaseLaserBlockEntity baseLaserBlockEntity, Operation<Void> original) {
        original.call(instance, baseLaserBlockEntity);
        if (instance instanceof LaserRendererInternals.Access access && (Object) this instanceof HighEnergyLaserBlockEntity heLaser) {
            access.updateFromSource(heLaser, he$isPureHELaser, new HashSet<>());
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
        if (baseLaserBlockEntity instanceof LaserRendererInternals.Access access) {
            value = access.hasPureHELaserSource();
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
    public void echo(CommandSourceStack source) {
        StringBuilder sb = new StringBuilder();
        sb.append("BaseLaserBlockEntity[");
        sb.append("he$isPureHELaser=").append(he$isPureHELaser);
        sb.append("]");
        source.sendSystemMessage(Component.literal(sb.toString()));
    }
}
