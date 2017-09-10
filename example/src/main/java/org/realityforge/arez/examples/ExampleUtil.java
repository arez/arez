package org.realityforge.arez.examples;

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
      System.out.println( "Observer error: " + error + "\nobserver: " + observer );
      if ( null != throwable )
      {
        throwable.printStackTrace( System.out );
      }
    } );
  }
}
