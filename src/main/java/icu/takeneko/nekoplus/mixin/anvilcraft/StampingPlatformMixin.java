package icu.takeneko.nekoplus.mixin.anvilcraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.anvilcraft.lib.v2.piston.IMoveableEntityBlock;
import dev.dubhe.anvilcraft.block.StampingPlatformBlock;
import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.block.tile.StampingPlatformBlockEntity;
import icu.takeneko.nekoplus.internal.StampingPlatformsInternals;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StampingPlatformBlock.class)
public class StampingPlatformMixin extends Block implements EntityBlock, IMoveableEntityBlock {
    public StampingPlatformMixin(Properties properties) {
        super(properties);
    }

    @WrapOperation(
        method = "<init>",
        at = @At(value = "INVOKE", target = "Ldev/dubhe/anvilcraft/block/StampingPlatformBlock;registerDefaultState(Lnet/minecraft/world/level/block/state/BlockState;)V")
    )
    void modifyDefaultState(StampingPlatformBlock instance, BlockState state, Operation<Void> original) {
        original.call(
            instance,
            state.setValue(StampingPlatformsInternals.LASER_TARGETED, false)
        );
    }

    @Inject(
        method = "createBlockStateDefinition",
        at = @At("RETURN")
    )
    void addLaserTargetedState(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(StampingPlatformsInternals.LASER_TARGETED);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StampingPlatformBlockEntity(NPBlockEntities.STAMPING_PLATFORM.get(), pos, state);
    }
}
