package icu.takeneko.nekoplus.block.tile.logic.fpg;

import com.lowdragmc.lowdraglib2.syncdata.IContentChangeAware;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import icu.takeneko.nekoplus.block.ProgrammableLogicGateBlock;
import icu.takeneko.nekoplus.util.CodecUtils;
import icu.takeneko.nekoplus.util.thirdparty.appeng.api.orientation.HorizontalFacingStrategy;
import icu.takeneko.nekoplus.util.thirdparty.appeng.api.orientation.IOrientationStrategy;
import icu.takeneko.nekoplus.util.thirdparty.appeng.api.orientation.RelativeSide;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.Objects;

public class PinState implements INBTSerializable<CompoundTag>, IContentChangeAware {
    @Getter
    private final String name;
    private final RelativeSide side;
    private BooleanProperty boundProperty;


    @Getter
    private PinMode mode = PinMode.DISABLE;

    @Getter
    private boolean state = false;

    @Getter
    private String pinExpression = "";

    @Getter
    @Setter
    private Runnable onContentsChanged;

    public PinState(String name, PinMode mode, RelativeSide side) {
        this.name = name;
        this.mode = mode;
        this.side = side;
    }

    public void load(SaveData data) {
        this.state = data.state;
        this.mode = data.mode;
        this.pinExpression = data.pinExpr;
    }

    public BlockState update(Level level, BlockPos pos, BlockState blockState) {
        if (mode == PinMode.DISABLE) return blockState;
        if (boundProperty == null) throw new IllegalStateException("No bind state specified for pin " + name);
        if (mode == PinMode.INPUT) {
            Direction direction = ProgrammableLogicGateBlock.ORIENTATION_STRATEGY.getSide(blockState, side);
            this.state = level.getSignal(pos.relative(direction), direction.getOpposite()) > 0;
            return blockState.setValue(boundProperty, this.state);
        }
        if (mode != PinMode.OUTPUT) return blockState;
        return blockState.setValue(boundProperty, this.state);
    }

    public void bind(BooleanProperty property) {
        this.boundProperty = property;
    }

    public SaveData intoData() {
        return new SaveData(
            this.mode,
            this.state,
            this.pinExpression
        );
    }

    public void setMode(PinMode mode) {
        PinMode old = this.mode;
        this.mode = mode;
        if (old != mode && onContentsChanged != null) {
            onContentsChanged.run();
        }
    }

    public void setPinExpression(String pinExpression) {
        String old = this.pinExpression;
        this.pinExpression = pinExpression;
        if (!Objects.equals(old, pinExpression) && onContentsChanged != null) {
            onContentsChanged.run();
        }
    }

    public void setState(boolean state) {
        boolean old = this.state;
        this.state = state;
        if (old != state && onContentsChanged != null) {
            onContentsChanged.run();
        }
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return (CompoundTag) SaveData.CODEC.encodeStart(NbtOps.INSTANCE, intoData()).getOrThrow();
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {
        SaveData saveData = SaveData.CODEC.decode(NbtOps.INSTANCE, compoundTag).getOrThrow().getFirst();
        this.load(saveData);
    }

    public record SaveData(
        PinMode mode,
        boolean state,
        String pinExpr
    ) {
        public static final Codec<SaveData> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            PinMode.CODEC.fieldOf("mode").forGetter(SaveData::mode),
            Codec.BOOL.fieldOf("state").forGetter(SaveData::state),
            Codec.STRING.fieldOf("pinExpr").forGetter(SaveData::pinExpr)
        ).apply(ins, SaveData::new));
    }
}
