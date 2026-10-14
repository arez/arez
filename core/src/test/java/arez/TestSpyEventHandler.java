package arez;

import static org.testng.Assert.*;

import arez.spy.SerializableEvent;
import arez.spy.SpyEventHandler;
import arez.spy.SpyEventTestUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;

@SuppressWarnings("NonJREEmulationClassesInClientCode")
public final class TestSpyEventHandler implements SpyEventHandler {

    private final ArezContext _context;

    private final List<Object> _events = new ArrayList<>();
    /**
     * When using assertNextEvent this tracks the index that we are up to.
     */
    private int _currentAssertIndex;

    TestSpyEventHandler(final ArezContext context) {
        _context = Objects.requireNonNull(context);
    }

    public static TestSpyEventHandler subscribe() {
        return subscribe(Arez.context());
    }

    static TestSpyEventHandler subscribe(final ArezContext context) {
        final var handler = new TestSpyEventHandler(context);
        context.getSpy().addSpyEventHandler(handler);
        return handler;
    }

    public void unsubscribe() {
        _context.getSpy().removeSpyEventHandler(this);
    }

    @Override
    public void onSpyEvent(final Object event) {
        _events.add(event);
    }

    public void assertEventCount(final int count) {
        assertEquals(_events.size(), count, "Actual events:\n" + eventsDebug());
    }

    private String eventsDebug() {
        final var counter = new AtomicInteger(0);
        return _events.stream()
                .map(this::convertEventToString)
                .map(s -> counter.getAndIncrement() + ": " + s)
                .collect(Collectors.joining("\n"));
    }

    private String convertEventToString(final Object event) {
        return (event instanceof SerializableEvent se)
                ? SpyEventTestUtil.toJsonObject(se, false).toString()
                : event.toString();
    }

    /**
     * Assert "next" Event is of specific type.
     * Increment the next counter.
     */
    public <T> void assertNextEvent(final Class<T> type) {
        assertEvent(type, null);
    }

    /**
     * Assert "next" Event is of specific type.
     * Increment the next counter, run action.
     */
    public <T> void assertNextEvent(final Class<T> type, final Consumer<T> action) {
        assertEvent(type, action);
    }

    public void reset() {
        _events.clear();
        _currentAssertIndex = 0;
    }

    private <T> void assertEvent(final Class<T> type, @Nullable final Consumer<T> action) {
        assertTrue(_events.size() > _currentAssertIndex);
        final Object e = _events.get(_currentAssertIndex);
        assertTrue(
                type.isInstance(e),
                "Expected event at index " + _currentAssertIndex + " to be of type " + type + " but is " + "of type "
                        + e.getClass() + " with value " + e + "\n\n" + "Actual events:\n"
                        + eventsDebug());
        _currentAssertIndex++;
        final T event = type.cast(e);
        if (null != action) {
            action.accept(event);
        }
    }
}
