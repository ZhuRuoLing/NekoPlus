package icu.takeneko.highenergyanvilology.block.tile;

import dev.dubhe.anvilcraft.api.power.PowerGrid;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import icu.takeneko.highenergyanvilology.block.HighEnergyLaserBlock;
import icu.takeneko.highenergyanvilology.foundation.Tickable;
import icu.takeneko.highenergyanvilology.foundation.block.entity.HEPowerConsumer;
import icu.takeneko.highenergyanvilology.internal.LaserRendererInternals;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class HighEnergyLaserBlockEntity extends BaseLaserBlockEntity implements HEPowerConsumer, Tickable {
    @Nullable
    @Getter
    @Setter
    private PowerGrid grid;

    public HighEnergyLaserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void tick() {
        tick(level);
    }

    @Override
    public void tick(Level level) {
        this.resetState();
        flushState();
        if (level.hasNeighborSignal(getBlockPos()) == !getPoweredState()) {
            setPoweredState(!level.hasNeighborSignal(getBlockPos()), 2);
        }
        ((LaserRendererInternals.Access) this).setPureHELaserSourceDirect(true);
        if (isSwitchedOn()) {
            emitLaser(getFacing());
        } else {
            if (irradiateBlockPos != null && level.getBlockEntity(irradiateBlockPos) instanceof BaseLaserBlockEntity irradiateBlockEntity) {
                irradiateBlockEntity.onCancelingIrradiation(this);
            }
            updateIrradiateBlockPos(null);
        }
        super.tick(level);
    }


    @Override
    public @Nullable Level getCurrentLevel() {
        return level;
    }

    @Override
    public BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    protected int getBaseLaserLevel() {
        return isSwitchedOn() ? 64 : 0;
    }

    @Override
    public Direction getFacing() {
        return getBlockState().getValue(HighEnergyLaserBlock.FACING);
    }

    @Override
    public int getInputPower() {
        return 1024;
    }

    @Override
    public void setOverload(boolean value) {
        level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(HighEnergyLaserBlock.OVERLOAD, value));
    }

    @Override
    public boolean isOverload() {
        return getBlockState().getValue(HighEnergyLaserBlock.OVERLOAD);
    }

    public void setPoweredState(boolean value, int flags) {
        level.setBlock(
            getPos(),
            getBlockState().setValue(
                HighEnergyLaserBlock.POWERED,
                value
            ),
            flags
        );
    }

    public void onIrradiated(BaseLaserBlockEntity baseLaserBlockEntity) {
    }

    public boolean getPoweredState() {
        return getBlockState().getValue(HighEnergyLaserBlock.POWERED);
    }

    public boolean isSwitchedOn() {
        return !getPoweredState() && !isOverload();
    }


}
