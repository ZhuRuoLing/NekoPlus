package icu.takeneko.nekoplus.data.provider;

import dev.dubhe.anvilcraft.AnvilCraft;
import icu.takeneko.nekoplus.NekoPlus;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Optional;

public class NPTradeSets{
    public static final ResourceKey<TradeSet> JEWELER_2 = key("jeweler/level_2");

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static void bootstrap(BootstrapContext<TradeSet> context) {
        HolderLookup<VillagerTrade> lookup = context.holderLookup(Registries.VILLAGER_TRADE).get();
        context.register(
            JEWELER_2,
            new TradeSet(
                HolderSet.direct(lookup.getOrThrow(NPVillagerTrades.MODULAR_ENHANCEMENT_TEMPLATE)),
                ConstantValue.exactly(2),
                false,
                Optional.of(NekoPlus.location("trade_set/jeweler/level_2"))
            )
        );
    }

    private static ResourceKey<TradeSet> key(String s) {
        return ResourceKey.create(Registries.TRADE_SET, NekoPlus.location(s));
    }

    private static ResourceKey<TradeSet> ancKey(String s) {
        return ResourceKey.create(Registries.TRADE_SET, AnvilCraft.of(s));
    }
}
