package arez.doc.examples.observer_error;

import arez.Arez;
import elemental2.dom.DomGlobal;

@SuppressWarnings( "CodeBlock2Expr" )
public class ObserverErrorHandlerExample
{
  public static void main( String[] args )
  {
    Arez.context().addObserverErrorHandler( ( ( observer, error, throwable ) -> {
      DomGlobal.console.error( error + ": Error occurred", throwable );
    } ) );
  }
}
