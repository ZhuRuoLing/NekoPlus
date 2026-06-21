package icu.takeneko.nekoplus.mixin.anvilcraft;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.syncdata.IManaged;
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib2.syncdata.holder.blockentity.ISyncPersistRPCBlockEntity;
import com.lowdragmc.lowdraglib2.syncdata.storage.FieldManagedStorage;
import com.lowdragmc.lowdraglib2.syncdata.storage.IManagedStorage;
import dev.dubhe.anvilcraft.api.itemhandler.FilteredItemStackHandler;
import dev.dubhe.anvilcraft.block.entity.ItemCollectorBlockEntity;
import icu.takeneko.nekoplus.block.tile.ParticleStabilizerBlockEntity;
import icu.takeneko.nekoplus.foundation.block.tile.NPUIBlock;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import icu.takeneko.nekoplus.internal.ItemCollectorBlockEntityInternals;
import icu.takeneko.nekoplus.ui.ItemCollectorUI;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemCollectorBlockEntity.class)
public abstract class ItemCollectorBlockEntityMixin implements NPUIBlock.Provider, ISyncPersistRPCBlockEntity, ItemCollectorBlockEntityInternals.Access {

    @Shadow
    public abstract FilteredItemStackHandler getFilteredItemStackHandler();

    @SuppressWarnings("RedundantCast")
    @Unique
    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder((Class<? extends IManaged>)(Object) ItemCollectorBlockEntity.class);

    @Unique
    private final FieldManagedStorage np$syncStorage = new FieldManagedStorage(this);
    
    @Unique
    @DescSynced
    private boolean np$isFilterEnabled = false;

    @Override
    public ModularUI getModularUI(BlockUIMenuType.BlockUIHolder holder) {
        return NPUI.ofTransparent(new ItemCollectorUI((ItemCollectorBlockEntity) (Object) this), holder);
    }

    @Inject(
        method = "tick",
        at = @At("HEAD")
    )
    void updateState(Level level, BlockPos blockPos, CallbackInfo ci) {
        boolean filterEnabled = this.getFilteredItemStackHandler().isFilterEnabled();
        if (filterEnabled != np$isFilterEnabled){
            np$isFilterEnabled = filterEnabled;
        }
    }

    @Override
    public boolean nekoplus$isFilteringEnabled() {
        return np$isFilterEnabled;
    }

    @Override
    public IManagedStorage getSyncStorage() {
        return np$syncStorage;
    }

    @Override
    public IManagedStorage getRootStorage() {
        return np$syncStorage;
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
