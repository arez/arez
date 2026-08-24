package arez.doc.examples.at_memoize2;

import arez.ComputableValue;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.DepType;
import arez.annotations.Memoize;
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

  // Specify depType so can explicitly trigger a recalculation
  // of method using reportPossiblyChanged()
  @Memoize( depType = DepType.AREZ_OR_EXTERNAL )
  public boolean isOnLine()
  {
    return navigator().onLine();
  }

  @ComputableValueRef
  abstract ComputableValue<Boolean> getOnLineComputableValue();

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

  @Action
  void updateOnlineStatus()
  {
    // Explicitly trigger a recalculation of the OnLine value
    getOnLineComputableValue().reportPossiblyChanged();
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
