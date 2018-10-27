package arez.mediaquery.example;

import arez.Arez;
import arez.mediaquery.MediaQuery;
import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;

public class MediaQueryExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final MediaQuery mediaQuery = MediaQuery.create( "(max-width: 600px)" );
    Arez.context().observer( () ->
                               DomGlobal.document.querySelector( "#status" ).textContent =
                                 "Screen size Status: " + ( mediaQuery.matches() ? "Narrow" : "Wide" ) );
  }
}
