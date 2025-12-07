package icu.takeneko.highenergyanvilology.foundation.material;

import com.mojang.serialization.Codec;
import icu.takeneko.highenergyanvilology.all.HEBuiltinRegistries;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

public class AnvilonType {

    private static final Map<Block, AnvilMaterial> ANVIL_MATERIAL_MAP = new IdentityHashMap<>();

    public static void handleRegistration() {
        for (AnvilMaterial anvilMaterial : HEBuiltinRegistries.MATERIAL) {
            ANVIL_MATERIAL_MAP.put(anvilMaterial.anvilBlock().get(), anvilMaterial);
        }
    }

    @Nullable
    public static AnvilMaterial findType(Block block) {
        return ANVIL_MATERIAL_MAP.get(block);
    }

    public enum Contained implements StringRepresentable {
        UNSTABLE, ENTANGLED;

        @Override
        public String getSerializedName() {
            return name();
        }

        public static final Codec<Contained> CODEC = StringRepresentable.fromEnum(Contained::values);
        public static final StreamCodec<? super ByteBuf, Contained> STREAM_CODEC = ByteBufCodecs.idMapper(
            i -> Contained.values()[i],
            Contained::ordinal
        );
    }
}
