package icu.takeneko.nekoplus.block.tile;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import dev.dubhe.anvilcraft.api.item.IDiskCloneable;
import icu.takeneko.nekoplus.block.ProgrammableLogicGateBlock;
import icu.takeneko.nekoplus.content.tile.logic.fpg.PinMode;
import icu.takeneko.nekoplus.content.tile.logic.fpg.PinState;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ExpEvaluationContext;
import icu.takeneko.nekoplus.foundation.block.tile.NPSynedBlockEntity;
import icu.takeneko.nekoplus.foundation.block.tile.NPUIBlock;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import icu.takeneko.nekoplus.ui.ProgrammableLogicGateUI;
import icu.takeneko.nekoplus.util.thirdparty.appeng.api.orientation.RelativeSide;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class ProgrammableLogicGateBlockEntity
    extends NPSynedBlockEntity
    implements NPUIBlock.Provider, IDiskCloneable {

    @Persisted
    @DescSynced
    @Getter
    private final PinState pinR = new PinState("r", PinMode.INPUT, RelativeSide.LEFT);

    @Persisted
    @DescSynced
    @Getter
    private final PinState pinG = new PinState("g", PinMode.INPUT, RelativeSide.BACK);

    @Persisted
    @DescSynced
    @Getter
    private final PinState pinB = new PinState("b", PinMode.INPUT, RelativeSide.RIGHT);

    @Persisted
    @DescSynced
    @Getter
    private final PinState pinW = new PinState("w", PinMode.OUTPUT, RelativeSide.FRONT);

    public ProgrammableLogicGateBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        pinR.bind(ProgrammableLogicGateBlock.RED_ENABLE);
        pinG.bind(ProgrammableLogicGateBlock.GREEN_ENABLE);
        pinB.bind(ProgrammableLogicGateBlock.BLUE_ENABLE);
        pinW.bind(ProgrammableLogicGateBlock.WHITE_ENABLE);
    }

    @Override
    public ModularUI getModularUI(BlockUIMenuType.BlockUIHolder holder) {
        return NPUI.of(new ProgrammableLogicGateUI(this), holder);
    }

    public void updatePins() {
        BlockPos blockPos = getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);
        ExpEvaluationContext context = new ExpEvaluationContext();
        pinR.updateContext(context);
        pinG.updateContext(context);
        pinB.updateContext(context);
        pinW.updateContext(context);
        blockState = pinR.update(level, blockPos, blockState, context);
        blockState = pinG.update(level, blockPos, blockState, context);
        blockState = pinB.update(level, blockPos, blockState, context);
        blockState = pinW.update(level, blockPos, blockState, context);
        level.setBlockAndUpdate(blockPos, blockState);
    }

    @Override
    public void storeDiskData(ValueOutput output) {

    }

    @Override
    public void applyDiskData(ValueInput input) {

    }
}
