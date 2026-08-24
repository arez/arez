package arez.doc.examples.lifecycle;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.PostConstruct;
import arez.annotations.PreDispose;
import javax.annotation.Nonnull;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;

@ArezComponent
public abstract class BrowserLocation
{
  //DOC ELIDE START
  @JsFunction
  private interface HashChangeEventListener
  {
    void handleEvent( Object event );
  }

  private final HashChangeEventListener _listener = this::onHashChangeEvent;
  //DOC ELIDE END

  @PostConstruct
  void postConstruct()
  {
    addEventListener( "hashchange", _listener, false );
    //DOC ELIDE START
    //DOC ELIDE END
  }

  @PreDispose
  void preDispose()
  {
    removeEventListener( "hashchange", _listener, false );
  }

  //DOC ELIDE START
  @Action
  void onHashChangeEvent( @Nonnull final Object e )
  {
  }

  @JsMethod( name = "addEventListener", namespace = JsPackage.GLOBAL )
  private static native void addEventListener( String type, HashChangeEventListener listener, boolean capture );

  @JsMethod( name = "removeEventListener", namespace = JsPackage.GLOBAL )
  private static native void removeEventListener( String type, HashChangeEventListener listener, boolean capture );
  //DOC ELIDE END
}
