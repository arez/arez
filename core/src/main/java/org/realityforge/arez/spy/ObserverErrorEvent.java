package org.realityforge.arez.spy;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.Observer;
import org.realityforge.arez.ObserverError;

/**
 * Notification when Observer produces an error.
 */
public final class ObserverErrorEvent
{
  @Nonnull
  private final Observer _observer;
  @Nonnull
  private final ObserverError _error;
  @Nullable
  private final Throwable _throwable;

  public ObserverErrorEvent( @Nonnull final Observer observer,
                             @Nonnull final ObserverError error,
                             @Nullable final Throwable throwable )
  {
    _observer = Objects.requireNonNull( observer );
    _error = Objects.requireNonNull( error );
    _throwable = throwable;
  }

  @Nonnull
  public Observer getObserver()
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
}
