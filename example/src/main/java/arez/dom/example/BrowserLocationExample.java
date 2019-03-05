package arez.dom.example;

import arez.Arez;
import arez.ArezContext;
import arez.Flags;
import arez.dom.BrowserLocation;
import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import javax.annotation.Nonnull;

public class BrowserLocationExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final BrowserLocation browserLocation = BrowserLocation.create();

    final ArezContext context = Arez.context();
    context.observer( () -> cleanLocation( browserLocation ), Flags.READ_WRITE | Flags.NESTED_ACTIONS_ALLOWED );
    context.observer( () -> printBrowserLocation( browserLocation ) );

    DomGlobal.document.querySelector( "#route_base" ).
      addEventListener( "click", e -> browserLocation.changeLocation( "" ) );
    DomGlobal.document.querySelector( "#route_slash" ).
      addEventListener( "click", e -> browserLocation.changeLocation( "/" ) );
    DomGlobal.document.querySelector( "#route_event" ).
      addEventListener( "click", e -> browserLocation.changeLocation( "/event" ) );
    DomGlobal.document.querySelector( "#route_other" ).
      addEventListener( "click", e -> browserLocation.changeLocation( "/other" ) );
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
    DomGlobal.document.querySelector( selector ).textContent = message;
    DomGlobal.console.log( message );
  }
}
