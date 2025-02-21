package arez;

import arez.spy.TransactionCompleteEvent;
import arez.spy.TransactionStartEvent;
import java.util.List;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "SimplifiableAssertion" )
public final class TransactionTest
  extends AbstractTest
{
  @Test
  public void defaultObserverTransactionModeUnlessSpecified()
  {
    assertEquals( Transaction.Flags.transactionMode( Transaction.Flags.READ_ONLY ), 0 );
    assertEquals( Transaction.Flags.transactionMode( Transaction.Flags.READ_WRITE ), 0 );
    assertEquals( Transaction.Flags.transactionMode( 0 ), Transaction.Flags.READ_ONLY );

    ArezTestUtil.noEnforceTransactionType();
    assertEquals( Transaction.Flags.transactionMode( 0 ), 0 );
  }

  @Test
  public void isTransactionModeSpecified()
  {
    assertTrue( Transaction.Flags.isTransactionModeSpecified( Transaction.Flags.READ_ONLY ) );
    assertTrue( Transaction.Flags.isTransactionModeSpecified( Transaction.Flags.READ_WRITE ) );
    assertFalse( Transaction.Flags.isTransactionModeSpecified( 0 ) );
  }

  @Test
  public void isTransactionModeValid()
  {
    assertTrue( Transaction.Flags.isTransactionModeValid( Transaction.Flags.READ_ONLY ) );
    assertTrue( Transaction.Flags.isTransactionModeValid( Transaction.Flags.READ_WRITE ) );
    assertFalse( Transaction.Flags.isTransactionModeValid( 0 ) );
    assertFalse( Transaction.Flags.isTransactionModeValid( Transaction.Flags.READ_ONLY |
                                                           Transaction.Flags.READ_WRITE ) );
  }

  @Test
  public void getTransactionModeName()
  {
    assertEquals( Transaction.Flags.getTransactionModeName( Transaction.Flags.READ_ONLY ), "READ_ONLY" );
    assertEquals( Transaction.Flags.getTransactionModeName( Transaction.Flags.READ_WRITE ), "READ_WRITE" );
    assertEquals( Transaction.Flags.getTransactionModeName( 0 ), "UNKNOWN(0)" );
  }

  @Test
  public void construction()
  {
    final ArezContext context = Arez.context();
    final String name1 = ValueUtil.randomString();
    final int nextNodeId = context.currentNextTransactionId();

    final Transaction transaction = new Transaction( context, null, name1, false, null, false );

    assertEquals( transaction.getName(), name1 );
    assertEquals( transaction.toString(), name1 );
    assertEquals( transaction.getContext(), context );
    assertEquals( transaction.getId(), nextNodeId );
    assertNull( transaction.getPrevious() );
    assertNull( transaction.getTracker() );
    assertNull( transaction.getObservableValues() );
    assertNull( transaction.getPendingDeactivations() );
    assertFalse( transaction.isMutation() );
    assertNotEquals( transaction.getStartedAt(), 0 );

    assertEquals( context.currentNextTransactionId(), nextNodeId + 1 );
  }

  @Test
  public void getName_whenNamesDisabled()
  {
    ArezTestUtil.disableNames();

    final Transaction transaction = new Transaction( Arez.context(), null, null, false, null, false );
    assertInvariantFailure( transaction::getName,
                            "Arez-0133: Transaction.getName() invoked when Arez.areNamesEnabled() is false" );
  }

  @Test
  public void constructorPassedContext_whenZonesDisabled()
  {
    ArezTestUtil.disableZones();

    assertInvariantFailure( () -> new Transaction( Arez.context(),
                                                   null,
                                                   ValueUtil.randomString(),
                                                   false,
                                                   null,
                                                   false ),
                            "Arez-184: Transaction passed a context but Arez.areZonesEnabled() is false" );
  }

  @Test
  public void construct_withNameWhenNamesDisabled()
  {
    ArezTestUtil.disableNames();

    assertInvariantFailure( () -> new Transaction( Arez.context(),
                                                   null,
                                                   "X",
                                                   false,
                                                   null, false ),
                            "Arez-0131: Transaction passed a name 'X' but Arez.areNamesEnabled() is false" );
  }

  @Test
  public void construction_whenSpyDisabled()
  {
    ArezTestUtil.disableSpies();

    final Transaction transaction =
      new Transaction( Arez.context(), null, ValueUtil.randomString(), false, null, false );

    // Re-enable spy so can read field
    ArezTestUtil.enableSpies();

    assertEquals( transaction.getStartedAt(), 0 );
  }

  @Test
  public void construction_with_READ_WRITE_and_computableValue()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final ComputableValue<String> computableValue = context.computable( () -> "" );

    assertInvariantFailure( () -> new Transaction( context,
                                                   null,
                                                   name,
                                                   true,
                                                   computableValue.getObserver(), false ),
                            "Arez-0132: Attempted to create transaction named '" + name +
                            "' with mode READ_WRITE when ComputableValue tracker specified." );
  }

  @Test
  public void rootTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction1 = new Transaction( context, null, ValueUtil.randomString(), false, null, false );
    final Transaction transaction2 =
      new Transaction( context, transaction1, ValueUtil.randomString(), false, null, false );

    assertTrue( transaction1.isRootTransaction() );
    assertEquals( transaction1.getRootTransaction(), transaction1 );

    assertFalse( transaction2.isRootTransaction() );
    assertEquals( transaction2.getRootTransaction(), transaction1 );
  }

  @Test
  public void rootTransaction_zonesDisabled()
  {
    ArezTestUtil.disableZones();

    final Transaction transaction1 = new Transaction( null, null, ValueUtil.randomString(), false, null, false );
    final Transaction transaction2 =
      new Transaction( null, transaction1, ValueUtil.randomString(), false, null, false );

    assertTrue( transaction1.isRootTransaction() );
    assertEquals( transaction1.getRootTransaction(), transaction1 );

    assertFalse( transaction2.isRootTransaction() );
    assertEquals( transaction2.getRootTransaction(), transaction1 );
  }

  @Test
  public void begin()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    assertEquals( tracker.getState(), Observer.Flags.STATE_INACTIVE );

    transaction.begin();

    //Just verify that it ultimately invokes beginTracking
    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void commit()
  {
    final ArezContext context = Arez.context();
    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer tracker = computableValue.getObserver();

    ensureDerivationHasObserver( tracker );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue1 = context.observable();
    final Observer derivation = context.computable( () -> "" ).getObserver();
    derivation.setState( Observer.Flags.STATE_UP_TO_DATE );
    final ObservableValue<?> observableValue2 = derivation.getComputableValue().getObservableValue();

    observableValue2.markAsPendingDeactivation();
    transaction.queueForDeactivation( observableValue2 );

    assertNotNull( transaction.getPendingDeactivations() );
    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertTrue( observableValue2.isActive() );
    assertTrue( observableValue2.isPendingDeactivation() );

    transaction.safeGetObservables().add( observableValue1 );
    final FastList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.commit();

    // The next code block essentially verifies it calls completeTracking
    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertTrue( tracker.getDependencies().contains( observableValue1 ) );
    assertEquals( observableValue1.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );

    // This section essentially verifies processPendingDeactivations() is called
    assertFalse( observableValue2.isPendingDeactivation() );
    assertFalse( observableValue2.isActive() );
    assertEquals( observableValue2.getObserver(), derivation );
    assertEquals( derivation.getState(), Observer.Flags.STATE_INACTIVE );
  }

  @Test
  public void trackingCycleWithNoTracker()
  {
    final ArezContext context = Arez.context();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );

    final ObservableValue<?> observableValue = context.observable();

    assertEquals( observableValue.getObservers().size(), 0 );
    assertNull( transaction.getObservableValues() );

    //Transaction should perform no action during tracking if there is no associated tracker
    transaction.beginTracking();
    transaction.observe( observableValue );
    transaction.completeTracking();

    assertEquals( observableValue.getObservers().size(), 0 );
    assertNull( transaction.getObservableValues() );
  }

  @Test
  public void beginTracking_firstTracking()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    assertEquals( tracker.getState(), Observer.Flags.STATE_INACTIVE );

    transaction.beginTracking();

    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void beginTracking()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    ensureDerivationHasObserver( tracker );

    tracker.setState( Observer.Flags.STATE_STALE );
    assertEquals( tracker.getState(), Observer.Flags.STATE_STALE );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_STALE );
    tracker.getDependencies().add( observableValue );
    observableValue.rawAddObserver( tracker );

    transaction.beginTracking();

    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void observe()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    transaction.beginTracking();

    final ObservableValue<?> observableValue = context.observable();

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue.getObservers().size(), 0 );
    assertNull( transaction.getObservableValues() );
    assertNotEquals( transaction.getId(), observableValue.getLastTrackerTransactionId() );
    assertFalse( transaction.hasTransactionUseOccurred() );

    transaction.observe( observableValue );

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( transaction.getId(), observableValue.getLastTrackerTransactionId() );
    assertNotNull( transaction.getObservableValues() );

    assertTrue( transaction.getObservableValues().contains( observableValue ) );
    assertEquals( transaction.getObservableValues().size(), 1 );
    assertTrue( transaction.hasTransactionUseOccurred() );
  }

  @Test
  public void registerOnDeactivationHook()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    transaction.beginTracking();

    assertEquals( tracker.getHooks().size(), 0 );
    assertNull( transaction.getHooks() );
    assertFalse( transaction.hasTransactionUseOccurred() );

    final String key = ValueUtil.randomString();
    transaction.registerHook( key, new NoopProcedure(), new NoopProcedure() );

    assertNotNull( transaction.getHooks() );
    assertEquals( transaction.getHooks().size(), 1 );
    assertTrue( transaction.getHooks().containsKey( key ) );

    assertEquals( tracker.getHooks().size(), 0 );

    transaction.completeTracking();

    assertEquals( tracker.getHooks().size(), 1 );
    assertTrue( transaction.getHooks().containsKey( key ) );
  }

  @Test
  public void registerOnDeactivationHook_outsideTrackingTransaction()
  {
    final ArezContext context = Arez.context();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );
    Transaction.setTransaction( transaction );

    transaction.beginTracking();

    assertNull( transaction.getHooks() );
    assertFalse( transaction.hasTransactionUseOccurred() );

    assertInvariantFailure( () -> transaction.registerHook( ValueUtil.randomString(),
                                                            new NoopProcedure(),
                                                            new NoopProcedure() ),
                            "Arez-0045: registerHook() invoked outside of a tracking transaction." );
  }

  @Test
  public void observe_onDisposedObservable()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    transaction.beginTracking();

    final ObservableValue<?> observableValue = context.observable();

    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue.getObservers().size(), 0 );
    assertNull( transaction.getObservableValues() );
    assertNotEquals( transaction.getId(), observableValue.getLastTrackerTransactionId() );

    observableValue.setWorkState( ObservableValue.DISPOSED );
    assertInvariantFailure( () -> transaction.observe( observableValue ),
                            "Arez-0142: Invoked observe on transaction named '" +
                            transaction.getName() + "' for observableValue named '" + observableValue.getName() +
                            "' where the observableValue is disposed." );
  }

  @Test
  public void observe_noObserveIfOwnedByTracker()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    transaction.beginTracking();

    final ObservableValue<?> observableValue = tracker.getComputableValue().getObservableValue();

    assertInvariantFailure( () -> transaction.observe( observableValue ),
                            "Arez-0143: Invoked observe on transaction named '" +
                            transaction.getName() + "' for observableValue named '" + observableValue.getName() +
                            "' where the observableValue is owned by the tracker." );
  }

  @Test
  public void multipleObserves()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
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
    final Observer tracker = context.computable( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
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
    assertEquals( transaction.getObservableValues().size(), 1 );
  }

  @Test
  public void completeTracking_noObservables()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    assertNull( transaction.getObservableValues() );

    final FastList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );
    //noinspection SimplifiableAssertion
    assertTrue( tracker.getDependencies() == dependencies );
    assertEquals( tracker.getDependencies().size(), 0 );
  }

  @Test
  public void completeTracking_noTrackerButObservablesPresent_shouldFail()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );

    // This next line forces the creation of observables
    transaction.safeGetObservables();

    assertInvariantFailure( transaction::completeTracking,
                            "Arez-0154: Transaction named '" + transaction.getName() + "' has no associated " +
                            "tracker so _observableValues should be null but are not." );
  }

  @Test
  public void completeTracking_withBadTrackerState()
  {
    final ArezContext context = Arez.context();

    final Observer tracker = context.computable( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_INACTIVE );

    assertInvariantFailure( transaction::completeTracking,
                            "Arez-0155: Transaction named '" +
                            transaction.getName() +
                            "' called completeTracking " +
                            "but _tracker state of INACTIVE is not expected when tracker has not been disposed." );
  }

  @Test
  public void completeTracking_withDisposedTracker()
  {
    final ArezContext context = Arez.context();

    final Observer tracker = context.computable( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    ensureDerivationHasObserver( tracker );

    tracker.markDependenciesLeastStaleObserverAsUpToDate();
    tracker.clearDependencies();
    tracker.setState( Observer.Flags.STATE_DISPOSED );

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

    final FastList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Observer.Flags.STATE_DISPOSED );
    final FastList<ObservableValue<?>> dependencies1 = tracker.getDependencies();
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

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_STALE );
    observer2.setState( Observer.Flags.STATE_STALE );

    // Setup existing observableValue dependency
    final ObservableValue<?> observableValue = context.observable();

    observer2.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer2 );
    observableValue.setLeastStaleObserverState( observer2.getState() );

    final FastList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.observe( observableValue );

    transaction.completeTracking();

    assertEquals( tracker.getState(), Observer.Flags.STATE_STALE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue.getObservers().size(), 2 );
    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_STALE );
  }

  @Test
  public void completeTracking_noNewObservablesButExistingObservables()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    // Setup existing observable dependency
    final ObservableValue<?> observableValue1 = context.observable();
    tracker.getDependencies().add( observableValue1 );
    observableValue1.getObservers().add( tracker );

    final FastList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue1.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue1.getObservers().size(), 0 );
  }

  @Test
  public void completeTracking_singleObservable()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();

    ensureDerivationHasObserver( tracker );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = context.observable();

    transaction.safeGetObservables().add( observableValue );

    final FastList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertTrue( tracker.getDependencies().contains( observableValue ) );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_multipleObservable()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();

    ensureDerivationHasObserver( tracker );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue1 = context.observable();
    final ObservableValue<?> observableValue2 = context.observable();
    final ObservableValue<?> observableValue3 = context.observable();
    final ObservableValue<?> observableValue4 = context.observable();

    transaction.safeGetObservables().add( observableValue1 );
    transaction.safeGetObservables().add( observableValue2 );
    transaction.safeGetObservables().add( observableValue3 );
    transaction.safeGetObservables().add( observableValue4 );

    final FastList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 4 );
    assertTrue( tracker.getDependencies().contains( observableValue1 ) );
    assertEquals( observableValue1.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue2.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue3.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue4.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_singleObservableMultipleEntries()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();

    ensureDerivationHasObserver( tracker );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = context.observable();

    transaction.safeGetObservables().add( observableValue );
    transaction.safeGetObservables().add( observableValue );
    transaction.safeGetObservables().add( observableValue );
    transaction.safeGetObservables().add( observableValue );

    final FastList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertTrue( tracker.getDependencies().contains( observableValue ) );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_multipleObservableMultipleEntries()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();

    ensureDerivationHasObserver( tracker );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue1 = context.observable();
    final ObservableValue<?> observableValue2 = context.observable();

    /*
     * Forcing the order 1, 1, 2, 1 means that the sequence needs to be collapsed and 2 will be moved so 1, 2
     */
    transaction.safeGetObservables().add( observableValue1 );
    transaction.safeGetObservables().add( observableValue1 );
    transaction.safeGetObservables().add( observableValue2 );
    transaction.safeGetObservables().add( observableValue1 );

    final FastList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 2 );
    assertTrue( tracker.getDependencies().contains( observableValue1 ) );
    assertTrue( tracker.getDependencies().contains( observableValue2 ) );
    assertEquals( observableValue1.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue2.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_matchingExistingDependencies()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    final ObservableValue<?> observableValue1 = context.observable();
    final ObservableValue<?> observableValue2 = context.observable();

    tracker.getDependencies().add( observableValue1 );
    tracker.getDependencies().add( observableValue2 );

    observableValue1.getObservers().add( tracker );
    observableValue2.getObservers().add( tracker );

    transaction.safeGetObservables().add( observableValue1 );
    transaction.safeGetObservables().add( observableValue2 );

    final FastList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );
    //noinspection SimplifiableAssertion
    assertTrue( tracker.getDependencies() == dependencies );
    assertEquals( tracker.getDependencies().size(), 2 );
    assertTrue( tracker.getDependencies().contains( observableValue1 ) );
    assertTrue( tracker.getDependencies().contains( observableValue2 ) );
    assertEquals( observableValue1.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue2.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_dependenciesChanged()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();

    ensureDerivationHasObserver( tracker );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

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

    final FastList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 2 );
    assertTrue( tracker.getDependencies().contains( observableValue1 ) );
    assertTrue( tracker.getDependencies().contains( observableValue3 ) );
    assertEquals( observableValue1.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue2.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue3.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_calculatedObservableUpToDate()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    final Observer derivation = context.computable( () -> "" ).getObserver();
    derivation.setState( Observer.Flags.STATE_UP_TO_DATE );
    final ObservableValue<?> observableValue = derivation.getComputableValue().getObservableValue();

    tracker.getDependencies().add( observableValue );
    observableValue.getObservers().add( tracker );
    transaction.safeGetObservables().add( observableValue );

    final FastList<ObservableValue<?>> dependencies = tracker.getDependencies();

    transaction.completeTracking();

    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );
    //noinspection SimplifiableAssertion
    assertTrue( tracker.getDependencies() == dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertTrue( tracker.getDependencies().contains( observableValue ) );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
  }

  @Test
  public void completeTracking_calculatedObservableStale()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    ensureDerivationHasObserver( tracker );

    final Observer derivation = context.computable( () -> "" ).getObserver();
    derivation.setState( Observer.Flags.STATE_STALE );
    final ObservableValue<?> observableValue = derivation.getComputableValue().getObservableValue();

    tracker.getDependencies().add( observableValue );
    observableValue.getObservers().add( tracker );
    transaction.safeGetObservables().add( observableValue );

    final FastList<ObservableValue<?>> dependencies = tracker.getDependencies();

    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );

    transaction.completeTracking();

    assertEquals( tracker.getState(), Observer.Flags.STATE_STALE );
    //noinspection SimplifiableAssertion
    assertTrue( tracker.getDependencies() == dependencies );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertTrue( tracker.getDependencies().contains( observableValue ) );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );

    // Make sure the derivation observer has state updated
    assertEquals( tracker.getState(), Observer.Flags.STATE_STALE );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void completeTracking_calculatedObservableStale_mustUpdateNewDependencies()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = Arez.context().computable( () -> "" ).getObserver();

    setCurrentTransaction( tracker );

    ensureDerivationHasObserver( tracker );
    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer derivation = context.computable( () -> "" ).getObserver();
    derivation.setState( Observer.Flags.STATE_POSSIBLY_STALE );

    final ObservableValue<?> observableValue = derivation.getComputableValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_POSSIBLY_STALE );

    Transaction.setTransaction( null );
    final Observer observer = context.observer( new CountAndObserveProcedure() );
    setupReadWriteTransaction();
    observer.setState( Observer.Flags.STATE_STALE );

    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    setCurrentTransaction( tracker );

    Transaction.current().safeGetObservables().add( observableValue );

    Transaction.current().completeTracking();

    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( tracker.getDependencies().size(), 1 );
    assertTrue( tracker.getDependencies().contains( observableValue ) );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );

    // Make sure the derivation observer has state updated
    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void completeTracking_calculatedObservable_noObservers_queuedForDeactivation()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer derivation = context.computable( () -> "" ).getObserver();
    derivation.setState( Observer.Flags.STATE_STALE );
    final ObservableValue<?> observableValue = derivation.getComputableValue().getObservableValue();

    tracker.getDependencies().add( observableValue );
    observableValue.getObservers().add( tracker );

    final FastList<ObservableValue<?>> dependencies = tracker.getDependencies();

    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );

    transaction.completeTracking();

    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );

    final List<ObservableValue<?>> pendingDeactivations = transaction.getPendingDeactivations();
    assertNotNull( pendingDeactivations );
    assertTrue( pendingDeactivations.contains( tracker.getComputableValue().getObservableValue() ) );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void completeTracking_calculatedObservable_noObservers_keepAlive()
  {
    final ArezContext context = Arez.context();
    final Observer tracker =
      context.computable( () -> "", ComputableValue.Flags.KEEPALIVE | ComputableValue.Flags.RUN_LATER ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer derivation = context.computable( () -> "" ).getObserver();
    derivation.setState( Observer.Flags.STATE_STALE );
    final ObservableValue<?> observableValue = derivation.getComputableValue().getObservableValue();

    tracker.getDependencies().add( observableValue );
    observableValue.getObservers().add( tracker );

    final FastList<ObservableValue<?>> dependencies = tracker.getDependencies();

    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );

    transaction.completeTracking();

    assertEquals( tracker.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertTrue( tracker.getDependencies() != dependencies );
    assertEquals( tracker.getDependencies().size(), 0 );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );

    final List<ObservableValue<?>> pendingDeactivations = transaction.getPendingDeactivations();
    assertNotNull( pendingDeactivations );
    assertFalse( pendingDeactivations.contains( tracker.getComputableValue().getObservableValue() ) );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void queueForDeactivation_singleObserver()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<String> observableValue = context.computable( () -> "" ).getObservableValue();

    assertNull( transaction.getPendingDeactivations() );

    transaction.queueForDeactivation( observableValue );

    assertNotNull( transaction.getPendingDeactivations() );

    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertTrue( transaction.getPendingDeactivations().contains( observableValue ) );
  }

  @Test
  public void queueForDeactivation_nestedTransaction()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();

    final Transaction transaction1 = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    final Transaction transaction2 =
      new Transaction( context, transaction1, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction2 );
    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<String> observableValue = context.computable( () -> "" ).getObservableValue();

    assertNull( transaction1.getPendingDeactivations() );
    assertNull( transaction2.getPendingDeactivations() );

    transaction2.queueForDeactivation( observableValue );

    assertNotNull( transaction1.getPendingDeactivations() );
    assertNotNull( transaction2.getPendingDeactivations() );

    // Pending deactivations list in both transactions should be the same instance
    //noinspection SimplifiableAssertion
    assertTrue( transaction1.getPendingDeactivations() == transaction2.getPendingDeactivations() );

    assertEquals( transaction1.getPendingDeactivations().size(), 1 );
    assertTrue( transaction1.getPendingDeactivations().contains( observableValue ) );
    assertEquals( transaction2.getPendingDeactivations().size(), 1 );
    assertTrue( transaction2.getPendingDeactivations().contains( observableValue ) );
  }

  @Test
  public void queueForDeactivation_observerAlreadyDeactivated()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<String> observableValue = context.computable( () -> "" ).getObservableValue();

    transaction.queueForDeactivation( observableValue );

    assertInvariantFailure( () -> transaction.queueForDeactivation( observableValue ),
                            "Arez-0141: Invoked queueForDeactivation on transaction named '" +
                            transaction.getName() + "' for observableValue named '" + observableValue.getName() +
                            "' when pending deactivation already exists for observableValue." );
  }

  @Test
  public void queueForDeactivation_observerCanNotDeactivate()
  {
    final ArezContext context = Arez.context();
    final Observer tracker = context.computable( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = context.observable();

    assertInvariantFailure( () -> transaction.queueForDeactivation( observableValue ),
                            "Arez-0140: Invoked queueForDeactivation on transaction named '" +
                            transaction.getName() + "' for observableValue named '" + observableValue.getName() +
                            "' when observableValue can not be deactivated." );
  }

  @Test
  public void processPendingDeactivations_observableHasNoListeners()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );
    Transaction.setTransaction( transaction );

    final Observer derivation = context.computable( () -> "" ).getObserver();
    derivation.setState( Observer.Flags.STATE_UP_TO_DATE );
    final ObservableValue<?> observableValue = derivation.getComputableValue().getObservableValue();

    observableValue.markAsPendingDeactivation();
    transaction.queueForDeactivation( observableValue );

    assertNotNull( transaction.getPendingDeactivations() );
    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertTrue( transaction.getPendingDeactivations().contains( observableValue ) );
    assertTrue( observableValue.isActive() );
    assertTrue( observableValue.isPendingDeactivation() );

    transaction.processPendingDeactivations();

    assertEquals( transaction.getPendingDeactivations().size(), 0 );

    assertFalse( observableValue.isPendingDeactivation() );
    assertFalse( observableValue.isActive() );
    assertEquals( observableValue.getObserver(), derivation );
    assertEquals( derivation.getState(), Observer.Flags.STATE_INACTIVE );
  }

  @Test
  public void processPendingDeactivations_noDeactivationsPending()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );

    assertNull( transaction.getPendingDeactivations() );

    transaction.processPendingDeactivations();

    assertNull( transaction.getPendingDeactivations() );
  }

  @Test
  public void processPendingDeactivations_observableHasListener()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );
    Transaction.setTransaction( transaction );

    final Observer otherObserver = context.computable( () -> "" ).getObserver();
    otherObserver.setState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer derivation = context.computable( () -> "" ).getObserver();
    derivation.setState( Observer.Flags.STATE_UP_TO_DATE );
    final ObservableValue<?> observableValue = derivation.getComputableValue().getObservableValue();

    observableValue.markAsPendingDeactivation();
    transaction.queueForDeactivation( observableValue );
    otherObserver.getDependencies().add( observableValue );
    observableValue.rawAddObserver( otherObserver );

    assertNotNull( transaction.getPendingDeactivations() );
    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertTrue( transaction.getPendingDeactivations().contains( observableValue ) );
    assertTrue( observableValue.isActive() );
    assertTrue( observableValue.isPendingDeactivation() );

    transaction.processPendingDeactivations();

    assertEquals( transaction.getPendingDeactivations().size(), 0 );

    assertFalse( observableValue.isPendingDeactivation() );
    assertTrue( observableValue.isActive() );
    assertEquals( derivation.getState(), Observer.Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void processPendingDeactivations_observableHasListenerAndIsDisposed()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );
    Transaction.setTransaction( transaction );

    final Observer otherObserver = context.computable( () -> "" ).getObserver();
    otherObserver.setState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer derivation = context.computable( () -> "" ).getObserver();
    derivation.setState( Observer.Flags.STATE_UP_TO_DATE );
    final ObservableValue<?> observableValue = derivation.getComputableValue().getObservableValue();

    observableValue.markAsPendingDeactivation();
    transaction.queueForDeactivation( observableValue );
    otherObserver.getDependencies().add( observableValue );
    observableValue.rawAddObserver( otherObserver );

    observableValue.setWorkState( ObservableValue.DISPOSED );

    assertInvariantFailure( transaction::processPendingDeactivations,
                            "Arez-0139: Attempting to deactivate disposed observableValue named '" +
                            observableValue.getName() + "' in transaction named '" + transaction.getName() +
                            "' but the observableValue still has observers." );

  }

  @Test
  public void processPendingDeactivations_causesMorePendingDeactivations()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );

    //Setup transaction as queueForDeactivation retrieves trnsaction from context
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue1 = context.observable();

    final Observer derivation1 = context.computable( () -> "" ).getObserver();
    derivation1.setState( Observer.Flags.STATE_UP_TO_DATE );

    derivation1.getDependencies().add( observableValue1 );
    observableValue1.getObservers().add( derivation1 );

    final ObservableValue<?> observableValue2 = derivation1.getComputableValue().getObservableValue();

    final Observer derivation2 = context.computable( () -> "" ).getObserver();
    derivation2.setState( Observer.Flags.STATE_UP_TO_DATE );

    derivation2.getDependencies().add( observableValue2 );
    observableValue2.getObservers().add( derivation2 );

    final ObservableValue<?> observableValue3 = derivation2.getComputableValue().getObservableValue();

    observableValue3.markAsPendingDeactivation();
    transaction.queueForDeactivation( observableValue3 );

    assertNotNull( transaction.getPendingDeactivations() );
    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertTrue( transaction.getPendingDeactivations().contains( observableValue3 ) );
    assertTrue( observableValue3.isActive() );
    assertTrue( observableValue3.isPendingDeactivation() );

    transaction.processPendingDeactivations();

    assertEquals( transaction.getPendingDeactivations().size(), 0 );

    assertFalse( observableValue3.isPendingDeactivation() );
    assertFalse( observableValue3.isActive() );
    assertEquals( observableValue3.getObserver(), derivation2 );
    assertFalse( observableValue2.isPendingDeactivation() );
    assertFalse( observableValue2.isActive() );
    assertEquals( observableValue2.getObserver(), derivation1 );
    assertFalse( observableValue1.isPendingDeactivation() );
    assertTrue( observableValue1.isActive() );
    assertFalse( observableValue1.isComputableValue() );
    assertEquals( derivation2.getState(), Observer.Flags.STATE_INACTIVE );
    assertEquals( derivation2.getDependencies().size(), 0 );
    assertEquals( observableValue2.getObservers().size(), 0 );
    assertEquals( derivation1.getState(), Observer.Flags.STATE_INACTIVE );
    assertEquals( derivation1.getDependencies().size(), 0 );
    assertEquals( observableValue1.getObservers().size(), 0 );
  }

  @Test
  public void processPendingDeactivations_producesNoMorePendingDeactivations()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> baseObservableValue = context.observable();
    final Observer derivation = context.computable( () -> "" ).getObserver();
    derivation.setState( Observer.Flags.STATE_UP_TO_DATE );

    derivation.getDependencies().add( baseObservableValue );
    baseObservableValue.getObservers().add( derivation );

    final ObservableValue<?> derivedObservableValue = derivation.getComputableValue().getObservableValue();

    derivedObservableValue.markAsPendingDeactivation();
    transaction.queueForDeactivation( derivedObservableValue );

    assertNotNull( transaction.getPendingDeactivations() );
    assertEquals( transaction.getPendingDeactivations().size(), 1 );
    assertTrue( transaction.getPendingDeactivations().contains( derivedObservableValue ) );
    assertTrue( derivedObservableValue.isActive() );
    assertTrue( derivedObservableValue.isPendingDeactivation() );

    transaction.processPendingDeactivations();

    assertEquals( transaction.getPendingDeactivations().size(), 0 );

    assertFalse( derivedObservableValue.isPendingDeactivation() );
    assertFalse( derivedObservableValue.isActive() );
    assertEquals( derivedObservableValue.getObserver(), derivation );
    assertEquals( derivation.getState(), Observer.Flags.STATE_INACTIVE );
    assertEquals( derivation.getDependencies().size(), 0 );
    assertEquals( baseObservableValue.getObservers().size(), 0 );
  }

  @Test
  public void processPendingDeactivations_calledOnNonRootTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction1 = new Transaction( context, null, ValueUtil.randomString(), false, null, false );
    final Transaction transaction2 =
      new Transaction( context, transaction1, ValueUtil.randomString(), false, null, false );

    assertInvariantFailure( transaction2::processPendingDeactivations,
                            "Arez-0138: Invoked processPendingDeactivations on transaction named '" +
                            transaction2.getName() + "' which is not the root transaction." );
  }

  @Test
  public void verifyWriteAllowed_withReadOnlyTransaction_enforceTransactionType_set_to_false()
  {
    ArezTestUtil.noEnforceTransactionType();

    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );

    final ObservableValue<?> observableValue = context.observable();

    //Should produce no error
    transaction.verifyWriteAllowed( observableValue );
  }

  @Test
  public void verifyWriteAllowed_withReadOnlyTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );

    final ObservableValue<?> observableValue = context.observable();

    assertInvariantFailure( () -> transaction.verifyWriteAllowed( observableValue ),
                            "Arez-0152: Transaction named '" +
                            transaction.getName() +
                            "' attempted to change " +
                            "ObservableValue named '" +
                            observableValue.getName() +
                            "' but the transaction mode is READ_ONLY." );
  }

  @Test
  public void verifyWriteAllowed_withReadWriteTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );

    final ObservableValue<?> observableValue = context.observable();

    //Should produce no error
    transaction.verifyWriteAllowed( observableValue );
  }

  @Test
  public void verifyWriteAllowed_withReadWriteOwnedTransaction()
  {
    final ArezContext context = Arez.context();

    final Observer tracker = context.computable( () -> "" ).getObserver();
    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );

    final ObservableValue<?> observableValue1 = tracker.getComputableValue().getObservableValue();
    final ObservableValue<?> observableValue2 = context.observable();

    tracker.getDependencies().add( observableValue2 );
    observableValue2.getObservers().add( tracker );

    //Should produce no error as it is owned by derivation
    transaction.verifyWriteAllowed( observableValue1 );

    assertInvariantFailure( () -> transaction.verifyWriteAllowed( observableValue2 ),
                            "Arez-0153: Transaction named '" +
                            transaction.getName() +
                            "' attempted to change " +
                            "ObservableValue named '" +
                            observableValue2.getName() +
                            "' and the transaction mode is " +
                            "READ_WRITE_OWNED but the ObservableValue has not been created by the transaction." );
  }

  @Test
  public void reportChanged_noObservers()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertFalse( transaction.hasTransactionUseOccurred() );

    transaction.reportChanged( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertTrue( transaction.hasTransactionUseOccurred() );
  }

  @Test
  public void reportChanged_singleUpToDateObserver()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computable( () -> "" ).getObserver();
    observer.setState( Observer.Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertFalse( transaction.hasTransactionUseOccurred() );

    Transaction.setTransaction( transaction );
    transaction.reportChanged( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_STALE );
    assertEquals( observer.getState(), Observer.Flags.STATE_STALE );
    assertTrue( transaction.hasTransactionUseOccurred() );
  }

  @Test
  public void reportChanged_withDisposedObservable()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computable( () -> "" ).getObserver();
    observer.setState( Observer.Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( transaction );

    observableValue.setWorkState( ObservableValue.DISPOSED );

    assertInvariantFailure( () -> transaction.reportChanged( observableValue ),
                            "Arez-0144: Invoked reportChanged on transaction named '" +
                            transaction.getName() +
                            "' for ObservableValue named '" +
                            observableValue.getName() +
                            "' where the ObservableValue " +
                            "is disposed." );
  }

  @Test
  public void reportChanged_readOnlyTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computable( () -> "" ).getObserver();
    observer.setState( Observer.Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( transaction );

    assertInvariantFailure( () -> transaction.reportChanged( observableValue ),
                            "Arez-0152: Transaction named '" +
                            transaction.getName() +
                            "' attempted to change " +
                            "ObservableValue named '" +
                            observableValue.getName() +
                            "' but the transaction mode is READ_ONLY." );
  }

  @Test
  public void reportChanged_singleObserver_notTrackingState()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computable( () -> "" ).getObserver();
    observer.setState( Observer.Flags.STATE_INACTIVE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Observer.Flags.STATE_INACTIVE );

    Transaction.setTransaction( transaction );

    assertInvariantFailure( () -> transaction.reportChanged( observableValue ),
                            "Arez-0145: Transaction named '" +
                            transaction.getName() +
                            "' has attempted to " +
                            "explicitly change observableValue named '" +
                            observableValue.getName() +
                            "' and observableValue " +
                            "is in unexpected state INACTIVE." );
  }

  @Test
  public void reportChanged_singleObserver_possiblyStaleState()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_POSSIBLY_STALE );

    final Observer observer = context.computable( () -> "" ).getObserver();
    observer.setState( Observer.Flags.STATE_POSSIBLY_STALE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer.getState(), Observer.Flags.STATE_POSSIBLY_STALE );

    Transaction.setTransaction( transaction );

    transaction.reportChanged( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_STALE );
    assertEquals( observer.getState(), Observer.Flags.STATE_STALE );
  }

  @Test
  public void reportChanged_multipleObservers()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer observer1 = context.computable( () -> "" ).getObserver();
    observer1.setState( Observer.Flags.STATE_UP_TO_DATE );
    observer1.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer1 );

    final Observer observer2 = context.computable( () -> "" ).getObserver();
    observer2.setState( Observer.Flags.STATE_STALE );
    observer2.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer2 );

    final Observer observer3 = context.computable( () -> "" ).getObserver();
    observer3.setState( Observer.Flags.STATE_STALE );
    observer3.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer3 );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer1.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer2.getState(), Observer.Flags.STATE_STALE );
    assertEquals( observer3.getState(), Observer.Flags.STATE_STALE );

    Transaction.setTransaction( transaction );
    transaction.reportChanged( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_STALE );
    assertEquals( observer1.getState(), Observer.Flags.STATE_STALE );
    assertEquals( observer2.getState(), Observer.Flags.STATE_STALE );
    assertEquals( observer3.getState(), Observer.Flags.STATE_STALE );
  }

  @Test
  public void reportPossiblyChanged_singleUpToDateObserver()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computable( () -> "" ).getObserver();
    calculator.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputableValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computable( () -> "" ).getObserver();
    observer.setState( Observer.Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( transaction );
    transaction.reportPossiblyChanged( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer.getState(), Observer.Flags.STATE_POSSIBLY_STALE );
  }

  @Test
  public void reportPossiblyChanged_onDisposedObservable()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computable( () -> "" ).getObserver();
    calculator.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputableValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computable( () -> "" ).getObserver();
    observer.setState( Observer.Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( transaction );

    observableValue.setWorkState( ObservableValue.DISPOSED );

    assertInvariantFailure( () -> transaction.reportPossiblyChanged( observableValue ),
                            "Arez-0146: Invoked reportPossiblyChanged on transaction named '" + transaction.getName() +
                            "' for ObservableValue named '" + observableValue.getName() + "' where the " +
                            "ObservableValue is disposed." );
  }

  @Test
  public void reportPossiblyChanged_noObserver()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computable( () -> "" ).getObserver();
    calculator.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputableValue().getObservableValue();

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );

    transaction.reportPossiblyChanged( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void reportPossiblyChanged_ownedObserver_transaction_READ_WRITE_OWNED()
  {
    final ArezContext context = Arez.context();

    final Observer calculator = context.computable( () -> "" ).getObserver();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), false, calculator, false );
    Transaction.setTransaction( transaction );

    calculator.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputableValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_UP_TO_DATE );

    calculator.getDependencies().add( observableValue );
    observableValue.getObservers().add( calculator );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( calculator.getState(), Observer.Flags.STATE_UP_TO_DATE );

    assertInvariantFailure( () -> transaction.reportPossiblyChanged( observableValue ),
                            "Arez-0014: Observer named '" +
                            calculator.getName() +
                            "' attempted to schedule itself " +
                            "during read-only tracking transaction. Observers that are supporting ComputableValue instances must" +
                            " not schedule self." );
  }

  @Test
  public void reportPossiblyChanged_nonOwnedObserver_transaction_READ_WRITE_OWNED()
  {
    final ArezContext context = Arez.context();

    final Observer tracker = context.computable( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer calculator = context.computable( () -> "" ).getObserver();
    calculator.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputableValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_UP_TO_DATE );

    calculator.getDependencies().add( observableValue );
    observableValue.getObservers().add( calculator );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( calculator.getState(), Observer.Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( transaction );

    // A computable that causes another computable to recalculate should be allowed
    transaction.reportPossiblyChanged( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_POSSIBLY_STALE );
  }

  @Test
  public void reportPossiblyChanged_readOnlyTransaction()
  {
    final ArezContext context = Arez.context();

    setupReadOnlyTransaction();

    final Observer calculator = context.computable( () -> "" ).getObserver();
    calculator.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputableValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computable( () -> "" ).getObserver();
    observer.setState( Observer.Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );

    // A read-only transaction can not cause a computable to recalculate

    assertInvariantFailure( () -> Transaction.current().reportPossiblyChanged( observableValue ),
                            "Arez-0148: Transaction named '" + Transaction.current().getName() + "' attempted to " +
                            "call reportPossiblyChanged in read-only transaction." );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void reportPossiblyChanged_where_observable_is_not_derived()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computable( () -> "" ).getObserver();
    observer.setState( Observer.Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( transaction );

    assertInvariantFailure( () -> transaction.reportPossiblyChanged( observableValue ),
                            "Arez-0147: Transaction named '" + transaction.getName() + "' has attempted to mark " +
                            "ObservableValue named '" + observableValue.getName() + "' as potentially changed " +
                            "but ObservableValue is not a derived value." );
  }

  @Test
  public void reportPossiblyChanged_multipleObservers()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computable( () -> "" ).getObserver();
    calculator.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputableValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer observer1 = context.computable( () -> "" ).getObserver();
    observer1.setState( Observer.Flags.STATE_UP_TO_DATE );
    observer1.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer1 );

    final Observer observer2 = context.computable( () -> "" ).getObserver();
    observer2.setState( Observer.Flags.STATE_POSSIBLY_STALE );
    observer2.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer2 );

    final Observer observer3 = context.computable( () -> "" ).getObserver();
    observer3.setState( Observer.Flags.STATE_STALE );
    observer3.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer3 );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer1.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer2.getState(), Observer.Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer3.getState(), Observer.Flags.STATE_STALE );

    Transaction.setTransaction( transaction );
    transaction.reportPossiblyChanged( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer1.getState(), Observer.Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer2.getState(), Observer.Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer3.getState(), Observer.Flags.STATE_STALE );
  }

  @Test
  public void reportChangeConfirmed_singleUpToDateObserver()
  {
    final ArezContext context = Arez.context();

    final Observer calculator = context.computable( () -> "" ).getObserver();
    setCurrentTransaction( calculator );

    calculator.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputableValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_UP_TO_DATE );

    calculator.getDependencies().add( observableValue );
    observableValue.getObservers().add( calculator );

    context.getTransaction().reportChangeConfirmed( observableValue );

    // Assume observer is being updated so keep that state
    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( calculator.getState(), Observer.Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void reportChangeConfirmed_disposedObservable()
  {
    final ArezContext context = Arez.context();

    final Observer calculator = context.computable( () -> "" ).getObserver();
    setCurrentTransaction( calculator );

    calculator.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputableValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_UP_TO_DATE );

    calculator.getDependencies().add( observableValue );
    observableValue.getObservers().add( calculator );

    observableValue.setWorkState( ObservableValue.DISPOSED );

    assertInvariantFailure( () -> context.getTransaction().reportChangeConfirmed( observableValue ),
                            "Arez-0149: Invoked reportChangeConfirmed on transaction named '" +
                            context.getTransaction().getName() +
                            "' for ObservableValue named '" +
                            observableValue.getName() +
                            "' where the ObservableValue is disposed." );
  }

  @Test
  public void reportChangeConfirmed_noObservers()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computable( () -> "" ).getObserver();
    calculator.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputableValue().getObservableValue();

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );

    transaction.reportChangeConfirmed( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void reportChangeConfirmed_singlePossiblyStaleObserver()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computable( () -> "" ).getObserver();
    calculator.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputableValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_POSSIBLY_STALE );

    final Observer observer = context.computable( () -> "" ).getObserver();
    observer.setState( observableValue.getLeastStaleObserverState() );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    Transaction.setTransaction( transaction );
    transaction.reportChangeConfirmed( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_STALE );
    assertEquals( observer.getState(), Observer.Flags.STATE_STALE );
  }

  @Test
  public void reportChangeConfirmed_singleStaleObserver()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computable( () -> "" ).getObserver();
    calculator.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputableValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_STALE );

    final Observer observer = context.computable( () -> "" ).getObserver();
    observer.setState( observableValue.getLeastStaleObserverState() );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    Transaction.setTransaction( transaction );
    transaction.reportChangeConfirmed( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_STALE );
    assertEquals( observer.getState(), Observer.Flags.STATE_STALE );
  }

  @Test
  public void reportChangeConfirmed_ownedObserver_transaction_READ_WRITE_OWNED()
  {
    final ArezContext context = Arez.context();

    final Observer calculator = context.computable( () -> "" ).getObserver();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), false, calculator, false );
    Transaction.setTransaction( transaction );

    calculator.setState( Observer.Flags.STATE_POSSIBLY_STALE );

    final ObservableValue<?> observableValue = calculator.getComputableValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_POSSIBLY_STALE );

    calculator.getDependencies().add( observableValue );
    observableValue.getObservers().add( calculator );

    Transaction.setTransaction( transaction );
    transaction.reportChangeConfirmed( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_STALE );
    assertEquals( calculator.getState(), Observer.Flags.STATE_STALE );
  }

  @Test
  public void reportChangeConfirmed_nonOwnedObserver_transaction_READ_WRITE_OWNED()
  {
    final ArezContext context = Arez.context();

    final Observer tracker = context.computable( () -> "" ).getObserver();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, tracker, false );
    Transaction.setTransaction( transaction );

    tracker.setState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer calculator = context.computable( () -> "" ).getObserver();
    calculator.setState( Observer.Flags.STATE_POSSIBLY_STALE );

    final ObservableValue<?> observableValue = calculator.getComputableValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_POSSIBLY_STALE );

    calculator.getDependencies().add( observableValue );
    observableValue.getObservers().add( calculator );

    Transaction.setTransaction( transaction );
    assertInvariantFailure( () -> transaction.reportChangeConfirmed( observableValue ),
                            "Arez-0153: Transaction named '" +
                            transaction.getName() +
                            "' attempted to change " +
                            "ObservableValue named '" +
                            observableValue.getName() +
                            "' and the transaction mode is " +
                            "READ_WRITE_OWNED but the ObservableValue has not been created by the transaction." );
  }

  @Test
  public void reportChangeConfirmed_readOnlyTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computable( () -> "" ).getObserver();
    calculator.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputableValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computable( () -> "" ).getObserver();
    observer.setState( Observer.Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( transaction );

    assertInvariantFailure( () -> transaction.reportChangeConfirmed( observableValue ),
                            "Arez-0152: Transaction named '" +
                            transaction.getName() +
                            "' attempted to change " +
                            "ObservableValue named '" +
                            observableValue.getName() +
                            "' but the transaction mode is READ_ONLY." );
  }

  @Test
  public void reportChangeConfirmed_where_observable_is_not_derived()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );
    Transaction.setTransaction( transaction );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer observer = context.computable( () -> "" ).getObserver();
    observer.setState( Observer.Flags.STATE_UP_TO_DATE );
    observer.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );

    assertInvariantFailure( () -> transaction.reportChangeConfirmed( observableValue ),
                            "Arez-0150: Transaction named '" + transaction.getName() + "' has attempted to mark " +
                            "ObservableValue named '" + observableValue.getName() + "' as potentially changed but " +
                            "ObservableValue is not a derived value." );
  }

  @Test
  public void reportChangeConfirmed_multipleObservers()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );
    Transaction.setTransaction( transaction );

    final Observer calculator = context.computable( () -> "" ).getObserver();
    calculator.setState( Observer.Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = calculator.getComputableValue().getObservableValue();
    observableValue.setLeastStaleObserverState( Observer.Flags.STATE_UP_TO_DATE );

    final Observer observer1 = context.computable( () -> "" ).getObserver();
    observer1.setState( Observer.Flags.STATE_POSSIBLY_STALE );
    observer1.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer1 );

    final Observer observer2 = context.computable( () -> "" ).getObserver();
    observer2.setState( Observer.Flags.STATE_POSSIBLY_STALE );
    observer2.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer2 );

    final Observer observer3 = context.computable( () -> "" ).getObserver();
    observer3.setState( Observer.Flags.STATE_STALE );
    observer3.getDependencies().add( observableValue );
    observableValue.getObservers().add( observer3 );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer1.getState(), Observer.Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer2.getState(), Observer.Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer3.getState(), Observer.Flags.STATE_STALE );

    Transaction.setTransaction( transaction );
    transaction.reportChangeConfirmed( observableValue );

    assertEquals( observableValue.getLeastStaleObserverState(), Observer.Flags.STATE_STALE );
    assertEquals( observer1.getState(), Observer.Flags.STATE_STALE );
    assertEquals( observer2.getState(), Observer.Flags.STATE_STALE );
    assertEquals( observer3.getState(), Observer.Flags.STATE_STALE );
  }

  @Test
  public void invariantObserverIsTracker_notTracker()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observable = context.observable();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, null, false );
    Transaction.setTransaction( transaction );

    observable.rawAddObserver( observer );
    observer.getDependencies().add( observable );

    assertInvariantFailure( () -> transaction.invariantObserverIsTracker( observable, observer ),
                            "Arez-0151: Transaction named '" + transaction.getName() + "' attempted " +
                            "to call reportChangeConfirmed for ObservableValue named '" + observable.getName() +
                            "' and found a dependency named '" + observer.getName() + "' that is UP_TO_DATE but is " +
                            "not the tracker of any transactions in the hierarchy: [" + transaction.getName() + "]." );
  }

  @Test
  public void invariantObserverIsTracker_trackerOfSurroundingTransaction()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue = context.observable();
    final Observer observer = context.observer( new CountAndObserveProcedure() );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, observer, false );
    Transaction.setTransaction( transaction );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    transaction.invariantObserverIsTracker( observableValue, observer );
  }

  @Test
  public void invariantObserverIsTracker_trackerUpTransactionHierarchy()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue = context.observable();
    final Observer observer = context.observer( new CountAndObserveProcedure() );

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), true, observer, false );
    final Transaction transaction2 =
      new Transaction( context, transaction, ValueUtil.randomString(), true, null, false );
    final Transaction transaction3 = new Transaction( context,
                                                      transaction2,
                                                      ValueUtil.randomString(),
                                                      true,
                                                      context.observer( new CountAndObserveProcedure() ),
                                                      false );
    final Transaction transaction4 = new Transaction( context,
                                                      transaction3,
                                                      ValueUtil.randomString(),
                                                      true,
                                                      context.observer( new CountAndObserveProcedure() ),
                                                      false );
    Transaction.setTransaction( transaction4 );

    observableValue.rawAddObserver( observer );
    observer.getDependencies().add( observableValue );

    transaction.invariantObserverIsTracker( observableValue, observer );
  }

  @Test
  public void getStartedAt_whenSpyDisabled()
  {
    ArezTestUtil.disableSpies();

    final Transaction transaction =
      new Transaction( Arez.context(), null, ValueUtil.randomString(), false, null, false );

    assertInvariantFailure( transaction::getStartedAt,
                            "Arez-0134: Transaction.getStartedAt() invoked when Arez.areSpiesEnabled() is false" );
  }

  @Test
  public void asInfo_whenSpyDisabled()
  {
    ArezTestUtil.disableSpies();

    final Transaction transaction =
      new Transaction( Arez.context(), null, ValueUtil.randomString(), false, null, false );

    assertInvariantFailure( transaction::asInfo,
                            "Arez-0198: TransactionInfo.asInfo() invoked but Arez.areSpiesEnabled() returned false." );
  }

  @Test
  public void beginTransaction()
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final String name = ValueUtil.randomString();
    final Transaction transaction = Transaction.begin( context, name, false, null );

    assertTrue( context.isTransactionActive() );

    assertEquals( context.getTransaction(), transaction );
    assertEquals( transaction.getContext(), context );
    assertEquals( transaction.getName(), name );
    assertFalse( transaction.isMutation() );
    assertNull( transaction.getTracker() );
    assertNull( transaction.getPrevious() );
    assertNull( transaction.getPreviousInSameContext() );
    assertFalse( transaction.isMutation() );
  }

  @Test
  public void beginTransaction_nested()
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
  {
    final Zone zone1 = Arez.createZone();
    final Zone zone2 = Arez.createZone();
    final ArezContext context1 = zone1.getContext();
    final ArezContext context2 = zone2.getContext();

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
    assertNull( transaction2.getPreviousInSameContext() );
  }

  @Test
  public void beginTransaction_triple_nested_alternating_contexts()
  {
    final Zone zone1 = Arez.createZone();
    final Zone zone2 = Arez.createZone();
    final ArezContext context1 = zone1.getContext();
    final ArezContext context2 = zone2.getContext();

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
  {
    ArezTestUtil.disableZones();

    final ArezContext context1 = new ArezContext( null );
    final ArezContext context2 = new ArezContext( null );

    assertFalse( context1.isTransactionActive() );
    assertFalse( context2.isTransactionActive() );

    final String name1 = ValueUtil.randomString();
    final String name2 = ValueUtil.randomString();
    Transaction.begin( context1, name1, false, null );
    assertTrue( context1.isTransactionActive() );
    assertTrue( context2.isTransactionActive() );

    assertInvariantFailure( () -> Transaction.begin( context2, name2, false, null ),
                            "Arez-0120: Zones are not enabled but the transaction named '" +
                            name2 +
                            "' is nested in a " +
                            "transaction named '" +
                            name1 +
                            "' from a different context." );
  }

  @Test
  public void beginTransaction_generates_spyEvent()
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    final String name = ValueUtil.randomString();
    Transaction.begin( context, name, false, null );

    handler.assertEventCount( 1 );
    handler.assertNextEvent( TransactionStartEvent.class, event -> {
      assertEquals( event.getName(), name );
      assertFalse( event.isMutation() );
      assertNull( event.getTracker() );
    } );
  }

  @Test
  public void beginTransaction_attemptToNest_READ_WRITE_in_READ_ONLY()
  {
    final ArezContext context = Arez.context();

    setCurrentTransaction( context.observer( new CountAndObserveProcedure() ) );
    final Transaction transaction = context.getTransaction();

    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> Transaction.begin( context, name, true, null ),
                            "Arez-0119: Attempting to create READ_WRITE transaction named '" + name +
                            "' but it is nested in transaction named '" + transaction.getName() + "' with " +
                            "mode READ_ONLY which is not equal to READ_WRITE." );
  }

  @Test
  public void beginTransaction_attemptToNest_READ_WRITE_in_READ_WRITE_OWNED()
  {
    final ArezContext context = Arez.context();

    setCurrentTransaction( context.computable( () -> "" ).getObserver() );
    final Transaction transaction = context.getTransaction();

    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> Transaction.begin( context, name, true, null ),
                            "Arez-0186: Attempting to create transaction named '" + name +
                            "' nested in ComputableValue transaction named '" + transaction.getName() + "'. " +
                            "ComputableValues must not invoke actions or observe methods as they should derive " +
                            "values from other computeds and observables." );
  }

  @Test
  public void begin_attempt_to_nestAction_in_non_computed_tracker_transaction()
  {
    final ArezContext context = Arez.context();

    final Observer tracker = context.tracker( () -> {
    } );
    setCurrentTransaction( newReadWriteObserver( context ) );

    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> Transaction.begin( context, name, true, tracker ),
                            "Arez-0171: Attempting to create a tracking transaction named '" +
                            name +
                            "' for the " +
                            "observer named '" +
                            tracker.getName() +
                            "' but the transaction is not a top-level transaction " +
                            "when this is required. This may be a result of nesting a observe() call inside an action or " +
                            "another observer function." );
  }

  @Test
  public void beginTransaction_attemptToNest_READ_ONLY_in_READ_WRITE_OWNED()
  {
    final ArezContext context = Arez.context();

    setCurrentTransaction( context.computable( () -> "" ).getObserver() );
    final Transaction transaction = context.getTransaction();

    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> Transaction.begin( context, name, false, null ),
                            "Arez-0186: Attempting to create transaction named '" + name +
                            "' nested in ComputableValue transaction named '" + transaction.getName() + "'. " +
                            "ComputableValues must not invoke actions or observe methods as they should derive " +
                            "values from other computeds and observables." );
  }

  @Test
  public void commitTransaction_matchingRootTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );
    transaction.begin();
    Transaction.setTransaction( transaction );

    Transaction.commit( transaction );

    assertFalse( context.isTransactionActive() );
    assertTrue( context.isSchedulerEnabled() );
  }

  @Test
  public void commitTransaction_onlyRootEnablesScheduler()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction1 =
      Transaction.begin( context, ValueUtil.randomString(), false, null );
    final Transaction transaction2 =
      Transaction.begin( context, ValueUtil.randomString(), false, null );

    assertTrue( context.isTransactionActive() );
    assertFalse( context.isSchedulerEnabled() );

    Transaction.commit( transaction2 );

    assertTrue( context.isTransactionActive() );
    assertFalse( context.isSchedulerEnabled() );

    Transaction.commit( transaction1 );

    assertFalse( context.isTransactionActive() );
    assertTrue( context.isSchedulerEnabled() );
  }

  @Test
  public void commitTransaction_enablesScheduler_forInterleavedContexts()
  {
    final Zone zone1 = Arez.createZone();
    final Zone zone2 = Arez.createZone();
    final ArezContext context1 = zone1.getContext();
    final ArezContext context2 = zone2.getContext();

    final Transaction transaction1 =
      Transaction.begin( context1, ValueUtil.randomString(), false, null );
    final Transaction transaction2 =
      Transaction.begin( context2, ValueUtil.randomString(), false, null );

    assertFalse( context1.isTransactionActive() );
    assertFalse( context1.isSchedulerEnabled() );
    assertTrue( context2.isTransactionActive() );
    assertFalse( context2.isSchedulerEnabled() );

    Transaction.commit( transaction2 );

    assertTrue( context1.isTransactionActive() );
    assertFalse( context1.isSchedulerEnabled() );
    assertFalse( context2.isTransactionActive() );
    assertTrue( context2.isSchedulerEnabled() );

    Transaction.commit( transaction1 );

    assertFalse( context1.isTransactionActive() );
    assertTrue( context1.isSchedulerEnabled() );
  }

  @Test
  public void commitTransaction_enablesScheduler_forStripedContexts()
  {
    final Zone zone1 = Arez.createZone();
    final Zone zone2 = Arez.createZone();
    final ArezContext context1 = zone1.getContext();
    final ArezContext context2 = zone2.getContext();

    final Transaction transaction1 =
      Transaction.begin( context1, ValueUtil.randomString(), false, null );
    final Transaction transaction2 =
      Transaction.begin( context2, ValueUtil.randomString(), false, null );
    final Transaction transaction3 =
      Transaction.begin( context1, ValueUtil.randomString(), false, null );
    final Transaction transaction4 =
      Transaction.begin( context2, ValueUtil.randomString(), false, null );

    assertFalse( context1.isTransactionActive() );
    assertFalse( context1.isSchedulerEnabled() );
    assertTrue( context2.isTransactionActive() );
    assertFalse( context2.isSchedulerEnabled() );

    Transaction.commit( transaction4 );

    assertTrue( context1.isTransactionActive() );
    assertFalse( context1.isSchedulerEnabled() );
    assertFalse( context2.isTransactionActive() );
    assertFalse( context2.isSchedulerEnabled() );

    Transaction.commit( transaction3 );

    assertFalse( context1.isTransactionActive() );
    assertFalse( context1.isSchedulerEnabled() );
    assertTrue( context2.isTransactionActive() );
    assertFalse( context2.isSchedulerEnabled() );

    Transaction.commit( transaction2 );

    assertTrue( context1.isTransactionActive() );
    assertFalse( context1.isSchedulerEnabled() );
    assertFalse( context2.isTransactionActive() );
    assertTrue( context2.isSchedulerEnabled() );

    Transaction.commit( transaction1 );

    assertFalse( context1.isTransactionActive() );
    assertTrue( context1.isSchedulerEnabled() );
  }

  @Test
  public void commitTransaction_generates_spyEvent()
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    final String name = ValueUtil.randomString();
    final Transaction transaction = new Transaction( context, null, name, false, null, false );
    transaction.begin();
    Transaction.setTransaction( transaction );

    Transaction.commit( transaction );

    handler.assertEventCount( 1 );
    handler.assertNextEvent( TransactionCompleteEvent.class, event -> {
      assertEquals( event.getName(), name );
      assertFalse( event.isMutation() );
      assertNull( event.getTracker() );
      assertTrue( event.getDuration() >= 0 );
    } );
  }

  @Test
  public void commitTransaction_nonMatchingRootTransaction()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );
    transaction.begin();
    Transaction.setTransaction( transaction );

    final Transaction transaction2 = new Transaction( context, null, ValueUtil.randomString(), false, null, false );
    transaction2.begin();

    assertInvariantFailure( () -> Transaction.commit( transaction2 ),
                            "Arez-0123: Attempting to commit transaction named '" + transaction2.getName() +
                            "' but this does not match existing transaction named '" + transaction.getName() + "'." );
  }

  @Test
  public void commitTransaction_noTransactionActive()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );
    transaction.begin();

    assertInvariantFailure( () -> Transaction.commit( transaction ),
                            "Arez-0122: Attempting to commit transaction named '" + transaction.getName() +
                            "' but no transaction is active." );
  }

  @Test
  public void getPreviousInSameContext_zonesNotEnabled()
  {
    ArezTestUtil.disableZones();

    final Transaction transaction = new Transaction( null, null, ValueUtil.randomString(), false, null, false );

    assertInvariantFailure( transaction::getPreviousInSameContext,
                            "Arez-0137: Attempted to invoke getPreviousInSameContext() on transaction named '" +
                            transaction.getName() + "' when zones are not enabled." );
  }

  @Test
  public void current()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );
    transaction.begin();
    Transaction.setTransaction( transaction );

    assertEquals( Transaction.current(), transaction );
  }

  @Test
  public void current_noTransaction()
  {
    assertInvariantFailure( Transaction::current,
                            "Arez-0117: Attempting to get current transaction but no transaction is active." );
  }

  @Test
  public void isTransactionActive()
  {
    final ArezContext context = Arez.context();

    final Transaction transaction = new Transaction( context, null, ValueUtil.randomString(), false, null, false );
    transaction.begin();

    assertFalse( Transaction.isTransactionActive( context ) );

    Transaction.setTransaction( transaction );

    assertTrue( Transaction.isTransactionActive( context ) );
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
    final Transaction transaction =
      new Transaction( Arez.context(), null, ValueUtil.randomString(), true, null, false );

    final MyDisposable node = new MyDisposable();

    transaction.reportDispose( node );

    node.dispose();

    assertInvariantFailure( () -> transaction.reportDispose( node ),
                            "Arez-0176: Invoked reportDispose on transaction named '" +
                            transaction.getName() + "' where the element is disposed." );
  }

  @Test
  public void reportDispose_BAD_TransactionMode()
  {
    final Transaction transaction =
      new Transaction( Arez.context(), null, ValueUtil.randomString(), false, null, false );

    final MyDisposable node = new MyDisposable();

    assertInvariantFailure( () -> transaction.reportDispose( node ),
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
    randomObserver.setState( Observer.Flags.STATE_UP_TO_DATE );
    final ComputableValue<?> computableValue = observer.getComputableValue();
    final ObservableValue<?> observableValue = computableValue.getObservableValue();
    observableValue.rawAddObserver( randomObserver );
    randomObserver.getDependencies().add( observableValue );
    return randomObserver;
  }
}
