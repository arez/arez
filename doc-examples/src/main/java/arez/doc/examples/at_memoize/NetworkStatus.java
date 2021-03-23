package arez.doc.examples.at_memoize;

import akasha.EventListener;
import akasha.Global;
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
    Global.addOnlineListener( _listener );
    Global.addOfflineListener( _listener );
  }

  @OnDeactivate
  void onOnLineDeactivate()
  {
    Global.removeOnlineListener( _listener );
    Global.removeOfflineListener( _listener );
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
    return Global.navigator().onLine();
  }
}
