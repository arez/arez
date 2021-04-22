package arez.dom;

import akasha.AddEventListenerOptions;
import akasha.EventListener;
import akasha.WindowGlobal;
import akasha.TimerHandler;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import arez.annotations.PostConstruct;
import arez.annotations.PreDispose;
import java.util.Arrays;
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
 * import akasha.Console;
 * import arez.Arez;
 * import arez.dom.IdleStatus;
 *
 * public class IdleStatusExample
 *   implements EntryPoint
 * {
 *   public void onModuleLoad()
 *   {
 *     final IdleStatus idleStatus = IdleStatus.create();
 *     Arez.context().autorun( () -> {
 *       final String message = "Interaction Status: " + ( idleStatus.isIdle() ? "Idle" : "Active" );
 *       Console.log( message );
 *     } );
 *   }
 * }
 * }</pre>
 */
@ArezComponent( requireId = Feature.DISABLE )
public abstract class IdleStatus
{
  private static final long DEFAULT_TIMEOUT = 2000L;
  @Nonnull
  private final TimerHandler _timeoutCallback = this::onTimeout;
  @Nonnull
  private final EventListener _listener = e -> resetLastActivityTime();
  @Nonnull
  private Set<String> _events =
    new HashSet<>( Arrays.asList( "keydown", "touchstart", "scroll", "mousemove", "mouseup", "mousedown", "wheel" ) );
  /**
   * True if an Observer is watching idle state.
   */
  private boolean _active;
  /**
   * The id of timeout scheduled action, 0 if none set.
   */
  private int _timeoutId;

  /**
   * Create an instance of this model.
   *
   * @return an instance of IdleStatus.
   */
  @Nonnull
  public static IdleStatus create()
  {
    return create( DEFAULT_TIMEOUT );
  }

  /**
   * Create an instance of this model.
   *
   * @param timeout the duration to after activity before becoming idle.
   * @return an instance of IdleStatus.
   */
  @Nonnull
  public static IdleStatus create( final long timeout )
  {
    return new Arez_IdleStatus( timeout );
  }

  IdleStatus()
  {
  }

  @PostConstruct
  void postConstruct()
  {
    resetLastActivityTime();
  }

  @PreDispose
  void preDispose()
  {
    cancelTimeout();
  }

  /**
   * Return true if the user is idle.
   *
   * @return true if the user is idle, false otherwise.
   */
  @Memoize
  public boolean isIdle()
  {
    if ( isRawIdle() )
    {
      return true;
    }
    else
    {
      final int timeToWait = getTimeToWait();
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
  void onIdleActivate()
  {
    _active = true;
    _events.forEach( e -> WindowGlobal.addEventListener( e, _listener, AddEventListenerOptions.create().passive( true ) ) );
  }

  @OnDeactivate
  void onIdleDeactivate()
  {
    _active = false;
    _events.forEach( e -> WindowGlobal.removeEventListener( e, _listener ) );
  }

  /**
   * Short cut observable field checked after idle state is confirmed.
   */
  @Observable
  abstract void setRawIdle( boolean rawIdle );

  abstract boolean isRawIdle();

  /**
   * Return the duration for which no events should be received for the idle condition to be triggered.
   *
   * @return the timeout.
   */
  @Observable( initializer = Feature.ENABLE )
  public abstract long getTimeout();

  /**
   * Set the timeout.
   *
   * @param timeout the timeout.
   */
  public abstract void setTimeout( long timeout );

  /**
   * Return the set of events to listen to.
   *
   * @return the set of events.
   */
  @Nonnull
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
    _events = new HashSet<>( events );
    updateListeners( oldEvents );
  }

  /**
   * Synchronize listeners against the dom based on new events.
   */
  private void updateListeners( @Nonnull final Set<String> oldEvents )
  {
    if ( _active )
    {
      //Remove any old events
      oldEvents.stream().
        filter( e -> !_events.contains( e ) ).
        forEach( e -> WindowGlobal.removeEventListener( e, _listener ) );
      // Add any new events
      _events.stream().
        filter( e -> !oldEvents.contains( e ) ).
        forEach( e -> WindowGlobal.addEventListener( e, _listener ) );
    }
  }

  /**
   * Return the time at which the last monitored event was received.
   *
   * @return the time at which the last event was received.
   */
  @Observable
  public abstract long getLastActivityAt();

  abstract void setLastActivityAt( long lastActivityAt );

  private int getTimeToWait()
  {
    return (int) ( getLastActivityAt() + getTimeout() - System.currentTimeMillis() );
  }

  private void cancelTimeout()
  {
    WindowGlobal.clearTimeout( _timeoutId );
    _timeoutId = 0;
  }

  private void scheduleTimeout( final int timeToWait )
  {
    _timeoutId = WindowGlobal.setTimeout( _timeoutCallback, timeToWait );
  }

  @Action
  void onTimeout()
  {
    _timeoutId = 0;
    final int timeToWait = getTimeToWait();
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
