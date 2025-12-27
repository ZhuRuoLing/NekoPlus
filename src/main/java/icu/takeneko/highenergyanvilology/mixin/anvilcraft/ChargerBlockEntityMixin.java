package icu.takeneko.highenergyanvilology.mixin.anvilcraft;

import dev.dubhe.anvilcraft.block.ChargerBlock;
import dev.dubhe.anvilcraft.block.entity.ChargerBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.block.tile.HEOverclockablePowerConsumer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
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
public abstract class ChargerBlockEntityMixin extends BlockEntity implements HEOverclockablePowerConsumer {

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
        return isCharger && isGridWorking() && timeLeft > 0 && !getBlockState().getValue(ChargerBlock.POWERED);
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
        return isCharger && !this.getBlockState().getValue(ChargerBlock.POWERED) ? -powerValue : 0;
    }

    @Overwrite
    @Override
    public int getInputPower() {
        return HEOverclockablePowerConsumer.super.getInputPower();
    }

    @Override
    public int getOverclockedInputPower() {
        return getBaseInputPower() * he$efficency;
    }
}
