package org.realityforge.arez.doc.examples.autorun;

import org.realityforge.arez.Arez;

public class AutorunExample2
{
  public static void main( String[] args )
    throws Throwable
  {
    final String name = "MyAutorunObserver";
    final boolean mutation = false;
    Arez.context().autorun( name, mutation, () -> {
      // Interact with arez observable state (or computed values) here
      // and any time these changed this function will be re-run.
      //DOC ELIDE START
      //DOC ELIDE END
    } );
  }
}
