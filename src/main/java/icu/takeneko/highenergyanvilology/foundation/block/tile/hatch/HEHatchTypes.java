package icu.takeneko.highenergyanvilology.foundation.block.tile.hatch;

import icu.takeneko.highenergyanvilology.all.HEBlockEntities;
import icu.takeneko.highenergyanvilology.block.tile.logic.hatch.EnergyHatchLogic;
import icu.takeneko.highenergyanvilology.foundation.block.tile.hatch.logic.HatchLogic;
import icu.takeneko.highenergyanvilology.block.tile.logic.hatch.ItemHatchLogic;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public final class HEHatchTypes {
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

    public static final HatchType<IItemHandler> ITEM = new HatchType<>() {
        @Override
        public HatchLogic<IItemHandler> createHatchLogic(HatchLogicHost logicHost, boolean isInput) {
            return new ItemHatchLogic(logicHost, isInput);
        }

        @Override
        @Nullable
        public IItemHandler getCapability(HatchLogic<IItemHandler> logic) {
            return logic.getCapabilityInstance();
        }

        @Override
        public BlockEntityType<?> getHostType(boolean isInput) {
            return isInput ? HEBlockEntities.ITEM_INPUT_HATCH.get() : HEBlockEntities.ITEM_OUTPUT_HATCH.get();
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
            return isInput ? null : HEBlockEntities.ENERGY_OUTPUT_HATCH.get();
        }

        @Override
        public String getSerializedName() {
            return "energy";
        }
    };
}
