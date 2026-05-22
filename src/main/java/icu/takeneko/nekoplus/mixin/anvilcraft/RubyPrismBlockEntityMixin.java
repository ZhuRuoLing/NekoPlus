package icu.takeneko.nekoplus.mixin.anvilcraft;

import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import dev.dubhe.anvilcraft.block.entity.RubyPrismBlockEntity;
import icu.takeneko.nekoplus.block.tile.HighEnergyLaserBlockEntity;
import icu.takeneko.nekoplus.internal.LaserRendererInternals;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RubyPrismBlockEntity.class)
public abstract class RubyPrismBlockEntityMixin extends BaseLaserBlockEntity {

    public RubyPrismBlockEntityMixin(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState blockState
    ) {
        super(type, pos, blockState);
    }

    @Override
    public int getLaserColor() {
        if (LaserRendererInternals.hasPureHELaserSource(this)) {
            return HighEnergyLaserBlockEntity.HIGH_ENERGY_LASER_COLOR;
        }
        return super.getLaserColor();
    }
}
