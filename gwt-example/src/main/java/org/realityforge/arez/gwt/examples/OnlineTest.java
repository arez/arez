package org.realityforge.arez.gwt.examples;

import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import elemental2.dom.Element;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.browser.extras.IdleStatus;
import org.realityforge.arez.browser.extras.NetworkStatus;

public class OnlineTest
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final ArezContext context = new ArezContext();
    ExampleUtil.logAllErrors( context );
    context.getSpy().addSpyEventHandler( SpyUtil::emitEvent );

    final NetworkStatus networkStatus = NetworkStatus.create();
    final IdleStatus idleStatus = IdleStatus.create();

    context.autorun( "Status Printer", false, () -> printNetworkStatus( networkStatus ), true );
    context.autorun( "IDLE Status Printer", false, () -> printIdleStatus( idleStatus ), true );
  }

  private void printIdleStatus( @Nonnull final IdleStatus idleStatus )
  {
    final String message = "Interaction Status: " + ( idleStatus.isIdle() ? "Idle" : "Active" );
    final Element element = DomGlobal.document.querySelector( "#idle" );
    element.textContent = message;
    DomGlobal.console.log( message );
  }

  private void printNetworkStatus( @Nonnull final NetworkStatus networkStatus )
  {
    final String message = "Network Status: " + ( networkStatus.isOnLine() ? "Online" : "Offline" );
    DomGlobal.console.log( message );
    final Element element = DomGlobal.document.querySelector( "#network" );
    element.textContent = message;
  }
}
