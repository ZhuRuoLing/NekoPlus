package icu.takeneko.highenergyanvilology.integration.jade;

import icu.takeneko.highenergyanvilology.HEAnvilology;
import icu.takeneko.highenergyanvilology.block.entity.ParticleStabilizerBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class ParticleStabilizerDataProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    public static final ParticleStabilizerDataProvider INSTANCE = new ParticleStabilizerDataProvider();
    public static final ResourceLocation ID = HEAnvilology.location("particle_stabilizer");

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof ParticleStabilizerBlockEntity blockEntity) {
            compoundTag.putInt("Cooldown", blockEntity.getCountdown());
        }
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        CompoundTag serverData = blockAccessor.getServerData();
        if (!serverData.contains("Cooldown")) return;
        int cooldown = serverData.getInt("Cooldown") / 20;
        if (cooldown > 0) {
            iTooltip.add(
                Component.translatable("tooltip.highenergyanvilology.particle_stabilizer.state")
                    .append(
                        Component.translatable("tooltip.highenergyanvilology.particle_stabilizer.cooling")
                            .copy()
                            .withStyle(ChatFormatting.AQUA)
                    )
            );
            iTooltip.add(Component.translatable("tooltip.highenergyanvilology.particle_stabilizer.cooldown", cooldown));
            return;
        }

        iTooltip.add(
            Component.translatable("tooltip.highenergyanvilology.particle_stabilizer.state")
                .append(
                    Component.translatable("tooltip.highenergyanvilology.particle_stabilizer.ready")
                        .copy()
                        .withStyle(ChatFormatting.GREEN)
                )
        );

    }
}
