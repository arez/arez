package arez.component.internal;

import arez.Disposable;
import arez.component.ComponentObservable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;

/** Internal reconciliation support for generated mapped relationships. */
public final class MappedRelationshipCache<F extends @Nullable Object, T> {
    private Map<F, Entry<F, T>> _cache = new IdentityHashMap<>();
    private List<Entry<F, T>> _order = new ArrayList<>();

    /** Creates an empty relationship cache. */
    public MappedRelationshipCache() {}

    /**
     * Reconciles and observes the targets for a newly supplied source stream.
     *
     * @param sources the source stream, consumed sequentially and closed by this method.
     * @param factory the target factory.
     * @param filter the optional presentation predicate.
     * @param comparator the optional comparator provider.
     * @param retention the optional absent-target retention predicate.
     * @return presented targets, including retained entries and repeated source occurrences.
     */
    public List<T> getRelated(
            final Stream<F> sources,
            final Function<? super F, ? extends T> factory,
            final @Nullable Predicate<? super T> filter,
            final @Nullable Supplier<? extends Comparator<? super T>> comparator,
            final @Nullable Predicate<? super T> retention) {
        final Map<F, Entry<F, T>> existing = _cache;
        final List<Entry<F, T>> previousOrder = _order;
        _cache = new IdentityHashMap<>();
        _order = new ArrayList<>();
        final List<T> result = new ArrayList<>();
        try (sources) {
            sources.sequential().forEachOrdered(source -> {
                Entry<F, T> entry = _cache.get(source);
                if (null == entry) {
                    entry = existing.get(source);
                    if (null == entry || Disposable.isDisposed(entry._target)) {
                        entry = new Entry<>(
                                source,
                                Objects.requireNonNull(
                                        factory.apply(source), "Mapped relationship factory returned null"));
                    }
                    _cache.put(source, entry);
                    _order.add(entry);
                }
                ComponentObservable.observe(entry._target);
                result.add(entry._target);
            });
        }
        if (null != retention) {
            for (final Entry<F, T> entry : previousOrder) {
                if (!_cache.containsKey(entry._source)
                        && Disposable.isNotDisposed(entry._target)
                        && retention.test(entry._target)) {
                    _cache.put(entry._source, entry);
                    _order.add(entry);
                    ComponentObservable.observe(entry._target);
                    result.add(entry._target);
                }
            }
        }
        if (null != filter) {
            result.removeIf(target -> !filter.test(target));
        }
        if (null != comparator) {
            result.sort(Objects.requireNonNull(comparator.get(), "Mapped relationship comparator returned null"));
        }
        return result;
    }

    /** Releases all cached references without explicitly disposing target components. */
    public void clear() {
        _cache.clear();
        _order.clear();
    }

    private static final class Entry<F extends @Nullable Object, T> {
        private final F _source;
        private final T _target;

        private Entry(final F source, final T target) {
            _source = source;
            _target = target;
        }
    }
}
