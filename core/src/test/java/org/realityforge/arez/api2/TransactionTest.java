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

    final Transaction transaction = new Transaction( context, null, name1, TransactionMode.READ_ONLY, null );

    assertEquals( transaction.getName(), name1 );
    assertEquals( transaction.getContext(), context );
    assertEquals( transaction.getId(), nextNodeId );
    assertEquals( transaction.getPrevious(), null );
    assertEquals( transaction.getTracker(), null );
    assertEquals( transaction.getObservables(), null );
    assertEquals( transaction.getPendingPassivations(), null );
    assertEquals( transaction.getMode(), TransactionMode.READ_ONLY );

    assertEquals( context.currentNextNodeId(), nextNodeId + 1 );
  }

  @Test
  public void construction_with_READ_WRITE_OWNED_but_no_tracker()
  {
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Transaction( context, null, name, TransactionMode.READ_WRITE_OWNED, null ) );

    assertEquals( exception.getMessage(),
                  "Attempted to create transaction named '" + name +
                  "' with mode READ_WRITE_OWNED but no tracker specified." );
  }

  @Test
  public void rootTransaction()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction1 =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    final Transaction transaction2 =
      new Transaction( context, transaction1, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    assertEquals( transaction1.isRootTransaction(), true );
    assertEquals( transaction1.getRootTransaction(), transaction1 );

    assertEquals( transaction2.isRootTransaction(), false );
    assertEquals( transaction2.getRootTransaction(), transaction1 );
  }

  @Test
  public void begin()
  {
    final ArezContext context = new ArezContext();
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

    assertEquals( tracker.getState(), ObserverState.INACTIVE );

    transaction.begin();

    //Just verify that it ultimately invokes beginTracking
    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void commit()
  {
    final ArezContext context = new ArezContext();
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

    final TestObservable observable1 = new TestObservable( context, ValueUtil.randomString() );
    final Derivation derivation = new Derivation( context, ValueUtil.randomString() );
    derivation.setState( ObserverState.UP_TO_DATE );
    final TestObservable observable2 = new TestObservable( context, ValueUtil.randomString(), derivation );

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
    assertEquals( observable2.getOwner(), derivation );
    assertEquals( derivation.getState(), ObserverState.INACTIVE );
  }

  @Test
  public void trackingCycleWithNoTracker()
  {
    final ArezContext context = new ArezContext();
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

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
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

    assertEquals( tracker.getState(), ObserverState.INACTIVE );

    transaction.beginTracking();

    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void observe()
  {
    final ArezContext context = new ArezContext();
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
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
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
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
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
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
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

    assertEquals( transaction.getObservables(), null );

    final ArrayList<Observable> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
    assertTrue( tracker.getDependencies() == dependencies );
    assertEquals( tracker.getDependencies().size(), 0 );
  }

  @Test
  public void completeTracking_noTrackerButObservablesPresent_shouldFail()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    // This next line forces the creation of observables
    transaction.safeGetObservables();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction::completeTracking );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' has no associated tracker so " +
                  "_observables should be null but are not." );
  }

  @Test
  public void completeTracking_withBadTrackerState()
  {
    final ArezContext context = new ArezContext();

    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.INACTIVE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction::completeTracking );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' called completeTracking but _tracker " +
                  "state of INACTIVE is unexpected." );
  }

  @Test
  public void completeTracking_noNewObservablesButExistingObservables()
  {
    final ArezContext context = new ArezContext();
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

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
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

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
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

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
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

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
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

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
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

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
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

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
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

    final Derivation derivation = new Derivation( context, ValueUtil.randomString() );
    derivation.setState( ObserverState.UP_TO_DATE );
    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), derivation );

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
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

    final Derivation derivation = new Derivation( context, ValueUtil.randomString() );
    derivation.setState( ObserverState.STALE );
    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), derivation );

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
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

    final TestObservable observable =
      new TestObservable( context, ValueUtil.randomString(), new Derivation( context, ValueUtil.randomString() ) );

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
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction1 =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    final Transaction transaction2 =
      new Transaction( context, transaction1, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

    final TestObservable observable =
      new TestObservable( context, ValueUtil.randomString(), new Derivation( context, ValueUtil.randomString() ) );

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
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

    final TestObservable observable =
      new TestObservable( context, ValueUtil.randomString(), new Derivation( context, ValueUtil.randomString() ) );

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
    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );

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

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    final Derivation derivation = new Derivation( context, ValueUtil.randomString() );
    derivation.setState( ObserverState.UP_TO_DATE );
    final TestObservable observable =
      new TestObservable( context, ValueUtil.randomString(), derivation );

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
    assertEquals( observable.getOwner(), derivation );
    assertEquals( derivation.getState(), ObserverState.INACTIVE );
  }

  @Test
  public void passivatePendingPassivations_noPassivationsPending()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    assertNull( transaction.getPendingPassivations() );

    final int passivatedCount = transaction.passivatePendingPassivations();
    assertEquals( passivatedCount, 0 );

    assertNull( transaction.getPendingPassivations() );
  }

  @Test
  public void passivatePendingPassivations_observableHasListener()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    final Derivation otherObserver = new Derivation( context, ValueUtil.randomString() );
    otherObserver.setState( ObserverState.UP_TO_DATE );

    final Derivation derivation = new Derivation( context, ValueUtil.randomString() );
    derivation.setState( ObserverState.UP_TO_DATE );
    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), derivation );

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
    assertEquals( derivation.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void passivatePendingPassivations_passivationCausesMorePendingPassivations()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    //Setup transaction as queueForPassivation retrieves trnsaction from context
    context.setTransaction( transaction );

    final TestObservable observable1 = new TestObservable( context, ValueUtil.randomString() );

    final Derivation derivation1 = new Derivation( context, ValueUtil.randomString() );
    derivation1.setState( ObserverState.UP_TO_DATE );

    derivation1.getDependencies().add( observable1 );
    observable1.getObservers().add( derivation1 );

    final TestObservable observable2 = new TestObservable( context, ValueUtil.randomString(), derivation1 );

    final Derivation derivation2 = new Derivation( context, ValueUtil.randomString() );
    derivation2.setState( ObserverState.UP_TO_DATE );

    derivation2.getDependencies().add( observable2 );
    observable2.getObservers().add( derivation2 );

    final TestObservable observable3 =
      new TestObservable( context, ValueUtil.randomString(), derivation2 );

    observable3.setPendingPassivation( true );
    transaction.queueForPassivation( observable3 );

    assertNotNull( transaction.getPendingPassivations() );
    assertEquals( transaction.getPendingPassivations().size(), 1 );
    assertEquals( transaction.getPendingPassivations().contains( observable3 ), true );
    assertEquals( observable3.isActive(), true );
    assertEquals( observable3.isPendingPassivation(), true );

    final int passivatedCount = transaction.passivatePendingPassivations();

    //Chained calculated derivation is passivated
    assertEquals( passivatedCount, 2 );

    assertEquals( observable3.isPendingPassivation(), false );
    assertEquals( observable3.isActive(), false );
    assertEquals( observable3.getOwner(), derivation2 );
    assertEquals( observable2.isPendingPassivation(), false );
    assertEquals( observable2.isActive(), false );
    assertEquals( observable2.getOwner(), derivation1 );
    assertEquals( observable1.isPendingPassivation(), false );
    assertEquals( observable1.isActive(), true );
    assertEquals( observable1.getOwner(), null );
    assertEquals( derivation2.getState(), ObserverState.INACTIVE );
    assertEquals( derivation2.getDependencies().size(), 0 );
    assertEquals( observable2.getObservers().size(), 0 );
    assertEquals( derivation1.getState(), ObserverState.INACTIVE );
    assertEquals( derivation1.getDependencies().size(), 0 );
    assertEquals( observable1.getObservers().size(), 0 );
  }

  @Test
  public void passivatePendingPassivations_passivationCausesDeactivationButNoMorePendingPassivations()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    final TestObservable baseObservable =
      new TestObservable( context, ValueUtil.randomString() );
    final Derivation derivation = new Derivation( context, ValueUtil.randomString() );
    derivation.setState( ObserverState.UP_TO_DATE );

    derivation.getDependencies().add( baseObservable );
    baseObservable.getObservers().add( derivation );

    final TestObservable derivedObservable =
      new TestObservable( context, ValueUtil.randomString(), derivation );

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
    assertEquals( derivedObservable.getOwner(), derivation );
    assertEquals( derivation.getState(), ObserverState.INACTIVE );
    assertEquals( derivation.getDependencies().size(), 0 );
    assertEquals( baseObservable.getObservers().size(), 0 );
  }

  @Test
  public void passivatePendingPassivations_calledOnNonRootTransaction()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction1 =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    final Transaction transaction2 =
      new Transaction( context, transaction1, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction2::passivatePendingPassivations );

    assertEquals( exception.getMessage(),
                  "Invoked passivatePendingPassivations on transaction named '" +
                  transaction2.getName() + "' which is not the root transaction." );
  }

  @Test
  public void verifyWriteAllowed_withReadOnlyTransaction_enforceTransactionType_set_to_false()
  {
    getConfigProvider().setEnforceTransactionType( false );

    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );

    //Should produce no error
    transaction.verifyWriteAllowed( observable );
  }

  @Test
  public void verifyWriteAllowed_withReadOnlyTransaction()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.verifyWriteAllowed( observable ) );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' attempted to change observable named '" +
                  observable.getName() + "' but transaction is READ_ONLY." );
  }

  @Test
  public void verifyWriteAllowed_withReadWriteTransaction()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );

    //Should produce no error
    transaction.verifyWriteAllowed( observable );
  }

  @Test
  public void verifyWriteAllowed_withReadWriteOwnedTransaction()
  {
    final ArezContext context = new ArezContext();

    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE_OWNED, tracker );

    final TestObservable observable1 = new TestObservable( context, ValueUtil.randomString(), tracker );
    final TestObservable observable2 = new TestObservable( context, ValueUtil.randomString() );
    final TestObservable observable3 = new TestObservable( context, ValueUtil.randomString() );

    tracker.getDependencies().add( observable3 );
    observable3.getObservers().add( tracker );

    //Should produce no error as it is owned by derivation
    transaction.verifyWriteAllowed( observable1 );

    // Should produce no errors as it has no observers and thus has been created in this transaction
    transaction.verifyWriteAllowed( observable2 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.verifyWriteAllowed( observable3 ) );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' attempted to change observable named '" +
                  observable3.getName() + "' and transaction is READ_WRITE_OWNED but the observable has not been " +
                  "created by the transaction." );
  }

  @Test
  public void reportChanged_singleUpToDateObserver()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Derivation observer = new Derivation( context, ValueUtil.randomString() );
    observer.setState( ObserverState.UP_TO_DATE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    context.setTransaction( transaction );
    transaction.reportChanged( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.STALE );
    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void reportChanged_readOnlyTransaction()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Derivation observer = new Derivation( context, ValueUtil.randomString() );
    observer.setState( ObserverState.UP_TO_DATE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    context.setTransaction( transaction );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportChanged( observable ) );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' attempted to change observable named '" +
                  observable.getName() + "' but transaction is READ_ONLY." );
  }

  @Test
  public void reportChanged_singleObserver_notTrackingState()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );
    observable.setLeastStaleObserverState( ObserverState.INACTIVE );

    final Derivation observer = new Derivation( context, ValueUtil.randomString() );
    observer.setState( ObserverState.INACTIVE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.INACTIVE );
    assertEquals( observer.getState(), ObserverState.INACTIVE );

    context.setTransaction( transaction );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportChanged( observable ) );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' has attempted to explicitly " +
                  "change observable named '" + observable.getName() + "' and observable is in unexpected " +
                  "state INACTIVE." );
  }

  @Test
  public void reportChanged_singleObserver_possiblyStaleState()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );
    observable.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    final Derivation observer = new Derivation( context, ValueUtil.randomString() );
    observer.setState( ObserverState.POSSIBLY_STALE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer.getState(), ObserverState.POSSIBLY_STALE );

    context.setTransaction( transaction );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportChanged( observable ) );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' has attempted to explicitly " +
                  "change observable named '" + observable.getName() + "' but observable is in state POSSIBLY_STALE " +
                  "indicating it is derived and thus can not be explicitly changed." );
  }

  @Test
  public void reportChanged_multipleObservers()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Derivation observer1 = new Derivation( context, ValueUtil.randomString() );
    observer1.setState( ObserverState.UP_TO_DATE );
    observer1.getDependencies().add( observable );
    observable.getObservers().add( observer1 );

    final Derivation observer2 = new Derivation( context, ValueUtil.randomString() );
    observer2.setState( ObserverState.STALE );
    observer2.getDependencies().add( observable );
    observable.getObservers().add( observer2 );

    final Derivation observer3 = new Derivation( context, ValueUtil.randomString() );
    observer3.setState( ObserverState.STALE );
    observer3.getDependencies().add( observable );
    observable.getObservers().add( observer3 );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer1.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer2.getState(), ObserverState.STALE );
    assertEquals( observer3.getState(), ObserverState.STALE );

    context.setTransaction( transaction );
    transaction.reportChanged( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.STALE );
    assertEquals( observer1.getState(), ObserverState.STALE );
    assertEquals( observer2.getState(), ObserverState.STALE );
    assertEquals( observer3.getState(), ObserverState.STALE );
  }

  @Test
  public void reportPossiblyChanged_singleUpToDateObserver()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );

    final Derivation calculator = new Derivation( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.UP_TO_DATE );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), calculator );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Derivation observer = new Derivation( context, ValueUtil.randomString() );
    observer.setState( ObserverState.UP_TO_DATE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    context.setTransaction( transaction );
    transaction.reportPossiblyChanged( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer.getState(), ObserverState.POSSIBLY_STALE );
  }

  @Test
  public void reportPossiblyChanged_ownedObserver_transaction_READ_WRITE_OWNED()
  {
    final ArezContext context = new ArezContext();

    final Derivation calculator = new Derivation( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE_OWNED, calculator );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), calculator );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    calculator.getDependencies().add( observable );
    observable.getObservers().add( calculator );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( calculator.getState(), ObserverState.UP_TO_DATE );

    context.setTransaction( transaction );
    transaction.reportPossiblyChanged( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );
    assertEquals( calculator.getState(), ObserverState.POSSIBLY_STALE );
  }

  @Test
  public void reportPossiblyChanged_nonOwnedObserver_transaction_READ_WRITE_OWNED()
  {
    final ArezContext context = new ArezContext();

    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE_OWNED, tracker );

    final Derivation calculator = new Derivation( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.UP_TO_DATE );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), calculator );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    calculator.getDependencies().add( observable );
    observable.getObservers().add( calculator );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( calculator.getState(), ObserverState.UP_TO_DATE );

    context.setTransaction( transaction );
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportPossiblyChanged( observable ) );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' attempted to change observable named '" +
                  observable.getName() + "' and transaction is READ_WRITE_OWNED but the observable has not been " +
                  "created by the transaction." );
  }

  @Test
  public void reportPossiblyChanged_readOnlyTransaction()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    final Derivation calculator = new Derivation( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.UP_TO_DATE );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), calculator );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Derivation observer = new Derivation( context, ValueUtil.randomString() );
    observer.setState( ObserverState.UP_TO_DATE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    context.setTransaction( transaction );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportPossiblyChanged( observable ) );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' attempted to change observable named '" +
                  observable.getName() + "' but transaction is READ_ONLY." );
  }

  @Test
  public void reportPossiblyChanged_where_observable_is_not_derived()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Derivation observer = new Derivation( context, ValueUtil.randomString() );
    observer.setState( ObserverState.UP_TO_DATE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    context.setTransaction( transaction );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportPossiblyChanged( observable ) );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' has attempted to mark " +
                  "observable named '" + observable.getName() + "' as potentially changed but observable " +
                  "is not a derived value." );
  }

  @Test
  public void reportPossiblyChanged_multipleObservers()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );

    final Derivation calculator = new Derivation( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.UP_TO_DATE );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), calculator );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Derivation observer1 = new Derivation( context, ValueUtil.randomString() );
    observer1.setState( ObserverState.UP_TO_DATE );
    observer1.getDependencies().add( observable );
    observable.getObservers().add( observer1 );

    final Derivation observer2 = new Derivation( context, ValueUtil.randomString() );
    observer2.setState( ObserverState.POSSIBLY_STALE );
    observer2.getDependencies().add( observable );
    observable.getObservers().add( observer2 );

    final Derivation observer3 = new Derivation( context, ValueUtil.randomString() );
    observer3.setState( ObserverState.STALE );
    observer3.getDependencies().add( observable );
    observable.getObservers().add( observer3 );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer1.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer2.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer3.getState(), ObserverState.STALE );

    context.setTransaction( transaction );
    transaction.reportPossiblyChanged( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer1.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer2.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer3.getState(), ObserverState.STALE );
  }

  @Test
  public void reportChangeConfirmed_singleUpToDateObserver()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );

    final Derivation calculator = new Derivation( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.UP_TO_DATE );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), calculator );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Derivation observer = new Derivation( context, ValueUtil.randomString() );
    observer.setState( observable.getLeastStaleObserverState() );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    context.setTransaction( transaction );
    transaction.reportChangeConfirmed( observable );

    // Assume observer is being updated so keep that state
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void reportChangeConfirmed_singlePossiblyStaleObserver()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );

    final Derivation calculator = new Derivation( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.UP_TO_DATE );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), calculator );
    observable.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    final Derivation observer = new Derivation( context, ValueUtil.randomString() );
    observer.setState( observable.getLeastStaleObserverState() );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    context.setTransaction( transaction );
    transaction.reportChangeConfirmed( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.STALE );
    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void reportChangeConfirmed_singleStaleObserver()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );

    final Derivation calculator = new Derivation( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.UP_TO_DATE );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), calculator );
    observable.setLeastStaleObserverState( ObserverState.STALE );

    final Derivation observer = new Derivation( context, ValueUtil.randomString() );
    observer.setState( observable.getLeastStaleObserverState() );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    context.setTransaction( transaction );
    transaction.reportChangeConfirmed( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.STALE );
    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void reportChangeConfirmed_ownedObserver_transaction_READ_WRITE_OWNED()
  {
    final ArezContext context = new ArezContext();

    final Derivation calculator = new Derivation( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.POSSIBLY_STALE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE_OWNED, calculator );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), calculator );
    observable.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    calculator.getDependencies().add( observable );
    observable.getObservers().add( calculator );

    context.setTransaction( transaction );
    transaction.reportChangeConfirmed( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.STALE );
    assertEquals( calculator.getState(), ObserverState.STALE );
  }

  @Test
  public void reportChangeConfirmed_nonOwnedObserver_transaction_READ_WRITE_OWNED()
  {
    final ArezContext context = new ArezContext();

    final Derivation tracker = new Derivation( context, ValueUtil.randomString() );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE_OWNED, tracker );

    final Derivation calculator = new Derivation( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.POSSIBLY_STALE );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), calculator );
    observable.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    calculator.getDependencies().add( observable );
    observable.getObservers().add( calculator );

    context.setTransaction( transaction );
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportChangeConfirmed( observable ) );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' attempted to change observable named '" +
                  observable.getName() + "' and transaction is READ_WRITE_OWNED but the observable has not been " +
                  "created by the transaction." );
  }

  @Test
  public void reportChangeConfirmed_readOnlyTransaction()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    final Derivation calculator = new Derivation( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.UP_TO_DATE );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), calculator );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Derivation observer = new Derivation( context, ValueUtil.randomString() );
    observer.setState( ObserverState.UP_TO_DATE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    context.setTransaction( transaction );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportChangeConfirmed( observable ) );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' attempted to change observable named '" +
                  observable.getName() + "' but transaction is READ_ONLY." );
  }

  @Test
  public void reportChangeConfirmed_where_observable_is_not_derived()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString() );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Derivation observer = new Derivation( context, ValueUtil.randomString() );
    observer.setState( ObserverState.UP_TO_DATE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    context.setTransaction( transaction );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportChangeConfirmed( observable ) );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' has attempted to mark " +
                  "observable named '" + observable.getName() + "' as potentially changed but observable " +
                  "is not a derived value." );
  }

  @Test
  public void reportChangeConfirmed_multipleObservers()
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );

    final Derivation calculator = new Derivation( context, ValueUtil.randomString() );
    calculator.setState( ObserverState.UP_TO_DATE );

    final TestObservable observable = new TestObservable( context, ValueUtil.randomString(), calculator );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Derivation observer1 = new Derivation( context, ValueUtil.randomString() );
    observer1.setState( ObserverState.POSSIBLY_STALE );
    observer1.getDependencies().add( observable );
    observable.getObservers().add( observer1 );

    final Derivation observer2 = new Derivation( context, ValueUtil.randomString() );
    observer2.setState( ObserverState.POSSIBLY_STALE );
    observer2.getDependencies().add( observable );
    observable.getObservers().add( observer2 );

    final Derivation observer3 = new Derivation( context, ValueUtil.randomString() );
    observer3.setState( ObserverState.STALE );
    observer3.getDependencies().add( observable );
    observable.getObservers().add( observer3 );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer1.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer2.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer3.getState(), ObserverState.STALE );

    context.setTransaction( transaction );
    transaction.reportChangeConfirmed( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.STALE );
    assertEquals( observer1.getState(), ObserverState.STALE );
    assertEquals( observer2.getState(), ObserverState.STALE );
    assertEquals( observer3.getState(), ObserverState.STALE );
  }
}
