package icu.takeneko.nekoplus.mixin.anvilcraft.laser;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.dubhe.anvilcraft.api.laser.LaserComponentMap;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import dev.dubhe.anvilcraft.network.LaserEmitPacket;
import icu.takeneko.nekoplus.block.tile.HighEnergyLaserBlockEntity;
import icu.takeneko.nekoplus.foundation.block.tile.NPInspectionSupported;
import icu.takeneko.nekoplus.internal.LaserRendererInternals;
import icu.takeneko.nekoplus.internal.StampingPlatformsInternals;
import icu.takeneko.nekoplus.all.NPLaserComponents;
import icu.takeneko.nekoplus.content.tile.laser.PureHighEnergyLaserComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseLaserBlockEntity.class)
public abstract class BaseLaserBlockEntityMixin
    extends BlockEntity
    implements LaserRendererInternals.Extension, NPInspectionSupported {

    @Shadow
    @UnknownNullability
    protected BlockPos irradiateBlockPos;

    @Shadow
    protected int laserLevel;

    @Shadow
    protected abstract int getBaseLaserLevel();

    @Shadow
    protected abstract int getGammaLaserLevel();

    public BaseLaserBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(
        method = "createLaserComponents",
        at = @At(
            value = "INVOKE",
            target = "Ldev/dubhe/anvilcraft/block/entity/BaseLaserBlockEntity;configureLaserComponents(Ldev/dubhe/anvilcraft/api/laser/LaserComponentMap;)V",
            shift = At.Shift.AFTER
        )
    )
    void configurePureHighEnergyComponent(
        boolean gamma,
        CallbackInfoReturnable<LaserComponentMap> cir,
        @Local(name = "result") LaserComponentMap components
    ) {
        int baseStrength = gamma ? getGammaLaserLevel() : getBaseLaserLevel();
        boolean contributesOwnLaser = !((Object) this instanceof HighEnergyLaserBlockEntity) && baseStrength > 0;
        if (components.get(NPLaserComponents.PURE_HIGH_ENERGY) == null || contributesOwnLaser) {
            components.put(NPLaserComponents.PURE_HIGH_ENERGY, PureHighEnergyLaserComponent.IMPURE);
        }
    }

    @WrapMethod(method = "emitLaser")
    void wrapEmitLaser(Direction direction, Operation<Void> original) {
        BlockPos oldTarget = irradiateBlockPos;
        original.call(direction);
        if (level != null && oldTarget != null && !oldTarget.equals(irradiateBlockPos)
            && level.getBlockEntity(oldTarget) instanceof StampingPlatformsInternals.LaserTarget tracker) {
            tracker.nekoplus$setEmitter(getBlockPos(), false);
        }
        nekoplus$updateTarget();
    }

    @Inject(method = "tick", at = @At("RETURN"))
    void updateTargetBlock(Level level, CallbackInfo ci) {
        nekoplus$updateTarget();
    }

    @Unique
    private void nekoplus$updateTarget() {
        if (level != null && !level.isClientSide() && irradiateBlockPos != null
            && level.getBlockEntity(irradiateBlockPos) instanceof StampingPlatformsInternals.LaserTarget tracker) {
            tracker.nekoplus$setEmitter(getBlockPos(), laserLevel >= 64);
        }
    }

    @Override
    public boolean targets(BlockPos pos) {
        return pos.equals(irradiateBlockPos);
    }

    @WrapOperation(
        method = "syncTo",
        at = @At(value = "NEW", target = "(ILnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Z)Ldev/dubhe/anvilcraft/network/LaserEmitPacket;")
    )
    LaserEmitPacket fillCustomValue1(
        int laserLevel,
        BlockPos laserBlockPos,
        BlockPos irradiateBlockPos,
        boolean gamma,
        Operation<LaserEmitPacket> original
    ) {
        LaserEmitPacket packet = original.call(laserLevel, laserBlockPos, irradiateBlockPos, gamma);
        LaserRendererInternals.PacketAccess packetAccess = (LaserRendererInternals.PacketAccess) (Object) packet;
        packetAccess.setPureHELaserSource(LaserRendererInternals.hasPureHELaserSource((BaseLaserBlockEntity) (Object) this));
        return packet;
    }

    @WrapOperation(
        method = "tick",
        at = @At(value = "NEW", target = "(ILnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Z)Ldev/dubhe/anvilcraft/network/LaserEmitPacket;")
    )
    LaserEmitPacket fillCustomValue2(
        int level,
        BlockPos laserPos,
        BlockPos irradiatePos,
        boolean gamma,
        Operation<LaserEmitPacket> original
    ) {
        LaserEmitPacket packet = original.call(level, laserPos, irradiatePos, gamma);
        LaserRendererInternals.PacketAccess packetAccess = (LaserRendererInternals.PacketAccess) (Object) packet;
        packetAccess.setPureHELaserSource(LaserRendererInternals.hasPureHELaserSource((BaseLaserBlockEntity) (Object) this));
        return packet;
    }


    @Override
    public void onTurnOff() {
        if (level != null && !level.isClientSide() && irradiateBlockPos != null
            && level.getBlockEntity(irradiateBlockPos) instanceof StampingPlatformsInternals.LaserTarget tracker) {
            tracker.nekoplus$setEmitter(getBlockPos(), false);
        }
    }
}
