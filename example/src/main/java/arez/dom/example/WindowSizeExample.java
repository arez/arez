package arez.dom.example;

import arez.Arez;
import arez.dom.Dimension;
import arez.dom.EventDrivenValue;
import arez.dom.WindowSize;
import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import elemental2.dom.Window;

public class WindowSizeExample
  implements EntryPoint
{
  public void onModuleLoad()
  {
    final EventDrivenValue<Window, Dimension> inner = WindowSize.inner( DomGlobal.window );

    Arez.context().observer( () -> {
      final Dimension dimension = inner.getValue();
      DomGlobal.document.querySelector( "#status" ).textContent =
        "Screen size: " + dimension.getWidth() + " x " + dimension.getHeight();
    } );
  }
}
