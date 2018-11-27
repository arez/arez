package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification where explicitly scheduled Task is starting.
 */
public final class TaskStartEvent
  implements SerializableEvent
{
  @Nonnull
  private final String _name;

  public TaskStartEvent( @Nonnull final String name )
  {
    _name = Objects.requireNonNull( name );
  }

  @Nonnull
  public String getName()
  {
    return _name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "TaskStart" );
    map.put( "name", getName() );
  }
}
