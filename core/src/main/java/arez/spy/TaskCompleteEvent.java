package arez.spy;

import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Notification that task has completed execution.
 */
public final class TaskCompleteEvent implements SerializableEvent {

    private final TaskInfo _task;

    @Nullable
    private final Throwable _throwable;

    private final int _duration;

    public TaskCompleteEvent(final TaskInfo task, @Nullable final Throwable throwable, final int duration) {
        assert duration >= 0;
        _task = task;
        _throwable = throwable;
        _duration = duration;
    }

    public TaskInfo getTask() {
        return _task;
    }

    @Nullable
    public Throwable getThrowable() {
        return _throwable;
    }

    public int getDuration() {
        return _duration;
    }

    @Override
    public void toMap(final Map<String, @Nullable Object> map) {
        map.put("type", "TaskComplete");
        map.put("name", getTask().getName());
        map.put("duration", getDuration());
        final Throwable throwable = getThrowable();
        final String message = null == throwable
                ? null
                : null == throwable.getMessage() ? throwable.toString() : throwable.getMessage();
        map.put("errorMessage", message);
        SpyEventUtil.maybeAddZone(map);
    }
}
