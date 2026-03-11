package icu.takeneko.nekoplus.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

import static net.neoforged.neoforge.items.ItemHandlerHelper.insertItem;

public class ItemTransferHelper {
    public static void exportToTarget(IItemHandler source, int maxAmount, Predicate<ItemStack> predicate, Level level, BlockPos pos, @Nullable Direction direction) {
        if (level.getBlockState(pos).hasBlockEntity()) {
            var blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {
                IItemHandler target = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, blockEntity.getBlockState(), blockEntity, direction);
                if (target != null) {
                    for (int srcIndex = 0; srcIndex < source.getSlots(); srcIndex++) {
                        ItemStack sourceStack = source.extractItem(srcIndex, Integer.MAX_VALUE, true);
                        if (sourceStack.isEmpty() || !predicate.test(sourceStack)) {
                            continue;
                        }
                        ItemStack remainder = insertItem(target, sourceStack, true);
                        int amountToInsert = sourceStack.getCount() - remainder.getCount();
                        if (amountToInsert > 0) {
                            sourceStack = source.extractItem(srcIndex, Math.min(maxAmount, amountToInsert), false);
                            insertItem(target, sourceStack, false);
                            maxAmount -= Math.min(maxAmount, amountToInsert);
                            if (maxAmount <= 0) return;
                        }
                    }
                }
            }
        }
    }

    public static void importToTarget(IItemHandler target, int maxAmount, Predicate<ItemStack> predicate, Level level, BlockPos pos, @Nullable Direction direction) {
        if (level.getBlockState(pos).hasBlockEntity()) {
            var blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {
                IItemHandler source = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, blockEntity.getBlockState(), blockEntity, direction);
                if (source != null) {
                    for (int srcIndex = 0; srcIndex < source.getSlots(); srcIndex++) {
                        ItemStack sourceStack = source.extractItem(srcIndex, Integer.MAX_VALUE, true);
                        if (sourceStack.isEmpty() || !predicate.test(sourceStack)) {
                            continue;
                        }
                        ItemStack remainder = insertItem(target, sourceStack, true);
                        int amountToInsert = sourceStack.getCount() - remainder.getCount();
                        if (amountToInsert > 0) {
                            sourceStack = source.extractItem(srcIndex, Math.min(maxAmount, amountToInsert), false);
                            insertItem(target, sourceStack, false);
                            maxAmount -= Math.min(maxAmount, amountToInsert);
                        }
                        if (maxAmount <= 0) return;
                    }
                }
            }
        }
    }
}
