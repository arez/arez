package arez.dom.example;

import akasha.Global;
import akasha.HTMLElement;
import akasha.WindowGlobal;
import arez.Arez;
import arez.dom.GeoPosition;
import arez.dom.Position;
import com.google.gwt.core.client.EntryPoint;
import jsinterop.base.Js;

public class GeoPositionExample
  implements EntryPoint
{
  public void onModuleLoad()
  {
    final GeoPosition geoPosition = GeoPosition.create();

    ( (HTMLElement) WindowGlobal.document().querySelector( "#watch" ) ).onclick = e -> {
      Arez.context().observer( () -> {
        final int status = geoPosition.getStatus();
        final String errorMessage = geoPosition.getErrorMessage();
        final Position position = geoPosition.getPosition();
        Js.debugger();
        WindowGlobal.document().querySelector( "#status" ).textContent =
          "GeoPosition: " + status +
          ( null != position ? " Position: " + position.getLatitude() + ", " + position.getLongitude() : "" ) +
          ( null != errorMessage ? " ErrorMessage: " + errorMessage : "" );
      } );
    };
  }
}
