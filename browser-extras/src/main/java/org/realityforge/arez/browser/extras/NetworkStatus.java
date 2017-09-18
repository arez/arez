package org.realityforge.arez.browser.extras;

import elemental2.dom.DomGlobal;
import elemental2.dom.EventListener;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.OnActivate;
import org.realityforge.arez.annotations.OnDeactivate;

@Container( singleton = true )
public class NetworkStatus
{
  private final EventListener _listener;
  private boolean _rawOnLine;

  public static NetworkStatus create( @Nonnull final ArezContext context )
  {
    return new Arez_NetworkStatus( Objects.requireNonNull( context ) );
  }

  NetworkStatus()
  {
    _listener = e -> updateOnlineStatus();
    _rawOnLine = DomGlobal.navigator.onLine;
  }

  @Computed
  public boolean isOnLine()
  {
    return isRawOnLine();
  }

  @OnActivate( name = "onLine" )
  public final void onActivate()
  {
    DomGlobal.window.addEventListener( "online", _listener );
    DomGlobal.window.addEventListener( "offline", _listener );
  }

  @OnDeactivate( name = "onLine" )
  public final void onDeactivate()
  {
    DomGlobal.window.removeEventListener( "online", _listener );
    DomGlobal.window.removeEventListener( "offline", _listener );
  }

  @Observable
  boolean isRawOnLine()
  {
    return _rawOnLine;
  }

  void setRawOnLine( final boolean rawOnLine )
  {
    _rawOnLine = rawOnLine;
  }

  @Action
  public void updateOnlineStatus()
  {
    setRawOnLine( DomGlobal.navigator.onLine );
  }
}
