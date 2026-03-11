package icu.takeneko.nekoplus.block.tile;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.syncdata.field.ManagedFieldHolder;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import icu.takeneko.nekoplus.foundation.block.tile.NPPowerConsumer;
import icu.takeneko.nekoplus.foundation.block.tile.NPSynedBlockEntity;
import icu.takeneko.nekoplus.foundation.Tickable;
import icu.takeneko.nekoplus.foundation.block.tile.NPUIBlock;
import icu.takeneko.nekoplus.foundation.inventory.NPItemHandler;
import icu.takeneko.nekoplus.foundation.inventory.NPItemHandlerOwner;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import icu.takeneko.nekoplus.ui.AnvilonEmitterUI;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnvilonEmitterBlockEntity
    extends NPSynedBlockEntity
    implements NPPowerConsumer, NPItemHandlerOwner, NPUIBlock.Provider, Tickable {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(AnvilonEmitterBlockEntity.class);

    @Persisted
    @Getter
    private final NPItemHandler itemHandler = new NPItemHandler(1, this);

    @Getter
    @Setter
    private PowerGrid grid;

    @DescSynced
    @Getter
    @Setter
    private boolean isOverload = false;

    public AnvilonEmitterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public int getInputPower() {
        return itemHandler.getStacks().get(0).isEmpty() ? 0 : 16;
    }

    @Override
    public void tick() {
        flushState();
    }

    @Override
    public @Nullable Level getCurrentLevel() {
        return level;
    }

    @Override
    public @NotNull BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    public void onContentChanged() {
        setChanged();
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void notifyPersistence() {

    }

    @Override
    public ModularUI getModularUI(BlockUIMenuType.BlockUIHolder holder) {
        return NPUI.of(new AnvilonEmitterUI(this), holder);
    }
}
