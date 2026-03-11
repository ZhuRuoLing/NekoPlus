package icu.takeneko.nekoplus.block.tile;

import dev.dubhe.anvilcraft.api.power.IPowerProducer;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import icu.takeneko.nekoplus.foundation.grid.OffCenterPowerComponent;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class StellarEngineBlockEntity extends BlockEntity implements IPowerProducer, OffCenterPowerComponent {

    private final AABB shape;
    @Getter
    @Setter
    private PowerGrid grid;

    public StellarEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        AABB inflated = new AABB(pos).inflate(3.5, 0, 3.5);
        this.shape = inflated.setMaxY(inflated.maxY + 8);
    }

    @Override
    public AABB getShape() {
        return shape;
    }

    @Override
    public int getOutputPower() {
        return 1024 * 1000;
    }

    @Override
    public @Nullable Level getCurrentLevel() {
        return getLevel();
    }

    @Override
    public BlockPos getPos() {
        return getBlockPos();
    }
}
