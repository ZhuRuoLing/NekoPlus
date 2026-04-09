package icu.takeneko.nekoplus.ui;

import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.DataBindingBuilder;
import com.lowdragmc.lowdraglib2.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.TextWrap;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Button;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Selector;
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.codeeditor.CodeEditor;
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import com.lowdragmc.lowdraglib2.gui.ui.rendering.GUIContext;
import dev.vfyjxf.taffy.style.AlignContent;
import dev.vfyjxf.taffy.style.AlignItems;
import dev.vfyjxf.taffy.style.FlexDirection;
import dev.vfyjxf.taffy.style.TaffyDisplay;
import dev.vfyjxf.taffy.style.TaffyPosition;
import icu.takeneko.nekoplus.block.ProgrammableLogicGateBlock;
import icu.takeneko.nekoplus.block.tile.ProgrammableLogicGateBlockEntity;
import icu.takeneko.nekoplus.block.tile.logic.fpg.PinMode;
import icu.takeneko.nekoplus.block.tile.logic.fpg.PinState;
import icu.takeneko.nekoplus.content.expression.ldlib.ExpLanguageDefinition;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import icu.takeneko.nekoplus.foundation.ui.widgets.FourDirectionBlockDisplayElement;
import icu.takeneko.nekoplus.util.NPUIUtils;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector2f;

import java.util.List;

public class ProgrammableLogicGateUI extends NPUI<ProgrammableLogicGateBlockEntity> {
    public ProgrammableLogicGateUI(ProgrammableLogicGateBlockEntity blockEntity) {
        super(blockEntity, Component.translatable("block.nekoplus.programmable_logic_gate"));
        BlockState state = blockEntity.getBlockState();
        Direction direction = state.getValue(ProgrammableLogicGateBlock.FACING);
        int yRot = (((int) direction.toYRot() + 180) % 360) - (direction.getAxis() == Direction.Axis.X ? -180 : 0);
        UIElement redWindow = createPinWindow(blockEntity.getPinR(), "ui.programmable_logic_gate.red", FourDirectionBlockDisplayElement.ColorDirection.RED.color());
        UIElement greenWindow = createPinWindow(blockEntity.getPinG(), "ui.programmable_logic_gate.green", FourDirectionBlockDisplayElement.ColorDirection.GREEN.color());
        UIElement blueWindow = createPinWindow(blockEntity.getPinB(), "ui.programmable_logic_gate.blue", FourDirectionBlockDisplayElement.ColorDirection.BLUE.color());
        UIElement whiteWindow = createPinWindow(blockEntity.getPinW(), "ui.programmable_logic_gate.white", FourDirectionBlockDisplayElement.ColorDirection.WHITE.color());
        addChildren(
            new FourDirectionBlockDisplayElement()
                .block(state, blockEntity)
                .yRot0(yRot)
                .bindIOState(FourDirectionBlockDisplayElement.ColorDirection.RED, DataBindingBuilder.enumValS2C(PinMode.class,blockEntity.getPinR()::getMode).build())
                .bindIOState(FourDirectionBlockDisplayElement.ColorDirection.GREEN, DataBindingBuilder.enumValS2C(PinMode.class,blockEntity.getPinG()::getMode).build())
                .bindIOState(FourDirectionBlockDisplayElement.ColorDirection.BLUE, DataBindingBuilder.enumValS2C(PinMode.class,blockEntity.getPinB()::getMode).build())
                .bindIOState(FourDirectionBlockDisplayElement.ColorDirection.WHITE, DataBindingBuilder.enumValS2C(PinMode.class,blockEntity.getPinW()::getMode).build())
                .setOnClickListener(FourDirectionBlockDisplayElement.ColorDirection.RED, () -> redWindow.setDisplay(TaffyDisplay.DEFAULT))
                .setOnClickListener(FourDirectionBlockDisplayElement.ColorDirection.GREEN, () -> greenWindow.setDisplay(TaffyDisplay.DEFAULT))
                .setOnClickListener(FourDirectionBlockDisplayElement.ColorDirection.BLUE, () -> blueWindow.setDisplay(TaffyDisplay.DEFAULT))
                .setOnClickListener(FourDirectionBlockDisplayElement.ColorDirection.WHITE, () -> whiteWindow.setDisplay(TaffyDisplay.DEFAULT)),
            new TextElement()
                .setText(Component.translatable("container.inventory"))
                .textStyle(ts -> ts.adaptiveHeight(true)),
            new InventorySlots()
        );
        addChildren(redWindow, greenWindow, blueWindow, whiteWindow);
    }

