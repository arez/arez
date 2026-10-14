package arez.spy;

import static org.testng.Assert.*;

import arez.AbstractTest;
import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.Task;
import java.util.HashMap;
import org.jspecify.annotations.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;

public final class TaskCompleteEventTest extends AbstractTest {
    @Test
    public void basicOperation() {
        final int duration = 23;
        final ArezContext context = Arez.context();
        final Task task = context.task(ValueUtil::randomString);
        final TaskInfo info = context.getSpy().asTaskInfo(task);
        final var event = new TaskCompleteEvent(info, null, duration);

        assertEquals(event.getTask(), info);
        assertEquals(event.getDuration(), duration);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "TaskComplete");
        assertEquals(data.get("name"), task.getName());
        assertEquals(data.get("duration"), duration);
        assertNull(data.get("errorMessage"));
        assertNotNull(data.get("zone"));
        assert null != data.get("zone");
        assertEquals(data.size(), 5);
    }

    @Test
    public void basicOperation_withError() {
        final int duration = 42;
        final var throwable = new Throwable("Boo!");
        final ArezContext context = Arez.context();
        final Task task = context.task(ValueUtil::randomString);
        final TaskInfo info = context.getSpy().asTaskInfo(task);
        final var event = new TaskCompleteEvent(info, throwable, duration);

        assertEquals(event.getTask(), info);
        assertEquals(event.getDuration(), duration);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "TaskComplete");
        assertEquals(data.get("name"), task.getName());
        assertEquals(data.get("duration"), duration);
        assertEquals(data.get("errorMessage"), "Boo!");
        assertNotNull(data.get("zone"));
        assert null != data.get("zone");
        assertEquals(data.size(), 5);
    }

    @Test
    public void basicOperation_zonesDisabled() {
        ArezTestUtil.disableZones();

        final int duration = 23;
        final ArezContext context = Arez.context();
        final Task task = context.task(ValueUtil::randomString);
        final TaskInfo info = context.getSpy().asTaskInfo(task);
        final var event = new TaskCompleteEvent(info, null, duration);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "TaskComplete");
        assertEquals(data.get("name"), task.getName());
        assertEquals(data.get("duration"), duration);
        assertNull(data.get("errorMessage"));
        assertNull(data.get("zone"));
        assertEquals(data.size(), 4);
    }
}
