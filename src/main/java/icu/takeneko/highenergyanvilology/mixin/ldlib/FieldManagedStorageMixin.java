package icu.takeneko.highenergyanvilology.mixin.ldlib;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lowdragmc.lowdraglib2.LDLib2;
import com.lowdragmc.lowdraglib2.syncdata.storage.FieldManagedStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FieldManagedStorage.class)
public class FieldManagedStorageMixin {

    @WrapOperation(
        method = "getSyncFields",
        at = @At(value = "INVOKE", target = "Lcom/lowdragmc/lowdraglib2/syncdata/storage/FieldManagedStorage;requireInit()V")
    )
    void overflowWordAround(FieldManagedStorage instance, Operation<Void> original) {
        if (!LDLib2.isClient()) {
            original.call(instance);
        }
    }
}
