package org.realityforge.arez.gwt.examples;

import elemental2.dom.DomGlobal;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container( singleton = true )
public class NetworkStatus
{
  private boolean _rawOnLine;

  @Computed
  public boolean isOnLine()
  {
    return isRawOnLine();
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
