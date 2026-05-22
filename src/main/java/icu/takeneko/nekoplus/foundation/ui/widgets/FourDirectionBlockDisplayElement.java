package icu.takeneko.nekoplus.foundation.ui.widgets;

import com.lowdragmc.lowdraglib2.gui.sync.bindings.IBindable;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.IBinding;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.IDataSource;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvent;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents;
import com.lowdragmc.lowdraglib2.gui.util.UISoundUtils;
import dev.anvilcraft.lib.v2.util.MathUtil;
import icu.takeneko.nekoplus.content.tile.logic.fpg.PinMode;
import lombok.Getter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.util.EnumMap;
import java.util.Optional;

public class FourDirectionBlockDisplayElement extends UIElement {

    @Getter
    private final EnumMap<ColorDirection, DirectionEntry> entries = new EnumMap<>(ColorDirection.class);
    @Getter
    private BlockState blockState = Blocks.FURNACE.defaultBlockState();
    @Getter
    private BlockEntity blockEntity = null;
    private float yRot0;

    public FourDirectionBlockDisplayElement() {
        this.layout(l -> l.minHeight(110));
        for (ColorDirection value : ColorDirection.values()) {
            DirectionEntry entry = new DirectionEntry();
            entries.put(value, entry);
            addChild(entry.ioState);
        }
        updateYRot(0);
        addEventListener(UIEvents.MOUSE_DOWN, this::onMouseDown);
    }

    private void onMouseDown(UIEvent uiEvent) {
        float x = getPositionX();
        float y = getPositionY();
        float width = getContentWidth();
        float height = getContentHeight();
        float horizontalCenter = x + width / 2;
        float verticalCenter = y + height / 2;
        if (uiEvent.button != GLFW.GLFW_MOUSE_BUTTON_LEFT) return;
        Vector2f position = new Vector2f(uiEvent.x, uiEvent.y);
        Optional<DirectionEntry> optional = entries.values().stream()
            .filter(it -> position.distance(it.position.x + horizontalCenter, it.position.y + verticalCenter) <= 10).findFirst();
        optional.ifPresent(entry -> {
            entry.callback.run();
            UISoundUtils.playButtonClickSound();
        });
    }

    public FourDirectionBlockDisplayElement block(BlockState blockState, BlockEntity blockEntity) {
        this.blockState = blockState;
        this.blockEntity = blockEntity;
        return this;
    }

    public FourDirectionBlockDisplayElement yRot0(float yRot0) {
        this.yRot0 = yRot0;
        return this;
    }

    public FourDirectionBlockDisplayElement bindIOState(ColorDirection direction, @Nullable IBinding<PinMode> binding) {
        entries.get(direction).ioState.bind(binding);
        return this;
    }

    public FourDirectionBlockDisplayElement setOnClickListener(ColorDirection direction, Runnable callback) {
        entries.get(direction).callback = callback;
        return this;
    }

    public void updateYRot(float clientRotation) {
        for (ColorDirection value : ColorDirection.values()) {
            float calculated = value.defaultYRot() + yRot0 + clientRotation;
            entries.get(value).rotationY = calculated;
            entries.get(value).position = MathUtil.rotationDegrees(new Vector2f(0, 45), -calculated);
        }
    }

    public enum ColorDirection {
        RED, GREEN, BLUE, WHITE;

        public int color() {
            return switch (this) {
                case RED -> 0xf35c5b;
                case GREEN -> 0x5cf35b;
                case BLUE -> 0x5cf3f2;
                case WHITE -> 0xf3f3f2;
            };
        }

        public int defaultYRot() {
            return switch (this) {
                case RED -> 90;
                case GREEN -> 180;
                case BLUE -> 270;
                case WHITE -> 0;
            };
        }
    }

    public static class BoundIODirectionUIStub extends UIElement implements IBindable<PinMode> {

        @Getter
        private PinMode mode = PinMode.DISABLE;

        @Override
        public PinMode getValue() {
            return mode;
        }

        @Override
        public IDataSource<PinMode> setValue(@Nullable PinMode value) {
            if (value == null) {
                mode = PinMode.DISABLE;
                return this;
            }
            mode = value;
            return this;
        }
    }

    public static class DirectionEntry {
        public float rotationY = 0;
        public Runnable callback = DirectionEntry::emptyCallback;
        public Vector2f position = new Vector2f(0, 42);
        public final BoundIODirectionUIStub ioState = new BoundIODirectionUIStub();

        private static void emptyCallback() {
        }
    }
}
