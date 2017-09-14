package org.realityforge.arez.spy;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.Node;
import org.realityforge.arez.ObserverError;

/**
 * Notification when Observer produces an error.
 */
public final class ObserverErrorEvent
{
  @Nonnull
  private final Node _observer;
  @Nonnull
  private final ObserverError _error;
  @Nullable
  private final Throwable _throwable;

  public ObserverErrorEvent( @Nonnull final Node observer,
                             @Nonnull final ObserverError error,
                             @Nullable final Throwable throwable )
  {
    _observer = Objects.requireNonNull( observer );
    _error = Objects.requireNonNull( error );
    _throwable = throwable;
  }

  @Nonnull
  public Node getObserver()
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
