package arez;

import arez.spy.ActionCompletedEvent;
import arez.spy.ActionStartedEvent;
import arez.spy.ComputeCompletedEvent;
import arez.spy.ComputeStartedEvent;
import arez.spy.ComputedValueDeactivatedEvent;
import arez.spy.ComputedValueDisposedEvent;
import arez.spy.ObservableValueChangedEvent;
import arez.spy.ObserverCreatedEvent;
import arez.spy.ObserverDisposedEvent;
import arez.spy.ObserverInfo;
import arez.spy.Priority;
import arez.spy.ReactionCompletedEvent;
import arez.spy.ReactionScheduledEvent;
import arez.spy.ReactionStartedEvent;
import arez.spy.TransactionCompletedEvent;
import arez.spy.TransactionStartedEvent;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "Duplicates" )
public class ObserverTest
  extends AbstractArezTest
{
  @Test
  public void initialState()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final CountAndObserveProcedure observed = new CountAndObserveProcedure();
    final CountingProcedure onDepsChanged = new CountingProcedure();
    final Observer observer = new Observer( context, null, name, observed, onDepsChanged, Flags.RUN_LATER );

    // Verify all "Node" behaviour
    assertEquals( observer.getContext(), context );
    assertEquals( observer.getName(), name );
    assertEquals( observer.toString(), name );

    assertTrue( observer.shouldExecuteObservedNext() );

    // Starts out inactive and inactive means no dependencies
    assertEquals( observer.getState(), Flags.STATE_INACTIVE );
    assertFalse( observer.isActive() );
    assertTrue( observer.isInactive() );
    assertEquals( observer.getDependencies().size(), 0 );

    // Reaction attributes
    assertFalse( observer.isMutation() );
    assertEquals( observer.getObserved(), observed );
    assertEquals( observer.getOnDepsChanged(), onDepsChanged );
    assertTrue( observer.isScheduled() );

    assertFalse( observer.isComputedValue() );

    assertEquals( observer.getPriority(), Priority.NORMAL );
    assertFalse( observer.canObserveLowerPriorityDependencies() );

    observer.invariantState();

    assertEquals( context.getTopLevelObservers().get( observer.getName() ), observer );
  }

  @Test
  public void initialState_externalExecutor()
  {
    final CountingProcedure onDepsChanged = new CountingProcedure();
    final Observer observer = new Observer( Arez.context(), null, ValueUtil.randomString(), null, onDepsChanged, 0 );

    assertFalse( observer.shouldExecuteObservedNext() );

    assertNull( observer.getObserved() );
    assertEquals( observer.getOnDepsChanged(), onDepsChanged );
    assertFalse( observer.isComputedValue() );
    assertTrue( observer.isApplicationExecutor() );
  }

  @Test
  public void initialStateForComputedValueObserver()
  {
    final ComputedValue<String> computedValue = Arez.context().computed( () -> "" );
    final Observer observer = computedValue.getObserver();

    assertTrue( observer.isComputedValue() );
    assertTrue( observer.shouldExecuteObservedNext() );

    assertEquals( computedValue.getObservableValue().getName(), observer.getName() );
    assertEquals( computedValue.getObservableValue().getObserver(), observer );

    assertEquals( computedValue.getName(), observer.getName() );
    assertEquals( computedValue.getObserver(), observer );
  }

  @Test
  public void construct_with_no_observed_and_no_onDepsChanged_parameter()
  {
    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> new Observer( Arez.context(),
                                                null,
                                                name,
                                                null,
                                                null,
                                                0 ),
                            "Arez-0204: Observer named '" + name + "' has not supplied a value for either " +
                            "the observed parameter or the onDepsChanged parameter." );
  }

  @Test
  public void construct_with_invalid_priority()
  {
    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> new Observer( Arez.context(),
                                                null,
                                                name,
                                                new CountAndObserveProcedure(),
                                                null,
                                                Flags.PRIORITY_LOWEST + Flags.PRIORITY_HIGHEST ),
                            "Arez-0080: Observer named '" + name + "' has invalid priority 5." );
  }

  @Test
  public void construct_with_OBSERVE_LOWER_PRIORITY_DEPENDENCIES_and_PRIORITY_LOWEST()
  {
    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> new Observer( Arez.context(),
                                                null,
                                                name,
                                                new CountingProcedure(),
                                                new CountingProcedure(),
                                                Flags.PRIORITY_LOWEST | Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES ),
                            "Arez-0184: Observer named '" +
                            name +
                            "' has LOWEST priority but has passed " +
                            "OBSERVE_LOWER_PRIORITY_DEPENDENCIES option which should not be present as the observer " +
                            "has no lower priority." );
  }

  @Test
  public void construct_with_READ_ONLY_and_READ_WRITE()
  {
    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> new Observer( Arez.context(),
                                                null,
                                                name,
                                                new CountAndObserveProcedure(),
                                                null,
                                                Flags.READ_ONLY | Flags.READ_WRITE ),
                            "Arez-0079: Observer named '" + name + "' incorrectly specified both " +
                            "READ_ONLY and READ_WRITE transaction mode flags." );
  }

  @Test
  public void construct_with_REACT_IMMEDIATELY_and_DEFER_REACT()
  {
    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> new Observer( Arez.context(),
                                                null,
                                                name,
                                                new CountAndObserveProcedure(),
                                                null,
                                                Flags.RUN_NOW | Flags.RUN_LATER ),
                            "Arez-0081: Observer named '" + name + "' incorrectly specified both " +
                            "RUN_NOW and RUN_LATER flags." );
  }

  @Test
  public void construct_with_REACT_IMMEDIATELY_and_no_observed()
  {
    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> new Observer( Arez.context(),
                                                null,
                                                name,
                                                null,
                                                new CountingProcedure(),
                                                Flags.RUN_NOW ),
                            "Arez-0206: Observer named '" + name + "' incorrectly specified RUN_NOW " +
                            "flag but the observed function is null." );
  }

  @Test
  public void construct_with_KEEPALIVE_and_DEACTIVATE_ON_UNOBSERVE()
  {
    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> new Observer( Arez.context(),
                                                null,
                                                name,
                                                new CountAndObserveProcedure(),
                                                new CountingProcedure(),
                                                Flags.KEEPALIVE | Flags.DEACTIVATE_ON_UNOBSERVE ),
                            "Arez-0210: Observer named '" + name + "' incorrectly specified multiple schedule " +
                            "type flags (KEEPALIVE, DEACTIVATE_ON_UNOBSERVE, APPLICATION_EXECUTOR)." );
  }

  @Test
  public void construct_with_IllegalFlags()
  {
    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> new Observer( Arez.context(),
                                                null,
                                                name,
                                                new CountAndObserveProcedure(),
                                                new CountingProcedure(),
                                                1 << 14 ),
                            "Arez-0207: Observer named '" + name + "' specified illegal flags: " + ( 1 << 14 ) );
  }

  @Test
  public void construct_with_NESTED_ACTIONS_ALLOWED_and_NESTED_ACTIONS_DISALLOWED()
  {
    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> new Observer( Arez.context(),
                                                null,
                                                name,
                                                new CountAndObserveProcedure(),
                                                new CountingProcedure(),
                                                Flags.NESTED_ACTIONS_ALLOWED | Flags.NESTED_ACTIONS_DISALLOWED ),
                            "Arez-0209: Observer named '" + name + "' incorrectly specified both the " +
                            "NESTED_ACTIONS_ALLOWED flag and the NESTED_ACTIONS_DISALLOWED flag." );
  }

  @Test
  public void construct_with_computed_value_REACT_IMMEDIATELY_and_not_KEEPALIVE()
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue = context.computed( () -> "" );

    assertInvariantFailure( () -> new Observer( computedValue, Flags.RUN_NOW ),
                            "Arez-0208: ComputedValue named 'ComputedValue@1' incorrectly specified RUN_NOW flag but not the KEEPALIVE flag." );
  }

  @Test
  public void construct_with_mode_but_checking_Disabled()
  {
    ArezTestUtil.noEnforceTransactionType();

    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> new Observer( Arez.context(),
                                                null,
                                                name,
                                                new CountingProcedure(),
                                                new CountingProcedure(),
                                                Flags.READ_ONLY ),
                            "Arez-0082: Observer named '" + name + "' specified transaction mode 'READ_ONLY' " +
                            "when Arez.enforceTransactionType() is false." );
  }

  @SuppressWarnings( "ResultOfMethodCallIgnored" )
  @Test
  public void isMutation_noEnforceTransactionType()
  {
    ArezTestUtil.noEnforceTransactionType();

    final String name = ValueUtil.randomString();

    final Observer observer =
      new Observer( Arez.context(),
                    null,
                    name,
                    new CountAndObserveProcedure(),
                    null,
                    0 );
    assertThrows( observer::isMutation );
  }

  @Test
  public void constructWithComponentWhenNativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final Component component =
      new Component( Arez.context(),
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     null,
                     null );

    final String name = ValueUtil.randomString();
    assertInvariantFailure( () -> new Observer( Arez.context(),
                                                component,
                                                name,
                                                new CountingProcedure(),
                                                new CountingProcedure(),
                                                0 ),
                            "Arez-0083: Observer named '" + name + "' has component specified but " +
                            "Arez.areNativeComponentsEnabled() is false." );
  }

  @Test
  public void basicLifecycle_withComponent()
  {
    final Component component =
      new Component( Arez.context(),
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     null,
                     null );

    final String name = ValueUtil.randomString();
    final Observer observer =
      new Observer( Arez.context(),
                    component,
                    name,
                    new CountAndObserveProcedure(),
                    null,
                    0 );
    assertEquals( observer.getName(), name );
    assertEquals( observer.getComponent(), component );

    assertTrue( component.getObservers().contains( observer ) );

    observer.dispose();

    assertFalse( component.getObservers().contains( observer ) );
  }

  @Test
  public void invariantDependenciesBackLink()
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = Arez.context().observable();
    observer.getDependencies().add( observableValue );

    assertInvariantFailure( () -> observer.invariantDependenciesBackLink( "TEST1" ),
                            "Arez-0090: TEST1: Observer named '" +
                            observer.getName() +
                            "' has ObservableValue " +
                            "dependency named '" +
                            observableValue.getName() +
                            "' which does not contain the observer in " +
                            "the list of observers." );

    //Setup correct back link
    observableValue.addObserver( observer );

    // Back link created so should be good
    observer.invariantDependenciesBackLink( "TEST2" );
  }

  @Test
  public void invariantDependenciesNotDisposed()
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = Arez.context().observable();
    observer.getDependencies().add( observableValue );
    observableValue.addObserver( observer );

    observableValue.setWorkState( ObservableValue.DISPOSED );

    assertInvariantFailure( observer::invariantDependenciesNotDisposed,
                            "Arez-0091: Observer named '" +
                            observer.getName() +
                            "' has ObservableValue dependency named '" +
                            observableValue.getName() +
                            "' which is disposed." );

    observableValue.setWorkState( 0 );

    observer.invariantDependenciesNotDisposed();
  }

  @Test
  public void invariantDependenciesUnique()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.observer( observable::reportObserved );

    observer.invariantDependenciesUnique( "TEST1" );

    // Add a duplicate
    observer.getDependencies().add( observable );

    assertInvariantFailure( () -> observer.invariantDependenciesUnique( "TEST2" ),
                            "Arez-0089: TEST2: The set of dependencies in observer named '" +
                            observer.getName() +
                            "' is not unique. Current list: '[" +
                            observable.getName() +
                            ", " +
                            observable.getName() +
                            "]'." );
  }

  @Test
  public void invariantState()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.observer( new CountingProcedure(), Flags.RUN_LATER );

    observer.invariantState();

    observer.getDependencies().add( observable );

    assertInvariantFailure( observer::invariantState,
                            "Arez-0092: Observer named '" + observer.getName() + "' is inactive " +
                            "but still has dependencies: [" + observable.getName() + "]." );
  }

  @Test
  public void invariantState_derivedNotLinkBack()
    throws Exception
  {
    final Observer observer = Arez.context().computed( () -> "" ).getObserver();
    final ComputedValue<?> computedValue = observer.getComputedValue();

    observer.invariantState();

    setField( computedValue, "_observableValue", Arez.context().observable() );

    assertInvariantFailure( observer::invariantState,
                            "Arez-0093: Observer named '" + observer.getName() + "' is associated with an " +
                            "ObservableValue that does not link back to observer." );
  }

  @Test
  public void invariantDerivationState()
  {
    final ArezContext context = Arez.context();
    final ComputedValue<String> computedValue = context.computed( () -> "" );
    final Observer observer = computedValue.getObserver();

    observer.invariantComputedValueObserverState();

    context.safeAction( () -> observer.setState( Flags.STATE_UP_TO_DATE ), Flags.NO_VERIFY_ACTION_REQUIRED );

    assertInvariantFailure( () -> context.safeAction( observer::invariantComputedValueObserverState ),
                            "Arez-0094: Observer named '" + observer.getName() + "' is a ComputedValue and " +
                            "active but the associated ObservableValue has no observers." );

    context.observer( () -> computedValue.getObservableValue().reportObserved() );

    observer.invariantComputedValueObserverState();
  }

  @Test
  public void invariantDerivationState_observerCanHaveNoDependenciesIfItIsTracker()
  {
    final Observer observer = Arez.context().computed( () -> "" ).getObserver();

    observer.invariantComputedValueObserverState();

    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_UP_TO_DATE );

    observer.invariantComputedValueObserverState();
  }

  @Test
  public void replaceDependencies()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.observer( new CountingProcedure(), Flags.AREZ_OR_NO_DEPENDENCIES );

    final ArrayList<ObservableValue<?>> originalDependencies = observer.getDependencies();

    assertTrue( originalDependencies.isEmpty() );

    context.safeAction( () -> {

      final ArrayList<ObservableValue<?>> newDependencies = new ArrayList<>();
      newDependencies.add( observable );
      observable.rawAddObserver( observer );

      observer.replaceDependencies( newDependencies );

      assertEquals( observer.getDependencies().size(), 1 );
      assertNotSame( observer.getDependencies(), originalDependencies );
      assertTrue( observer.getDependencies().contains( observable ) );
    }, Flags.NO_VERIFY_ACTION_REQUIRED );
  }

  @Test
  public void replaceDependencies_duplicateDependency()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.observer( observable::reportObserved );

    final ArrayList<ObservableValue<?>> newDependencies = new ArrayList<>();
    newDependencies.add( observable );
    newDependencies.add( observable );

    assertInvariantFailure( () -> observer.replaceDependencies( newDependencies ),
                            "Arez-0089: Post replaceDependencies: The set of dependencies in observer named 'Observer@2' is not unique. Current list: '[ObservableValue@1, ObservableValue@1]'." );
  }

  @Test
  public void replaceDependencies_notBackLinkedDependency()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final ObservableValue<Object> observable2 = context.observable();
    final Observer observer = context.observer( observable::reportObserved );

    final ArrayList<ObservableValue<?>> newDependencies = new ArrayList<>();
    newDependencies.add( observable );
    newDependencies.add( observable2 );

    assertInvariantFailure( () -> observer.replaceDependencies( newDependencies ),
                            "Arez-0090: Post replaceDependencies: Observer named 'Observer@3' has ObservableValue dependency named 'ObservableValue@2' which does not contain the observer in the list of observers." );
  }

  @Test
  public void clearDependencies()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<?> observableValue1 = context.observable();
    final ObservableValue<?> observableValue2 = context.observable();

    final Observer observer = context.observer( () -> {
      observableValue1.reportObserved();
      observableValue2.reportObserved();
    } );

    assertEquals( observer.getDependencies().size(), 2 );
    assertEquals( observableValue1.getObservers().size(), 1 );
    assertEquals( observableValue2.getObservers().size(), 1 );

    context.safeAction( observer::clearDependencies, Flags.NO_VERIFY_ACTION_REQUIRED );

    assertEquals( observer.getDependencies().size(), 0 );
    assertEquals( observableValue1.getObservers().size(), 0 );
    assertEquals( observableValue2.getObservers().size(), 0 );
  }

  @Test
  public void runHook_nullHook()
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );

    observer.runHook( null, ObserverError.ON_ACTIVATE_ERROR );
  }

  @Test
  public void runHook_normalHook()
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );

    final AtomicInteger counter = new AtomicInteger();

    observer.runHook( counter::incrementAndGet, ObserverError.ON_ACTIVATE_ERROR );

    assertEquals( counter.get(), 1 );
  }

  @Test
  public void runHook_hookThrowsException()
  {
    setIgnoreObserverErrors( true );

    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );

    final Exception exception = new Exception( "X" );

    final AtomicInteger handlerCallCount = new AtomicInteger();
    final AtomicBoolean observerMatches = new AtomicBoolean( false );
    final AtomicBoolean errorMatches = new AtomicBoolean( false );
    final AtomicBoolean throwableMatches = new AtomicBoolean( false );

    Arez.context().addObserverErrorHandler( ( ( observer1, error, throwable ) -> {
      handlerCallCount.incrementAndGet();
      observerMatches.set( observer == observer1 );
      errorMatches.set( error == ObserverError.ON_ACTIVATE_ERROR );
      throwableMatches.set( throwable == exception );
    } ) );

    observer.runHook( () -> {
      throw exception;
    }, ObserverError.ON_ACTIVATE_ERROR );

    assertEquals( handlerCallCount.get(), 1 );
    assertTrue( observerMatches.get(), "Observer matches" );
    assertTrue( errorMatches.get(), "Error matches" );
    assertTrue( throwableMatches.get(), "throwable matches" );
  }

  @Test
  public void setState()
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );

    setupReadWriteTransaction();

    observer.setState( Flags.STATE_INACTIVE );

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );
    assertFalse( observer.isScheduled() );

    observer.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertFalse( observer.isScheduled() );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    assertEquals( observer.getState(), Flags.STATE_POSSIBLY_STALE );
    assertFalse( observer.isScheduled() );

    observer.clearScheduledFlag();
    Arez.context().getScheduler().getPendingObservers().truncate( 0 );
    assertFalse( observer.isScheduled() );

    observer.setState( Flags.STATE_STALE );
    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertTrue( observer.isScheduled() );

    observer.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    observer.setState( Flags.STATE_STALE );

    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertTrue( observer.isScheduled() );

    final ObservableValue<?> observableValue1 = Arez.context().observable();
    final ObservableValue<?> observableValue2 = Arez.context().observable();

    observer.getDependencies().add( observableValue1 );
    observer.getDependencies().add( observableValue2 );
    observableValue1.rawAddObserver( observer );
    observableValue2.rawAddObserver( observer );

    assertEquals( observer.getDependencies().size(), 2 );
    assertEquals( observableValue1.getObservers().size(), 1 );
    assertEquals( observableValue2.getObservers().size(), 1 );

    observer.setState( Flags.STATE_INACTIVE );

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );

    assertEquals( observer.getDependencies().size(), 0 );
    assertEquals( observableValue1.getObservers().size(), 0 );
    assertEquals( observableValue2.getObservers().size(), 0 );
  }

  @Test
  public void setState_scheduleFalse()
  {
    setupReadWriteTransaction();

    final Observer observer = Arez.context().computed( () -> "" ).getObserver();

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );

    observer.setState( Flags.STATE_INACTIVE, false );

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );
    assertFalse( observer.isScheduled() );

    observer.setState( Flags.STATE_UP_TO_DATE, false );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertFalse( observer.isScheduled() );

    observer.setState( Flags.STATE_POSSIBLY_STALE, false );

    assertEquals( observer.getState(), Flags.STATE_POSSIBLY_STALE );
    assertFalse( observer.isScheduled() );

    observer.clearScheduledFlag();
    Arez.context().getScheduler().getPendingObservers().truncate( 0 );
    assertFalse( observer.isScheduled() );

    observer.setState( Flags.STATE_STALE, false );
    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertFalse( observer.isScheduled() );

    observer.setState( Flags.STATE_UP_TO_DATE, false );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    observer.setState( Flags.STATE_STALE, false );

    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertFalse( observer.isScheduled() );

    final ObservableValue<?> observableValue1 = Arez.context().observable();
    final ObservableValue<?> observableValue2 = Arez.context().observable();

    observer.getDependencies().add( observableValue1 );
    observer.getDependencies().add( observableValue2 );
    observableValue1.rawAddObserver( observer );
    observableValue2.rawAddObserver( observer );

    assertEquals( observer.getDependencies().size(), 2 );
    assertEquals( observableValue1.getObservers().size(), 1 );
    assertEquals( observableValue2.getObservers().size(), 1 );

    observer.setState( Flags.STATE_INACTIVE, false );

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );

    assertEquals( observer.getDependencies().size(), 0 );
    assertEquals( observableValue1.getObservers().size(), 0 );
    assertEquals( observableValue2.getObservers().size(), 0 );
  }

  @Test
  public void setState_onComputedValue()
  {
    final TestProcedure onActivate = new TestProcedure();
    final TestProcedure onDeactivate = new TestProcedure();
    final TestProcedure onStale = new TestProcedure();

    final ComputedValue<String> computedValue =
      Arez.context().computed( null, null, () -> "", onActivate, onDeactivate, onStale );
    final ObservableValue<String> derivedValue = computedValue.getObservableValue();
    final Observer observer = computedValue.getObserver();

    final Observer watcher = observer.getContext().observer( new CountAndObserveProcedure() );

    setupReadWriteTransaction();

    watcher.setState( Flags.STATE_UP_TO_DATE );
    observer.getComputedValue().getObservableValue().rawAddObserver( watcher );
    watcher.getDependencies().add( observer.getComputedValue().getObservableValue() );

    watcher.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );

    observer.setState( Flags.STATE_INACTIVE );

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );
    assertFalse( observer.isScheduled() );
    assertEquals( onActivate.getCalls(), 0 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 0 );

    computedValue.setValue( ValueUtil.randomString() );
    observer.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertFalse( observer.isScheduled() );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 0 );
    assertNotNull( computedValue.getValue() );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    assertEquals( observer.getState(), Flags.STATE_POSSIBLY_STALE );
    assertTrue( observer.isScheduled() );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 1 );
    assertEquals( watcher.getState(), Flags.STATE_POSSIBLY_STALE );
    assertEquals( derivedValue.getLeastStaleObserverState(), Flags.STATE_POSSIBLY_STALE );
    assertNotNull( computedValue.getValue() );

    observer.clearScheduledFlag();
    Arez.context().getScheduler().getPendingObservers().truncate( 0 );
    assertFalse( observer.isScheduled() );

    observer.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 1 );
    assertNotNull( computedValue.getValue() );

    watcher.setState( Flags.STATE_UP_TO_DATE );
    derivedValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    observer.setState( Flags.STATE_STALE );

    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertTrue( observer.isScheduled() );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 2 );

    assertEquals( watcher.getState(), Flags.STATE_POSSIBLY_STALE );
    assertEquals( derivedValue.getLeastStaleObserverState(), Flags.STATE_POSSIBLY_STALE );

    observer.setState( Flags.STATE_INACTIVE );

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 1 );
    assertEquals( onStale.getCalls(), 2 );
    assertNull( computedValue.getValue() );
  }

  @Test
  public void setState_activateWhenDisposed()
  {
    final Observer observer = Arez.context().computed( () -> "" ).getObserver();
    setCurrentTransaction( observer );

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );

    observer.markAsDisposed();

    assertInvariantFailure( () -> observer.setState( Flags.STATE_UP_TO_DATE ),
                            "Arez-0087: Attempted to activate disposed observer named '" + observer.getName() + "'." );
  }

  @Test
  public void setState_noTransaction()
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );

    assertInvariantFailure( () -> observer.setState( Flags.STATE_UP_TO_DATE ),
                            "Arez-0086: Attempt to invoke setState on observer named '" +
                            observer.getName() +
                            "' when " +
                            "there is no active transaction." );
  }

  @Test
  public void scheduleReaction()
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );

    setupReadWriteTransaction();
    observer.setState( Flags.STATE_UP_TO_DATE );

    assertFalse( observer.isScheduled() );

    observer.scheduleReaction();

    final ArezContext context = Arez.context();

    assertTrue( observer.isScheduled() );
    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
    assertTrue( context.getScheduler().getPendingObservers().contains( observer ) );

    //Duplicate schedule should not result in it being added again
    observer.scheduleReaction();

    assertTrue( observer.isScheduled() );
    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
    assertTrue( context.getScheduler().getPendingObservers().contains( observer ) );
  }

  @Test
  public void scheduleReaction_when_Disposed()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );

    context.safeAction( () -> {
      observer.setState( Flags.STATE_INACTIVE );
      observer.setState( Flags.STATE_DISPOSED );

      assertFalse( observer.isScheduled() );

      observer.scheduleReaction();

      assertFalse( observer.isScheduled() );
    }, Flags.NO_VERIFY_ACTION_REQUIRED );
  }

  @Test
  public void scheduleReaction_whenInactive()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );

    context.safeAction( () -> observer.setState( Flags.STATE_INACTIVE ), Flags.NO_VERIFY_ACTION_REQUIRED );

    assertInvariantFailure( () -> context.safeAction( observer::scheduleReaction, Flags.NO_VERIFY_ACTION_REQUIRED ),
                            "Arez-0088: Observer named '" + observer.getName() + "' is not active but an attempt has " +
                            "been made to schedule observer." );
  }

  @Test
  public void dispose()
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );
    observer.setState( Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( null );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertTrue( observer.isNotDisposed() );
    assertFalse( observer.isDisposed() );

    observer.dispose();

    assertEquals( observer.getState(), Flags.STATE_DISPOSED );
    assertFalse( observer.isNotDisposed() );
    assertTrue( observer.isDisposed() );

    final ArezContext context = Arez.context();

    final int currentNextTransactionId = context.currentNextTransactionId();

    observer.dispose();

    // This verifies no new transactions were created
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId );
  }

  @Test
  public void dispose_onComputedValue()
  {
    final Observer observer = Arez.context().computed( () -> "" ).getObserver();
    setCurrentTransaction( observer );
    observer.setState( Flags.STATE_STALE );

    final ObservableValue<?> observableValue = Arez.context().observable();
    observableValue.setLeastStaleObserverState( Flags.STATE_STALE );
    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    Transaction.setTransaction( null );

    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertTrue( observer.isNotDisposed() );
    assertFalse( observer.isDisposed() );
    assertEquals( observableValue.getObservers().size(), 1 );

    observer.dispose();

    assertEquals( observer.getState(), Flags.STATE_DISPOSED );
    assertFalse( observer.isNotDisposed() );
    assertTrue( observer.isDisposed() );
    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );

    final ArezContext context = Arez.context();

    final int currentNextTransactionId = context.currentNextTransactionId();

    observer.dispose();

    // This verifies no new transactions were created
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId );
  }

  @Test
  public void dispose_generates_spyEvent()
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );
    observer.setState( Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( null );

    assertFalse( observer.isDisposed() );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    Arez.context().getSpy().addSpyEventHandler( handler );

    observer.dispose();

    assertTrue( observer.isDisposed() );

    handler.assertEventCount( 5 );
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class );
    handler.assertNextEvent( ObserverDisposedEvent.class,
                             event -> assertEquals( event.getObserver().getName(), observer.getName() ) );
  }

  @Test
  public void dispose_does_not_generate_spyEvent_forDerivedValues()
  {
    final Observer observer = Arez.context().computed( () -> "" ).getObserver();
    final ComputedValue<?> computedValue = observer.getComputedValue();
    final ObservableValue<?> derivedValue = computedValue.getObservableValue();
    setCurrentTransaction( observer );
    observer.setState( Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( null );

    assertFalse( observer.isDisposed() );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    Arez.context().getSpy().addSpyEventHandler( handler );

    observer.dispose();

    assertTrue( observer.isDisposed() );
    assertTrue( derivedValue.isDisposed() );
    assertTrue( computedValue.isDisposed() );

    handler.assertEventCount( 14 );

    // This is the part that disposes the Observer
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class );

    // This is the part that disposes the associated ComputedValue
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class );
    handler.assertNextEvent( ComputedValueDisposedEvent.class );

    // This is the part that disposes the associated ObservableValue
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( ObservableValueChangedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class );
  }

  @Test
  public void dispose_via_ComputedValue()
  {
    setIgnoreObserverErrors( true );

    final AtomicBoolean hasErrorOccurred = new AtomicBoolean();
    hasErrorOccurred.set( false );
    Arez.context().addObserverErrorHandler( ( observer, error, throwable ) -> {
      hasErrorOccurred.set( true );
      fail();
    } );
    final Observer observer = Arez.context().computed( () -> "" ).getObserver();
    final ComputedValue<?> computedValue = observer.getComputedValue();
    setCurrentTransaction( observer );
    observer.setState( Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( null );

    assertFalse( observer.isDisposed() );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    Arez.context().getSpy().addSpyEventHandler( handler );

    computedValue.dispose();

    assertTrue( observer.isDisposed() );
    assertTrue( computedValue.isDisposed() );

    handler.assertEventCount( 14 );

    // This is the part that disposes the associated ComputedValue
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class );
    handler.assertNextEvent( ComputedValueDisposedEvent.class );

    // This is the part that disposes the associated ObservableValue
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( ObservableValueChangedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class );

    // This is the part that disposes the Observer
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class );

    // Ensure no observer errors occur
    assertFalse( hasErrorOccurred.get() );
  }

  @Test
  public void getComputedValue_throwsExceptionWhenNotDerived()
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );

    assertFalse( observer.isComputedValue() );

    assertInvariantFailure( observer::getComputedValue,
                            "Arez-0095: Attempted to invoke getComputedValue on observer named '" + observer.getName() +
                            "' when is not a computed observer." );
  }

  @Test
  public void getComputedValue_whenDisposed()
  {
    final Observer observer = Arez.context().computed( () -> "" ).getObserver();

    observer.markAsDisposed();

    // Should be able to do this because sometimes when we dispose ComputedValue it gets deactivated and
    // part of dispose of observer needs to access ComputedValue to send out a spy message
    assertEquals( observer.getComputedValue().getName(), observer.getName() );
  }

  @Test
  public void markDependenciesLeastStaleObserverAsUpToDate()
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );

    final ObservableValue<?> observableValue1 = Arez.context().observable();
    final ObservableValue<?> observableValue2 = Arez.context().observable();
    final ObservableValue<?> observableValue3 = Arez.context().observable();

    observer.getDependencies().add( observableValue1 );
    observer.getDependencies().add( observableValue2 );
    observer.getDependencies().add( observableValue3 );

    setCurrentTransaction( observer );

    observableValue1.addObserver( observer );
    observableValue2.addObserver( observer );
    observableValue3.addObserver( observer );

    observableValue1.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );
    observableValue2.setLeastStaleObserverState( Flags.STATE_POSSIBLY_STALE );
    observableValue3.setLeastStaleObserverState( Flags.STATE_STALE );

    observer.markDependenciesLeastStaleObserverAsUpToDate();

    assertEquals( observableValue1.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observableValue2.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observableValue3.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void invokeReaction()
  {
    setIgnoreObserverErrors( true );

    final ArezContext context = Arez.context();

    final AtomicInteger errorCount = new AtomicInteger();

    context.addObserverErrorHandler( ( observer, error, throwable ) -> errorCount.incrementAndGet() );

    final CountAndObserveProcedure observed = new CountAndObserveProcedure();

    final Observer observer = new Observer( context, null, ValueUtil.randomString(), observed, null, 0 );

    observer.invokeReaction();

    assertEquals( observed.getCallCount(), 1 );
    assertEquals( errorCount.get(), 0 );
  }

  @Test
  public void invokeReaction_Observer_SpyEventHandlerPresent()
  {
    final TestSpyEventHandler handler = new TestSpyEventHandler();
    final ArezContext context = Arez.context();

    final ObservableValue<Object> observable = context.observable();

    final CountingProcedure observed = new CountingProcedure()
    {
      @Override
      public void call()
        throws Throwable
      {
        observable.reportObserved();
        super.call();
        Thread.sleep( 1 );
      }
    };
    context.getSpy().addSpyEventHandler( handler );

    final Observer observer =
      new Observer( context,
                    null,
                    ValueUtil.randomString(),
                    observed,
                    null,
                    0 );

    context.triggerScheduler();

    handler.assertEventCount( 8 );

    handler.assertNextEvent( ObserverCreatedEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
    handler.assertNextEvent( ReactionScheduledEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
    handler.assertNextEvent( ReactionStartedEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
    handler.assertNextEvent( ActionStartedEvent.class, e -> assertEquals( e.getName(), observer.getName() ) );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> assertEquals( e.getName(), observer.getName() ) );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> assertEquals( e.getName(), observer.getName() ) );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> assertEquals( e.getName(), observer.getName() ) );
    handler.assertNextEvent( ReactionCompletedEvent.class, e -> {
      assertEquals( e.getObserver().getName(), observer.getName() );
      assertTrue( e.getDuration() > 0 );
    } );
  }

  @SuppressWarnings( "ConstantConditions" )
  @Test
  public void invokeReaction_ComputedValue_SpyEventHandlerPresent()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observableValue = context.observable();
    final TestSpyEventHandler handler = new TestSpyEventHandler();

    final SafeFunction<Integer> function = () -> {
      observableValue.reportObserved();
      return 1;
    };
    final ComputedValue<Integer> computedValue = context.computed( function );

    context.getSpy().addSpyEventHandler( handler );

    computedValue.getObserver().invokeReaction();

    handler.assertEventCount( 6 );

    handler.assertNextEvent( ComputeStartedEvent.class,
                             e -> assertEquals( e.getComputedValue().getName(), computedValue.getName() ) );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( ObservableValueChangedEvent.class,
                             e -> assertEquals( e.getObservableValue().getName(),
                                                computedValue.getObservableValue().getName() ) );
    handler.assertNextEvent( ComputedValueDeactivatedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ComputeCompletedEvent.class, event -> {
      assertEquals( event.getComputedValue().getName(), computedValue.getName() );
      assertTrue( event.getDuration() >= 0 );
    } );
  }

  @Test
  public void invokeReaction_onDisposedObserver()
  {
    final CountingProcedure observed = new CountAndObserveProcedure();
    final Observer observer =
      new Observer( Arez.context(),
                    null,
                    ValueUtil.randomString(),
                    observed,
                    null,
                    0 );

    observer.invokeReaction();

    assertEquals( observed.getCallCount(), 1 );

    observer.markAsDisposed();

    observer.invokeReaction();

    assertEquals( observed.getCallCount(), 1 );
  }

  @Test
  public void invokeReaction_onUpToDateObserver()
  {
    final CountAndObserveProcedure observed = new CountAndObserveProcedure();
    final ArezContext context = Arez.context();
    final Observer observer =
      new Observer( context,
                    null,
                    ValueUtil.randomString(),
                    observed,
                    null,
                    0 );

    context.triggerScheduler();

    assertEquals( observed.getCallCount(), 1 );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    //Invoke reaction - observer is uptodate
    observer.invokeReaction();

    assertEquals( observed.getCallCount(), 1 );
  }

  @Test
  public void invokeReaction_onUpToDateObserver_onDepsChanged_Present()
  {
    final CountAndObserveProcedure observed = new CountAndObserveProcedure();
    final CountingProcedure onDepsChanged = new CountingProcedure();
    final ArezContext context = Arez.context();
    final Observer observer =
      new Observer( context,
                    null,
                    ValueUtil.randomString(),
                    observed,
                    onDepsChanged,
                    0 );

    context.triggerScheduler();

    assertEquals( observed.getCallCount(), 1 );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    observer.executeObservedNextIfPresent();

    assertTrue( observer.shouldExecuteObservedNext() );

    //Invoke reaction - observer is uptodate
    observer.invokeReaction();

    assertFalse( observer.shouldExecuteObservedNext() );
    assertEquals( observed.getCallCount(), 1 );
    assertEquals( onDepsChanged.getCallCount(), 0 );
  }

  @Test
  public void invokeReaction_reactionGeneratesError()
  {
    setIgnoreObserverErrors( true );

    final AtomicInteger errorCount = new AtomicInteger();

    final RuntimeException exception = new RuntimeException( "X" );

    Arez.context().addObserverErrorHandler( ( observer, error, throwable ) -> {
      errorCount.incrementAndGet();
      assertEquals( error, ObserverError.REACTION_ERROR );
      assertEquals( throwable, exception );
    } );

    final CountingProcedure observed = new CountingProcedure()
    {
      @Override
      public void call()
        throws Throwable
      {
        super.call();
        throw exception;
      }
    };

    final Observer observer =
      new Observer( Arez.context(),
                    null,
                    ValueUtil.randomString(),
                    observed,
                    null,
                    0 );

    observer.invokeReaction();

    assertEquals( observed.getCallCount(), 1 );
    assertEquals( errorCount.get(), 1 );
  }

  @Test
  public void shouldCompute_DISPOSING()
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_DISPOSING );

    assertInvariantFailure( observer::shouldCompute,
                            "Arez-0205: Observer.shouldCompute() invoked on observer named 'Observer@1' but observer is in state DISPOSING" );
  }

  @Test
  public void shouldCompute_UP_TO_DATE()
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_UP_TO_DATE );

    assertFalse( observer.shouldCompute() );
  }

  @Test
  public void shouldCompute_INACTIVE()
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_INACTIVE );

    assertTrue( observer.shouldCompute() );
  }

  @Test
  public void shouldCompute_STALE()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );

    context.safeAction( () -> {
      observer.setState( Flags.STATE_STALE );
      assertTrue( observer.shouldCompute() );
    }, Flags.NO_VERIFY_ACTION_REQUIRED );
  }

  @Test
  public void shouldCompute_POSSIBLY_STALE()
  {
    final ArezContext context = Arez.context();
    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final ComputedValue<String> computedValue = context.computed( function );

    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    final SafeFunction<String> function2 = () -> {
      observeADependency();
      return "";
    };
    final ComputedValue<String> computedValue2 = context.computed( function2 );

    observer.getDependencies().add( computedValue2.getObservableValue() );
    computedValue2.getObservableValue().addObserver( observer );

    // Set it to something random so it will change
    computedValue2.setValue( ValueUtil.randomString() );

    assertTrue( observer.shouldCompute() );

    assertEquals( observer.getState(), Flags.STATE_STALE );
  }

  @Test
  public void shouldCompute_POSSIBLY_STALE_ComputableThrowsException()
  {
    setIgnoreObserverErrors( true );

    final ArezContext context = Arez.context();
    final SafeFunction<String> function1 = () -> {
      observeADependency();
      return ValueUtil.randomString();
    };
    final ComputedValue<String> computedValue = context.computed( function1 );
    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    final SafeFunction<String> function = () -> {
      observeADependency();
      throw new IllegalStateException();
    };
    final ComputedValue<String> computedValue2 = context.computed( function );

    observer.getDependencies().add( computedValue2.getObservableValue() );
    computedValue2.getObservableValue().addObserver( observer );

    // Set it to something random so it will change
    computedValue2.setValue( ValueUtil.randomString() );

    assertTrue( observer.shouldCompute() );

    assertEquals( observer.getState(), Flags.STATE_STALE );
  }

  @Test
  public void shouldCompute_POSSIBLY_STALE_ComputableReThrowsException()
  {
    setIgnoreObserverErrors( true );

    final SafeFunction<String> function1 = () -> {
      observeADependency();
      return ValueUtil.randomString();
    };
    final ArezContext context = Arez.context();
    final ComputedValue<String> computedValue = context.computed( function1 );
    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    final SafeFunction<String> function = () -> {
      observeADependency();
      throw new IllegalStateException();
    };
    final ComputedValue<String> computedValue2 = context.computed( function );

    observer.getDependencies().add( computedValue2.getObservableValue() );
    computedValue2.getObservableValue().addObserver( observer );

    // Set it as state error so should not trigger a change in container
    computedValue2.setError( new IllegalStateException() );

    assertFalse( observer.shouldCompute() );

    assertEquals( observer.getState(), Flags.STATE_POSSIBLY_STALE );
  }

  @Test
  public void shouldCompute_POSSIBLY_STALE_whereDependencyRecomputesButDoesNotChange()
  {
    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final ArezContext context = Arez.context();
    final ComputedValue<String> computedValue = context.computed( function );

    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    final ComputedValue<String> computedValue2 = context.computed( function );

    observer.getDependencies().add( computedValue2.getObservableValue() );
    computedValue2.getObservableValue().addObserver( observer );

    // Set it to same so no change
    computedValue2.setValue( "" );

    assertFalse( observer.shouldCompute() );

    assertEquals( computedValue2.getObserver().getState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void shouldCompute_POSSIBLY_STALE_ignoresNonComputedDependencies()
  {
    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final ArezContext context = Arez.context();
    final ComputedValue<String> computedValue = context.computed( function );

    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    final ObservableValue<?> observableValue = context.observable();
    observer.getDependencies().add( observableValue );
    observableValue.addObserver( observer );

    assertFalse( observer.shouldCompute() );
  }

  @Test
  public void asInfo()
  {
    final Observer observer = Arez.context().tracker( ValueUtil::randomString );

    final ObserverInfo info = observer.asInfo();
    assertEquals( info.getName(), observer.getName() );
  }

  @Test
  public void asInfo_spyDisabled()
  {
    ArezTestUtil.disableSpies();
    ArezTestUtil.resetState();

    final Observer observer = Arez.context().tracker( ValueUtil::randomString );

    assertInvariantFailure( observer::asInfo,
                            "Arez-0197: Observer.asInfo() invoked but Arez.areSpiesEnabled() returned false." );
  }

  @Test
  public void reportStale()
  {
    final ArezContext context = Arez.context();

    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            new CountingProcedure(),
                                            new CountingProcedure(),
                                            Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );
    context.triggerScheduler();

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertFalse( observer.isScheduled() );

    context.safeAction( () -> {
      observer.reportStale();

      assertTrue( observer.isScheduled() );
      assertEquals( observer.getState(), Flags.STATE_STALE );
      assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
      assertTrue( context.getScheduler().getPendingObservers().contains( observer ) );
    } );
  }

  @Test
  public void reportStale_arezOnlyDependencies()
  {
    final ArezContext context = Arez.context();

    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            new CountAndObserveProcedure(),
                                            null,
                                            0 );
    context.triggerScheduler();

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertFalse( observer.isScheduled() );

    context.safeAction( () -> {
      assertInvariantFailure( observer::reportStale,
                              "Arez-0199: Observer.reportStale() invoked on observer named '" + observer.getName() +
                              "' but the observer has not specified AREZ_OR_EXTERNAL_DEPENDENCIES flag." );
      assertFalse( observer.isScheduled() );
      assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
      assertEquals( context.getScheduler().getPendingObservers().size(), 0 );
    }, Flags.NO_VERIFY_ACTION_REQUIRED );
  }

  @Test
  public void reportStale_noTransaction()
  {
    final ArezContext context = Arez.context();

    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            new CountingProcedure(),
                                            new CountingProcedure(),
                                            Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    context.triggerScheduler();

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertFalse( observer.isScheduled() );

    assertInvariantFailure( observer::reportStale,
                            "Arez-0200: Observer.reportStale() invoked on observer named '" + observer.getName() +
                            "' when there is no active transaction." );
    assertFalse( observer.isScheduled() );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );
  }

  @Test
  public void reportStale_readOnlyTransaction()
  {
    final ArezContext context = Arez.context();

    final Observer observer = context.observer( new CountingProcedure(), Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    context.safeAction( () -> observer.setState( Flags.STATE_UP_TO_DATE ), Flags.NO_VERIFY_ACTION_REQUIRED );
    assertFalse( observer.isScheduled() );

    assertInvariantFailure( () -> context.safeAction( "MyAction", observer::reportStale, Flags.READ_ONLY ),
                            "Arez-0201: Observer.reportStale() invoked on observer named '" + observer.getName() +
                            "' when the active transaction 'MyAction' is READ_ONLY rather " +
                            "than READ_WRITE." );
    assertFalse( observer.isScheduled() );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );
  }

  @Test
  public void schedule()
  {
    final ArezContext context = Arez.context();

    final CountAndObserveProcedure observed = new CountAndObserveProcedure();
    final CountingProcedure onDepsChanged = new CountingProcedure();
    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            observed,
                                            onDepsChanged,
                                            Flags.RUN_LATER );

    context.safeAction( () -> {
      observer.setState( Flags.STATE_STALE );

      // reset the scheduling that occurred due to setState
      observer.clearScheduledFlag();
      context.getScheduler().getPendingObservers().clear();
    }, Flags.NO_VERIFY_ACTION_REQUIRED );

    assertFalse( observer.isScheduled() );
    assertEquals( observed.getCallCount(), 0 );
    assertEquals( onDepsChanged.getCallCount(), 0 );

    final Disposable schedulerLock = context.pauseScheduler();

    observer.schedule();

    assertTrue( observer.isScheduled() );

    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
    assertTrue( context.getScheduler().getPendingObservers().contains( observer ) );

    schedulerLock.dispose();

    // reaction not executed as state was still UP_TO_DATE
    assertFalse( observer.isScheduled() );
    assertEquals( observed.getCallCount(), 1 );
    assertEquals( onDepsChanged.getCallCount(), 0 );
  }

  @Test
  public void schedule_in_transactionMarksTransactionAsUsed()
  {
    final ArezContext context = Arez.context();

    final CountAndObserveProcedure observed = new CountAndObserveProcedure();
    final CountingProcedure onDepsChanged = new CountingProcedure();
    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            observed,
                                            onDepsChanged,
                                            Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    assertFalse( observer.isScheduled() );
    assertEquals( observed.getCallCount(), 1 );
    assertEquals( onDepsChanged.getCallCount(), 0 );

    context.safeAction( observer::reportStale );

    assertFalse( observer.isScheduled() );
    assertEquals( observed.getCallCount(), 1 );
    assertEquals( onDepsChanged.getCallCount(), 1 );

    final Disposable schedulerLock = context.pauseScheduler();

    // This does not cause exception - thus transaction must be marked as used
    context.safeAction( observer::schedule );

    assertTrue( observer.isScheduled() );

    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
    assertTrue( context.getScheduler().getPendingObservers().contains( observer ) );

    schedulerLock.dispose();

    assertFalse( observer.isScheduled() );
    assertEquals( observed.getCallCount(), 2 );
    assertEquals( onDepsChanged.getCallCount(), 1 );
  }

  @Test
  public void schedule_but_state_UP_TO_DATE()
  {
    final ArezContext context = Arez.context();

    final CountAndObserveProcedure observed = new CountAndObserveProcedure();
    final CountingProcedure onDepsChanged = new CountingProcedure();
    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            observed,
                                            onDepsChanged,
                                            0 );
    context.triggerScheduler();

    assertEquals( observed.getCallCount(), 1 );
    assertEquals( onDepsChanged.getCallCount(), 0 );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertFalse( observer.isScheduled() );

    final Disposable schedulerLock = context.pauseScheduler();

    observer.schedule();

    assertTrue( observer.isScheduled() );

    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
    assertTrue( context.getScheduler().getPendingObservers().contains( observer ) );

    schedulerLock.dispose();

    assertFalse( observer.isScheduled() );
    assertEquals( observed.getCallCount(), 1 );
    assertEquals( onDepsChanged.getCallCount(), 0 );
  }

  @Test
  public void schedule_onNonManualScheduleObserver()
  {
    final ArezContext context = Arez.context();

    final CountAndObserveProcedure observed = new CountAndObserveProcedure();
    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            observed,
                                            null,
                                            Flags.RUN_LATER );
    context.safeAction( () -> {
      observer.setState( Flags.STATE_STALE );

      // reset the scheduling that occurred due to setState
      observer.clearScheduledFlag();
      context.getScheduler().getPendingObservers().clear();
    }, Flags.NO_VERIFY_ACTION_REQUIRED );

    assertFalse( observer.isScheduled() );

    final Disposable schedulerLock = context.pauseScheduler();

    assertInvariantFailure( observer::schedule,
                            "Arez-0202: Observer.schedule() invoked on observer named '" + observer.getName() +
                            "' but supportsManualSchedule() returns false." );
    assertFalse( observer.isScheduled() );
    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );

    schedulerLock.dispose();

    assertEquals( observed.getCallCount(), 0 );
  }
}
