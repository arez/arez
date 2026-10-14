package arez.spy;

import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Notification when Observer is about to call method being observed.
 */
public final class ObserveStartEvent implements SerializableEvent {

    private final ObserverInfo _observer;

    public ObserveStartEvent(final ObserverInfo observer) {
        _observer = Objects.requireNonNull(observer);
    }

    public ObserverInfo getObserver() {
        return _observer;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "ObserveStart");
        map.put("name", getObserver().getName());
        SpyEventUtil.maybeAddZone(map);
    }
}
