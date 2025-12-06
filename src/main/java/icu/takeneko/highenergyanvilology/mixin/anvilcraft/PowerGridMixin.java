package icu.takeneko.highenergyanvilology.mixin.anvilcraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.dubhe.anvilcraft.api.power.IPowerConsumer;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import icu.takeneko.highenergyanvilology.foundation.block.entity.HEOverclockablePowerConsumer;
import icu.takeneko.highenergyanvilology.util.OverclockUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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

    @WrapOperation(
        method = "flush",
        at = @At(value = "INVOKE", target = "Ldev/dubhe/anvilcraft/api/power/IPowerConsumer;getInputPower()I")
    )
    int overclockAvg(IPowerConsumer instance, Operation<Integer> original) {
        if (instance instanceof HEOverclockablePowerConsumer overclockable) {
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
            value = "TAIL"
        )
    )
    void calculate(
        CallbackInfoReturnable<Boolean> cir,
        @Local(index = 1) int oldConsume,
        @Local(index = 2) int oldGenerate
    ) {
        List<HEOverclockablePowerConsumer> list = new ArrayList<>();
        for (IPowerConsumer it : consumers) {
            if (it instanceof HEOverclockablePowerConsumer overclockable) {
                list.add(overclockable);
            }
        }

        OverclockUtil.overclock(list, this.generate, this.consume);

        for (HEOverclockablePowerConsumer overclockable : list) {
            if (!overclockable.isOverclockable()) continue;
            this.consume += overclockable.getInputPower();
        }

        if (this.consume != oldConsume || this.generate != oldGenerate) {
            this.changed = true;
        }
    }


}
