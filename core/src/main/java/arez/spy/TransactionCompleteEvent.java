package arez.spy;

import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Notification when Transaction completes.
 */
public final class TransactionCompleteEvent implements SerializableEvent {

    private final String _name;

    private final boolean _mutation;

    @Nullable
    private final ObserverInfo _tracker;

    private final int _duration;

    public TransactionCompleteEvent(
            final String name, final boolean mutation, @Nullable final ObserverInfo tracker, final int duration) {
        assert duration >= 0;
        _name = Objects.requireNonNull(name);
        _mutation = mutation;
        _tracker = tracker;
        _duration = duration;
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

    public int getDuration() {
        return _duration;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "TransactionComplete");
        map.put("name", getName());
        map.put("mutation", isMutation());
        final ObserverInfo tracker = getTracker();
        map.put("tracker", null == tracker ? null : tracker.getName());
        map.put("duration", getDuration());
        SpyEventUtil.maybeAddZone(map);
    }
}
