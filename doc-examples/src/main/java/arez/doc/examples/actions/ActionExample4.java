package arez.doc.examples.actions;

import arez.Arez;
import arez.Flags;

public class ActionExample4
{
  public static void main( String[] args )
    throws Throwable
  {
    final int result = Arez.context().action( "MyAction", () -> {
      // Interact with arez observable state (or computable values) here
      //DOC ELIDE START
      int value = 0;
      //DOC ELIDE END
      return value;
    }, Flags.READ_ONLY );
  }
}
