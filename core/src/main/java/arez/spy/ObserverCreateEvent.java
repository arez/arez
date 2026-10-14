package arez.spy;

import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Notification when Observer is created.
 */
public final class ObserverCreateEvent implements SerializableEvent {

    private final ObserverInfo _observer;

    public ObserverCreateEvent(final ObserverInfo observer) {
        _observer = Objects.requireNonNull(observer);
    }

    public ObserverInfo getObserver() {
        return _observer;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "ObserverCreate");
        map.put("name", getObserver().getName());
        SpyEventUtil.maybeAddZone(map);
    }
}
