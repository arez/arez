package arez.spy;

import arez.ObserverError;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Notification when Observer produces an error.
 */
public final class ObserverErrorEvent
  implements SerializableEvent
{
  @Nonnull
  private final ObserverInfo _observer;
  @Nonnull
  private final ObserverError _error;
  @Nullable
  private final Throwable _throwable;

  public ObserverErrorEvent( @Nonnull final ObserverInfo observer,
                             @Nonnull final ObserverError error,
                             @Nullable final Throwable throwable )
  {
    _observer = Objects.requireNonNull( observer );
    _error = Objects.requireNonNull( error );
    _throwable = throwable;
  }

  @Nonnull
  public ObserverInfo getObserver()
  {
    return _observer;
  }

  @Nonnull
  public ObserverError getError()
  {
    return _error;
  }

  @Nullable
  public Throwable getThrowable()
  {
    return _throwable;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "ObserverError" );
    map.put( "name", getObserver().getName() );
    map.put( "errorType", getError().name() );
    final Throwable throwable = getThrowable();
    final String message =
      null == throwable ? null : null == throwable.getMessage() ? throwable.toString() : throwable.getMessage();
    map.put( "message", message );
    SpyEventUtil.maybeAddZone( map );
  }
}
