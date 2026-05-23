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
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class ProgrammableLogicGateBlockEntity
    extends NPSynedBlockEntity
    implements NPUIBlock.Provider, IDiskCloneable, PinState.Callback {

    @Persisted
    @DescSynced
    @Getter
    private final PinState pinR = new PinState("r", PinMode.INPUT, RelativeSide.LEFT, this);

    @Persisted
    @DescSynced
    @Getter
    private final PinState pinG = new PinState("g", PinMode.INPUT, RelativeSide.BACK, this);

    @Persisted
    @DescSynced
    @Getter
    private final PinState pinB = new PinState("b", PinMode.INPUT, RelativeSide.RIGHT, this);

    @Persisted
    @DescSynced
    @Getter
    private final PinState pinW = new PinState("w", PinMode.OUTPUT, RelativeSide.FRONT, this);

    private final PinState[] pins = new PinState[]{
        pinR, pinG, pinB, pinW
    };

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

    public void scheduleUpdate() {
        if (level instanceof ServerLevel) {
            level.scheduleTick(getBlockPos(), getBlockState().getBlock(), 2);
        }
    }

    public int getSignal(Direction face) {
        for (PinState pin : pins) {
            if (pin.getDirection(this.getBlockState()) == face && pin.getMode() == PinMode.OUTPUT) {
                return pin.getState() ? 15 : 0;
            }
        }
        return 0;
    }

    @Override
    public void storeDiskData(ValueOutput output) {
        pinR.serialize(output.child("r"));
        pinG.serialize(output.child("g"));
        pinB.serialize(output.child("b"));
        pinW.serialize(output.child("w"));
    }

    @Override
    public void applyDiskData(ValueInput input) {
        input.child("r").ifPresent(pinR::deserialize);
        input.child("g").ifPresent(pinG::deserialize);
        input.child("b").ifPresent(pinB::deserialize);
        input.child("w").ifPresent(pinW::deserialize);
    }
}
