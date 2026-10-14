package arez.integration.util;

import arez.spy.SerializableEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class SpyEventTestUtil {
    private SpyEventTestUtil() {}

    public static JsonObject toJsonObject(final SerializableEvent event, final boolean keepValue) {
        final Map<String, @Nullable Object> map = new HashMap<>();
        event.toMap(map);
        map.remove("duration");
        map.remove("zone");
        if (!keepValue) {
            map.remove("value");
        }
        final Map<String, @Nullable Object> output = new HashMap<>();
        for (final Map.Entry<String, @Nullable Object> entry : map.entrySet()) {
            final String key = entry.getKey();
            final var value = entry.getValue();
            if (key.equals("value") && value instanceof Collection) {
                // Useful for debugging repositories
                output.put(key, ((Collection<?>) value).size() + " items");
            } else if (null != value && value.getClass().isArray()) {
                final ArrayList<@Nullable Object> v = new ArrayList<>();
                for (int i = 0, end = Array.getLength(value); i < end; i++) {
                    v.add(Array.get(value, i));
                }
                output.put(key, v);
            } else {
                output.put(key, value);
            }
        }
        return Json.createObjectBuilder(output).build();
    }
}
