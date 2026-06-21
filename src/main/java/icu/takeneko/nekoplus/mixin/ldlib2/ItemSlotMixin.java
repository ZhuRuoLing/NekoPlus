package icu.takeneko.nekoplus.mixin.ldlib2;

import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvent;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemSlot.class)
public class ItemSlotMixin extends UIElement {
    @Inject(
        method = "onHoverTooltips",
        at = @At("HEAD"),
        cancellable = true
    )
    void doNotShowDuplicatedTooltip(UIEvent event, CallbackInfo ci) {
        if (this.getModularUI().ui.rootElement instanceof NPUI<?>) {
            ci.cancel();
        }
    }
}
