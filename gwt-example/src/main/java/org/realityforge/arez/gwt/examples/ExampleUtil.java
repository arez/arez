package org.realityforge.arez.gwt.examples;

import elemental2.dom.DomGlobal;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;

final class ExampleUtil
{
  private ExampleUtil()
  {
  }

  static void logAllErrors( @Nonnull final ArezContext context )
  {
    context.addObserverErrorHandler( ( observer, error, throwable ) -> {
      DomGlobal.console.log( "Observer error: " + error + "\nobserver: " + observer );
      if ( null != throwable )
      {
        DomGlobal.console.log( throwable );
      }
    } );
  }
}
