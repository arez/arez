package arez.persist;

import arez.persist.runtime.ArezPersistTestUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class TestLogger
  implements ArezPersistTestUtil.Logger
{
  public static final class LogEntry
  {
    @Nonnull
    private final String _message;
    @Nullable
    private final Throwable _throwable;

    LogEntry( @Nonnull final String message, @Nullable final Throwable throwable )
    {
      _message = Objects.requireNonNull(message);
      _throwable = throwable;
    }

    @Nonnull
    public String getMessage()
    {
      return _message;
    }

    @Nullable
    public Throwable getThrowable()
    {
      return _throwable;
    }
  }

  @Nonnull
  private final List<LogEntry> _entries = new ArrayList<>();

  @Override
  public void log( @Nonnull final String message, @Nullable final Throwable throwable )
  {
    _entries.add( new LogEntry( message, throwable ) );
  }

  @Nonnull
  public List<LogEntry> getEntries()
  {
    return _entries;
  }
}
