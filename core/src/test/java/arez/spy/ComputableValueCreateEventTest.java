package arez.spy;

import static org.testng.Assert.*;

import arez.AbstractTest;
import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.ComputableValue;
import java.util.HashMap;
import org.jspecify.annotations.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;

public final class ComputableValueCreateEventTest extends AbstractTest {
    @Test
    public void basicOperation() {
        final ArezContext context = Arez.context();
        final ComputableValue<?> computableValue = context.computable("Foo@1", ValueUtil::randomString);
        final ComputableValueInfo info = context.getSpy().asComputableValueInfo(computableValue);
        final var event = new ComputableValueCreateEvent(info);

        assertEquals(event.getComputableValue(), info);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "ComputableValueCreate");
        assertEquals(data.get("name"), "Foo@1");
        assertNotNull(data.get("zone"));
        assert null != data.get("zone");
        assertEquals(data.size(), 3);
    }

    @Test
    public void basicOperation_zonesDisabled() {
        ArezTestUtil.disableZones();

        final ArezContext context = Arez.context();
        final ComputableValue<?> computableValue = context.computable("Foo@1", ValueUtil::randomString);
        final ComputableValueInfo info = context.getSpy().asComputableValueInfo(computableValue);
        final var event = new ComputableValueCreateEvent(info);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "ComputableValueCreate");
        assertEquals(data.get("name"), "Foo@1");
        assertNull(data.get("zone"));
        assertEquals(data.size(), 2);
    }
}
