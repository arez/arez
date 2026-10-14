package arez.component.internal;

import arez.Arez;
import grim.annotations.OmitType;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;

/**
 * Utility methods used when returning results from repositories.
 */
@OmitType(unless = "arez.collections_properties_unmodifiable")
public final class CollectionsUtil {
    private CollectionsUtil() {}

    /**
     * Wrap specified parameter with unmodifiable variant if and only if {@link Arez#areCollectionsPropertiesUnmodifiable()}
     * returns true, otherwise return the supplied list. This method is invoked by the generated code when it detects
     * list properties and should be used by repository extensions when returning collection results.
     *
     * @param <T>        the type of elements in collection.
     * @param collection the input collection.
     * @return the output collection.
     */
    public static <T extends @Nullable Object> Collection<T> wrap(final Collection<T> collection) {
        return Arez.areCollectionsPropertiesUnmodifiable()
                ? Collections.unmodifiableCollection(collection)
                : collection;
    }

    /**
     * Wrap specified parameter with unmodifiable set if and only if {@link Arez#areCollectionsPropertiesUnmodifiable()}
     * returns true, otherwise return the supplied set. This method is invoked by the generated code when it detects
     * set properties and should be used by repository extensions when returning set results.
     *
     * @param <T> the type of elements in set.
     * @param set the input set.
     * @return the output set.
     */
    public static <T extends @Nullable Object> Set<T> wrap(final Set<T> set) {
        return Arez.areCollectionsPropertiesUnmodifiable() ? Collections.unmodifiableSet(set) : set;
    }

    /**
     * Wrap specified parameter with unmodifiable map if and only if {@link Arez#areCollectionsPropertiesUnmodifiable()}
     * returns true, otherwise return the supplied map. This method is invoked by the generated code when it detects
     * map properties and should be used by repository extensions when returning map results.
     *
     * @param <K> the type of key elements in map.
     * @param <V> the type of value elements in map.
     * @param map the input map.
     * @return the output map.
     */
    public static <K extends @Nullable Object, V extends @Nullable Object> Map<K, V> wrap(final Map<K, V> map) {
        return Arez.areCollectionsPropertiesUnmodifiable() ? Collections.unmodifiableMap(map) : map;
    }

    /**
     * Wrap specified list with unmodifiable list if and only if {@link Arez#areCollectionsPropertiesUnmodifiable()}
     * returns true, otherwise return the supplied list. This method is invoked by the generated code when it detects
     * list properties and should be used by repository extensions when returning list results.
     *
     * @param <T>  the type of elements in collection.
     * @param list the input collection.
     * @return the output collection.
     */
    public static <T extends @Nullable Object> List<T> wrap(final List<T> list) {
        return Arez.areCollectionsPropertiesUnmodifiable() ? Collections.unmodifiableList(list) : list;
    }

    /**
     * Convert specified stream to a list, wrapping as an unmodifiable list if required.
     * This method should be called by repository extensions when returning list results.
     *
     * @param <T>    the type of elements in stream.
     * @param stream the input stream.
     * @return the output list
     */
    public static <T extends @Nullable Object> List<T> asList(final Stream<T> stream) {
        return wrap(stream.collect(Collectors.toList()));
    }
}
