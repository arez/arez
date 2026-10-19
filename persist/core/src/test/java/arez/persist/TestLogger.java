package arez.persist;

import static org.testng.Assert.assertNotNull;

import arez.persist.runtime.ArezPersistTestUtil;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

public final class TestLogger implements ArezPersistTestUtil.Logger {
    public static final class LogEntry {
        private final String _message;

        @Nullable
        private final Throwable _throwable;

        LogEntry(final String message, @Nullable final Throwable throwable) {
            assertNotNull(message);
            _message = message;
            _throwable = throwable;
        }

        public String getMessage() {
            return _message;
        }

        @Nullable
        public Throwable getThrowable() {
            return _throwable;
        }
    }

    private final List<LogEntry> _entries = new ArrayList<>();

    @Override
    public void log(final String message, @Nullable final Throwable throwable) {
        _entries.add(new LogEntry(message, throwable));
    }

    public List<LogEntry> getEntries() {
        return _entries;
    }
}
