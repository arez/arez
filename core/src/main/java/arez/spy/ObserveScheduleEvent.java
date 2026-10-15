package arez.spy;

import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Notification when on observe is scheduled.
 */
public final class ObserveScheduleEvent implements SerializableEvent {

    private final ObserverInfo _observer;

    public ObserveScheduleEvent(final ObserverInfo observer) {
        _observer = observer;
    }

    public ObserverInfo getObserver() {
        return _observer;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "ObserveSchedule");
        map.put("name", getObserver().getName());
        SpyEventUtil.maybeAddZone(map);
    }
}
