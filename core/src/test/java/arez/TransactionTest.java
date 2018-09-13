package arez;

import arez.spy.TransactionCompletedEvent;
import arez.spy.TransactionStartedEvent;
import java.util.ArrayList;
import javax.annotation.Nonnull;
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

    final Transaction transaction = new Transaction( context, null, name1, false, null );

    assertEquals( transaction.getName(), name1 );
    assertEquals( transaction.toString(), name1 );
    assertEquals( transaction.getContext(), context );
    assertEquals( transaction.getId(), nextNodeId );
    assertEquals( transaction.getPrevious(), null );
    assertEquals( transaction.getTracker(), null );
    assertEquals( transaction.getObservableValues(), null );
    assertEquals( transaction.getPendingDeactivations(), null );
    assertEquals( transaction.isMutation(), false );
    assertNotEquals( transaction.getStartedAt(), 0 );

    assertEquals( context.currentNextTransactionId(), nextNodeId + 1 );
  }

  @Test
  public void getName_whenNamesDisabled()
  {
    ArezTestUtil.disableNames();

    final Transaction transaction = new Transaction( Arez.context(), null, null, false, null );
    final IllegalStateException exception = expectThrows( IllegalStateException.class, transaction::getName );

    assertEquals( exception.getMessage(),
                  "Arez-0133: Transaction.getName() invoked when Arez.areNamesEnabled() is false" );
  }

  @Test
  public void constructorPassedContext_whenZonesDisabled()
  {
    ArezTestUtil.disableZones();
    ArezTestUtil.resetState();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Transaction( Arez.context(),
                                           null,
                                           ValueUtil.randomString(),
                                           false,
                                           null ) );

    assertEquals( exception.getMessage(),
                  "Arez-184: Transaction passed a context but Arez.areZonesEnabled() is false" );
  }

  @Test
  public void construct_withNameWhenNamesDisabled()
  {
    ArezTestUtil.disableNames();

    final IllegalStateException exception = expectThrows( IllegalStateException.class,
                                                          () -> new Transaction( Arez.context(),
                                                                                 null,
                                                                                 "X",
                                                                                 false,
                                                                                 null ) );

    assertEquals( exception.getMessage(),
                  "Arez-0131: Transaction passed a name 'X' but Arez.areNamesEnabled() is false" );
  }

  @Test
  public void construction_whenSpyDisabled()
  {
    ArezTestUtil.disableSpies();

    final Transaction transaction = new Transaction( Arez.context(), null, ValueUtil.randomString(), false, null );

    // Re-enable spy so can read field
    ArezTestUtil.enableSpies();

    assertEquals( transaction.getStartedAt(), 0 );
  }

  @Test
  public void construction_with_READ_WRITE_and_computedValue()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final ComputedValue<String> computedValue = context.computed( () -> "" );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Transaction( context,
                                           null,
                                           name,
                                           true,
                                           computedValue.getObserver() ) );

    assertEquals( exception.getMessage(),
                  "Arez-0132: Attempted to create transaction named '" + name +
                  "' with mode READ_WRITE when ComputedValue tracker specified." );
  }

  @Test
  public void rootTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction1 = new Transaction( context, null, ValueUtil.randomString(), false, null );
    final Transaction transaction2 = new Transaction( context, transaction1, ValueUtil.randomString(), false, null );

    assertEquals( transaction1.isRootTransaction(), true );
    assertEquals( transaction1.getRootTransaction(), transaction1 );

    assertEquals( transaction2.isRootTransaction(), false );
    assertEquals( transaction2.getRootTransaction(), transaction1 );
  }

  @Test
  public void rootTransaction_zonesDisabled()
  {
    ArezTestUtil.disableZones();

    final Transaction transaction1 = new Transaction( null, null, ValueUtil.randomString(), false, null );
    final Transaction transaction2 = new Transaction( null, transaction1, ValueUtil.randomString(), false, null );

    assertEquals( transaction1.isRootTransaction(), true );
    assertEquals( transaction1.getRootTransaction(), transaction1 );

    assertEquals( transaction2.isRootTransaction(), false );
    assertEquals( transaction2.getRootTransaction(), transaction1 );
  }

  @Test
  public void begin()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    assertEquals( tracker.getState(), Flags.STATE_INACTIVE );

    transaction.begin();

    //Just verify that it ultimately invokes beginTracking
    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void commit()
  {
    final ArezContext context = Arez.context();
    final ComputedValue<String> computedValue = context.computed( () -> "" );
    final Observer tracker = computedValue.getObserver();

    ensureDerivationHasObserver( tracker );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue1 = context.observable();
    final Observer derivation = context.computed( () -> "" ).getObserver();
    derivation.setState( Flags.STATE_UP_TO_DATE );
    final ObservableValue<?> observableValue2 = derivation.getComputedValue().getObservableValue();

    tracker.getDependencies().add( observableValue2 );
    observableValue2.getObservers().add( tracker );

    observableValue2.markAsPendingDeactivation();
    transaction.queueForDeactivation( observableValue2 );

    assertNotNull( transaction.getPendingDeactivations() );
    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertEquals( observableValue2.isActive(), true );
    assertEquals( observableValue2.isPendingDeactivation(), true );

    transaction.safeGetObservables().add( observableValue1 );
    final ArrayList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.commit();

    // The next code block essentially verifies it calls completeTracking
    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertEquals( tracker.getDependencies().contains( observableValue1 ), true );
    assertEquals( observableValue1.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );

    // This section essentially verifies processPendingDeactivations() is called
    assertEquals( observableValue2.isPendingDeactivation(), false );
    assertEquals( observableValue2.isActive(), false );
    assertEquals( observableValue2.getObserver(), derivation );
    assertEquals( derivation.getState(), Flags.STATE_INACTIVE );
  }

  @Test
  public void trackingCycleWithNoTracker()
  {
    final ArezContext context = Arez.context();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );

    final ObservableValue<?> observableValue = context.observable();

    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( transaction.getObservableValues(), null );

    //Transaction should perform no action during tracking if there is no associated tracker
    transaction.beginTracking();
    transaction.observe( observableValue );
    transaction.completeTracking();

    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( transaction.getObservableValues(), null );
  }

  @Test
  public void beginTracking_firstTracking()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    assertEquals( tracker.getState(), Flags.STATE_INACTIVE );

    transaction.beginTracking();

    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void beginTracking()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    ensureDerivationHasObserver( tracker );

    tracker.setState( Flags.STATE_STALE );
    assertEquals( tracker.getState(), Flags.STATE_STALE );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Flags.STATE_STALE );
    tracker.getDependencies().add( observableValue );
    observableValue.rawAddObserver( tracker );

    transaction.beginTracking();

    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void observe()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    transaction.beginTracking();

    final ObservableValue<?> observableValue = context.observable();

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue.getObservers().size(), 0 );
    assertNull( transaction.getObservableValues() );
    assertNotEquals( transaction.getId(), observableValue.getLastTrackerTransactionId() );
    assertEquals( transaction.hasTransactionUseOccured(), false );

    transaction.observe( observableValue );

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( transaction.getId(), observableValue.getLastTrackerTransactionId() );
    assertNotNull( transaction.getObservableValues() );

    assertTrue( transaction.getObservableValues().contains( observableValue ) );
    assertEquals( transaction.getObservableValues().size(), 1 );
    assertEquals( transaction.hasTransactionUseOccured(), true );
  }

  @Test
  public void observe_onDisposedObservable()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    transaction.beginTracking();

    final ObservableValue<?> observableValue = context.observable();

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue.getObservers().size(), 0 );
    assertNull( transaction.getObservableValues() );
    assertNotEquals( transaction.getId(), observableValue.getLastTrackerTransactionId() );

    observableValue.setWorkState( ObservableValue.DISPOSED );
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.observe( observableValue ) );

    assertEquals( exception.getMessage(),
                  "Arez-0142: Invoked observe on transaction named '" +
                  transaction.getName() + "' for observableValue named '" + observableValue.getName() +
                  "' where the observableValue is disposed." );
  }

  @Test
  public void observe_noObserveIfOwnedByTracker()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    transaction.beginTracking();

    final ObservableValue<?> observableValue = tracker.getComputedValue().getObservableValue();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.observe( observableValue ) );

    assertEquals( exception.getMessage(),
                  "Arez-0143: Invoked observe on transaction named '" +
                  transaction.getName() + "' for observableValue named '" + observableValue.getName() +
                  "' where the observableValue is owned by the tracker." );
  }

  @Test
  public void multipleObserves()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    transaction.beginTracking();

    final ObservableValue<?> observableValue = context.observable();

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue.getObservers().size(), 0 );
    assertNull( transaction.getObservableValues() );
    assertNotEquals( transaction.getId(), observableValue.getLastTrackerTransactionId() );

    transaction.observe( observableValue );
    transaction.observe( observableValue );
    transaction.observe( observableValue );
    transaction.observe( observableValue );

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( transaction.getId(), observableValue.getLastTrackerTransactionId() );
    assertNotNull( transaction.getObservableValues() );

    assertTrue( transaction.getObservableValues().contains( observableValue ) );
    assertEquals( transaction.getObservableValues().size(), 1 );
  }

  @Test
  public void multipleObservesNestedTransactionInBetween()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    transaction.beginTracking();

    final ObservableValue<?> observableValue = context.observable();

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue.getObservers().size(), 0 );
    assertNull( transaction.getObservableValues() );
    assertNotEquals( transaction.getId(), observableValue.getLastTrackerTransactionId() );

    transaction.observe( observableValue );
    transaction.observe( observableValue );

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( transaction.getId(), observableValue.getLastTrackerTransactionId() );
    assertNotNull( transaction.getObservableValues() );

    // Simulate a nested transaction that observes this observableValue by updating tracking id
    observableValue.setLastTrackerTransactionId( context.nextTransactionId() );

    transaction.observe( observableValue );
    transaction.observe( observableValue );

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( transaction.getId(), observableValue.getLastTrackerTransactionId() );
    assertNotNull( transaction.getObservableValues() );

    assertTrue( transaction.getObservableValues().contains( observableValue ) );

    // Two instances of same observableValue is expected as the LastTrackerTransactionId
    // failed to match causing duplicate to be added. This would normally be cleaned
    // up at later time in process during completeTracking()
    assertEquals( transaction.getObservableValues().size(), 2 );
  }

  @Test
  public void completeTracking_noObservables()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    assertEquals( transaction.getObservableValues(), null );

    final ArrayList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() == dependencies );
    assertEquals( tracker.getDependencies().size(), 0 );
  }

  @Test
  public void completeTracking_noTrackerButObservablesPresent_shouldFail()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );

    // This next line forces the creation of observables
    transaction.safeGetObservables();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction::completeTracking );

    assertEquals( exception.getMessage(),
                  "Arez-0154: Transaction named '" + transaction.getName() + "' has no associated " +
                  "tracker so _observableValues should be null but are not." );
  }

  @Test
  public void completeTracking_withBadTrackerState()
  {
    final ArezContext context = Arez.context();

    final Observer tracker = context.computed( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_INACTIVE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction::completeTracking );

    assertEquals( exception.getMessage(),
                  "Arez-0155: Transaction named '" + transaction.getName() + "' called completeTracking " +
                  "but _tracker state of INACTIVE is not expected when tracker has not been disposed." );
  }

  @Test
  public void completeTracking_withDisposedTracker()
  {
    final ArezContext context = Arez.context();

    final Observer tracker = context.computed( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    ensureDerivationHasObserver( tracker );

    tracker.markDependenciesLeastStaleObserverAsUpToDate();
    tracker.clearDependencies();
    tracker.setState( Flags.STATE_DISPOSED );

    // This dependency "retained" (until tracker disposed)
    final ObservableValue<?> observableValue1 = context.observable();
    // This dependency no longer observed
    final ObservableValue<?> observableValue2 = context.observable();
    // This dependency newly observed (until tracker disposed)
    final ObservableValue<?> observableValue3 = context.observable();

    tracker.getDependencies().add( observableValue1 );
    tracker.getDependencies().add( observableValue2 );

    observableValue1.getObservers().add( tracker );
    observableValue2.getObservers().add( tracker );

    transaction.safeGetObservables().add( observableValue1 );
    transaction.safeGetObservables().add( observableValue3 );

    final ArrayList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Flags.STATE_DISPOSED );
    final ArrayList<ObservableValue<?>> dependencies1 = tracker.getDependencies();
    assertTrue( dependencies1 != dependencies );
    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue1.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue2.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue3.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_addingNewObservableWhenTrackerIsAlreadyStaleAndObservableHasAnotherStaleObserver()
  {
    /*
     * This scenario very was a long standing but infrequently triggered bug. It has been fixed but this
     * somewhat specific test exists to avoid it ever returning.
     */
    final ArezContext context = Arez.context();
    final Observer tracker = newReadWriteObserver( context );
    final Observer observer2 = newReadWriteObserver( context );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_STALE );
    observer2.setState( Flags.STATE_STALE );

    // Setup existing observableValue dependency
    final ObservableValue<?> observableValue = context.observable();

    observer2.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer2 );
    observableValue.setLeastStaleObserverState( observer2.getState() );

    final ArrayList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.observe( observableValue );

    transaction.completeTracking();

    assertEquals( tracker.getState(), Flags.STATE_STALE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue.getObservers().size(), 2 );
    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_STALE );
  }

  @Test
  public void completeTracking_noNewObservablesButExistingObservables()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    // Setup existing observable dependency
    final ObservableValue<?> observableValue1 = context.observable();
    tracker.getDependencies().add( observableValue1 );
    observableValue1.getObservers().add( tracker );

    final ArrayList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue1.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue1.getObservers().size(), 0 );
  }

  @Test
  public void completeTracking_singleObservable()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();

    ensureDerivationHasObserver( tracker );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = context.observable();

    transaction.safeGetObservables().add( observableValue );

    final ArrayList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertEquals( tracker.getDependencies().contains( observableValue ), true );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_multipleObservable()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();

    ensureDerivationHasObserver( tracker );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue1 = context.observable();
    final ObservableValue<?> observableValue2 = context.observable();
    final ObservableValue<?> observableValue3 = context.observable();
    final ObservableValue<?> observableValue4 = context.observable();

    transaction.safeGetObservables().add( observableValue1 );
    transaction.safeGetObservables().add( observableValue2 );
    transaction.safeGetObservables().add( observableValue3 );
    transaction.safeGetObservables().add( observableValue4 );

    final ArrayList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 4 );
    assertEquals( tracker.getDependencies().contains( observableValue1 ), true );
    assertEquals( observableValue1.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue2.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue3.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue4.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_singleObservableMultipleEntries()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();

    ensureDerivationHasObserver( tracker );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = context.observable();

    transaction.safeGetObservables().add( observableValue );
    transaction.safeGetObservables().add( observableValue );
    transaction.safeGetObservables().add( observableValue );
    transaction.safeGetObservables().add( observableValue );

    final ArrayList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertEquals( tracker.getDependencies().contains( observableValue ), true );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_multipleObservableMultipleEntries()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();

    ensureDerivationHasObserver( tracker );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue1 = context.observable();
    final ObservableValue<?> observableValue2 = context.observable();

    /*
     * Forcing the order 1, 1, 2, 1 means that the sequence needs to be collapsed and 2 will be moved so 1, 2
     */
    transaction.safeGetObservables().add( observableValue1 );
    transaction.safeGetObservables().add( observableValue1 );
    transaction.safeGetObservables().add( observableValue2 );
    transaction.safeGetObservables().add( observableValue1 );

    final ArrayList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 2 );
    assertEquals( tracker.getDependencies().contains( observableValue1 ), true );
    assertEquals( tracker.getDependencies().contains( observableValue2 ), true );
    assertEquals( observableValue1.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue2.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_matchingExistingDependencies()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    final ObservableValue<?> observableValue1 = context.observable();
    final ObservableValue<?> observableValue2 = context.observable();

    tracker.getDependencies().add( observableValue1 );
    tracker.getDependencies().add( observableValue2 );

    observableValue1.getObservers().add( tracker );
    observableValue2.getObservers().add( tracker );

    transaction.safeGetObservables().add( observableValue1 );
    transaction.safeGetObservables().add( observableValue2 );

    final ArrayList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() == dependencies );
    assertEquals( tracker.getDependencies().size(), 2 );
    assertEquals( tracker.getDependencies().contains( observableValue1 ), true );
    assertEquals( tracker.getDependencies().contains( observableValue2 ), true );
    assertEquals( observableValue1.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue2.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_dependenciesChanged()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();

    ensureDerivationHasObserver( tracker );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    // This dependency retained
    final ObservableValue<?> observableValue1 = context.observable();
    // This dependency no longer observed
    final ObservableValue<?> observableValue2 = context.observable();
    // This dependency newly observed
    final ObservableValue<?> observableValue3 = context.observable();

    tracker.getDependencies().add( observableValue1 );
    tracker.getDependencies().add( observableValue2 );

    observableValue1.getObservers().add( tracker );
    observableValue2.getObservers().add( tracker );

    transaction.safeGetObservables().add( observableValue1 );
    transaction.safeGetObservables().add( observableValue3 );

    final ArrayList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 2 );
    assertEquals( tracker.getDependencies().contains( observableValue1 ), true );
    assertEquals( tracker.getDependencies().contains( observableValue3 ), true );
    assertEquals( observableValue1.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue2.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue3.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_calculatedObservableUpToDate()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    final Observer derivation = context.computed( () -> "" ).getObserver();
    derivation.setState( Flags.STATE_UP_TO_DATE );
    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    tracker.getDependencies().add( observableValue );
    observableValue.getObservers().add( tracker );
    transaction.safeGetObservables().add( observableValue );

    final ArrayList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() == dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertEquals( tracker.getDependencies().contains( observableValue ), true );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_calculatedObservableStale()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    final Observer derivation = context.computed( () -> "" ).getObserver();
    derivation.setState( Flags.STATE_STALE );
    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    tracker.getDependencies().add( observableValue );
    observableValue.getObservers().add( tracker );
    transaction.safeGetObservables().add( observableValue );

    final ArrayList<ObservableValue<?>> dependencies = tracker.getDependencies();

    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );

    transaction.completeTracking();

    assertEquals( tracker.getState(), Flags.STATE_STALE );
    assertTrue( tracker.getDependencies() == dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertEquals( tracker.getDependencies().contains( observableValue ), true );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );

    // Make sure the derivation observer has state updated
    assertEquals( tracker.getState(), Flags.STATE_STALE );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void completeTracking_calculatedObservableStale_mustUpdateNewDependencies()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = Arez.context().computed( () -> "" ).getObserver();

    setCurrentTransaction( tracker );

    ensureDerivationHasObserver( tracker );
    tracker.setState( Flags.STATE_UP_TO_DATE );

    final Observer derivation = context.computed( () -> "" ).getObserver();
    derivation.setState( Flags.STATE_POSSIBLY_STALE );

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_POSSIBLY_STALE );

    Transaction.setTransaction( null );
    final Observer observer = context.observer( new CountAndObserveProcedure() );
    setupReadWriteTransaction();
    observer.setState( Flags.STATE_STALE );

    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    setCurrentTransaction( tracker );

    Transaction.current().safeGetObservables().add( observableValue );

    Transaction.current().completeTracking();

    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertEquals( tracker.getDependencies().contains( observableValue ), true );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );

    // Make sure the derivation observer has state updated
    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void completeTracking_calculatedObservable_noObservers_queuedForDeactivation()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    final Observer derivation = context.computed( () -> "" ).getObserver();
    derivation.setState( Flags.STATE_STALE );
    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    tracker.getDependencies().add( observableValue );
    observableValue.getObservers().add( tracker );

    final ArrayList<ObservableValue<?>> dependencies = tracker.getDependencies();

    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );

    transaction.completeTracking();

    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );

    final ArrayList<ObservableValue> pendingDeactivations = transaction.getPendingDeactivations();
    assertNotNull( pendingDeactivations );
    assertTrue( pendingDeactivations.contains( tracker.getComputedValue().getObservableValue() ) );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void completeTracking_calculatedObservable_noObservers_keepAlive()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "", Flags.KEEPALIVE | Flags.RUN_LATER ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    final Observer derivation = context.computed( () -> "" ).getObserver();
    derivation.setState( Flags.STATE_STALE );
    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    tracker.getDependencies().add( observableValue );
    observableValue.getObservers().add( tracker );

    final ArrayList<ObservableValue<?>> dependencies = tracker.getDependencies();

    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );

    transaction.completeTracking();

    assertEquals( tracker.getState(), Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );

    final ArrayList<ObservableValue> pendingDeactivations = transaction.getPendingDeactivations();
    assertNotNull( pendingDeactivations );
    assertFalse( pendingDeactivations.contains( tracker.getComputedValue().getObservableValue() ) );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void queueForDeactivation_singleObserver()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<String> observableValue = context.computed( () -> "" ).getObservableValue();

    assertNull( transaction.getPendingDeactivations() );

    transaction.queueForDeactivation( observableValue );

    assertNotNull( transaction.getPendingDeactivations() );

    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertEquals( transaction.getPendingDeactivations().contains( observableValue ), true );
  }

  @Test
  public void queueForDeactivation_nestedTransaction()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();

    final Transaction transaction1 = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    final Transaction transaction2 = new Transaction( context, transaction1, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction2 );
    tracker.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<String> observableValue = context.computed( () -> "" ).getObservableValue();

    assertNull( transaction1.getPendingDeactivations() );
    assertNull( transaction2.getPendingDeactivations() );

    transaction2.queueForDeactivation( observableValue );

    assertNotNull( transaction1.getPendingDeactivations() );
    assertNotNull( transaction2.getPendingDeactivations() );

    // Pending deactivations list in both transactions should be the same instance
    assertTrue( transaction1.getPendingDeactivations() == transaction2.getPendingDeactivations() );

    assertEquals( transaction1.getPendingDeactivations().size(), 1 );
    assertEquals( transaction1.getPendingDeactivations().contains( observableValue ), true );
    assertEquals( transaction2.getPendingDeactivations().size(), 1 );
    assertEquals( transaction2.getPendingDeactivations().contains( observableValue ), true );
  }

  @Test
  public void queueForDeactivation_observerAlreadyDeactivated()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<String> observableValue = context.computed( () -> "" ).getObservableValue();

    transaction.queueForDeactivation( observableValue );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.queueForDeactivation( observableValue ) );

    assertEquals( exception.getMessage(),
                  "Arez-0141: Invoked queueForDeactivation on transaction named '" +
                  transaction.getName() + "' for observableValue named '" + observableValue.getName() +
                  "' when pending deactivation already exists for observableValue." );
  }

  @Test
  public void queueForDeactivation_observerCanNotDeactivate()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computed( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = context.observable();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.queueForDeactivation( observableValue ) );

    assertEquals( exception.getMessage(),
                  "Arez-0140: Invoked queueForDeactivation on transaction named '" +
                  transaction.getName() + "' for observableValue named '" + observableValue.getName() +
                  "' when observableValue can not be deactivated." );
  }

  @Test
  public void processPendingDeactivations_observableHasNoListeners()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
    Transaction.setTransaction( transaction );

    final Observer derivation = context.computed( () -> "" ).getObserver();
    derivation.setState( Flags.STATE_UP_TO_DATE );
    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    observableValue.markAsPendingDeactivation();
    transaction.queueForDeactivation( observableValue );

    assertNotNull( transaction.getPendingDeactivations() );
    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertEquals( transaction.getPendingDeactivations().contains( observableValue ), true );
    assertEquals( observableValue.isActive(), true );
    assertEquals( observableValue.isPendingDeactivation(), true );

    final int deactivationCount = transaction.processPendingDeactivations();
    assertEquals( deactivationCount, 1 );

    assertEquals( observableValue.isPendingDeactivation(), false );
    assertEquals( observableValue.isActive(), false );
    assertEquals( observableValue.getObserver(), derivation );
    assertEquals( derivation.getState(), Flags.STATE_INACTIVE );
  }

  @Test
  public void processPendingDeactivations_noDeactivationsPending()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );

    assertNull( transaction.getPendingDeactivations() );

    final int deactivationCount = transaction.processPendingDeactivations();
    assertEquals( deactivationCount, 0 );

    assertNull( transaction.getPendingDeactivations() );
  }

  @Test
  public void processPendingDeactivations_observableHasListener()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
    Transaction.setTransaction( transaction );

    final Observer otherObserver = context.computed( () -> "" ).getObserver();
    otherObserver.setState( Flags.STATE_UP_TO_DATE );

    final Observer derivation = context.computed( () -> "" ).getObserver();
    derivation.setState( Flags.STATE_UP_TO_DATE );
    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    observableValue.markAsPendingDeactivation();
    transaction.queueForDeactivation( observableValue );
    otherObserver.getDependencies().add( observableValue );
    observableValue.rawAddObserver( otherObserver );

    assertNotNull( transaction.getPendingDeactivations() );
    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertEquals( transaction.getPendingDeactivations().contains( observableValue ), true );
    assertEquals( observableValue.isActive(), true );
    assertEquals( observableValue.isPendingDeactivation(), true );

    final int deactivationCount = transaction.processPendingDeactivations();
    assertEquals( deactivationCount, 0 );

    assertEquals( observableValue.isPendingDeactivation(), false );
    assertEquals( observableValue.isActive(), true );
    assertEquals( derivation.getState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void processPendingDeactivations_observableHasListenerAndIsDisposed()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
    Transaction.setTransaction( transaction );

    final Observer otherObserver = context.computed( () -> "" ).getObserver();
    otherObserver.setState( Flags.STATE_UP_TO_DATE );

    final Observer derivation = context.computed( () -> "" ).getObserver();
    derivation.setState( Flags.STATE_UP_TO_DATE );
    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    observableValue.markAsPendingDeactivation();
    transaction.queueForDeactivation( observableValue );
    otherObserver.getDependencies().add( observableValue );
    observableValue.rawAddObserver( otherObserver );

    observableValue.setWorkState( ObservableValue.DISPOSED );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction::processPendingDeactivations );

    assertEquals( exception.getMessage(),
                  "Arez-0139: Attempting to deactivate disposed observableValue named '" +
                  observableValue.getName() + "' in transaction named '" + transaction.getName() +
                  "' but the observableValue still has observers." );

  }

  @Test
  public void processPendingDeactivations_causesMorePendingDeactivations()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );

    //Setup transaction as queueForDeactivation retrieves trnsaction from context
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue1 = context.observable();

    final Observer derivation1 = context.computed( () -> "" ).getObserver();
    derivation1.setState( Flags.STATE_UP_TO_DATE );

    derivation1.getDependencies().add( observableValue1 );
    observableValue1.getObservers().add( derivation1 );

    final ObservableValue<?> observableValue2 = derivation1.getComputedValue().getObservableValue();

    final Observer derivation2 = context.computed( () -> "" ).getObserver();
    derivation2.setState( Flags.STATE_UP_TO_DATE );

    derivation2.getDependencies().add( observableValue2 );
    observableValue2.getObservers().add( derivation2 );

    final ObservableValue<?> observableValue3 = derivation2.getComputedValue().getObservableValue();

    observableValue3.markAsPendingDeactivation();
    transaction.queueForDeactivation( observableValue3 );

    assertNotNull( transaction.getPendingDeactivations() );
    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertEquals( transaction.getPendingDeactivations().contains( observableValue3 ), true );
    assertEquals( observableValue3.isActive(), true );
    assertEquals( observableValue3.isPendingDeactivation(), true );

    final int deactivationCount = transaction.processPendingDeactivations();

    //Chained calculated derivation is deactivated
    assertEquals( deactivationCount, 2 );

    assertEquals( observableValue3.isPendingDeactivation(), false );
    assertEquals( observableValue3.isActive(), false );
    assertEquals( observableValue3.getObserver(), derivation2 );
    assertEquals( observableValue2.isPendingDeactivation(), false );
    assertEquals( observableValue2.isActive(), false );
    assertEquals( observableValue2.getObserver(), derivation1 );
    assertEquals( observableValue1.isPendingDeactivation(), false );
    assertEquals( observableValue1.isActive(), true );
    assertEquals( observableValue1.isComputedValue(), false );
    assertEquals( derivation2.getState(), Flags.STATE_INACTIVE );
    assertEquals( derivation2.getDependencies().size(), 0 );
    assertEquals( observableValue2.getObservers().size(), 0 );
    assertEquals( derivation1.getState(), Flags.STATE_INACTIVE );
    assertEquals( derivation1.getDependencies().size(), 0 );
    assertEquals( observableValue1.getObservers().size(), 0 );
  }

  @Test
  public void processPendingDeactivations_producesNoMorePendingDeactivations()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> baseObservableValue = context.observable();
    final Observer derivation = context.computed( () -> "" ).getObserver();
    derivation.setState( Flags.STATE_UP_TO_DATE );

    derivation.getDependencies().add( baseObservableValue );
    baseObservableValue.getObservers().add( derivation );

    final ObservableValue derivedObservableValue = derivation.getComputedValue().getObservableValue();

    derivedObservableValue.markAsPendingDeactivation();
    transaction.queueForDeactivation( derivedObservableValue );

    assertNotNull( transaction.getPendingDeactivations() );
    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertEquals( transaction.getPendingDeactivations().contains( derivedObservableValue ), true );
    assertEquals( derivedObservableValue.isActive(), true );
    assertEquals( derivedObservableValue.isPendingDeactivation(), true );

    final int deactivationCount = transaction.processPendingDeactivations();

    //baseObservableValue is not active so it needs deactivation
    assertEquals( deactivationCount, 1 );

    assertEquals( derivedObservableValue.isPendingDeactivation(), false );
    assertEquals( derivedObservableValue.isActive(), false );
    assertEquals( derivedObservableValue.getObserver(), derivation );
    assertEquals( derivation.getState(), Flags.STATE_INACTIVE );
    assertEquals( derivation.getDependencies().size(), 0 );
    assertEquals( baseObservableValue.getObservers().size(), 0 );
  }

  @Test
  public void processPendingDeactivations_calledOnNonRootTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction1 = new Transaction( context, null, ValueUtil.randomString(), false, null );
    final Transaction transaction2 = new Transaction( context, transaction1, ValueUtil.randomString(), false, null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction2::processPendingDeactivations );

    assertEquals( exception.getMessage(),
                  "Arez-0138: Invoked processPendingDeactivations on transaction named '" +
                  transaction2.getName() + "' which is not the root transaction." );
  }

  @Test
  public void verifyWriteAllowed_withReadOnlyTransaction_enforceTransactionType_set_to_false()
  {
    ArezTestUtil.noEnforceTransactionType();

    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );

    final ObservableValue<?> observableValue = context.observable();

    //Should produce no error
    transaction.verifyWriteAllowed( observableValue );
  }

  @Test
  public void verifyWriteAllowed_withReadOnlyTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );

    final ObservableValue<?> observableValue = context.observable();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.verifyWriteAllowed( observableValue ) );

    assertEquals( exception.getMessage(),
                  "Arez-0152: Transaction named '" + transaction.getName() + "' attempted to change " +
                  "ObservableValue named '" + observableValue.getName() + "' but the transaction mode is READ_ONLY." );
  }

  @Test
  public void verifyWriteAllowed_withReadWriteTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );

    final ObservableValue<?> observableValue = context.observable();

    //Should produce no error
    transaction.verifyWriteAllowed( observableValue );
  }

  @Test
  public void verifyWriteAllowed_withReadWriteOwnedTransaction()
  {
    final ArezContext context = Arez.context();

    final Observer tracker = context.computed( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );

    final ObservableValue<?> observableValue1 = tracker.getComputedValue().getObservableValue();
    final ObservableValue<?> observableValue2 = context.observable();

    tracker.getDependencies().add( observableValue2 );
    observableValue2.getObservers().add( tracker );

    //Should produce no error as it is owned by derivation
    transaction.verifyWriteAllowed( observableValue1 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.verifyWriteAllowed( observableValue2 ) );

    assertEquals( exception.getMessage(),
                  "Arez-0153: Transaction named '" + transaction.getName() + "' attempted to change " +
                  "ObservableValue named '" + observableValue2.getName() + "' and the transaction mode is " +
                  "READ_WRITE_OWNED but the ObservableValue has not been created by the transaction." );
  }

  @Test
  public void reportChanged_noObservers()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( transaction.hasTransactionUseOccured(), false );

    transaction.reportChanged( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( transaction.hasTransactionUseOccured(), true );
  }

  @Test
  public void reportChanged_singleUpToDateObserver()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computed( () -> "" ).getObserver();
    observer.setState( Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( transaction.hasTransactionUseOccured(), false );

    Transaction.setTransaction( transaction );
    transaction.reportChanged( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_STALE );
    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertEquals( transaction.hasTransactionUseOccured(), true );
  }

  @Test
  public void reportChanged_withDisposedObservable()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computed( () -> "" ).getObserver();
    observer.setState( Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( transaction );

    observableValue.setWorkState( ObservableValue.DISPOSED );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportChanged( observableValue ) );

    assertEquals( exception.getMessage(),
                  "Arez-0144: Invoked reportChanged on transaction named '" + transaction.getName() +
                  "' for ObservableValue named '" + observableValue.getName() + "' where the ObservableValue " +
                  "is disposed." );
  }

  @Test
  public void reportChanged_readOnlyTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computed( () -> "" ).getObserver();
    observer.setState( Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( transaction );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportChanged( observableValue ) );

    assertEquals( exception.getMessage(),
                  "Arez-0152: Transaction named '" + transaction.getName() + "' attempted to change " +
                  "ObservableValue named '" + observableValue.getName() + "' but the transaction mode is READ_ONLY." );
  }

  @Test
  public void reportChanged_singleObserver_notTrackingState()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computed( () -> "" ).getObserver();
    observer.setState( Flags.STATE_INACTIVE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Flags.STATE_INACTIVE );

    Transaction.setTransaction( transaction );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportChanged( observableValue ) );

    assertEquals( exception.getMessage(),
                  "Arez-0145: Transaction named '" + transaction.getName() + "' has attempted to " +
                  "explicitly change observableValue named '" + observableValue.getName() + "' and observableValue " +
                  "is in unexpected state INACTIVE." );
  }

  @Test
  public void reportChanged_singleObserver_possiblyStaleState()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Flags.STATE_POSSIBLY_STALE );

    final Observer observer = context.computed( () -> "" ).getObserver();
    observer.setState( Flags.STATE_POSSIBLY_STALE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer.getState(), Flags.STATE_POSSIBLY_STALE );

    Transaction.setTransaction( transaction );

    transaction.reportChanged( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_STALE );
    assertEquals( observer.getState(), Flags.STATE_STALE );
  }

  @Test
  public void reportChanged_multipleObservers()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    final Observer observer1 = context.computed( () -> "" ).getObserver();
    observer1.setState( Flags.STATE_UP_TO_DATE );
    observer1.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer1 );

    final Observer observer2 = context.computed( () -> "" ).getObserver();
    observer2.setState( Flags.STATE_STALE );
    observer2.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer2 );

    final Observer observer3 = context.computed( () -> "" ).getObserver();
    observer3.setState( Flags.STATE_STALE );
    observer3.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer3 );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer1.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer2.getState(), Flags.STATE_STALE );
    assertEquals( observer3.getState(), Flags.STATE_STALE );

    Transaction.setTransaction( transaction );
    transaction.reportChanged( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_STALE );
    assertEquals( observer1.getState(), Flags.STATE_STALE );
    assertEquals( observer2.getState(), Flags.STATE_STALE );
    assertEquals( observer3.getState(), Flags.STATE_STALE );
  }

  @Test
  public void reportPossiblyChanged_singleUpToDateObserver()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computed( () -> "" ).getObserver();
    calculator.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computed( () -> "" ).getObserver();
    observer.setState( Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( transaction );
    transaction.reportPossiblyChanged( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer.getState(), Flags.STATE_POSSIBLY_STALE );
  }

  @Test
  public void reportPossiblyChanged_onDisposedObservable()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computed( () -> "" ).getObserver();
    calculator.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computed( () -> "" ).getObserver();
    observer.setState( Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( transaction );

    observableValue.setWorkState( ObservableValue.DISPOSED );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportPossiblyChanged( observableValue ) );

    assertEquals( exception.getMessage(),
                  "Arez-0146: Invoked reportPossiblyChanged on transaction named '" + transaction.getName() +
                  "' for ObservableValue named '" + observableValue.getName() + "' where the " +
                  "ObservableValue is disposed." );
  }

  @Test
  public void reportPossiblyChanged_noObserver()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computed( () -> "" ).getObserver();
    calculator.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputedValue().getObservableValue();

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );

    transaction.reportPossiblyChanged( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void reportPossiblyChanged_ownedObserver_transaction_READ_WRITE_OWNED()
  {
    final ArezContext context = Arez.context();

    final Observer calculator = context.computed( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, calculator );
    Transaction.setTransaction( transaction );

    calculator.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    calculator.getDependencies().add( observableValue );
    observableValue.getObservers().add( calculator );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( calculator.getState(), Flags.STATE_UP_TO_DATE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportPossiblyChanged( observableValue ) );

    assertEquals( exception.getMessage(),
                  "Arez-0014: Observer named '" + calculator.getName() + "' attempted to schedule itself " +
                  "during read-only tracking transaction. Observers that are supporting ComputedValue instances must" +
                  " not schedule self." );
  }

  @Test
  public void reportPossiblyChanged_nonOwnedObserver_transaction_READ_WRITE_OWNED()
  {
    final ArezContext context = Arez.context();

    final Observer tracker = context.computed( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    final Observer calculator = context.computed( () -> "" ).getObserver();
    calculator.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    calculator.getDependencies().add( observableValue );
    observableValue.getObservers().add( calculator );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( calculator.getState(), Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( transaction );

    // A computed that causes another computed to recalculate should be allowed
    transaction.reportPossiblyChanged( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_POSSIBLY_STALE );
  }

  @Test
  public void reportPossiblyChanged_readOnlyTransaction()
  {
    final ArezContext context = Arez.context();

    setupReadOnlyTransaction();

    final Observer calculator = context.computed( () -> "" ).getObserver();
    calculator.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computed( () -> "" ).getObserver();
    observer.setState( Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    // A read-only transaction can not cause a computed to recalculate

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.current().reportPossiblyChanged( observableValue ) );

    assertEquals( exception.getMessage(),
                  "Arez-0148: Transaction named '" + Transaction.current().getName() + "' attempted to " +
                  "call reportPossiblyChanged in read-only transaction." );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void reportPossiblyChanged_where_observable_is_not_derived()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computed( () -> "" ).getObserver();
    observer.setState( Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( transaction );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportPossiblyChanged( observableValue ) );

    assertEquals( exception.getMessage(),
                  "Arez-0147: Transaction named '" + transaction.getName() + "' has attempted to mark " +
                  "ObservableValue named '" + observableValue.getName() + "' as potentially changed " +
                  "but ObservableValue is not a derived value." );
  }

  @Test
  public void reportPossiblyChanged_multipleObservers()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computed( () -> "" ).getObserver();
    calculator.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    final Observer observer1 = context.computed( () -> "" ).getObserver();
    observer1.setState( Flags.STATE_UP_TO_DATE );
    observer1.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer1 );

    final Observer observer2 = context.computed( () -> "" ).getObserver();
    observer2.setState( Flags.STATE_POSSIBLY_STALE );
    observer2.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer2 );

    final Observer observer3 = context.computed( () -> "" ).getObserver();
    observer3.setState( Flags.STATE_STALE );
    observer3.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer3 );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer1.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer2.getState(), Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer3.getState(), Flags.STATE_STALE );

    Transaction.setTransaction( transaction );
    transaction.reportPossiblyChanged( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer1.getState(), Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer2.getState(), Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer3.getState(), Flags.STATE_STALE );
  }

  @Test
  public void reportChangeConfirmed_singleUpToDateObserver()
  {
    final ArezContext context = Arez.context();

    final Observer calculator = context.computed( () -> "" ).getObserver();
    setCurrentTransaction( calculator );

    calculator.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    calculator.getDependencies().add( observableValue );
    observableValue.getObservers().add( calculator );

    context.getTransaction().reportChangeConfirmed( observableValue );

    // Assume observer is being updated so keep that state
    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( calculator.getState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void reportChangeConfirmed_disposedObservable()
  {
    final ArezContext context = Arez.context();

    final Observer calculator = context.computed( () -> "" ).getObserver();
    setCurrentTransaction( calculator );

    calculator.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    calculator.getDependencies().add( observableValue );
    observableValue.getObservers().add( calculator );

    observableValue.setWorkState( ObservableValue.DISPOSED );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.getTransaction().reportChangeConfirmed( observableValue ) );

    assertEquals( exception.getMessage(),
                  "Arez-0149: Invoked reportChangeConfirmed on transaction named '" +
                  context.getTransaction().getName() + "' for ObservableValue named '" + observableValue.getName() +
                  "' where the ObservableValue is disposed." );
  }

  @Test
  public void reportChangeConfirmed_noObservers()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computed( () -> "" ).getObserver();
    calculator.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputedValue().getObservableValue();

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );

    transaction.reportChangeConfirmed( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void reportChangeConfirmed_singlePossiblyStaleObserver()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computed( () -> "" ).getObserver();
    calculator.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_POSSIBLY_STALE );

    final Observer observer = context.computed( () -> "" ).getObserver();
    observer.setState( observableValue.getLeastStaleObserverState() );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    Transaction.setTransaction( transaction );
    transaction.reportChangeConfirmed( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_STALE );
    assertEquals( observer.getState(), Flags.STATE_STALE );
  }

  @Test
  public void reportChangeConfirmed_singleStaleObserver()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computed( () -> "" ).getObserver();
    calculator.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_STALE );

    final Observer observer = context.computed( () -> "" ).getObserver();
    observer.setState( observableValue.getLeastStaleObserverState() );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    Transaction.setTransaction( transaction );
    transaction.reportChangeConfirmed( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_STALE );
    assertEquals( observer.getState(), Flags.STATE_STALE );
  }

  @Test
  public void reportChangeConfirmed_ownedObserver_transaction_READ_WRITE_OWNED()
  {
    final ArezContext context = Arez.context();

    final Observer calculator = context.computed( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, calculator );
    Transaction.setTransaction( transaction );

    calculator.setState( Flags.STATE_POSSIBLY_STALE );

    final ObservableValue<?> observableValue = calculator.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_POSSIBLY_STALE );

    calculator.getDependencies().add( observableValue );
    observableValue.getObservers().add( calculator );

    Transaction.setTransaction( transaction );
    transaction.reportChangeConfirmed( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_STALE );
    assertEquals( calculator.getState(), Flags.STATE_STALE );
  }

  @Test
  public void reportChangeConfirmed_nonOwnedObserver_transaction_READ_WRITE_OWNED()
  {
    final ArezContext context = Arez.context();

    final Observer tracker = context.computed( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker );
    Transaction.setTransaction( transaction );

    tracker.setState( Flags.STATE_UP_TO_DATE );

    final Observer calculator = context.computed( () -> "" ).getObserver();
    calculator.setState( Flags.STATE_POSSIBLY_STALE );

    final ObservableValue<?> observableValue = calculator.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_POSSIBLY_STALE );

    calculator.getDependencies().add( observableValue );
    observableValue.getObservers().add( calculator );

    Transaction.setTransaction( transaction );
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportChangeConfirmed( observableValue ) );

    assertEquals( exception.getMessage(),
                  "Arez-0153: Transaction named '" + transaction.getName() + "' attempted to change " +
                  "ObservableValue named '" + observableValue.getName() + "' and the transaction mode is " +
                  "READ_WRITE_OWNED but the ObservableValue has not been created by the transaction." );
  }

  @Test
  public void reportChangeConfirmed_readOnlyTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computed( () -> "" ).getObserver();
    calculator.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computed( () -> "" ).getObserver();
    observer.setState( Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( transaction );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportChangeConfirmed( observableValue ) );

    assertEquals( exception.getMessage(),
                  "Arez-0152: Transaction named '" + transaction.getName() + "' attempted to change " +
                  "ObservableValue named '" + observableValue.getName() + "' but the transaction mode is READ_ONLY." );
  }

  @Test
  public void reportChangeConfirmed_where_observable_is_not_derived()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computed( () -> "" ).getObserver();
    observer.setState( Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportChangeConfirmed( observableValue ) );

    assertEquals( exception.getMessage(),
                  "Arez-0150: Transaction named '" + transaction.getName() + "' has attempted to mark " +
                  "ObservableValue named '" + observableValue.getName() + "' as potentially changed but " +
                  "ObservableValue is not a derived value." );
  }

  @Test
  public void reportChangeConfirmed_multipleObservers()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computed( () -> "" ).getObserver();
    calculator.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    final Observer observer1 = context.computed( () -> "" ).getObserver();
    observer1.setState( Flags.STATE_POSSIBLY_STALE );
    observer1.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer1 );

    final Observer observer2 = context.computed( () -> "" ).getObserver();
    observer2.setState( Flags.STATE_POSSIBLY_STALE );
    observer2.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer2 );

    final Observer observer3 = context.computed( () -> "" ).getObserver();
    observer3.setState( Flags.STATE_STALE );
    observer3.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer3 );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer1.getState(), Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer2.getState(), Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer3.getState(), Flags.STATE_STALE );

    Transaction.setTransaction( transaction );
    transaction.reportChangeConfirmed( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_STALE );
    assertEquals( observer1.getState(), Flags.STATE_STALE );
    assertEquals( observer2.getState(), Flags.STATE_STALE );
    assertEquals( observer3.getState(), Flags.STATE_STALE );
  }

  @Test
  public void invariantObserverIsTracker_notTracker()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observable = context.observable();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null );
    Transaction.setTransaction( transaction );

    observable.rawAddObserver( observer );
    observer.getDependencies().add( observable );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> transaction.invariantObserverIsTracker( observable, observer ) );

    assertEquals( exception.getMessage(),
                  "Arez-0151: Transaction named '" + transaction.getName() + "' attempted to call " +
                  "reportChangeConfirmed for ObservableValue named '" + observable.getName() + "' and found a " +
                  "dependency named '" + observer.getName() + "' that is UP_TO_DATE but is not the tracker of " +
                  "any transactions in the hierarchy: [" + transaction.getName() + "]." );
  }

  @Test
  public void invariantObserverIsTracker_trackerOfSurroundingTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue = context.observable();
    final Observer observer = context.observer( new CountAndObserveProcedure() );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, observer );
    Transaction.setTransaction( transaction );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    transaction.invariantObserverIsTracker( observableValue, observer );
  }

  @Test
  public void invariantObserverIsTracker_trackerUpTransactionHierarchy()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue = context.observable();
    final Observer observer = context.observer( new CountAndObserveProcedure() );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, observer );
    final Transaction transaction2 = new Transaction( context, transaction, ValueUtil.randomString(), true, null );
    final Transaction transaction3 = new Transaction( context,
                                                      transaction2,
                                                      ValueUtil.randomString(),
                                                      true,
                                                      context.observer( new CountAndObserveProcedure() ) );
    final Transaction transaction4 = new Transaction( context,
                                                      transaction3,
                                                      ValueUtil.randomString(),
                                                      true,
                                                      context.observer( new CountAndObserveProcedure() ) );
    Transaction.setTransaction( transaction4 );

    observableValue.rawAddObserver( observer );
    observer.getDependencies().add( observableValue );

    transaction.invariantObserverIsTracker( observableValue, observer );
  }

  @Test
  public void getStartedAt_whenSpyDisabled()
  {
    ArezTestUtil.disableSpies();

    final Transaction transaction = new Transaction( Arez.context(), null, ValueUtil.randomString(), false, null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction::getStartedAt );

    assertEquals( exception.getMessage(),
                  "Arez-0134: Transaction.getStartedAt() invoked when Arez.areSpiesEnabled() is false" );
  }

  @Test
  public void asInfo_whenSpyDisabled()
  {
    ArezTestUtil.disableSpies();

    final Transaction transaction = new Transaction( Arez.context(), null, ValueUtil.randomString(), false, null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction::asInfo );

    assertEquals( exception.getMessage(),
                  "Arez-0198: TransactionInfo.asInfo() invoked but Arez.areSpiesEnabled() returned false." );
  }

  @Test
  public void beginTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final String name = ValueUtil.randomString();
    final Observer tracker = null;
    final Transaction transaction = Transaction.begin( context, name, false, tracker );

    assertTrue( context.isTransactionActive() );

    assertEquals( context.getTransaction(), transaction );
    assertEquals( transaction.getContext(), context );
    assertEquals( transaction.getName(), name );
    assertEquals( transaction.isMutation(), false );
    assertEquals( transaction.getTracker(), tracker );
    assertEquals( transaction.getPrevious(), null );
    assertEquals( transaction.getPreviousInSameContext(), null );
    assertEquals( transaction.isMutation(), false );
  }

  @Test
  public void beginTransaction_nested()
    throws Exception
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final Transaction transaction1 = Transaction.begin( context,
                                                        ValueUtil.randomString(),
                                                        false, null );
    assertTrue( context.isTransactionActive() );

    final Transaction transaction2 = Transaction.begin( context, ValueUtil.randomString(),
                                                        false, null );

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
      Transaction.begin( context1, ValueUtil.randomString(), false, null );
    assertTrue( context1.isTransactionActive() );
    assertFalse( context2.isTransactionActive() );

    final Transaction transaction2 =
      Transaction.begin( context2, ValueUtil.randomString(), false, null );

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
      Transaction.begin( context1, ValueUtil.randomString(), false, null );
    assertTrue( context1.isTransactionActive() );
    assertFalse( context2.isTransactionActive() );

    final Transaction transaction2 =
      Transaction.begin( context2, ValueUtil.randomString(), false, null );

    assertFalse( context1.isTransactionActive() );
    assertTrue( context2.isTransactionActive() );

    final Transaction transaction3 =
      Transaction.begin( context1, ValueUtil.randomString(), false, null );

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
    ArezTestUtil.resetState();

    final ArezContext context1 = new ArezContext();
    final ArezContext context2 = new ArezContext();

    assertFalse( context1.isTransactionActive() );
    assertFalse( context2.isTransactionActive() );

    final String name1 = ValueUtil.randomString();
    final String name2 = ValueUtil.randomString();
    Transaction.begin( context1, name1, false, null );
    assertTrue( context1.isTransactionActive() );
    assertTrue( context2.isTransactionActive() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> Transaction.begin( context2, name2, false, null ) );

    assertEquals( exception.getMessage(),
                  "Arez-0120: Zones are not enabled but the transaction named '" + name2 + "' is nested in a " +
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
    Transaction.begin( context, name, false, null );

    handler.assertEventCount( 1 );
    handler.assertNextEvent( TransactionStartedEvent.class, event -> {
      assertEquals( event.getName(), name );
      assertEquals( event.isMutation(), false );
      assertEquals( event.getTracker(), null );
    } );
  }

  @Test
  public void beginTransaction_attemptToNest_READ_WRITE_in_READ_ONLY()
    throws Exception
  {
    final ArezContext context = Arez.context();

    setCurrentTransaction( context.observer( new CountAndObserveProcedure() ) );
    final Transaction transaction = context.getTransaction();

    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.begin( context, name, true, null ) );
    assertEquals( exception.getMessage(),
                  "Arez-0119: Attempting to create READ_WRITE transaction named '" + name +
                  "' but it is nested in transaction named '" + transaction.getName() + "' with " +
                  "mode READ_ONLY which is not equal to READ_WRITE." );
  }

  @Test
  public void beginTransaction_nest_in_suspended_transaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    setCurrentTransaction( context.observer( new CountAndObserveProcedure() ) );
    final Transaction transaction = context.getTransaction();
    Transaction.markAsSuspended();

    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> Transaction.begin( context, name, false, null ) );
    assertEquals( exception.getMessage(),
                  "Arez-0121: Attempted to create transaction named '" + name + "' while " +
                  "nested in a suspended transaction named '" + transaction.getName() + "'." );
  }

  @Test
  public void beginTransaction_attemptToNest_READ_WRITE_in_READ_WRITE_OWNED()
    throws Exception
  {
    final ArezContext context = Arez.context();

    setCurrentTransaction( context.computed( () -> "" ).getObserver() );
    final Transaction transaction = context.getTransaction();

    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.begin( context, name, true, null ) );
    assertEquals( exception.getMessage(),
                  "Arez-0186: Attempting to create transaction named '" + name +
                  "' nested in ComputedValue transaction named '" + transaction.getName() + "'. " +
                  "ComputedValues must not invoke actions or observe methods as they should derive " +
                  "values from other computeds and observables." );
  }

  @Test
  public void begin_attempt_to_nestAction_in_non_computed_tracker_transaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer tracker = context.tracker( () -> {
    } );
    setCurrentTransaction( newReadWriteObserver( context ) );

    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.begin( context, name, true, tracker ) );
    assertEquals( exception.getMessage(),
                  "Arez-0171: Attempting to create a tracking transaction named '" + name + "' for the " +
                  "observer named '" + tracker.getName() + "' but the transaction is not a top-level transaction " +
                  "when this is required. This may be a result of nesting a observe() call inside an action or " +
                  "another observer function." );
  }

  @Test
  public void beginTransaction_attemptToNest_READ_ONLY_in_READ_WRITE_OWNED()
    throws Exception
  {
    final ArezContext context = Arez.context();

    setCurrentTransaction( context.computed( () -> "" ).getObserver() );
    final Transaction transaction = context.getTransaction();

    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> Transaction.begin( context, name, false, null ) );
    assertEquals( exception.getMessage(),
                  "Arez-0186: Attempting to create transaction named '" + name +
                  "' nested in ComputedValue transaction named '" + transaction.getName() + "'. " +
                  "ComputedValues must not invoke actions or observe methods as they should derive " +
                  "values from other computeds and observables." );
  }

  @Test
  public void commitTransaction_matchingRootTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
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
      Transaction.begin( context, ValueUtil.randomString(), false, null );
    final Transaction transaction2 =
      Transaction.begin( context, ValueUtil.randomString(), false, null );

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
      Transaction.begin( context1, ValueUtil.randomString(), false, null );
    final Transaction transaction2 =
      Transaction.begin( context2, ValueUtil.randomString(), false, null );

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
      Transaction.begin( context1, ValueUtil.randomString(), false, null );
    final Transaction transaction2 =
      Transaction.begin( context2, ValueUtil.randomString(), false, null );
    final Transaction transaction3 =
      Transaction.begin( context1, ValueUtil.randomString(), false, null );
    final Transaction transaction4 =
      Transaction.begin( context2, ValueUtil.randomString(), false, null );

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
    final Transaction transaction = new Transaction( context, null, name, false, null );
    transaction.begin();
    Transaction.setTransaction( transaction );

    Transaction.commit( transaction );

    handler.assertEventCount( 1 );
    handler.assertNextEvent( TransactionCompletedEvent.class, event -> {
      assertEquals( event.getName(), name );
      assertEquals( event.isMutation(), false );
      assertEquals( event.getTracker(), null );
      assertTrue( event.getDuration() >= 0 );
    } );
  }

  @Test
  public void commitTransaction_nonMatchingRootTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
    transaction.begin();
    Transaction.setTransaction( transaction );

    final Transaction transaction2 = new Transaction( context, null, ValueUtil.randomString(), false, null );
    transaction2.begin();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.commit( transaction2 ) );
    assertEquals( exception.getMessage(),
                  "Arez-0123: Attempting to commit transaction named '" + transaction2.getName() +
                  "' but this does not match existing transaction named '" + transaction.getName() + "'." );
  }

  @Test
  public void commitTransaction_transactionSuspended()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
    transaction.begin();
    Transaction.setTransaction( transaction );
    Transaction.markAsSuspended();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.commit( transaction ) );
    assertEquals( exception.getMessage(),
                  "Arez-0124: Attempting to commit transaction named '" +
                  transaction.getName() +
                  "' transaction is suspended." );
  }

  @Test
  public void commitTransaction_noTransactionActive()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
    transaction.begin();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.commit( transaction ) );
    assertEquals( exception.getMessage(),
                  "Arez-0122: Attempting to commit transaction named '" + transaction.getName() +
                  "' but no transaction is active." );
  }

  @Test
  public void getPreviousInSameContext_zonesNotEnabled()
  {
    ArezTestUtil.disableZones();

    final Transaction transaction = new Transaction( null, null, ValueUtil.randomString(), false, null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, transaction::getPreviousInSameContext );
    assertEquals( exception.getMessage(),
                  "Arez-0137: Attempted to invoke getPreviousInSameContext() on transaction named '" +
                  transaction.getName() + "' when zones are not enabled." );
  }

  @Test
  public void current()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
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
    assertEquals( exception.getMessage(),
                  "Arez-0117: Attempting to get current transaction but no transaction is active." );
  }

  @Test
  public void current_transactionSuspended()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
    transaction.begin();
    Transaction.setTransaction( transaction );
    Transaction.markAsSuspended();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, Transaction::current );
    assertEquals( exception.getMessage(),
                  "Arez-0118: Attempting to get current transaction but transaction is suspended." );
  }

  @Test
  public void resume()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
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

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
    transaction.begin();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.resume( transaction ) );
    assertEquals( exception.getMessage(),
                  "Arez-0128: Attempting to resume transaction named '" + transaction.getName() +
                  "' but no transaction is active." );
  }

  @Test
  public void resume_transactionNotSuspended()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
    transaction.begin();
    Transaction.setTransaction( transaction );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.resume( transaction ) );
    assertEquals( exception.getMessage(),
                  "Arez-0130: Attempting to resume transaction named '" + transaction.getName() +
                  "' but transaction is not suspended." );
  }

  @Test
  public void resume_transactionNotMatched()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
    transaction.begin();
    Transaction.setTransaction( transaction );
    Transaction.markAsSuspended();

    final Transaction transaction2 = new Transaction( context, transaction, ValueUtil.randomString(), false, null );
    transaction2.begin();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.resume( transaction2 ) );
    assertEquals( exception.getMessage(),
                  "Arez-0129: Attempting to resume transaction named '" + transaction2.getName() +
                  "' but this does not match existing transaction named '" + transaction.getName() + "'." );
  }

  @Test
  public void suspend()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
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

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
    transaction.begin();

    final Transaction transaction2 = new Transaction( context, null, ValueUtil.randomString(), false, null );
    transaction2.begin();
    Transaction.setTransaction( transaction2 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.suspend( transaction ) );
    assertEquals( exception.getMessage(),
                  "Arez-0126: Attempting to suspend transaction named '" + transaction.getName() +
                  "' but this does not match existing transaction named '" + transaction2.getName() + "'." );
  }

  @Test
  public void suspend_transactionAlreadySuspended()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
    transaction.begin();
    Transaction.setTransaction( transaction );
    Transaction.markAsSuspended();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.suspend( transaction ) );
    assertEquals( exception.getMessage(),
                  "Arez-0127: Attempting to suspend transaction named '" + transaction.getName() +
                  "' but transaction is already suspended." );
  }

  @Test
  public void suspend_noActiveTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
    transaction.begin();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Transaction.suspend( transaction ) );
    assertEquals( exception.getMessage(),
                  "Arez-0125: Attempting to suspend transaction named '" + transaction.getName() +
                  "' but no transaction is active." );
  }

  @Test
  public void isTransactionActive()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null );
    transaction.begin();

    assertFalse( Transaction.isTransactionActive( context ) );

    Transaction.setTransaction( transaction );

    assertTrue( Transaction.isTransactionActive( context ) );

    Transaction.markAsSuspended();

    assertFalse( Transaction.isTransactionActive( context ) );
  }

  static class MyDisposable
    implements Disposable
  {
    private boolean _disposed;

    @Override
    public void dispose()
    {
      _disposed = true;
    }

    @Override
    public boolean isDisposed()
    {
      return _disposed;
    }
  }

  @Test
  public void reportDispose()
  {
    final Transaction transaction = new Transaction( Arez.context(), null, ValueUtil.randomString(), true, null );

    final MyDisposable node = new MyDisposable();

    transaction.reportDispose( node );

    node.dispose();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportDispose( node ) );
    assertEquals( exception.getMessage(),
                  "Arez-0176: Invoked reportDispose on transaction named '" +
                  transaction.getName() + "' where the element is disposed." );
  }

  @Test
  public void reportDispose_BAD_TransactionMode()
  {
    final Transaction transaction = new Transaction( Arez.context(), null, ValueUtil.randomString(), false, null );

    final MyDisposable node = new MyDisposable();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> transaction.reportDispose( node ) );
    assertEquals( exception.getMessage(),
                  "Arez-0177: Invoked reportDispose on transaction named '" +
                  transaction.getName() + "' but the transaction mode is not READ_WRITE but is READ_ONLY." );
  }

  @SuppressWarnings( "UnusedReturnValue" )
  @Nonnull
  private Observer ensureDerivationHasObserver( @Nonnull final Observer observer )
  {
    Transaction.setTransaction( null );
    final Observer randomObserver = observer.getContext().observer( new CountAndObserveProcedure() );
    setupReadWriteTransaction();
    randomObserver.setState( Flags.STATE_UP_TO_DATE );
    final ComputedValue<?> computedValue = observer.getComputedValue();
    final ObservableValue<?> observableValue = computedValue.getObservableValue();
    observableValue.rawAddObserver( randomObserver );
    randomObserver.getDependencies().add( observableValue );
    return randomObserver;
  }
}
