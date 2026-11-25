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
        previousScale = scale;
        previousStretch = stretch;
        float targetScale = redstoneSignal == 0 ? 1 : redstoneSignal * 2;
        scale += (targetScale - scale) * 0.25f;
        if (Math.abs(targetScale - scale) < 0.001f) {
            scale = targetScale;
        }
        float targetStrength = redstoneSignal == 0 ? 0 : 1;
        animationStrength += (targetStrength - animationStrength) * 0.25f;
        if (Math.abs(targetStrength - animationStrength) < 0.001f) {
            animationStrength = targetStrength;
        }
        rotation %= 360;
        previousRotation = rotation;
        rotation += 2 + (360f / 54 - 2) * animationStrength;
        animationTicks = (animationTicks + 1) % 54;
        double phase = animationTicks * Math.PI * 2 / 9;
        float wave = (float) Math.cos(phase);
        stretch = 1 + animationStrength * 0.22f * wave;
    }

    public void setRedstoneSignal(int signal) {
        if (redstoneSignal == signal) return;
        redstoneSignal = signal;
        contentsChanged();
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
        return displayItem.copyWithCount(1);
    }

    public boolean hasDisplayItem() {
        return !displayItem.isEmpty();
    }

    public boolean placeItem(ItemStack stack) {
        if (hasDisplayItem() || stack.isEmpty()) return false;
        displayItem = stack.copyWithCount(1);
        contentsChanged();
        return true;
    }

    public ItemStack takeItem() {
        ItemStack result = getDisplayItem();
        displayItem = ItemStack.EMPTY;
        contentsChanged();
        return result;
    }

    private void contentsChanged() {
        setChanged();
        if (level != null && !level.isClientSide()) {
            sync(false);
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (level != null && !level.isClientSide() && hasDisplayItem()) {
            Block.popResource(level, pos, takeItem());
        }
    }
}
