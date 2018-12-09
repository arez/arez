package arez.windowsize.example;

import arez.Arez;
import arez.windowsize.Dimension;
import arez.windowsize.EventDrivenValue;
import arez.windowsize.WindowSize;
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
