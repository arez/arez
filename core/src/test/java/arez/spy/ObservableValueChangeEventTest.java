package arez.spy;

import static org.testng.Assert.*;

import arez.AbstractTest;
import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.ObservableValue;
import java.util.HashMap;
import org.jspecify.annotations.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;

public final class ObservableValueChangeEventTest extends AbstractTest {
    @Test
    public void basicOperation() {
        final ArezContext context = Arez.context();
        final String name = "Foo@1";
        final ObservableValue<?> observableValue = context.observable(name);
        final ObservableValueInfo info = context.getSpy().asObservableValueInfo(observableValue);
        final String value = ValueUtil.randomString();
        final var event = new ObservableValueChangeEvent(info, value);

        assertEquals(event.getObservableValue(), info);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "ObservableValueChange");
        assertEquals(data.get("name"), name);
        assertEquals(data.get("value"), value);
        assertNotNull(data.get("zone"));
        assert null != data.get("zone");
        assertEquals(data.size(), 4);
    }

    @Test
    public void basicOperation_zonesDisabled() {
        ArezTestUtil.disableZones();

        final ArezContext context = Arez.context();
        final String name = "Foo@1";
        final ObservableValue<?> observableValue = context.observable(name);
        final ObservableValueInfo info = context.getSpy().asObservableValueInfo(observableValue);
        final String value = ValueUtil.randomString();
        final var event = new ObservableValueChangeEvent(info, value);

        final HashMap<String, @Nullable Object> data = new HashMap<>();
        event.toMap(data);

        assertEquals(data.get("type"), "ObservableValueChange");
        assertEquals(data.get("name"), name);
        assertEquals(data.get("value"), value);
        assertNull(data.get("zone"));
        assertEquals(data.size(), 3);
    }
}
