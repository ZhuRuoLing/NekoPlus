package icu.takeneko.highenergyanvilology.mixin.anvilcraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.IManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IAsyncAutoSyncBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IAutoPersistBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.field.FieldManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import icu.takeneko.highenergyanvilology.block.tile.AnvilonEmitterBlockEntity;
import icu.takeneko.highenergyanvilology.block.tile.HighEnergyLaserBlockEntity;
import icu.takeneko.highenergyanvilology.internal.LaserRendererInternals;
import net.minecraft.core.BlockPos;
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
import java.util.concurrent.locks.ReentrantLock;

@Mixin(BaseLaserBlockEntity.class)
public class BaseLaserBlockEntityMixin
    extends BlockEntity
    implements LaserRendererInternals.Access {

    @Shadow
    protected HashSet<BaseLaserBlockEntity> irradiateSelfLaserBlockSet;

    @Shadow
    @UnknownNullability
    protected BlockPos irradiateBlockPos;
    @Unique
    private final HashMap<BaseLaserBlockEntity, Boolean> he$dataMap = new HashMap<>();

    @Unique
    @DescSynced
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
        if (!he$dataMap.containsKey(blockEntity) || !this.irradiateSelfLaserBlockSet.contains(blockEntity)) {
            return;
        }
        he$dataMap.put(blockEntity, value);
        flushPureHESourceState(context);
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
        if (he$isPureHELaser == value) return;
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
        if (instance instanceof LaserRendererInternals.Access access && (Object)this instanceof HighEnergyLaserBlockEntity heLaser) {
            access.updateFromSource(heLaser, true, new HashSet<>());
        }
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
        he$dataMap.put(baseLaserBlockEntity, false);
        Set<BaseLaserBlockEntity> set = new HashSet<>();
        set.add((BaseLaserBlockEntity) (Object) this);
        updateFromSource(baseLaserBlockEntity, value, set);
    }

    @Inject(
        method = "onCancelingIrradiation",
        at = @At("RETURN")
    )
    void handleSourceRemoval(BaseLaserBlockEntity baseLaserBlockEntity, CallbackInfo ci) {
        he$dataMap.remove(baseLaserBlockEntity);
        flushPureHESourceState(new HashSet<>());
    }
}
