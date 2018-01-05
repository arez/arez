package org.realityforge.arez.doc.examples.autorun;

import org.realityforge.arez.Arez;

public class AutorunExample
{
  public static void main( String[] args )
    throws Throwable
  {
    Arez.context().autorun( () -> {
      // Interact with arez observable state (or computed values) here
      // and any time these changed this function will be re-run.
      //DOC ELIDE START
      //DOC ELIDE END
    } );
  }
}
