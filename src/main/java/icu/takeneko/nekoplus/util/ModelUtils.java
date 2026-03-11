package icu.takeneko.nekoplus.util;

import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;

public class ModelUtils {
    public static <T extends ModelBuilder<T>> ModelBuilder<T> wrapDefaultBlockItemTransform(ModelBuilder<T> builder) {
        return builder.transforms()
            .transform(ItemDisplayContext.GUI).rotation(30, 225, 0).translation(0, 0, 0).scale(0.625f, 0.625f, 0.625f).end()
            .transform(ItemDisplayContext.GROUND).rotation(0, 0, 0).translation(0, 3, 0).scale(0.25f, 0.25f, 0.25f).end()
            .transform(ItemDisplayContext.FIXED).rotation(0, 0, 0).translation(0, 0, 0).scale(0.5f, 0.5f, 0.5f).end()
            .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(75, 225, 0).translation(0, 2.5f, 0).scale(0.375f, 0.375f, 0.375f).end()
            .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(75, 45, 0).translation(0, 2.5f, 0).scale(0.375f, 0.375f, 0.375f).end()
            .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, 45, 0).translation(0, 0, 0).scale(0.40f, 0.40f, 0.40f).end()
            .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 225, 0).translation(0, 0, 0).scale(0.40f, 0.40f, 0.40f).end()
            .end();
    }
}
