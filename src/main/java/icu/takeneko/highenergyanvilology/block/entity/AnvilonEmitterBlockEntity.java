package icu.takeneko.highenergyanvilology.block.entity;

import dev.dubhe.anvilcraft.util.WatchableCyclingValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AnvilonEmitterBlockEntity extends BlockEntity {
    private final WatchableCyclingValue<Float> rate = new WatchableCyclingValue<>(
        "working_rate",
        a -> {
        },
        0f, 0.8f, 1f, 2f, 3f, 5f
    );

    public AnvilonEmitterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void onActivated() {
        rate.next();
    }
}
