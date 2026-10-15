package arez.spy;

import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Notification when Observer is disposed.
 */
public final class ObserverDisposeEvent implements SerializableEvent {

    private final ObserverInfo _observer;

    public ObserverDisposeEvent(final ObserverInfo observer) {
        _observer = observer;
    }

    public ObserverInfo getObserver() {
        return _observer;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "ObserverDispose");
        map.put("name", getObserver().getName());
        SpyEventUtil.maybeAddZone(map);
    }
}
