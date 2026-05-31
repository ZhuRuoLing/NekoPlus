package icu.takeneko.nekoplus.foundation.block.tile.hatch;

import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.content.tile.logic.hatch.EnergyHatchLogic;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.logic.HatchLogic;
import icu.takeneko.nekoplus.content.tile.logic.hatch.ItemHatchLogic;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.apache.commons.lang3.NotImplementedException;
import org.jspecify.annotations.Nullable;

public final class NPHatchTypes {
    public static final HatchType<ResourceHandler<FluidResource>> FLUID = new HatchType<>() {
        @Override
        public HatchLogic<ResourceHandler<FluidResource>> createHatchLogic(HatchLogicHost logicHost, boolean isInput) {
            throw new NotImplementedException();
        }

        @Override
        @Nullable
        public ResourceHandler<FluidResource> getCapability(HatchLogic<ResourceHandler<FluidResource>> logic) {
            return null;
        }

        @Override
        public BlockEntityType<?> getHostType(boolean isInput) {
            return null;
        }

        @Override
        public String getSerializedName() {
            return "fluid";
        }
    };

    public static final HatchType<ResourceHandler<ItemResource>> ITEM = new HatchType<>() {
        @Override
        public HatchLogic<ResourceHandler<ItemResource>> createHatchLogic(HatchLogicHost logicHost, boolean isInput) {
            return new ItemHatchLogic(logicHost, isInput);
        }

        @Override
        @Nullable
        public ResourceHandler<ItemResource> getCapability(HatchLogic<ResourceHandler<ItemResource>> logic) {
            return logic.getCapabilityInstance();
        }

        @Override
        public BlockEntityType<?> getHostType(boolean isInput) {
            return isInput ? NPBlockEntities.ITEM_INPUT_HATCH.get() : NPBlockEntities.ITEM_OUTPUT_HATCH.get();
        }

        @Override
        public String getSerializedName() {
            return "item";
        }
    };

    public static final HatchType<EnergyHandler> ENERGY = new HatchType<>() {
        @Override
        public HatchLogic<EnergyHandler> createHatchLogic(HatchLogicHost logicHost, boolean isInput) {
            return new EnergyHatchLogic();
        }

        @Override
        @Nullable
        public EnergyHandler getCapability(HatchLogic<EnergyHandler> logic) {
            return logic.getCapabilityInstance();
        }

        @Override
        public BlockEntityType<?> getHostType(boolean isInput) {
            return isInput ? null : NPBlockEntities.ENERGY_OUTPUT_HATCH.get();
        }

        @Override
        public String getSerializedName() {
            return "energy";
        }
    };
}
