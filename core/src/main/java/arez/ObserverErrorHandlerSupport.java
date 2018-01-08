package arez;

import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.anodoc.TestOnly;
import static org.realityforge.braincheck.Guards.*;

/**
 * Class supporting the propagation of errors for ObserverErrorHandler callback to multiple error handlers.
 */
final class ObserverErrorHandlerSupport
  implements ObserverErrorHandler
{
  /**
   * The list of error handlers to call when an error is received.
   */
  private final ArrayList<ObserverErrorHandler> _observerErrorHandlers = new ArrayList<>();

  /**
   * Add error handler to the list of error handlers called.
   * The handler should not already be in the list.
   *
   * @param handler the error handler.
   */
  void addObserverErrorHandler( @Nonnull final ObserverErrorHandler handler )
  {
    apiInvariant( () -> !_observerErrorHandlers.contains( handler ),
                  () -> "Attempting to add handler " + handler + " that is already in the list of error handlers." );
    _observerErrorHandlers.add( Objects.requireNonNull( handler ) );
  }

  /**
   * Remove error handler from list of existing error handlers.
   * The handler should already be in the list.
   *
   * @param handler the error handler.
   */
  void removeObserverErrorHandler( @Nonnull final ObserverErrorHandler handler )
  {
    apiInvariant( () -> _observerErrorHandlers.contains( handler ),
                  () -> "Attempting to remove handler " + handler + " that is not in the list of error handlers." );
    _observerErrorHandlers.remove( Objects.requireNonNull( handler ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onObserverError( @Nonnull final Observer observer,
                               @Nonnull final ObserverError error,
                               @Nullable final Throwable throwable )
  {
    for ( final ObserverErrorHandler errorHandler : _observerErrorHandlers )
    {
      try
      {
        errorHandler.onObserverError( observer, error, throwable );
      }
      catch ( final Throwable nestedError )
      {
        final String message =
          ArezUtil.safeGetString( () -> "Exception when notifying error handler '" + errorHandler + "' of '" +
                                        error.name() + "' error in observer named '" + observer.getName() + "'." );
        ArezLogger.log( message, nestedError );
      }
    }
  }

  @TestOnly
  @Nonnull
  ArrayList<ObserverErrorHandler> getObserverErrorHandlers()
  {
    return _observerErrorHandlers;
  }
}
