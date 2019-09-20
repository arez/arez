package arez;

import arez.spy.SpyEventHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import static org.realityforge.braincheck.Guards.*;

/**
 * Class supporting the propagation of events to SpyEventHandler callbacks.
 */
final class SpyEventHandlerSupport
{
  /**
   * The list of spy handlers to call when an event is received.
   */
  @Nonnull
  private final List<SpyEventHandler> _spyEventHandlers = new ArrayList<>();

  void addSpyEventHandler( @Nonnull final SpyEventHandler handler )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !_spyEventHandlers.contains( handler ),
                    () -> "Arez-0102: Attempting to add handler " + handler + " that is already " +
                          "in the list of spy handlers." );
    }
    _spyEventHandlers.add( Objects.requireNonNull( handler ) );
  }

  void removeSpyEventHandler( @Nonnull final SpyEventHandler handler )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> _spyEventHandlers.contains( handler ),
                    () -> "Arez-0103: Attempting to remove handler " + handler + " that is not " +
                          "in the list of spy handlers." );
    }
    _spyEventHandlers.remove( Objects.requireNonNull( handler ) );
  }

  void reportSpyEvent( @Nonnull final Object event )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( this::willPropagateSpyEvents,
                 () -> "Arez-0104: Attempting to report SpyEvent '" + event + "' but " +
                       "willPropagateSpyEvents() returns false." );
    }
    for ( final SpyEventHandler handler : _spyEventHandlers )
    {
      try
      {
        handler.onSpyEvent( event );
      }
      catch ( final Throwable error )
      {
        final String message =
          ArezUtil.safeGetString( () -> "Exception when notifying spy handler '" + handler + "' of '" +
                                        event + "' event." );
        ArezLogger.log( message, error );
      }
    }
  }

  boolean willPropagateSpyEvents()
  {
    return Arez.areSpiesEnabled() && !getSpyEventHandlers().isEmpty();
  }

  @Nonnull
  List<SpyEventHandler> getSpyEventHandlers()
  {
    return _spyEventHandlers;
  }
}
