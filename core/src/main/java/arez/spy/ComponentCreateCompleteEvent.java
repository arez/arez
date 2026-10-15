package arez.spy;

import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Notification when a Component is has finished being created.
 */
public final class ComponentCreateCompleteEvent implements SerializableEvent {

    private final ComponentInfo _componentInfo;

    public ComponentCreateCompleteEvent(final ComponentInfo componentInfo) {
        _componentInfo = componentInfo;
    }

    public ComponentInfo getComponentInfo() {
        return _componentInfo;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "ComponentCreateComplete");
        map.put("name", getComponentInfo().getName());
        SpyEventUtil.maybeAddZone(map);
    }
}
