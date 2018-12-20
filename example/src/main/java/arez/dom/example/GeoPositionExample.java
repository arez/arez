package arez.dom.example;

import arez.Arez;
import arez.dom.GeoPosition;
import arez.dom.Position;
import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import jsinterop.base.Js;

public class GeoPositionExample
  implements EntryPoint
{
  public void onModuleLoad()
  {
    final GeoPosition geoPosition = GeoPosition.create();

    DomGlobal.document.querySelector( "#watch" ).onclick = e -> {
      Arez.context().observer( () -> {
        final int status = geoPosition.getStatus();
        final String errorMessage = geoPosition.getErrorMessage();
        final Position position = geoPosition.getPosition();
        Js.debugger();
        DomGlobal.document.querySelector( "#status" ).textContent =
          "GeoPosition: " + status +
          ( null != position ? " Position: " + position.getLatitude() + ", " + position.getLongitude() : "" ) +
          ( null != errorMessage ? " ErrorMessage: " + errorMessage : "" );
      } );
      return null;
    };

  }
}
