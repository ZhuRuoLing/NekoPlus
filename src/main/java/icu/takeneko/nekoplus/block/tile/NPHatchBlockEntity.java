package icu.takeneko.nekoplus.block.tile;

import com.lowdragmc.lowdraglib2.gui.holder.IModularUIHolder;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.syncdata.field.ManagedFieldHolder;
import icu.takeneko.nekoplus.foundation.Tickable;
import icu.takeneko.nekoplus.foundation.block.tile.NPSynedBlockEntity;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.HatchLogicHost;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.HatchType;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.logic.HatchLogic;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class NPHatchBlockEntity<C> extends NPSynedBlockEntity implements HatchLogicHost, IModularUIHolder, Tickable {
    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(NPHatchBlockEntity.class);

    @Getter
    private final HatchType<C> hatchType;

    @Getter
    @Persisted
    private final HatchLogic<C> logic;

    private ItemStack resultStack;

    public NPHatchBlockEntity(
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

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
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
