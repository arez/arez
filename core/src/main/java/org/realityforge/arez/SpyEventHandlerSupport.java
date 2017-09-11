package org.realityforge.arez;

import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.TestOnly;

/**
 * Class supporting the propagation of events to SpyEventHandler callbacks.
 */
final class SpyEventHandlerSupport
  implements SpyEventHandler
{
  /**
   * The list of spy handlers to call when an event is received.
   */
  private final ArrayList<SpyEventHandler> _spyEventHandlers = new ArrayList<>();

  /**
   * Add a spy handler to the list of handlers.
   * The handler should not already be in the list.
   *
   * @param handler the spy handler.
   */
  void addSpyEventHandler( @Nonnull final SpyEventHandler handler )
  {
    Guards.invariant( () -> !_spyEventHandlers.contains( handler ),
                      () -> "Attempting to add handler " + handler +
                            " that is already in the list of spy handlers." );
    _spyEventHandlers.add( Objects.requireNonNull( handler ) );
  }

  /**
   * Remove spy handler from list of existing handlers.
   * The handler should already be in the list.
   *
   * @param handler the spy handler.
   */
  void removeSpyEventHandler( @Nonnull final SpyEventHandler handler )
  {
    Guards.invariant( () -> _spyEventHandlers.contains( handler ),
                      () -> "Attempting to remove handler " + handler + " that is not in the list of spy handlers." );
    _spyEventHandlers.remove( Objects.requireNonNull( handler ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onSpyEvent( @Nonnull final Object event )
  {
    for ( final SpyEventHandler handler : _spyEventHandlers )
    {
      try
      {
        handler.onSpyEvent( event );
      }
      catch ( final Throwable error )
      {
        final String message =
          ArezUtil.safeGetString( () ->
                                    String.format( "Exception when notifying spy handler '%s' of '%s' event.",
                                                   String.valueOf( handler ),
                                                   String.valueOf( event ) ) );
        ArezLogger.log( message, error );
      }
    }
  }

  @TestOnly
  @Nonnull
  ArrayList<SpyEventHandler> getSpyEventHandlers()
  {
    return _spyEventHandlers;
  }
}
