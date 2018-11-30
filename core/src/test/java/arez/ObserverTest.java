package arez;

import arez.spy.ActionCompleteEvent;
import arez.spy.ActionStartEvent;
import arez.spy.ComputableValueDisposeEvent;
import arez.spy.ComputeCompleteEvent;
import arez.spy.ComputeStartEvent;
import arez.spy.ObservableValueChangeEvent;
import arez.spy.ObserveCompleteEvent;
import arez.spy.ObserveScheduleEvent;
import arez.spy.ObserveStartEvent;
import arez.spy.ObserverCreateEvent;
import arez.spy.ObserverDisposeEvent;
import arez.spy.ObserverErrorEvent;
import arez.spy.ObserverInfo;
import arez.spy.Priority;
import arez.spy.TransactionCompleteEvent;
import arez.spy.TransactionStartEvent;
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
    final CountingProcedure onDepsChange = new CountingProcedure();
    final Observer observer = new Observer( context, null, name, observed, onDepsChange, Flags.RUN_LATER );

    // Verify all "Node" behaviour
    assertEquals( observer.getContext(), context );
    assertEquals( observer.getName(), name );
    assertEquals( observer.toString(), name );

    assertTrue( observer.shouldExecuteObserveNext() );

    assertTrue( observer.isEnvironmentRequired() );

    // Starts out inactive and inactive means no dependencies
    assertEquals( observer.getState(), Flags.STATE_INACTIVE );
    assertFalse( observer.isActive() );
    assertTrue( observer.isInactive() );
    assertEquals( observer.getDependencies().size(), 0 );

    // Reaction attributes
    assertFalse( observer.isMutation() );
    assertEquals( observer.getObserve(), observed );
    assertEquals( observer.getOnDepsChange(), onDepsChange );
    assertTrue( observer.getTask().isQueued() );

    assertFalse( observer.isComputableValue() );

    assertEquals( observer.getTask().getPriority(), Priority.NORMAL );
    assertFalse( observer.canObserveLowerPriorityDependencies() );

    observer.invariantState();

    assertEquals( context.getTopLevelObservers().get( observer.getName() ), observer );
  }

  @Test
  public void initialState_externalExecutor()
  {
    final CountingProcedure onDepsChange = new CountingProcedure();
    final Observer observer = new Observer( Arez.context(), null, ValueUtil.randomString(), null, onDepsChange, 0 );

    assertFalse( observer.shouldExecuteObserveNext() );

    assertFalse( observer.isEnvironmentRequired() );

    assertNull( observer.getObserve() );
    assertEquals( observer.getOnDepsChange(), onDepsChange );
    assertFalse( observer.isComputableValue() );
    assertTrue( observer.isApplicationExecutor() );
  }

  @Test
  public void initialStateForComputableValueObserver()
  {
    final ComputableValue<String> computableValue = Arez.context().computable( () -> "" );
    final Observer observer = computableValue.getObserver();

    assertTrue( observer.isComputableValue() );
    assertTrue( observer.shouldExecuteObserveNext() );

    assertFalse( observer.isEnvironmentRequired() );

    assertEquals( computableValue.getObservableValue().getName(), observer.getName() );
    assertEquals( computableValue.getObservableValue().getObserver(), observer );

    assertEquals( computableValue.getName(), observer.getName() );
    assertEquals( computableValue.getObserver(), observer );
  }

  @Test
  public void construct_with_no_observed_and_no_onDepsChange_parameter()
  {
    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> new Observer( Arez.context(),
                                                null,
                                                name,
                                                null,
                                                null,
                                                0 ),
                            "Arez-0204: Observer named '" + name + "' has not supplied a value for either " +
                            "the observe parameter or the onDepsChange parameter." );
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
                            "flag but the observe function is null." );
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
    assertInvariantFailure( () -> new Observer( Arez.context(),
                                                null,
                                                "MyObserver",
                                                new CountAndObserveProcedure(),
                                                new CountingProcedure(),
                                                Flags.REQUIRE_NEW_TRANSACTION ),
                            "Arez-0207: Observer named 'MyObserver' specified illegal flags: " +
                            Flags.REQUIRE_NEW_TRANSACTION );
  }

  @Test
  public void construct_with_duplicate_ENVIRONMENT_flags()
  {
    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> Arez.context().observer( name,
                                                           new CountAndObserveProcedure(),
                                                           new CountingProcedure(),
                                                           Flags.ENVIRONMENT_REQUIRED |
                                                           Flags.ENVIRONMENT_NOT_REQUIRED ),
                            "Arez-0121: Observer named '" + name + "' incorrectly specified both " +
                            "the ENVIRONMENT_REQUIRED flag and the ENVIRONMENT_NOT_REQUIRED flag." );
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
    final Observer observer = Arez.context().computable( () -> "" ).getObserver();
    final ComputableValue<?> computableValue = observer.getComputableValue();

    observer.invariantState();

    setField( computableValue, "_observableValue", Arez.context().observable() );

    assertInvariantFailure( observer::invariantState,
                            "Arez-0093: Observer named '" + observer.getName() + "' is associated with an " +
                            "ObservableValue that does not link back to observer." );
  }

  @Test
  public void invariantDerivationState()
  {
    final ArezContext context = Arez.context();
    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();

    observer.invariantComputableValueObserverState();

    context.safeAction( () -> observer.setState( Flags.STATE_UP_TO_DATE ), Flags.NO_VERIFY_ACTION_REQUIRED );

    assertInvariantFailure( () -> context.safeAction( observer::invariantComputableValueObserverState ),
                            "Arez-0094: Observer named '" + observer.getName() + "' is a ComputableValue and " +
                            "active but the associated ObservableValue has no observers." );

    context.observer( () -> computableValue.getObservableValue().reportObserved() );

    observer.invariantComputableValueObserverState();
  }

  @Test
  public void invariantDerivationState_observerCanHaveNoDependenciesIfItIsTracker()
  {
    final Observer observer = Arez.context().computable( () -> "" ).getObserver();

    observer.invariantComputableValueObserverState();

    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_UP_TO_DATE );

    observer.invariantComputableValueObserverState();
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
    assertFalse( observer.getTask().isQueued() );

    observer.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertFalse( observer.getTask().isQueued() );

    observer.setState( Flags.STATE_STALE );
    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertTrue( observer.getTask().isQueued() );

    observer.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    observer.setState( Flags.STATE_STALE );

    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertTrue( observer.getTask().isQueued() );

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

    final Observer observer = Arez.context().computable( () -> "" ).getObserver();

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );

    observer.setState( Flags.STATE_INACTIVE, false );

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );
    assertFalse( observer.getTask().isQueued() );

    observer.setState( Flags.STATE_UP_TO_DATE, false );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertFalse( observer.getTask().isQueued() );

    observer.setState( Flags.STATE_POSSIBLY_STALE, false );

    assertEquals( observer.getState(), Flags.STATE_POSSIBLY_STALE );
    assertFalse( observer.getTask().isQueued() );

    observer.setState( Flags.STATE_STALE, false );
    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertFalse( observer.getTask().isQueued() );

    observer.setState( Flags.STATE_UP_TO_DATE, false );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    observer.setState( Flags.STATE_STALE, false );

    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertFalse( observer.getTask().isQueued() );

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
  public void setState_onComputableValue()
  {
    final TestProcedure onActivate = new TestProcedure();
    final TestProcedure onDeactivate = new TestProcedure();
    final TestProcedure onStale = new TestProcedure();

    final ComputableValue<String> computableValue =
      Arez.context().computable( null, null, () -> "", onActivate, onDeactivate, onStale );
    final ObservableValue<String> derivedValue = computableValue.getObservableValue();
    final Observer observer = computableValue.getObserver();

    final Observer watcher = observer.getContext().observer( new CountAndObserveProcedure() );

    setupReadWriteTransaction();

    watcher.setState( Flags.STATE_UP_TO_DATE );
    observer.getComputableValue().getObservableValue().rawAddObserver( watcher );
    watcher.getDependencies().add( observer.getComputableValue().getObservableValue() );

    watcher.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );

    observer.setState( Flags.STATE_INACTIVE );

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );
    assertFalse( observer.getTask().isQueued() );
    assertEquals( onActivate.getCalls(), 0 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 0 );

    computableValue.setValue( ValueUtil.randomString() );
    observer.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertFalse( observer.getTask().isQueued() );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 0 );
    assertNotNull( computableValue.getValue() );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    assertEquals( observer.getState(), Flags.STATE_POSSIBLY_STALE );
    assertTrue( observer.getTask().isQueued() );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 1 );
    assertEquals( watcher.getState(), Flags.STATE_POSSIBLY_STALE );
    assertEquals( derivedValue.getLeastStaleObserverState(), Flags.STATE_POSSIBLY_STALE );
    assertNotNull( computableValue.getValue() );

    Arez.context().getTaskQueue().clear();
    assertFalse( observer.getTask().isQueued() );

    observer.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 1 );
    assertNotNull( computableValue.getValue() );

    watcher.setState( Flags.STATE_UP_TO_DATE );
    derivedValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    observer.setState( Flags.STATE_STALE );

    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertTrue( observer.getTask().isQueued() );
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
    assertNull( computableValue.getValue() );
  }

  @Test
  public void setState_activateWhenDisposed()
  {
    final Observer observer = Arez.context().computable( () -> "" ).getObserver();
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

    assertFalse( observer.getTask().isQueued() );

    observer.scheduleReaction();

    final ArezContext context = Arez.context();

    assertTrue( observer.getTask().isQueued() );
    assertEquals( context.getTaskQueue().getOrderedTasks().count(), 1L );
    assertTrue( context.getTaskQueue().getOrderedTasks().anyMatch( o -> o == observer.getTask() ) );

    //Duplicate schedule should not result in it being added again
    observer.scheduleReaction();

    assertTrue( observer.getTask().isQueued() );
    assertEquals( context.getTaskQueue().getOrderedTasks().count(), 1L );
    assertTrue( context.getTaskQueue().getOrderedTasks().anyMatch( o -> o == observer.getTask() ) );
  }

  @Test
  public void scheduleReaction_when_Disposed()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );

    context.safeAction( () -> {
      observer.setState( Flags.STATE_INACTIVE );
      observer.setState( Flags.STATE_DISPOSED );

      assertFalse( observer.getTask().isQueued() );

      observer.scheduleReaction();

      assertFalse( observer.getTask().isQueued() );
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
    assertFalse( observer.getTask().isDisposed() );

    observer.dispose();

    assertEquals( observer.getState(), Flags.STATE_DISPOSED );
    assertFalse( observer.isNotDisposed() );
    assertTrue( observer.isDisposed() );
    assertTrue( observer.getTask().isDisposed() );

    final ArezContext context = Arez.context();

    final int currentNextTransactionId = context.currentNextTransactionId();

    observer.dispose();

    // This verifies no new transactions were created
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId );
  }

  @Test
  public void dispose_onComputableValue()
  {
    final Observer observer = Arez.context().computable( () -> "" ).getObserver();
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
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );
    handler.assertNextEvent( ObserverDisposeEvent.class,
                             event -> assertEquals( event.getObserver().getName(), observer.getName() ) );
  }

  @Test
  public void dispose_does_not_generate_spyEvent_forDerivedValues()
  {
    final Observer observer = Arez.context().computable( () -> "" ).getObserver();
    final ComputableValue<?> computableValue = observer.getComputableValue();
    final ObservableValue<?> derivedValue = computableValue.getObservableValue();
    setCurrentTransaction( observer );
    observer.setState( Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( null );

    assertFalse( observer.isDisposed() );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    Arez.context().getSpy().addSpyEventHandler( handler );

    observer.dispose();

    assertTrue( observer.isDisposed() );
    assertTrue( derivedValue.isDisposed() );
    assertTrue( computableValue.isDisposed() );

    handler.assertEventCount( 14 );

    // This is the part that disposes the Observer
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );

    // This is the part that disposes the associated ComputableValue
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );
    handler.assertNextEvent( ComputableValueDisposeEvent.class );

    // This is the part that disposes the associated ObservableValue
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ObservableValueChangeEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );
  }

  @Test
  public void dispose_via_ComputableValue()
  {
    setIgnoreObserverErrors( true );

    final AtomicBoolean hasErrorOccurred = new AtomicBoolean();
    hasErrorOccurred.set( false );
    Arez.context().addObserverErrorHandler( ( observer, error, throwable ) -> {
      hasErrorOccurred.set( true );
      fail();
    } );
    final Observer observer = Arez.context().computable( () -> "" ).getObserver();
    final ComputableValue<?> computableValue = observer.getComputableValue();
    setCurrentTransaction( observer );
    observer.setState( Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( null );

    assertFalse( observer.isDisposed() );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    Arez.context().getSpy().addSpyEventHandler( handler );

    computableValue.dispose();

    assertTrue( observer.isDisposed() );
    assertTrue( computableValue.isDisposed() );

    handler.assertEventCount( 14 );

    // This is the part that disposes the associated ComputableValue
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );
    handler.assertNextEvent( ComputableValueDisposeEvent.class );

    // This is the part that disposes the associated ObservableValue
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ObservableValueChangeEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );

    // This is the part that disposes the Observer
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );

    // Ensure no observer errors occur
    assertFalse( hasErrorOccurred.get() );
  }

  @Test
  public void getComputableValue_throwsExceptionWhenNotDerived()
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );

    assertFalse( observer.isComputableValue() );

    assertInvariantFailure( observer::getComputableValue,
                            "Arez-0095: Attempted to invoke getComputableValue on observer named '" +
                            observer.getName() +
                            "' when is not a computable observer." );
  }

  @Test
  public void getComputableValue_whenDisposed()
  {
    final Observer observer = Arez.context().computable( () -> "" ).getObserver();

    observer.markAsDisposed();

    // Should be able to do this because sometimes when we dispose ComputableValue it gets deactivated and
    // part of dispose of observer needs to access ComputableValue to send out a spy message
    assertEquals( observer.getComputableValue().getName(), observer.getName() );
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

    handler.assertNextEvent( ObserverCreateEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
    handler.assertNextEvent( ObserveScheduleEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
    handler.assertNextEvent( ObserveStartEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
    handler.assertNextEvent( ActionStartEvent.class, e -> assertEquals( e.getName(), observer.getName() ) );
    handler.assertNextEvent( TransactionStartEvent.class, e -> assertEquals( e.getName(), observer.getName() ) );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> assertEquals( e.getName(), observer.getName() ) );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> assertEquals( e.getName(), observer.getName() ) );
    handler.assertNextEvent( ObserveCompleteEvent.class, e -> {
      assertEquals( e.getObserver().getName(), observer.getName() );
      assertNull( e.getThrowable() );
      assertTrue( e.getDuration() > 0 );
    } );
  }

  @SuppressWarnings( "ConstantConditions" )
  @Test
  public void invokeReaction_ComputableValue_SpyEventHandlerPresent()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observableValue = context.observable();
    final TestSpyEventHandler handler = new TestSpyEventHandler();

    final SafeFunction<Integer> function = () -> {
      observableValue.reportObserved();
      return 1;
    };
    final ComputableValue<Integer> computableValue = context.computable( function );

    context.getSpy().addSpyEventHandler( handler );

    Arez.context().safeAction( () -> computableValue.getObserver().invokeReaction(), Flags.NO_VERIFY_ACTION_REQUIRED );

    handler.assertEventCount( 9 );

    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );

    handler.assertNextEvent( ComputeStartEvent.class,
                             e -> assertEquals( e.getComputableValue().getName(), computableValue.getName() ) );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ObservableValueChangeEvent.class,
                             e -> assertEquals( e.getObservableValue().getName(),
                                                computableValue.getObservableValue().getName() ) );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ComputeCompleteEvent.class, event -> {
      assertEquals( event.getComputableValue().getName(), computableValue.getName() );
      assertEquals( event.getResult(), 1 );
      assertNull( event.getThrowable() );
      assertTrue( event.getDuration() >= 0 );
    } );

    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );
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
  public void invokeReaction_onUpToDateObserver_onDepsChange_Present()
  {
    final CountAndObserveProcedure observed = new CountAndObserveProcedure();
    final CountingProcedure onDepsChange = new CountingProcedure();
    final ArezContext context = Arez.context();
    final Observer observer =
      new Observer( context,
                    null,
                    ValueUtil.randomString(),
                    observed,
                    onDepsChange,
                    0 );

    context.triggerScheduler();

    assertEquals( observed.getCallCount(), 1 );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    observer.executeObserveNextIfPresent();

    assertTrue( observer.shouldExecuteObserveNext() );

    //Invoke reaction - observer is uptodate
    observer.invokeReaction();

    assertFalse( observer.shouldExecuteObserveNext() );
    assertEquals( observed.getCallCount(), 1 );
    assertEquals( onDepsChange.getCallCount(), 0 );
  }

  @Test
  public void invokeReaction_reactionGeneratesError()
  {
    setIgnoreObserverErrors( true );

    final AtomicInteger errorCount = new AtomicInteger();

    final RuntimeException exception = new RuntimeException( "X" );

    final ArezContext context = Arez.context();

    context.addObserverErrorHandler( ( observer, error, throwable ) -> {
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

    final Observer observer = context.observer( observed, Flags.RUN_LATER );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    context.triggerScheduler();

    assertEquals( observed.getCallCount(), 1 );
    assertEquals( errorCount.get(), 1 );

    handler.assertEventCount( 7 );

    handler.assertNextEvent( ObserveStartEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );

    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );

    handler.assertNextEvent( ObserverErrorEvent.class, e -> {
      assertEquals( e.getObserver().getName(), observer.getName() );
      assertEquals( e.getError(), ObserverError.REACTION_ERROR );
      assertEquals( e.getThrowable(), exception );
    } );

    handler.assertNextEvent( ObserveCompleteEvent.class, e -> {
      assertEquals( e.getObserver().getName(), observer.getName() );
      assertEquals( e.getThrowable(), exception );
      assertTrue( e.getDuration() >= 0 );
    } );
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
    final ComputableValue<String> computableValue = context.computable( function );

    final Observer observer = computableValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    final SafeFunction<String> function2 = () -> {
      observeADependency();
      return "";
    };
    final ComputableValue<String> computableValue2 = context.computable( function2 );

    observer.getDependencies().add( computableValue2.getObservableValue() );
    computableValue2.getObservableValue().addObserver( observer );

    // Set it to something random so it will change
    computableValue2.setValue( ValueUtil.randomString() );

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
    final ComputableValue<String> computableValue = context.computable( function1 );
    final Observer observer = computableValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    final SafeFunction<String> function = () -> {
      observeADependency();
      throw new IllegalStateException();
    };
    final ComputableValue<String> computableValue2 = context.computable( function );

    observer.getDependencies().add( computableValue2.getObservableValue() );
    computableValue2.getObservableValue().addObserver( observer );

    // Set it to something random so it will change
    computableValue2.setValue( ValueUtil.randomString() );

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
    final ComputableValue<String> computableValue = context.computable( function1 );
    final Observer observer = computableValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    final SafeFunction<String> function = () -> {
      observeADependency();
      throw new IllegalStateException();
    };
    final ComputableValue<String> computableValue2 = context.computable( function );

    observer.getDependencies().add( computableValue2.getObservableValue() );
    computableValue2.getObservableValue().addObserver( observer );

    // Set it as state error so should not trigger a change in container
    computableValue2.setError( new IllegalStateException() );

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
    final ComputableValue<String> computableValue = context.computable( function );

    final Observer observer = computableValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    final ComputableValue<String> computableValue2 = context.computable( function );

    observer.getDependencies().add( computableValue2.getObservableValue() );
    computableValue2.getObservableValue().addObserver( observer );

    // Set it to same so no change
    computableValue2.setValue( "" );

    assertFalse( observer.shouldCompute() );

    assertEquals( computableValue2.getObserver().getState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void shouldCompute_POSSIBLY_STALE_ignoresNonComputableValueDependencies()
  {
    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final ArezContext context = Arez.context();
    final ComputableValue<String> computableValue = context.computable( function );

    final Observer observer = computableValue.getObserver();
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
    assertFalse( observer.getTask().isQueued() );

    context.safeAction( () -> {
      observer.reportStale();

      assertTrue( observer.getTask().isQueued() );
      assertEquals( observer.getState(), Flags.STATE_STALE );
      assertEquals( context.getTaskQueue().getOrderedTasks().count(), 1L );
      assertTrue( context.getTaskQueue().getOrderedTasks().anyMatch( o -> o == observer.getTask() ) );
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
    assertFalse( observer.getTask().isQueued() );

    context.safeAction( () -> {
      assertInvariantFailure( observer::reportStale,
                              "Arez-0199: Observer.reportStale() invoked on observer named '" + observer.getName() +
                              "' but the observer has not specified AREZ_OR_EXTERNAL_DEPENDENCIES flag." );
      assertFalse( observer.getTask().isQueued() );
      assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
      assertEquals( context.getTaskQueue().getOrderedTasks().count(), 0L );
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
    assertFalse( observer.getTask().isQueued() );

    assertInvariantFailure( observer::reportStale,
                            "Arez-0200: Observer.reportStale() invoked on observer named '" + observer.getName() +
                            "' when there is no active transaction." );
    assertFalse( observer.getTask().isQueued() );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( context.getTaskQueue().getOrderedTasks().count(), 0L );
  }

  @Test
  public void reportStale_readOnlyTransaction()
  {
    final ArezContext context = Arez.context();

    final Observer observer = context.observer( new CountingProcedure(), Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    context.safeAction( () -> observer.setState( Flags.STATE_UP_TO_DATE ), Flags.NO_VERIFY_ACTION_REQUIRED );
    assertFalse( observer.getTask().isQueued() );

    assertInvariantFailure( () -> context.safeAction( "MyAction", observer::reportStale, Flags.READ_ONLY ),
                            "Arez-0201: Observer.reportStale() invoked on observer named '" + observer.getName() +
                            "' when the active transaction 'MyAction' is READ_ONLY rather " +
                            "than READ_WRITE." );
    assertFalse( observer.getTask().isQueued() );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( context.getTaskQueue().getOrderedTasks().count(), 0L );
  }

  @Test
  public void schedule()
  {
    final ArezContext context = Arez.context();

    final CountAndObserveProcedure observed = new CountAndObserveProcedure();
    final CountingProcedure onDepsChange = new CountingProcedure();
    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            observed,
                                            onDepsChange,
                                            Flags.RUN_LATER );

    context.safeAction( () -> {
      observer.setState( Flags.STATE_STALE );

      // reset the scheduling that occurred due to setState
      context.getTaskQueue().clear();
    }, Flags.NO_VERIFY_ACTION_REQUIRED );

    assertFalse( observer.getTask().isQueued() );
    assertEquals( observed.getCallCount(), 0 );
    assertEquals( onDepsChange.getCallCount(), 0 );

    final Disposable schedulerLock = context.pauseScheduler();

    observer.schedule();

    assertTrue( observer.getTask().isQueued() );

    assertEquals( context.getTaskQueue().getOrderedTasks().count(), 1L );
    assertTrue( context.getTaskQueue().getOrderedTasks().anyMatch( o -> o == observer.getTask() ) );

    schedulerLock.dispose();

    // reaction not executed as state was still UP_TO_DATE
    assertFalse( observer.getTask().isQueued() );
    assertEquals( observed.getCallCount(), 1 );
    assertEquals( onDepsChange.getCallCount(), 0 );
  }

  @Test
  public void schedule_in_transactionMarksTransactionAsUsed()
  {
    final ArezContext context = Arez.context();

    final CountAndObserveProcedure observed = new CountAndObserveProcedure();
    final CountingProcedure onDepsChange = new CountingProcedure();
    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            observed,
                                            onDepsChange,
                                            Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    assertFalse( observer.getTask().isQueued() );
    assertEquals( observed.getCallCount(), 1 );
    assertEquals( onDepsChange.getCallCount(), 0 );

    context.safeAction( observer::reportStale );

    assertFalse( observer.getTask().isQueued() );
    assertEquals( observed.getCallCount(), 1 );
    assertEquals( onDepsChange.getCallCount(), 1 );

    final Disposable schedulerLock = context.pauseScheduler();

    // This does not cause exception - thus transaction must be marked as used
    context.safeAction( observer::schedule );

    assertTrue( observer.getTask().isQueued() );

    assertEquals( context.getTaskQueue().getOrderedTasks().count(), 1L );
    assertTrue( context.getTaskQueue().getOrderedTasks().anyMatch( o -> o == observer.getTask() ) );

    schedulerLock.dispose();

    assertFalse( observer.getTask().isQueued() );
    assertEquals( observed.getCallCount(), 2 );
    assertEquals( onDepsChange.getCallCount(), 1 );
  }

  @Test
  public void schedule_but_state_UP_TO_DATE()
  {
    final ArezContext context = Arez.context();

    final CountAndObserveProcedure observed = new CountAndObserveProcedure();
    final CountingProcedure onDepsChange = new CountingProcedure();
    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            observed,
                                            onDepsChange,
                                            0 );
    context.triggerScheduler();

    assertEquals( observed.getCallCount(), 1 );
    assertEquals( onDepsChange.getCallCount(), 0 );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertFalse( observer.getTask().isQueued() );

    final Disposable schedulerLock = context.pauseScheduler();

    observer.schedule();

    assertTrue( observer.getTask().isQueued() );

    assertEquals( context.getTaskQueue().getOrderedTasks().count(), 1L );
    assertTrue( context.getTaskQueue().getOrderedTasks().anyMatch( o -> o == observer.getTask() ) );

    schedulerLock.dispose();

    assertFalse( observer.getTask().isQueued() );
    assertEquals( observed.getCallCount(), 1 );
    assertEquals( onDepsChange.getCallCount(), 0 );
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
      context.getTaskQueue().clear();
    }, Flags.NO_VERIFY_ACTION_REQUIRED );

    assertFalse( observer.getTask().isQueued() );

    final Disposable schedulerLock = context.pauseScheduler();

    assertInvariantFailure( observer::schedule,
                            "Arez-0202: Observer.schedule() invoked on observer named '" + observer.getName() +
                            "' but supportsManualSchedule() returns false." );
    assertFalse( observer.getTask().isQueued() );
    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertEquals( context.getTaskQueue().getOrderedTasks().count(), 0L );

    schedulerLock.dispose();

    assertEquals( observed.getCallCount(), 0 );
  }
}
