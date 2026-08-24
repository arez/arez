package arez.doc.examples.at_memoize;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@ArezComponent
public abstract class NetworkStatus
{
  //DOC ELIDE START
  @JsFunction
  private interface EventListener
  {
    void handleEvent( Object event );
  }

  @JsType( isNative = true, name = "Navigator", namespace = JsPackage.GLOBAL )
  private static class Navigator
  {
    @JsProperty( name = "onLine" )
    native boolean onLine();
  }
  //DOC ELIDE END

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
    addEventListener( "online", _listener );
    addEventListener( "offline", _listener );
  }

  @OnDeactivate
  void onOnLineDeactivate()
  {
    removeEventListener( "online", _listener );
    removeEventListener( "offline", _listener );
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
    return navigator().onLine();
  }

  //DOC ELIDE START
  @JsProperty( name = "navigator", namespace = JsPackage.GLOBAL )
  private static native Navigator navigator();

  @JsMethod( name = "addEventListener", namespace = JsPackage.GLOBAL )
  private static native void addEventListener( String type, EventListener listener );

  @JsMethod( name = "removeEventListener", namespace = JsPackage.GLOBAL )
  private static native void removeEventListener( String type, EventListener listener );
  //DOC ELIDE END
}
