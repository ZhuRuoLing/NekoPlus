package icu.takeneko.highenergyanvilology.foundation.block.tile.hatch;

import icu.takeneko.highenergyanvilology.foundation.block.tile.hatch.logic.HatchLogic;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

public interface HatchType<C> extends StringRepresentable {
    HatchLogic<C> createHatchLogic(HatchLogicHost logicHost, boolean isInput);

    @Nullable
    C getCapability(HatchLogic<C> logic);

    BlockEntityType<?> getHostType(boolean isInput);
}
