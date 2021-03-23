package arez.doc.examples.observer_error;

import akasha.Console;
import arez.Arez;

@SuppressWarnings( "CodeBlock2Expr" )
public class ObserverErrorHandlerExample
{
  public static void main( String[] args )
  {
    Arez.context().addObserverErrorHandler( ( ( observer, error, throwable ) -> {
      Console.error( error + ": Error occurred", throwable );
    } ) );
  }
}
