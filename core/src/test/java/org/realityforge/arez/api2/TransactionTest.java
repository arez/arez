package org.realityforge.arez.api2;

import java.util.ArrayList;
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
  public void rootTransaction()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction1 = new Transaction( context, null, ValueUtil.randomString(), null );
    final Transaction transaction2 = new Transaction( context, transaction1, ValueUtil.randomString(), null );

    assertEquals( transaction1.isRootTransaction(), true );
    assertEquals( transaction1.getRootTransaction(), transaction1 );

    assertEquals( transaction2.isRootTransaction(), false );
    assertEquals( transaction2.getRootTransaction(), transaction1 );
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

  @Test
  public void beginTracking()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );

    assertEquals( tracker.getState(), ObserverState.NOT_TRACKING );

    transaction.beginTracking();

    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void observe()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );
    transaction.beginTracking();

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observable.getObservers().size(), 0 );
    assertNull( transaction.getObservables() );
    assertNotEquals( transaction.getId(), observable.getLastTrackerTransactionId() );

    transaction.observe( observable );

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( transaction.getId(), observable.getLastTrackerTransactionId() );
    assertNotNull( transaction.getObservables() );

    assertTrue( transaction.getObservables().contains( observable ) );
    assertEquals( transaction.getObservables().size(), 1 );
  }

  @Test
  public void multipleObserves()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );
    transaction.beginTracking();

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observable.getObservers().size(), 0 );
    assertNull( transaction.getObservables() );
    assertNotEquals( transaction.getId(), observable.getLastTrackerTransactionId() );

    transaction.observe( observable );
    transaction.observe( observable );
    transaction.observe( observable );
    transaction.observe( observable );

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( transaction.getId(), observable.getLastTrackerTransactionId() );
    assertNotNull( transaction.getObservables() );

    assertTrue( transaction.getObservables().contains( observable ) );
    assertEquals( transaction.getObservables().size(), 1 );
  }

  @Test
  public void multipleObservesNestedTransactionInBetween()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );
    transaction.beginTracking();

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observable.getObservers().size(), 0 );
    assertNull( transaction.getObservables() );
    assertNotEquals( transaction.getId(), observable.getLastTrackerTransactionId() );

    transaction.observe( observable );
    transaction.observe( observable );

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( transaction.getId(), observable.getLastTrackerTransactionId() );
    assertNotNull( transaction.getObservables() );

    // Simulate a nested transaction that observes this observable by updating tracking id
    observable.setLastTrackerTransactionId( context.nextNodeId() );

    transaction.observe( observable );
    transaction.observe( observable );

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( transaction.getId(), observable.getLastTrackerTransactionId() );
    assertNotNull( transaction.getObservables() );

    assertTrue( transaction.getObservables().contains( observable ) );

    // Two instances of same observable is expected as the LastTrackerTransactionId
    // failed to match causing duplicate to be added. This would normally be cleaned
    // up at later time in process during completeTracking()
    assertEquals( transaction.getObservables().size(), 2 );
  }

  @Test
  public void completeTracking_noObservables()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.STALE );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );

    assertEquals( transaction.getObservables(), null );

    final ArrayList<Observable> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 0 );
  }

  @Test
  public void completeTracking_singleObservable()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );

    transaction.safeGetObservables().add( observable );

    final ArrayList<Observable> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertEquals( tracker.getDependencies().contains( observable ), true );
    assertEquals( observable.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_multipleObservable()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );

    final TestObservable observable1 = new TestObservable( context, ValueUtil.randomString() );
    final TestObservable observable2 = new TestObservable( context, ValueUtil.randomString() );
    final TestObservable observable3 = new TestObservable( context, ValueUtil.randomString() );
    final TestObservable observable4 = new TestObservable( context, ValueUtil.randomString() );

    transaction.safeGetObservables().add( observable1 );
    transaction.safeGetObservables().add( observable2 );
    transaction.safeGetObservables().add( observable3 );
    transaction.safeGetObservables().add( observable4 );

    final ArrayList<Observable> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 4 );
    assertEquals( tracker.getDependencies().contains( observable1 ), true );
    assertEquals( observable1.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
    assertEquals( observable2.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
    assertEquals( observable3.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
    assertEquals( observable4.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_singleObservableMultipleEntries()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );

    transaction.safeGetObservables().add( observable );
    transaction.safeGetObservables().add( observable );
    transaction.safeGetObservables().add( observable );
    transaction.safeGetObservables().add( observable );

    final ArrayList<Observable> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertEquals( tracker.getDependencies().contains( observable ), true );
    assertEquals( observable.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_multipleObservableMultipleEntries()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );

    final TestObservable observable1 = new TestObservable( context, ValueUtil.randomString() );
    final TestObservable observable2 = new TestObservable( context, ValueUtil.randomString() );

    /*
     * Forcing the order 1, 1, 2, 1 means that the sequence needs to be collapsed and 2 will be moved so 1, 2
     */
    transaction.safeGetObservables().add( observable1 );
    transaction.safeGetObservables().add( observable1 );
    transaction.safeGetObservables().add( observable2 );
    transaction.safeGetObservables().add( observable1 );

    final ArrayList<Observable> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 2 );
    assertEquals( tracker.getDependencies().contains( observable1 ), true );
    assertEquals( tracker.getDependencies().contains( observable2 ), true );
    assertEquals( observable1.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
    assertEquals( observable2.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_matchingExistingDependencies()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );

    final TestObservable observable1 = new TestObservable( context, ValueUtil.randomString() );
    final TestObservable observable2 = new TestObservable( context, ValueUtil.randomString() );

    tracker.getDependencies().add( observable1 );
    tracker.getDependencies().add( observable2 );

    transaction.safeGetObservables().add( observable1 );
    transaction.safeGetObservables().add( observable2 );

    final ArrayList<Observable> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
    assertTrue( tracker.getDependencies() == dependencies );
    assertEquals( tracker.getDependencies().size(), 2 );
    assertEquals( tracker.getDependencies().contains( observable1 ), true );
    assertEquals( tracker.getDependencies().contains( observable2 ), true );
    assertEquals( observable1.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
    assertEquals( observable2.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_dependenciesChanged()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );

    // This dependency retained
    final TestObservable observable1 = new TestObservable( context, ValueUtil.randomString() );
    // This dependency no longer observed
    final TestObservable observable2 = new TestObservable( context, ValueUtil.randomString() );
    // This dependency newly observed
    final TestObservable observable3 = new TestObservable( context, ValueUtil.randomString() );

    tracker.getDependencies().add( observable1 );
    tracker.getDependencies().add( observable2 );

    observable1.getObservers().add( tracker );
    observable2.getObservers().add( tracker );

    transaction.safeGetObservables().add( observable1 );
    transaction.safeGetObservables().add( observable3 );

    final ArrayList<Observable> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 2 );
    assertEquals( tracker.getDependencies().contains( observable1 ), true );
    assertEquals( tracker.getDependencies().contains( observable3 ), true );
    assertEquals( observable1.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
    assertEquals( observable2.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
    assertEquals( observable3.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
  }
}
