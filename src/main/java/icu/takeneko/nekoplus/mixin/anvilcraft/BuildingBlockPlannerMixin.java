package icu.takeneko.nekoplus.mixin.anvilcraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.anvilcraft.building.BuildingBlockPlanner;
import dev.dubhe.anvilcraft.building.BuildingPlan.Cell;
import icu.takeneko.nekoplus.foundation.block.NPSimpleMultiPartBlock;
import icu.takeneko.nekoplus.item.NPSimpleMultiPartBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BuildingBlockPlanner.class)
public abstract class BuildingBlockPlannerMixin {
    @Shadow
    private static List<Cell> singleAttempt(BlockItem item, UseOnContext use) {
        throw new AssertionError();
    }

    @Inject(method = "singlePlacement", at = @At("RETURN"), cancellable = true)
    private static void retryNPMultipart(UseOnContext use, CallbackInfoReturnable<List<Cell>> cir) {
        if (!cir.getReturnValue().isEmpty() || use.getPlayer() == null
            || !(use.getItemInHand().getItem() instanceof NPSimpleMultiPartBlockItem<?> item)) {
            return;
        }
        int distance = item.getMaxOffsetDistance(use.getClickedFace());
        cir.setReturnValue(singleAttempt(item, new UseOnContext(
            use.getLevel(), use.getPlayer(), use.getHand(), use.getItemInHand(),
            new BlockHitResult(use.getClickLocation().relative(use.getClickedFace(), distance), use.getClickedFace(),
                use.getClickedPos().relative(use.getClickedFace(), distance), false))));
    }

    @WrapOperation(
        method = "tile",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;getStateForPlacement(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;"
        )
    )
    private static @Nullable BlockState getNPPlacementState(
        Block block, BlockPlaceContext context, Operation<BlockState> original
    ) {
        return block instanceof NPSimpleMultiPartBlock<?> multipart
            ? multipart.getPlacementState(context) : original.call(block, context);
    }
}
