package arez.dom.example;

import arez.Arez;
import arez.dom.GeoPosition;
import arez.dom.Position;
import com.google.gwt.core.client.EntryPoint;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;

public class GeoPositionExample
  implements EntryPoint
{
  @JsFunction
  private interface EventListener
  {
    void handleEvent( Object event );
  }

  @JsType( isNative = true, name = "Element", namespace = JsPackage.GLOBAL )
  private static class Element
  {
    @JsMethod
    native void addEventListener( String type, EventListener listener );

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
    final GeoPosition geoPosition = GeoPosition.create();

    document().querySelector( "#watch" ).addEventListener( "click", e -> {
      Arez.context().observer( () -> {
        final int status = geoPosition.getStatus();
        final String errorMessage = geoPosition.getErrorMessage();
        final Position position = geoPosition.getPosition();
        Js.debugger();
        document().querySelector( "#status" ).setTextContent(
          "GeoPosition: " + status +
          ( null != position ? " Position: " + position.getLatitude() + ", " + position.getLongitude() : "" ) +
          ( null != errorMessage ? " ErrorMessage: " + errorMessage : "" ) );
      } );
    } );
  }

  @JsProperty( name = "document", namespace = JsPackage.GLOBAL )
  private static native Document document();
}
