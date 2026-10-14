package arez.spy;

import static org.testng.Assert.*;

import arez.AbstractTest;
import arez.ArezTestUtil;
import java.util.HashMap;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@SuppressWarnings("ConstantConditions")
public final class TransactionCompleteEventTest extends AbstractTest {
    @Test
    public void basicOperation() {
        final String name = "Foo@1";
        final boolean mutation = true;
        final ObserverInfo tracker = null;
        final int duration = 23;
        final var event = new TransactionCompleteEvent(name, mutation, tracker, duration);

        assertEquals(event.getName(), name);
        assertEquals(event.isMutation(), mutation);
        assertEquals(event.getTracker(), tracker);
        assertEquals(event.getDuration(), duration);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "TransactionComplete");
        assertEquals(data.get("name"), name);
        assertEquals(data.get("mutation"), mutation);
        assertEquals(data.get("tracker"), tracker);
        assertEquals(data.get("duration"), duration);
        assertNotNull(data.get("zone"));
        assert null != data.get("zone");
        assertEquals(data.size(), 6);
    }

    @Test
    public void basicOperation_zonesDisabled() {
        ArezTestUtil.disableZones();

        final String name = "Foo@1";
        final boolean mutation = true;
        final ObserverInfo tracker = null;
        final int duration = 23;
        final var event = new TransactionCompleteEvent(name, mutation, tracker, duration);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "TransactionComplete");
        assertEquals(data.get("name"), name);
        assertEquals(data.get("mutation"), mutation);
        assertEquals(data.get("tracker"), tracker);
        assertEquals(data.get("duration"), duration);
        assertNull(data.get("zone"));
        assertEquals(data.size(), 5);
    }
}
