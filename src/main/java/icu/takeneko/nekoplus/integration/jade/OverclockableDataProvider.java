package icu.takeneko.nekoplus.integration.jade;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.foundation.block.tile.NPOverclockablePowerConsumer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.JadeUI;
import snownee.jade.api.view.ProgressView;

public class OverclockableDataProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    public static final Identifier ID = NekoPlus.location("overclockable");
    private static final BoxStyle STYLE = BoxStyle.transparent();
    public static final OverclockableDataProvider INSTANCE = new OverclockableDataProvider();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        CompoundTag serverData = blockAccessor.getServerData();
        if (!serverData.contains("OCEnabled")) return;
        boolean ocEnabled = serverData.getBooleanOr("oc_enabled", false);
        boolean inOCState = serverData.getBooleanOr("is_overclocking", false);
        iTooltip.add(ocEnabled
            ? Component.translatable("tooltip.nekoplus.overclock.enabled")
            : Component.translatable("tooltip.nekoplus.overclock.disabled")
        );
        if (ocEnabled) {
            int ocCurrent = serverData.getIntOr("overclock_current_ratio", 1);
            int ocMax = serverData.getIntOr("overclock_max_ratio", 1);
            if (ocCurrent == 1 && !inOCState) {
                ocCurrent = 0;
            }
            Component.translatable("tooltip.nekoplus.overclock.ratio", ocCurrent, ocMax);
            float progress = Mth.clamp(ocCurrent / (float) ocMax, 0, 1);
            iTooltip.add(
                JadeUI.progress(
                    new ProgressView(
                        ProgressView.Part.of(
                            progress,
                            JadeUI.horizontalTiledSprite(
                                RenderPipelines.GUI_TEXTURED,
                                NekoPlus.location("energy_progress"),
                                16,
                                16
                            )
                        ),
                        Component.translatable("tooltip.nekoplus.overclock.ratio", ocCurrent, ocMax),
                        JadeUI.progressStyle(),
                        BoxStyle.nestedBox()
                    )
                )
            );
        }
    }

    @Override
    public Identifier getUid() {
        return ID;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (!(blockAccessor.getBlockEntity() instanceof NPOverclockablePowerConsumer powerConsumer)) return;
        compoundTag.putBoolean("OCEnabled", powerConsumer.isOverclockEnabled());
        compoundTag.putBoolean("InOCState", powerConsumer.isOverclockable());
        compoundTag.putInt("OCMax", powerConsumer.maxOverclockRatio());
        compoundTag.putInt("OCCurrent", powerConsumer.isOverclockable() ? powerConsumer.currentOverclockRatio() : 0);
    }
}
