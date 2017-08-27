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
  public void begin()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );

    assertEquals( tracker.getState(), ObserverState.NOT_TRACKING );

    transaction.begin();

    //Just verify that it ultimately invokes beginTracking
    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void commit()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );

    final TestObservable observable1 = new TestObservable( context, ValueUtil.randomString() );
    final Observer calculator = new Observer( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.UP_TO_DATE );
    final TestObservable observable2 = new TestObservable( context, ValueUtil.randomString(), calculator );

    tracker.getDependencies().add( observable2 );
    observable2.getObservers().add( tracker );

    observable2.setPendingPassivation( true );
    transaction.queueForPassivation( observable2 );

    assertNotNull( transaction.getPendingPassivations() );
    assertEquals( transaction.getPendingPassivations().size(), 1 );
    assertEquals( observable2.isActive(), true );
    assertEquals( observable2.isPendingPassivation(), true );

    transaction.safeGetObservables().add( observable1 );
    final ArrayList<Observable> dependencies = tracker.getDependencies();

    transaction.commit();

    // The next code block essentially verifies it calls completeTracking
    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertEquals( tracker.getDependencies().contains( observable1 ), true );
    assertEquals( observable1.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );

    // This section essentially verifies passivatePendingPassivations() is called
    assertEquals( observable2.isPendingPassivation(), false );
    assertEquals( observable2.isActive(), false );
    assertEquals( observable2.getOwner(), calculator );
    assertEquals( calculator.getState(), ObserverState.NOT_TRACKING );
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
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );

    assertEquals( transaction.getObservables(), null );

    final ArrayList<Observable> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
    assertTrue( tracker.getDependencies() == dependencies );
    assertEquals( tracker.getDependencies().size(), 0 );
  }

  @Test
  public void completeTracking_noNewObservablesButExistingObservables()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );

    // Setup existing observable dependency
    final TestObservable observable1 = new TestObservable( context, ValueUtil.randomString() );
    tracker.getDependencies().add( observable1 );
    observable1.getObservers().add( tracker );

    final ArrayList<Observable> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observable1.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
    assertEquals( observable1.getObservers().size(), 0 );
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

    observable1.getObservers().add( tracker );
    observable2.getObservers().add( tracker );

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

  @Test
  public void completeTracking_calculatedObservableUpToDate()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );

    final Observer calculator = new Observer( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.UP_TO_DATE );
    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), calculator );

    tracker.getDependencies().add( observable );
    observable.getObservers().add( tracker );
    transaction.safeGetObservables().add( observable );

    final ArrayList<Observable> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
    assertTrue( tracker.getDependencies() == dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertEquals( tracker.getDependencies().contains( observable ), true );
    assertEquals( observable.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_calculatedObservableStale()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );

    final Observer calculator = new Observer( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.STALE );
    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), calculator );

    tracker.getDependencies().add( observable );
    observable.getObservers().add( tracker );
    transaction.safeGetObservables().add( observable );

    final ArrayList<Observable> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), ObserverState.STALE );
    assertTrue( tracker.getDependencies() == dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertEquals( tracker.getDependencies().contains( observable ), true );
    assertEquals( observable.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void queueForPassivation_singleObserver()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );

    final TestObservable observable =
      new TestObservable( context, ValueUtil.randomString(), new Observer( context, ValueUtil.randomString() ) );

    assertNull( transaction.getPendingPassivations() );

    transaction.queueForPassivation( observable );

    assertNotNull( transaction.getPendingPassivations() );

    assertEquals( transaction.getPendingPassivations().size(), 1 );
    assertEquals( transaction.getPendingPassivations().contains( observable ), true );
  }

  @Test
  public void queueForPassivation_nestedTransaction()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction1 = new Transaction( context, null, ValueUtil.randomString(), tracker );
    final Transaction transaction2 = new Transaction( context, transaction1, ValueUtil.randomString(), tracker );

    final TestObservable observable =
      new TestObservable( context, ValueUtil.randomString(), new Observer( context, ValueUtil.randomString() ) );

    assertNull( transaction1.getPendingPassivations() );
    assertNull( transaction2.getPendingPassivations() );

    transaction2.queueForPassivation( observable );

    assertNotNull( transaction1.getPendingPassivations() );
    assertNotNull( transaction2.getPendingPassivations() );

    // Pending passivations list in both transactions should be the same instance
    assertTrue( transaction1.getPendingPassivations() == transaction2.getPendingPassivations() );

    assertEquals( transaction1.getPendingPassivations().size(), 1 );
    assertEquals( transaction1.getPendingPassivations().contains( observable ), true );
    assertEquals( transaction2.getPendingPassivations().size(), 1 );
    assertEquals( transaction2.getPendingPassivations().contains( observable ), true );
  }

  @Test
  public void queueForPassivation_observerAlreadyPassivated()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );

    final TestObservable observable =
      new TestObservable( context, ValueUtil.randomString(), new Observer( context, ValueUtil.randomString() ) );

    transaction.queueForPassivation( observable );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.queueForPassivation( observable ) );

    assertEquals( exception.getMessage(),
                  "Invoked queueForPassivation on transaction named '" +
                  transaction.getName() + "' for observable named '" + observable.getName() +
                  "' when pending passivation already exists for observable." );
  }

  @Test
  public void queueForPassivation_observerNotPassivatable()
  {
    final ArezContext context = new ArezContext();
    final Observer tracker = new Observer( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), tracker );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.queueForPassivation( observable ) );

    assertEquals( exception.getMessage(),
                  "Invoked queueForPassivation on transaction named '" +
                  transaction.getName() + "' for observable named '" + observable.getName() +
                  "' when observable can not be passivated." );
  }

  @Test
  public void passivatePendingPassivations_observableHasNoListeners()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), null );

    final Observer calculator = new Observer( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.UP_TO_DATE );
    final TestObservable observable =
      new TestObservable( context, ValueUtil.randomString(), calculator );

    observable.setPendingPassivation( true );
    transaction.queueForPassivation( observable );

    assertNotNull( transaction.getPendingPassivations() );
    assertEquals( transaction.getPendingPassivations().size(), 1 );
    assertEquals( transaction.getPendingPassivations().contains( observable ), true );
    assertEquals( observable.isActive(), true );
    assertEquals( observable.isPendingPassivation(), true );

    final int passivatedCount = transaction.passivatePendingPassivations();
    assertEquals( passivatedCount, 1 );

    assertEquals( observable.isPendingPassivation(), false );
    assertEquals( observable.isActive(), false );
    assertEquals( observable.getOwner(), calculator );
    assertEquals( calculator.getState(), ObserverState.NOT_TRACKING );
  }

  @Test
  public void passivatePendingPassivations_noPassivationsPending()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), null );

    assertNull( transaction.getPendingPassivations() );

    final int passivatedCount = transaction.passivatePendingPassivations();
    assertEquals( passivatedCount, 0 );

    assertNull( transaction.getPendingPassivations() );
  }

  @Test
  public void passivatePendingPassivations_observableHasListener()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), null );

    final Observer otherObserver = new Observer( context, ValueUtil.randomString() );
    otherObserver.setState( ObserverState.UP_TO_DATE );

    final Observer calculator = new Observer( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.UP_TO_DATE );
    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), calculator );

    observable.setPendingPassivation( true );
    transaction.queueForPassivation( observable );
    otherObserver.getDependencies().add( observable );
    observable.addObserver( otherObserver );

    assertNotNull( transaction.getPendingPassivations() );
    assertEquals( transaction.getPendingPassivations().size(), 1 );
    assertEquals( transaction.getPendingPassivations().contains( observable ), true );
    assertEquals( observable.isActive(), true );
    assertEquals( observable.isPendingPassivation(), true );

    final int passivatedCount = transaction.passivatePendingPassivations();
    assertEquals( passivatedCount, 0 );

    assertEquals( observable.isPendingPassivation(), false );
    assertEquals( observable.isActive(), true );
    assertEquals( calculator.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void passivatePendingPassivations_passivationCausesMorePendingPassivations()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), null );

    //Setup transaction as queueForPassivation retrieves trnsaction from context
    context.setTransaction( transaction );

    final TestObservable observable1 = new TestObservable( context, ValueUtil.randomString() );

    final Observer observer1 = new Observer( context, ValueUtil.randomString() );
    observer1.setState( ObserverState.UP_TO_DATE );

    observer1.getDependencies().add( observable1 );
    observable1.getObservers().add( observer1 );

    final TestObservable observable2 = new TestObservable( context, ValueUtil.randomString(), observer1 );

    final Observer observer2 = new Observer( context, ValueUtil.randomString() );
    observer2.setState( ObserverState.UP_TO_DATE );

    observer2.getDependencies().add( observable2 );
    observable2.getObservers().add( observer2 );

    final TestObservable observable3 =
      new TestObservable( context, ValueUtil.randomString(), observer2 );

    observable3.setPendingPassivation( true );
    transaction.queueForPassivation( observable3 );

    assertNotNull( transaction.getPendingPassivations() );
    assertEquals( transaction.getPendingPassivations().size(), 1 );
    assertEquals( transaction.getPendingPassivations().contains( observable3 ), true );
    assertEquals( observable3.isActive(), true );
    assertEquals( observable3.isPendingPassivation(), true );

    final int passivatedCount = transaction.passivatePendingPassivations();

    //Chained calculated observer is passivated
    assertEquals( passivatedCount, 2 );

    assertEquals( observable3.isPendingPassivation(), false );
    assertEquals( observable3.isActive(), false );
    assertEquals( observable3.getOwner(), observer2 );
    assertEquals( observable2.isPendingPassivation(), false );
    assertEquals( observable2.isActive(), false );
    assertEquals( observable2.getOwner(), observer1 );
    assertEquals( observable1.isPendingPassivation(), false );
    assertEquals( observable1.isActive(), true );
    assertEquals( observable1.getOwner(), null );
    assertEquals( observer2.getState(), ObserverState.NOT_TRACKING );
    assertEquals( observer2.getDependencies().size(), 0 );
    assertEquals( observable2.getObservers().size(), 0 );
    assertEquals( observer1.getState(), ObserverState.NOT_TRACKING );
    assertEquals( observer1.getDependencies().size(), 0 );
    assertEquals( observable1.getObservers().size(), 0 );
  }

  @Test
  public void passivatePendingPassivations_passivationCausesDeactivationButNoMorePendingPassivations()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), null );

    final TestObservable baseObservable =
      new TestObservable( context, ValueUtil.randomString() );
    final Observer calculator = new Observer( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.UP_TO_DATE );

    calculator.getDependencies().add( baseObservable );
    baseObservable.getObservers().add( calculator );

    final TestObservable derivedObservable =
      new TestObservable( context, ValueUtil.randomString(), calculator );

    derivedObservable.setPendingPassivation( true );
    transaction.queueForPassivation( derivedObservable );

    assertNotNull( transaction.getPendingPassivations() );
    assertEquals( transaction.getPendingPassivations().size(), 1 );
    assertEquals( transaction.getPendingPassivations().contains( derivedObservable ), true );
    assertEquals( derivedObservable.isActive(), true );
    assertEquals( derivedObservable.isPendingPassivation(), true );

    final int passivatedCount = transaction.passivatePendingPassivations();

    //baseObservable is not active so it needs to passivation
    assertEquals( passivatedCount, 1 );

    assertEquals( derivedObservable.isPendingPassivation(), false );
    assertEquals( derivedObservable.isActive(), false );
    assertEquals( derivedObservable.getOwner(), calculator );
    assertEquals( calculator.getState(), ObserverState.NOT_TRACKING );
    assertEquals( calculator.getDependencies().size(), 0 );
    assertEquals( baseObservable.getObservers().size(), 0 );
  }

  @Test
  public void passivatePendingPassivations_calledOnNonRootTransaction()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction1 = new Transaction( context, null, ValueUtil.randomString(), null );
    final Transaction transaction2 = new Transaction( context, transaction1, ValueUtil.randomString(), null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction2::passivatePendingPassivations );

    assertEquals( exception.getMessage(),
                  "Invoked passivatePendingPassivations on transaction named '" +
                  transaction2.getName() + "' which is not the root transaction." );
  }
}
