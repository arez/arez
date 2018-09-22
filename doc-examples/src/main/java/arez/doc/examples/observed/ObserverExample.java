package arez.doc.examples.observed;

import arez.Arez;

public class ObserverExample
{
  public static void main( String[] args )
  {
    Arez.context().observer( () -> {
      // Interact with arez observable state (or computed values) here
      // and any time these changed this function will be re-run.
      //DOC ELIDE START
      //DOC ELIDE END
    } );
  }
}
