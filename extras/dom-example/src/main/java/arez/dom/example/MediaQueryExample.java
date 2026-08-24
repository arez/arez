package arez.dom.example;

import arez.Arez;
import arez.ArezContext;
import arez.dom.MediaQuery;
import com.google.gwt.core.client.EntryPoint;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

public class MediaQueryExample
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

  @Override
  public void onModuleLoad()
  {
    final Element statusElement = document().querySelector( "#status" );
    final Element queryElement = document().querySelector( "#query" );
    final MediaQuery mediaQuery = MediaQuery.create( "(max-width: 600px)" );
    final ArezContext context = Arez.context();
    context
      .observer( () -> statusElement.setTextContent( "Screen size: " +
                                                     ( mediaQuery.matches() ? "Narrow" : "Wide" ) ) );
    context.observer( () -> queryElement.setTextContent( mediaQuery.getQuery() ) );
    document().querySelector( "#change-query" ).addEventListener( "click", e -> {
      context.safeAction( () -> {
        if ( mediaQuery.getQuery().equals( "(max-width: 600px)" ) )
        {
          mediaQuery.setQuery( "(max-width: 1200px)" );
        }
        else
        {
          mediaQuery.setQuery( "(max-width: 600px)" );
        }
      } );
    } );
  }

  @JsProperty( name = "document", namespace = JsPackage.GLOBAL )
  private static native Document document();
}
