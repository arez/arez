package arez.spy;

import arez.Arez;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Notification when ObservableValue has changed.
 */
public final class ObservableValueChangeEvent implements SerializableEvent {

    private final ObservableValueInfo _observableValue;

    @Nullable
    private final Object _value;

    public ObservableValueChangeEvent(final ObservableValueInfo observableValue, @Nullable final Object value) {
        _observableValue = Objects.requireNonNull(observableValue);
        _value = value;
    }

    public ObservableValueInfo getObservableValue() {
        return _observableValue;
    }

    @Nullable
    public Object getValue() {
        return _value;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "ObservableValueChange");
        map.put("name", getObservableValue().getName());
        if (Arez.arePropertyIntrospectorsEnabled()) {
            map.put("value", getValue());
        }
        SpyEventUtil.maybeAddZone(map);
    }
}
