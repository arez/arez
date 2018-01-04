package org.realityforge.arez.doc.examples.actions;

import org.realityforge.arez.Arez;

public class ActionExample
{
  public static void main( String[] args )
    throws Throwable
  {
    Arez.context().action( () -> {
      // Interact with arez observable state (or computed values) here
      //DOC ELIDE START
      //DOC ELIDE END
    } );
  }
}
