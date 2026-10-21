package icu.takeneko.nekoplus.mixin.anvilcraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.anvilcraft.block.state.ISimpleMultiPartBlockState;
import dev.dubhe.anvilcraft.building.BuildingPlan.Cell;
import dev.dubhe.anvilcraft.client.event.LargeBlockPlacePreviewEventListener;
import icu.takeneko.nekoplus.foundation.block.NPSimpleMultiPartBlock;
import icu.takeneko.nekoplus.item.NPSimpleMultiPartBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(LargeBlockPlacePreviewEventListener.class)
public abstract class LargeBlockPlacePreviewEventListenerMixin {
    @WrapOperation(
        method = "updatePreview",
        at = @At(
            value = "INVOKE",
            target = "Ldev/dubhe/anvilcraft/building/BuildingBlockPlanner;singlePlacement(Lnet/minecraft/world/item/context/UseOnContext;)Ljava/util/List;"
        )
    )
    private static List<Cell> previewFailedNPMultipart(UseOnContext use, Operation<List<Cell>> original) {
        List<Cell> cells = original.call(use);
        if (!cells.isEmpty() || !(use.getItemInHand().getItem() instanceof NPSimpleMultiPartBlockItem<?> item)
            || !(item.getBlock() instanceof NPSimpleMultiPartBlock<?> block)) {
            return cells;
        }
        int distance = item.getMaxOffsetDistance(use.getClickedFace());
        BlockPlaceContext context = item.updatePlacementContext(new BlockPlaceContext(new UseOnContext(
            use.getLevel(), use.getPlayer(), use.getHand(), use.getItemInHand(),
            new BlockHitResult(use.getClickLocation().relative(use.getClickedFace(), distance), use.getClickedFace(),
                use.getClickedPos().relative(use.getClickedFace(), distance), false))));
        if (context == null) return cells;
        BlockState state = block.getPlacementState(context);
        return state == null ? cells : np$previewCells(block, context.getClickedPos(), state);
    }

    @Unique
    private static <P extends Enum<P> & ISimpleMultiPartBlockState<@NonNull P>> List<Cell> np$previewCells(
        NPSimpleMultiPartBlock<P> block, BlockPos pos, BlockState state
    ) {
        List<Cell> cells = new ArrayList<>();
        cells.add(new Cell(pos, state, new CompoundTag(), List.of()));
        for (P part : block.getParts()) {
            if (part == state.getValue(block.getPart())) continue;
            cells.add(new Cell(pos.offset(block.offsetFrom(state, part)), block.placedState(part, state),
                new CompoundTag(), List.of()));
        }
        return cells;
    }
}
