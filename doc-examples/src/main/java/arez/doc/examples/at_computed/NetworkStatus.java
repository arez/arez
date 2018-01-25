package arez.doc.examples.at_computed;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Observable;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import elemental2.dom.DomGlobal;
import elemental2.dom.EventListener;

@ArezComponent
public abstract class NetworkStatus
{
  private final EventListener _listener;
  private boolean _rawOnLine;

  public NetworkStatus()
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
    //Updating the observable will force @Computed method to recalculate
    setRawOnLine( DomGlobal.navigator.onLine );
  }
}
