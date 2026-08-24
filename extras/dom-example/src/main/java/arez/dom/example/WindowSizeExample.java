package arez.dom.example;

import arez.Arez;
import arez.dom.Dimension;
import arez.dom.EventDrivenValue;
import arez.dom.WindowSize;
import com.google.gwt.core.client.EntryPoint;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

public class WindowSizeExample
  implements EntryPoint
{
  @JsType( isNative = true, name = "Element", namespace = JsPackage.GLOBAL )
  private static class Element
  {
    @JsProperty
    native void setTextContent( String text );
  }

  @JsType( isNative = true, name = "Document", namespace = JsPackage.GLOBAL )
  private static class Document
  {
    @JsMethod
    native Element querySelector( String selector );
  }

  public void onModuleLoad()
  {
    final EventDrivenValue<WindowSize.Window, Dimension> inner = WindowSize.inner( window() );

    Arez.context().observer( () -> {
      final Dimension dimension = inner.getValue();
      document().querySelector( "#status" ).setTextContent(
        "Screen size: " + dimension.getWidth() + " x " + dimension.getHeight() );
    } );
  }

  @JsProperty( name = "window", namespace = JsPackage.GLOBAL )
  private static native WindowSize.Window window();

  @JsProperty( name = "document", namespace = JsPackage.GLOBAL )
  private static native Document document();
}
