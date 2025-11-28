package icu.takeneko.highenergyanvilology.material;

import lombok.Builder;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

@Builder
public record AnvilMaterial(
     float processRate,
     int maxDamage,
//     Supplier<Item> materialBlockItemRef,
//     Supplier<Item> materialIngotItemRef,
     Supplier<Item> materialAnvilItemRef
) {
    

}
