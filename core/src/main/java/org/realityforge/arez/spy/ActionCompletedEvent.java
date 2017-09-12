package org.realityforge.arez.spy;

import java.util.Objects;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Notification when Action completes.
 */
public final class ActionCompletedEvent
{
  @Nonnull
  private final String _name;
  @Nonnull
  private final Object[] _parameters;
  @Nullable
  private final Object _result;
  @Nonnegative
  private final long _duration;

  public ActionCompletedEvent( @Nonnull final String name,
                               @Nonnull final Object[] parameters,
                               @Nullable final Object result,
                               @Nonnegative final long duration )
  {
    assert duration >= 0;
    _name = Objects.requireNonNull( name );
    _parameters = Objects.requireNonNull( parameters );
    _result = result;
    _duration = duration;
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

  @Nullable
  public Object getResult()
  {
    return _result;
  }

  @Nonnegative
  public long getDuration()
  {
    return _duration;
  }
}
