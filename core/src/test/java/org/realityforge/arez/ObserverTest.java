package org.realityforge.arez;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.arez.spy.ActionCompletedEvent;
import org.realityforge.arez.spy.ActionStartedEvent;
import org.realityforge.arez.spy.ComputeCompletedEvent;
import org.realityforge.arez.spy.ComputeStartedEvent;
import org.realityforge.arez.spy.ComputedValueDisposedEvent;
import org.realityforge.arez.spy.ObservableChangedEvent;
import org.realityforge.arez.spy.ObserverDisposedEvent;
import org.realityforge.arez.spy.ReactionCompletedEvent;
import org.realityforge.arez.spy.ReactionStartedEvent;
import org.realityforge.arez.spy.TransactionCompletedEvent;
import org.realityforge.arez.spy.TransactionStartedEvent;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObserverTest
  extends AbstractArezTest
{
  @Test
  public void initialState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();
    final Reaction reaction = new TestReaction();
    final Observer observer = new Observer( context, null, name, null, TransactionMode.READ_ONLY, reaction, false );

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
    final ArezContext context = new ArezContext();
    final Observer observer = newDerivation( context );

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
                    () -> new Observer( new ArezContext(),
                                        null,
                                        name,
                                        null,
                                        TransactionMode.READ_WRITE_OWNED,
                                        new TestReaction(),
                                        false ) );

    assertEquals( exception.getMessage(),
                  "Attempted to construct an observer named '" + name + "' with READ_WRITE_OWNED " +
                  "transaction mode but no ComputedValue." );
  }

  @Test
  public void construct_with_mode_but_checking_DIsabled()
    throws Exception
  {
    ArezTestUtil.noEnforceTransactionType();

    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( new ArezContext(),
                                        null,
                                        name,
                                        null,
                                        TransactionMode.READ_ONLY,
                                        new TestReaction(),
                                        false ) );

    assertEquals( exception.getMessage(),
                  "Observer named '" + name + "' specified mode 'READ_ONLY' " +
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
      new Observer( new ArezContext(), null, name, null, null, new TestReaction(), false );
    assertThrows( observer::getMode );
  }

  @Test
  public void construct_with_READ_ONLY_but_ComputableValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();

    final ComputedValue<?> computedValue = newDerivation( context ).getComputedValue();
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( context,
                                        null,
                                        name,
                                        computedValue,
                                        TransactionMode.READ_ONLY,
                                        new TestReaction(),
                                        false ) );

    assertEquals( exception.getMessage(),
                  "Attempted to construct an observer named '" + name + "' with READ_ONLY " +
                  "transaction mode and a ComputedValue." );
  }

  @Test
  public void construct_with_canExplicitlyTrack_and_ComputableValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final ComputedValue<?> computedValue = newDerivation( context ).getComputedValue();
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( context,
                                        null,
                                        computedValue.getName(),
                                        computedValue,
                                        TransactionMode.READ_WRITE_OWNED,
                                        new TestReaction(),
                                        true ) );

    assertEquals( exception.getMessage(),
                  "Attempted to construct an ComputedValue '" + computedValue.getName() +
                  "' that could track explicitly." );
  }

  @Test
  public void constructWithComponentWhenNativeComponentsDisabled()
    throws Exception
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = new ArezContext();
    final Component component =
      new Component( context,
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     null,
                     null );

    final String name = ValueUtil.randomString();
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( context,
                                        component,
                                        name,
                                        null,
                                        TransactionMode.READ_ONLY,
                                        new TestReaction(),
                                        false ) );
    assertEquals( exception.getMessage(),
                  "Observer named '" + name + "' has component specified but " +
                  "Arez.areNativeComponentsEnabled() is false." );
  }

  @Test
  public void basicLifecycle_withComponent()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Component component =
      new Component( context,
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     null,
                     null );

    final String name = ValueUtil.randomString();
    final Observer observer =
      new Observer( context, component, name, null, TransactionMode.READ_ONLY, new TestReaction(), false );
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
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final Observable<?> observable = newObservable( context );
    observer.getDependencies().add( observable );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observer.invariantDependenciesBackLink( "TEST1" ) );

    assertEquals( exception.getMessage(),
                  "TEST1: Observer named '" + observer.getName() + "' has dependency observable named '" +
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
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final Observable<?> observable = newObservable( context );
    observer.getDependencies().add( observable );
    observable.addObserver( observer );

    observable.setWorkState( Observable.DISPOSED );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observer::invariantDependenciesNotDisposed );

    assertEquals( exception.getMessage(),
                  "Observer named '" + observer.getName() + "' has dependency observable named '" +
                  observable.getName() + "' which is disposed." );

    observable.setWorkState( 0 );

    observer.invariantDependenciesNotDisposed();
  }

  @Test
  public void invariantDependenciesUnique()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    final Observable<?> observable = newObservable( context );

    observer.getDependencies().add( observable );

    observer.invariantDependenciesUnique( "TEST1" );

    // Add a duplicate
    observer.getDependencies().add( observable );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observer.invariantDependenciesUnique( "TEST2" ) );

    assertEquals( exception.getMessage(),
                  "TEST2: The set of dependencies in observer named '" + observer.getName() + "' is " +
                  "not unique. Current list: '[" + observable.getName() + ", " + observable.getName() + "]'." );
  }

  @Test
  public void invariantState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    observer.invariantState();

    final Observable<?> observable = newObservable( context );
    observer.getDependencies().add( observable );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observer::invariantState );

    assertEquals( exception.getMessage(),
                  "Observer named '" + observer.getName() + "' is inactive " +
                  "but still has dependencies: [" + observable.getName() + "]." );
  }

  @Test
  public void invariantState_derivedNotLinkBack()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newDerivation( context );

    observer.invariantState();

    setField( observer, "_derivedValue", newObservable( context ) );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observer::invariantState );

    assertEquals( exception.getMessage(),
                  "Observer named '" + observer.getName() + "' has a derived value " +
                  "that does not link back to observer." );
  }

  @Test
  public void invariantDerivationState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newDerivation( context );

    observer.invariantDerivationState();

    setCurrentTransaction( newReadOnlyObserver( context ) );

    observer.setState( ObserverState.UP_TO_DATE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observer::invariantDerivationState );

    assertEquals( exception.getMessage(),
                  "Observer named '" + observer.getName() + "' is a derivation and " +
                  "active but the derived value has no observers." );

    ensureDerivationHasObserver( observer );

    observer.invariantDerivationState();
  }

  @Test
  public void invariantDerivationState_observerCanHaveNoDependenciesIfItIsTracker()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newDerivation( context );

    observer.invariantDerivationState();

    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    observer.invariantDerivationState();
  }

  @Test
  public void replaceDependencies()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final ArrayList<Observable<?>> originalDependencies = observer.getDependencies();

    assertEquals( originalDependencies.isEmpty(), true );

    final Observable<?> observable = newObservable( context );

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
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final ArrayList<Observable<?>> originalDependencies = observer.getDependencies();

    assertEquals( originalDependencies.isEmpty(), true );

    final Observable<?> observable = newObservable( context );

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
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final ArrayList<Observable<?>> originalDependencies = observer.getDependencies();

    assertEquals( originalDependencies.isEmpty(), true );

    final Observable<?> observable = newObservable( context );

    final ArrayList<Observable<?>> newDependencies = new ArrayList<>();
    newDependencies.add( observable );

    assertThrows( () -> observer.replaceDependencies( newDependencies ) );
  }

  @Test
  public void clearDependencies()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable1 = newObservable( context );
    final Observable<?> observable2 = newObservable( context );

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
    final Observer observer = newReadOnlyObserver( new ArezContext() );

    observer.runHook( null, ObserverError.ON_ACTIVATE_ERROR );
  }

  @Test
  public void runHook_normalHook()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    final AtomicInteger counter = new AtomicInteger();

    observer.runHook( counter::incrementAndGet, ObserverError.ON_ACTIVATE_ERROR );

    assertEquals( counter.get(), 1 );
  }

  @Test
  public void runHook_hookThrowsException()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    final Exception exception = new Exception( "X" );

    final AtomicInteger handlerCallCount = new AtomicInteger();
    final AtomicBoolean observerMatches = new AtomicBoolean( false );
    final AtomicBoolean errorMatches = new AtomicBoolean( false );
    final AtomicBoolean throwableMatches = new AtomicBoolean( false );

    context.addObserverErrorHandler( ( ( observer1, error, throwable ) -> {
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
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

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
    context.getScheduler().getPendingObservers().remove( observer );
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

    final Observable<?> observable1 = newObservable( context );
    final Observable<?> observable2 = newObservable( context );

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
  public void setState_onComputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newDerivation( context );
    final Observable<?> derivedValue = observer.getDerivedValue();
    setCurrentTransaction( observer );

    final Observer watcher = ensureDerivationHasObserver( observer );
    watcher.setState( ObserverState.UP_TO_DATE );

    final TestProcedure onStale = new TestProcedure();

    observer.setOnStale( onStale );

    assertEquals( observer.getState(), ObserverState.INACTIVE );

    observer.setState( ObserverState.INACTIVE );

    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( observer.isScheduled(), false );
    assertEquals( onStale.getCalls(), 0 );

    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.isScheduled(), false );
    assertEquals( onStale.getCalls(), 0 );

    observer.setState( ObserverState.POSSIBLY_STALE );

    assertEquals( observer.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( observer.isScheduled(), true );
    assertEquals( onStale.getCalls(), 1 );
    assertEquals( watcher.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( derivedValue.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );

    observer.clearScheduledFlag();
    context.getScheduler().getPendingObservers().remove( observer );
    assertEquals( observer.isScheduled(), false );

    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( onStale.getCalls(), 1 );

    watcher.setState( ObserverState.UP_TO_DATE );
    derivedValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observer.setState( ObserverState.STALE );

    assertEquals( observer.getState(), ObserverState.STALE );
    assertEquals( observer.isScheduled(), true );
    assertEquals( onStale.getCalls(), 2 );

    assertEquals( watcher.getState(), ObserverState.POSSIBLY_STALE );
    assertEquals( derivedValue.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );
  }

  @Test
  public void setState_activateWhenDisposed()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newDerivation( context );
    setCurrentTransaction( observer );

    assertEquals( observer.getState(), ObserverState.INACTIVE );

    observer.setDisposed( true );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observer.setState( ObserverState.UP_TO_DATE ) );

    assertEquals( exception.getMessage(),
                  "Attempted to activate disposed observer named '" + observer.getName() + "'." );

    observer.setDisposed( false );

    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void setState_noTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observer.setState( ObserverState.UP_TO_DATE ) );

    assertEquals( exception.getMessage(),
                  "Attempt to invoke setState on observer named '" + observer.getName() + "' when " +
                  "there is no active transaction." );
  }

  @Test
  public void schedule()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.isScheduled(), false );

    observer.schedule();

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
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

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
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::schedule );

    assertEquals( exception.getMessage(),
                  "Observer named '" + observer.getName() + "' is not active but an attempt has " +
                  "been made to schedule observer." );
  }

  @Test
  public void dispose()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );
    observer.setState( ObserverState.UP_TO_DATE );

    Transaction.setTransaction( null );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.isLive(), true );
    assertEquals( observer.isDisposed(), false );

    observer.dispose();

    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( observer.isLive(), false );
    assertEquals( observer.isDisposed(), true );

    final int currentNextTransactionId = context.currentNextTransactionId();

    observer.dispose();

    // This verifies no new transactions were created
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId );
  }

  @Test
  public void dispose_invokedHook()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newDerivation( context );
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
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );
    observer.setState( ObserverState.UP_TO_DATE );

    Transaction.setTransaction( null );

    assertEquals( observer.isDisposed(), false );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

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
    final ArezContext context = new ArezContext();
    final Observer observer = newDerivation( context );
    final Observable<?> derivedValue = observer.getDerivedValue();
    final ComputedValue<?> computedValue = observer.getComputedValue();
    setCurrentTransaction( observer );
    observer.setState( ObserverState.UP_TO_DATE );

    Transaction.setTransaction( null );

    assertEquals( observer.isDisposed(), false );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observer.dispose();

    assertEquals( observer.isDisposed(), true );
    assertEquals( derivedValue.isDisposed(), true );
    assertEquals( computedValue.isDisposed(), true );

    handler.assertEventCount( 10 );

    // This is the part that disposes the Observer
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class );

    // This is the part that disposes the associated ComputedValue
    handler.assertNextEvent( ComputedValueDisposedEvent.class );

    // This is the part that disposes the associated Observable
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( ObservableChangedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class );
  }

  @Test
  public void getDerivedValue_throwsExceptionWhenNotDerived()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    assertEquals( observer.isDerivation(), false );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::getDerivedValue );

    assertEquals( exception.getMessage(),
                  "Attempted to invoke getDerivedValue on observer named '" + observer.getName() +
                  "' when is not a computed observer." );
  }

  @Test
  public void getDerivedValue_onDisposedObserver()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newDerivation( context );

    observer.setDisposed( true );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::getDerivedValue );

    assertEquals( exception.getMessage(),
                  "Attempted to invoke getDerivedValue on disposed observer named '" + observer.getName() + "'." );

    observer.setDisposed( false );

    assertEquals( observer.getDerivedValue().getName(), observer.getName() );
  }

  @Test
  public void getComputedValue_throwsExceptionWhenNotDerived()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    assertEquals( observer.isDerivation(), false );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::getComputedValue );

    assertEquals( exception.getMessage(),
                  "Attempted to invoke getComputedValue on observer named '" + observer.getName() +
                  "' when is not a computed observer." );
  }

  @Test
  public void getComputedValue_whenDisposed()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newDerivation( context );

    observer.setDisposed( true );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::getComputedValue );

    assertEquals( exception.getMessage(),
                  "Attempted to invoke getComputedValue on disposed observer named '" +
                  observer.getName() + "'." );

    observer.setDisposed( false );

    assertEquals( observer.getComputedValue().getName(), observer.getName() );
  }

  @Test
  public void markDependenciesLeastStaleObserverAsUpToDate()
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    final Observable<?> observable1 = newObservable( context );
    final Observable<?> observable2 = newObservable( context );
    final Observable<?> observable3 = newObservable( context );

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
    final ArezContext context = new ArezContext();

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

    final Observer observer = new Observer( context, null, name, null, mode, reaction, false );

    observer.invokeReaction();

    assertEquals( callCount.get(), 1 );
    assertEquals( errorCount.get(), 0 );
  }

  @Test
  public void invokeReaction_Observer_SpyEventHandlerPresent()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final Observer observer =
      new Observer( context,
                    null,
                    ValueUtil.randomString(),
                    null,
                    TransactionMode.READ_ONLY,
                    o -> Thread.sleep( 1 ),
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
    final ArezContext context = new ArezContext();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final ComputedValue<Integer> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> 1, Objects::equals );

    computedValue.getObserver().invokeReaction();

    handler.assertEventCount( 5 );

    {
      final ComputeStartedEvent event = handler.assertEvent( ComputeStartedEvent.class, 0 );
      assertEquals( event.getComputedValue(), computedValue );
    }
    handler.assertEvent( TransactionStartedEvent.class, 1 );
    {
      final ObservableChangedEvent event = handler.assertEvent( ObservableChangedEvent.class, 2 );
      assertEquals( event.getObservable().getName(), computedValue.getObservable().getName() );
    }
    handler.assertEvent( TransactionCompletedEvent.class, 3 );
    {
      final ComputeCompletedEvent event = handler.assertEvent( ComputeCompletedEvent.class, 4 );
      assertEquals( event.getComputedValue(), computedValue );
      assertTrue( event.getDuration() >= 0 );
    }
  }

  @Test
  public void invokeReaction_onDisposedObserver()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final TestReaction reaction = new TestReaction();
    final Observer observer =
      new Observer( context, null, ValueUtil.randomString(), null, TransactionMode.READ_ONLY, reaction, false );

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
    final ArezContext context = new ArezContext();

    final TestReaction reaction = new TestReaction();
    final Observer observer =
      new Observer( context, null, ValueUtil.randomString(), null, TransactionMode.READ_ONLY, reaction, false );

    setCurrentTransaction( context );
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
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    final TransactionMode mode = TransactionMode.READ_ONLY;

    final AtomicInteger callCount = new AtomicInteger();
    final AtomicInteger errorCount = new AtomicInteger();

    final RuntimeException exception = new RuntimeException( "X" );

    context.addObserverErrorHandler( ( observer, error, throwable ) -> {
      errorCount.incrementAndGet();
      assertEquals( error, ObserverError.REACTION_ERROR );
      assertEquals( throwable, exception );
    } );

    final Reaction reaction = observer -> {
      callCount.incrementAndGet();
      throw exception;
    };

    final Observer observer = new Observer( context, null, name, null, mode, reaction, false );

    observer.invokeReaction();

    assertEquals( callCount.get(), 1 );
    assertEquals( errorCount.get(), 1 );
  }

  @Test
  public void shouldCompute_UP_TO_DATE()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.shouldCompute(), false );
  }

  @Test
  public void shouldCompute_INACTIVE()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.INACTIVE );

    assertEquals( observer.shouldCompute(), true );
  }

  @Test
  public void shouldCompute_STALE()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.STALE );

    assertEquals( observer.shouldCompute(), true );
  }

  @Test
  public void shouldCompute_POSSIBLY_STALE()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Objects::equals );

    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final ComputedValue<String> computedValue2 =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Objects::equals );

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
    final ArezContext context = new ArezContext();
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), ValueUtil::randomString, Objects::equals );
    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final SafeFunction<String> function = () -> {
      throw new IllegalStateException();
    };
    final ComputedValue<String> computedValue2 =
      new ComputedValue<>( context, null, ValueUtil.randomString(), function, Objects::equals );

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
    final ArezContext context = new ArezContext();
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), ValueUtil::randomString, Objects::equals );
    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final SafeFunction<String> function = () -> {
      throw new IllegalStateException();
    };
    final ComputedValue<String> computedValue2 =
      new ComputedValue<>( context, null, ValueUtil.randomString(), function, Objects::equals );

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
    final ArezContext context = new ArezContext();
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Objects::equals );

    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final ComputedValue<String> computedValue2 =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Objects::equals );

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
    final ArezContext context = new ArezContext();
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Objects::equals );

    final Observer observer = computedValue.getObserver();
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final Observable<?> observable = newObservable( context );
    observer.getDependencies().add( observable );
    observable.addObserver( observer );

    assertEquals( observer.shouldCompute(), false );
  }
}
