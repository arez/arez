package arez.mediaquery.example;

import arez.Arez;
import arez.ArezContext;
import arez.mediaquery.MediaQuery;
import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import elemental2.dom.Element;

public class MediaQueryExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final Element statusElement = DomGlobal.document.querySelector( "#status" );
    final Element queryElement = DomGlobal.document.querySelector( "#query" );
    final MediaQuery mediaQuery = MediaQuery.create( "(max-width: 600px)" );
    final ArezContext context = Arez.context();
    context
      .observer( () -> statusElement.textContent = "Screen size: " + ( mediaQuery.matches() ? "Narrow" : "Wide" ) );
    context.observer( () -> queryElement.textContent = mediaQuery.getQuery() );
    Arez.context().getSpy().addSpyEventHandler( e -> DomGlobal.console.log( e ) );
    DomGlobal.document.querySelector( "#change-query" ).addEventListener( "click", e -> {
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
}
