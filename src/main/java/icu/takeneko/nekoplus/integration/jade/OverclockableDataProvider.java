package icu.takeneko.nekoplus.integration.jade;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.foundation.block.tile.NPOverclockablePowerConsumer;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;

public class OverclockableDataProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    public static final ResourceLocation ID = NekoPlus.location("overclockable");
    private static final BoxStyle.GradientBorder STYLE = BoxStyle.GradientBorder.TRANSPARENT.clone();
    public static final OverclockableDataProvider INSTANCE = new OverclockableDataProvider();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        CompoundTag serverData = blockAccessor.getServerData();
        boolean ocEnabled = serverData.getBoolean("OCEnabled");
        iTooltip.add(ocEnabled
            ? Component.translatable("tooltip.nekoplus.overclock.enabled")
            : Component.translatable("tooltip.nekoplus.overclock.disabled")
        );
        if (ocEnabled) {
            int ocCurrent = serverData.getInt("OCCurrent");
            int ocMax = serverData.getInt("OCMax");
            if (ocCurrent == 1) {
                ocCurrent = 0;
            }
            iTooltip.add(
                IElementHelper.get().progress(
                    ocCurrent / (float) ocMax,
                    Component.translatable("tooltip.nekoplus.overclock.ratio", ocMax, ocCurrent),
                    IElementHelper.get().progressStyle().color(0x1111ee).textColor(-1),
                    Util.make(STYLE, boxStyle -> {
                        boxStyle.borderColor = new int[]{0xFFE0E0E0, 0xFFE0E0E0, 0xFFE0E0E0, 0xFFE0E0E0};
                        boxStyle.borderWidth = 1.0f;
                        boxStyle.bgColor = 0xFF32CD32;
                    }),
                    true
                )
            );
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (!(blockAccessor.getBlockEntity() instanceof NPOverclockablePowerConsumer powerConsumer)) return;
        compoundTag.putBoolean("OCEnabled", powerConsumer.isOverclockable());
        compoundTag.putInt("OCMax", powerConsumer.maxOverclockRatio());
        compoundTag.putInt("OCCurrent", powerConsumer.currentOverclockRatio());
    }
}
