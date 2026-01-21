package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when Action is skipped.
 */
public final class ActionSkippedEvent
  implements SerializableEvent
{
  @Nonnull
  private final String _name;
  /**
   * Is the action a "tracking" action.
   */
  private final boolean _tracked;
  @Nonnull
  private final Object[] _parameters;

  public ActionSkippedEvent( @Nonnull final String name, final boolean tracked, @Nonnull final Object[] parameters )
  {
    _name = Objects.requireNonNull( name );
    _tracked = tracked;
    _parameters = Objects.requireNonNull( parameters );
  }

  @Nonnull
  public String getName()
  {
    return _name;
  }

  public boolean isTracked()
  {
    return _tracked;
  }

  @Nonnull
  public Object[] getParameters()
  {
    return _parameters;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "ActionSkipped" );
    map.put( "name", getName() );
    map.put( "tracked", isTracked() );
    map.put( "parameters", getParameters() );
    SpyEventUtil.maybeAddZone( map );
  }
}
