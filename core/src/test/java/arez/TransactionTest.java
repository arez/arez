package arez;

import arez.spy.TransactionCompletedEvent;
import arez.spy.TransactionStartedEvent;
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
    final ArezContext context = Arez.context();
    final String name1 = ValueUtil.randomString();
    final int nextNodeId = context.currentNextTransactionId();

    final Transaction transaction = new Transaction( context, null, name1, TransactionMode.READ_ONLY, null );

    assertEquals( transaction.getName(), name1 );
    assertEquals( transaction.toString(), name1 );
    assertEquals( transaction.getContext(), context );
    assertEquals( transaction.getId(), nextNodeId );
    assertEquals( transaction.getPrevious(), null );
    assertEquals( transaction.getTracker(), null );
    assertEquals( transaction.getObservables(), null );
    assertEquals( transaction.getPendingDeactivations(), null );
    assertEquals( transaction.getMode(), TransactionMode.READ_ONLY );
    assertNotEquals( transaction.getStartedAt(), 0 );

    assertEquals( context.currentNextTransactionId(), nextNodeId + 1 );
  }

  @Test
  public void getName_whenNamesDisabled()
  {
    ArezTestUtil.disableNames();

    final Transaction transaction = new Transaction( Arez.context(), null, null, TransactionMode.READ_ONLY, null );
    final IllegalStateException exception = expectThrows( IllegalStateException.class, transaction::getName );

    assertEquals( exception.getMessage(), "Transaction.getName() invoked when Arez.areNamesEnabled() is false" );
  }

  @Test
  public void construct_withNameWhenNamesDisabled()
  {
    ArezTestUtil.disableNames();

    final IllegalStateException exception = expectThrows( IllegalStateException.class,
                                                          () -> new Transaction( Arez.context(),
                                                                                 null,
                                                                                 "X",
                                                                                 TransactionMode.READ_ONLY,
                                                                                 null ) );

    assertEquals( exception.getMessage(), "Transaction passed a name 'X' but Arez.areNamesEnabled() is false" );
  }

  @Test
  public void construction_whenSpyDisabled()
  {
    ArezTestUtil.disableSpies();

    final Transaction transaction =
      new Transaction( Arez.context(), null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    // Re-enable spy so can read field
    ArezTestUtil.enableSpies();

    assertEquals( transaction.getStartedAt(), 0 );
  }

  @Test
  public void construction_with_READ_WRITE_OWNED_but_no_tracker()
  {
    final ArezContext context = Arez.context();
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
    final ArezContext context = Arez.context();

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
  public void rootTransaction_zonesDisabled()
  {
    ArezTestUtil.disableZones();
    final ArezContext context = Arez.context();

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
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    assertEquals( tracker.getState(), ObserverState.INACTIVE );

    transaction.begin();

    //Just verify that it ultimately invokes beginTracking
    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void commit()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    final Observable<?> observable1 = newObservable( context );
    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );
    final Observable<?> observable2 = derivation.getDerivedValue();

    tracker.getDependencies().add( observable2 );
    observable2.getObservers().add( tracker );

    observable2.markAsPendingDeactivation();
    transaction.queueForDeactivation( observable2 );

    assertNotNull( transaction.getPendingDeactivations() );
    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertEquals( observable2.isActive(), true );
    assertEquals( observable2.isPendingDeactivation(), true );

    transaction.safeGetObservables().add( observable1 );
    final ArrayList<Observable<?>> dependencies = tracker.getDependencies();

    transaction.commit();

    // The next code block essentially verifies it calls completeTracking
    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertEquals( tracker.getDependencies().contains( observable1 ), true );
    assertEquals( observable1.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );

    // This section essentially verifies processPendingDeactivations() is called
    assertEquals( observable2.isPendingDeactivation(), false );
    assertEquals( observable2.isActive(), false );
    assertEquals( observable2.getOwner(), derivation );
    assertEquals( derivation.getState(), ObserverState.INACTIVE );
  }

  @Test
  public void trackingCycleWithNoTracker()
  {
    final ArezContext context = Arez.context();
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    final Observable<?> observable = newObservable( context );

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
  public void beginTracking_firstTracking()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    assertEquals( tracker.getState(), ObserverState.INACTIVE );

    transaction.beginTracking();

    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void beginTracking()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    ensureDerivationHasObserver( tracker );

    tracker.setState( ObserverState.STALE );
    assertEquals( tracker.getState(), ObserverState.STALE );

    final Observable<?> observable = newObservable( context );
    observable.setLeastStaleObserverState( ObserverState.STALE );
    tracker.getDependencies().add( observable );
    observable.addObserver( tracker );

    transaction.beginTracking();

    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void observe()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    transaction.beginTracking();

    final Observable<?> observable = newObservable( context );

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
  public void observe_onDisposedObservable()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    transaction.beginTracking();

    final Observable<?> observable = newObservable( context );

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observable.getObservers().size(), 0 );
    assertNull( transaction.getObservables() );
    assertNotEquals( transaction.getId(), observable.getLastTrackerTransactionId() );

    observable.setWorkState( Observable.DISPOSED );
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.observe( observable ) );

    assertEquals( exception.getMessage(),
                  "Invoked observe on transaction named '" +
                  transaction.getName() + "' for observable named '" + observable.getName() +
                  "' where the observable is disposed." );
  }

  @Test
  public void observe_noObserveIfOwnedByTracker()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    transaction.beginTracking();

    final Observable<?> observable = tracker.getDerivedValue();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.observe( observable ) );

    assertEquals( exception.getMessage(),
                  "Invoked observe on transaction named '" +
                  transaction.getName() + "' for observable named '" + observable.getName() +
                  "' where the observable is owned by the tracker." );
  }

  @Test
  public void multipleObserves()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    transaction.beginTracking();

    final Observable<?> observable = newObservable( context );

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
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    transaction.beginTracking();

    final Observable<?> observable = newObservable( context );

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
    observable.setLastTrackerTransactionId( context.nextTransactionId() );

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
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    assertEquals( transaction.getObservables(), null );

    final ArrayList<Observable<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );
    assertTrue( tracker.getDependencies() == dependencies );
    assertEquals( tracker.getDependencies().size(), 0 );
  }

  @Test
  public void completeTracking_noTrackerButObservablesPresent_shouldFail()
  {
    final ArezContext context = Arez.context();

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
    final ArezContext context = Arez.context();

    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.INACTIVE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction::completeTracking );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' called completeTracking but _tracker " +
                  "state of INACTIVE is not expected when tracker has not been disposed." );
  }

  @Test
  public void completeTracking_withDisposedTracker()
  {
    final ArezContext context = Arez.context();

    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, tracker );
    Transaction.setTransaction( transaction );

    ensureDerivationHasObserver( tracker );

    tracker.setState( ObserverState.UP_TO_DATE );
    transaction.markTrackerAsDisposed();

    // This dependency "retained" (until tracker disposed)
    final Observable<?> observable1 = newObservable( context );
    // This dependency no longer observed
    final Observable<?> observable2 = newObservable( context );
    // This dependency newly observed (until tracker disposed)
    final Observable<?> observable3 = newObservable( context );

    tracker.getDependencies().add( observable1 );
    tracker.getDependencies().add( observable2 );

    observable1.getObservers().add( tracker );
    observable2.getObservers().add( tracker );

    transaction.safeGetObservables().add( observable1 );
    transaction.safeGetObservables().add( observable3 );

    final ArrayList<Observable<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), ObserverState.INACTIVE );
    final ArrayList<Observable<?>> dependencies1 = tracker.getDependencies();
    assertTrue( dependencies1 != dependencies );
    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observable1.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
    assertEquals( observable2.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
    assertEquals( observable3.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_noNewObservablesButExistingObservables()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    // Setup existing observable dependency
    final Observable<?> observable1 = newObservable( context );
    tracker.getDependencies().add( observable1 );
    observable1.getObservers().add( tracker );

    final ArrayList<Observable<?>> dependencies = tracker.getDependencies();

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
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    final Observable<?> observable = newObservable( context );

    transaction.safeGetObservables().add( observable );

    final ArrayList<Observable<?>> dependencies = tracker.getDependencies();

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
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    final Observable<?> observable1 = newObservable( context );
    final Observable<?> observable2 = newObservable( context );
    final Observable<?> observable3 = newObservable( context );
    final Observable<?> observable4 = newObservable( context );

    transaction.safeGetObservables().add( observable1 );
    transaction.safeGetObservables().add( observable2 );
    transaction.safeGetObservables().add( observable3 );
    transaction.safeGetObservables().add( observable4 );

    final ArrayList<Observable<?>> dependencies = tracker.getDependencies();

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
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    final Observable<?> observable = newObservable( context );

    transaction.safeGetObservables().add( observable );
    transaction.safeGetObservables().add( observable );
    transaction.safeGetObservables().add( observable );
    transaction.safeGetObservables().add( observable );

    final ArrayList<Observable<?>> dependencies = tracker.getDependencies();

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
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    final Observable<?> observable1 = newObservable( context );
    final Observable<?> observable2 = newObservable( context );

    /*
     * Forcing the order 1, 1, 2, 1 means that the sequence needs to be collapsed and 2 will be moved so 1, 2
     */
    transaction.safeGetObservables().add( observable1 );
    transaction.safeGetObservables().add( observable1 );
    transaction.safeGetObservables().add( observable2 );
    transaction.safeGetObservables().add( observable1 );

    final ArrayList<Observable<?>> dependencies = tracker.getDependencies();

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
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    final Observable<?> observable1 = newObservable( context );
    final Observable<?> observable2 = newObservable( context );

    tracker.getDependencies().add( observable1 );
    tracker.getDependencies().add( observable2 );

    observable1.getObservers().add( tracker );
    observable2.getObservers().add( tracker );

    transaction.safeGetObservables().add( observable1 );
    transaction.safeGetObservables().add( observable2 );

    final ArrayList<Observable<?>> dependencies = tracker.getDependencies();

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
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    // This dependency retained
    final Observable<?> observable1 = newObservable( context );
    // This dependency no longer observed
    final Observable<?> observable2 = newObservable( context );
    // This dependency newly observed
    final Observable<?> observable3 = newObservable( context );

    tracker.getDependencies().add( observable1 );
    tracker.getDependencies().add( observable2 );

    observable1.getObservers().add( tracker );
    observable2.getObservers().add( tracker );

    transaction.safeGetObservables().add( observable1 );
    transaction.safeGetObservables().add( observable3 );

    final ArrayList<Observable<?>> dependencies = tracker.getDependencies();

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
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );
    final Observable<?> observable = derivation.getDerivedValue();

    tracker.getDependencies().add( observable );
    observable.getObservers().add( tracker );
    transaction.safeGetObservables().add( observable );

    final ArrayList<Observable<?>> dependencies = tracker.getDependencies();

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
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE_OWNED, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.STALE );
    final Observable<?> observable = derivation.getDerivedValue();

    tracker.getDependencies().add( observable );
    observable.getObservers().add( tracker );
    transaction.safeGetObservables().add( observable );

    final ArrayList<Observable<?>> dependencies = tracker.getDependencies();

    assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );

    transaction.completeTracking();

    assertEquals( tracker.getState(), ObserverState.STALE );
    assertTrue( tracker.getDependencies() == dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertEquals( tracker.getDependencies().contains( observable ), true );
    assertEquals( observable.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );

    // Make sure the derivation observer has state updated
    assertEquals( tracker.getState(), ObserverState.STALE );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void completeTracking_calculatedObservableStale_mustUpdateNewDependencies()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE_OWNED, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.UP_TO_DATE );

    ensureDerivationHasObserver( tracker );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.POSSIBLY_STALE );

    final Observable<?> observable = derivation.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    final Observer observer = newReadOnlyObserver( context );
    observer.setState( ObserverState.STALE );

    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    transaction.safeGetObservables().add( observable );

    transaction.completeTracking();

    assertEquals( tracker.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertEquals( tracker.getDependencies().contains( observable ), true );
    assertEquals( observable.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );

    // Make sure the derivation observer has state updated
    assertEquals( tracker.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void queueForDeactivation_singleObserver()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = newDerivation( context ).getDerivedValue();

    assertNull( transaction.getPendingDeactivations() );

    transaction.queueForDeactivation( observable );

    assertNotNull( transaction.getPendingDeactivations() );

    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertEquals( transaction.getPendingDeactivations().contains( observable ), true );
  }

  @Test
  public void queueForDeactivation_nestedTransaction()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );

    final Transaction transaction1 =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    final Transaction transaction2 =
      new Transaction( context, transaction1, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction2 );
    tracker.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = newDerivation( context ).getDerivedValue();

    assertNull( transaction1.getPendingDeactivations() );
    assertNull( transaction2.getPendingDeactivations() );

    transaction2.queueForDeactivation( observable );

    assertNotNull( transaction1.getPendingDeactivations() );
    assertNotNull( transaction2.getPendingDeactivations() );

    // Pending deactivations list in both transactions should be the same instance
    assertTrue( transaction1.getPendingDeactivations() == transaction2.getPendingDeactivations() );

    assertEquals( transaction1.getPendingDeactivations().size(), 1 );
    assertEquals( transaction1.getPendingDeactivations().contains( observable ), true );
    assertEquals( transaction2.getPendingDeactivations().size(), 1 );
    assertEquals( transaction2.getPendingDeactivations().contains( observable ), true );
  }

  @Test
  public void queueForDeactivation_observerAlreadyDeactivated()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = newDerivation( context ).getDerivedValue();

    transaction.queueForDeactivation( observable );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.queueForDeactivation( observable ) );

    assertEquals( exception.getMessage(),
                  "Invoked queueForDeactivation on transaction named '" +
                  transaction.getName() + "' for observable named '" + observable.getName() +
                  "' when pending deactivation already exists for observable." );
  }

  @Test
  public void queueForDeactivation_observerCanNotDeactivate()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = newObservable( context );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.queueForDeactivation( observable ) );

    assertEquals( exception.getMessage(),
                  "Invoked queueForDeactivation on transaction named '" +
                  transaction.getName() + "' for observable named '" + observable.getName() +
                  "' when observable can not be deactivated." );
  }

  @Test
  public void processPendingDeactivations_observableHasNoListeners()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    Transaction.setTransaction( transaction );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );
    final Observable<?> observable = derivation.getDerivedValue();

    observable.markAsPendingDeactivation();
    transaction.queueForDeactivation( observable );

    assertNotNull( transaction.getPendingDeactivations() );
    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertEquals( transaction.getPendingDeactivations().contains( observable ), true );
    assertEquals( observable.isActive(), true );
    assertEquals( observable.isPendingDeactivation(), true );

    final int deactivationCount = transaction.processPendingDeactivations();
    assertEquals( deactivationCount, 1 );

    assertEquals( observable.isPendingDeactivation(), false );
    assertEquals( observable.isActive(), false );
    assertEquals( observable.getOwner(), derivation );
    assertEquals( derivation.getState(), ObserverState.INACTIVE );
  }

  @Test
  public void processPendingDeactivations_noDeactivationsPending()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    assertNull( transaction.getPendingDeactivations() );

    final int deactivationCount = transaction.processPendingDeactivations();
    assertEquals( deactivationCount, 0 );

    assertNull( transaction.getPendingDeactivations() );
  }

  @Test
  public void processPendingDeactivations_observableHasListener()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    Transaction.setTransaction( transaction );

    final Observer otherObserver = newDerivation( context );
    otherObserver.setState( ObserverState.UP_TO_DATE );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );
    final Observable<?> observable = derivation.getDerivedValue();

    observable.markAsPendingDeactivation();
    transaction.queueForDeactivation( observable );
    otherObserver.getDependencies().add( observable );
    observable.addObserver( otherObserver );

    assertNotNull( transaction.getPendingDeactivations() );
    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertEquals( transaction.getPendingDeactivations().contains( observable ), true );
    assertEquals( observable.isActive(), true );
    assertEquals( observable.isPendingDeactivation(), true );

    final int deactivationCount = transaction.processPendingDeactivations();
    assertEquals( deactivationCount, 0 );

    assertEquals( observable.isPendingDeactivation(), false );
    assertEquals( observable.isActive(), true );
    assertEquals( derivation.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void processPendingDeactivations_observableHasListenerAndIsDisposed()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    Transaction.setTransaction( transaction );

    final Observer otherObserver = newDerivation( context );
    otherObserver.setState( ObserverState.UP_TO_DATE );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );
    final Observable<?> observable = derivation.getDerivedValue();

    observable.markAsPendingDeactivation();
    transaction.queueForDeactivation( observable );
    otherObserver.getDependencies().add( observable );
    observable.addObserver( otherObserver );

    observable.setWorkState( Observable.DISPOSED );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction::processPendingDeactivations );

    assertEquals( exception.getMessage(),
                  "Attempting to deactivate disposed observable named '" + observable.getName() +
                  "' in transaction named '" + transaction.getName() + "' but the observable still has observers." );

  }

  @Test
  public void processPendingDeactivations_causesMorePendingDeactivations()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    //Setup transaction as queueForDeactivation retrieves trnsaction from context
    Transaction.setTransaction( transaction );

    final Observable<?> observable1 = newObservable( context );

    final Observer derivation1 = newDerivation( context );
    derivation1.setState( ObserverState.UP_TO_DATE );

    derivation1.getDependencies().add( observable1 );
    observable1.getObservers().add( derivation1 );

    final Observable<?> observable2 = derivation1.getDerivedValue();

    final Observer derivation2 = newDerivation( context );
    derivation2.setState( ObserverState.UP_TO_DATE );

    derivation2.getDependencies().add( observable2 );
    observable2.getObservers().add( derivation2 );

    final Observable<?> observable3 = derivation2.getDerivedValue();

    observable3.markAsPendingDeactivation();
    transaction.queueForDeactivation( observable3 );

    assertNotNull( transaction.getPendingDeactivations() );
    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertEquals( transaction.getPendingDeactivations().contains( observable3 ), true );
    assertEquals( observable3.isActive(), true );
    assertEquals( observable3.isPendingDeactivation(), true );

    final int deactivationCount = transaction.processPendingDeactivations();

    //Chained calculated derivation is deactivated
    assertEquals( deactivationCount, 2 );

    assertEquals( observable3.isPendingDeactivation(), false );
    assertEquals( observable3.isActive(), false );
    assertEquals( observable3.getOwner(), derivation2 );
    assertEquals( observable2.isPendingDeactivation(), false );
    assertEquals( observable2.isActive(), false );
    assertEquals( observable2.getOwner(), derivation1 );
    assertEquals( observable1.isPendingDeactivation(), false );
    assertEquals( observable1.isActive(), true );
    assertEquals( observable1.hasOwner(), false );
    assertEquals( derivation2.getState(), ObserverState.INACTIVE );
    assertEquals( derivation2.getDependencies().size(), 0 );
    assertEquals( observable2.getObservers().size(), 0 );
    assertEquals( derivation1.getState(), ObserverState.INACTIVE );
    assertEquals( derivation1.getDependencies().size(), 0 );
    assertEquals( observable1.getObservers().size(), 0 );
  }

  @Test
  public void processPendingDeactivations_producesNoMorePendingDeactivations()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    Transaction.setTransaction( transaction );

    final Observable<?> baseObservable = newObservable( context );
    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    derivation.getDependencies().add( baseObservable );
    baseObservable.getObservers().add( derivation );

    final Observable derivedObservable = derivation.getDerivedValue();

    derivedObservable.markAsPendingDeactivation();
    transaction.queueForDeactivation( derivedObservable );

    assertNotNull( transaction.getPendingDeactivations() );
    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertEquals( transaction.getPendingDeactivations().contains( derivedObservable ), true );
    assertEquals( derivedObservable.isActive(), true );
    assertEquals( derivedObservable.isPendingDeactivation(), true );

    final int deactivationCount = transaction.processPendingDeactivations();

    //baseObservable is not active so it needs deactivation
    assertEquals( deactivationCount, 1 );

    assertEquals( derivedObservable.isPendingDeactivation(), false );
    assertEquals( derivedObservable.isActive(), false );
    assertEquals( derivedObservable.getOwner(), derivation );
    assertEquals( derivation.getState(), ObserverState.INACTIVE );
    assertEquals( derivation.getDependencies().size(), 0 );
    assertEquals( baseObservable.getObservers().size(), 0 );
  }

  @Test
  public void processPendingDeactivations_calledOnNonRootTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction1 =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    final Transaction transaction2 =
      new Transaction( context, transaction1, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction2::processPendingDeactivations );

    assertEquals( exception.getMessage(),
                  "Invoked processPendingDeactivations on transaction named '" +
                  transaction2.getName() + "' which is not the root transaction." );
  }

  @Test
  public void verifyWriteAllowed_withReadOnlyTransaction_enforceTransactionType_set_to_false()
  {
    ArezTestUtil.noEnforceTransactionType();

    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    final Observable<?> observable = newObservable( context );

    //Should produce no error
    transaction.verifyWriteAllowed( observable );
  }

  @Test
  public void verifyWriteAllowed_withReadOnlyTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    final Observable<?> observable = newObservable( context );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.verifyWriteAllowed( observable ) );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' attempted to change observable named '" +
                  observable.getName() + "' but transaction is READ_ONLY." );
  }

  @Test
  public void verifyWriteAllowed_withReadWriteTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );

    final Observable<?> observable = newObservable( context );

    //Should produce no error
    transaction.verifyWriteAllowed( observable );
  }

  @Test
  public void verifyWriteAllowed_withReadWriteOwnedTransaction()
  {
    final ArezContext context = Arez.context();

    final Observer tracker = newDerivation( context );
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE_OWNED, tracker );

    final Observable<?> observable1 = tracker.getDerivedValue();
    final Observable<?> observable2 = newObservable( context );

    tracker.getDependencies().add( observable2 );
    observable2.getObservers().add( tracker );

    //Should produce no error as it is owned by derivation
    transaction.verifyWriteAllowed( observable1 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.verifyWriteAllowed( observable2 ) );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' attempted to change observable named '" +
                  observable2.getName() + "' and transaction is READ_WRITE_OWNED but the observable has not been " +
                  "created by the transaction." );
  }

  @Test
  public void reportChanged_noObservers()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    Transaction.setTransaction( transaction );

    final Observable<?> observable = newObservable( context );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    transaction.reportChanged( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void reportChanged_singleUpToDateObserver()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    Transaction.setTransaction( transaction );

    final Observable<?> observable = newObservable( context );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Observer observer = newDerivation( context );
    observer.setState( ObserverState.UP_TO_DATE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    Transaction.setTransaction( transaction );
    transaction.reportChanged( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.STALE );
    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void reportChanged_withDisposedObservable()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    Transaction.setTransaction( transaction );

    final Observable<?> observable = newObservable( context );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Observer observer = newDerivation( context );
    observer.setState( ObserverState.UP_TO_DATE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    Transaction.setTransaction( transaction );

    observable.setWorkState( Observable.DISPOSED );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportChanged( observable ) );

    assertEquals( exception.getMessage(),
                  "Invoked reportChanged on transaction named '" + transaction.getName() +
                  "' for observable named '" + observable.getName() + "' where the observable is disposed." );
  }

  @Test
  public void reportChanged_readOnlyTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    Transaction.setTransaction( transaction );

    final Observable<?> observable = newObservable( context );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Observer observer = newDerivation( context );
    observer.setState( ObserverState.UP_TO_DATE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    Transaction.setTransaction( transaction );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportChanged( observable ) );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' attempted to change observable named '" +
                  observable.getName() + "' but transaction is READ_ONLY." );
  }

  @Test
  public void reportChanged_singleObserver_notTrackingState()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    Transaction.setTransaction( transaction );

    final Observable<?> observable = newObservable( context );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Observer observer = newDerivation( context );
    observer.setState( ObserverState.INACTIVE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.INACTIVE );

    Transaction.setTransaction( transaction );

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
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    Transaction.setTransaction( transaction );

    final Observable<?> observable = newObservable( context );
    observable.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    final Observer observer = newDerivation( context );
    observer.setState( ObserverState.POSSIBLY_STALE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer.getState(), ObserverState.POSSIBLY_STALE );

    Transaction.setTransaction( transaction );

    transaction.reportChanged( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.STALE );
    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void reportChanged_multipleObservers()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    Transaction.setTransaction( transaction );

    final Observable<?> observable = newObservable( context );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Observer observer1 = newDerivation( context );
    observer1.setState( ObserverState.UP_TO_DATE );
    observer1.getDependencies().add( observable );
    observable.getObservers().add( observer1 );

    final Observer observer2 = newDerivation( context );
    observer2.setState( ObserverState.STALE );
    observer2.getDependencies().add( observable );
    observable.getObservers().add( observer2 );

    final Observer observer3 = newDerivation( context );
    observer3.setState( ObserverState.STALE );
    observer3.getDependencies().add( observable );
    observable.getObservers().add( observer3 );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer1.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer2.getState(), ObserverState.STALE );
    assertEquals( observer3.getState(), ObserverState.STALE );

    Transaction.setTransaction( transaction );
    transaction.reportChanged( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.STALE );
    assertEquals( observer1.getState(), ObserverState.STALE );
    assertEquals( observer2.getState(), ObserverState.STALE );
    assertEquals( observer3.getState(), ObserverState.STALE );
  }

  @Test
  public void reportPossiblyChanged_singleUpToDateObserver()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = newDerivation( context );
    calculator.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = calculator.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Observer observer = newDerivation( context );
    observer.setState( ObserverState.UP_TO_DATE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    Transaction.setTransaction( transaction );
    transaction.reportPossiblyChanged( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer.getState(), ObserverState.POSSIBLY_STALE );
  }

  @Test
  public void reportPossiblyChanged_onDisposedObservable()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = newDerivation( context );
    calculator.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = calculator.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Observer observer = newDerivation( context );
    observer.setState( ObserverState.UP_TO_DATE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    Transaction.setTransaction( transaction );

    observable.setWorkState( Observable.DISPOSED );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportPossiblyChanged( observable ) );

    assertEquals( exception.getMessage(),
                  "Invoked reportPossiblyChanged on transaction named '" + transaction.getName() +
                  "' for observable named '" + observable.getName() + "' where the observable is disposed." );
  }

  @Test
  public void reportPossiblyChanged_noObserver()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = newDerivation( context );
    calculator.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = calculator.getDerivedValue();

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    transaction.reportPossiblyChanged( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void reportPossiblyChanged_ownedObserver_transaction_READ_WRITE_OWNED()
  {
    final ArezContext context = Arez.context();

    final Observer calculator = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE_OWNED, calculator );
    Transaction.setTransaction( transaction );

    calculator.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = calculator.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    calculator.getDependencies().add( observable );
    observable.getObservers().add( calculator );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( calculator.getState(), ObserverState.UP_TO_DATE );

    Transaction.setTransaction( transaction );
    transaction.reportPossiblyChanged( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );
    assertEquals( calculator.getState(), ObserverState.POSSIBLY_STALE );
  }

  @Test
  public void reportPossiblyChanged_nonOwnedObserver_transaction_READ_WRITE_OWNED()
  {
    final ArezContext context = Arez.context();

    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE_OWNED, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.UP_TO_DATE );

    final Observer calculator = newDerivation( context );
    calculator.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = calculator.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    calculator.getDependencies().add( observable );
    observable.getObservers().add( calculator );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( calculator.getState(), ObserverState.UP_TO_DATE );

    Transaction.setTransaction( transaction );

    // A computed that causes another computed to recalculate should be allowed
    transaction.reportPossiblyChanged( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );
  }

  @Test
  public void reportPossiblyChanged_readOnlyTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = newDerivation( context );
    calculator.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = calculator.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Observer observer = newDerivation( context );
    observer.setState( ObserverState.UP_TO_DATE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    Transaction.setTransaction( transaction );

    // A computed that causes another computed to recalculate should be allowed
    transaction.reportPossiblyChanged( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );
  }

  @Test
  public void reportPossiblyChanged_where_observable_is_not_derived()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    Transaction.setTransaction( transaction );

    final Observable<?> observable = newObservable( context );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Observer observer = newDerivation( context );
    observer.setState( ObserverState.UP_TO_DATE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    Transaction.setTransaction( transaction );

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
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = newDerivation( context );
    calculator.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = calculator.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Observer observer1 = newDerivation( context );
    observer1.setState( ObserverState.UP_TO_DATE );
    observer1.getDependencies().add( observable );
    observable.getObservers().add( observer1 );

    final Observer observer2 = newDerivation( context );
    observer2.setState( ObserverState.POSSIBLY_STALE );
    observer2.getDependencies().add( observable );
    observable.getObservers().add( observer2 );

    final Observer observer3 = newDerivation( context );
    observer3.setState( ObserverState.STALE );
    observer3.getDependencies().add( observable );
    observable.getObservers().add( observer3 );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer1.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer2.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer3.getState(), ObserverState.STALE );

    Transaction.setTransaction( transaction );
    transaction.reportPossiblyChanged( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer1.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer2.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer3.getState(), ObserverState.STALE );
  }

  @Test
  public void reportChangeConfirmed_singleUpToDateObserver()
  {
    final ArezContext context = Arez.context();

    final Observer calculator = newDerivation( context );
    setCurrentTransaction( calculator );

    calculator.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = calculator.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    calculator.getDependencies().add( observable );
    observable.getObservers().add( calculator );

    context.getTransaction().reportChangeConfirmed( observable );

    // Assume observer is being updated so keep that state
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( calculator.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void reportChangeConfirmed_disposedObservable()
  {
    final ArezContext context = Arez.context();

    final Observer calculator = newDerivation( context );
    setCurrentTransaction( calculator );

    calculator.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = calculator.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    calculator.getDependencies().add( observable );
    observable.getObservers().add( calculator );

    observable.setWorkState( Observable.DISPOSED );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> context.getTransaction().reportChangeConfirmed( observable ) );

    assertEquals( exception.getMessage(),
                  "Invoked reportChangeConfirmed on transaction named '" + context.getTransaction().getName() +
                  "' for observable named '" + observable.getName() + "' where the observable is disposed." );
  }

  @Test
  public void reportChangeConfirmed_noObservers()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = newDerivation( context );
    calculator.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = calculator.getDerivedValue();

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    transaction.reportChangeConfirmed( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void reportChangeConfirmed_singlePossiblyStaleObserver()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = newDerivation( context );
    calculator.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = calculator.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    final Observer observer = newDerivation( context );
    observer.setState( observable.getLeastStaleObserverState() );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    Transaction.setTransaction( transaction );
    transaction.reportChangeConfirmed( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.STALE );
    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void reportChangeConfirmed_singleStaleObserver()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = newDerivation( context );
    calculator.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = calculator.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.STALE );

    final Observer observer = newDerivation( context );
    observer.setState( observable.getLeastStaleObserverState() );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    Transaction.setTransaction( transaction );
    transaction.reportChangeConfirmed( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.STALE );
    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void reportChangeConfirmed_ownedObserver_transaction_READ_WRITE_OWNED()
  {
    final ArezContext context = Arez.context();

    final Observer calculator = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE_OWNED, calculator );
    Transaction.setTransaction( transaction );

    calculator.setState( ObserverState.POSSIBLY_STALE );

    final Observable<?> observable = calculator.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    calculator.getDependencies().add( observable );
    observable.getObservers().add( calculator );

    Transaction.setTransaction( transaction );
    transaction.reportChangeConfirmed( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.STALE );
    assertEquals( calculator.getState(), ObserverState.STALE );
  }

  @Test
  public void reportChangeConfirmed_nonOwnedObserver_transaction_READ_WRITE_OWNED()
  {
    final ArezContext context = Arez.context();

    final Observer tracker = newDerivation( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE_OWNED, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( ObserverState.UP_TO_DATE );

    final Observer calculator = newDerivation( context );
    calculator.setState( ObserverState.POSSIBLY_STALE );

    final Observable<?> observable = calculator.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    calculator.getDependencies().add( observable );
    observable.getObservers().add( calculator );

    Transaction.setTransaction( transaction );
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
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = newDerivation( context );
    calculator.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = calculator.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Observer observer = newDerivation( context );
    observer.setState( ObserverState.UP_TO_DATE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    Transaction.setTransaction( transaction );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportChangeConfirmed( observable ) );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' attempted to change observable named '" +
                  observable.getName() + "' but transaction is READ_ONLY." );
  }

  @Test
  public void reportChangeConfirmed_where_observable_is_not_derived()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    Transaction.setTransaction( transaction );

    final Observable<?> observable = newObservable( context );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Observer observer = newDerivation( context );
    observer.setState( ObserverState.UP_TO_DATE );
    observer.getDependencies().add( observable );
    observable.getObservers().add( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

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
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = newDerivation( context );
    calculator.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = calculator.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final Observer observer1 = newDerivation( context );
    observer1.setState( ObserverState.POSSIBLY_STALE );
    observer1.getDependencies().add( observable );
    observable.getObservers().add( observer1 );

    final Observer observer2 = newDerivation( context );
    observer2.setState( ObserverState.POSSIBLY_STALE );
    observer2.getDependencies().add( observable );
    observable.getObservers().add( observer2 );

    final Observer observer3 = newDerivation( context );
    observer3.setState( ObserverState.STALE );
    observer3.getDependencies().add( observable );
    observable.getObservers().add( observer3 );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observer1.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer2.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer3.getState(), ObserverState.STALE );

    Transaction.setTransaction( transaction );
    transaction.reportChangeConfirmed( observable );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.STALE );
    assertEquals( observer1.getState(), ObserverState.STALE );
    assertEquals( observer2.getState(), ObserverState.STALE );
    assertEquals( observer3.getState(), ObserverState.STALE );
  }

  @Test
  public void invariantObserverIsTracker_notTracker()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    Transaction.setTransaction( transaction );

    final Observable<?> observable = newObservable( context );

    final Observer observer = newReadOnlyObserver( context );

    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.invariantObserverIsTracker( observable, observer ) );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + transaction.getName() + "' attempted to call " +
                  "reportChangeConfirmed for observable named '" + observable.getName() + "' and found a " +
                  "dependency named '" + observer.getName() + "' that is UP_TO_DATE but is not the tracker of " +
                  "any transactions in the hierarchy: [" + transaction.getName() + "]." );
  }

  @Test
  public void invariantObserverIsTracker_trackerOfSurroundingTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observable<?> observable = newObservable( context );
    final Observer observer = newReadOnlyObserver( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, observer );
    Transaction.setTransaction( transaction );

    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    transaction.invariantObserverIsTracker( observable, observer );
  }

  @Test
  public void invariantObserverIsTracker_trackerUpTransactionHierarchy()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observable<?> observable = newObservable( context );
    final Observer observer = newReadOnlyObserver( context );

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, observer );
    final Transaction transaction2 =
      new Transaction( context, transaction, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    final Transaction transaction3 =
      new Transaction( context,
                       transaction2,
                       ValueUtil.randomString(),
                       TransactionMode.READ_WRITE,
                       newReadOnlyObserver( context ) );
    final Transaction transaction4 =
      new Transaction( context,
                       transaction3,
                       ValueUtil.randomString(),
                       TransactionMode.READ_WRITE,
                       newReadOnlyObserver( context ) );
    Transaction.setTransaction( transaction4 );

    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    transaction.invariantObserverIsTracker( observable, observer );
  }

  @Test
  public void markTrackerAsDisposed_whenNoTracker()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    Transaction.setTransaction( transaction );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction::markTrackerAsDisposed );

    assertEquals( exception.getMessage(),
                  "Attempted to invoke markTrackerAsDisposed on transaction named '" + transaction.getName() +
                  "' when there is no tracker associated with the transaction." );
  }

  @Test
  public void markTrackerAsDisposed()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer tracker = newDerivation( context );
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, tracker );
    Transaction.setTransaction( transaction );

    assertEquals( transaction.shouldDisposeTracker(), false );
    assertEquals( tracker.isDisposed(), false );

    transaction.markTrackerAsDisposed();

    assertEquals( transaction.shouldDisposeTracker(), true );
    assertEquals( tracker.isDisposed(), true );
  }

  @Test
  public void markTrackerAsDisposed_butTransactionREAD_ONLY()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer tracker = newDerivation( context );
    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, tracker );
    Transaction.setTransaction( transaction );

    assertEquals( transaction.shouldDisposeTracker(), false );
    assertEquals( tracker.isDisposed(), false );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction::markTrackerAsDisposed );

    assertEquals( exception.getMessage(),
                  "Attempted to invoke markTrackerAsDisposed on transaction named '" + transaction.getName() +
                  "' when the transaction mode is READ_ONLY and not READ_WRITE." );

    assertEquals( transaction.shouldDisposeTracker(), false );
    assertEquals( tracker.isDisposed(), false );
  }

  @Test
  public void getStartedAt_whenSpyDisabled()
  {
    ArezTestUtil.disableSpies();

    final Transaction transaction =
      new Transaction( Arez.context(), null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction::getStartedAt );

    assertEquals( exception.getMessage(), "Transaction.getStartedAt() invoked when Arez.areSpiesEnabled() is false" );
  }

  @Test
  public void beginTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final String name = ValueUtil.randomString();
    final TransactionMode mode = TransactionMode.READ_ONLY;
    final Observer tracker = null;
    final Transaction transaction = Transaction.begin( context, name, mode, tracker );

    assertTrue( context.isTransactionActive() );

    assertEquals( context.getTransaction(), transaction );
    assertEquals( transaction.getContext(), context );
    assertEquals( transaction.getName(), name );
    assertEquals( transaction.getMode(), mode );
    assertEquals( transaction.getTracker(), tracker );
    assertEquals( transaction.getPrevious(), null );
    assertEquals( transaction.getPreviousInSameContext(), null );
    assertEquals( transaction.getMode(), TransactionMode.READ_ONLY );
  }

  @Test
  public void beginTransaction_nested()
    throws Exception
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final Transaction transaction1 = Transaction.begin( context,
                                                        ValueUtil.randomString(),
                                                        TransactionMode.READ_ONLY, null );
    assertTrue( context.isTransactionActive() );

    final Transaction transaction2 = Transaction.begin( context, ValueUtil.randomString(),
                                                        TransactionMode.READ_ONLY, null );

    assertTrue( context.isTransactionActive() );

    assertEquals( context.getTransaction(), transaction2 );
    assertEquals( transaction2.getPrevious(), transaction1 );
    assertEquals( transaction2.getPreviousInSameContext(), transaction1 );
  }

  @Test
  public void beginTransaction_nested_inDifferentContext()
    throws Exception
  {
    final ArezContext context1 = new ArezContext();
    final ArezContext context2 = new ArezContext();

    assertFalse( context1.isTransactionActive() );
    assertFalse( context2.isTransactionActive() );

    final Transaction transaction1 =
      Transaction.begin( context1, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    assertTrue( context1.isTransactionActive() );
    assertFalse( context2.isTransactionActive() );

    final Transaction transaction2 =
      Transaction.begin( context2, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    assertFalse( context1.isTransactionActive() );
    assertTrue( context2.isTransactionActive() );

    assertEquals( context2.getTransaction(), transaction2 );
    assertEquals( transaction2.getPrevious(), transaction1 );
    assertEquals( transaction2.getPreviousInSameContext(), null );
  }

  @Test
  public void beginTransaction_triple_nested_alternating_contexts()
    throws Exception
  {
    final ArezContext context1 = new ArezContext();
    final ArezContext context2 = new ArezContext();

    assertFalse( context1.isTransactionActive() );
    assertFalse( context2.isTransactionActive() );

    final Transaction transaction1 =
      Transaction.begin( context1, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    assertTrue( context1.isTransactionActive() );
    assertFalse( context2.isTransactionActive() );

    final Transaction transaction2 =
      Transaction.begin( context2, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    assertFalse( context1.isTransactionActive() );
    assertTrue( context2.isTransactionActive() );

    final Transaction transaction3 =
      Transaction.begin( context1, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    assertTrue( context1.isTransactionActive() );
    assertFalse( context2.isTransactionActive() );

    assertEquals( context1.getTransaction(), transaction3 );
    assertEquals( transaction3.getPrevious(), transaction2 );
    assertEquals( transaction3.getPreviousInSameContext(), transaction1 );
  }

  @Test
  public void beginTransaction_triple_nested_alternating_contexts_but_zones_disabled()
    throws Exception
  {
    ArezTestUtil.disableZones();

    final ArezContext context1 = new ArezContext();
    final ArezContext context2 = new ArezContext();

    assertFalse( context1.isTransactionActive() );
    assertFalse( context2.isTransactionActive() );

    final String name1 = ValueUtil.randomString();
    final String name2 = ValueUtil.randomString();
    Transaction.begin( context1, name1, TransactionMode.READ_ONLY, null );
    assertTrue( context1.isTransactionActive() );
    assertTrue( context2.isTransactionActive() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> Transaction.begin( context2, name2, TransactionMode.READ_ONLY, null ) );

    assertEquals( exception.getMessage(),
                  "Zones are not enabled but the transaction named '" + name2 + "' is nested in a " +
                  "transaction named '" + name1 + "' from a different context." );
  }

  @Test
  public void beginTransaction_generates_spyEvent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String name = ValueUtil.randomString();
    Transaction.begin( context, name, TransactionMode.READ_ONLY, null );

    handler.assertEventCount( 1 );
    final TransactionStartedEvent event = handler.assertEvent( TransactionStartedEvent.class, 0 );
    assertEquals( event.getName(), name );
    assertEquals( event.isMutation(), false );
    assertEquals( event.getTracker(), null );
  }

  @Test
  public void beginTransaction_attemptToNest_READ_WRITE_in_READ_ONLY()
    throws Exception
  {
    final ArezContext context = Arez.context();

    setCurrentTransaction( newReadOnlyObserver( context ) );
    final Transaction transaction = context.getTransaction();

    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> Transaction.begin( context, name, TransactionMode.READ_WRITE, null ) );
    assertEquals( exception.getMessage(),
                  "Attempting to create READ_WRITE transaction named '" + name +
                  "' but it is nested in transaction named '" + transaction.getName() + "' with " +
                  "mode READ_ONLY which is not equal to READ_WRITE." );
  }

  @Test
  public void beginTransaction_nest_in_suspended_transaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    setCurrentTransaction( newReadOnlyObserver( context ) );
    final Transaction transaction = context.getTransaction();
    Transaction.markAsSuspended();

    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> Transaction.begin( context, name, TransactionMode.READ_ONLY, null ) );
    assertEquals( exception.getMessage(),
                  "Attempted to create transaction named '" + name + "' while " +
                  "nested in a suspended transaction named '" + transaction.getName() + "'." );
  }

  @Test
  public void beginTransaction_attemptToNest_READ_WRITE_in_READ_WRITE_OWNED()
    throws Exception
  {
    final ArezContext context = Arez.context();

    setCurrentTransaction( newDerivation( context ) );
    final Transaction transaction = context.getTransaction();

    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> Transaction.begin( context, name, TransactionMode.READ_WRITE, null ) );
    assertEquals( exception.getMessage(),
                  "Attempting to create READ_WRITE transaction named '" + name +
                  "' but it is nested in transaction named '" + transaction.getName() + "' with " +
                  "mode READ_WRITE_OWNED which is not equal to READ_WRITE." );
  }

  @Test
  public void commitTransaction_matchingRootTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction.begin();
    Transaction.setTransaction( transaction );

    Transaction.commit( transaction );

    assertEquals( context.isTransactionActive(), false );
    assertEquals( context.isSchedulerEnabled(), true );
  }

  @Test
  public void commitTransaction_onlyRootEnablesScheduler()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction1 =
      Transaction.begin( context, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    final Transaction transaction2 =
      Transaction.begin( context, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    assertEquals( context.isTransactionActive(), true );
    assertEquals( context.isSchedulerEnabled(), false );

    Transaction.commit( transaction2 );

    assertEquals( context.isTransactionActive(), true );
    assertEquals( context.isSchedulerEnabled(), false );

    Transaction.commit( transaction1 );

    assertEquals( context.isTransactionActive(), false );
    assertEquals( context.isSchedulerEnabled(), true );
  }

  @Test
  public void commitTransaction_enablesScheduler_forInterleavedContexts()
    throws Exception
  {
    final ArezContext context1 = new ArezContext();
    final ArezContext context2 = new ArezContext();

    final Transaction transaction1 =
      Transaction.begin( context1, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    final Transaction transaction2 =
      Transaction.begin( context2, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    assertEquals( context1.isTransactionActive(), false );
    assertEquals( context1.isSchedulerEnabled(), false );
    assertEquals( context2.isTransactionActive(), true );
    assertEquals( context2.isSchedulerEnabled(), false );

    Transaction.commit( transaction2 );

    assertEquals( context1.isTransactionActive(), true );
    assertEquals( context1.isSchedulerEnabled(), false );
    assertEquals( context2.isTransactionActive(), false );
    assertEquals( context2.isSchedulerEnabled(), true );

    Transaction.commit( transaction1 );

    assertEquals( context1.isTransactionActive(), false );
    assertEquals( context1.isSchedulerEnabled(), true );
  }

  @Test
  public void commitTransaction_enablesScheduler_forStripedContexts()
    throws Exception
  {
    final ArezContext context1 = new ArezContext();
    final ArezContext context2 = new ArezContext();

    final Transaction transaction1 =
      Transaction.begin( context1, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    final Transaction transaction2 =
      Transaction.begin( context2, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    final Transaction transaction3 =
      Transaction.begin( context1, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    final Transaction transaction4 =
      Transaction.begin( context2, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    assertEquals( context1.isTransactionActive(), false );
    assertEquals( context1.isSchedulerEnabled(), false );
    assertEquals( context2.isTransactionActive(), true );
    assertEquals( context2.isSchedulerEnabled(), false );

    Transaction.commit( transaction4 );

    assertEquals( context1.isTransactionActive(), true );
    assertEquals( context1.isSchedulerEnabled(), false );
    assertEquals( context2.isTransactionActive(), false );
    assertEquals( context2.isSchedulerEnabled(), false );

    Transaction.commit( transaction3 );

    assertEquals( context1.isTransactionActive(), false );
    assertEquals( context1.isSchedulerEnabled(), false );
    assertEquals( context2.isTransactionActive(), true );
    assertEquals( context2.isSchedulerEnabled(), false );

    Transaction.commit( transaction2 );

    assertEquals( context1.isTransactionActive(), true );
    assertEquals( context1.isSchedulerEnabled(), false );
    assertEquals( context2.isTransactionActive(), false );
    assertEquals( context2.isSchedulerEnabled(), true );

    Transaction.commit( transaction1 );

    assertEquals( context1.isTransactionActive(), false );
    assertEquals( context1.isSchedulerEnabled(), true );
  }

  @SuppressWarnings( "ConstantConditions" )
  @Test
  public void commitTransaction_generates_spyEvent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String name = ValueUtil.randomString();
    final Transaction transaction =
      new Transaction( context, null, name, TransactionMode.READ_ONLY, null );
    transaction.begin();
    Transaction.setTransaction( transaction );

    Transaction.commit( transaction );

    handler.assertEventCount( 1 );
    final TransactionCompletedEvent event = handler.assertEvent( TransactionCompletedEvent.class, 0 );
    assertEquals( event.getName(), name );
    assertEquals( event.isMutation(), false );
    assertEquals( event.getTracker(), null );
    assertTrue( event.getDuration() >= 0 );
  }

  @Test
  public void commitTransaction_nonMatchingRootTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction.begin();
    Transaction.setTransaction( transaction );

    final Transaction transaction2 =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction2.begin();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.commit( transaction2 ) );
    assertEquals( exception.getMessage(),
                  "Attempting to commit transaction named '" + transaction2.getName() +
                  "' but this does not match existing transaction named '" + transaction.getName() + "'." );
  }

  @Test
  public void commitTransaction_transactionSuspended()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction.begin();
    Transaction.setTransaction( transaction );
    Transaction.markAsSuspended();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.commit( transaction ) );
    assertEquals( exception.getMessage(),
                  "Attempting to commit transaction named '" + transaction.getName() + "' transaction is suspended." );
  }

  @Test
  public void commitTransaction_noTransactionActive()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction.begin();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.commit( transaction ) );
    assertEquals( exception.getMessage(),
                  "Attempting to commit transaction named '" + transaction.getName() +
                  "' but no transaction is active." );
  }

  @Test
  public void getPreviousInSameContext_zonesNotEnabled()
  {
    ArezTestUtil.disableZones();

    final Transaction transaction =
      new Transaction( Arez.context(), null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction::getPreviousInSameContext );
    assertEquals( exception.getMessage(),
                  "Attempted to invoke getPreviousInSameContext() on transaction named '" +
                  transaction.getName() + "' when zones are not enabled." );
  }

  @Test
  public void current()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction.begin();
    Transaction.setTransaction( transaction );

    assertEquals( Transaction.current(), transaction );
  }

  @Test
  public void current_noTransaction()
    throws Exception
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, Transaction::current );
    assertEquals( exception.getMessage(), "Attempting to get current transaction but no transaction is active." );
  }

  @Test
  public void current_transactionSuspended()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction.begin();
    Transaction.setTransaction( transaction );
    Transaction.markAsSuspended();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, Transaction::current );
    assertEquals( exception.getMessage(), "Attempting to get current transaction but transaction is suspended." );
  }

  @Test
  public void resume()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction.begin();
    Transaction.setTransaction( transaction );
    Transaction.markAsSuspended();

    assertTrue( Transaction.isSuspended() );

    Transaction.resume( transaction );

    assertFalse( Transaction.isSuspended() );
  }

  @Test
  public void resume_noActiveTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction.begin();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.resume( transaction ) );
    assertEquals( exception.getMessage(),
                  "Attempting to resume transaction named '" + transaction.getName() +
                  "' but no transaction is active." );
  }

  @Test
  public void resume_transactionNotSuspended()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction.begin();
    Transaction.setTransaction( transaction );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.resume( transaction ) );
    assertEquals( exception.getMessage(),
                  "Attempting to resume transaction named '" + transaction.getName() +
                  "' but transaction is not suspended." );
  }

  @Test
  public void resume_transactionNotMatched()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction.begin();
    Transaction.setTransaction( transaction );
    Transaction.markAsSuspended();

    final Transaction transaction2 =
      new Transaction( context, transaction, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction2.begin();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.resume( transaction2 ) );
    assertEquals( exception.getMessage(),
                  "Attempting to resume transaction named '" + transaction2.getName() +
                  "' but this does not match existing transaction named '" + transaction.getName() + "'." );
  }

  @Test
  public void suspend()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction.begin();
    Transaction.setTransaction( transaction );

    assertFalse( Transaction.isSuspended() );

    Transaction.suspend( transaction );

    assertTrue( Transaction.isSuspended() );
  }

  @Test
  public void suspend_transactionNotMatched()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction.begin();

    final Transaction transaction2 =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction2.begin();
    Transaction.setTransaction( transaction2 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.suspend( transaction ) );
    assertEquals( exception.getMessage(),
                  "Attempting to suspend transaction named '" + transaction.getName() +
                  "' but this does not match existing transaction named '" + transaction2.getName() + "'." );
  }

  @Test
  public void suspend_transactionAlreadySuspended()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction.begin();
    Transaction.setTransaction( transaction );
    Transaction.markAsSuspended();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.suspend( transaction ) );
    assertEquals( exception.getMessage(),
                  "Attempting to suspend transaction named '" + transaction.getName() +
                  "' but transaction is already suspended." );
  }

  @Test
  public void suspend_noActiveTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction.begin();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.suspend( transaction ) );
    assertEquals( exception.getMessage(),
                  "Attempting to suspend transaction named '" + transaction.getName() +
                  "' but no transaction is active." );
  }
}
