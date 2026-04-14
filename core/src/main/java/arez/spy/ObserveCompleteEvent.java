package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Notification when Observer completes method being observed.
 */
public final class ObserveCompleteEvent
  implements SerializableEvent
{
  @Nonnull
  private final ObserverInfo _observer;
  @Nullable
  private final Throwable _throwable;
  private final int _duration;

  public ObserveCompleteEvent( @Nonnull final ObserverInfo observer,
                               @Nullable final Throwable throwable,
                               final int duration )
  {
    assert duration >= 0;
    _observer = Objects.requireNonNull( observer );
    _throwable = throwable;
    _duration = duration;
  }

  @Nonnull
  public ObserverInfo getObserver()
  {
    return _observer;
  }

  @Nullable
  public Throwable getThrowable()
  {
    return _throwable;
  }

  public int getDuration()
  {
    return _duration;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "ObserveComplete" );
    map.put( "name", getObserver().getName() );
    map.put( "duration", getDuration() );
    final Throwable throwable = getThrowable();
    final String message =
      null == throwable ? null : null == throwable.getMessage() ? throwable.toString() : throwable.getMessage();
    map.put( "errorMessage", message );
    SpyEventUtil.maybeAddZone( map );
  }
}
