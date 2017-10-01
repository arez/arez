package org.realityforge.arez.gwt.examples;

import elemental2.dom.DomGlobal;
import org.realityforge.arez.Arez;
import org.realityforge.arez.extras.WhyRun;

final class ExampleUtil
{
  private ExampleUtil()
  {
  }

  @SuppressWarnings( "Convert2MethodRef" )
  static void spyEvents()
  {
    Arez.context().getSpy().addSpyEventHandler( e -> GwtExamplesSpyUtil.emitEvent( e ) );
  }

  static void whyRun()
  {
    DomGlobal.console.log( new WhyRun( Arez.context() ).whyRun() );
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
