package icu.takeneko.highenergyanvilology.internal;

import dev.dubhe.anvilcraft.event.giantanvil.shock.DestroyMode;
import dev.dubhe.anvilcraft.event.giantanvil.shock.DestroyType;
import dev.dubhe.anvilcraft.event.giantanvil.shock.ShockContext;
import icu.takeneko.highenergyanvilology.all.HETags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class GiantAnvilShockInternals {
    public static final DestroyType LIGHTWEIGHT_BLOCKS = new DestroyType() {

        @Override
        public void accept(ShockContext context, List<BlockPos> list, DestroyMode mode) {
            Level level = context.level();
            for (BlockPos pos : list) {
                BlockState state = level.getBlockState(pos);
                if (!state.is(HETags.Blocks.LIGHTWEIGHT_BLOCK)) continue;
                if (pos.distSqr(context.centerPos().above()) <= 2) continue;
                if (state.getBlock().defaultDestroyTime() < 0) continue;
                List<ItemStack> drops = mode.apply(state, pos, context);
                Containers.dropContents(level, pos, NonNullList.copyOf(drops));
                level.destroyBlock(pos, false);
            }
        }
    };
}
