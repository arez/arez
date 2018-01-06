package org.realityforge.arez.doc.examples.observer_error;

import elemental2.dom.DomGlobal;
import org.realityforge.arez.Arez;

public class ObserverErrorHandlerExample
{
  public static void main( String[] args )
  {
    Arez.context().addObserverErrorHandler( ( ( observer, error, throwable ) -> {
      DomGlobal.console.error( error + ": Error occurred", throwable );
    } ) );
  }
}
