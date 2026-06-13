package icu.takeneko.nekoplus.block.tile;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.field.ManagedFieldHolder;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.foundation.block.tile.NPPowerConsumer;
import icu.takeneko.nekoplus.foundation.block.tile.NPSynedBlockEntity;
import icu.takeneko.nekoplus.foundation.block.tile.NPUIBlock;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import icu.takeneko.nekoplus.ui.TestUI;
import it.unimi.dsi.fastutil.ints.Int2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class TestBlockEntity extends NPSynedBlockEntity implements NPPowerConsumer, NPUIBlock.Provider {
    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(TestBlockEntity.class);
    private static final String CSV_HEADER = "sample,generated_power";

    @Getter
    private final Int2IntMap generatedPowerRecords = new Int2IntLinkedOpenHashMap();

    @Nullable
    private PowerGrid grid;

    @DescSynced
    private boolean isOverload = false;

    @Getter
    @DescSynced
    private boolean recording = false;

    @DescSynced
    private int recordCount = 0;

    @DescSynced
    private int lastGeneratedPower = 0;

    public TestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public void startRecording() {
        this.generatedPowerRecords.clear();
        this.recording = true;
        this.recordCount = 0;
        this.lastGeneratedPower = 0;
        setChanged();
    }

    public void stopRecording() {
        this.recording = false;
        setChanged();
    }

    @Override
    public void gridTick() {
        if (!this.recording || this.grid == null) {
            return;
        }
        int sample = this.recordCount;
        int generatedPower = this.grid.getGenerate();
        this.generatedPowerRecords.put(sample, generatedPower);
        this.recordCount = sample + 1;
        this.lastGeneratedPower = generatedPower;
        setChanged();
    }

    public void exportRecordsCsv() {
        StringBuilder builder = new StringBuilder(CSV_HEADER).append('\n');
        for (Int2IntMap.Entry entry : this.generatedPowerRecords.int2IntEntrySet()) {
            builder.append(entry.getIntKey())
                .append(',')
                .append(entry.getIntValue())
                .append('\n');
        }
        NekoPlus.LOGGER.info(
            "Test block power records at {}:\n{}",
            getBlockPos(),
            builder
        );
    }

    public String getRecordStatusText() {
        return "status="
            + (this.recording ? "recording" : "stopped")
            + ", samples="
            + this.recordCount
            + ", generated="
            + this.lastGeneratedPower;
    }

    @Override
    public @Nullable Level getCurrentLevel() {
        return getLevel();
    }

    @Override
    public BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    public void setGrid(@Nullable PowerGrid grid) {
        this.grid = grid;
    }

    @Override
    public @Nullable PowerGrid getGrid() {
        return this.grid;
    }

    @Override
    public void setOverload(boolean value) {
        this.isOverload = value;
    }

    @Override
    public boolean isOverload() {
        return this.isOverload;
    }

    @Override
    public ModularUI getModularUI(BlockUIMenuType.BlockUIHolder holder) {
        return NPUI.of(new TestUI(this), holder);
    }
}
