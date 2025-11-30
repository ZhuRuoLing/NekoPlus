package icu.takeneko.highenergyanvilology.block.entity;

import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib2.syncdata.storage.FieldManagedStorage;
import com.lowdragmc.lowdraglib2.syncdata.storage.IManagedStorage;
import dev.dubhe.anvilcraft.api.power.IPowerConsumer;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import dev.dubhe.anvilcraft.util.WatchableCyclingValue;
import icu.takeneko.highenergyanvilology.all.HEMenuTypes;
import icu.takeneko.highenergyanvilology.client.ui.AnvilonEmitterUI;
import icu.takeneko.highenergyanvilology.foundation.inventory.HEItemHandler;
import icu.takeneko.highenergyanvilology.foundation.inventory.ItemHandlerOwner;
import icu.takeneko.highenergyanvilology.foundation.ui.BlockUIHolder;
import icu.takeneko.highenergyanvilology.ui.menu.AnvilonEmitterMenu;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnvilonEmitterBlockEntity
    extends HESynedBlockEntity
    implements IPowerConsumer, ItemHandlerOwner, BlockUIHolder {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(AnvilonEmitterBlockEntity.class);

    private final WatchableCyclingValue<Float> rate = new WatchableCyclingValue<>(
        "working_rate",
        a -> {
        },
        0f, 0.8f, 1f, 2f, 3f, 5f
    );

    @Persisted
    @Getter
    private final ItemStackHandler itemHandler = new HEItemHandler(1, this);

    @Getter
    @Setter
    private PowerGrid grid;

    public AnvilonEmitterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void onActivated() {
        rate.next();
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
    public ModularUI createUI(Player player) {
        return new ModularUI(UI.of(new AnvilonEmitterUI()), player);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("menu.highenergyanvilology.anvilon_emitter");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new AnvilonEmitterMenu(HEMenuTypes.ANVILON_EMITTER.get(), containerId, playerInventory, this);
    }
}
