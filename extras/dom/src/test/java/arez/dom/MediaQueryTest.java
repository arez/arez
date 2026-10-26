package arez.dom;

import static org.testng.Assert.*;

import arez.Arez;
import arez.Disposable;
import arez.Observer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

public final class MediaQueryTest extends AbstractTest {
    @Test
    public void externalEventsBatchAndListenersFollowLifecycle() {
        final var window = new TestWindow();
        final var query = MediaQuery.create(window, "first");
        final TestMediaQueryList first = window.matchMedia("first");
        final var observed = new ArrayList<Boolean>();
        assertSame(query.getWindow(), window);
        assertTrue(first._listeners.isEmpty());
        final Observer observer = Arez.context().observer(() -> observed.add(query.matches()));
        assertEquals(observed, List.of(false));
        final MediaQuery.EventListener listener = first._listeners.get(0);

        first._matches = true;
        listener.handleEvent(new Object());
        assertEquals(observed, List.of(false, true));

        Arez.context().safeAction(() -> {
            first._matches = false;
            listener.handleEvent(new Object());
            first._matches = true;
            listener.handleEvent(new Object());
            first._matches = false;
            listener.handleEvent(new Object());
            assertEquals(observed, List.of(false, true));
        });
        assertEquals(observed, List.of(false, true, false));

        Arez.context().safeAction(() -> query.setQuery("second"));
        final TestMediaQueryList second = window.matchMedia("second");
        assertTrue(first._listeners.isEmpty());
        assertEquals(second._listeners, List.of(listener));
        second._matches = true;
        listener.handleEvent(new Object());
        assertEquals(observed, List.of(false, true, false, true));

        observer.dispose();
        assertTrue(second._listeners.isEmpty());
        Disposable.dispose(query);
        listener.handleEvent(new Object());
        assertEquals(observed, List.of(false, true, false, true));
    }

    private static final class TestWindow extends MediaQuery.Window {
        private final Map<String, TestMediaQueryList> _queries = new HashMap<>();

        @Override
        TestMediaQueryList matchMedia(final String query) {
            return _queries.computeIfAbsent(query, TestMediaQueryList::new);
        }
    }

    private static final class TestMediaQueryList extends MediaQuery.MediaQueryList {
        private final String _query;
        private final List<MediaQuery.EventListener> _listeners = new ArrayList<>();
        private boolean _matches;

        TestMediaQueryList(final String query) {
            _query = query;
        }

        @Override
        String media() {
            return _query;
        }

        @Override
        boolean matches() {
            return _matches;
        }

        @Override
        void addListener(final MediaQuery.EventListener listener) {
            _listeners.add(listener);
        }

        @Override
        void removeListener(final MediaQuery.EventListener listener) {
            assertTrue(_listeners.remove(listener));
        }
    }
}
