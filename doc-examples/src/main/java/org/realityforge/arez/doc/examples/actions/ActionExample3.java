package org.realityforge.arez.doc.examples.actions;

import org.realityforge.arez.Arez;

public class ActionExample3
{
  public static void main( String[] args )
  {
    final int result = Arez.context().safeAction( () -> {
      // Interact with arez observable state (or computed values) here
      //DOC ELIDE START
      int value = 0;
      //DOC ELIDE END
      return value;
    } );
  }
}
