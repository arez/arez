package org.realityforge.arez.gwt.examples;

import elemental2.dom.DomGlobal;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container( singleton = true )
public class NetworkStatus
{
  private boolean _onLine;

  @Observable
  public boolean isOnLine()
  {
    return _onLine;
  }

  public void setOnLine( final boolean onLine )
  {
    _onLine = onLine;
  }

  @Action
  public void updateOnlineStatus()
  {
    setOnLine( DomGlobal.navigator.onLine );
  }
}
