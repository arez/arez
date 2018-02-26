package arez.doc.examples.when;

import arez.Arez;

public class WhenExample
{
  public static void main( String[] args )
    throws Throwable
  {
    Arez.context().when( () -> {
      // Interact with arez observable state (or computed values) here
      // and any time these changed this function will be re-run.
      // This method should return true, when the effect is to be run
      //DOC ELIDE START
      return false;
      //DOC ELIDE END
    }, () -> {
      // This action will be invoked when the condition block returns true
      //DOC ELIDE START
      //DOC ELIDE END
    } );
  }
}
