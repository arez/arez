package arez.dom.example;

import akasha.Console;
import akasha.WindowGlobal;
import arez.Arez;
import arez.ArezContext;
import arez.Observer;
import arez.dom.BrowserLocation;
import com.google.gwt.core.client.EntryPoint;
import javax.annotation.Nonnull;

public class BrowserLocationExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final BrowserLocation browserLocation = BrowserLocation.create();

    final ArezContext context = Arez.context();
    context.observer( () -> cleanLocation( browserLocation ),
                      Observer.Flags.READ_WRITE | Observer.Flags.NESTED_ACTIONS_ALLOWED );
    context.observer( () -> printBrowserLocation( browserLocation ) );

    WindowGlobal.document().querySelector( "#route_base" ).
      addClickListener( e -> browserLocation.changeLocation( "" ) );
    WindowGlobal.document().querySelector( "#route_slash" ).
      addClickListener( e -> browserLocation.changeLocation( "/" ) );
    WindowGlobal.document().querySelector( "#route_event" ).
      addClickListener( e -> browserLocation.changeLocation( "/event" ) );
    WindowGlobal.document().querySelector( "#route_other" ).
      addClickListener( e -> browserLocation.changeLocation( "/other" ) );
  }

  private void cleanLocation( @Nonnull final BrowserLocation l )
  {
    final String browserLocation = l.getBrowserLocation();
    if ( isValid( browserLocation ) )
    {
      l.changeLocation( browserLocation );
    }
    else if ( isValid( l.getLocation() ) )
    {
      l.resetBrowserLocation();
    }
    else
    {
      l.changeLocation( "" );
    }
  }

  private boolean isValid( @Nonnull final String location )
  {
    return "/event".equals( location ) ||
           "/other".equals( location ) ||
           "/".equals( location ) ||
           "".equals( location );
  }

  private void printBrowserLocation( @Nonnull final BrowserLocation browserLocation )
  {
    emitLocation( "#browser_location", "Browser Location: " + browserLocation.getBrowserLocation() );
    emitLocation( "#app_location", "Application Location: " + browserLocation.getLocation() );
  }

  private void emitLocation( @Nonnull final String selector, @Nonnull final String message )
  {
    WindowGlobal.document().querySelector( selector ).textContent = message;
    Console.log( message );
  }
}
