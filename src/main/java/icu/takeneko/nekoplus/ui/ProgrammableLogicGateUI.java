package icu.takeneko.nekoplus.ui;

import com.lowdragmc.lowdraglib2.gui.sync.bindings.IBinding;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.IDataSource;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.DataBindingBuilder;
import com.lowdragmc.lowdraglib2.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.TextWrap;
import com.lowdragmc.lowdraglib2.gui.ui.data.Tooltips;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Button;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Selector;
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextArea;
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import com.lowdragmc.lowdraglib2.gui.ui.rendering.GUIContext;
import com.lowdragmc.lowdraglib2.gui.ui.rendering.IGUIContext;
import dev.vfyjxf.taffy.style.AlignContent;
import dev.vfyjxf.taffy.style.AlignItems;
import dev.vfyjxf.taffy.style.FlexDirection;
import dev.vfyjxf.taffy.style.TaffyDisplay;
import dev.vfyjxf.taffy.style.TaffyPosition;
import icu.takeneko.nekoplus.block.ProgrammableLogicGateBlock;
import icu.takeneko.nekoplus.block.tile.ProgrammableLogicGateBlockEntity;
import icu.takeneko.nekoplus.content.tile.logic.fpg.PinMode;
import icu.takeneko.nekoplus.content.tile.logic.fpg.PinState;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import icu.takeneko.nekoplus.foundation.ui.widgets.FourDirectionBlockDisplayElement;
import icu.takeneko.nekoplus.foundation.ui.widgets.ResizeAwareUIElement;
import icu.takeneko.nekoplus.util.NPUIUtils;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.joml.Vector2f;

import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

public class ProgrammableLogicGateUI extends NPUI<ProgrammableLogicGateBlockEntity> {
    private static final EnumMap<FourDirectionBlockDisplayElement.ColorDirection, WindowPosition> WINDOW_POSITION_CACHE = new EnumMap<>(
        FourDirectionBlockDisplayElement.ColorDirection.class);
    private final ResizeAwareUIElement[] windows = new ResizeAwareUIElement[4];

    public ProgrammableLogicGateUI(ProgrammableLogicGateBlockEntity blockEntity) {
        super(blockEntity, Component.translatable("block.nekoplus.programmable_logic_gate"));
        BlockState state = blockEntity.getBlockState();
        Direction direction = state.getValue(ProgrammableLogicGateBlock.FACING);
        int yRot = (((int) direction.toYRot() + 180) % 360) - (direction.getAxis() == Direction.Axis.X ? -180 : 0);
        ResizeAwareUIElement redWindow = createPinWindow(
            blockEntity.getPinR(),
            "ui.programmable_logic_gate.red",
            FourDirectionBlockDisplayElement.ColorDirection.RED.color(),
            FourDirectionBlockDisplayElement.ColorDirection.RED
        );
        ResizeAwareUIElement greenWindow = createPinWindow(
            blockEntity.getPinG(),
            "ui.programmable_logic_gate.green",
            FourDirectionBlockDisplayElement.ColorDirection.GREEN.color(),
            FourDirectionBlockDisplayElement.ColorDirection.GREEN
        );
        ResizeAwareUIElement blueWindow = createPinWindow(
            blockEntity.getPinB(),
            "ui.programmable_logic_gate.blue",
            FourDirectionBlockDisplayElement.ColorDirection.BLUE.color(),
            FourDirectionBlockDisplayElement.ColorDirection.BLUE
        );
        ResizeAwareUIElement whiteWindow = createPinWindow(
            blockEntity.getPinW(),
            "ui.programmable_logic_gate.white",
            FourDirectionBlockDisplayElement.ColorDirection.WHITE.color(),
            FourDirectionBlockDisplayElement.ColorDirection.WHITE
        );
        windows[0] = redWindow;
        windows[1] = greenWindow;
        windows[2] = blueWindow;
        windows[3] = whiteWindow;
        addChildren(
            new FourDirectionBlockDisplayElement()
                .block(state, blockEntity)
                .yRot0(yRot)
                .bindIOState(
                    FourDirectionBlockDisplayElement.ColorDirection.RED,
                    DataBindingBuilder.enumValS2C(PinMode.class, blockEntity.getPinR()::getMode).build()
                ).bindIOState(
                    FourDirectionBlockDisplayElement.ColorDirection.GREEN,
                    DataBindingBuilder.enumValS2C(PinMode.class, blockEntity.getPinG()::getMode).build()
                ).bindIOState(
                    FourDirectionBlockDisplayElement.ColorDirection.BLUE,
                    DataBindingBuilder.enumValS2C(PinMode.class, blockEntity.getPinB()::getMode).build()
                ).bindIOState(
                    FourDirectionBlockDisplayElement.ColorDirection.WHITE,
                    DataBindingBuilder.enumValS2C(PinMode.class, blockEntity.getPinW()::getMode).build()
                )
                .setOnClickListener(
                    FourDirectionBlockDisplayElement.ColorDirection.RED, () -> {
                        redWindow.setDisplay(TaffyDisplay.DEFAULT);
                        NPUIUtils.forceRelayout(this);
                        saveWindowPosition(FourDirectionBlockDisplayElement.ColorDirection.RED, redWindow, true);
                    }
                )
                .setOnClickListener(
                    FourDirectionBlockDisplayElement.ColorDirection.GREEN, () -> {
                        greenWindow.setDisplay(TaffyDisplay.DEFAULT);
                        NPUIUtils.forceRelayout(this);
                        saveWindowPosition(FourDirectionBlockDisplayElement.ColorDirection.GREEN, greenWindow, true);
                    }
                )
                .setOnClickListener(
                    FourDirectionBlockDisplayElement.ColorDirection.BLUE, () -> {
                        blueWindow.setDisplay(TaffyDisplay.DEFAULT);
                        NPUIUtils.forceRelayout(this);
                        saveWindowPosition(FourDirectionBlockDisplayElement.ColorDirection.BLUE, blueWindow, true);
                    }
                )
                .setOnClickListener(
                    FourDirectionBlockDisplayElement.ColorDirection.WHITE, () -> {
                        whiteWindow.setDisplay(TaffyDisplay.DEFAULT);
                        NPUIUtils.forceRelayout(this);
                        saveWindowPosition(FourDirectionBlockDisplayElement.ColorDirection.WHITE, whiteWindow, true);
                    }
                ),
            new TextElement()
                .setText(Component.translatable("container.inventory"))
                .textStyle(ts -> ts.adaptiveHeight(true)),
            new InventorySlots()
        );
        addChildren(redWindow, greenWindow, blueWindow, whiteWindow);
    }

