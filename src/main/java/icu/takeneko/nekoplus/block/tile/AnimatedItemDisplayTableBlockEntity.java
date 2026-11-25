package icu.takeneko.nekoplus.block.tile;

import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.syncdata.field.ManagedFieldHolder;
import icu.takeneko.nekoplus.foundation.Tickable;
import icu.takeneko.nekoplus.foundation.block.tile.NPSynedBlockEntity;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AnimatedItemDisplayTableBlockEntity extends NPSynedBlockEntity implements Tickable {
    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(AnimatedItemDisplayTableBlockEntity.class);

    @Persisted
    @DescSynced
    private ItemStack displayItem = ItemStack.EMPTY;

    @Persisted
    @DescSynced
    @Getter
    private int redstoneSignal;

    @Getter
    private float previousScale = 1;

    @Getter
    private float scale = 1;

    @Getter
    private float previousStretch = 1;

    @Getter
    private float stretch = 1;

    @Getter
    private float previousRotation;

    @Getter
    private float rotation;

    private float animationStrength;
    private int animationTicks;

    @Override
    public void tick() {
        this.previousScale = this.scale;
        this.previousStretch = this.stretch;
        float targetScale = this.redstoneSignal == 0 ? 1 : this.redstoneSignal * 2;
        this.scale += (targetScale - this.scale) * 0.25f;
        if (Math.abs(targetScale - this.scale) < 0.001f) {
            this.scale = targetScale;
        }
        float targetStrength = this.redstoneSignal == 0 ? 0 : 1;
        this.animationStrength += (targetStrength - this.animationStrength) * 0.25f;
        if (Math.abs(targetStrength - this.animationStrength) < 0.001f) {
            this.animationStrength = targetStrength;
        }
        this.rotation %= 360;
        this.previousRotation = this.rotation;
        this.rotation += 2 + (360f / 54 - 2) * this.animationStrength;
        this.animationTicks = (this.animationTicks + 1) % 54;
        double phase = this.animationTicks * Math.PI * 2 / 9;
        float wave = (float) Math.cos(phase);
        this.stretch = 1 + this.animationStrength * 0.22f * wave;
    }

    public void setRedstoneSignal(int signal) {
        if (this.redstoneSignal == signal) return;
        this.redstoneSignal = signal;
        this.contentsChanged();
    }

    public AnimatedItemDisplayTableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public boolean useAsyncThread() {
        return false;
    }

    public ItemStack getDisplayItem() {
        return this.displayItem.copyWithCount(1);
    }

    public boolean hasDisplayItem() {
        return !this.displayItem.isEmpty();
    }

    public boolean placeItem(ItemStack stack) {
        if (this.hasDisplayItem() || stack.isEmpty()) return false;
        this.displayItem = stack.copyWithCount(1);
        this.contentsChanged();
        return true;
    }

    public ItemStack takeItem() {
        ItemStack result = this.getDisplayItem();
        this.displayItem = ItemStack.EMPTY;
        this.contentsChanged();
        return result;
    }

    private void contentsChanged() {
        this.setChanged();
        if (this.level != null && !this.level.isClientSide()) {
            this.sync(false);
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (this.level != null && !this.level.isClientSide() && this.hasDisplayItem()) {
            Block.popResource(this.level, pos, this.takeItem());
        }
    }
}
