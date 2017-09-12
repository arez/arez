package org.realityforge.arez.spy;

import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.ComputedValue;

/**
 * Notification when Action starts.
 */
public final class ActionStartedEvent
{
  @Nonnull
  private final String _name;
  @Nonnull
  private final Object[] _parameters;

  public ActionStartedEvent( @Nonnull final String name, @Nonnull final Object[] parameters )
  {
    _name = Objects.requireNonNull( name );
    _parameters = Objects.requireNonNull( parameters );
  }

  @Nonnull
  public String getName()
  {
    return _name;
  }

  @Nonnull
  public Object[] getParameters()
  {
    return _parameters;
  }
}
