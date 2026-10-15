package arez;

import arez.spy.Priority;
import arez.spy.TaskInfo;
import grim.annotations.OmitType;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;

/**
 * A implementation of {@link TaskInfo} that proxies to a {@link Task}.
 */
@OmitType(unless = "arez.enable_spies")
final class TaskInfoImpl implements TaskInfo {

    private final Task _task;

    TaskInfoImpl(final Task task) {
        _task = task;
    }

    private static List<TaskInfo> asInfos(final Collection<Task> tasks) {
        return tasks.stream().map(Task::asInfo).collect(Collectors.toList());
    }

    static List<TaskInfo> asUnmodifiableInfos(final Collection<Task> tasks) {
        return Collections.unmodifiableList(asInfos(tasks));
    }

    @Override
    public String getName() {
        return _task.getName();
    }

    @Override
    public boolean isIdle() {
        return _task.isIdle();
    }

    @Override
    public boolean isScheduled() {
        return _task.isQueued();
    }

    @Override
    public Priority getPriority() {
        return _task.getPriority();
    }

    @Override
    public boolean isDisposed() {
        return _task.isDisposed();
    }

    @Override
    public String toString() {
        return _task.toString();
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (o instanceof TaskInfoImpl) {
            final var that = (TaskInfoImpl) o;
            return _task.equals(that._task);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return _task.hashCode();
    }
}
