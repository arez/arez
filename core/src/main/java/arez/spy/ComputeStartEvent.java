package arez.spy;

import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Notification when Computation starts.
 */
public final class ComputeStartEvent implements SerializableEvent {

    private final ComputableValueInfo _computableValue;

    public ComputeStartEvent(final ComputableValueInfo computableValue) {
        _computableValue = Objects.requireNonNull(computableValue);
    }

    public ComputableValueInfo getComputableValue() {
        return _computableValue;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "ComputeStart");
        map.put("name", getComputableValue().getName());
        SpyEventUtil.maybeAddZone(map);
    }
}