    private UIElement createPinWindow(PinState state, String name, int color) {
        UIElement background = new UIElement() {
            @Override
            public void drawBackgroundAdditional(GUIContext guiContext) {
                super.drawBackgroundAdditional(guiContext);
                NPUIUtils.drawResizeIcon(guiContext, this, 4);
            }
        };
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
        UIElement root = new UIElement();
        root.style(s -> s.background(NPGuiResources.UI_BACKGROUND));
        background.addChildren(root);
        root.layout(l -> l.paddingAll(4).minHeightPercent(100).minWidthPercent(100).flexDirection(FlexDirection.COLUMN));

        UIElement titleBar = new UIElement().layout(layout -> {
            layout.flexDirection(FlexDirection.ROW);
            layout.justifyContent(AlignContent.SPACE_BETWEEN);
            layout.alignItems(AlignItems.CENTER);
            layout.gapAll(2);
        });


        NPUIUtils.setDragMove(titleBar, background, null, null);
        NPUIUtils.setBorderResize(background, background, 4, new Vector2f(140, 76), new Vector2f(1000, 1000), null, null, null);

        titleBar.addChildren(
            new TextElement()
                .setText(name, true)
                .textStyle(textStyle -> textStyle.textWrap(TextWrap.HOVER_ROLL)
                    .adaptiveHeight(true)
                    .textShadow(false)
                    .textColor(0x403e53)
                ),
            new Button()
                .noText()
                .buttonStyle(s -> s.baseTexture(NPGuiResources.UI_BACKGROUND)
                    .hoverTexture(NPGuiResources.BUTTON_HOVERED)
                    .pressedTexture(NPGuiResources.BUTTON_PRESSED)
                )
                .addPostIcon(NPGuiResources.CROSS)
                .setOnClick(e -> background.layout(l -> l.display(TaffyDisplay.NONE)))
                .layout(l -> l.width(14).height(14))
        );
        root.addChildren(titleBar);
        CodeEditor codeEditor = new CodeEditor();
        codeEditor.contentView.style(s -> s.background(IGuiTexture.EMPTY));
        UIElement expression = horizontalLayout(
            new TextElement()
                .setText(Component.translatable("ui.programmable_logic_gate.expression")),
            codeEditor
                .setLanguage(ExpLanguageDefinition.INSTANCE)
                .textAreaStyle(ts -> ts.focusOverlay(IGuiTexture.EMPTY))
                .bind(DataBindingBuilder.create(state::getPinExpression, state::setPinExpression).build())
                .layout(l -> l.alignSelf(AlignItems.END).height(20).minHeight(20).flexGrow(1).heightPercent(100).paddingVertical(2).justifySelf(AlignItems.END))
                .style(s -> s.background(NPGuiResources.UI_BACKGROUND))
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
        UIElement pinMode = horizontalLayout(
            new TextElement()
                .setText(Component.translatable("ui.programmable_logic_gate.pin_mode")),
            pinModeSelector
                .selectorStyle(s -> s.showOverlay(false).focusOverlay(IGuiTexture.EMPTY))
                .setCandidates(List.of(PinMode.values()))
                .setOnValueChanged(pm -> expression.setDisplay(pm == PinMode.OUTPUT))
                .bind(DataBindingBuilder.enumVal(PinMode.class, state::getMode, state::setMode).build())
                .layout(l -> l.alignSelf(AlignItems.END).minWidthPercent(50).paddingVertical(2).justifySelf(AlignItems.END))
                .style(s -> s.background(NPGuiResources.UI_BACKGROUND))
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
}
