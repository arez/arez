package arez;

import arez.spy.ActionCompletedEvent;
import arez.spy.ActionStartedEvent;
import arez.spy.ComputeCompletedEvent;
import arez.spy.ComputeStartedEvent;
import arez.spy.ComputedValueDeactivatedEvent;
import arez.spy.ComputedValueDisposedEvent;
import arez.spy.ObservableValueChangedEvent;
import arez.spy.ObserverDisposedEvent;
import arez.spy.ObserverInfo;
import arez.spy.ReactionCompletedEvent;
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
    final CountingProcedure trackedExecutable = new CountingProcedure();
    final CountingProcedure onDepsUpdated = new CountingProcedure();
    final Observer observer =
      new Observer( context,
                    null,
                    name,
                    null,
                    TransactionMode.READ_ONLY,
                    trackedExecutable,
                    onDepsUpdated,
                    Priority.NORMAL,
                    false,
                    false,
                    true,
                    true,
                    false );

    // Verify all "Node" behaviour
    assertEquals( observer.getContext(), context );
    assertEquals( observer.getName(), name );
    assertEquals( observer.toString(), name );

    // Starts out inactive and inactive means no dependencies
    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( observer.isActive(), false );
    assertEquals( observer.isInactive(), true );
    assertEquals( observer.getDependencies().size(), 0 );

    // Reaction attributes
    assertEquals( observer.getMode(), TransactionMode.READ_ONLY );
    assertEquals( observer.getTrackedExecutable(), trackedExecutable );
    assertEquals( observer.getOnDepsUpdated(), onDepsUpdated );
    assertEquals( observer.isScheduled(), false );

    assertEquals( observer.isComputedValue(), false );

    assertEquals( observer.getPriority(), Priority.NORMAL );
    assertEquals( observer.canObserveLowerPriorityDependencies(), false );

    observer.invariantState();

    assertEquals( context.getTopLevelObservers().get( observer.getName() ), observer );
  }

  @Test
  public void initialStateForComputedValueObserver()
    throws Exception
  {
    final ComputedValue<String> computedValue = Arez.context().computed( () -> "" );
    final Observer observer = computedValue.getObserver();

    assertEquals( observer.isComputedValue(), true );

    assertEquals( computedValue.getObservableValue().getName(), observer.getName() );
    assertEquals( computedValue.getObservableValue().getOwner(), observer );

    assertEquals( computedValue.getName(), observer.getName() );
    assertEquals( computedValue.getObserver(), observer );
  }

  @Test
  public void construct_with_READ_WRITE_OWNED_but_noComputableValue()
    throws Exception
  {
    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( Arez.context(),
                                        null,
                                        name,
                                        null,
                                        TransactionMode.READ_WRITE_OWNED,
                                        new CountingProcedure(),
                                        new CountingProcedure(),
                                        Priority.NORMAL,
                                        false,
                                        false,
                                        true,
                                        true,
                                        false ) );

    assertEquals( exception.getMessage(),
                  "Arez-0079: Attempted to construct an observer named '" + name + "' with READ_WRITE_OWNED " +
                  "transaction mode but no ComputedValue." );
  }

  @Test
  public void construct_with_canObserveLowerPriorityDependencies_but_LOWEST_priority()
    throws Exception
  {
    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( Arez.context(),
                                        null,
                                        name,
                                        null,
                                        TransactionMode.READ_ONLY,
                                        new CountingProcedure(),
                                        new CountingProcedure(),
                                        Priority.LOWEST,
                                        false,
                                        true,
                                        true,
                                        true,
                                        false ) );

    assertEquals( exception.getMessage(),
                  "Arez-0184: Observer named '" + name + "' has LOWEST priority but has passed " +
                  "observeLowerPriorityDependencies = true which should be false as no lower priority." );
  }

  @Test
  public void construct_with_mode_but_checking_DIsabled()
    throws Exception
  {
    ArezTestUtil.noEnforceTransactionType();

    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( Arez.context(),
                                        null,
                                        name,
                                        null,
                                        TransactionMode.READ_ONLY,
                                        new CountingProcedure(),
                                        new CountingProcedure(),
                                        Priority.NORMAL,
                                        false,
                                        false,
                                        true,
                                        true,
                                        false ) );

    assertEquals( exception.getMessage(),
                  "Arez-0082: Observer named '" + name + "' specified mode 'READ_ONLY' " +
                  "when Arez.enforceTransactionType() is false." );
  }

  @SuppressWarnings( "ResultOfMethodCallIgnored" )
  @Test
  public void construct_with_no_mode_and_noEnforceTransactionType()
    throws Exception
  {
    ArezTestUtil.noEnforceTransactionType();

    final String name = ValueUtil.randomString();

    final Observer observer =
      new Observer( Arez.context(),
                    null,
                    name,
                    null,
                    null,
                    new CountingProcedure(),
                    new CountingProcedure(),
                    Priority.NORMAL,
                    false,
                    false,
                    true,
                    true,
                    false );
    assertThrows( observer::getMode );
  }

  @Test
  public void construct_with_READ_ONLY_but_ComputableValue()
    throws Exception
  {
    final String name = ValueUtil.randomString();

    final ArezContext context = Arez.context();
    final ComputedValue<?> computedValue = context.computed( () -> "" );
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( context,
                                        null,
                                        name,
                                        computedValue,
                                        TransactionMode.READ_ONLY,
                                        new CountingProcedure(),
                                        new CountingProcedure(),
                                        Priority.NORMAL,
                                        false,
                                        false,
                                        true,
                                        true,
                                        false ) );

    assertEquals( exception.getMessage(),
                  "Arez-0081: Attempted to construct an observer named '" + name + "' with READ_ONLY " +
                  "transaction mode and a ComputedValue." );
  }

  @Test
  public void construct_with_canExplicitlyTrack_and_ComputableValue()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ComputedValue<?> computedValue = context.computed( () -> "" );
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( context,
                                        null,
                                        computedValue.getName(),
                                        computedValue,
                                        TransactionMode.READ_WRITE_OWNED,
                                        new CountingProcedure(),
                                        new CountingProcedure(),
                                        Priority.NORMAL,
                                        true,
                                        false,
                                        true,
                                        true,
                                        false ) );

    assertEquals( exception.getMessage(),
                  "Arez-0080: Attempted to construct an ComputedValue '" + computedValue.getName() +
                  "' that could track explicitly." );
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
                                        null,
                                        TransactionMode.READ_ONLY,
                                        new CountingProcedure(),
                                        new CountingProcedure(),
                                        Priority.NORMAL,
                                        false,
                                        false,
                                        true,
                                        true,
                                        false ) );
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
                    null,
                    TransactionMode.READ_ONLY,
                    new CountingProcedure(),
                    new CountingProcedure(),
                    Priority.NORMAL,
                    false,
                    false,
                    true,
                    true,
                    false );
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
    final Observer observer = Arez.context().autorun( new CountAndObserveProcedure() );
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
    final Observer observer = Arez.context().autorun( new CountAndObserveProcedure() );
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
    final Observer observer = context.autorun( observable::reportObserved );

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
    final Observer observer = context.autorun( null, null, false, new CountingProcedure(), Priority.NORMAL, false );

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

    context.safeAction( null, false, false, () -> observer.setState( ObserverState.UP_TO_DATE ) );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.safeAction( observer::invariantComputedValueObserverState ) );

    assertEquals( exception.getMessage(),
                  "Arez-0094: Observer named '" + observer.getName() + "' is a ComputedValue and " +
                  "active but the associated ObservableValue has no observers." );

    context.autorun( () -> computedValue.getObservableValue().reportObserved() );

    observer.invariantComputedValueObserverState();
  }

  @Test
  public void invariantDerivationState_observerCanHaveNoDependenciesIfItIsTracker()
    throws Exception
  {
    final Observer observer = Arez.context().computed( () -> "" ).getObserver();

    observer.invariantComputedValueObserverState();

    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    observer.invariantComputedValueObserverState();
  }

  @Test
  public void replaceDependencies()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer =
      context.autorun( null, null, false, new CountingProcedure(), Priority.NORMAL, true, false, false, false, null );

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
    final Observer observer = context.autorun( observable::reportObserved );

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
    final Observer observer = context.autorun( observable::reportObserved );

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

    final Observer observer = context.autorun( () -> {
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
    final Observer observer = Arez.context().autorun( new CountAndObserveProcedure() );

    observer.runHook( null, ObserverError.ON_ACTIVATE_ERROR );
  }

  @Test
  public void runHook_normalHook()
    throws Exception
  {
    final Observer observer = Arez.context().autorun( new CountAndObserveProcedure() );

    final AtomicInteger counter = new AtomicInteger();

    observer.runHook( counter::incrementAndGet, ObserverError.ON_ACTIVATE_ERROR );

    assertEquals( counter.get(), 1 );
  }

  @Test
  public void runHook_hookThrowsException()
    throws Exception
  {
    setIgnoreObserverErrors( true );

    final Observer observer = Arez.context().autorun( new CountAndObserveProcedure() );

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
    final Observer observer = Arez.context().autorun( new CountAndObserveProcedure() );

    setupReadWriteTransaction();

    observer.setState( ObserverState.INACTIVE );

    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( observer.isScheduled(), false );

    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.isScheduled(), false );

    observer.setState( ObserverState.POSSIBLY_STALE );

    assertEquals( observer.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer.isScheduled(), false );

    observer.clearScheduledFlag();
    Arez.context().getScheduler().getPendingObservers().truncate( 0 );
    assertEquals( observer.isScheduled(), false );

    observer.setState( ObserverState.STALE );
    assertEquals( observer.getState(), ObserverState.STALE );
    assertEquals( observer.isScheduled(), true );

    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    observer.setState( ObserverState.STALE );

    assertEquals( observer.getState(), ObserverState.STALE );
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

    observer.setState( ObserverState.INACTIVE );

    assertEquals( observer.getState(), ObserverState.INACTIVE );

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

    assertEquals( observer.getState(), ObserverState.INACTIVE );

    observer.setState( ObserverState.INACTIVE, false );

    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( observer.isScheduled(), false );

    observer.setState( ObserverState.UP_TO_DATE, false );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.isScheduled(), false );

    observer.setState( ObserverState.POSSIBLY_STALE, false );

    assertEquals( observer.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer.isScheduled(), false );

    observer.clearScheduledFlag();
    Arez.context().getScheduler().getPendingObservers().truncate( 0 );
    assertEquals( observer.isScheduled(), false );

    observer.setState( ObserverState.STALE, false );
    assertEquals( observer.getState(), ObserverState.STALE );
    assertEquals( observer.isScheduled(), false );

    observer.setState( ObserverState.UP_TO_DATE, false );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    observer.setState( ObserverState.STALE, false );

    assertEquals( observer.getState(), ObserverState.STALE );
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

    observer.setState( ObserverState.INACTIVE, false );

    assertEquals( observer.getState(), ObserverState.INACTIVE );

    assertEquals( observer.getDependencies().size(), 0 );
    assertEquals( observableValue1.getObservers().size(), 0 );
    assertEquals( observableValue2.getObservers().size(), 0 );
  }

  @Test
  public void setState_onComputedValue()
    throws Exception
  {
    final ComputedValue<String> computedValue = Arez.context().computed( () -> "" );
    final ObservableValue<String> derivedValue = computedValue.getObservableValue();
    final Observer observer = computedValue.getObserver();

    final Observer watcher = observer.getContext().autorun( new CountAndObserveProcedure() );

    setupReadWriteTransaction();

    watcher.setState( ObserverState.UP_TO_DATE );
    observer.getComputedValue().getObservableValue().rawAddObserver( watcher );
    watcher.getDependencies().add( observer.getComputedValue().getObservableValue() );

    watcher.setState( ObserverState.UP_TO_DATE );

    final TestProcedure onActivate = new TestProcedure();
    final TestProcedure onDeactivate = new TestProcedure();
    final TestProcedure onStale = new TestProcedure();

    computedValue.setOnActivate( onActivate );
    computedValue.setOnDeactivate( onDeactivate );
    computedValue.setOnStale( onStale );

    assertEquals( observer.getState(), ObserverState.INACTIVE );

    observer.setState( ObserverState.INACTIVE );

    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( observer.isScheduled(), false );
    assertEquals( onActivate.getCalls(), 0 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 0 );

    computedValue.setValue( ValueUtil.randomString() );
    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.isScheduled(), false );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 0 );
    assertNotNull( computedValue.getValue() );

    observer.setState( ObserverState.POSSIBLY_STALE );

    assertEquals( observer.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer.isScheduled(), true );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 1 );
    assertEquals( watcher.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( derivedValue.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );
    assertNotNull( computedValue.getValue() );

    observer.clearScheduledFlag();
    Arez.context().getScheduler().getPendingObservers().truncate( 0 );
    assertEquals( observer.isScheduled(), false );

    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 1 );
    assertNotNull( computedValue.getValue() );

    watcher.setState( ObserverState.UP_TO_DATE );
    derivedValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observer.setState( ObserverState.STALE );

    assertEquals( observer.getState(), ObserverState.STALE );
    assertEquals( observer.isScheduled(), true );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 2 );

    assertEquals( watcher.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( derivedValue.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );

    observer.setState( ObserverState.INACTIVE );

    assertEquals( observer.getState(), ObserverState.INACTIVE );
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

    assertEquals( observer.getState(), ObserverState.INACTIVE );

    observer.markAsDisposed();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observer.setState( ObserverState.UP_TO_DATE ) );

    assertEquals( exception.getMessage(),
                  "Arez-0087: Attempted to activate disposed observer named '" + observer.getName() + "'." );
  }

  @Test
  public void setState_noTransaction()
    throws Exception
  {
    final Observer observer = Arez.context().autorun( new CountAndObserveProcedure() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observer.setState( ObserverState.UP_TO_DATE ) );

    assertEquals( exception.getMessage(),
                  "Arez-0086: Attempt to invoke setState on observer named '" + observer.getName() + "' when " +
                  "there is no active transaction." );
  }

  @Test
  public void scheduleReaction()
    throws Exception
  {
    final Observer observer = Arez.context().autorun( new CountAndObserveProcedure() );

    setupReadWriteTransaction();
    observer.setState( ObserverState.UP_TO_DATE );

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
    final Observer observer = context.autorun( new CountAndObserveProcedure() );

    context.safeAction( null, true, false, () -> {
      observer.setState( ObserverState.INACTIVE );
      observer.setState( ObserverState.DISPOSED );

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
    final Observer observer = context.autorun( new CountAndObserveProcedure() );

    context.safeAction( null, true, false, () -> observer.setState( ObserverState.INACTIVE ) );

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
    final Observer observer = Arez.context().autorun( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );
    observer.setState( ObserverState.UP_TO_DATE );

    Transaction.setTransaction( null );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.isNotDisposed(), true );
    assertEquals( observer.isDisposed(), false );

    observer.dispose();

    assertEquals( observer.getState(), ObserverState.DISPOSED );
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
    observer.setState( ObserverState.STALE );

    final ObservableValue<?> observableValue = Arez.context().observable();
    observableValue.setLeastStaleObserverState( ObserverState.STALE );
    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    Transaction.setTransaction( null );

    assertEquals( observer.getState(), ObserverState.STALE );
    assertEquals( observer.isNotDisposed(), true );
    assertEquals( observer.isDisposed(), false );
    assertEquals( observableValue.getObservers().size(), 1 );

    observer.dispose();

    assertEquals( observer.getState(), ObserverState.DISPOSED );
    assertEquals( observer.isNotDisposed(), false );
    assertEquals( observer.isDisposed(), true );
    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( observableValue.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    final ArezContext context = Arez.context();

    final int currentNextTransactionId = context.currentNextTransactionId();

    observer.dispose();

    // This verifies no new transactions were created
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId );
  }

  @Test
  public void dispose_invokedHook()
    throws Exception
  {
    final Observer observer = Arez.context().computed( () -> "" ).getObserver();
    setCurrentTransaction( observer );
    observer.setState( ObserverState.UP_TO_DATE );
    final AtomicInteger callCount = new AtomicInteger();
    observer.setOnDispose( callCount::incrementAndGet );

    Transaction.setTransaction( null );

    assertEquals( observer.isDisposed(), false );
    assertEquals( callCount.get(), 0 );

    observer.dispose();

    assertEquals( observer.isDisposed(), true );
    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void dispose_generates_spyEvent()
    throws Exception
  {
    final Observer observer = Arez.context().autorun( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );
    observer.setState( ObserverState.UP_TO_DATE );

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
    observer.setState( ObserverState.UP_TO_DATE );

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
    observer.setState( ObserverState.UP_TO_DATE );

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
    final Observer observer = Arez.context().autorun( new CountAndObserveProcedure() );

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
    final Observer observer = Arez.context().autorun( new CountAndObserveProcedure() );

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

    observableValue1.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observableValue2.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );
    observableValue3.setLeastStaleObserverState( ObserverState.STALE );

    observer.markDependenciesLeastStaleObserverAsUpToDate();

    assertEquals( observableValue1.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observableValue2.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observableValue3.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void invokeReaction()
    throws Exception
  {
    setIgnoreObserverErrors( true );

    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final TransactionMode mode = TransactionMode.READ_ONLY;

    final AtomicInteger errorCount = new AtomicInteger();

    context.addObserverErrorHandler( ( observer, error, throwable ) -> errorCount.incrementAndGet() );

    final CountingProcedure trackedExecutable = new CountingProcedure();

    final Observer observer =
      new Observer( context,
                    null,
                    name,
                    null,
                    mode,
                    trackedExecutable,
                    null,
                    Priority.NORMAL,
                    false,
                    false,
                    true,
                    false,
                    false );

    observer.invokeReaction();

    assertEquals( trackedExecutable.getCallCount(), 1 );
    assertEquals( errorCount.get(), 0 );
  }

  @Test
  public void invokeReaction_Observer_SpyEventHandlerPresent()
    throws Exception
  {
    final TestSpyEventHandler handler = new TestSpyEventHandler();
    Arez.context().getSpy().addSpyEventHandler( handler );

    final CountingProcedure trackedExecutable = new CountingProcedure()
    {
      @Override
      public void call()
        throws Throwable
      {
        super.call();
        Thread.sleep( 1 );
      }
    };
    final Observer observer =
      new Observer( Arez.context(),
                    null,
                    ValueUtil.randomString(),
                    null,
                    TransactionMode.READ_ONLY,
                    trackedExecutable,
                    null,
                    Priority.NORMAL,
                    false,
                    false,
                    true,
                    false,
                    false );

    observer.invokeReaction();

    handler.assertEventCount( 6 );

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
    final ObservableValue<Object> observableValue = Arez.context().observable();
    final TestSpyEventHandler handler = new TestSpyEventHandler();
    Arez.context().getSpy().addSpyEventHandler( handler );

    final SafeFunction<Integer> function = () -> {
      observableValue.reportObserved();
      return 1;
    };
    final ComputedValue<Integer> computedValue =
      new ComputedValue<>( Arez.context(),
                           null,
                           ValueUtil.randomString(),
                           function,
                           Priority.NORMAL,
                           false,
                           false,
                           true );

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
    final CountingProcedure trackedExecutable = new CountingProcedure();
    final Observer observer =
      new Observer( Arez.context(),
                    null,
                    ValueUtil.randomString(),
                    null,
                    TransactionMode.READ_ONLY,
                    trackedExecutable,
                    null,
                    Priority.NORMAL,
                    false,
                    false,
                    true,
                    false,
                    false );

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
    final CountingProcedure trackedExecutable = new CountingProcedure();
    final CountingProcedure onDepsUpdated = new CountingProcedure();
    final Observer observer =
      new Observer( Arez.context(),
                    null,
                    ValueUtil.randomString(),
                    null,
                    TransactionMode.READ_ONLY,
                    trackedExecutable,
                    onDepsUpdated,
                    Priority.NORMAL,
                    false,
                    false,
                    true,
                    true,
                    false );

    setupReadWriteTransaction();
    observer.setState( ObserverState.UP_TO_DATE );
    Transaction.setTransaction( null );

    //Invoke reaction
    observer.invokeReaction();

    assertEquals( trackedExecutable.getCallCount(), 0 );
    assertEquals( onDepsUpdated.getCallCount(), 0 );
  }

  @Test
  public void invokeReaction_reactionGeneratesError()
    throws Exception
  {
    setIgnoreObserverErrors( true );

    final String name = ValueUtil.randomString();
    final TransactionMode mode = TransactionMode.READ_ONLY;

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
                    name,
                    null,
                    mode,
                    trackedExecutable,
                    null,
                    Priority.NORMAL,
                    false,
                    false,
                    true,
                    false,
                    false );

    observer.invokeReaction();

    assertEquals( trackedExecutable.getCallCount(), 1 );
    assertEquals( errorCount.get(), 1 );
  }

  @Test
  public void shouldCompute_UP_TO_DATE()
    throws Exception
  {
    final Observer observer = Arez.context().autorun( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.shouldCompute(), false );
  }

  @Test
  public void shouldCompute_INACTIVE()
    throws Exception
  {
    final Observer observer = Arez.context().autorun( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.INACTIVE );

    assertEquals( observer.shouldCompute(), true );
  }

  @Test
  public void shouldCompute_STALE()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.autorun( new CountAndObserveProcedure() );

    context.safeAction( null, true, false, () -> {
      observer.setState( ObserverState.STALE );
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
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), function, Priority.NORMAL, false, false, true );

    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final SafeFunction<String> function2 = () -> {
      observeADependency();
      return "";
    };
    final ComputedValue<String> computedValue2 =
      new ComputedValue<>( context, null, ValueUtil.randomString(), function2, Priority.NORMAL, false, false, true );

    observer.getDependencies().add( computedValue2.getObservableValue() );
    computedValue2.getObservableValue().addObserver( observer );

    // Set it to something random so it will change
    computedValue2.setValue( ValueUtil.randomString() );

    assertEquals( observer.shouldCompute(), true );

    assertEquals( observer.getState(), ObserverState.STALE );
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
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), function1, Priority.NORMAL, false, false, true );
    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final SafeFunction<String> function = () -> {
      observeADependency();
      throw new IllegalStateException();
    };
    final ComputedValue<String> computedValue2 =
      new ComputedValue<>( context, null, ValueUtil.randomString(), function, Priority.NORMAL, false, false, true );

    observer.getDependencies().add( computedValue2.getObservableValue() );
    computedValue2.getObservableValue().addObserver( observer );

    // Set it to something random so it will change
    computedValue2.setValue( ValueUtil.randomString() );

    assertEquals( observer.shouldCompute(), true );

    assertEquals( observer.getState(), ObserverState.STALE );
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
    final ComputedValue<String> computedValue =
      new ComputedValue<>( Arez.context(),
                           null,
                           ValueUtil.randomString(),
                           function1,
                           Priority.NORMAL,
                           false,
                           false,
                           true );
    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final SafeFunction<String> function = () -> {
      observeADependency();
      throw new IllegalStateException();
    };
    final ComputedValue<String> computedValue2 =
      new ComputedValue<>( Arez.context(),
                           null,
                           ValueUtil.randomString(),
                           function,
                           Priority.NORMAL,
                           false,
                           false,
                           true );

    observer.getDependencies().add( computedValue2.getObservableValue() );
    computedValue2.getObservableValue().addObserver( observer );

    // Set it as state error so should not trigger a change in container
    computedValue2.setError( new IllegalStateException() );

    assertEquals( observer.shouldCompute(), false );

    assertEquals( observer.getState(), ObserverState.POSSIBLY_STALE );
  }

  @Test
  public void shouldCompute_POSSIBLY_STALE_whereDependencyRecomputesButDoesNotChange()
    throws Exception
  {
    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final ComputedValue<String> computedValue =
      new ComputedValue<>( Arez.context(),
                           null,
                           ValueUtil.randomString(),
                           function,
                           Priority.NORMAL,
                           false,
                           false,
                           true );

    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final ComputedValue<String> computedValue2 =
      new ComputedValue<>( Arez.context(),
                           null,
                           ValueUtil.randomString(),
                           function,
                           Priority.NORMAL,
                           false,
                           false,
                           true );

    observer.getDependencies().add( computedValue2.getObservableValue() );
    computedValue2.getObservableValue().addObserver( observer );

    // Set it to same so no change
    computedValue2.setValue( "" );

    assertEquals( observer.shouldCompute(), false );

    assertEquals( computedValue2.getObserver().getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void shouldCompute_POSSIBLY_STALE_ignoresNonComputedDependencies()
    throws Exception
  {
    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final ComputedValue<String> computedValue =
      new ComputedValue<>( Arez.context(),
                           null,
                           ValueUtil.randomString(),
                           function,
                           Priority.NORMAL,
                           false,
                           false,
                           true );

    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final ObservableValue<?> observableValue = Arez.context().observable();
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

    setupReadWriteTransaction();
    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            null,
                                            TransactionMode.READ_ONLY,
                                            new CountingProcedure(),
                                            new CountingProcedure(),
                                            Priority.NORMAL,
                                            false,
                                            false,
                                            true,
                                            false,
                                            false );
    observer.setState( ObserverState.UP_TO_DATE );
    assertEquals( observer.isScheduled(), false );

    observer.reportStale();

    assertEquals( observer.isScheduled(), true );
    assertEquals( observer.getState(), ObserverState.STALE );
    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
    assertEquals( context.getScheduler().getPendingObservers().contains( observer ), true );
  }

  @Test
  public void reportStale_arezOnlyDependencies()
    throws Exception
  {
    final ArezContext context = Arez.context();

    setupReadWriteTransaction();
    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            null,
                                            TransactionMode.READ_ONLY,
                                            new CountingProcedure(),
                                            new CountingProcedure(),
                                            Priority.NORMAL,
                                            false,
                                            false,
                                            true,
                                            true,
                                            false );
    observer.setState( ObserverState.UP_TO_DATE );
    assertEquals( observer.isScheduled(), false );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::reportStale );

    assertEquals( exception.getMessage(),
                  "Arez-0199: Observer.reportStale() invoked on observer named '" + observer.getName() +
                  "' but arezOnlyDependencies = true." );
    assertEquals( observer.isScheduled(), false );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );
  }

  @Test
  public void reportStale_noTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    setupReadWriteTransaction();
    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            null,
                                            TransactionMode.READ_ONLY,
                                            new CountingProcedure(),
                                            new CountingProcedure(),
                                            Priority.NORMAL,
                                            false,
                                            false,
                                            true,
                                            false,
                                            false );
    observer.setState( ObserverState.UP_TO_DATE );
    assertEquals( observer.isScheduled(), false );

    Transaction.setTransaction( null );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::reportStale );

    assertEquals( exception.getMessage(),
                  "Arez-0200: Observer.reportStale() invoked on observer named '" + observer.getName() +
                  "' when there is no active transaction." );
    assertEquals( observer.isScheduled(), false );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );
  }

  @Test
  public void reportStale_readOnlyTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer observer =
      context.autorun( null, null, false, new CountingProcedure(), Priority.NORMAL, true, false, false, false, null );

    context.safeAction( null, false, false, () -> observer.setState( ObserverState.UP_TO_DATE ) );
    assertEquals( observer.isScheduled(), false );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> context.safeAction( "MyAction", false, observer::reportStale ) );

    assertEquals( exception.getMessage(),
                  "Arez-0201: Observer.reportStale() invoked on observer named '" + observer.getName() +
                  "' when the active transaction 'MyAction' is READ_ONLY rather " +
                  "than READ_WRITE." );
    assertEquals( observer.isScheduled(), false );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );
  }

  @Test
  public void schedule()
    throws Exception
  {
    final ArezContext context = Arez.context();

    setupReadWriteTransaction();
    final CountingProcedure trackedExecutable = new CountingProcedure();
    final CountingProcedure onDepsUpdated = new CountingProcedure();
    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            null,
                                            TransactionMode.READ_ONLY,
                                            trackedExecutable,
                                            onDepsUpdated,
                                            Priority.NORMAL,
                                            false,
                                            false,
                                            true,
                                            false,
                                            true );
    observer.setState( ObserverState.STALE );

    // reset the scheduling that occurred due to setState
    observer.clearScheduledFlag();
    context.getScheduler().getPendingObservers().clear();

    assertEquals( observer.isScheduled(), false );
    Transaction.setTransaction( null );

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

    setupReadWriteTransaction();
    final CountingProcedure trackedExecutable = new CountingProcedure();
    final CountingProcedure onDepsUpdated = new CountingProcedure();
    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            null,
                                            TransactionMode.READ_ONLY,
                                            trackedExecutable,
                                            onDepsUpdated,
                                            Priority.NORMAL,
                                            false,
                                            false,
                                            true,
                                            false,
                                            true );
    observer.setState( ObserverState.UP_TO_DATE );
    assertEquals( observer.isScheduled(), false );
    Transaction.setTransaction( null );

    final Disposable schedulerLock = context.pauseScheduler();

    observer.schedule();

    assertEquals( observer.isScheduled(), true );

    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
    assertEquals( context.getScheduler().getPendingObservers().contains( observer ), true );

    schedulerLock.dispose();

    assertEquals( observer.isScheduled(), false );
    assertEquals( trackedExecutable.getCallCount(), 0 );
    assertEquals( onDepsUpdated.getCallCount(), 0 );
  }

  @Test
  public void schedule_onNonManualScheduleObserver()
    throws Exception
  {
    final ArezContext context = Arez.context();

    setupReadWriteTransaction();
    final CountingProcedure trackedExecutable = new CountingProcedure();
    final CountingProcedure onDepsUpdated = new CountingProcedure();
    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            null,
                                            TransactionMode.READ_ONLY,
                                            trackedExecutable,
                                            onDepsUpdated,
                                            Priority.NORMAL,
                                            false,
                                            false,
                                            true,
                                            false,
                                            false );
    observer.setState( ObserverState.STALE );

    // reset the scheduling that occurred due to setState
    observer.clearScheduledFlag();
    context.getScheduler().getPendingObservers().clear();

    assertEquals( observer.isScheduled(), false );
    Transaction.setTransaction( null );

    final Disposable schedulerLock = context.pauseScheduler();

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::schedule );

    assertEquals( exception.getMessage(),
                  "Arez-0202: Observer.schedule() invoked on observer named '" + observer.getName() +
                  "' but supportsManualSchedule = false." );
    assertEquals( observer.isScheduled(), false );
    assertEquals( observer.getState(), ObserverState.STALE );
    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );

    schedulerLock.dispose();

    assertEquals( trackedExecutable.getCallCount(), 0 );
    assertEquals( onDepsUpdated.getCallCount(), 0 );
  }
}
