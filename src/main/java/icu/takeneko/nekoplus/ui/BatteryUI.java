package icu.takeneko.nekoplus.ui;

import com.lowdragmc.lowdraglib2.gui.sync.bindings.IBinding;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.DataBindingBuilder;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextElement;
import com.lowdragmc.lowdraglib2.gui.ui.event.HoverTooltips;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents;
import com.lowdragmc.lowdraglib2.gui.ui.style.BasicStyle;
import com.lowdragmc.lowdraglib2.gui.ui.style.LayoutStyle;
import dev.vfyjxf.taffy.style.AlignItems;
import icu.takeneko.nekoplus.block.tile.BatteryBlockEntity;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import net.minecraft.network.chat.Component;

import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

public class BatteryUI extends NPUI<BatteryBlockEntity> {
    private static final int TEXT = 0xFF3e3e3e;
    private static final int STRONG = 0xFF1F1F1F;
    private static final int CHARGING_GREEN = 0xFF2FBF71;
    private static final int DISCHARGING_BLUE = 0xFF35B8FF;
    private static final int STORED_ORANGE = 0xFFF59E0B;
    private static final int CAPACITY_CYAN = 0xFF17B8C7;
    private static final int UNIT_LIGHT = 0xFFD7F2EF;
    private static final int TIME_PURPLE = 0xFFD977FF;

    private static final int NORMAL_CHARGING_STEP = 100;
    private static final int SHIFT_CHARGING_STEP = 1000;

    public BatteryUI(BatteryBlockEntity blockEntity) {
        super(blockEntity, Component.translatable("block.nekoplus.battery"));

        addChild(
            new TextElement()
                .setText(title)
                .textStyle(ts -> ts.adaptiveHeight(true))
                .layout(it -> it.paddingBottom(4))
        );

        addChildren(
            uiRow(
                text("ui.battery.status", TEXT),
                boundText(
                    this::batteryStatus,
                    () -> blockEntity.isDischarging()
                        ? DISCHARGING_BLUE
                        : (blockEntity.isCharging() ? CHARGING_GREEN : STRONG)
                )
            ),
            uiRow(
                text("ui.battery.max_charging_rate", TEXT),
                wheelAdjustedChargingPowerValue()
            ),
            uiRow(
                text("ui.battery.remaining_time", TEXT),
                boundText(() -> Component.literal(blockEntity.getBatteryRemainingTimeText()), TIME_PURPLE)
            ),
            capacityRow("ui.battery.capacity", blockEntity::getStoredPower, blockEntity::getCapacity),
            uiRow(
                text("ui.battery.current_discharging_rate", TEXT),
                boundText(() -> Component.literal(Integer.toString(blockEntity.getDischargingRate())), DISCHARGING_BLUE)
            ),
            uiRow(
                text("ui.battery.max_discharging_rate", TEXT),
                boundText(() -> Component.literal(Integer.toString(blockEntity.getMaxDischargingRate())), STRONG)
            ),
            gridPanel()
        );

        layout(this::configureLayout);
    }

    private Label wheelAdjustedChargingPowerValue() {
        Label label = boundText(
            () -> Component.literal(Integer.toString(blockEntity.getMaxChargingRate())),
            CHARGING_GREEN
        );
        label.layout(layout -> layout.minWidth(34));
        label.addEventListener(
            UIEvents.HOVER_TOOLTIPS,
            event -> event.hoverTooltips = HoverTooltips.create(
                Component.translatable("tooltip.nekoplus.battery.max_charging_rate")
            )
        );
        label.addServerEventListener(
            UIEvents.MOUSE_WHEEL, event -> {
                int step = event.isShiftDown() ? SHIFT_CHARGING_STEP : NORMAL_CHARGING_STEP;
                int direction = event.deltaY > 0 ? 1 : -1;
                blockEntity.setMaxChargingRate(blockEntity.getMaxChargingRate() + direction * step);
                event.stopPropagation();
            }
        );
        return label;
    }

    private UIElement capacityRow(String label, LongSupplier stored, LongSupplier capacity) {
        return uiRow(
            text(label, TEXT),
            boundText(
                () -> Component.literal(BatteryBlockEntity.formatEnergyNumber(stored.getAsLong())),
                STORED_ORANGE
            ),
            boundText(() -> Component.literal(BatteryBlockEntity.formatEnergyUnit(stored.getAsLong())), UNIT_LIGHT),
            text("/", UNIT_LIGHT),
            boundText(
                () -> Component.literal(BatteryBlockEntity.formatEnergyNumber(capacity.getAsLong())),
                CAPACITY_CYAN
            ),
            boundText(() -> Component.literal(BatteryBlockEntity.formatEnergyUnit(capacity.getAsLong())), UNIT_LIGHT)
        );
    }

    private UIElement gridPanel() {
        return div(
            capacityRow("ui.battery.grid_capacity", blockEntity::getGridStoredPower, blockEntity::getGridCapacity),
            uiRow(
                text("ui.battery.grid_remaining_time", TEXT),
                boundText(() -> Component.literal(blockEntity.getGridBatteryRemainingTimeText()), TIME_PURPLE)
            )
        ).layout(layout -> layout
            .paddingAll(4)
            .gapAll(1)
        ).style(s ->
            s.background(NPGuiResources.UI_BACKGROUND)
        );
    }

    private Component batteryStatus() {
        if (blockEntity.isDischarging()) return Component.translatable("ui.battery.status.discharging");
        if (blockEntity.isCharging()) return Component.translatable("ui.battery.status.charging");
        return Component.translatable("ui.battery.status.idle");
    }

    private Label text(String translationKey, int color) {
        return text(Component.translatable(translationKey), color);
    }

    private Label text(Component component, int color) {
        Label label = new Label();
        label
            .setText(component)
            .textStyle(style -> style
                .textColor(color)
                .adaptiveHeight(true)
                .textShadow(false)
                .adaptiveWidth(true)
            );
        label.layout(layout -> layout.minHeight(11));
        return label;
    }

    private Label boundText(Supplier<Component> supplier, int color) {
        return boundText(supplier, () -> color);
    }

    private Label boundText(Supplier<Component> supplier, IntSupplier colorSupplier) {
        Label label = text(supplier.get(), colorSupplier.getAsInt());
        IBinding<Component> binding = DataBindingBuilder.componentS2C(supplier).build();
        label.bind(binding);
        label.addEventListener(UIEvents.TICK, _ -> label.textStyle(style -> style.textColor(colorSupplier.getAsInt())));
        return label;
    }

    private static UIElement uiRow(UIElement... children) {
        return NPUI.row(children).layout(layout -> layout
            .alignItems(AlignItems.CENTER)
            .gapAll(1)
            .minHeight(13)
        );
    }

    private void configureLayout(LayoutStyle layout) {
        layout.paddingAll(8)
            .gapAll(1);
    }
}
