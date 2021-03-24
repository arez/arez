package arez.dom.example;

import arez.Arez;
import arez.dom.NetworkStatus;
import com.google.gwt.core.client.EntryPoint;
import akasha.Global;

public class NetworkStatusExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final NetworkStatus networkStatus = NetworkStatus.create();
    Arez.context().observer( () ->
                               Global.document().querySelector( "#network" ).textContent =
                                 "Network Status: " + ( networkStatus.isOnLine() ? "Online" : "Offline" ) );
  }
}
