package icu.takeneko.nekoplus.ui;

import com.lowdragmc.lowdraglib2.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.TextWrap;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Button;
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import com.lowdragmc.lowdraglib2.gui.util.WindowDragHelper;
import dev.vfyjxf.taffy.style.AlignContent;
import dev.vfyjxf.taffy.style.AlignItems;
import dev.vfyjxf.taffy.style.FlexDirection;
import dev.vfyjxf.taffy.style.TaffyDisplay;
import dev.vfyjxf.taffy.style.TaffyPosition;
import icu.takeneko.nekoplus.block.ProgrammableLogicGateBlock;
import icu.takeneko.nekoplus.block.tile.ProgrammableLogicGateBlockEntity;
import icu.takeneko.nekoplus.block.tile.logic.fpg.PinState;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import icu.takeneko.nekoplus.foundation.ui.widgets.FourDirectionBlockDisplayElement;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class ProgrammableLogicGateUI extends NPUI<ProgrammableLogicGateBlockEntity> {
    public ProgrammableLogicGateUI(ProgrammableLogicGateBlockEntity blockEntity) {
        super(blockEntity, Component.translatable("block.nekoplus.programmable_logic_gate"));
        BlockState state = blockEntity.getBlockState();
        int yRot = ((int) state.getValue(ProgrammableLogicGateBlock.FACING).toYRot() + 180) % 360;
        UIElement redWindow = createPinWindow(blockEntity.getPinR(), "ui.programmable_logic_gate.red", FourDirectionBlockDisplayElement.ColorDirection.RED.color());
        UIElement greenWindow = createPinWindow(blockEntity.getPinG(), "ui.programmable_logic_gate.green", FourDirectionBlockDisplayElement.ColorDirection.GREEN.color());
        UIElement blueWindow = createPinWindow(blockEntity.getPinB(), "ui.programmable_logic_gate.blue", FourDirectionBlockDisplayElement.ColorDirection.BLUE.color());
        UIElement whiteWindow = createPinWindow(blockEntity.getPinW(), "ui.programmable_logic_gate.white", FourDirectionBlockDisplayElement.ColorDirection.WHITE.color());
        addChildren(
            new FourDirectionBlockDisplayElement()
                .block(state, blockEntity)
                .yRot0(yRot)
                .setOnClickListener(FourDirectionBlockDisplayElement.ColorDirection.RED, () -> {
                    redWindow.setDisplay(TaffyDisplay.DEFAULT);
                })
                .setOnClickListener(FourDirectionBlockDisplayElement.ColorDirection.GREEN, () -> {
                    greenWindow.setDisplay(TaffyDisplay.DEFAULT);
                })
                .setOnClickListener(FourDirectionBlockDisplayElement.ColorDirection.BLUE, () -> {
                    blueWindow.setDisplay(TaffyDisplay.DEFAULT);
                })
                .setOnClickListener(FourDirectionBlockDisplayElement.ColorDirection.WHITE, () -> {
                    whiteWindow.setDisplay(TaffyDisplay.DEFAULT);
                }),
            new TextElement()
                .setText(Component.translatable("container.inventory"))
                .textStyle(ts -> ts.adaptiveHeight(true)),
            new InventorySlots()
        );
        addChildren(redWindow, greenWindow, blueWindow, whiteWindow);
    }

    private UIElement createPinWindow(PinState state, String name, int color) {
        UIElement background = new UIElement();
        background.style(s -> s.backgroundTexture(new ColorRectTexture(color | 0xff000000)));
        background.layout(l -> l
            .positionType(TaffyPosition.ABSOLUTE)
            .display(TaffyDisplay.NONE)
            .left(6)
            .top(6)
            .minWidth(140)
        );
        UIElement root = new UIElement();
        root.style(s -> s.background(NPGuiResources.UI_BACKGROUND));
        background.addChildren(root);
        root.layout(l -> l.marginAll(1).paddingAll(3));

        UIElement titleBar = new UIElement().layout(layout -> {
            layout.flexDirection(FlexDirection.ROW);
            layout.justifyContent(AlignContent.SPACE_BETWEEN);
            layout.alignItems(AlignItems.CENTER);
            layout.gapAll(2);
        });

        WindowDragHelper.setDragMove(titleBar, background, null, null);

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
                .setOnClick(e -> background.layout(l -> l.display(TaffyDisplay.NONE)))
                .layout(l -> l.width(12).height(12).paddingAll(1))
        );
        root.addChildren(titleBar);

        return background;
    }
}
