package org.realityforge.arez.gwt.examples;

import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import org.realityforge.arez.Arez;
import org.realityforge.arez.browser.extras.NetworkStatus;

public class NetworkStatusExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final NetworkStatus networkStatus = NetworkStatus.create();
    Arez.context().autorun( () ->
                              DomGlobal.document.querySelector( "#network" ).textContent =
                                "Network Status: " + ( networkStatus.isOnLine() ? "Online" : "Offline" ) );
  }
}
