package arez.doc.examples.at_memoize;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import elemental2.dom.DomGlobal;
import elemental2.dom.EventListener;

@ArezComponent
public abstract class NetworkStatus
{
  private final EventListener _listener = e -> updateOnlineStatus();
  private boolean _rawOnLine = DomGlobal.navigator.onLine;

  @Memoize
  public boolean isOnLine()
  {
    return isRawOnLine();
  }

  @OnActivate
  void onOnLineActivate()
  {
    DomGlobal.window.addEventListener( "online", _listener );
    DomGlobal.window.addEventListener( "offline", _listener );
  }

  @OnDeactivate
  void onOnLineDeactivate()
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
    //Updating the observable will force @Memoize method to recalculate
    setRawOnLine( DomGlobal.navigator.onLine );
  }
}
