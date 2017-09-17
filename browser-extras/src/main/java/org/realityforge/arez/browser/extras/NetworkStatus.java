package org.realityforge.arez.browser.extras;

import elemental2.dom.DomGlobal;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container( singleton = true )
public class NetworkStatus
{
  private boolean _rawOnLine;

  public static NetworkStatus create( @Nonnull final ArezContext context )
  {
    return new Arez_NetworkStatus( Objects.requireNonNull( context ) );
  }

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
