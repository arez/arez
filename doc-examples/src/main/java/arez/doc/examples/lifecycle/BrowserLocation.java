package arez.doc.examples.lifecycle;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.PostConstruct;
import arez.annotations.PreDispose;
import elemental2.dom.DomGlobal;
import elemental2.dom.Event;
import elemental2.dom.EventListener;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class BrowserLocation
{
  private final EventListener _listener = this::onHashChangeEvent;
  //DOC ELIDE START
  //DOC ELIDE END

  @PostConstruct
  void postConstruct()
  {
    DomGlobal.window.addEventListener( "hashchange", _listener, false );
    //DOC ELIDE START
    //DOC ELIDE END
  }

  @PreDispose
  void preDispose()
  {
    DomGlobal.window.removeEventListener( "hashchange", _listener, false );
  }

  //DOC ELIDE START
  @Action
  void onHashChangeEvent( @Nonnull final Event e )
  {
  }
  //DOC ELIDE END
}
