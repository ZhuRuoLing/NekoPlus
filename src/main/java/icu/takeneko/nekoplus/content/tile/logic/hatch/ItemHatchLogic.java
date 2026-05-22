package icu.takeneko.nekoplus.content.tile.logic.hatch;

import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import dev.dubhe.anvilcraft.api.itemhandler.ItemHandlerUtil;
import icu.takeneko.nekoplus.block.NPHatchBlock;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.HatchLogicHost;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.logic.HatchLogic;
import icu.takeneko.nekoplus.foundation.inventory.NPItemHandler;
import icu.takeneko.nekoplus.foundation.inventory.NPItemHandlerOwner;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class ItemHatchLogic implements HatchLogic<ResourceHandler<ItemResource>>, NPItemHandlerOwner {

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
        ResourceHandler<ItemResource> capability = level.getCapability(Capabilities.Item.BLOCK, pos, facing.getOpposite());
        if (capability != null) {
            if (isInput) {
                ItemHandlerUtil.exportToTarget(
                    capability,
                    Integer.MAX_VALUE,
                    (_,_) -> true,
                    this.itemHandler
                );
                return;
            }
            ItemHandlerUtil.exportToTarget(
                this.itemHandler,
                Integer.MAX_VALUE,
                (_,_) -> true,
                capability
            );
        }
    }

    @Override
    public void onRemoved() {
        Containers.dropContents(host.getLevel(), host.getBlockPos(), itemHandler.getStacks());
    }

    @Override
    public ResourceHandler<ItemResource> getCapabilityInstance() {
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
    public void onContentChanged() {
        host.markDirty();
    }

    @Override
    public boolean isItemValid(int slot, ItemResource stack) {
        return true;
    }

    @Override
    public void serialize(ValueOutput output) {
        itemHandler.serialize(output);
    }

    @Override
    public void deserialize(ValueInput input) {
        itemHandler.deserialize(input);
    }
}
