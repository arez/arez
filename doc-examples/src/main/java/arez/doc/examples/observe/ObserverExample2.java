package arez.doc.examples.observe;

import arez.Arez;
import arez.Flags;

public class ObserverExample2
{
  public static void main( String[] args )
  {
    Arez.context().observer( "MyObserver", () -> {
      // Interact with arez observable state (or computable values) here
      // and any time these changed this function will be re-run.
      //DOC ELIDE START
      //DOC ELIDE END
    }, Flags.READ_WRITE );
  }
}
