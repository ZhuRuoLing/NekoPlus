package icu.takeneko.nekoplus.mixin.anvilcraft;

import dev.dubhe.anvilcraft.block.ChargerBlock;
import dev.dubhe.anvilcraft.block.entity.ChargerBlockEntity;
import icu.takeneko.nekoplus.foundation.block.tile.NPOverclockablePowerConsumer;
import icu.takeneko.nekoplus.internal.ChargerBlockEntityInternals;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings({"AddedMixinMembersNamePattern", "OverwriteAuthorRequired"})
@Mixin(ChargerBlockEntity.class)
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class ChargerBlockEntityMixin extends BlockEntity implements NPOverclockablePowerConsumer, ChargerBlockEntityInternals.Extension {

    @Shadow
    private boolean isCharger;

    @Shadow
    private int timeLeft;

    @Shadow
    public abstract BlockPos getPos();

    @Shadow
    private int powerValue;

    @Unique
    private int he$efficency = 1;

    @Unique
    private boolean np$ocEnabled = false;

    public ChargerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }


    @ModifyConstant(
        method = "tick",
        constant = @Constant(intValue = 1, log = true)
    )
    private int handleMinus(int constant) {
        return he$efficency;
    }

    @Inject(
        method = "tick",
        at = @At(value = "FIELD", target = "Ldev/dubhe/anvilcraft/block/entity/ChargerBlockEntity;timeLeft:I", opcode = Opcodes.GETFIELD, ordinal = 3)
    )
    private void handleCorrectlyMinus(Level level1, BlockPos blockPos, CallbackInfo ci){
        this.timeLeft = Math.clamp(timeLeft, 0, Integer.MAX_VALUE);
    }

    @Inject(
        method = "saveAdditional",
        at = @At("HEAD")
    )
    void saveNP(CompoundTag tag, HolderLookup.Provider provider, CallbackInfo ci){
        tag.putBoolean("OCEnabled", np$ocEnabled);
    }

    @Inject(
        method = "loadAdditional",
        at = @At("HEAD")
    )
    void loadNP(CompoundTag tag, HolderLookup.Provider provider, CallbackInfo ci){
        this.np$ocEnabled = tag.getBoolean("OCEnabled");
    }

    @Override
    public void setEfficiency(int value) {
        he$efficency = value;
    }

    @Override
    public int getBaseOverclockCost() {
        return getBaseInputPower();
    }

    @Override
    public int maxOverclockRatio() {
        return 100;
    }

    @Override
    public boolean isOverclockable() {
        return isCharger && np$ocEnabled && isGridWorking() && timeLeft > 0 && !getBlockState().getValue(ChargerBlock.POWERED);
    }

    @Override
    public void setOverload(boolean value) {
        getCurrentLevel().setBlockAndUpdate(getPos(), getBlockState().setValue(ChargerBlockEntity.OVERLOAD, value));
    }

    @Override
    public boolean isOverload() {
        return level.getBlockState(getPos()).getValue(ChargerBlockEntity.OVERLOAD);
    }

    @Override
    public int getBaseInputPower() {
        return (isCharger && !this.getBlockState().getValue(ChargerBlock.POWERED)) ? -powerValue : 0;
    }

    @Overwrite
    @Override
    public int getInputPower() {
        return NPOverclockablePowerConsumer.super.getInputPower();
    }

    @Override
    public int getOverclockedInputPower() {
        return getBaseInputPower() * he$efficency;
    }

    @Override
    public void toggleOverclock() {
        this.np$ocEnabled = !this.np$ocEnabled;
    }

    @Override
    public boolean isOverclockEnabled() {
        return np$ocEnabled;
    }

    @Override
    public int currentOverclockRatio() {
        return he$efficency;
    }
}
