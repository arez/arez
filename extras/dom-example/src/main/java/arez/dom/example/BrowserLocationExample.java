package arez.dom.example;

import arez.Arez;
import arez.ArezContext;
import arez.Observer;
import arez.dom.BrowserLocation;
import com.google.gwt.core.client.EntryPoint;
import javax.annotation.Nonnull;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

public class BrowserLocationExample
  implements EntryPoint
{
  @JsFunction
  private interface EventListener
  {
    void handleEvent( Object event );
  }

  @JsType( isNative = true, name = "Element", namespace = JsPackage.GLOBAL )
  private static class Element
  {
    @JsMethod
    native void addEventListener( String type, EventListener listener );

    @JsProperty
    native void setTextContent( String text );
  }

  @JsType( isNative = true, name = "Document", namespace = JsPackage.GLOBAL )
  private static class Document
  {
    @JsMethod
    native Element querySelector( String selector );
  }

  @JsType( isNative = true, name = "globalThis.console", namespace = JsPackage.GLOBAL )
  private static final class NativeConsole
  {
    @JsMethod
    private static native void log( Object message );
  }

  @Override
  public void onModuleLoad()
  {
    final BrowserLocation browserLocation = BrowserLocation.create();

    final ArezContext context = Arez.context();
    context.observer( () -> cleanLocation( browserLocation ),
                      Observer.Flags.READ_WRITE | Observer.Flags.NESTED_ACTIONS_ALLOWED );
    context.observer( () -> printBrowserLocation( browserLocation ) );

    document().querySelector( "#route_base" ).
      addEventListener( "click", e -> browserLocation.changeLocation( "" ) );
    document().querySelector( "#route_slash" ).
      addEventListener( "click", e -> browserLocation.changeLocation( "/" ) );
    document().querySelector( "#route_event" ).
      addEventListener( "click", e -> browserLocation.changeLocation( "/event" ) );
    document().querySelector( "#route_other" ).
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
    document().querySelector( selector ).setTextContent( message );
    NativeConsole.log( message );
  }

  @JsProperty( name = "document", namespace = JsPackage.GLOBAL )
  private static native Document document();
}
