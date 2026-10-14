package arez.spy;

import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Notification when a reaction cycle starts.
 */
public final class ReactionCycleStartEvent implements SerializableEvent {
    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "ReactionCycleStart");
        SpyEventUtil.maybeAddZone(map);
    }
}
