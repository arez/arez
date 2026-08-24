package arez.dom.example;

import arez.Arez;
import arez.dom.NetworkStatus;
import com.google.gwt.core.client.EntryPoint;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

public class NetworkStatusExample
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

  @Override
  public void onModuleLoad()
  {
    final NetworkStatus networkStatus = NetworkStatus.create();
    Arez.context().observer( () ->
                               document().querySelector( "#network" ).setTextContent(
                                 "Network Status: " + ( networkStatus.isOnLine() ? "Online" : "Offline" ) ) );
  }

  @JsProperty( name = "document", namespace = JsPackage.GLOBAL )
  private static native Document document();
}
