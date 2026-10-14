package arez.spy;

import static org.testng.Assert.*;

import arez.AbstractTest;
import arez.ArezTestUtil;
import java.util.HashMap;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

public final class ReactionCycleStartEventTest extends AbstractTest {
    @Test
    public void basicOperation() {
        final var event = new ReactionCycleStartEvent();

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "ReactionCycleStart");
        assertNotNull(data.get("zone"));
        assert null != data.get("zone");
        assertEquals(data.size(), 2);
    }

    @Test
    public void basicOperation_zonesDisabled() {
        ArezTestUtil.disableZones();

        final var event = new ReactionCycleStartEvent();

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "ReactionCycleStart");
        assertNull(data.get("zone"));
        assertEquals(data.size(), 1);
    }
}
