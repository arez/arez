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
  /**
   * Is the action a "tracking" action.
   */
  private final boolean _tracked;
  @Nonnull
  private final Object[] _parameters;
  private final boolean _expectsResult;
  @Nullable
  private final Object _result;
  @Nullable
  private final Throwable _throwable;
  @Nonnegative
  private final long _duration;

  public ActionCompletedEvent( @Nonnull final String name,
                               final boolean tracked,
                               @Nonnull final Object[] parameters,
                               final boolean expectsResult,
                               @Nullable final Object result,
                               @Nullable final Throwable throwable,
                               @Nonnegative final long duration )
  {
    assert duration >= 0;
    assert null == throwable || null == result;
    _name = Objects.requireNonNull( name );
    _tracked = tracked;
    _parameters = Objects.requireNonNull( parameters );
    _expectsResult = expectsResult;
    _result = result;
    _throwable = throwable;
    _duration = duration;
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

  public boolean isExpectsResult()
  {
    return _expectsResult;
  }

  @Nullable
  public Object getResult()
  {
    return _result;
  }

  @Nullable
  public Throwable getThrowable()
  {
    return _throwable;
  }

  @Nonnegative
  public long getDuration()
  {
    return _duration;
  }
}
