package icu.takeneko.nekoplus.integration.jade.client;

import icu.takeneko.nekoplus.integration.jade.ParticleStabilizerDataProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class ParticleStabilizerComponentProvider implements IBlockComponentProvider {
    public static final ParticleStabilizerComponentProvider INSTANCE = new ParticleStabilizerComponentProvider();

    @Override
    public Identifier getUid() {
        return ParticleStabilizerDataProvider.ID;
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        CompoundTag serverData = blockAccessor.getServerData();
        if (!serverData.contains("Cooldown")) return;
        int cooldown = serverData.getIntOr("Cooldown", 0) / 20;
        if (cooldown > 0) {
            iTooltip.add(
                Component.translatable("tooltip.nekoplus.particle_stabilizer.state")
                    .append(
                        Component.translatable("tooltip.nekoplus.particle_stabilizer.cooling")
                            .copy()
                            .withStyle(ChatFormatting.AQUA)
                    )
            );
            iTooltip.add(Component.translatable("tooltip.nekoplus.particle_stabilizer.cooldown", cooldown));
            return;
        }

        iTooltip.add(
            Component.translatable("tooltip.nekoplus.particle_stabilizer.state")
                .append(
                    Component.translatable("tooltip.nekoplus.particle_stabilizer.ready")
                        .copy()
                        .withStyle(ChatFormatting.GREEN)
                )
        );

    }
}
