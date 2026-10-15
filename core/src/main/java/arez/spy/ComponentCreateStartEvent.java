package arez.spy;

import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Notification when a Component is starting to be created.
 */
public final class ComponentCreateStartEvent implements SerializableEvent {

    private final ComponentInfo _componentInfo;

    public ComponentCreateStartEvent(final ComponentInfo componentInfo) {
        _componentInfo = componentInfo;
    }

    public ComponentInfo getComponentInfo() {
        return _componentInfo;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "ComponentCreateStart");
        map.put("name", getComponentInfo().getName());
        SpyEventUtil.maybeAddZone(map);
    }
}
