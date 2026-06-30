package icu.takeneko.nekoplus.mixin.anvilcraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.dubhe.anvilcraft.api.power.IPowerComponent;
import dev.dubhe.anvilcraft.api.power.IPowerConsumer;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import icu.takeneko.nekoplus.block.tile.BatteryBlockEntity;
import icu.takeneko.nekoplus.foundation.block.tile.NPOverclockablePowerConsumer;
import icu.takeneko.nekoplus.foundation.block.tile.NPPowerComponent;
import icu.takeneko.nekoplus.util.OverclockUtil;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mixin(PowerGrid.class)
public class PowerGridMixin {
    @Shadow
    private int consume;

    @Shadow
    private int generate;

    @Shadow
    private boolean changed;

    @Shadow
    @Final
    Set<IPowerConsumer> consumers;

    @Shadow
    @Final
    Set<IPowerComponent> components;

    @Inject(
        method = "checkRemove",
        at = @At("RETURN"),
        cancellable = true
    )
    void myCheckRemove(IPowerComponent component, CallbackInfoReturnable<Boolean> cir) {
        if (component instanceof NPPowerComponent hepc && hepc.isRemoved()) {
            cir.setReturnValue(true);
        }
    }

    @WrapOperation(
        method = "flush",
        at = @At(value = "INVOKE", target = "Ldev/dubhe/anvilcraft/api/power/IPowerConsumer;getInputPower()I")
    )
    int overclockAvg(IPowerConsumer instance, Operation<Integer> original) {
        if (instance instanceof NPOverclockablePowerConsumer overclockable) {
            if (!overclockable.isOverclockable()) {
                return overclockable.getBaseInputPower();
            }
            return 0;
        }
        return original.call(instance);
    }

    @Inject(
        method = "flush",
        at = @At(
            value = "FIELD",
            target = "Ldev/dubhe/anvilcraft/api/power/PowerGrid;consume:I",
            opcode = Opcodes.GETFIELD,
            ordinal = 3
        )
    )
    void calculate(
        CallbackInfoReturnable<Boolean> cir,
        @Local(index = 1) int oldGenerate,
        @Local(index = 2) int oldConsume
    ) {
        List<NPOverclockablePowerConsumer> list = new ArrayList<>();
        for (IPowerConsumer it : consumers) {
            if (it instanceof NPOverclockablePowerConsumer overclockable) {
                list.add(overclockable);
            }
        }

        OverclockUtil.overclock(list, this.generate, this.consume);

        for (NPOverclockablePowerConsumer overclockable : list) {
            if (!overclockable.isOverclockable()) continue;
            this.consume += overclockable.getInputPower();
        }

        if (this.consume != oldConsume || this.generate != oldGenerate) {
            this.changed = true;
        }
    }

    @Inject(
        method = "gridTick",
        at = @At("HEAD")
    )
    void tickBatteriesBeforeComponents(CallbackInfo ci) {
        List<BatteryBlockEntity> batteries = new ArrayList<>();
        for (IPowerComponent component : components) {
            if (component instanceof BatteryBlockEntity battery) {
                batteries.add(battery);
            }
        }
        if (batteries.isEmpty()) return;

        int previousBatteryOutput = 0;
        for (BatteryBlockEntity battery : batteries) {
            previousBatteryOutput += battery.getOutputPower();
        }

        int externalGenerate = this.generate - previousBatteryOutput;
        int dischargeRequest = this.consume - externalGenerate;
        if (dischargeRequest > 0) {
            int remainingRequest = dischargeRequest;
            for (BatteryBlockEntity battery : batteries) {
                int used = battery.dischargeFromGridRequest(remainingRequest);
                remainingRequest = Math.max(remainingRequest - used, 0);
            }
            updateBatterySummaries(batteries);
            return;
        }

        for (BatteryBlockEntity battery : batteries) {
            battery.clearDischarge();
        }

        int availableChargingPower = Math.max(this.generate - this.consume, 0);
        boolean canCharge = previousBatteryOutput == 0;
        for (BatteryBlockEntity battery : batteries) {
            int used = battery.chargeFromGridBudget(availableChargingPower, canCharge);
            availableChargingPower = Math.max(availableChargingPower - used, 0);
        }
        updateBatterySummaries(batteries);
    }

    @Unique
    private void updateBatterySummaries(List<BatteryBlockEntity> batteries) {
        long gridStoredPower = 0;
        long gridCapacity = 0;
        int gridDischargingRate = 0;
        int gridChargingRate = 0;
        for (BatteryBlockEntity battery : batteries) {
            gridStoredPower += battery.getStoredPower();
            gridCapacity += battery.getCapacity();
            gridDischargingRate += battery.getDischargingRate();
            gridChargingRate += battery.getChargingRate();
        }
        for (BatteryBlockEntity battery : batteries) {
            battery.updateGridBatterySummary(
                gridStoredPower,
                gridCapacity,
                gridDischargingRate,
                gridChargingRate
            );
        }
    }

}
