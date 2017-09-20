package org.realityforge.arez.browser.extras;

import elemental2.dom.DomGlobal;
import elemental2.dom.EventListener;
import javax.annotation.Nonnull;
import org.realityforge.arez.Unsupported;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.OnActivate;
import org.realityforge.arez.annotations.OnDeactivate;

@Unsupported( "This is still considered experimental and will likely evolve over time" )
@Container( singleton = true )
public class NetworkStatus
{
  private final EventListener _listener;
  private boolean _rawOnLine;

  @Nonnull
  public static NetworkStatus create()
  {
    return new Arez_NetworkStatus();
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

  @OnActivate
  final void onOnLineActivate()
  {
    DomGlobal.window.addEventListener( "online", _listener );
    DomGlobal.window.addEventListener( "offline", _listener );
  }

  @OnDeactivate
  final void onOnLineDeactivate()
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
  void updateOnlineStatus()
  {
    setRawOnLine( DomGlobal.navigator.onLine );
  }
}
