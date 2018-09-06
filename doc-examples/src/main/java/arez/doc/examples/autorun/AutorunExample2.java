package arez.doc.examples.autorun;

import arez.Arez;
import arez.Flags;

public class AutorunExample2
{
  public static void main( String[] args )
    throws Throwable
  {
    final String name = "MyAutorunObserver";
    Arez.context().observer( name, () -> {
      // Interact with arez observable state (or computed values) here
      // and any time these changed this function will be re-run.
      //DOC ELIDE START
      //DOC ELIDE END
    }, Flags.READ_WRITE );
  }
}
