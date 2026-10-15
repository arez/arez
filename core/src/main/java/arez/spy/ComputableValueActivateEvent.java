package arez.spy;

import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * ComputableValue has activated.
 */
public final class ComputableValueActivateEvent implements SerializableEvent {

    private final ComputableValueInfo _computableValue;

    public ComputableValueActivateEvent(final ComputableValueInfo computableValue) {
        _computableValue = computableValue;
    }

    public ComputableValueInfo getComputableValue() {
        return _computableValue;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "ComputableValueActivate");
        map.put("name", getComputableValue().getName());
        SpyEventUtil.maybeAddZone(map);
    }
}
