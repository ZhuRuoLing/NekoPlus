package icu.takeneko.nekoplus.mixin.anvilcraft;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import dev.dubhe.anvilcraft.block.better.BetterBaseEntityBlock;
import dev.dubhe.anvilcraft.block.entity.ItemCollectorBlockEntity;
import dev.dubhe.anvilcraft.block.power.consumer.ItemCollectorBlock;
import dev.dubhe.anvilcraft.util.Util;
import icu.takeneko.nekoplus.foundation.block.tile.NPUIBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(ItemCollectorBlock.class)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class ItemCollectorBlockMixin extends BetterBaseEntityBlock implements NPUIBlock {

    protected ItemCollectorBlockMixin(Properties properties) {
        super(properties);
    }


    @Inject(
        method = "use",
        at = @At("HEAD"),
        cancellable = true
    )
    protected void handleNPGui(
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        InteractionHand hand,
        BlockHitResult hit,
        CallbackInfoReturnable<InteractionResult> cir
    ) {
        if (player.getItemInHand(hand).isEmpty() && player.isSecondaryUseActive()) {
            if (level instanceof ServerLevel) {
                if (level.getBlockEntity(pos) instanceof ItemCollectorBlockEntity) {
                    BlockUIMenuType.openUI((ServerPlayer) player, pos);
                    cir.cancel();
                    cir.setReturnValue(Util.sidedSuccess(level));
                }
            }
            cir.cancel();
            cir.setReturnValue(InteractionResult.SUCCESS);
        }

    }
}
