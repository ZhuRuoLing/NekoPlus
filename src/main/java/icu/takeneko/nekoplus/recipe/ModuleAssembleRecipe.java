package icu.takeneko.nekoplus.recipe;

import com.mojang.serialization.MapCodec;
import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.all.NPDataComponents;
import icu.takeneko.nekoplus.foundation.item.module.NPEnhancementModule;
import icu.takeneko.nekoplus.foundation.item.module.type.NPEnhancementModuleType;
import icu.takeneko.nekoplus.item.EnhancementModuleItem;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

public class ModuleAssembleRecipe extends CustomRecipe {
    public static final Identifier NAME = NekoPlus.location("crafting_module_assemble");
    public static final ModuleAssembleRecipe INSTANCE = new ModuleAssembleRecipe();

    public static final RecipeSerializer<ModuleAssembleRecipe> SERIALIZER = new RecipeSerializer<>(
        MapCodec.unit(INSTANCE),
        StreamCodec.unit(INSTANCE)
    );

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int moduleCount = 0;
        int equipmentCount = 0;
        List<ItemStack> modules = new ArrayList<>();
        ItemStack equipment = null;
        for (ItemStack item : input.items()) {
            if (item.is(Tags.Items.TOOLS) || item.is(Tags.Items.ARMORS) || item.is(Items.ELYTRA)) {
                equipmentCount++;
                equipment = item;
            }
        }
        if (equipmentCount != 1) return false;
        if (!equipment.has(NPDataComponents.ENHANCEMENT_MODULE)) return false;

        List<NPEnhancementModuleType<?>> moduleTypes = new ArrayList<>(
            equipment.get(NPDataComponents.ENHANCEMENT_MODULE)
                .stream()
                .map(NPEnhancementModule::getType)
                .toList()
        );

        for (ItemStack item : input.items()) {
            if (item.getItem() instanceof EnhancementModuleItem<?> moduleItem) {
                moduleCount++;
                modules.add(item);
                NPEnhancementModuleType<?> moduleType = moduleItem.getModuleType();
                long count = moduleTypes.stream()
                    .filter(it -> it == moduleType)
                    .count();
                if ((count + 1) > moduleType.installationLimit()) {
                    return false;
                } else {
                    moduleTypes.add(moduleType);
                }
            }
        }

        if (moduleCount == 0) {
            return false;
        }
        for (ItemStack module : modules) {
            if (module.getItem() instanceof EnhancementModuleItem<?> moduleItem) {
                if (!moduleItem.getModuleType().appliesTo(equipment)) {
                    return false;
                }
                continue;
            }
            return false;
        }
        return true;
    }


    @Override
    public ItemStack assemble(CraftingInput input) {
        List<ItemStack> modules = new ArrayList<>();
        ItemStack equipment = null;
        for (ItemStack item : input.items()) {
            if (item.getItem() instanceof EnhancementModuleItem<?>) {
                modules.add(item);
            }
            if (item.is(Tags.Items.TOOLS) || item.is(Tags.Items.ARMORS) || item.is(Items.ELYTRA)) {
                equipment = item.copy();
            }
        }
        if (equipment == null || !equipment.has(NPDataComponents.ENHANCEMENT_MODULE)) {
            return ItemStack.EMPTY;
        }
        List<NPEnhancementModule> moduleList = new ArrayList<>(equipment.get(NPDataComponents.ENHANCEMENT_MODULE));
        List<NPEnhancementModuleType<?>> moduleTypes = new ArrayList<>();
        for (ItemStack module : modules) {
            if (module.getItem() instanceof EnhancementModuleItem<?> moduleItem) {
                NPEnhancementModuleType<?> moduleType = moduleItem.getModuleType();
                long count = moduleTypes.stream()
                    .filter(it -> it == moduleType)
                    .count();
                if (!moduleItem.getModuleType().appliesTo(equipment) || (count + 1) > moduleType.installationLimit()) {
                    return ItemStack.EMPTY;
                }
                moduleTypes.add(moduleType);
                moduleList.add(moduleItem.getModuleType().factory().create());
                continue;
            }
            return ItemStack.EMPTY;
        }
        equipment.set(NPDataComponents.ENHANCEMENT_MODULE, moduleList);
        return equipment;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return SERIALIZER;
    }
}
