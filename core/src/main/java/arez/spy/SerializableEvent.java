package arez.spy;

import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Interface used to serialize events.
 * This interface is to enhance debugging and test tools.
 */
public interface SerializableEvent {
    /**
     * Convert event attributes to json compatible values in map.
     *
     * @param map the map in which to serialize values.
     */
    void toMap(final Map<String, @Nullable Object> map);
}
