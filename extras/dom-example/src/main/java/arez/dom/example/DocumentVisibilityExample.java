package arez.dom.example;

import arez.Arez;
import arez.dom.DocumentVisibility;
import com.google.gwt.core.client.EntryPoint;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

public class DocumentVisibilityExample
  implements EntryPoint
{
  @JsType( isNative = true, name = "globalThis.console", namespace = JsPackage.GLOBAL )
  private static final class NativeConsole
  {
    @JsMethod
    private static native void log( Object message );
  }

  public void onModuleLoad()
  {
    final DocumentVisibility v = DocumentVisibility.create();
    Arez.context().observer( () -> NativeConsole.log( "Document Visibility: " + v.getVisibility() ) );
  }
}
