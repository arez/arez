package arez.doc.examples.actions;

import arez.Arez;

public class ActionExample4
{
  public static void main( String[] args )
    throws Throwable
  {
    final String name = "MyAction";
    final boolean mutation = false;
    final int result = Arez.context().action( name, mutation, () -> {
      // Interact with arez observable state (or computed values) here
      //DOC ELIDE START
      int value = 0;
      //DOC ELIDE END
      return value;
    } );
  }
}
