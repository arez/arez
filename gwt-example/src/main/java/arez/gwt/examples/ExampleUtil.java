package arez.gwt.examples;

import arez.Arez;
import elemental2.dom.DomGlobal;

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
