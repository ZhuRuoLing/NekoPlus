package icu.takeneko.nekoplus.data.provider;

import com.mojang.serialization.Codec;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class NPDataProvider<T> implements DataProvider {
    private final Map<Identifier, T> elements = new HashMap<>();
    private final Codec<T> codec;
    private final String prefix;
    private final PackOutput.PathProvider pathProvider;

    public NPDataProvider(PackOutput output, Codec<T> codec, String prefix, String kind, boolean isData) {
        this.codec = codec;
        this.prefix = prefix;
        this.pathProvider = output.createPathProvider(
            isData ? PackOutput.Target.DATA_PACK : PackOutput.Target.RESOURCE_PACK,
            kind
        );
    }

    protected abstract void addEntries();

    protected void add(Identifier location, T t) {
        elements.put(location, t);
    }

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput cache) {
        this.addEntries();
        CompletableFuture<?>[] futures = new CompletableFuture[elements.size()];
        int i = 0;
        for (Map.Entry<Identifier, T> entry : elements.entrySet()) {
            futures[i++] = DataProvider.saveStable(cache, codec, entry.getValue(), this.pathProvider.json(entry.getKey().withPrefix(prefix)));
        }
        return CompletableFuture.allOf(futures);
    }
}
