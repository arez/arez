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

public final class TaskStartEventTest extends AbstractTest {
    @Test
    public void basicOperation() {
        final ArezContext context = Arez.context();
        final Task task = context.task(ValueUtil::randomString);
        final TaskInfo info = context.getSpy().asTaskInfo(task);
        final var event = new TaskStartEvent(info);

        assertEquals(event.getTask(), info);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "TaskStart");
        assertEquals(data.get("name"), info.getName());
        assertNotNull(data.get("zone"));
        assert null != data.get("zone");
        assertEquals(data.size(), 3);
    }

    @Test
    public void basicOperation_zonesDisabled() {
        ArezTestUtil.disableZones();

        final ArezContext context = Arez.context();
        final Task task = context.task(ValueUtil::randomString);
        final TaskInfo info = context.getSpy().asTaskInfo(task);
        final var event = new TaskStartEvent(info);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "TaskStart");
        assertEquals(data.get("name"), info.getName());
        assertNull(data.get("zone"));
        assertEquals(data.size(), 2);
    }
}
