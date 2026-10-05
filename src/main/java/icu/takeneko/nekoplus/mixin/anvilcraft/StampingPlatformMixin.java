package icu.takeneko.nekoplus.mixin.anvilcraft;

import dev.dubhe.anvilcraft.block.ProcessingTableBlock;
import dev.dubhe.anvilcraft.block.entity.StampingPlatformBlockEntity;
import dev.dubhe.anvilcraft.block.workstation.StampingPlatformBlock;
import icu.takeneko.nekoplus.internal.StampingPlatformsInternals;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StampingPlatformBlock.class)
public abstract class StampingPlatformMixin extends ProcessingTableBlock {
    protected StampingPlatformMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void nekoplus$defaultLaserState(Properties properties, CallbackInfo ci) {
        if ((Object) this instanceof StampingPlatformBlock) {
            registerDefaultState(defaultBlockState().setValue(StampingPlatformsInternals.LASER_TARGETED, false));
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (!(level instanceof ServerLevel)) return null;
        return (_, _, _, entity) -> {
            if (entity instanceof StampingPlatformBlockEntity && entity instanceof StampingPlatformsInternals.LaserTarget tracker) {
                tracker.nekoplus$tick();
            }
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(StampingPlatformsInternals.LASER_TARGETED);
    }
}
