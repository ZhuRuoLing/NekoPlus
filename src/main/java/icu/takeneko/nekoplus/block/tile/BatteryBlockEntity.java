package icu.takeneko.nekoplus.block.tile;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.syncdata.field.ManagedFieldHolder;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import icu.takeneko.nekoplus.block.BatteryBlock;
import icu.takeneko.nekoplus.config.NPConfig;
import icu.takeneko.nekoplus.foundation.Tickable;
import icu.takeneko.nekoplus.foundation.block.tile.NPPowerProducer;
import icu.takeneko.nekoplus.foundation.block.tile.NPUIBlock;
import icu.takeneko.nekoplus.foundation.block.tile.NPSynedBlockEntity;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import icu.takeneko.nekoplus.ui.BatteryUI;
import icu.takeneko.nekoplus.util.EnergyFormatUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

@Getter
public class BatteryBlockEntity extends NPSynedBlockEntity implements NPPowerProducer, Tickable, NPUIBlock.Provider {
    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(BatteryBlockEntity.class);

    @Nullable
    @Setter
    private PowerGrid grid;
    @Persisted
    private int dischargingRate = 0;
    private int chargingRate = 0;
    @Persisted
    private int maxDischargingRate = getDefaultMaxDischargingRate();
    @Persisted
    private int maxChargingRate = getMaxChargingRateLimit();
    //kW s
    @Persisted
    private long storedPower = 0;
    @Persisted
    @DescSynced
    private boolean discharging = false;
    @Persisted
    @DescSynced
    private boolean charging = false;

    private long gridStoredPower = 0;
    private long gridCapacity = Long.MIN_VALUE;
    private int gridDischargingRate = 0;
    private int gridChargingRate = 0;

    public BatteryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public int dischargeFromGridRequest(int requestedPower) {
        if (requestedPower <= 0 || storedPower <= 0) {
            clearDischarge();
            this.charging = false;
            this.chargingRate = 0;
            return 0;
        }

        int suppliedPower = Math.clamp(requestedPower, 0, getMaxDischargingRate());
        suppliedPower = (int) Math.min(suppliedPower, storedPower);
        this.dischargingRate = suppliedPower;
        this.discharging = suppliedPower > 0;
        this.charging = false;
        this.chargingRate = 0;
        this.storedPower -= suppliedPower;
        return suppliedPower;
    }

    public int chargeFromGridBudget(int availableChargingPower, boolean canCharge) {
        if (!canCharge || availableChargingPower <= 0) {
            this.charging = false;
            this.chargingRate = 0;
            return 0;
        }

        long capacityLeft = getCapacity() - storedPower;
        int chargedPower = (int) Math.clamp(
            capacityLeft,
            0L,
            Math.min(availableChargingPower, getMaxChargingRate())
        );
        this.storedPower += chargedPower;
        this.chargingRate = chargedPower;
        this.charging = chargedPower > 0;
        return chargedPower;
    }

    public void clearDischarge() {
        this.dischargingRate = 0;
        this.discharging = false;
    }

    public void setMaxChargingRate(int value) {
        this.maxChargingRate = Math.clamp(value, 0, getMaxChargingRateLimit());
        setChanged();
    }

    public void setMaxDischargingRate(int value) {
        this.maxDischargingRate = Math.clamp(value, 0, getMaxDischargingRateLimit());
        setChanged();
    }

    public long getCapacity() {
        return NPConfig.BATTERY_CAPACITY.getAsLong();
    }

    public long getGridCapacity() {
        return gridCapacity == Long.MIN_VALUE ? getCapacity() : gridCapacity;
    }

    public int getMaxChargingRateLimit() {
        return NPConfig.BATTERY_MAX_CHARGING_RATE.getAsInt();
    }

    public int getDefaultMaxDischargingRate() {
        return NPConfig.BATTERY_MAX_DISCHARGING_RATE_DEFAULT.getAsInt();
    }

    public int getMaxDischargingRateLimit() {
        return NPConfig.BATTERY_MAX_DISCHARGING_RATE_MAX.getAsInt();
    }

    public String getBatteryRemainingTimeText() {
        int rate = discharging ? dischargingRate : (charging ? chargingRate : 0);
        if (rate <= 0) return "--:--";
        long amount = discharging ? storedPower : Math.max(getCapacity() - storedPower, 0L);
        return formatSeconds(amount / rate);
    }

    public String getGridBatteryRemainingTimeText() {
        int rate = gridDischargingRate > 0 ? gridDischargingRate : gridChargingRate;
        if (rate <= 0) return "--:--";
        long amount = gridDischargingRate > 0 ? gridStoredPower : Math.max(getGridCapacity() - gridStoredPower, 0L);
        return formatSeconds(amount / rate);
    }

    public void updateGridBatterySummary(
        long storedPower,
        long capacity,
        int dischargingRate,
        int chargingRate
    ) {
        this.gridStoredPower = storedPower;
        this.gridCapacity = capacity;
        this.gridDischargingRate = dischargingRate;
        this.gridChargingRate = chargingRate;
    }

    public static String formatEnergyNumber(long value) {
        return EnergyFormatUtil.formatStoredEnergyNumber(value);
    }

    public static String formatEnergyUnit(long value) {
        return EnergyFormatUtil.formatStoredEnergyUnit(value);
    }

    private static String formatSeconds(long seconds) {
        long minutes = seconds / 60;
        long rest = seconds % 60;
        return "%d:%02d".formatted(minutes, rest);
    }

    @Override
    public void tick() {
        flushState();
        level.setBlockAndUpdate(
            getBlockPos(),
            level.getBlockState(getBlockPos()).setValue(BatteryBlock.DISCHARGING, discharging)
        );
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public @Nullable Level getCurrentLevel() {
        return getLevel();
    }

    @Override
    public BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    public int getOutputPower() {
        return dischargingRate;
    }

    @Override
    public ModularUI getModularUI(BlockUIMenuType.BlockUIHolder holder) {
        return NPUI.of(new BatteryUI(this), holder);
    }

    @Override
    public void setOverload(boolean value) {
        level.setBlockAndUpdate(getPos(), getBlockState().setValue(BatteryBlock.OVERLOAD, value));
    }

    @Override
    public boolean isOverload() {
        return level.getBlockState(getPos()).getValue(BatteryBlock.OVERLOAD);
    }

    public void chargeDirect(int charged) {
        this.storedPower = Math.clamp(
            storedPower + charged,
            0,
            getCapacity()
        );
    }
}
