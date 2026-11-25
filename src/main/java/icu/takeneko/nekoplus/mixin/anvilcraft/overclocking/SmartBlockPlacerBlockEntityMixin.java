package icu.takeneko.nekoplus.mixin.anvilcraft.overclocking;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.anvilcraft.block.entity.SmartBlockPlacerBlockEntity;
import dev.dubhe.anvilcraft.block.power.consumer.SmartBlockPlacerBlock;
import icu.takeneko.nekoplus.config.NPConfig;
import icu.takeneko.nekoplus.foundation.block.tile.NPOverclockablePowerConsumer;
import icu.takeneko.nekoplus.internal.SmartBlockPlacerBlockEntityInternals;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmartBlockPlacerBlockEntity.class)
public abstract class SmartBlockPlacerBlockEntityMixin
    extends BlockEntity
    implements SmartBlockPlacerBlockEntityInternals.Extension, NPOverclockablePowerConsumer {

    @Shadow
    public abstract SmartBlockPlacerBlockEntity.TargetMode getTarget();

    @Shadow
    public abstract boolean isOverloaded();

    @Shadow
    @Nullable
    public abstract Level getCurrentLevel();

    @Shadow
    public abstract BlockPos getPos();

    @Unique
    private boolean np$ocEnabled;

    @Unique
    private int np$efficiency;

    public SmartBlockPlacerBlockEntityMixin(
        BlockEntityType<?> type,
        BlockPos worldPosition,
        BlockState blockState
    ) {
        super(type, worldPosition, blockState);
    }

    @Inject(method = "tickServer", at = @At("RETURN"))
    void resetEfficiencyWhenOverloaded(Level level, BlockPos pos, CallbackInfo ci) {
        if (isOverload()) {
            np$efficiency = 0;
        }
    }

    @Inject(
        method = "saveAdditional(Lnet/minecraft/world/level/storage/ValueOutput;)V",
        at = @At("HEAD")
    )
    void saveNP(ValueOutput output, CallbackInfo ci) {
        output.putBoolean("oc_enabled", np$ocEnabled);
    }

    @Inject(
        method = "loadAdditional(Lnet/minecraft/world/level/storage/ValueInput;)V",
        at = @At("HEAD")
    )
    void loadNP(ValueInput input, CallbackInfo ci) {
        np$ocEnabled = input.getBooleanOr("oc_enabled", false);
    }

    @WrapOperation(
        method = "advancePhaseProgress",
        at = @At(
            value = "FIELD",
            target = "Ldev/dubhe/anvilcraft/block/entity/SmartBlockPlacerBlockEntity;phaseElapsedTicks:I",
            opcode = Opcodes.PUTFIELD
        )
    )
    private void speedUpPhaseProgress(SmartBlockPlacerBlockEntity instance, int value, Operation<Void> original) {
        int increment = isOverclockEnabled() ? Math.max(1, currentOverclockRatio()) : 1;
        original.call(instance, value + increment - 1);
    }

    @Override
    public void toggleOverclock() {
        np$ocEnabled = !np$ocEnabled;
    }

    @Override
    public boolean isOverclockable() {
        return np$ocEnabled;
    }

    @Override
    public void setEfficiency(int value) {
        np$efficiency = value;
    }

    @Override
    public int getBaseOverclockCost() {
        return NPConfig.SMART_BLOCK_PLACER_BASE_OVERCLOCK_COST.getAsInt();
    }

    @Override
    public int maxOverclockRatio() {
        return NPConfig.SMART_BLOCK_PLACER_MAX_OVERCLOCK_RATIO.getAsInt();
    }

    @Override
    public int currentOverclockRatio() {
        return np$efficiency;
    }

    @Override
    public boolean isOverclockEnabled() {
        return np$ocEnabled;
    }

    @Override
    public int getBaseInputPower() {
        return getTarget().getPower();
    }

    @Override
    public int getOverclockedInputPower() {
        return getBaseInputPower() + np$efficiency * getBaseOverclockCost();
    }

    @Inject(method = "getInputPower", at = @At("RETURN"), cancellable = true)
    void handleOverclockedPower(CallbackInfoReturnable<Integer> cir) {
        if (isOverclockEnabled()) {
            cir.setReturnValue(getOverclockedInputPower());
        }
    }

    @Override
    public void setOverload(boolean value) {
        Level currentLevel = getCurrentLevel();
        if (currentLevel != null) {
            currentLevel.setBlockAndUpdate(getPos(), getBlockState().setValue(SmartBlockPlacerBlock.OVERLOAD, value));
        }
    }

    @Override
    public boolean isOverload() {
        return isOverloaded();
    }
}
