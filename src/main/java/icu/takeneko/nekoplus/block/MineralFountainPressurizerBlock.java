package icu.takeneko.nekoplus.block;

import dev.dubhe.anvilcraft.block.state.Cube323PartHalf;
import icu.takeneko.nekoplus.foundation.block.NPSimpleMultiPartBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class MineralFountainPressurizerBlock extends NPSimpleMultiPartBlock<Cube323PartHalf> {
    public static final EnumProperty<Cube323PartHalf> PART = EnumProperty.create("part", Cube323PartHalf.class);

    public MineralFountainPressurizerBlock(Properties properties) {
        super(properties);
        registerDefaultState(
            getStateDefinition()
                .any()
                .setValue(PART, Cube323PartHalf.BOTTOM_CENTER)
        );
    }

    @Override
    public Property<Cube323PartHalf> getPart() {
        return PART;
    }

    @Override
    public Cube323PartHalf[] getParts() {
        return Cube323PartHalf.values();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PART);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }
}
