package arez;

import arez.spy.ActionCompletedEvent;
import arez.spy.ActionStartedEvent;
import arez.spy.ComputeCompletedEvent;
import arez.spy.ComputeStartedEvent;
import arez.spy.ComputedValueDeactivatedEvent;
import arez.spy.ComputedValueDisposedEvent;
import arez.spy.ObservableChangedEvent;
import arez.spy.ObserverDisposedEvent;
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
    final Reaction reaction = new TestReaction();
    final Observer observer =
      new Observer( context, null, name, null, TransactionMode.READ_ONLY, reaction, Priority.NORMAL, false, false );

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
    assertEquals( observer.getReaction(), reaction );
    assertEquals( observer.isScheduled(), false );

    assertEquals( observer.isDerivation(), false );

    assertEquals( observer.getPriority(), Priority.NORMAL );
    assertEquals( observer.canObserveLowerPriorityDependencies(), false );

    // All the hooks start out null
    assertEquals( observer.getOnActivate(), null );
    assertEquals( observer.getOnDeactivate(), null );
    assertEquals( observer.getOnStale(), null );

    final Procedure onActivate = new NoopProcedure();
    final Procedure onDeactivate = new NoopProcedure();
    final Procedure onStale = new NoopProcedure();

    // Ensure hooks can be modified
    observer.setOnActivate( onActivate );
    observer.setOnDeactivate( onDeactivate );
    observer.setOnStale( onStale );

    assertEquals( observer.getOnActivate(), onActivate );
    assertEquals( observer.getOnDeactivate(), onDeactivate );
    assertEquals( observer.getOnStale(), onStale );

    observer.invariantState();

    assertEquals( context.getTopLevelObservers().get( observer.getName() ), observer );
  }

  @Test
  public void initialStateForDerivation()
    throws Exception
  {
    final Observer observer = newDerivation();

    assertEquals( observer.isDerivation(), true );

    assertEquals( observer.getDerivedValue().getName(), observer.getName() );
    assertEquals( observer.getDerivedValue().getOwner(), observer );

    assertEquals( observer.getComputedValue().getName(), observer.getName() );
    assertEquals( observer.getComputedValue().getObserver(), observer );
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
                                        new TestReaction(),
                                        Priority.NORMAL,
                                        false,
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
                                        new TestReaction(),
                                        Priority.LOWEST,
                                        false,
                                        true ) );

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
                                        new TestReaction(),
                                        Priority.NORMAL,
                                        false,
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
      new Observer( Arez.context(), null, name, null, null, new TestReaction(), Priority.NORMAL, false, false );
    assertThrows( observer::getMode );
  }

  @Test
  public void construct_with_READ_ONLY_but_ComputableValue()
    throws Exception
  {
    final String name = ValueUtil.randomString();

    final ComputedValue<?> computedValue = newDerivation().getComputedValue();
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( Arez.context(),
                                        null,
                                        name,
                                        computedValue,
                                        TransactionMode.READ_ONLY,
                                        new TestReaction(),
                                        Priority.NORMAL,
                                        false,
                                        false ) );

    assertEquals( exception.getMessage(),
                  "Arez-0081: Attempted to construct an observer named '" + name + "' with READ_ONLY " +
                  "transaction mode and a ComputedValue." );
  }

  @Test
  public void construct_with_canExplicitlyTrack_and_ComputableValue()
    throws Exception
  {
    final ComputedValue<?> computedValue = newDerivation().getComputedValue();
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( Arez.context(),
                                        null,
                                        computedValue.getName(),
                                        computedValue,
                                        TransactionMode.READ_WRITE_OWNED,
                                        new TestReaction(),
                                        Priority.NORMAL,
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
                                        new TestReaction(),
                                        Priority.NORMAL,
                                        false,
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
                    new TestReaction(),
                    Priority.NORMAL,
                    false,
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
    final Observer observer = newReadOnlyObserver();
    setCurrentTransaction( observer );

    final Observable<?> observable = newObservable();
    observer.getDependencies().add( observable );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observer.invariantDependenciesBackLink( "TEST1" ) );

    assertEquals( exception.getMessage(),
                  "Arez-0090: TEST1: Observer named '" + observer.getName() + "' has dependency observable named '" +
                  observable.getName() + "' which does not contain the observer in the list of observers." );

    //Setup correct back link
    observable.addObserver( observer );

    // Back link created so should be good
    observer.invariantDependenciesBackLink( "TEST2" );
  }

  @Test
  public void invariantDependenciesNotDisposed()
    throws Exception
  {
    final Observer observer = newReadOnlyObserver();
    setCurrentTransaction( observer );

    final Observable<?> observable = newObservable();
    observer.getDependencies().add( observable );
    observable.addObserver( observer );

    observable.setWorkState( Observable.DISPOSED );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observer::invariantDependenciesNotDisposed );

    assertEquals( exception.getMessage(),
                  "Arez-0091: Observer named '" + observer.getName() + "' has dependency observable named '" +
                  observable.getName() + "' which is disposed." );

    observable.setWorkState( 0 );

    observer.invariantDependenciesNotDisposed();
  }

  @Test
  public void invariantDependenciesUnique()
    throws Exception
  {
    final Observer observer = newReadOnlyObserver();

    final Observable<?> observable = newObservable();

    observer.getDependencies().add( observable );

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
    final Observer observer = newReadOnlyObserver();

    observer.invariantState();

    final Observable<?> observable = newObservable();
    observer.getDependencies().add( observable );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observer::invariantState );

    assertEquals( exception.getMessage(),
                  "Arez-0092: Observer named '" + observer.getName() + "' is inactive " +
                  "but still has dependencies: [" + observable.getName() + "]." );
  }

  @Test
  public void invariantState_derivedNotLinkBack()
    throws Exception
  {
    final Observer observer = newDerivation();

    observer.invariantState();

    setField( observer, "_derivedValue", newObservable() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observer::invariantState );

    assertEquals( exception.getMessage(),
                  "Arez-0093: Observer named '" + observer.getName() + "' has a derived value " +
                  "that does not link back to observer." );
  }

  @Test
  public void invariantDerivationState()
    throws Exception
  {
    final Observer observer = newDerivation();

    observer.invariantDerivationState();

    setCurrentTransaction( newReadOnlyObserver() );

    observer.setState( ObserverState.UP_TO_DATE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observer::invariantDerivationState );

    assertEquals( exception.getMessage(),
                  "Arez-0094: Observer named '" + observer.getName() + "' is a derivation and " +
                  "active but the derived value has no observers." );

    ensureDerivationHasObserver( observer );

    observer.invariantDerivationState();
  }

  @Test
  public void invariantDerivationState_observerCanHaveNoDependenciesIfItIsTracker()
    throws Exception
  {
    final Observer observer = newDerivation();

    observer.invariantDerivationState();

    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    observer.invariantDerivationState();
  }

  @Test
  public void replaceDependencies()
    throws Exception
  {
    final Observer observer = newReadOnlyObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final ArrayList<Observable<?>> originalDependencies = observer.getDependencies();

    assertEquals( originalDependencies.isEmpty(), true );

    final Observable<?> observable = newObservable();

    final ArrayList<Observable<?>> newDependencies = new ArrayList<>();
    newDependencies.add( observable );
    observable.addObserver( observer );

    observer.replaceDependencies( newDependencies );

    assertEquals( observer.getDependencies().size(), 1 );
    assertTrue( observer.getDependencies() != originalDependencies );
    assertTrue( observer.getDependencies().contains( observable ) );
  }

  @Test
  public void replaceDependencies_duplicateDependency()
    throws Exception
  {
    final Observer observer = newReadOnlyObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final ArrayList<Observable<?>> originalDependencies = observer.getDependencies();

    assertEquals( originalDependencies.isEmpty(), true );

    final Observable<?> observable = newObservable();

    final ArrayList<Observable<?>> newDependencies = new ArrayList<>();
    newDependencies.add( observable );
    newDependencies.add( observable );
    observable.addObserver( observer );

    assertThrows( () -> observer.replaceDependencies( newDependencies ) );
  }

  @Test
  public void replaceDependencies_notBacklinedDependency()
    throws Exception
  {
    final Observer observer = newReadOnlyObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final ArrayList<Observable<?>> originalDependencies = observer.getDependencies();

    assertEquals( originalDependencies.isEmpty(), true );

    final Observable<?> observable = newObservable();

    final ArrayList<Observable<?>> newDependencies = new ArrayList<>();
    newDependencies.add( observable );

    assertThrows( () -> observer.replaceDependencies( newDependencies ) );
  }

  @Test
  public void clearDependencies()
    throws Exception
  {
    final Observer observer = newReadOnlyObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable1 = newObservable();
    final Observable<?> observable2 = newObservable();

    observer.getDependencies().add( observable1 );
    observer.getDependencies().add( observable2 );
    observable1.addObserver( observer );
    observable2.addObserver( observer );

    assertEquals( observer.getDependencies().size(), 2 );
    assertEquals( observable1.getObservers().size(), 1 );
    assertEquals( observable2.getObservers().size(), 1 );

    observer.clearDependencies();

    assertEquals( observer.getDependencies().size(), 0 );
    assertEquals( observable1.getObservers().size(), 0 );
    assertEquals( observable2.getObservers().size(), 0 );
  }

  @Test
  public void runHook_nullHook()
    throws Exception
  {
    final Observer observer = newReadOnlyObserver( Arez.context() );

    observer.runHook( null, ObserverError.ON_ACTIVATE_ERROR );
  }

  @Test
  public void runHook_normalHook()
    throws Exception
  {
    final Observer observer = newReadOnlyObserver();

    final AtomicInteger counter = new AtomicInteger();

    observer.runHook( counter::incrementAndGet, ObserverError.ON_ACTIVATE_ERROR );

    assertEquals( counter.get(), 1 );
  }

  @Test
  public void runHook_hookThrowsException()
    throws Exception
  {
    setIgnoreObserverErrors( true );
    setPrintObserverErrors( false );

    final Observer observer = newReadOnlyObserver();

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
    setupReadWriteTransaction();

    final Observer observer = newReadOnlyObserver();

    final TestProcedure onActivate = new TestProcedure();
    final TestProcedure onDeactivate = new TestProcedure();
    final TestProcedure onStale = new TestProcedure();

    observer.setOnActivate( onActivate );
    observer.setOnDeactivate( onDeactivate );
    observer.setOnStale( onStale );

    assertEquals( observer.getState(), ObserverState.INACTIVE );

    observer.setState( ObserverState.INACTIVE );

    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( observer.isScheduled(), false );
    assertEquals( onActivate.getCalls(), 0 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 0 );

    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.isScheduled(), false );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 0 );

    observer.setState( ObserverState.POSSIBLY_STALE );

    assertEquals( observer.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer.isScheduled(), false );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 0 );

    observer.clearScheduledFlag();
    Arez.context().getScheduler().getPendingObservers().truncate( 0 );
    assertEquals( observer.isScheduled(), false );

    observer.setState( ObserverState.STALE );
    assertEquals( observer.getState(), ObserverState.STALE );
    assertEquals( observer.isScheduled(), true );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 1 );

    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 1 );

    observer.setState( ObserverState.STALE );

    assertEquals( observer.getState(), ObserverState.STALE );
    assertEquals( observer.isScheduled(), true );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 2 );

    final Observable<?> observable1 = newObservable();
    final Observable<?> observable2 = newObservable();

    observer.getDependencies().add( observable1 );
    observer.getDependencies().add( observable2 );
    observable1.addObserver( observer );
    observable2.addObserver( observer );

    assertEquals( observer.getDependencies().size(), 2 );
    assertEquals( observable1.getObservers().size(), 1 );
    assertEquals( observable2.getObservers().size(), 1 );

    observer.setState( ObserverState.INACTIVE );

    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 1 );
    assertEquals( onStale.getCalls(), 2 );

    assertEquals( observer.getDependencies().size(), 0 );
    assertEquals( observable1.getObservers().size(), 0 );
    assertEquals( observable2.getObservers().size(), 0 );
  }

  @Test
  public void setState_scheduleFalse()
    throws Exception
  {
    setupReadWriteTransaction();

    final Observer observer = newReadOnlyObserver();

    final TestProcedure onActivate = new TestProcedure();
    final TestProcedure onDeactivate = new TestProcedure();
    final TestProcedure onStale = new TestProcedure();

    observer.setOnActivate( onActivate );
    observer.setOnDeactivate( onDeactivate );
    observer.setOnStale( onStale );

    assertEquals( observer.getState(), ObserverState.INACTIVE );

    observer.setState( ObserverState.INACTIVE, false );

    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( observer.isScheduled(), false );
    assertEquals( onActivate.getCalls(), 0 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 0 );

    observer.setState( ObserverState.UP_TO_DATE, false );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.isScheduled(), false );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 0 );

    observer.setState( ObserverState.POSSIBLY_STALE, false );

    assertEquals( observer.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer.isScheduled(), false );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 0 );

    observer.clearScheduledFlag();
    Arez.context().getScheduler().getPendingObservers().truncate( 0 );
    assertEquals( observer.isScheduled(), false );

    observer.setState( ObserverState.STALE, false );
    assertEquals( observer.getState(), ObserverState.STALE );
    assertEquals( observer.isScheduled(), false );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 1 );

    observer.setState( ObserverState.UP_TO_DATE, false );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 1 );

    observer.setState( ObserverState.STALE, false );

    assertEquals( observer.getState(), ObserverState.STALE );
    assertEquals( observer.isScheduled(), false );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 2 );

    final Observable<?> observable1 = newObservable();
    final Observable<?> observable2 = newObservable();

    observer.getDependencies().add( observable1 );
    observer.getDependencies().add( observable2 );
    observable1.addObserver( observer );
    observable2.addObserver( observer );

    assertEquals( observer.getDependencies().size(), 2 );
    assertEquals( observable1.getObservers().size(), 1 );
    assertEquals( observable2.getObservers().size(), 1 );

    observer.setState( ObserverState.INACTIVE, false );

    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 1 );
    assertEquals( onStale.getCalls(), 2 );

    assertEquals( observer.getDependencies().size(), 0 );
    assertEquals( observable1.getObservers().size(), 0 );
    assertEquals( observable2.getObservers().size(), 0 );
  }

  @Test
  public void setState_onComputedValue()
    throws Exception
  {
    setupReadWriteTransaction();

    final ComputedValue<String> computedValue = newComputedValue();
    final Observable<String> derivedValue = computedValue.getObservable();
    final Observer observer = computedValue.getObserver();

    final Observer watcher = ensureDerivationHasObserver( observer );
    watcher.setState( ObserverState.UP_TO_DATE );

    final TestProcedure onActivate = new TestProcedure();
    final TestProcedure onDeactivate = new TestProcedure();
    final TestProcedure onStale = new TestProcedure();

    observer.setOnActivate( onActivate );
    observer.setOnDeactivate( onDeactivate );
    observer.setOnStale( onStale );

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
    final Observer observer = newDerivation();
    setCurrentTransaction( observer );

    assertEquals( observer.getState(), ObserverState.INACTIVE );

    observer.setDisposed( true );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observer.setState( ObserverState.UP_TO_DATE ) );

    assertEquals( exception.getMessage(),
                  "Arez-0087: Attempted to activate disposed observer named '" + observer.getName() + "'." );

    observer.setDisposed( false );

    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void setState_noTransaction()
    throws Exception
  {
    final Observer observer = newReadOnlyObserver();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observer.setState( ObserverState.UP_TO_DATE ) );

    assertEquals( exception.getMessage(),
                  "Arez-0086: Attempt to invoke setState on observer named '" + observer.getName() + "' when " +
                  "there is no active transaction." );
  }

  @Test
  public void schedule()
    throws Exception
  {
    setupReadWriteTransaction();
    final Observer observer = newReadOnlyObserver();

    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.isScheduled(), false );

    observer.schedule();

    final ArezContext context = Arez.context();

    assertEquals( observer.isScheduled(), true );
    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
    assertEquals( context.getScheduler().getPendingObservers().contains( observer ), true );

    //Duplicate schedule should not result in it being added again
    observer.schedule();

    assertEquals( observer.isScheduled(), true );
    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
    assertEquals( context.getScheduler().getPendingObservers().contains( observer ), true );
  }

  @Test
  public void schedule_when_Disposed()
    throws Exception
  {
    setupReadWriteTransaction();

    final Observer observer = newReadOnlyObserver();

    observer.setState( ObserverState.UP_TO_DATE );
    observer.setDisposed( true );

    assertEquals( observer.isScheduled(), false );

    observer.schedule();

    assertEquals( observer.isScheduled(), false );

    observer.setDisposed( false );

    observer.schedule();

    assertEquals( observer.isScheduled(), true );
  }

  @Test
  public void schedule_whenInactive()
    throws Exception
  {
    final Observer observer = newReadOnlyObserver();
    setCurrentTransaction( observer );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::schedule );

    assertEquals( exception.getMessage(),
                  "Arez-0088: Observer named '" + observer.getName() + "' is not active but an attempt has " +
                  "been made to schedule observer." );
  }

  @Test
  public void dispose()
    throws Exception
  {
    final Observer observer = newReadOnlyObserver();
    setCurrentTransaction( observer );
    observer.setState( ObserverState.UP_TO_DATE );

    Transaction.setTransaction( null );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.isNotDisposed(), true );
    assertEquals( observer.isDisposed(), false );

    observer.dispose();

    assertEquals( observer.getState(), ObserverState.INACTIVE );
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
    final Observer observer = newDerivation();
    setCurrentTransaction( observer );
    observer.setState( ObserverState.STALE );

    final Observable<?> observable = newObservable();
    observable.setLeastStaleObserverState( ObserverState.STALE );
    observable.getObservers().add( observer );
    observer.getDependencies().add( observable );

    Transaction.setTransaction( null );

    assertEquals( observer.getState(), ObserverState.STALE );
    assertEquals( observer.isNotDisposed(), true );
    assertEquals( observer.isDisposed(), false );
    assertEquals( observable.getObservers().size(), 1 );

    observer.dispose();

    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( observer.isNotDisposed(), false );
    assertEquals( observer.isDisposed(), true );
    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

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
    final Observer observer = newDerivation();
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
    final Observer observer = newReadOnlyObserver();
    setCurrentTransaction( observer );
    observer.setState( ObserverState.UP_TO_DATE );

    Transaction.setTransaction( null );

    assertEquals( observer.isDisposed(), false );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    Arez.context().getSpy().addSpyEventHandler( handler );

    observer.dispose();

    assertEquals( observer.isDisposed(), true );

    handler.assertEventCount( 5 );
    handler.assertEvent( ActionStartedEvent.class, 0 );
    handler.assertEvent( TransactionStartedEvent.class, 1 );
    handler.assertEvent( TransactionCompletedEvent.class, 2 );
    handler.assertEvent( ActionCompletedEvent.class, 3 );
    final ObserverDisposedEvent event = handler.assertEvent( ObserverDisposedEvent.class, 4 );
    assertEquals( event.getObserver().getName(), observer.getName() );
  }

  @Test
  public void dispose_does_not_generate_spyEvent_forDerivedValues()
    throws Exception
  {
    final Observer observer = newDerivation();
    final Observable<?> derivedValue = observer.getDerivedValue();
    final ComputedValue<?> computedValue = observer.getComputedValue();
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

    // This is the part that disposes the associated Observable
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( ObservableChangedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class );
  }

  @Test
  public void dispose_via_ComputedValue()
    throws Exception
  {
    setIgnoreObserverErrors( true );
    setPrintObserverErrors( false );

    final AtomicBoolean hasErrorOccurred = new AtomicBoolean();
    hasErrorOccurred.set( false );
    Arez.context().addObserverErrorHandler( ( observer, error, throwable ) -> {
      hasErrorOccurred.set( true );
      fail();
    } );
    final Observer observer = newDerivation();
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

    // This is the part that disposes the Observer
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class );

    // This is the part that disposes the associated Observable
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( ObservableChangedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class );

    // Ensure no observer errors occur
    assertFalse( hasErrorOccurred.get() );
  }

  @Test
  public void getDerivedValue_throwsExceptionWhenNotDerived()
    throws Exception
  {
    final Observer observer = newReadOnlyObserver();

    assertEquals( observer.isDerivation(), false );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::getDerivedValue );

    assertEquals( exception.getMessage(),
                  "Arez-0085: Attempted to invoke getDerivedValue on observer named '" + observer.getName() +
                  "' when is not a computed observer." );
  }

  @Test
  public void getDerivedValue_onDisposedObserver()
    throws Exception
  {
    final Observer observer = newDerivation();

    observer.setDisposed( true );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::getDerivedValue );

    assertEquals( exception.getMessage(),
                  "Arez-0084: Attempted to invoke getDerivedValue on disposed observer named '" +
                  observer.getName() + "'." );

    observer.setDisposed( false );

    assertEquals( observer.getDerivedValue().getName(), observer.getName() );
  }

  @Test
  public void getComputedValue_throwsExceptionWhenNotDerived()
    throws Exception
  {
    final Observer observer = newReadOnlyObserver();

    assertEquals( observer.isDerivation(), false );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::getComputedValue );

    assertEquals( exception.getMessage(),
                  "Arez-0095: Attempted to invoke getComputedValue on observer named '" + observer.getName() +
                  "' when is not a computed observer." );
  }

  @Test
  public void getComputedValue_whenDisposed()
    throws Exception
  {
    final Observer observer = newDerivation();

    observer.setDisposed( true );

    // Should be able to do this because sometimes when we dispose ComputedValue it gets deactivated and
    // part of dispose of observer needs to access ComputedValue to send out a spy message
    assertEquals( observer.getComputedValue().getName(), observer.getName() );
  }

  @Test
  public void markDependenciesLeastStaleObserverAsUpToDate()
  {
    final Observer observer = newReadOnlyObserver();

    final Observable<?> observable1 = newObservable();
    final Observable<?> observable2 = newObservable();
    final Observable<?> observable3 = newObservable();

    observer.getDependencies().add( observable1 );
    observer.getDependencies().add( observable2 );
    observer.getDependencies().add( observable3 );

    setCurrentTransaction( observer );

    observable1.addObserver( observer );
    observable2.addObserver( observer );
    observable3.addObserver( observer );

    observable1.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observable2.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );
    observable3.setLeastStaleObserverState( ObserverState.STALE );

    observer.markDependenciesLeastStaleObserverAsUpToDate();

    assertEquals( observable1.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observable2.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observable3.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void invokeReaction()
    throws Exception
  {
    setIgnoreObserverErrors( true );
    setPrintObserverErrors( false );

    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final TransactionMode mode = TransactionMode.READ_ONLY;

    final AtomicInteger callCount = new AtomicInteger();
    final AtomicInteger errorCount = new AtomicInteger();

    context.addObserverErrorHandler( ( observer, error, throwable ) -> errorCount.incrementAndGet() );

    final Reaction reaction = observer -> {
      callCount.incrementAndGet();
      assertEquals( observer.getContext(), context );
      assertEquals( context.isTransactionActive(), false );
      assertEquals( observer.getName(), name );
    };

    final Observer observer = new Observer( context, null, name, null, mode, reaction, Priority.NORMAL, false, false );

    observer.invokeReaction();

    assertEquals( callCount.get(), 1 );
    assertEquals( errorCount.get(), 0 );
  }

  @Test
  public void invokeReaction_Observer_SpyEventHandlerPresent()
    throws Exception
  {
    final TestSpyEventHandler handler = new TestSpyEventHandler();
    Arez.context().getSpy().addSpyEventHandler( handler );

    final Observer observer =
      new Observer( Arez.context(),
                    null,
                    ValueUtil.randomString(),
                    null,
                    TransactionMode.READ_ONLY,
                    o -> Thread.sleep( 1 ),
                    Priority.NORMAL,
                    false,
                    false );

    observer.invokeReaction();

    handler.assertEventCount( 2 );

    {
      final ReactionStartedEvent event = handler.assertEvent( ReactionStartedEvent.class, 0 );
      assertEquals( event.getObserver().getName(), observer.getName() );
    }
    {
      final ReactionCompletedEvent event = handler.assertEvent( ReactionCompletedEvent.class, 1 );
      assertEquals( event.getObserver().getName(), observer.getName() );
      assertTrue( event.getDuration() > 0 );
    }
  }

  @SuppressWarnings( "ConstantConditions" )
  @Test
  public void invokeReaction_ComputedValue_SpyEventHandlerPresent()
    throws Exception
  {
    final Observable<Object> observable = Arez.context().observable();
    final TestSpyEventHandler handler = new TestSpyEventHandler();
    Arez.context().getSpy().addSpyEventHandler( handler );

    final SafeFunction<Integer> function = () -> {
      observable.reportObserved();
      return 1;
    };
    final ComputedValue<Integer> computedValue =
      new ComputedValue<>( Arez.context(), null, ValueUtil.randomString(), function, Priority.NORMAL, false, false );

    computedValue.getObserver().invokeReaction();

    handler.assertEventCount( 6 );

    {
      final ComputeStartedEvent event = handler.assertEvent( ComputeStartedEvent.class, 0 );
      assertEquals( event.getComputedValue().getName(), computedValue.getName() );
    }
    handler.assertEvent( TransactionStartedEvent.class, 1 );
    {
      final ObservableChangedEvent event = handler.assertEvent( ObservableChangedEvent.class, 2 );
      assertEquals( event.getObservable().getName(), computedValue.getObservable().getName() );
    }
    handler.assertEvent( ComputedValueDeactivatedEvent.class, 3 );
    handler.assertEvent( TransactionCompletedEvent.class, 4 );
    {
      final ComputeCompletedEvent event = handler.assertEvent( ComputeCompletedEvent.class, 5 );
      assertEquals( event.getComputedValue().getName(), computedValue.getName() );
      assertTrue( event.getDuration() >= 0 );
    }
  }

  @Test
  public void invokeReaction_onDisposedObserver()
    throws Exception
  {
    final TestReaction reaction = new TestReaction();
    final Observer observer =
      new Observer( Arez.context(),
                    null,
                    ValueUtil.randomString(),
                    null,
                    TransactionMode.READ_ONLY,
                    reaction,
                    Priority.NORMAL,
                    false,
                    false );

    observer.setDisposed( true );

    observer.invokeReaction();

    assertEquals( reaction.getCallCount(), 0 );

    observer.setDisposed( false );

    observer.invokeReaction();

    assertEquals( reaction.getCallCount(), 1 );
  }

  @Test
  public void invokeReaction_onUpToDateObserver()
    throws Exception
  {
    final TestReaction reaction = new TestReaction();
    final Observer observer =
      new Observer( Arez.context(),
                    null,
                    ValueUtil.randomString(),
                    null,
                    TransactionMode.READ_ONLY,
                    reaction,
                    Priority.NORMAL,
                    false,
                    false );

    setupReadWriteTransaction();
    observer.setState( ObserverState.UP_TO_DATE );
    Transaction.setTransaction( null );

    //Invoke reaction
    observer.invokeReaction();

    assertEquals( reaction.getCallCount(), 0 );
  }

  @Test
  public void invokeReaction_reactionGeneratesError()
    throws Exception
  {
    setIgnoreObserverErrors( true );
    setPrintObserverErrors( false );

    final String name = ValueUtil.randomString();
    final TransactionMode mode = TransactionMode.READ_ONLY;

    final AtomicInteger callCount = new AtomicInteger();
    final AtomicInteger errorCount = new AtomicInteger();

    final RuntimeException exception = new RuntimeException( "X" );

    Arez.context().addObserverErrorHandler( ( observer, error, throwable ) -> {
      errorCount.incrementAndGet();
      assertEquals( error, ObserverError.REACTION_ERROR );
      assertEquals( throwable, exception );
    } );

    final Reaction reaction = observer -> {
      callCount.incrementAndGet();
      throw exception;
    };

    final Observer observer =
      new Observer( Arez.context(),
                    null,
                    name,
                    null,
                    mode,
                    reaction,
                    Priority.NORMAL,
                    false,
                    false );

    observer.invokeReaction();

    assertEquals( callCount.get(), 1 );
    assertEquals( errorCount.get(), 1 );
  }

  @Test
  public void shouldCompute_UP_TO_DATE()
    throws Exception
  {
    final Observer observer = newReadOnlyObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.shouldCompute(), false );
  }

  @Test
  public void shouldCompute_INACTIVE()
    throws Exception
  {
    final Observer observer = newReadOnlyObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.INACTIVE );

    assertEquals( observer.shouldCompute(), true );
  }

  @Test
  public void shouldCompute_STALE()
    throws Exception
  {
    setupReadWriteTransaction();

    final Observer observer = newReadOnlyObserver();

    observer.setState( ObserverState.STALE );

    assertEquals( observer.shouldCompute(), true );
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
      new ComputedValue<>( context, null, ValueUtil.randomString(), function, Priority.NORMAL, false, false );

    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final SafeFunction<String> function2 = () -> {
      observeADependency();
      return "";
    };
    final ComputedValue<String> computedValue2 =
      new ComputedValue<>( context, null, ValueUtil.randomString(), function2, Priority.NORMAL, false, false );

    observer.getDependencies().add( computedValue2.getObservable() );
    computedValue2.getObservable().addObserver( observer );

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
    setPrintObserverErrors( false );

    final ArezContext context = Arez.context();
    final SafeFunction<String> function1 = () -> {
      observeADependency();
      return ValueUtil.randomString();
    };
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), function1, Priority.NORMAL, false, false );
    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final SafeFunction<String> function = () -> {
      observeADependency();
      throw new IllegalStateException();
    };
    final ComputedValue<String> computedValue2 =
      new ComputedValue<>( context, null, ValueUtil.randomString(), function, Priority.NORMAL, false, false );

    observer.getDependencies().add( computedValue2.getObservable() );
    computedValue2.getObservable().addObserver( observer );

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
    setPrintObserverErrors( false );

    final SafeFunction<String> function1 = () -> {
      observeADependency();
      return ValueUtil.randomString();
    };
    final ComputedValue<String> computedValue =
      new ComputedValue<>( Arez.context(), null, ValueUtil.randomString(), function1, Priority.NORMAL, false, false );
    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final SafeFunction<String> function = () -> {
      observeADependency();
      throw new IllegalStateException();
    };
    final ComputedValue<String> computedValue2 =
      new ComputedValue<>( Arez.context(), null, ValueUtil.randomString(), function, Priority.NORMAL, false, false );

    observer.getDependencies().add( computedValue2.getObservable() );
    computedValue2.getObservable().addObserver( observer );

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
      new ComputedValue<>( Arez.context(), null, ValueUtil.randomString(), function, Priority.NORMAL, false, false );

    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final ComputedValue<String> computedValue2 =
      new ComputedValue<>( Arez.context(), null, ValueUtil.randomString(), function, Priority.NORMAL, false, false );

    observer.getDependencies().add( computedValue2.getObservable() );
    computedValue2.getObservable().addObserver( observer );

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
      new ComputedValue<>( Arez.context(), null, ValueUtil.randomString(), function, Priority.NORMAL, false, false );

    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final Observable<?> observable = newObservable();
    observer.getDependencies().add( observable );
    observable.addObserver( observer );

    assertEquals( observer.shouldCompute(), false );
  }
}
