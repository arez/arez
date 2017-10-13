package org.realityforge.arez.gwt.examples;

import elemental2.dom.DomGlobal;
import org.realityforge.arez.Arez;

final class ExampleUtil
{
  private ExampleUtil()
  {
  }

  static void jsonLogSpyEvents()
  {
    Arez.context().getSpy().addSpyEventHandler( new JsonLogSpyEventProcessor() );
  }

  static void logAllErrors()
  {
    Arez.context().addObserverErrorHandler( ( observer, error, throwable ) -> {
      DomGlobal.console.log( "Observer error: " + error + "\nobserver: " + observer );
      if ( null != throwable )
      {
        DomGlobal.console.log( throwable );
      }
    } );
  }
}
