package arez.spy;

import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * ComputableValue has de-activated.
 */
public final class ComputableValueDeactivateEvent implements SerializableEvent {

    private final ComputableValueInfo _computableValue;

    public ComputableValueDeactivateEvent(final ComputableValueInfo computableValue) {
        _computableValue = computableValue;
    }

    public ComputableValueInfo getComputableValue() {
        return _computableValue;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "ComputableValueDeactivate");
        map.put("name", getComputableValue().getName());
        SpyEventUtil.maybeAddZone(map);
    }
}
