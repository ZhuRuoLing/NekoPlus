package icu.takeneko.nekoplus.data.provider;

import dev.dubhe.anvilcraft.init.item.ModItems;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.all.NPItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class NPLootTablesProvider extends NPDataProvider<LootTable> {
    public static final ResourceKey<LootTable> WORKSHOP_RUIN = key("workshop_ruin");

    public NPLootTablesProvider(
        PackOutput output,
        CompletableFuture<HolderLookup.Provider> lookupProvider
    ) {
        super(
            output,
            LootTable.DIRECT_CODEC,
            "",
            "loot_table",
            true,
            lookupProvider
        );
    }

    private static ResourceKey<LootTable> key(String location) {
        return ResourceKey.create(Registries.LOOT_TABLE, NekoPlus.location(location));
    }

    @Override
    protected void addEntries(HolderLookup.Provider registries) {
        add(
            WORKSHOP_RUIN.identifier(),
            LootTable.lootTable()
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(3.0F))
                        .add(LootItem.lootTableItem(Items.ANVIL)
                            .setWeight(40)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                        )
                        .add(LootItem.lootTableItem(ModItems.MAGNET_INGOT)
                            .setWeight(40)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 32.0F)))
                        )
                        .add(LootItem.lootTableItem(NPItems.ADVANCED_PROCESSOR)
                            .setWeight(40)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 32.0F)))
                        )
                        .add(LootItem.lootTableItem(NPItems.MODULAR_ENHANCEMENT_TEMPLATE)
                            .setWeight(10)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F)))
                        )
                )
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(EmptyLootItem.emptyItem().setWeight(5))
                        .add(LootItem.lootTableItem(NPBlocks.TITANIUM_ALLOY_BLOCK)
                            .setWeight(5)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F)))
                        )
                        .add(LootItem.lootTableItem(NPItems.ANTI_GRAVITY_MODULE)
                            .setWeight(2)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                        )
                ).withPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(EmptyLootItem.emptyItem().setWeight(80))
                        .add(LootItem.lootTableItem(ModItems.AMETHYST_AXE)
                            .setWeight(8)
                        )
                        .add(LootItem.lootTableItem(ModItems.AMETHYST_PICKAXE)
                            .setWeight(8)
                        )
                        .add(LootItem.lootTableItem(ModItems.AMETHYST_SWORD)
                            .setWeight(8)
                        )
                        .add(LootItem.lootTableItem(ModItems.EMBER_METAL_PICKAXE)
                            .setWeight(4)
                        )
                        .add(LootItem.lootTableItem(ModItems.EMBER_METAL_AXE)
                            .setWeight(3)
                        )
                        .add(LootItem.lootTableItem(ModItems.FROST_METAL_PICKAXE)
                            .setWeight(3)
                        )
                )
                .build()
        );
    }

    @Override
    public @NonNull String getName() {
        return "NekoPLUS! LootTables";
    }
}
