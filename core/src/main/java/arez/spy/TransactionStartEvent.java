package arez.spy;

import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Notification when Transaction starts.
 */
public final class TransactionStartEvent implements SerializableEvent {

    private final String _name;

    private final boolean _mutation;

    @Nullable
    private final ObserverInfo _tracker;

    public TransactionStartEvent(final String name, final boolean mutation, @Nullable final ObserverInfo tracker) {
        _name = Objects.requireNonNull(name);
        _mutation = mutation;
        _tracker = tracker;
    }

    public String getName() {
        return _name;
    }

    public boolean isMutation() {
        return _mutation;
    }

    @Nullable
    public ObserverInfo getTracker() {
        return _tracker;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "TransactionStart");
        map.put("name", getName());
        map.put("mutation", isMutation());
        final ObserverInfo tracker = getTracker();
        map.put("tracker", null == tracker ? null : tracker.getName());
        SpyEventUtil.maybeAddZone(map);
    }
}