    @Override
    protected void drawBackgroundAdditional(IGUIContext context) {
        super.drawBackgroundAdditional(context);
        if (context instanceof GUIContext guiContext) {
            NPUIUtils.drawResizeIcon(guiContext, 4, windows);
        }
    }

    private ResizeAwareUIElement createPinWindow(
        PinState state,
        String name,
        int color,
        FourDirectionBlockDisplayElement.ColorDirection direction
    ) {
        ResizeAwareUIElement background = new ResizeAwareUIElement();
        background.style(s -> s.backgroundTexture(new ColorRectTexture(color | 0xff000000)));
        background.layout(l -> l
            .positionType(TaffyPosition.ABSOLUTE)
            .display(TaffyDisplay.NONE)
            .heightFitContent()
            .widthFitContent()
            .left(6)
            .top(6)
            .paddingAll(2)
            .minWidth(140)
        );
        // restore cached position/size/display
        WindowPosition cached = WINDOW_POSITION_CACHE.get(direction);
        if (cached != null && cacheValid(cached)) {
            background.layout(l -> l
                .left(cached.x())
                .top(cached.y())
                .width(cached.width())
                .height(cached.height())
                .display(cached.shown() ? TaffyDisplay.DEFAULT : TaffyDisplay.NONE)
            );
        }
        UIElement root = new UIElement();
        root.style(s -> s.background(NPGuiResources.UI_BACKGROUND));
        background.addChildren(root);
        root.layout(l ->
            l.paddingAll(4)
                .minHeightPercent(100)
                .minWidthPercent(100)
                .flexDirection(FlexDirection.COLUMN)
        );

        UIElement titleBar = new UIElement().layout(layout -> {
            layout.flexDirection(FlexDirection.ROW);
            layout.justifyContent(AlignContent.SPACE_BETWEEN);
            layout.alignItems(AlignItems.CENTER);
            layout.gapAll(2);
        });

        NPUIUtils.setDragMove(
            titleBar,
            background,
            null,
            e -> saveWindowPosition(direction, background)
        );
        NPUIUtils.setBorderResize(
            background,
            background,
            4,
            new Vector2f(140, 76),
            new Vector2f(1000, 1000),
            null,
            (a, b) -> {
                background.setResizing(true);
                return true;
            },
            e -> {
                saveWindowPosition(direction, background);
                background.setResizing(false);
            }
        );

        titleBar.addChildren(
            new TextElement()
                .setText(name, true)
                .textStyle(textStyle -> textStyle.textWrap(TextWrap.HOVER_ROLL)
                    .adaptiveHeight(true)
                    .textShadow(false)
                    .textColor(0xff403e53)
                ),
            new Button()
                .noText()
                .buttonStyle(s -> s.baseTexture(NPGuiResources.UI_BACKGROUND)
                    .hoverTexture(NPGuiResources.BUTTON_HOVERED)
                    .pressedTexture(NPGuiResources.BUTTON_PRESSED)
                ).addPostIcon(NPGuiResources.CROSS)
                .setOnClick(e -> {
                    background.layout(l -> l.display(TaffyDisplay.NONE));
                    updatePositionWithClosed(direction);
                }).layout(l -> l.width(14).height(14))
        );
        root.addChildren(titleBar);
        TextArea codeEditor = new TextArea();
        codeEditor.contentView.style(s -> s.background(IGuiTexture.EMPTY));
        IBinding<String> expressionBinding = DataBindingBuilder
            .create(state::getPinExpression, state::setPinExpression)
            .syncType(String.class)
            .build();
        expressionBinding.setRemoteDataSource(new IDataSource<>() {
            @Override
            public String getValue() {
                return String.join("\n", codeEditor.getValue());
            }

            @Override
            public IDataSource<String> setValue(@Nullable String value) {
                String current = String.join("\n", codeEditor.getValue());
                if (Objects.equals(current, value)) {
                    return this;
                }
                String[] lines;
                if (value == null || value.isEmpty()) {
                    lines = new String[]{""};
                    codeEditor.setValue(lines, false);
                } else {
                    lines = value.split("\n", -1);
                    codeEditor.setValue(lines, false);
                }
                List<Component> validationResult = PinState.validate(lines);
                codeEditor.style(s -> {
                    if (validationResult == null) {
                        s.tooltips();
                    } else {
                        s.tooltips(validationResult.toArray(new Component[0]));
                    }
                });
                return this;
            }
        });
        codeEditor.addSyncValue(expressionBinding.getSyncValue());
        codeEditor.registerValueListener(arr -> {
            expressionBinding.getSyncValue().setValue(String.join("\n", arr));
            List<Component> validationResult = PinState.validate(arr);
            codeEditor.style(s -> {
                if (validationResult == null) {
                    s.tooltips(Tooltips.empty());
                } else {
                    s.tooltips(validationResult.toArray(new Component[0]));
                }
            });
        });
        UIElement expression = horizontalLayout(
            new TextElement().setText(Component.translatable("ui.programmable_logic_gate.expression")),
            codeEditor
                .textAreaStyle(ts -> ts.focusOverlay(IGuiTexture.EMPTY))
                .layout(l ->
                    l.alignSelf(AlignItems.END)
                        .height(20)
                        .minHeight(20)
                        .flexGrow(1)
                        .heightPercent(100)
                        .paddingVertical(2)
                        .justifySelf(AlignItems.END)
                ).style(s -> s.background(NPGuiResources.TEXT_AREA_BACKGROUND))
        ).layout(l ->
            l.alignItems(AlignItems.CENTER)
                .justifyContent(AlignContent.SPACE_BETWEEN)
                .paddingVertical(4)
                .widthPercent(100)
                .gapAll(4)
                .flexGrow(1)
        ).setDisplay(state.getMode() == PinMode.OUTPUT);
        Selector<PinMode> pinModeSelector = new Selector<>();
        pinModeSelector.buttonIcon.style(s -> s.background(NPGuiResources.DOWN_ARROW));
        pinModeSelector.bind(DataBindingBuilder.enumVal(PinMode.class, state::getMode, state::setMode).build());
        UIElement pinMode = horizontalLayout(
            new TextElement()
                .setText(Component.translatable("ui.programmable_logic_gate.pin_mode")),
            pinModeSelector
                .selectorStyle(s -> s.showOverlay(false).focusOverlay(IGuiTexture.EMPTY))
                .setCandidates(List.of(PinMode.values()))
                .setOnValueChanged(pm -> expression.setDisplay(pm == PinMode.OUTPUT))
                .layout(l ->
                    l.alignSelf(AlignItems.END)
                        .minWidthPercent(50)
                        .paddingVertical(2)
                        .justifySelf(AlignItems.END)
                ).style(s -> s.background(NPGuiResources.UI_BACKGROUND))
        ).layout(l ->
            l.alignItems(AlignItems.CENTER)
                .justifyContent(AlignContent.SPACE_BETWEEN)
                .paddingVertical(4)
                .widthPercent(100)
        );
        root.addChildren(pinMode);
        root.addChildren(expression);
        return background;
    }

