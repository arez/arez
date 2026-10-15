package arez;

import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

final class TestLogger implements ArezTestUtil.Logger {
    static final class LogEntry {

        private final String _message;

        @Nullable
        private final Throwable _throwable;

        LogEntry(final String message, @Nullable final Throwable throwable) {
            assertNotNull(message);
            _message = message;
            _throwable = throwable;
        }

        String getMessage() {
            return _message;
        }

        @Nullable
        Throwable getThrowable() {
            return _throwable;
        }
    }

    private final List<LogEntry> _entries = new ArrayList<>();

    @Override
    public void log(final String message, @Nullable final Throwable throwable) {
        _entries.add(new LogEntry(message, throwable));
    }

    List<LogEntry> getEntries() {
        return _entries;
    }
}
