package arez.doc.examples.actions;

import arez.Arez;

public class ActionExample2
{
  public static void main( String[] args )
    throws Throwable
  {
    final int result = Arez.context().action( () -> {
      // Interact with arez observable state (or computed values) here
      //DOC ELIDE START
      int value = 0;
      //DOC ELIDE END
      return value;
    } );
  }
}
