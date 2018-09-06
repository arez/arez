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
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final CountAndObserveProcedure trackedExecutable = new CountAndObserveProcedure();
    final CountingProcedure onDepsUpdated = new CountingProcedure();
    final Observer observer = new Observer( context, null, name, trackedExecutable, onDepsUpdated, Flags.DEFER_REACT );

    // Verify all "Node" behaviour
    assertEquals( observer.getContext(), context );
    assertEquals( observer.getName(), name );
    assertEquals( observer.toString(), name );

    assertEquals( observer.shouldExecuteTrackedNext(), true );

    // Starts out inactive and inactive means no dependencies
    assertEquals( observer.getState(), Flags.STATE_INACTIVE );
    assertEquals( observer.isActive(), false );
    assertEquals( observer.isInactive(), true );
    assertEquals( observer.getDependencies().size(), 0 );

    // Reaction attributes
    assertEquals( observer.getMode(), TransactionMode.READ_ONLY );
    assertEquals( observer.getTracked(), trackedExecutable );
    assertEquals( observer.getOnDepsUpdated(), onDepsUpdated );
    assertEquals( observer.isScheduled(), true );

    assertEquals( observer.isComputedValue(), false );

    assertEquals( observer.getPriority(), Priority.NORMAL );
    assertEquals( observer.canObserveLowerPriorityDependencies(), false );

    observer.invariantState();

    assertEquals( context.getTopLevelObservers().get( observer.getName() ), observer );
  }

  @Test
  public void initialState_externallyTracked()
    throws Exception
  {
    final CountingProcedure onDepsUpdated = new CountingProcedure();
    final Observer observer = new Observer( Arez.context(), null, ValueUtil.randomString(), null, onDepsUpdated, 0 );

    assertEquals( observer.shouldExecuteTrackedNext(), false );

    assertEquals( observer.getTracked(), null );
    assertEquals( observer.getOnDepsUpdated(), onDepsUpdated );
    assertEquals( observer.isComputedValue(), false );
  }

  @Test
  public void initialStateForComputedValueObserver()
    throws Exception
  {
    final ComputedValue<String> computedValue = Arez.context().computed( () -> "" );
    final Observer observer = computedValue.getObserver();

    assertEquals( observer.isComputedValue(), true );
    assertEquals( observer.shouldExecuteTrackedNext(), true );

    assertEquals( computedValue.getObservableValue().getName(), observer.getName() );
    assertEquals( computedValue.getObservableValue().getOwner(), observer );

    assertEquals( computedValue.getName(), observer.getName() );
    assertEquals( computedValue.getObserver(), observer );
  }

  @Test
  public void construct_with_no_tracked_and_no_onDepsUpdated_parameter()
    throws Exception
  {
    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( Arez.context(),
                                        null,
                                        name,
                                        null,
                                        null,
                                        0 ) );

    assertEquals( exception.getMessage(),
                  "Arez-0204: Observer named '" + name + "' has not supplied a value for either " +
                  "the tracked parameter or the onDepsUpdated parameter." );
  }

  @Test
  public void construct_with_invalid_priority()
    throws Exception
  {
    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( Arez.context(),
                                        null,
                                        name,
                                        new CountAndObserveProcedure(),
                                        null,
                                        Flags.PRIORITY_LOWEST + Flags.PRIORITY_HIGHEST ) );

    assertEquals( exception.getMessage(), "Arez-0080: Observer named '" + name + "' has invalid priority 5." );
  }

  @Test
  public void construct_with_OBSERVE_LOWER_PRIORITY_DEPENDENCIES_and_PRIORITY_LOWEST()
    throws Exception
  {
    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( Arez.context(),
                                        null,
                                        name,
                                        new CountingProcedure(),
                                        new CountingProcedure(),
                                        Flags.PRIORITY_LOWEST | Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES ) );

    assertEquals( exception.getMessage(),
                  "Arez-0184: Observer named '" + name + "' has LOWEST priority but has passed " +
                  "OBSERVE_LOWER_PRIORITY_DEPENDENCIES option which should not be present as the observer " +
                  "has no lower priority." );
  }

  @Test
  public void construct_with_READ_ONLY_and_READ_WRITE()
    throws Exception
  {
    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( Arez.context(),
                                        null,
                                        name,
                                        new CountAndObserveProcedure(),
                                        null,
                                        Flags.READ_ONLY | Flags.READ_WRITE ) );

    assertEquals( exception.getMessage(),
                  "Arez-0079: Observer named '" + name + "' incorrectly specified both " +
                  "READ_ONLY and READ_WRITE transaction mode flags." );
  }

  @Test
  public void construct_with_REACT_IMMEDIATELY_and_DEFER_REACT()
    throws Exception
  {
    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( Arez.context(),
                                        null,
                                        name,
                                        new CountAndObserveProcedure(),
                                        null,
                                        Flags.REACT_IMMEDIATELY | Flags.DEFER_REACT ) );

    assertEquals( exception.getMessage(),
                  "Arez-0081: Observer named '" + name + "' incorrectly specified both " +
                  "REACT_IMMEDIATELY and DEFER_REACT flags." );
  }

  @Test
  public void construct_with_REACT_IMMEDIATELY_and_no_tracked()
    throws Exception
  {
    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( Arez.context(),
                                        null,
                                        name,
                                        null,
                                        new CountingProcedure(),
                                        Flags.REACT_IMMEDIATELY ) );

    assertEquals( exception.getMessage(),
                  "Arez-0206: Observer named '" + name + "' incorrectly specified REACT_IMMEDIATELY " +
                  "flag but the tracked function is null." );
  }

  @Test
  public void construct_with_SCHEDULED_EXTERNALLY_and_no_tracked()
    throws Exception
  {
    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( Arez.context(),
                                        null,
                                        name,
                                        new CountAndObserveProcedure(),
                                        new CountingProcedure(),
                                        Flags.SCHEDULED_EXTERNALLY ) );

    assertEquals( exception.getMessage(),
                  "Arez-0207: Observer named '" + name + "' specified SCHEDULED_EXTERNALLY schedule type but " +
                  "the tracked function is non-null." );
  }

  @Test
  public void construct_with_computed_value_REACT_IMMEDIATELY_and_not_KEEPALIVE()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue = context.computed( () -> "" );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( computedValue, Flags.REACT_IMMEDIATELY ) );

    assertEquals( exception.getMessage(),
                  "Arez-0208: ComputedValue named 'ComputedValue@1' incorrectly specified REACT_IMMEDIATELY flag but not the KEEPALIVE flag." );
  }

  @Test
  public void construct_with_mode_but_checking_Disabled()
    throws Exception
  {
    ArezTestUtil.noEnforceTransactionType();

    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( Arez.context(),
                                        null,
                                        name,
                                        new CountingProcedure(),
                                        new CountingProcedure(),
                                        Flags.READ_ONLY ) );

    assertEquals( exception.getMessage(),
                  "Arez-0082: Observer named '" + name + "' specified transaction mode 'READ_ONLY' " +
                  "when Arez.enforceTransactionType() is false." );
  }

  @SuppressWarnings( "ResultOfMethodCallIgnored" )
  @Test
  public void getMode_noEnforceTransactionType()
    throws Exception
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
    assertThrows( observer::getMode );
  }

  @Test
  public void constructWithComponentWhenNativeComponentsDisabled()
    throws Exception
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
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( Arez.context(),
                                        component,
                                        name,
                                        new CountingProcedure(),
                                        new CountingProcedure(),
                                        0 ) );
    assertEquals( exception.getMessage(),
                  "Arez-0083: Observer named '" + name + "' has component specified but " +
                  "Arez.areNativeComponentsEnabled() is false." );
  }

  @Test
  public void basicLifecycle_withComponent()
    throws Exception
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
    throws Exception
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = Arez.context().observable();
    observer.getDependencies().add( observableValue );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observer.invariantDependenciesBackLink( "TEST1" ) );

    assertEquals( exception.getMessage(),
                  "Arez-0090: TEST1: Observer named '" + observer.getName() + "' has ObservableValue " +
                  "dependency named '" + observableValue.getName() + "' which does not contain the observer in " +
                  "the list of observers." );

    //Setup correct back link
    observableValue.addObserver( observer );

    // Back link created so should be good
    observer.invariantDependenciesBackLink( "TEST2" );
  }

  @Test
  public void invariantDependenciesNotDisposed()
    throws Exception
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = Arez.context().observable();
    observer.getDependencies().add( observableValue );
    observableValue.addObserver( observer );

    observableValue.setWorkState( ObservableValue.DISPOSED );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observer::invariantDependenciesNotDisposed );

    assertEquals( exception.getMessage(),
                  "Arez-0091: Observer named '" + observer.getName() + "' has ObservableValue dependency named '" +
                  observableValue.getName() + "' which is disposed." );

    observableValue.setWorkState( 0 );

    observer.invariantDependenciesNotDisposed();
  }

  @Test
  public void invariantDependenciesUnique()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.observer( observable::reportObserved );

    observer.invariantDependenciesUnique( "TEST1" );

    // Add a duplicate
    observer.getDependencies().add( observable );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observer.invariantDependenciesUnique( "TEST2" ) );

    assertEquals( exception.getMessage(),
                  "Arez-0089: TEST2: The set of dependencies in observer named '" + observer.getName() +
                  "' is not unique. Current list: '[" + observable.getName() + ", " + observable.getName() + "]'." );
  }

  @Test
  public void invariantState()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.observer( new CountingProcedure(), Flags.DEFER_REACT );

    observer.invariantState();

    observer.getDependencies().add( observable );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::invariantState );

    assertEquals( exception.getMessage(),
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

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observer::invariantState );

    assertEquals( exception.getMessage(),
                  "Arez-0093: Observer named '" + observer.getName() + "' is associated with an " +
                  "ObservableValue that does not link back to observer." );
  }

  @Test
  public void invariantDerivationState()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ComputedValue<String> computedValue = context.computed( () -> "" );
    final Observer observer = computedValue.getObserver();

    observer.invariantComputedValueObserverState();

    context.safeAction( null, false, false, () -> observer.setState( Flags.STATE_UP_TO_DATE ) );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.safeAction( observer::invariantComputedValueObserverState ) );

    assertEquals( exception.getMessage(),
                  "Arez-0094: Observer named '" + observer.getName() + "' is a ComputedValue and " +
                  "active but the associated ObservableValue has no observers." );

    context.observer( () -> computedValue.getObservableValue().reportObserved() );

    observer.invariantComputedValueObserverState();
  }

  @Test
  public void invariantDerivationState_observerCanHaveNoDependenciesIfItIsTracker()
    throws Exception
  {
    final Observer observer = Arez.context().computed( () -> "" ).getObserver();

    observer.invariantComputedValueObserverState();

    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_UP_TO_DATE );

    observer.invariantComputedValueObserverState();
  }

  @Test
  public void replaceDependencies()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.observer( new CountingProcedure(), Flags.NON_AREZ_DEPENDENCIES );

    final ArrayList<ObservableValue<?>> originalDependencies = observer.getDependencies();

    assertEquals( originalDependencies.isEmpty(), true );

    context.safeAction( null, false, false, () -> {

      final ArrayList<ObservableValue<?>> newDependencies = new ArrayList<>();
      newDependencies.add( observable );
      observable.rawAddObserver( observer );

      observer.replaceDependencies( newDependencies );

      assertEquals( observer.getDependencies().size(), 1 );
      assertTrue( observer.getDependencies() != originalDependencies );
      assertTrue( observer.getDependencies().contains( observable ) );
    } );
  }

  @Test
  public void replaceDependencies_duplicateDependency()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.observer( observable::reportObserved );

    final ArrayList<ObservableValue<?>> newDependencies = new ArrayList<>();
    newDependencies.add( observable );
    newDependencies.add( observable );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observer.replaceDependencies( newDependencies ) );
    assertEquals( exception.getMessage(),
                  "Arez-0089: Post replaceDependencies: The set of dependencies in observer named 'Observer@2' is not unique. Current list: '[ObservableValue@1, ObservableValue@1]'." );
  }

  @Test
  public void replaceDependencies_notBackLinkedDependency()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final ObservableValue<Object> observable2 = context.observable();
    final Observer observer = context.observer( observable::reportObserved );

    final ArrayList<ObservableValue<?>> newDependencies = new ArrayList<>();
    newDependencies.add( observable );
    newDependencies.add( observable2 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observer.replaceDependencies( newDependencies ) );
    assertEquals( exception.getMessage(),
                  "Arez-0090: Post replaceDependencies: Observer named 'Observer@3' has ObservableValue dependency named 'ObservableValue@2' which does not contain the observer in the list of observers." );
  }

  @Test
  public void clearDependencies()
    throws Exception
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

    context.safeAction( null, false, false, observer::clearDependencies );

    assertEquals( observer.getDependencies().size(), 0 );
    assertEquals( observableValue1.getObservers().size(), 0 );
    assertEquals( observableValue2.getObservers().size(), 0 );
  }

  @Test
  public void runHook_nullHook()
    throws Exception
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );

    observer.runHook( null, ObserverError.ON_ACTIVATE_ERROR );
  }

  @Test
  public void runHook_normalHook()
    throws Exception
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );

    final AtomicInteger counter = new AtomicInteger();

    observer.runHook( counter::incrementAndGet, ObserverError.ON_ACTIVATE_ERROR );

    assertEquals( counter.get(), 1 );
  }

  @Test
  public void runHook_hookThrowsException()
    throws Exception
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
    throws Exception
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );

    setupReadWriteTransaction();

    observer.setState( Flags.STATE_INACTIVE );

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );
    assertEquals( observer.isScheduled(), false );

    observer.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.isScheduled(), false );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    assertEquals( observer.getState(), Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer.isScheduled(), false );

    observer.clearScheduledFlag();
    Arez.context().getScheduler().getPendingObservers().truncate( 0 );
    assertEquals( observer.isScheduled(), false );

    observer.setState( Flags.STATE_STALE );
    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertEquals( observer.isScheduled(), true );

    observer.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    observer.setState( Flags.STATE_STALE );

    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertEquals( observer.isScheduled(), true );

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
    throws Exception
  {
    setupReadWriteTransaction();

    final Observer observer = Arez.context().computed( () -> "" ).getObserver();

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );

    observer.setState( Flags.STATE_INACTIVE, false );

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );
    assertEquals( observer.isScheduled(), false );

    observer.setState( Flags.STATE_UP_TO_DATE, false );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.isScheduled(), false );

    observer.setState( Flags.STATE_POSSIBLY_STALE, false );

    assertEquals( observer.getState(), Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer.isScheduled(), false );

    observer.clearScheduledFlag();
    Arez.context().getScheduler().getPendingObservers().truncate( 0 );
    assertEquals( observer.isScheduled(), false );

    observer.setState( Flags.STATE_STALE, false );
    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertEquals( observer.isScheduled(), false );

    observer.setState( Flags.STATE_UP_TO_DATE, false );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    observer.setState( Flags.STATE_STALE, false );

    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertEquals( observer.isScheduled(), false );

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
    throws Exception
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
    assertEquals( observer.isScheduled(), false );
    assertEquals( onActivate.getCalls(), 0 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 0 );

    computedValue.setValue( ValueUtil.randomString() );
    observer.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.isScheduled(), false );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 0 );
    assertNotNull( computedValue.getValue() );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    assertEquals( observer.getState(), Flags.STATE_POSSIBLY_STALE );
    assertEquals( observer.isScheduled(), true );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 1 );
    assertEquals( watcher.getState(), Flags.STATE_POSSIBLY_STALE );
    assertEquals( derivedValue.getLeastStaleObserverState(), Flags.STATE_POSSIBLY_STALE );
    assertNotNull( computedValue.getValue() );

    observer.clearScheduledFlag();
    Arez.context().getScheduler().getPendingObservers().truncate( 0 );
    assertEquals( observer.isScheduled(), false );

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
    assertEquals( observer.isScheduled(), true );
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
    throws Exception
  {
    final Observer observer = Arez.context().computed( () -> "" ).getObserver();
    setCurrentTransaction( observer );

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );

    observer.markAsDisposed();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observer.setState( Flags.STATE_UP_TO_DATE ) );

    assertEquals( exception.getMessage(),
                  "Arez-0087: Attempted to activate disposed observer named '" + observer.getName() + "'." );
  }

  @Test
  public void setState_noTransaction()
    throws Exception
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observer.setState( Flags.STATE_UP_TO_DATE ) );

    assertEquals( exception.getMessage(),
                  "Arez-0086: Attempt to invoke setState on observer named '" + observer.getName() + "' when " +
                  "there is no active transaction." );
  }

  @Test
  public void scheduleReaction()
    throws Exception
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );

    setupReadWriteTransaction();
    observer.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.isScheduled(), false );

    observer.scheduleReaction();

    final ArezContext context = Arez.context();

    assertEquals( observer.isScheduled(), true );
    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
    assertEquals( context.getScheduler().getPendingObservers().contains( observer ), true );

    //Duplicate schedule should not result in it being added again
    observer.scheduleReaction();

    assertEquals( observer.isScheduled(), true );
    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
    assertEquals( context.getScheduler().getPendingObservers().contains( observer ), true );
  }

  @Test
  public void scheduleReaction_when_Disposed()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );

    context.safeAction( null, true, false, () -> {
      observer.setState( Flags.STATE_INACTIVE );
      observer.setState( Flags.STATE_DISPOSED );

      assertEquals( observer.isScheduled(), false );

      observer.scheduleReaction();

      assertEquals( observer.isScheduled(), false );
    } );
  }

  @Test
  public void scheduleReaction_whenInactive()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );

    context.safeAction( null, true, false, () -> observer.setState( Flags.STATE_INACTIVE ) );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.safeAction( null, true, false, observer::scheduleReaction ) );

    assertEquals( exception.getMessage(),
                  "Arez-0088: Observer named '" + observer.getName() + "' is not active but an attempt has " +
                  "been made to schedule observer." );
  }

  @Test
  public void dispose()
    throws Exception
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );
    observer.setState( Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( null );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.isNotDisposed(), true );
    assertEquals( observer.isDisposed(), false );

    observer.dispose();

    assertEquals( observer.getState(), Flags.STATE_DISPOSED );
    assertEquals( observer.isNotDisposed(), false );
    assertEquals( observer.isDisposed(), true );

    final ArezContext context = Arez.context();

    final int currentNextTransactionId = context.currentNextTransactionId();

    observer.dispose();

    // This verifies no new transactions were created
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId );
  }

  @Test
  public void dispose_onComputedValue()
    throws Exception
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
    assertEquals( observer.isNotDisposed(), true );
    assertEquals( observer.isDisposed(), false );
    assertEquals( observableValue.getObservers().size(), 1 );

    observer.dispose();

    assertEquals( observer.getState(), Flags.STATE_DISPOSED );
    assertEquals( observer.isNotDisposed(), false );
    assertEquals( observer.isDisposed(), true );
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
    throws Exception
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );
    observer.setState( Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( null );

    assertEquals( observer.isDisposed(), false );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    Arez.context().getSpy().addSpyEventHandler( handler );

    observer.dispose();

    assertEquals( observer.isDisposed(), true );

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
    throws Exception
  {
    final Observer observer = Arez.context().computed( () -> "" ).getObserver();
    final ComputedValue<?> computedValue = observer.getComputedValue();
    final ObservableValue<?> derivedValue = computedValue.getObservableValue();
    setCurrentTransaction( observer );
    observer.setState( Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( null );

    assertEquals( observer.isDisposed(), false );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    Arez.context().getSpy().addSpyEventHandler( handler );

    observer.dispose();

    assertEquals( observer.isDisposed(), true );
    assertEquals( derivedValue.isDisposed(), true );
    assertEquals( computedValue.isDisposed(), true );

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
    throws Exception
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

    assertEquals( observer.isDisposed(), false );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    Arez.context().getSpy().addSpyEventHandler( handler );

    computedValue.dispose();

    assertEquals( observer.isDisposed(), true );
    assertEquals( computedValue.isDisposed(), true );

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
    throws Exception
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );

    assertEquals( observer.isComputedValue(), false );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::getComputedValue );

    assertEquals( exception.getMessage(),
                  "Arez-0095: Attempted to invoke getComputedValue on observer named '" + observer.getName() +
                  "' when is not a computed observer." );
  }

  @Test
  public void getComputedValue_whenDisposed()
    throws Exception
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
    throws Exception
  {
    setIgnoreObserverErrors( true );

    final ArezContext context = Arez.context();

    final AtomicInteger errorCount = new AtomicInteger();

    context.addObserverErrorHandler( ( observer, error, throwable ) -> errorCount.incrementAndGet() );

    final CountAndObserveProcedure trackedExecutable = new CountAndObserveProcedure();

    final Observer observer = new Observer( context, null, ValueUtil.randomString(), trackedExecutable, null, 0 );

    observer.invokeReaction();

    assertEquals( trackedExecutable.getCallCount(), 1 );
    assertEquals( errorCount.get(), 0 );
  }

  @Test
  public void invokeReaction_Observer_SpyEventHandlerPresent()
    throws Exception
  {
    final TestSpyEventHandler handler = new TestSpyEventHandler();
    final ArezContext context = Arez.context();

    final ObservableValue<Object> observable = context.observable();

    final CountingProcedure trackedExecutable = new CountingProcedure()
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
                    trackedExecutable,
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
    throws Exception
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
    throws Exception
  {
    final CountingProcedure trackedExecutable = new CountAndObserveProcedure();
    final Observer observer =
      new Observer( Arez.context(),
                    null,
                    ValueUtil.randomString(),
                    trackedExecutable,
                    null,
                    0 );

    observer.invokeReaction();

    assertEquals( trackedExecutable.getCallCount(), 1 );

    observer.markAsDisposed();

    observer.invokeReaction();

    assertEquals( trackedExecutable.getCallCount(), 1 );
  }

  @Test
  public void invokeReaction_onUpToDateObserver()
    throws Exception
  {
    final CountAndObserveProcedure trackedExecutable = new CountAndObserveProcedure();
    final CountingProcedure onDepsUpdated = new CountingProcedure();
    final ArezContext context = Arez.context();
    final Observer observer =
      new Observer( context,
                    null,
                    ValueUtil.randomString(),
                    trackedExecutable,
                    onDepsUpdated,
                    0 );

    context.triggerScheduler();

    assertEquals( trackedExecutable.getCallCount(), 1 );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    //Invoke reaction - observer is uptodate
    observer.invokeReaction();

    assertEquals( trackedExecutable.getCallCount(), 1 );
    assertEquals( onDepsUpdated.getCallCount(), 0 );
  }

  @Test
  public void invokeReaction_reactionGeneratesError()
    throws Exception
  {
    setIgnoreObserverErrors( true );

    final AtomicInteger errorCount = new AtomicInteger();

    final RuntimeException exception = new RuntimeException( "X" );

    Arez.context().addObserverErrorHandler( ( observer, error, throwable ) -> {
      errorCount.incrementAndGet();
      assertEquals( error, ObserverError.REACTION_ERROR );
      assertEquals( throwable, exception );
    } );

    final CountingProcedure trackedExecutable = new CountingProcedure()
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
                    trackedExecutable,
                    null,
                    0 );

    observer.invokeReaction();

    assertEquals( trackedExecutable.getCallCount(), 1 );
    assertEquals( errorCount.get(), 1 );
  }

  @Test
  public void shouldCompute_DISPOSING()
    throws Exception
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_DISPOSING );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::shouldCompute );
    assertEquals( exception.getMessage(),
                  "Arez-0205: Observer.shouldCompute() invoked on observer named 'Observer@1' but observer is in state DISPOSING" );
  }

  @Test
  public void shouldCompute_UP_TO_DATE()
    throws Exception
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.shouldCompute(), false );
  }

  @Test
  public void shouldCompute_INACTIVE()
    throws Exception
  {
    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_INACTIVE );

    assertEquals( observer.shouldCompute(), true );
  }

  @Test
  public void shouldCompute_STALE()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );

    context.safeAction( null, true, false, () -> {
      observer.setState( Flags.STATE_STALE );
      assertEquals( observer.shouldCompute(), true );
    } );
  }

  @Test
  public void shouldCompute_POSSIBLY_STALE()
    throws Exception
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

    assertEquals( observer.shouldCompute(), true );

    assertEquals( observer.getState(), Flags.STATE_STALE );
  }

  @Test
  public void shouldCompute_POSSIBLY_STALE_ComputableThrowsException()
    throws Exception
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

    assertEquals( observer.shouldCompute(), true );

    assertEquals( observer.getState(), Flags.STATE_STALE );
  }

  @Test
  public void shouldCompute_POSSIBLY_STALE_ComputableReThrowsException()
    throws Exception
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

    assertEquals( observer.shouldCompute(), false );

    assertEquals( observer.getState(), Flags.STATE_POSSIBLY_STALE );
  }

  @Test
  public void shouldCompute_POSSIBLY_STALE_whereDependencyRecomputesButDoesNotChange()
    throws Exception
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

    assertEquals( observer.shouldCompute(), false );

    assertEquals( computedValue2.getObserver().getState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void shouldCompute_POSSIBLY_STALE_ignoresNonComputedDependencies()
    throws Exception
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

    assertEquals( observer.shouldCompute(), false );
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

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::asInfo );
    assertEquals( exception.getMessage(),
                  "Arez-0197: Observer.asInfo() invoked but Arez.areSpiesEnabled() returned false." );
  }

  @Test
  public void reportStale()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            new CountingProcedure(),
                                            new CountingProcedure(),
                                            Flags.NON_AREZ_DEPENDENCIES );
    context.triggerScheduler();

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.isScheduled(), false );

    context.safeAction( null, true, false, () -> {
      observer.reportStale();

      assertEquals( observer.isScheduled(), true );
      assertEquals( observer.getState(), Flags.STATE_STALE );
      assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
      assertEquals( context.getScheduler().getPendingObservers().contains( observer ), true );
    } );
  }

  @Test
  public void reportStale_arezOnlyDependencies()
    throws Exception
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
    assertEquals( observer.isScheduled(), false );

    context.safeAction( null, true, false, () -> {
      final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::reportStale );

      assertEquals( exception.getMessage(),
                    "Arez-0199: Observer.reportStale() invoked on observer named '" + observer.getName() +
                    "' but arezOnlyDependencies = true." );
      assertEquals( observer.isScheduled(), false );
      assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
      assertEquals( context.getScheduler().getPendingObservers().size(), 0 );
    } );
  }

  @Test
  public void reportStale_noTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            new CountingProcedure(),
                                            new CountingProcedure(),
                                            Flags.NON_AREZ_DEPENDENCIES );

    context.triggerScheduler();

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.isScheduled(), false );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::reportStale );

    assertEquals( exception.getMessage(),
                  "Arez-0200: Observer.reportStale() invoked on observer named '" + observer.getName() +
                  "' when there is no active transaction." );
    assertEquals( observer.isScheduled(), false );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );
  }

  @Test
  public void reportStale_readOnlyTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer observer = context.observer( new CountingProcedure(), Flags.NON_AREZ_DEPENDENCIES );

    context.safeAction( null, false, false, () -> observer.setState( Flags.STATE_UP_TO_DATE ) );
    assertEquals( observer.isScheduled(), false );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> context.safeAction( "MyAction", false, observer::reportStale ) );

    assertEquals( exception.getMessage(),
                  "Arez-0201: Observer.reportStale() invoked on observer named '" + observer.getName() +
                  "' when the active transaction 'MyAction' is READ_ONLY rather " +
                  "than READ_WRITE." );
    assertEquals( observer.isScheduled(), false );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );
  }

  @Test
  public void schedule()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final CountAndObserveProcedure trackedExecutable = new CountAndObserveProcedure();
    final CountingProcedure onDepsUpdated = new CountingProcedure();
    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            trackedExecutable,
                                            onDepsUpdated,
                                            Flags.DEFER_REACT );

    context.safeAction( null, true, false, () -> {
      observer.setState( Flags.STATE_STALE );

      // reset the scheduling that occurred due to setState
      observer.clearScheduledFlag();
      context.getScheduler().getPendingObservers().clear();
    } );

    assertEquals( observer.isScheduled(), false );
    assertEquals( trackedExecutable.getCallCount(), 0 );
    assertEquals( onDepsUpdated.getCallCount(), 0 );

    final Disposable schedulerLock = context.pauseScheduler();

    observer.schedule();

    assertEquals( observer.isScheduled(), true );

    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
    assertEquals( context.getScheduler().getPendingObservers().contains( observer ), true );

    schedulerLock.dispose();

    // reaction not executed as state was still UP_TO_DATE
    assertEquals( observer.isScheduled(), false );
    assertEquals( trackedExecutable.getCallCount(), 1 );
    assertEquals( onDepsUpdated.getCallCount(), 0 );
  }

  @Test
  public void schedule_but_state_UP_TO_DATE()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final CountAndObserveProcedure trackedExecutable = new CountAndObserveProcedure();
    final CountingProcedure onDepsUpdated = new CountingProcedure();
    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            trackedExecutable,
                                            onDepsUpdated,
                                            0 );
    context.triggerScheduler();

    assertEquals( trackedExecutable.getCallCount(), 1 );
    assertEquals( onDepsUpdated.getCallCount(), 0 );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.isScheduled(), false );

    final Disposable schedulerLock = context.pauseScheduler();

    observer.schedule();

    assertEquals( observer.isScheduled(), true );

    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
    assertEquals( context.getScheduler().getPendingObservers().contains( observer ), true );

    schedulerLock.dispose();

    assertEquals( observer.isScheduled(), false );
    assertEquals( trackedExecutable.getCallCount(), 1 );
    assertEquals( onDepsUpdated.getCallCount(), 0 );
  }

  @Test
  public void schedule_onNonManualScheduleObserver()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final CountAndObserveProcedure trackedExecutable = new CountAndObserveProcedure();
    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            trackedExecutable,
                                            null,
                                            Flags.DEFER_REACT );
    context.safeAction( null, true, false, () -> {
      observer.setState( Flags.STATE_STALE );

      // reset the scheduling that occurred due to setState
      observer.clearScheduledFlag();
      context.getScheduler().getPendingObservers().clear();
    } );

    assertEquals( observer.isScheduled(), false );

    final Disposable schedulerLock = context.pauseScheduler();

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::schedule );

    assertEquals( exception.getMessage(),
                  "Arez-0202: Observer.schedule() invoked on observer named '" + observer.getName() +
                  "' but supportsManualSchedule() returns false." );
    assertEquals( observer.isScheduled(), false );
    assertEquals( observer.getState(), Flags.STATE_STALE );
    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );

    schedulerLock.dispose();

    assertEquals( trackedExecutable.getCallCount(), 0 );
  }
}
