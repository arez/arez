package arez;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class TestLogger
  implements ArezLogger.Logger
{
  static final class LogEntry
  {
    @Nonnull
    private final String _message;
    @Nullable
    private final Throwable _throwable;

    LogEntry( @Nonnull final String message, @Nullable final Throwable throwable )
    {
      _message = message;
      _throwable = throwable;
    }

    @Nonnull
    String getMessage()
    {
      return _message;
    }

    @Nullable
    Throwable getThrowable()
    {
      return _throwable;
    }
  }

  private final ArrayList<LogEntry> _entries = new ArrayList<>();

  @Override
  public void log( @Nonnull final String message, @Nullable final Throwable throwable )
  {
    _entries.add( new LogEntry( message, throwable ) );
  }

  @Nonnull
  ArrayList<LogEntry> getEntries()
  {
    return _entries;
  }
}
