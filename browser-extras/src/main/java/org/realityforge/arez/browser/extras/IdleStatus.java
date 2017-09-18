package org.realityforge.arez.browser.extras;

import elemental2.dom.DomGlobal;
import elemental2.dom.EventListener;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.OnActivate;
import org.realityforge.arez.annotations.OnDeactivate;
import org.realityforge.arez.annotations.PreDispose;

@Container( singleton = true )
public class IdleStatus
{
  private static final long DEFAULT_TIMEOUT = 2000L;
  private final EventListener _listener = e -> resetLastActivityTime();
  private Collection<String> _events =
    Arrays.asList( "keydown", "touchstart", "scroll", "mousemove", "mouseup", "mousedown", "wheel" );
  private final DomGlobal.SetTimeoutCallbackFn _timeoutCallback = e -> onTimeout();

  private long _lastActivityAt;
  private long _timeout = DEFAULT_TIMEOUT;
  private boolean _active;
  private boolean _rawIdle;
  private double _timeoutId;

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

  @PreDispose
  final void onDispose()
  {
    cancelTimeout();
  }

  @Observable
  public Collection<String> getEvents()
  {
    return _events;
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

  @Observable
  public long getTimeout()
  {
    return _timeout;
  }

  public void setTimeout( final long timeout )
  {
    _timeout = timeout;
  }

  private long getIdleTimeout()
  {
    return getLastActivityAt() + getTimeout();
  }

  @Observable
  void setEvents( @Nonnull final Collection<String> events )
  {
    final Collection<String> oldEvents = _events;
    _events = Arrays.asList( events.toArray( new String[ events.size() ] ) );
    updateListeners( oldEvents, events );
  }

  private void updateListeners( @Nonnull final Collection<String> oldEvents, @Nonnull final Collection<String> events )
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

  @Observable
  public long getLastActivityAt()
  {
    return _lastActivityAt;
  }

  void setLastActivityAt( final long lastActivityAt )
  {
    _lastActivityAt = lastActivityAt;
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
