package arez.dom.example;

import arez.Arez;
import arez.ArezContext;
import arez.dom.MediaQuery;
import com.google.gwt.core.client.EntryPoint;
import akasha.Global;
import akasha.Element;

public class MediaQueryExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final Element statusElement = Global.document().querySelector( "#status" );
    final Element queryElement = Global.document().querySelector( "#query" );
    final MediaQuery mediaQuery = MediaQuery.create( "(max-width: 600px)" );
    final ArezContext context = Arez.context();
    context
      .observer( () -> statusElement.textContent = "Screen size: " + ( mediaQuery.matches() ? "Narrow" : "Wide" ) );
    context.observer( () -> queryElement.textContent = mediaQuery.getQuery() );
    Global.document().querySelector( "#change-query" ).addEventListener( "click", e -> {
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
