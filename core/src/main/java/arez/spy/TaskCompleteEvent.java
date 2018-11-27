package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Notification where explicitly scheduled Task has completed.
 */
public final class TaskCompleteEvent
  implements SerializableEvent
{
  @Nonnull
  private final String _name;
  @Nullable
  private final Throwable _throwable;
  private final int _duration;

  public TaskCompleteEvent( @Nonnull final String name,
                            @Nullable final Throwable throwable,
                            final int duration )
  {
    assert duration >= 0;
    _name = Objects.requireNonNull( name );
    _throwable = throwable;
    _duration = duration;
  }

  @Nonnull
  public String getName()
  {
    return _name;
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "TaskComplete" );
    map.put( "name", getName() );
    map.put( "duration", getDuration() );
    final Throwable throwable = getThrowable();
    final String message =
      null == throwable ? null : null == throwable.getMessage() ? throwable.toString() : throwable.getMessage();
    map.put( "errorMessage", message );
  }
}
