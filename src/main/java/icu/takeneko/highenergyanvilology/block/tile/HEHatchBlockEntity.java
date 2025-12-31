package icu.takeneko.highenergyanvilology.block.tile;

import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import icu.takeneko.highenergyanvilology.foundation.Tickable;
import icu.takeneko.highenergyanvilology.foundation.block.tile.HESynedBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.block.tile.hatch.HatchLogicHost;
import icu.takeneko.highenergyanvilology.foundation.block.tile.hatch.HatchType;
import icu.takeneko.highenergyanvilology.foundation.block.tile.hatch.logic.HatchLogic;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class HEHatchBlockEntity<C> extends HESynedBlockEntity implements HatchLogicHost, IUIHolder.Block, Tickable {
    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(HEHatchBlockEntity.class);

    @Getter
    private final HatchType<C> hatchType;
    @Getter
    private final HatchLogic<C> logic;

    public HEHatchBlockEntity(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState blockState,
        HatchType<C> hatchType,
        boolean isInput
    ) {
        super(type, pos, blockState);
        this.hatchType = hatchType;
        this.logic = hatchType.createHatchLogic(this, isInput);
    }

    public void tick() {
        logic.tick();
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public void onRemoved() {
        this.logic.onRemoved();
    }

    @Override
    public void markDirty() {
        setChanged();
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        return logic.createUI();
    }
}
