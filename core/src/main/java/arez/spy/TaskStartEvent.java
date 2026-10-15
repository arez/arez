package arez.spy;

import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Notification that Task is starting.
 */
public final class TaskStartEvent implements SerializableEvent {

    private final TaskInfo _task;

    public TaskStartEvent(final TaskInfo task) {
        _task = task;
    }

    public TaskInfo getTask() {
        return _task;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "TaskStart");
        map.put("name", getTask().getName());
        SpyEventUtil.maybeAddZone(map);
    }
}
