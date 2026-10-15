package arez.persist;

import static org.testng.Assert.assertNotNull;

import arez.persist.runtime.ArezPersistTestUtil;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class TestLogger implements ArezPersistTestUtil.Logger {
    public static final class LogEntry {
        @NonNull
        private final String _message;

        @Nullable
        private final Throwable _throwable;

        LogEntry(@NonNull final String message, @Nullable final Throwable throwable) {
            assertNotNull(message);
            _message = message;
            _throwable = throwable;
        }

        @NonNull
        public String getMessage() {
            return _message;
        }

        @Nullable
        public Throwable getThrowable() {
            return _throwable;
        }
    }

    @NonNull
    private final List<LogEntry> _entries = new ArrayList<>();

    @Override
    public void log(@NonNull final String message, @Nullable final Throwable throwable) {
        _entries.add(new LogEntry(message, throwable));
    }

    @NonNull
    public List<LogEntry> getEntries() {
        return _entries;
    }
}
