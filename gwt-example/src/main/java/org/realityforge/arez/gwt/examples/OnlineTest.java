package org.realityforge.arez.gwt.examples;

import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import elemental2.dom.Element;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.browser.extras.NetworkStatus;

public class OnlineTest
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final ArezContext context = new ArezContext();

    final NetworkStatus networkStatus = NetworkStatus.create( context );

    context.autorun( "Status Printer", false, () -> printNetworkStatus( networkStatus ), true );
  }

  private void printNetworkStatus( @Nonnull final NetworkStatus networkStatus )
  {
    final String message = "Network Status: " + ( networkStatus.isOnLine() ? "Online" : "Offline" );
    DomGlobal.console.log( message );
    final Element element = DomGlobal.document.querySelector( "#app" );
    element.textContent = message;
  }
}
