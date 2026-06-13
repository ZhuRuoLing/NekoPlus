package icu.takeneko.nekoplus.ui;

import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.DataBindingBuilder;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Button;
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextElement;
import dev.vfyjxf.taffy.style.AlignContent;
import dev.vfyjxf.taffy.style.AlignItems;
import dev.vfyjxf.taffy.style.FlexDirection;
import icu.takeneko.nekoplus.block.tile.TestBlockEntity;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import net.minecraft.network.chat.Component;

public class TestUI extends NPUI<TestBlockEntity> {

    public TestUI(TestBlockEntity blockEntity) {
        super(blockEntity, Component.translatable("block.nekoplus.test"));

        var statusBinding = DataBindingBuilder
            .componentS2C(() -> Component.literal(blockEntity.getRecordStatusText()))
            .build();
        TextElement statusText = new TextElement();
        statusBinding.registerListener(statusText::setText);
        statusText
            .setText(Component.literal(blockEntity.getRecordStatusText()))
            .textStyle(style -> style.adaptiveHeight(true).adaptiveWidth(true));
        statusText.addSyncValue(statusBinding.getSyncValue());

        addChildren(
            statusText,
            horizontalLayout(
                new Button()
                    .setText("Start", false)
                    .setOnServerClick(_ -> blockEntity.startRecording())
                    .layout(layout -> layout.width(52).height(16)),
                new Button()
                    .setText("Stop", false)
                    .setOnServerClick(_ -> blockEntity.stopRecording())
                    .layout(layout -> layout.width(52).height(16)),
                new Button()
                    .setText("Export CSV", false)
                    .setOnServerClick(_ -> blockEntity.exportRecordsCsv())
                    .layout(layout -> layout.width(72).height(16))
            ).layout(layout -> layout
                .flexDirection(FlexDirection.ROW)
                .alignItems(AlignItems.CENTER)
                .justifyContent(AlignContent.SPACE_BETWEEN)
                .gapAll(4)
                .widthPercent(100)
            )
        );

        layout(layout -> layout.minWidth(200).gapAll(4));
    }
}
