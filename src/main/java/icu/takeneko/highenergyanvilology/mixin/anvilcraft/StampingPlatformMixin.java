package icu.takeneko.highenergyanvilology.mixin.anvilcraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.anvilcraft.lib.piston.IMoveableEntityBlock;
import dev.dubhe.anvilcraft.block.StampingPlatformBlock;
import icu.takeneko.highenergyanvilology.all.HEBlockEntities;
import icu.takeneko.highenergyanvilology.block.tile.AnvilonEmitterBlockEntity;
import icu.takeneko.highenergyanvilology.block.tile.StampingPlatformBlockEntity;
import icu.takeneko.highenergyanvilology.internal.StampingPlatformsInternals;
import icu.takeneko.highenergyanvilology.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level instanceof ServerLevel) {
            return (BlockEntityTicker<T>) BlockEntityUtil.<AnvilonEmitterBlockEntity>createTicker();
        }
        return null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StampingPlatformBlockEntity(HEBlockEntities.STAMPING_PLATFORM.get(), pos, state);
    }
}
