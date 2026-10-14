package arez.spy;

import static org.testng.Assert.*;

import arez.AbstractTest;
import arez.ArezTestUtil;
import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@SuppressWarnings("ConstantConditions")
public final class TransactionStartEventTest extends AbstractTest {
    @Test
    public void basicOperation() {
        final String name = "Foo@1";
        final boolean mutation = true;
        final ObserverInfo tracker = null;
        final var event = new TransactionStartEvent(name, mutation, tracker);

        assertEquals(event.getName(), name);
        assertEquals(event.isMutation(), mutation);
        assertEquals(event.getTracker(), tracker);

        final Map<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "TransactionStart");
        assertEquals(data.get("name"), name);
        assertEquals(data.get("mutation"), mutation);
        assertEquals(data.get("tracker"), tracker);
        assertNotNull(data.get("zone"));
        assert null != data.get("zone");
        assertEquals(data.size(), 5);
    }

    @Test
    public void basicOperation_zonesDisabled() {
        ArezTestUtil.disableZones();

        final String name = "Foo@1";
        final boolean mutation = true;
        final ObserverInfo tracker = null;
        final var event = new TransactionStartEvent(name, mutation, tracker);

        final Map<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "TransactionStart");
        assertEquals(data.get("name"), name);
        assertEquals(data.get("mutation"), mutation);
        assertEquals(data.get("tracker"), tracker);
        assertNull(data.get("zone"));
        assertEquals(data.size(), 4);
    }
}
