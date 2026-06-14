package icu.takeneko.nekoplus.data.provider;

import dev.dubhe.anvilcraft.init.item.ModItems;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.List;
import java.util.Optional;

public class NPVillagerTrades {

    public static final ResourceKey<VillagerTrade> MODULAR_ENHANCEMENT_TEMPLATE = key("jeweler/emerald_for_modular_enhancement_template");

    public static void bootstrap(BootstrapContext<VillagerTrade> context) {
        context.register(
            MODULAR_ENHANCEMENT_TEMPLATE,
            new VillagerTrade(
                new TradeCost(
                    Items.EMERALD,
                    4
                ),
                Optional.of(
                    new TradeCost(
                        ModItems.PROCESSOR,
                        8
                    )
                ),
                new ItemStackTemplate(NPItems.MODULAR_ENHANCEMENT_TEMPLATE, 4),
                16,
                10,
                0.5f,
                Optional.empty(),
                List.of(),
                Optional.empty()
            )
        );
    }

    private static ResourceKey<VillagerTrade> key(String s) {
        return ResourceKey.create(Registries.VILLAGER_TRADE, NekoPlus.location(s));
    }
}
