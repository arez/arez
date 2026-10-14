package arez.spy;

import static org.testng.Assert.*;

import arez.AbstractTest;
import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.Observer;
import java.util.HashMap;
import org.jspecify.annotations.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;

public final class ObserverCreateEventTest extends AbstractTest {
    @Test
    public void basicOperation() {
        final ArezContext context = Arez.context();
        final String name = "Foo@1";
        final Observer observer = context.tracker(name, ValueUtil::randomString);
        final ObserverInfo info = context.getSpy().asObserverInfo(observer);
        final var event = new ObserverCreateEvent(info);

        assertEquals(event.getObserver(), info);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "ObserverCreate");
        assertEquals(data.get("name"), name);
        assertNotNull(data.get("zone"));
        assert null != data.get("zone");
        assertEquals(data.size(), 3);
    }

    @Test
    public void basicOperation_zonesDisabled() {
        ArezTestUtil.disableZones();

        final ArezContext context = Arez.context();
        final String name = "Foo@1";
        final Observer observer = context.tracker(name, ValueUtil::randomString);
        final ObserverInfo info = context.getSpy().asObserverInfo(observer);
        final var event = new ObserverCreateEvent(info);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "ObserverCreate");
        assertEquals(data.get("name"), name);
        assertNull(data.get("zone"));
        assertEquals(data.size(), 2);
    }
}
