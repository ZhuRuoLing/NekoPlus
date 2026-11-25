package icu.takeneko.nekoplus.mixin.anvilcraft.overclocking;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import dev.dubhe.anvilcraft.block.power.consumer.SmartBlockPlacerBlock;
import dev.dubhe.anvilcraft.util.Util;
import icu.takeneko.nekoplus.all.NPItems;
import icu.takeneko.nekoplus.internal.SmartBlockPlacerBlockEntityInternals;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(SmartBlockPlacerBlock.class)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class SmartBlockPlacerBlockMixin extends BaseEntityBlock {

    public SmartBlockPlacerBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useItemOn(
        ItemStack itemStack,
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        InteractionHand hand,
        BlockHitResult hitResult
    ) {
        if (!itemStack.is(NPItems.STABILIZE_POWDER)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        if (level.isClientSide()) {
            level.playSound(player, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS);
            return Util.sidedSuccess(level);
        }
        if (level.getBlockEntity(pos) instanceof SmartBlockPlacerBlockEntityInternals.Extension extension) {
            extension.toggleOverclock();
            itemStack.shrink(1);
            player.setItemInHand(hand, itemStack);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }
}
