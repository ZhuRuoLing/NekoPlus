package icu.takeneko.nekoplus.foundation.block.tile.hatch;

import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.content.tile.logic.hatch.EnergyHatchLogic;
import icu.takeneko.nekoplus.foundation.block.tile.hatch.logic.HatchLogic;
import icu.takeneko.nekoplus.content.tile.logic.hatch.ItemHatchLogic;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.transfer.item.ItemStackResourceHandler;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public final class NPHatchTypes {
    public static final HatchType<IFluidHandler> FLUID = new HatchType<>() {
        @Override
        public HatchLogic<IFluidHandler> createHatchLogic(HatchLogicHost logicHost, boolean isInput) {
            throw new NotImplementedException();
        }

        @Override
        @Nullable
        public IFluidHandler getCapability(HatchLogic<IFluidHandler> logic) {
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

    public static final HatchType<ItemStackResourceHandler> ITEM = new HatchType<>() {
        @Override
        public HatchLogic<ItemStackResourceHandler> createHatchLogic(HatchLogicHost logicHost, boolean isInput) {
            return new ItemHatchLogic(logicHost, isInput);
        }

        @Override
        @Nullable
        public ItemStackResourceHandler getCapability(HatchLogic<ItemStackResourceHandler> logic) {
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

    public static final HatchType<IEnergyStorage> ENERGY = new HatchType<>() {
        @Override
        public HatchLogic<IEnergyStorage> createHatchLogic(HatchLogicHost logicHost, boolean isInput) {
            return new EnergyHatchLogic();
        }

        @Override
        @Nullable
        public IEnergyStorage getCapability(HatchLogic<IEnergyStorage> logic) {
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
