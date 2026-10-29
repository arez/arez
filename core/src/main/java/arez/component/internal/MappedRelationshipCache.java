package arez.component.internal;

import arez.Disposable;
import arez.component.ComponentObservable;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;

/** Internal reconciliation support for generated mapped relationships. */
public final class MappedRelationshipCache<F extends @Nullable Object, T> {
    private Map<F, T> _cache = new IdentityHashMap<>();

    /** Creates an empty relationship cache. */
    public MappedRelationshipCache() {}

    /**
     * Reconciles and observes the targets for a newly supplied source stream.
     *
     * @param sources the source stream, consumed sequentially and closed by this method.
     * @param factory the target factory.
     * @return targets in source encounter order, preserving duplicate occurrences.
     */
    public List<T> getRelated(final Stream<F> sources, final Function<? super F, ? extends T> factory) {
        final Map<F, T> existing = _cache;
        _cache = new IdentityHashMap<>();
        final List<T> result = new ArrayList<>();
        try (sources) {
            sources.sequential().forEachOrdered(source -> {
                T target = _cache.get(source);
                if (null == target) {
                    target = existing.get(source);
                    if (null == target || Disposable.isDisposed(target)) {
                        target = Objects.requireNonNull(
                                factory.apply(source), "Mapped relationship factory returned null");
                    }
                    _cache.put(source, target);
                }
                ComponentObservable.observe(target);
                result.add(target);
            });
        }
        return result;
    }

    /** Releases all cached references without explicitly disposing target components. */
    public void clear() {
        _cache.clear();
    }
}
