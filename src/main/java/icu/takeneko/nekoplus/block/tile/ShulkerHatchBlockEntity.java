package icu.takeneko.nekoplus.block.tile;

import icu.takeneko.nekoplus.block.ShulkerHatchBlock;
import icu.takeneko.nekoplus.foundation.Tickable;
import icu.takeneko.nekoplus.foundation.block.tile.NPSynedBlockEntity;
import icu.takeneko.nekoplus.util.ItemResourceHandlerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShulkerHatchBlockEntity extends NPSynedBlockEntity implements Tickable {

    private static final Map<UUID, Long> interactions = new HashMap<>();
    private int cooldown = 0;

    public ShulkerHatchBlockEntity(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState blockState
    ) {
        super(type, pos, blockState);
    }

    public void eject(Player player) {
        if (this.cooldown != 0) return;
        this.cooldown = 4;
        ResourceHandler<ItemResource> handler = getHandler();
        int amount = handler.getAmountAsInt(0);
        if (amount <= 0) return;
        ItemResource presentResource = handler.getResource(0);
        int count = player.isShiftKeyDown() ? presentResource.getMaxStackSize() : 1;
        count = Math.min(count, amount);
        int extracted;
        try (Transaction transaction = Transaction.openRoot()) {
            try (Transaction test = Transaction.open(transaction)) {
                extracted = handler.extract(0, presentResource, count, test);
            }
            if (extracted > 0) {
                handler.extract(0, presentResource, extracted, transaction);
                transaction.commit();
            }
        }
        ItemStack stack = presentResource.toStack(extracted);
        ItemResourceHandlerUtil.giveItemToPlayer(player, stack);
    }

    private ResourceHandler<ItemResource> getHandler() {
        Direction facing = getBlockState().getValue(ShulkerHatchBlock.FACING);
        return level.getCapability(
            Capabilities.Item.BLOCK,
            getBlockPos().relative(facing),
            facing.getOpposite()
        );
    }

    public void insert(Player player) {
        ItemStack itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND).copy();
        ResourceHandler<ItemResource> handler = getHandler();
        ItemResource presentResource = handler.getResource(0);
        ItemResource handResource = ItemResource.of(itemInHand);
        if (presentResource.isEmpty() || presentResource.equals(handResource)) {
            if (!itemInHand.isEmpty()) {
                try (Transaction transaction = Transaction.openRoot()) {
                    int inserted;
                    try (Transaction test = Transaction.open(transaction)) {
                        inserted = handler.insert(0, handResource, itemInHand.count(), test);
                    }
                    if (inserted > 0) {
                        handler.insert(0, handResource, inserted, transaction);
                        itemInHand.shrink(inserted);
                        player.setItemInHand(
                            InteractionHand.MAIN_HAND,
                            itemInHand.isEmpty() ? ItemStack.EMPTY : itemInHand
                        );
                        transaction.commit();
                    }
                }
            }
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - interactions.getOrDefault(player.getGameProfile().id(), currentTimeMillis) < 300) {
                for (ItemStack itemStack : player.getInventory().getNonEquipmentItems()) {
                    if (itemStack.isEmpty()) continue;
                    ItemResource resource = ItemResource.of(itemStack);
                    if (!resource.equals(presentResource)) continue;
                    try (Transaction transaction = Transaction.openRoot()) {
                        int inserted;
                        try (Transaction test = Transaction.open(transaction)) {
                            inserted = handler.insert(0, resource, itemStack.count(), test);
                        }
                        if (inserted > 0) {
                            handler.insert(0, resource, inserted, transaction);
                            itemInHand.shrink(inserted);
                            itemStack.setCount(itemStack.count() - inserted);
                            transaction.commit();
                        }
                    }
                }
            }
        }
        interactions.put(player.getGameProfile().id(), System.currentTimeMillis());
    }

    @Override
    public void tick() {
        this.cooldown = Math.max(this.cooldown - 1, 0);
    }
}
