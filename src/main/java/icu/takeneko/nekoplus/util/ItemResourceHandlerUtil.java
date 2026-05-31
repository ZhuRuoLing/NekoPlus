package icu.takeneko.nekoplus.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.PlayerInventoryWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class ItemResourceHandlerUtil {
    /**
     * Inserts the given itemstack into the players inventory.
     * If the inventory can't hold it, the item will be dropped in the world at the players position.
     *
     * @param player The player to give the item to
     * @param stack  The itemstack to insert
     */
    public static void giveItemToPlayer(Player player, ItemStack stack) {
        if (stack.isEmpty()) return;

        PlayerInventoryWrapper inventory = PlayerInventoryWrapper.of(player);
        Level level = player.level();
        int inserted = 0;
        ItemResource resource = ItemResource.of(stack);
        try (Transaction transaction = Transaction.openRoot()) {
            try (Transaction test = Transaction.open(transaction)) {
                inserted = inventory.insert(resource, stack.count(), test);
            }
            if (inserted >= 0) {
                inventory.insert(resource, inserted, transaction);
                transaction.commit();
            }
        }
        level.playSound(
            null,
            player.getX(),
            player.getY() + 0.5,
            player.getZ(),
            SoundEvents.ITEM_PICKUP,
            SoundSource.PLAYERS,
            0.2F,
            ((level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
        );
        if (inserted == stack.count()) return;

        // drop remaining itemstack into the level
        ItemEntity entity = new ItemEntity(level, player.getX(), player.getY() + 0.5, player.getZ(), resource.toStack(stack.count() - inserted));
        entity.setPickUpDelay(40);
        entity.setDeltaMovement(entity.getDeltaMovement().multiply(0, 1, 0));

        level.addFreshEntity(entity);
    }
}
