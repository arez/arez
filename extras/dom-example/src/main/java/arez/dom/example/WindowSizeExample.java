package arez.dom.example;

import akasha.WindowGlobal;
import arez.Arez;
import arez.dom.Dimension;
import arez.dom.EventDrivenValue;
import arez.dom.WindowSize;
import com.google.gwt.core.client.EntryPoint;
import akasha.Global;
import akasha.Window;

public class WindowSizeExample
  implements EntryPoint
{
  public void onModuleLoad()
  {
    final EventDrivenValue<Window, Dimension> inner = WindowSize.inner( WindowGlobal.window() );

    Arez.context().observer( () -> {
      final Dimension dimension = inner.getValue();
      WindowGlobal.document().querySelector( "#status" ).textContent =
        "Screen size: " + dimension.getWidth() + " x " + dimension.getHeight();
    } );
  }
}
