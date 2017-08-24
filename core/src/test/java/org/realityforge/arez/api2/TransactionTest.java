package org.realityforge.arez.api2;

import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TransactionTest
  extends AbstractArezTest
{
  @Test
  public void construction()
  {
    final ArezContext context = new ArezContext();
    final String name1 = ValueUtil.randomString();
    final int nextNodeId = context.currentNextNodeId();

    final Transaction transaction = new Transaction( context, null, name1, null );

    assertEquals( transaction.getName(), name1 );
    assertEquals( transaction.getContext(), context );
    assertEquals( transaction.getId(), nextNodeId );
    assertEquals( transaction.getPrevious(), null );
    assertEquals( transaction.getTracker(), null );
    assertEquals( transaction.getObservables(), null );
    assertEquals( transaction.getPendingPassivations(), null );

    assertEquals( context.currentNextNodeId(), nextNodeId + 1 );
  }

  @Test
  public void trackingCycleWithNoTracker()
  {
    final ArezContext context = new ArezContext();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), null );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( transaction.getObservables(), null );

    //Transaction should perform no action during tracking if there is no associated tracker
    transaction.beginTracking();
    transaction.observe( observable );
    transaction.completeTracking();

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( transaction.getObservables(), null );
  }
}
