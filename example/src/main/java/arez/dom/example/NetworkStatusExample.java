package arez.dom.example;

import arez.Arez;
import arez.dom.NetworkStatus;
import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;

public class NetworkStatusExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final NetworkStatus networkStatus = NetworkStatus.create();
    Arez.context().observer( () ->
                               DomGlobal.document.querySelector( "#network" ).textContent =
                                 "Network Status: " + ( networkStatus.isOnLine() ? "Online" : "Offline" ) );
  }
}
