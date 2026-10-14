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

public final class ObserveCompleteEventTest extends AbstractTest {
    @Test
    public void basicOperation() {
        final ArezContext context = Arez.context();
        final String name = "Foo@1";
        final Observer observer = context.tracker(name, ValueUtil::randomString);
        final ObserverInfo info = context.getSpy().asObserverInfo(observer);
        final int duration = 23;
        final var event = new ObserveCompleteEvent(info, null, duration);

        assertEquals(event.getObserver(), info);
        assertEquals(event.getDuration(), duration);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "ObserveComplete");
        assertEquals(data.get("name"), name);
        assertEquals(data.get("duration"), duration);
        assertNull(data.get("errorMessage"));
        assertNotNull(data.get("zone"));
        assert null != data.get("zone");
        assertEquals(data.size(), 5);
    }

    @Test
    public void basicOperation_withError() {
        final ArezContext context = Arez.context();
        final String name = "Foo@1";
        final Observer observer = context.tracker(name, ValueUtil::randomString);
        final ObserverInfo info = context.getSpy().asObserverInfo(observer);
        final int duration = 42;
        final var throwable = new Throwable("Boo!");
        final var event = new ObserveCompleteEvent(info, throwable, duration);

        assertEquals(event.getObserver(), info);
        assertEquals(event.getDuration(), duration);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "ObserveComplete");
        assertEquals(data.get("name"), name);
        assertEquals(data.get("duration"), duration);
        assertEquals(data.get("errorMessage"), "Boo!");
        assertNotNull(data.get("zone"));
        assert null != data.get("zone");
        assertEquals(data.size(), 5);
    }

    @Test
    public void basicOperation_zonesDisabled() {
        ArezTestUtil.disableZones();

        final ArezContext context = Arez.context();
        final String name = "Foo@1";
        final Observer observer = context.tracker(name, ValueUtil::randomString);
        final ObserverInfo info = context.getSpy().asObserverInfo(observer);
        final int duration = 23;
        final var event = new ObserveCompleteEvent(info, null, duration);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "ObserveComplete");
        assertEquals(data.get("name"), name);
        assertEquals(data.get("duration"), duration);
        assertNull(data.get("errorMessage"));
        assertNull(data.get("zone"));
        assertEquals(data.size(), 4);
    }
}
