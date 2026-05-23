package icu.takeneko.nekoplus.content.tile.logic.fpg;

import com.lowdragmc.lowdraglib2.syncdata.IContentChangeAware;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import icu.takeneko.nekoplus.block.ProgrammableLogicGateBlock;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.EvaluationException;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ExpEvaluationContext;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ExpExpressionParser;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ExpParser;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.ast.AstNode;
import icu.takeneko.nekoplus.content.tile.logic.fpg.expression.validation.ExpValidator;
import icu.takeneko.nekoplus.util.thirdparty.appeng.api.orientation.RelativeSide;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class PinState implements ValueIOSerializable, IContentChangeAware {
    public static final Set<String> PREDEFINED_SYMBOLS = Set.of("w", "r", "g", "b");

    @Getter
    private final String name;
    private final RelativeSide side;
    private final Callback callback;
    private BooleanProperty boundProperty;

    @Getter
    private PinMode mode;

    private boolean state = false;
    private boolean dirty = true;
    @Getter
    private String pinExpression = "";

    @Getter
    @Setter
    private Runnable onContentsChanged;

    @Getter
    @Nullable
    private Component serverError = null;

    private EvaluationCache cache = new EvaluationCache();

    public PinState(String name, PinMode mode, RelativeSide side, Callback callback) {
        this.name = name;
        this.mode = mode;
        this.side = side;
        this.callback = callback;
    }

    public void load(SaveData data) {
        this.state = data.state;
        this.mode = data.mode;
        this.pinExpression = data.pinExpr;
    }

    public Direction getDirection(BlockState blockState) {
        return ProgrammableLogicGateBlock.ORIENTATION_STRATEGY.getSide(blockState, side);
    }

    public BlockState update(Level level, BlockPos pos, BlockState blockState, ExpEvaluationContext context) {
        if (mode == PinMode.DISABLE) return blockState;
        if (boundProperty == null) {
            throw new IllegalStateException("No bind state specified for pin " + name);
        }
        if (mode == PinMode.INPUT) {
            Direction direction = getDirection(blockState);
            this.state = level.getSignal(pos.relative(direction), direction.getOpposite()) > 0;
            context.put(this.name, this.state);
            return blockState.setValue(boundProperty, this.state);
        }
        if (mode == PinMode.OUTPUT) {
            try {
                this.state = evaluatePin(context);
                this.serverError = null;
            } catch (ExpParser.ParseException e) {
                this.serverError = e.getFormattedMessage();
                this.state = false;
            } catch (EvaluationException e) {
                this.serverError = e.getPrettyMessage();
                this.state = false;
            }
        }
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
            if (this.mode == PinMode.OUTPUT) {
                this.dirty = true;
            }
            onContentsChanged.run();
        }
    }

    public void setPinExpression(String pinExpression) {
        String old = this.pinExpression;
        this.pinExpression = pinExpression;
        if (!Objects.equals(old, pinExpression)) {
            dirty = true;

            if (onContentsChanged != null) {
                onContentsChanged.run();
            }
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
    public void serialize(ValueOutput output) {
        output.store("data", SaveData.CODEC, intoData());
    }

    @Override
    public void deserialize(ValueInput input) {
        Optional<SaveData> data = input.read("data", SaveData.CODEC);
        data.ifPresent(this::load);
    }

    public boolean evaluatePin(ExpEvaluationContext context) {
        if (dirty) {
            String[] strings = this.pinExpression.split("\n");
            List<AstNode> nodes = new ArrayList<>();
            for (String string : strings) {
                AstNode parsed = ExpExpressionParser.parse(string);
                nodes.add(parsed);
            }
            cache.rebuild(context, nodes, name);
            this.dirty = false;
        }
        return cache.getResult(context);
    }

    public static List<Component> validate(String... expressions) {
        Set<String> symbols = new HashSet<>(PREDEFINED_SYMBOLS);
        ExpValidator validator = new ExpValidator(symbols);
        for (String s : expressions) {
            validator.nextLine(s);
            try {
                AstNode parsed = ExpExpressionParser.parse(s);
                parsed.accept(validator);
            } catch (ExpParser.ParseException pe) {
                validator.getValidationResult().add(pe.getFormattedMessage());
            }
        }
        if (validator.getValidationResult().isEmpty()) return null;
        validator.addSummary();
        return validator.getValidationResult();
    }

    public boolean getState() {
        return state;
    }

    public void updateContext(ExpEvaluationContext context) {
        if (this.mode == PinMode.INPUT) {
            context.put(name, state);
            return;
        }
        //context.put(name, false);
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

    public interface Callback {
        void scheduleUpdate();
    }
}
