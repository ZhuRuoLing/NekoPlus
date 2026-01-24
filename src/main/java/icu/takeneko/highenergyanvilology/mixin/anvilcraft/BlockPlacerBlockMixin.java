package icu.takeneko.highenergyanvilology.mixin.anvilcraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.anvilcraft.block.BlockPlacerBlock;
import dev.dubhe.anvilcraft.block.state.Orientation;
import icu.takeneko.highenergyanvilology.internal.BlockPlacerBlockInternals;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockPlacerBlock.class)
public class BlockPlacerBlockMixin {

    @WrapOperation(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/StateDefinition;any()Lnet/minecraft/world/level/block/state/StateHolder;"
        )
    )
    <O, S extends StateHolder<O, S>> S handleCustomStates(StateDefinition<O, S> instance, Operation<S> original) {
        BlockState call = (BlockState) original.call(instance);
        return (S) call.setValue(BlockPlacerBlockInternals.MODE, BlockPlacerBlockInternals.Mode.PLACER);
    }

    @Inject(
        method = "createBlockStateDefinition",
        at = @At("RETURN")
    )
    void handleCustomStatesDef(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(BlockPlacerBlockInternals.MODE);
    }

    @Inject(
        method = "placeBlock",
        at = @At("HEAD"),
        cancellable = true
    )
    void handleClicker(int distance, Level level, BlockPos blockPos, Orientation orientation, CallbackInfo ci) {
        if (level.getBlockState(blockPos).getValue(BlockPlacerBlockInternals.MODE) == BlockPlacerBlockInternals.Mode.CLICKER) {
            BlockPlacerBlockInternals.onActivate(distance, level, blockPos, orientation);
            ci.cancel();
        }
    }
}
