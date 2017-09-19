package org.realityforge.arez.gwt.examples;

import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import elemental2.dom.Element;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.browser.extras.BrowserLocation;
import org.realityforge.arez.browser.extras.IdleStatus;
import org.realityforge.arez.browser.extras.NetworkStatus;

public class OnlineTest
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final ArezContext context = Arez.context();
    ExampleUtil.logAllErrors( context );
    context.getSpy().addSpyEventHandler( SpyUtil::emitEvent );

    final NetworkStatus networkStatus = NetworkStatus.create();
    final IdleStatus idleStatus = IdleStatus.create();
    final BrowserLocation browserLocation = BrowserLocation.create();

    context.autorun( "Status Printer", false, () -> printNetworkStatus( networkStatus ), true );
    context.autorun( "IDLE Status Printer", false, () -> printIdleStatus( idleStatus ), true );

    context.autorun( "Location Cleaner", true, () -> cleanLocation( browserLocation ), true );
    context.autorun( "Location Printer", false, () -> printBrowserLocation( browserLocation ), true );

    DomGlobal.document.querySelector( "#route_base" ).
      addEventListener( "click", e -> browserLocation.changeLocation( "" ) );
    DomGlobal.document.querySelector( "#route_event" ).
      addEventListener( "click", e -> browserLocation.changeLocation( "/event" ) );
    DomGlobal.document.querySelector( "#route_other" ).
      addEventListener( "click", e -> browserLocation.changeLocation( "/other" ) );
  }

  private void cleanLocation( @Nonnull final BrowserLocation l )
  {
    final String browserLocation = l.getBrowserLocation();
    final String location = l.getLocation();
    if ( "/event".equals( browserLocation ) ||
         "/other".equals( browserLocation ) ||
         "/".equals( browserLocation ) ||
         "".equals( browserLocation ) )
    {
      l.changeLocation( browserLocation );
    }
    else if ( "/event".equals( location ) ||
              "/other".equals( location ) ||
              "/".equals( location ) ||
              "".equals( location ) )
    {
      l.resetBrowserLocation();
    }
    else
    {
      l.changeLocation( "" );
    }
  }

  private void printBrowserLocation( @Nonnull final BrowserLocation browserLocation )
  {
    final String message =
      "Browser Location: " + browserLocation.getBrowserLocation() + ", App Location: " + browserLocation.getLocation();
    final Element element = DomGlobal.document.querySelector( "#location" );
    element.textContent = message;
    DomGlobal.console.log( message );
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
    final Element element = DomGlobal.document.querySelector( "#network" );
    element.textContent = message;
    DomGlobal.console.log( message );
  }
}
