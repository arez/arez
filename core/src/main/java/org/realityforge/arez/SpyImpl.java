package org.realityforge.arez;

import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.TestOnly;

/**
 * Class supporting the propagation of events to SpyEventHandler callbacks.
 */
final class SpyImpl
  implements Spy
{
  /**
   * The list of spy handlers to call when an event is received.
   */
  private final ArrayList<SpyEventHandler> _spyEventHandlers = new ArrayList<>();

  /**
   * {@inheritDoc}
   */
  @Override
  public void addSpyEventHandler( @Nonnull final SpyEventHandler handler )
  {
    Guards.invariant( () -> !_spyEventHandlers.contains( handler ),
                      () -> "Attempting to add handler " + handler +
                            " that is already in the list of spy handlers." );
    _spyEventHandlers.add( Objects.requireNonNull( handler ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeSpyEventHandler( @Nonnull final SpyEventHandler handler )
  {
    Guards.invariant( () -> _spyEventHandlers.contains( handler ),
                      () -> "Attempting to remove handler " + handler + " that is not in the list of spy handlers." );
    _spyEventHandlers.remove( Objects.requireNonNull( handler ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reportSpyEvent( @Nonnull final Object event )
  {
    Guards.invariant( this::willPropagateSpyEvents,
                      () -> String.format( "Attempting to report SpyEvent '%s' but willPropagateSpyEvents() " +
                                           "returns false.", String.valueOf( event ) ) );
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

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean willPropagateSpyEvents()
  {
    return ArezConfig.enableSpy() && !getSpyEventHandlers().isEmpty();
  }

  @TestOnly
  @Nonnull
  ArrayList<SpyEventHandler> getSpyEventHandlers()
  {
    return _spyEventHandlers;
  }
}
