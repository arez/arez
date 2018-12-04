package arez.doc.examples.scheduler;

import arez.Arez;
import arez.SchedulerLock;

public class SchedulerExample
{
  public static void main( String[] args )
  {
    assert !Arez.context().isSchedulerPaused();
    // Pause the scheduler here
    final SchedulerLock pausedControl = Arez.context().pauseScheduler();
    assert Arez.context().isSchedulerPaused();
    // Perform work here that requires that the scheduler be disabled
    //DOC ELIDE START
    //DOC ELIDE END
    // Re-enable the scheduler
    pausedControl.dispose();
  }
}
