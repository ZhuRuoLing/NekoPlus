package icu.takeneko.nekoplus.mixin.anvilcraft;


import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.dubhe.anvilcraft.block.entity.SmartBlockPlacerBlockEntity;
import dev.dubhe.anvilcraft.block.power.consumer.SmartBlockPlacerBlock;
import dev.dubhe.anvilcraft.util.StructureLoadUtil;
import icu.takeneko.nekoplus.config.NPConfig;
import icu.takeneko.nekoplus.foundation.block.tile.NPOverclockablePowerConsumer;
import icu.takeneko.nekoplus.internal.SmartBlockPlacerBlockEntityInternals;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(SmartBlockPlacerBlockEntity.class)
public abstract class SmartBlockPlacerBlockEntityMixin
    extends BlockEntity
    implements SmartBlockPlacerBlockEntityInternals.Extension, NPOverclockablePowerConsumer {

    @Shadow
    @Nullable
    private StructureLoadUtil.StructureData loadedStructure;
    @Shadow
    @Final
    private static int POWER;
    @Shadow
    private int placeCooldown;
    @Shadow
    private ItemStack currentHeldBlock;
    @Shadow
    private int currentPlacementIndex;

    @Unique
    private boolean np$ocEnabled = false;
    @Unique
    private int np$efficency = 0;

    public SmartBlockPlacerBlockEntityMixin(
        BlockEntityType<?> type,
        BlockPos worldPosition,
        BlockState blockState
    ) {
        super(type, worldPosition, blockState);
    }

    @Inject(
        method = "tickServer",
        at = @At("RETURN")
    )
    void workaroundOC(Level level, BlockPos pos, CallbackInfo ci) {
        if (this.isOverload()) {
            np$efficency = 0;
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
        this.np$ocEnabled = input.getBooleanOr("oc_enabled", false);
    }

    @WrapOperation(
        method = "tickCommonCooldownLogic",
        at = @At(
            value = "FIELD",
            target = "Ldev/dubhe/anvilcraft/block/entity/SmartBlockPlacerBlockEntity;placeCooldown:I",
            opcode = Opcodes.PUTFIELD,
            ordinal = 0
        )
    )
    private void handleMinusCd(
        SmartBlockPlacerBlockEntity instance,
        int value,
        Operation<Void> original,
        @Local(index = 3, argsOnly = true) Runnable executeAction
    ) {
        if (isOverclockEnabled() && np$efficency != 0) {
            int before = this.placeCooldown;
            this.placeCooldown = Mth.clamp(this.placeCooldown - np$efficency, 0, Integer.MAX_VALUE);
            if (before > 6 && this.placeCooldown < 6) {
                if (this.currentHeldBlock.isEmpty()) {
                    this.currentPlacementIndex = 0;
                }
                executeAction.run();
            }
        } else {
            this.placeCooldown--;
        }
    }

    @Definition(id = "shouldExecute", local = @Local(type = boolean.class, name = "shouldExecute", argsOnly = true))
    @Expression("shouldExecute")
    @ModifyExpressionValue(
        method = "tickCommonCooldownLogic",
        at = @At(
            value = "MIXINEXTRAS:EXPRESSION",
            ordinal = 0
        )
    )
    boolean handleExtraCondition(boolean original) {
        return original && (!np$ocEnabled || np$efficency == 0 || np$efficency <= 14);
    }

    @Override
    public void toggleOverclock() {
        this.np$ocEnabled = !this.np$ocEnabled;
    }

    @Override
    public boolean isOverclockable() {
        return np$ocEnabled;
    }

    @Override
    public void setEfficiency(int value) {
        this.np$efficency = value;
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
        return np$efficency;
    }

    @Override
    public boolean isOverclockEnabled() {
        return np$ocEnabled;
    }

    @Override
    public int getBaseInputPower() {
        return (this.loadedStructure != null && !this.loadedStructure.isEmpty()) ? 64 : POWER;
    }

    @Override
    public int getOverclockedInputPower() {
        return getBaseInputPower() + np$efficency * getBaseOverclockCost();
    }

    @Inject(
        method = "getInputPower",
        at = @At("RETURN"),
        cancellable = true
    )
    void handleOverclockedPower(CallbackInfoReturnable<Integer> cir) {
        if (isOverclockEnabled()) {
            cir.setReturnValue(getOverclockedInputPower());
        }
    }

    @Override
    public void setOverload(boolean value) {
        level.setBlockAndUpdate(getPos(), getBlockState().setValue(SmartBlockPlacerBlock.OVERLOAD, value));
    }

    @Override
    public boolean isOverload() {
        return level.getBlockState(getPos()).getValue(SmartBlockPlacerBlock.OVERLOAD);
    }
}
