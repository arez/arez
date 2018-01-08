package arez.doc.examples.tracker;

import arez.Arez;
import arez.Observer;
import arez.Procedure;

public class TrackerExample
{
  public static void main( String[] args )
    throws Throwable
  {
    final Procedure trackedFunction = () -> {
      // Interact with arez observable state (or computed values) here
      // and any time these changed the rescheduleRender function will
      // be run which will somehow reschedule this function.
      //DOC ELIDE START
      //DOC ELIDE END
    };
    final Observer tracker =
      Arez.context().tracker( () -> rescheduleRender() );
    //DOC ELIDE START
    //DOC ELIDE END
    // The rescheduleRender should ultimately result in the following
    // invocation. This line will need to be run at least once so that
    // the Arez runtime can determine the dependencies and reschedule
    // when the dependencies are changed.
    Arez.context().track( tracker, trackedFunction );
  }

  static void rescheduleRender()
  {

  }
}
