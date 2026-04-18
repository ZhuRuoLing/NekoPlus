package icu.takeneko.nekoplus.content.tile.logic.hatch;

import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import icu.takeneko.nekoplus.block.NPHatchBlock;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.HatchLogicHost;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.logic.HatchLogic;
import icu.takeneko.nekoplus.foundation.inventory.NPItemHandler;
import icu.takeneko.nekoplus.foundation.inventory.NPItemHandlerOwner;
import icu.takeneko.nekoplus.util.ItemTransferHelper;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class ItemHatchLogic implements HatchLogic<IItemHandler>, NPItemHandlerOwner {

    @Getter
    private final NPItemHandler itemHandler = new NPItemHandler(9, this);
    private final HatchLogicHost host;
    private final boolean isInput;

    public ItemHatchLogic(HatchLogicHost host, boolean isInput) {
        this.host = host;
        this.isInput = isInput;
    }

    @Override
    public void tick() {
        Direction facing = host.getBlockState().getValue(NPHatchBlock.FACING);
        Level level = host.getLevel();
        BlockPos pos = host.getBlockPos().relative(facing);
        IItemHandler capability = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, facing.getOpposite());
        if (capability != null) {
            if (isInput) {
                ItemTransferHelper.importToTarget(
                    itemHandler,
                    Integer.MAX_VALUE,
                    __ -> true,
                    level,
                    pos,
                    facing.getOpposite()
                );
                return;
            }
            ItemTransferHelper.exportToTarget(
                itemHandler,
                Integer.MAX_VALUE,
                __ -> true,
                level,
                pos,
                facing.getOpposite()
            );
        }
    }

    @Override
    public void onRemoved() {
        Containers.dropContents(host.getLevel(), host.getBlockPos(), itemHandler.getStacks());
    }

    @Override
    public IItemHandler getCapabilityInstance() {
        return itemHandler;
    }

    @Override
    public ModularUI createUI() {
        return null;
    }

    @Override
    public void setOnContentsChanged(Runnable onContentChanged) {
        itemHandler.setOnContentsChanged(onContentChanged);
    }

    @Override
    public Runnable getOnContentsChanged() {
        return itemHandler.getOnContentsChanged();
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return itemHandler.serializeNBT(provider);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        itemHandler.deserializeNBT(provider, nbt);
    }

    @Override
    public void onContentChanged() {
        host.markDirty();
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }
}
