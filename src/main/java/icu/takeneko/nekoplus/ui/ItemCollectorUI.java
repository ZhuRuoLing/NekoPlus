package icu.takeneko.nekoplus.ui;

import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.DataBindingBuilder;
import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.Horizontal;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Button;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Toggle;
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import com.lowdragmc.lowdraglib2.gui.ui.event.HoverTooltips;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents;
import com.lowdragmc.lowdraglib2.gui.ui.style.BasicStyle;
import com.lowdragmc.lowdraglib2.gui.ui.style.LayoutStyle;
import dev.dubhe.anvilcraft.block.entity.ItemCollectorBlockEntity;
import dev.vfyjxf.taffy.style.AlignItems;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import icu.takeneko.nekoplus.foundation.ui.widgets.AncFilteredItemSlot;
import icu.takeneko.nekoplus.internal.ItemCollectorBlockEntityInternals;
import icu.takeneko.nekoplus.util.NPUIUtils;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class ItemCollectorUI extends NPUI<ItemCollectorBlockEntity> {
    public ItemCollectorUI(ItemCollectorBlockEntity blockEntity) {
        super(
            blockEntity,
            Component.translatable("block.anvilcraft.item_collector")
        );

        addChild(
            new TextElement()
                .setText(title)
                .textStyle(ts -> ts.adaptiveHeight(true))
                .layout(it -> it.paddingLeft(5))
        );

        InventorySlots inventorySlots = new InventorySlots();
        inventorySlots.layout(it -> it
            .paddingTop(7)
            .paddingHorizontal(5)
        );
        inventorySlots.hotbar.layout(it -> it.marginTop(4));

        addChildren(
            row(
                createSettingsPane(blockEntity),
                createFilteredSlots()
            ).layout(it -> it
                .paddingTop(2)
                .paddingBottom(3)
            ),
            inventorySlots
        );

        layout(this::configureLayout);
        style(this::configureStyle);
    }

    private UIElement createFilteredSlots() {
        return div(
            row(
                filteredSlot(0),
                filteredSlot(1),
                filteredSlot(2)
            ),
            row(
                filteredSlot(3),
                filteredSlot(4),
                filteredSlot(5)
            ),
            row(
                filteredSlot(6),
                filteredSlot(7),
                filteredSlot(8)
            )
        ).layout(it -> it
            .paddingLeft(5)
            .paddingTop(4)
        );
    }

    private AncFilteredItemSlot filteredSlot(int index) {
        AncFilteredItemSlot slot = new AncFilteredItemSlot();
        slot.bind(blockEntity.getItemHandler(), index);
        slot.style(it -> it
            .background(IGuiTexture.EMPTY)
        );
        slot.layout(it ->
            it.paddingAll(1)
        );
        // NPUIUtils.ghostIngredient(slot);
        return slot;
    }

    private @NonNull UIElement createSettingsPane(ItemCollectorBlockEntity blockEntity) {
        Toggle toggle = filterToggle();
        toggle.bind(
            DataBindingBuilder.bool(
                blockEntity::isFilterEnabled,
                blockEntity::setFilterEnabled
            ).build()
        ).addEventListener(
            UIEvents.HOVER_TOOLTIPS,
            event -> {
                event.hoverTooltips = HoverTooltips.create(
                    Component.translatable(
                        "screen.anvilcraft.button.record",
                        Component.translatable(
                            "screen.anvilcraft.button." + (toggle.isOn() ? "on" : "off")
                        )
                    )
                );
            }
        );
        return div(
            // range
            row(
                minusButton().setOnServerClick(_ -> blockEntity.getRangeRadius().previous()),
                text().bind(
                    DataBindingBuilder.componentS2C(() ->
                        Component.literal(blockEntity.getRangeRadius().get().toString())
                    ).build()
                ),
                plusButton().setOnServerClick(_ -> blockEntity.getRangeRadius().next())
            ).layout(it -> it.marginBottom(2).alignItems(AlignItems.CENTER)),
            // cooldown
            row(
                minusButton().setOnServerClick(_ -> blockEntity.getCooldown().previous()),
                text().bind(
                    DataBindingBuilder.componentS2C(() ->
                        Component.literal(blockEntity.getCooldown().get().toString())
                    ).build()
                ),
                plusButton().setOnServerClick(_ -> blockEntity.getCooldown().next())
            ).layout(it -> it.marginBottom(2).alignItems(AlignItems.CENTER)),
            // power & filter
            row(
                text().bind(
                    DataBindingBuilder.componentS2C(() ->
                        Component.literal(Integer.toString(blockEntity.getInputPower()))
                    ).build()
                ),
                toggle
            ).layout(it -> it.marginBottom(2))
        ).layout(it -> it
            .marginTop(9)
            .marginLeft(40)
            .maxWidth(50)
        );
    }

    private Toggle filterToggle() {
        Toggle toggle = new Toggle()
            .toggleStyle(ts -> ts
                .baseTexture(NPGuiResources.BUTTON_DARK)
                .hoverTexture(NPGuiResources.BUTTON_DARK_HOVERED)
                .markTexture(NPGuiResources.FILTER_ON)
                .unmarkTexture(NPGuiResources.FILTER_OFF)
            )
            .setText("");
        toggle.noText();
        toggle.layout(it -> it.minWidth(18).minHeight(18));
        toggle.layout(it -> it.marginTop(3).marginLeft(6));
        return toggle;
    }

    private Button button(IGuiTexture base, IGuiTexture hovered, int size, int margin) {
        Button button = new Button();
        button.buttonStyle(it -> it
            .hoverTexture(hovered)
            .pressedTexture(hovered)
            .baseTexture(base)
        );
        button.layout(it -> it
            .minWidth(size)
            .minHeight(size)
            .maxWidth(size)
            .maxHeight(size)
            .marginAll(margin)
        );
        button.text.setText("");
        button.text.layout(it -> it.maxHeight(0).maxWidth(0));
        return button;
    }

    private Label text() {
        Label textElement = new Label();
        textElement.textStyle(ts -> ts
            .textAlignHorizontal(Horizontal.CENTER)
            .textColor(-1)
            .textShadow(true)
            .adaptiveHeight()
        );
        textElement.layout(it -> it
            .minWidth(26)
            .minHeight(10)
            .paddingTop(1)
            .paddingHorizontal(2)
        );
        return textElement;
    }

    private Button plusButton() {
        return button(
            NPGuiResources.ADD_BUTTON,
            NPGuiResources.ADD_BUTTON_HOVERED,
            10,
            1
        );
    }

    private Button minusButton() {
        return button(
            NPGuiResources.MINUS_BUTTON,
            NPGuiResources.MINUS_BUTTON_HOVERED,
            10,
            1
        );
    }

    private void configureStyle(BasicStyle style) {
        style.background(NPGuiResources.ITEM_COLLECTOR_BACKGROUND);
    }

    private void configureLayout(LayoutStyle layout) {
        layout.minHeight(166);
        layout.minWidth(176);
    }

}
