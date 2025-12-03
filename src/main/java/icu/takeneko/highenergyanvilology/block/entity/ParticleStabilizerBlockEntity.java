package icu.takeneko.highenergyanvilology.block.entity;

import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import icu.takeneko.highenergyanvilology.foundation.block.entity.HESynedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ParticleStabilizerBlockEntity extends HESynedBlockEntity {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ParticleStabilizerBlockEntity.class);

    public ParticleStabilizerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
