package arez.integration.util;

import static org.testng.Assert.*;

import arez.spy.SpyEventHandler;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
public final class TestSpyEventHandler implements SpyEventHandler {
    private final @Nullable ArrayList<Object> _events = new ArrayList<>();
    /**
     * When using assertNextEvent this tracks the index that we are up to.
     */
    private int _currentAssertIndex;

    @Override
    public void onSpyEvent(final Object event) {
        _events.add(event);
    }

    /**
     * Assert Event at index is of specific type and return it.
     */
    @NonNull
    public <T extends @Nullable Object> T assertEvent(final Class<T> type, final int index) {
        assertTrue(eventCount() > index);
        final Object event = _events.get(index);
        assertTrue(
                type.isInstance(event),
                "Expected event at index " + index + " to be of type " + type + " but is " + "of type "
                        + event.getClass() + " with value " + event);
        return type.cast(event);
    }

    public void assertEventCount(final int count) {
        assertEquals(eventCount(), count, "Actual events: " + _events);
    }

    /**
     * Assert "next" Event is of specific type.
     * Increment the next counter.
     */
    @NonNull
    public <T extends @Nullable Object> T assertNextEvent(final Class<T> type) {
        final T event = assertEvent(type, _currentAssertIndex);
        _currentAssertIndex++;
        return event;
    }

    /**
     * Assert "next" Event is of specific type.
     * Increment the next counter, run action.
     */
    @NonNull
    public <T extends @Nullable Object> T assertNextEvent(final Class<T> type, final Consumer<T> action) {
        final T event = assertNextEvent(type);
        action.accept(event);
        return event;
    }

    public int eventCount() {
        return _events.size();
    }
}
