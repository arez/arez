package arez.spy;

import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Notification when Action is skipped.
 */
public final class ActionSkippedEvent implements SerializableEvent {

    private final String _name;
    /**
     * Is the action a "tracking" action.
     */
    private final boolean _tracked;

    private final @Nullable Object[] _parameters;

    public ActionSkippedEvent(final String name, final boolean tracked, final @Nullable Object[] parameters) {
        _name = name;
        _tracked = tracked;
        _parameters = parameters;
    }

    public String getName() {
        return _name;
    }

    public boolean isTracked() {
        return _tracked;
    }

    public @Nullable Object[] getParameters() {
        return _parameters;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "ActionSkipped");
        map.put("name", getName());
        map.put("tracked", isTracked());
        map.put("parameters", getParameters());
        SpyEventUtil.maybeAddZone(map);
    }
}
