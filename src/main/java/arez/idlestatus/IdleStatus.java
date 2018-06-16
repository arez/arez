package arez.idlestatus;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Observable;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import arez.annotations.PostConstruct;
import arez.annotations.PreDispose;
import elemental2.dom.DomGlobal;
import elemental2.dom.EventListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;

/**
 * An observable model that declares state that tracks when the user is "idle".
 * A user is considered idle if they have not interacted with the browser
 * for a specified amount of time.
 *
 * <p>Application code can observe the idle state via accessing {@link #isIdle()}.
 * Typically this is done in a tracking transaction such as those defined by autorun.</p>
 *
 * <p>The "amount of time" is defined by the Observable value "timeout" accessible via
 * {@link #getTimeout()} and mutable via {@link #setTimeout(long)}.</p>
 *
 * <p>The "not interacted with the browser" is detected by listening for interaction
 * events on the browser. The list of events that the model listens for is controlled via
 * {@link #getEvents()} and {@link #setEvents(Set)}. It should be noted that if
 * there is no observer observing the idle state then the model will remove listeners
 * so as not to have any significant performance impact.</p>
 *
 * <h1>A very simple example</h1>
 * <pre>{@code
 * import com.google.gwt.core.client.EntryPoint;
 * import elemental2.dom.DomGlobal;
 * import arez.Arez;
 * import arez.idlestatus.IdleStatus;
 *
 * public class IdleStatusExample
 *   implements EntryPoint
 * {
 *   public void onModuleLoad()
 *   {
 *     final IdleStatus idleStatus = IdleStatus.create();
 *     Arez.context().autorun( () -> {
 *       final String message = "Interaction Status: " + ( idleStatus.isIdle() ? "Idle" : "Active" );
 *       DomGlobal.console.log( message );
 *     } );
 *   }
 * }
 * }</pre>
 */
@ArezComponent( nameIncludesId = false )
public abstract class IdleStatus
{
  private static final long DEFAULT_TIMEOUT = 2000L;
  private final EventListener _listener = e -> resetLastActivityTime();
  private Set<String> _events =
    new HashSet<>( Arrays.asList( "keydown", "touchstart", "scroll", "mousemove", "mouseup", "mousedown", "wheel" ) );
  private final DomGlobal.SetTimeoutCallbackFn _timeoutCallback = e -> onTimeout();

  /**
   * The time at which the last event was received.
   */
  private long _lastActivityAt;
  /**
   * The duration for which no events should be received for the idle condition to be triggered.
   */
  private long _timeout = DEFAULT_TIMEOUT;
  /**
   * True if an Observer is watching idle state.
   */
  private boolean _active;
  /**
   * Short cut observable field checked after idle state is confirmed.
   */
  private boolean _rawIdle;
  /**
   * The id of timeout scheduled action, 0 if none set.
   */
  private double _timeoutId;

  /**
   * Create an instance of this model.
   *
   * @return an instance of IdleStatus.
   */
  public static IdleStatus create()
  {
    return new Arez_IdleStatus();
  }

  IdleStatus()
  {
  }

  @PostConstruct
  final void postConstruct()
  {
    resetLastActivityTime();
  }

  @PreDispose
  final void preDispose()
  {
    cancelTimeout();
  }

  /**
   * Return true if the user is idle.
   *
   * @return true if the user is idle, false otherwise.
   */
  @Computed
  public boolean isIdle()
  {
    if ( isRawIdle() )
    {
      return true;
    }
    else
    {
      final long timeToWait = getTimeToWait();
      if ( timeToWait > 0 )
      {
        if ( 0 == _timeoutId )
        {
          scheduleTimeout( timeToWait );
        }
        return false;
      }
      else
      {
        return true;
      }
    }
  }

  @OnActivate
  final void onIdleActivate()
  {
    _active = true;
    _events.forEach( e -> DomGlobal.window.addEventListener( e, _listener ) );
  }

  @OnDeactivate
  final void onIdleDeactivate()
  {
    _active = false;
    _events.forEach( e -> DomGlobal.window.removeEventListener( e, _listener ) );
  }

  @Observable
  void setRawIdle( final boolean rawIdle )
  {
    _rawIdle = rawIdle;
  }

  boolean isRawIdle()
  {
    return _rawIdle;
  }

  /**
   * Return the timeout.
   *
   * @return the timeout
   */
  @Observable
  public long getTimeout()
  {
    return _timeout;
  }

  /**
   * Set the timeout.
   *
   * @param timeout the timeout.
   */
  public void setTimeout( final long timeout )
  {
    _timeout = timeout;
  }

  /**
   * Return the set of events to listen to.
   *
   * @return the set of events.
   */
  @Observable
  public Set<String> getEvents()
  {
    return _events;
  }

  /**
   * Specify the set of events to listen to.
   * If the model is already active, the listeners will be updated to reflect the new events.
   *
   * @param events the set of events.
   */
  public void setEvents( @Nonnull final Set<String> events )
  {
    final Set<String> oldEvents = _events;
    final HashSet<String> newEvents = new HashSet<>();
    newEvents.addAll( events );
    _events = Collections.unmodifiableSet( newEvents );
    updateListeners( oldEvents, events );
  }

  /**
   * Synchronize listeners against the dom based on new events.
   */
  private void updateListeners( @Nonnull final Set<String> oldEvents, @Nonnull final Set<String> events )
  {
    if ( _active )
    {
      //Remove any old events
      oldEvents.stream().
        filter( e -> !events.contains( e ) ).
        forEach( e -> DomGlobal.window.removeEventListener( e, _listener ) );
      // Add any new events
      _events.stream().
        filter( e -> !oldEvents.contains( e ) ).
        forEach( e -> DomGlobal.window.addEventListener( e, _listener ) );
    }
  }

  /**
   * Return the time at which the last monitored event was received.
   *
   * @return the time at which the last event was received.
   */
  @Observable
  public long getLastActivityAt()
  {
    return _lastActivityAt;
  }

  void setLastActivityAt( final long lastActivityAt )
  {
    _lastActivityAt = lastActivityAt;
  }

  private long getIdleTimeout()
  {
    return getLastActivityAt() + getTimeout();
  }

  private long getTimeToWait()
  {
    return getIdleTimeout() - System.currentTimeMillis();
  }

  private void cancelTimeout()
  {
    DomGlobal.clearTimeout( _timeoutId );
    _timeoutId = 0;
  }

  private void scheduleTimeout( final long timeToWait )
  {
    _timeoutId = DomGlobal.setTimeout( _timeoutCallback, timeToWait );
  }

  @Action
  void onTimeout()
  {
    _timeoutId = 0;
    final long timeToWait = getTimeToWait();
    if ( timeToWait > 0 )
    {
      scheduleTimeout( timeToWait );
    }
    else
    {
      setRawIdle( true );
    }
  }

  @Action
  void resetLastActivityTime()
  {
    setRawIdle( false );
    setLastActivityAt( System.currentTimeMillis() );
  }
}
