package icu.takeneko.nekoplus.all;

import icu.takeneko.nekoplus.NekoPlus;
import icu.takeneko.nekoplus.foundation.item.module.impl.AntiGravityModule;
import icu.takeneko.nekoplus.foundation.item.module.impl.ExoskeletalLegFrameModule;
import icu.takeneko.nekoplus.foundation.item.module.impl.MechanicalHeartModule;
import icu.takeneko.nekoplus.foundation.item.module.impl.TitaniumCrystalModule;
import icu.takeneko.nekoplus.foundation.item.module.type.NPEnhancementModuleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NPEnhancementModules {
    public static final DeferredRegister<NPEnhancementModuleType<?>> DR = DeferredRegister.create(
        NPRegistries.ENHANCEMENT_MODULE_TYPE_KEY,
        NekoPlus.MODID
    );

    public static final DeferredHolder<NPEnhancementModuleType<?>, NPEnhancementModuleType<?>> ANTI_GRAVITY =
        DR.register(
            AntiGravityModule.NAME.getPath(),
            () -> AntiGravityModule.TYPE
        );

    public static final DeferredHolder<NPEnhancementModuleType<?>, NPEnhancementModuleType<?>> TITANIUM_CRYSTAL =
        DR.register(
            TitaniumCrystalModule.NAME.getPath(),
            () -> TitaniumCrystalModule.TYPE
        );

    public static final DeferredHolder<NPEnhancementModuleType<?>, NPEnhancementModuleType<?>> MECHANICAL_HEART =
        DR.register(
            MechanicalHeartModule.NAME.getPath(),
            () -> MechanicalHeartModule.TYPE
        );

    public static final DeferredHolder<NPEnhancementModuleType<?>, NPEnhancementModuleType<?>> EXOSKELETAL_LEG_FRAME =
        DR.register(
            ExoskeletalLegFrameModule.NAME.getPath(),
            () -> ExoskeletalLegFrameModule.TYPE
        );
}
