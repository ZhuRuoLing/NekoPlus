package icu.takeneko.highenergyanvilology.block.entity;

import dev.dubhe.anvilcraft.api.power.IPowerConsumer;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import dev.dubhe.anvilcraft.util.WatchableCyclingValue;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnvilonEmitterBlockEntity extends BlockEntity implements IPowerConsumer {
    private final WatchableCyclingValue<Float> rate = new WatchableCyclingValue<>(
        "working_rate",
        a -> {
        },
        0f, 0.8f, 1f, 2f, 3f, 5f
    );

    @Getter
    @Setter
    private PowerGrid grid;

    public AnvilonEmitterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void onActivated() {
        rate.next();
    }

    @Override
    public @Nullable Level getCurrentLevel() {
        return level;
    }

    @Override
    public @NotNull BlockPos getPos() {
        return getBlockPos();
    }
}
