package icu.takeneko.nekoplus.internal;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.api.entity.fakeplayer.AnvilCraftFakePlayers;
import dev.dubhe.anvilcraft.api.itemhandler.ItemHandlerUtil;
import dev.dubhe.anvilcraft.block.BlockPlacerBlock;
import dev.dubhe.anvilcraft.block.state.Orientation;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import dev.dubhe.anvilcraft.item.AnvilHammerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;

public class BlockPlacerBlockInternals {
    public static final EnumProperty<Mode> MODE = EnumProperty.create("mode", Mode.class);

    public static void onActivate(int distance, Level level, BlockPos blockPos, Orientation orientation) {
        Direction direction = orientation.getDirection();
        BlockPos inputPos = blockPos.relative(direction.getOpposite());
        ItemStack itemStack = ItemStack.EMPTY;
        ItemFrame itemFrame = null;
        ItemEntity itemEntity = null;
        IItemHandler itemHandler = null;
        int slot = -1;
        for (ItemFrame entitiesOfClass : level.getEntitiesOfClass(
            ItemFrame.class,
            AABB.encapsulatingFullBlocks(inputPos, inputPos)
        )) {
            if (!entitiesOfClass.getItem().isEmpty()) {
                itemFrame = entitiesOfClass;
                itemStack = itemFrame.getItem();
                break;
            }
        }


        if (itemStack.isEmpty()) {
            itemHandler = ItemHandlerUtil.getSourceItemHandlerRecursive(ModBlocks.BLOCK_PLACER.get(), blockPos, direction, level);
            for (slot = 0; itemHandler != null && slot < itemHandler.getSlots(); slot++) {
                ItemStack blockItemStack = itemHandler.extractItem(slot, 1, true);
                if (!blockItemStack.isEmpty()) {
                    itemStack = blockItemStack;
                    break;
                }
            }


            // 从放置器背后的掉落物中获取物品
            if (itemHandler == null) {
                int i = 0;
                do {
                    if (level.getBlockState(inputPos).is(ModBlocks.BLOCK_PLACER.get())
                        && level.getBlockState(inputPos).getValue(BlockPlacerBlock.ORIENTATION).getDirection() == direction
                    ) {
                        i++;
                        inputPos = inputPos.relative(direction.getOpposite());
                    } else {
                        AABB aabb = new AABB(inputPos);
                        List<ItemEntity> entities =
                            level.getEntities(
                                EntityTypeTest.forClass(ItemEntity.class),
                                aabb,
                                Entity::isAlive
                            );
                        if (entities.isEmpty()) break;
                        for (ItemEntity entity : entities) {
                            if (!entity.getItem().isEmpty()) {
                                itemEntity = entity;
                                itemStack = entity.getItem();
                                break;
                            }
                        }
                    }
                } while (itemEntity == null && i < AnvilCraft.CONFIG.blockPlacerRecursiveRetrievalDistanceMax);
            }
        }
        ServerPlayer player = AnvilCraftFakePlayers.anvilcraftBlockPlacer.getPlayer();
        if (level instanceof ServerLevel serverLevel) player.setServerLevel(serverLevel);
        if (itemStack.getItem().getFoodProperties(itemStack, player) != null) return;
        //"(24.5, -64.12000000476837, -8.5)"
        Orientation fakePlayerOrientation = orientation.flipHorizontalIfVertical();
        player.setPos(blockPos.getCenter().add(direction.getStepX(), -player.getEyeHeight() + direction.getStepY(), direction.getStepZ()));
        player.setYRot(fakePlayerOrientation.getYRotation());
        player.setXRot(fakePlayerOrientation.getXRotation());
        player.setYHeadRot(fakePlayerOrientation.getYRotation());
        player.setOldPosAndRot();
        HitResult picked = pick(player, Math.max(6, distance + 1), false);

        if (picked.getType() == HitResult.Type.MISS) return;
        InteractionResult result = InteractionResult.FAIL;
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack.copy());
        if (picked instanceof BlockHitResult blockHitResult) {
            if (itemStack.getItem() instanceof AnvilHammerItem) {
                AnvilHammerItem.dropAnvil(player, level, blockPos);
            } else {
                result = player.gameMode.useItemOn(player, level, itemStack, InteractionHand.MAIN_HAND, blockHitResult);
            }

        }
        if (picked instanceof EntityHitResult entityHitResult) {
            result = player.interactOn(entityHitResult.getEntity(), InteractionHand.MAIN_HAND);
        }

        ItemStack itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (result.indicateItemUse()
            && (itemInHand.getCount() != itemStack.getCount() || itemInHand.getDamageValue() != itemStack.getDamageValue())
        ) {
            if (itemStack.isDamageableItem()) {
                itemStack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            } else {
                itemStack.shrink(1);
            }
        }

        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        if (itemFrame != null) {
            itemFrame.setItem(itemStack.isEmpty() ? ItemStack.EMPTY : itemStack);
        }
        if (itemEntity != null) {
            itemEntity.setItem(itemStack);
            if (itemStack.isEmpty()) {
                itemEntity.discard();
            }
        }
        if (itemHandler != null) {
            if (slot != -1) {
                itemHandler.extractItem(slot, 1, false);
            }
        }
    }

    private static HitResult pick(Entity entity, double hitDistance, boolean hitFluids) {
        Vec3 vec3 = new Vec3(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
        Vec3 vec31 = entity.calculateViewVector(entity.getXRot(), entity.getYRot());
        Vec3 vec32 = vec3.add(vec31.x * hitDistance, vec31.y * hitDistance, vec31.z * hitDistance);
        return entity.level().clip(new ClipContext(vec3, vec32, ClipContext.Block.OUTLINE, hitFluids ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, entity));
    }

    public enum Mode implements StringRepresentable {
        PLACER, CLICKER;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
