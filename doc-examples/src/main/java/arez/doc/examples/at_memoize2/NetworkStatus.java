package arez.doc.examples.at_memoize2;

import akasha.EventListener;
import akasha.WindowGlobal;
import arez.ComputableValue;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.DepType;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;

@ArezComponent
public abstract class NetworkStatus
{
  private final EventListener _listener = e -> updateOnlineStatus();

  // Specify depType so can explicitly trigger a recalculation
  // of method using reportPossiblyChanged()
  @Memoize( depType = DepType.AREZ_OR_EXTERNAL )
  public boolean isOnLine()
  {
    return WindowGlobal.navigator().onLine();
  }

  @ComputableValueRef
  abstract ComputableValue<Boolean> getOnLineComputableValue();

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

  @Action
  void updateOnlineStatus()
  {
    // Explicitly trigger a recalculation of the OnLine value
    getOnLineComputableValue().reportPossiblyChanged();
  }
}