    private static boolean cacheValid(WindowPosition cached) {
        return cached.x != 0
            && cached.y != 0
            && cached.width != 0
            && cached.height != 0;
    }

    private void saveWindowPosition(FourDirectionBlockDisplayElement.ColorDirection direction, UIElement background) {
        saveWindowPosition(direction, background, background.isDisplayed());
    }

    private void updatePositionWithClosed(FourDirectionBlockDisplayElement.ColorDirection direction) {
        WindowPosition windowPosition = WINDOW_POSITION_CACHE.get(direction);
        if (windowPosition == null || !cacheValid(windowPosition)) return;
        WINDOW_POSITION_CACHE.put(
            direction,
            new WindowPosition(
                windowPosition.x,
                windowPosition.y,
                windowPosition.width,
                windowPosition.height,
                false
            )
        );
    }

    private void saveWindowPosition(
        FourDirectionBlockDisplayElement.ColorDirection direction,
        UIElement background,
        boolean displayed
    ) {
        WindowPosition value = new WindowPosition(
            (int) background.getLayoutX(),
            (int) background.getLayoutY(),
            (int) background.getSizeWidth(),
            (int) background.getSizeHeight(),
            displayed
        );
        WINDOW_POSITION_CACHE.put(direction, value);
    }

    private record WindowPosition(
        int x,
        int y,
        int width,
        int height,
        boolean shown
    ) {
    }
}
