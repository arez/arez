package org.realityforge.arez.api2;

import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.TestOnly;

/**
 * Class supporting the propagation of errors for ObserverErrorHandler callback to multiple error handlers.
 */
final class ObserverErrorHandlerSupport
  implements ObserverErrorHandler
{
  /**
   * The list of error handlers to call when an error is received.
   */
  private final ArrayList<ObserverErrorHandler> _errorHandlers = new ArrayList<>();

  /**
   * Add error handler to the list of error handlers called.
   * The handler should not already be in the list.
   *
   * @param handler the error handler.
   */
  void addObserverErrorHandler( @Nonnull final ObserverErrorHandler handler )
  {
    Guards.invariant( () -> !_errorHandlers.contains( handler ),
                      () -> "Attempting to add handler " + handler +
                            " that is already in the list of error handlers." );
    _errorHandlers.add( Objects.requireNonNull( handler ) );
  }

  /**
   * Remove error handler from list of existing error handlers.
   * The handler should already be in the list.
   *
   * @param handler the error handler.
   */
  void removeObserverErrorHandler( @Nonnull final ObserverErrorHandler handler )
  {
    Guards.invariant( () -> _errorHandlers.contains( handler ),
                      () -> "Attempting to remove handler " + handler + " that is not in the list of error handlers." );
    _errorHandlers.remove( Objects.requireNonNull( handler ) );
  }

  /**
   * {@inheritDoc}
   */
  public void onObserverError( @Nonnull final Observer observer,
                               @Nonnull final ObserverError error,
                               @Nullable final Throwable throwable )
  {
    for ( final ObserverErrorHandler errorHandler : _errorHandlers )
    {
      try
      {
        errorHandler.onObserverError( observer, error, throwable );
      }
      catch ( final Throwable nestedError )
      {
        final String message =
          ArezUtil.safeGetString( () ->
                                    String.format(
                                      "Exception when notifying error handler '%s' of '%s' error in observer named '%s'.",
                                      String.valueOf( errorHandler ),
                                      error.name(),
                                      observer.getName() ) );
        ArezLogger.log( message, nestedError );
      }
    }
  }

  @TestOnly
  @Nonnull
  ArrayList<ObserverErrorHandler> getErrorHandlers()
  {
    return _errorHandlers;
  }
}
