package arez.spy;

import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Notification when a reaction cycle completes.
 */
public final class ReactionCycleCompleteEvent
  implements SerializableEvent
{
  @Nullable
  private final Throwable _throwable;
  private final int _duration;

  public ReactionCycleCompleteEvent( @Nullable final Throwable throwable, final int duration )
  {
    assert duration >= 0;
    _throwable = throwable;
    _duration = duration;
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
    map.put( "type", "ReactionCycleComplete" );
    map.put( "duration", getDuration() );
    final Throwable throwable = getThrowable();
    final String message =
      null == throwable ? null : null == throwable.getMessage() ? throwable.toString() : throwable.getMessage();
    map.put( "errorMessage", message );
    SpyEventUtil.maybeAddZone( map );
  }
}
