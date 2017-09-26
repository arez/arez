package org.realityforge.arez.spy;

import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when Action starts.
 */
public final class ActionStartedEvent
{
  @Nonnull
  private final String _name;
  /**
   * Is the action a "tracking" action.
   */
  private final boolean _tracked;
  @Nonnull
  private final Object[] _parameters;

  public ActionStartedEvent( @Nonnull final String name, final boolean tracked, @Nonnull final Object[] parameters )
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
}
