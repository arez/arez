package arez.spy;

import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Notification when ObservableValue is created.
 */
public final class ObservableValueCreateEvent implements SerializableEvent {

    private final ObservableValueInfo _observableValue;

    public ObservableValueCreateEvent(final ObservableValueInfo observableValue) {
        _observableValue = observableValue;
    }

    public ObservableValueInfo getObservableValue() {
        return _observableValue;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "ObservableValueCreate");
        map.put("name", getObservableValue().getName());
        SpyEventUtil.maybeAddZone(map);
    }
}
