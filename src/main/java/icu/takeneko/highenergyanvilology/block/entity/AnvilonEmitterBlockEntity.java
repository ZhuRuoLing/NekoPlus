package icu.takeneko.highenergyanvilology.block.entity;

import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import dev.dubhe.anvilcraft.api.power.IPowerConsumer;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import dev.dubhe.anvilcraft.util.WatchableCyclingValue;
import icu.takeneko.highenergyanvilology.foundation.block.entity.HESynedBlockEntity;
import icu.takeneko.highenergyanvilology.foundation.inventory.HEItemHandler;
import icu.takeneko.highenergyanvilology.foundation.inventory.ItemHandlerOwner;
import icu.takeneko.highenergyanvilology.ui.menu.AnvilonEmitterUI;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnvilonEmitterBlockEntity
    extends HESynedBlockEntity
    implements IPowerConsumer, ItemHandlerOwner, IUIHolder.BlockEntityUI {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(AnvilonEmitterBlockEntity.class);

    private final WatchableCyclingValue<Float> rate = new WatchableCyclingValue<>(
        "working_rate",
        a -> {
        },
        0f, 0.8f, 1f, 2f, 3f, 5f
    );

    @Persisted
    @Getter
    private final HEItemHandler itemHandler = new HEItemHandler(1, this);

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

    private WidgetGroup createUi() {

        return new AnvilonEmitterUI(this);
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(createUi(), this, entityPlayer);
    }
}
