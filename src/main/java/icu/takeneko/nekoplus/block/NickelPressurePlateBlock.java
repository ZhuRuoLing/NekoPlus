package icu.takeneko.nekoplus.block;

import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class NickelPressurePlateBlock extends PressurePlateBlock {
    public NickelPressurePlateBlock(BlockBehaviour.Properties properties) {
        super(BlockSetType.IRON, properties);
    }
}
