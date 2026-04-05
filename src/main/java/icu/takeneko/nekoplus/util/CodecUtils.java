package icu.takeneko.nekoplus.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;

public class CodecUtils {
    public static <A, T> A unwrap(DataResult<Pair<A, T>> thiz) {
        return thiz.getOrThrow().getFirst();
    }
}
