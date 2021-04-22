package arez.doc.examples.at_memoize;

import akasha.EventListener;
import akasha.WindowGlobal;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;

@ArezComponent
public abstract class NetworkStatus
{
  private final EventListener _listener = e -> updateOnlineStatus();
  private boolean _rawOnLine = getIsOnLine();

  @Memoize
  public boolean isOnLine()
  {
    return isRawOnLine();
  }

  @OnActivate
  void onOnLineActivate()
  {
    WindowGlobal.addOnlineListener( _listener );
    WindowGlobal.addOfflineListener( _listener );
  }

  @OnDeactivate
  void onOnLineDeactivate()
  {
    WindowGlobal.removeOnlineListener( _listener );
    WindowGlobal.removeOfflineListener( _listener );
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
    setRawOnLine( getIsOnLine() );
  }

  private boolean getIsOnLine()
  {
    return WindowGlobal.navigator().onLine();
  }
}
