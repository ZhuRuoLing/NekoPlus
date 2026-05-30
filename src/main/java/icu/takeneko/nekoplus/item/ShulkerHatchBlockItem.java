package icu.takeneko.nekoplus.item;

import icu.takeneko.nekoplus.all.NPTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class ShulkerHatchBlockItem extends BlockItem {
    public ShulkerHatchBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return level.getBlockState(pos).is(NPTags.Blocks.NESTED_SHULKER_BLOCK) && context.getClickedFace().getAxis().isHorizontal()
            ? this.useOn(context)
            : super.onItemUseFirst(stack, context);
    }
}
