package arez.spy;

import arez.Arez;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Utility methods to assist spy event serialization.
 */
final class SpyEventUtil {
    private SpyEventUtil() {}

    /**
     * If zones and names are enabled, add the current zone name to the serialized event map.
     *
     * @param map the map to populate.
     */
    static void maybeAddZone(final Map<String, @Nullable Object> map) {
        if (Arez.areZonesEnabled()) {
            map.put("zone", Arez.currentZone().getName());
        }
    }
}
