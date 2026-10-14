package arez.spy;

import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Notification when a Component dispose has completed.
 */
public final class ComponentDisposeCompleteEvent implements SerializableEvent {

    private final ComponentInfo _componentInfo;

    public ComponentDisposeCompleteEvent(final ComponentInfo componentInfo) {
        _componentInfo = Objects.requireNonNull(componentInfo);
    }

    public ComponentInfo getComponentInfo() {
        return _componentInfo;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "ComponentDisposeComplete");
        map.put("name", getComponentInfo().getName());
        SpyEventUtil.maybeAddZone(map);
    }
}
