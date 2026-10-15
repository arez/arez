package arez.spy;

import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * ComputableValue has been disposed.
 */
public final class ComputableValueDisposeEvent implements SerializableEvent {

    private final ComputableValueInfo _computableValue;

    public ComputableValueDisposeEvent(final ComputableValueInfo computableValue) {
        _computableValue = computableValue;
    }

    public ComputableValueInfo getComputableValue() {
        return _computableValue;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "ComputableValueDispose");
        map.put("name", getComputableValue().getName());
        SpyEventUtil.maybeAddZone(map);
    }
}
