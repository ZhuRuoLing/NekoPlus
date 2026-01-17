package icu.takeneko.highenergyanvilology.block.tile;

import com.lowdragmc.lowdraglib2.gui.holder.IModularUIHolder;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.syncdata.field.ManagedFieldHolder;
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
import org.jetbrains.annotations.Nullable;

public class HEHatchBlockEntity<C> extends HESynedBlockEntity implements HatchLogicHost, IModularUIHolder, Tickable {
    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(HEHatchBlockEntity.class);

    @Getter
    private final HatchType<C> hatchType;

    @Getter
    @Persisted
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
    public @Nullable ModularUI getModularUI() {
        return logic.createUI();
    }
}
