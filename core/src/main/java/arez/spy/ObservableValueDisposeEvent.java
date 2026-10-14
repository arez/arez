package arez.spy;

import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Notification when ObservableValue is disposed.
 */
public final class ObservableValueDisposeEvent implements SerializableEvent {

    private final ObservableValueInfo _observableValue;

    public ObservableValueDisposeEvent(final ObservableValueInfo observableValue) {
        _observableValue = Objects.requireNonNull(observableValue);
    }

    public ObservableValueInfo getObservableValue() {
        return _observableValue;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "ObservableValueDispose");
        map.put("name", getObservableValue().getName());
        SpyEventUtil.maybeAddZone(map);
    }
}
