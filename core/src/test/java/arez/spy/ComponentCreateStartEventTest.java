package arez.spy;

import static org.testng.Assert.*;

import arez.AbstractTest;
import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.Component;
import java.util.HashMap;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

public final class ComponentCreateStartEventTest extends AbstractTest {
    @Test
    public void basicOperation() {
        final ArezContext context = Arez.context();
        final Component component = context.component("Foo", "1");
        final ComponentInfo info = context.getSpy().asComponentInfo(component);
        final var event = new ComponentCreateStartEvent(info);

        assertEquals(event.getComponentInfo(), info);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "ComponentCreateStart");
        assertEquals(data.get("name"), "Foo@1");
        assertNotNull(data.get("zone"));
        assert null != data.get("zone");
        assertEquals(data.size(), 3);
    }

    @Test
    public void basicOperation_zonesDisabled() {
        ArezTestUtil.disableZones();

        final ArezContext context = Arez.context();
        final Component component = context.component("Foo", "1");
        final ComponentInfo info = context.getSpy().asComponentInfo(component);
        final var event = new ComponentCreateStartEvent(info);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "ComponentCreateStart");
        assertEquals(data.get("name"), "Foo@1");
        assertNull(data.get("zone"));
        assertEquals(data.size(), 2);
    }
}
