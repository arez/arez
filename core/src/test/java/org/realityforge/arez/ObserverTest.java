package org.realityforge.arez;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
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
    final int nextNodeId = context.currentNextNodeId();
    final Reaction reaction = o -> {
    };
    final Observer observer = new Observer( context, name, TransactionMode.READ_ONLY, reaction );

    // Verify all "Node" behaviour
    assertEquals( observer.getContext(), context );
    assertEquals( observer.getName(), name );
    assertEquals( observer.getId(), nextNodeId );
    assertEquals( observer.toString(), name );

    // Starts out inactive and inactive means no dependencies
    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( observer.isActive(), false );
    assertEquals( observer.isInactive(), true );
    assertEquals( observer.getDependencies().size(), 0 );

    // Reaction attributes
    assertEquals( observer.getMode(), TransactionMode.READ_ONLY );
    assertEquals( observer.getReaction(), reaction );
    assertEquals( observer.hasReaction(), true );
    assertEquals( observer.isScheduled(), false );

    assertEquals( observer.isDerivation(), false );

    // All the hooks start out null
    assertEquals( observer.getOnActivate(), null );
    assertEquals( observer.getOnDeactivate(), null );
    assertEquals( observer.getOnStale(), null );

    final Procedure onActivate = () -> {
    };
    final Procedure onDeactivate = () -> {
    };
    final Procedure onStale = () -> {
    };

    // Ensure hooks can be modified
    observer.setOnActivate( onActivate );
    observer.setOnDeactivate( onDeactivate );
    observer.setOnStale( onStale );

    assertEquals( observer.getOnActivate(), onActivate );
    assertEquals( observer.getOnDeactivate(), onDeactivate );
    assertEquals( observer.getOnStale(), onStale );

    observer.invariantState();
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
  public void initialState_whenSuppliedNoReaction()
    throws Exception
  {
    final Observer observer = newReadOnlyObserverWithNoReaction( new ArezContext() );

    assertEquals( observer.getMode(), TransactionMode.READ_ONLY );
    assertEquals( observer.getReaction(), null );
    assertEquals( observer.hasReaction(), false );
    assertEquals( observer.isScheduled(), false );
  }

  @Test
  public void construct_with_READ_WRITE_OWNED_but_noComputableValue()
    throws Exception
  {
    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observer( new ArezContext(), name, null, TransactionMode.READ_WRITE_OWNED, null ) );

    assertEquals( exception.getMessage(),
                  "Attempted to construct an observer named '" + name + "' with READ_WRITE_OWNED " +
                  "transaction mode but no ComputedValue." );
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
                    () -> new Observer( context, name, computedValue, TransactionMode.READ_ONLY, null ) );

    assertEquals( exception.getMessage(),
                  "Attempted to construct an observer named '" + name + "' with READ_ONLY " +
                  "transaction mode and a ComputedValue." );
  }

  @Test
  public void invariantDependenciesBackLink()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( context, observer );

    final Observable observable = new Observable( context, ValueUtil.randomString() );
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
  public void invariantDependenciesUnique()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    final Observable observable = new Observable( context, ValueUtil.randomString() );

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

    final Observable observable = new Observable( context, ValueUtil.randomString() );
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

    setField( observer, "_derivedValue", new Observable( context, ValueUtil.randomString() ) );

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

    setCurrentTransaction( context, newReadOnlyObserver( context ) );

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

    setCurrentTransaction( context, observer );

    observer.setState( ObserverState.UP_TO_DATE );

    observer.invariantDerivationState();
  }

  @Test
  public void replaceDependencies()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( context, observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final ArrayList<Observable> originalDependencies = observer.getDependencies();

    assertEquals( originalDependencies.isEmpty(), true );

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    final ArrayList<Observable> newDependencies = new ArrayList<>();
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
    setCurrentTransaction( context, observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final ArrayList<Observable> originalDependencies = observer.getDependencies();

    assertEquals( originalDependencies.isEmpty(), true );

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    final ArrayList<Observable> newDependencies = new ArrayList<>();
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
    setCurrentTransaction( context, observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final ArrayList<Observable> originalDependencies = observer.getDependencies();

    assertEquals( originalDependencies.isEmpty(), true );

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    final ArrayList<Observable> newDependencies = new ArrayList<>();
    newDependencies.add( observable );

    assertThrows( () -> observer.replaceDependencies( newDependencies ) );
  }

  @Test
  public void clearDependencies()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( context, observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final Observable observable1 = new Observable( context, ValueUtil.randomString() );
    final Observable observable2 = new Observable( context, ValueUtil.randomString() );

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
    setCurrentTransaction( context, observer );

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
    assertEquals( observer.isScheduled(), true );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 1 );

    observer.clearScheduledFlag();
    context.getScheduler().getPendingObservers().remove( observer );
    assertEquals( observer.isScheduled(), false );

    observer.setState( ObserverState.STALE );
    assertEquals( observer.getState(), ObserverState.STALE );
    //Is scheduled false as only attempts to schedule when going from UP_TO_DATE to STALE
    assertEquals( observer.isScheduled(), false );
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

    final Observable observable1 = new Observable( context, ValueUtil.randomString() );
    final Observable observable2 = new Observable( context, ValueUtil.randomString() );

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
  public void setState_withNoReaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserverWithNoReaction( context );
    setCurrentTransaction( context, observer );

    final TestProcedure onActivate = new TestProcedure();
    final TestProcedure onDeactivate = new TestProcedure();
    final TestProcedure onStale = new TestProcedure();

    observer.setOnActivate( onActivate );
    observer.setOnDeactivate( onDeactivate );
    observer.setOnStale( onStale );

    assertEquals( observer.getState(), ObserverState.INACTIVE );

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
    assertEquals( onStale.getCalls(), 1 );

    observer.clearScheduledFlag();
    assertEquals( observer.isScheduled(), false );

    observer.setState( ObserverState.STALE );

    assertEquals( observer.getState(), ObserverState.STALE );
    assertEquals( observer.isScheduled(), false );
    assertEquals( onActivate.getCalls(), 1 );
    assertEquals( onDeactivate.getCalls(), 0 );
    assertEquals( onStale.getCalls(), 1 );
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
    setCurrentTransaction( context, observer );

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
  public void schedule_whenNoReaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserverWithNoReaction( context );
    setCurrentTransaction( context, observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::schedule );

    assertEquals( exception.getMessage(),
                  "Observer named '" + observer.getName() + "' has no reaction but an attempt " +
                  "has been made to schedule observer." );
  }

  @Test
  public void schedule_whenInactive()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( context, observer );

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
    setCurrentTransaction( context, observer );
    observer.setState( ObserverState.UP_TO_DATE );

    context.setTransaction( null );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    observer.dispose();

    assertEquals( observer.getState(), ObserverState.INACTIVE );
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
  public void markDependenciesLeastStaleObserverAsUpToDate()
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    final Observable observable1 = new Observable( context, ValueUtil.randomString() );
    final Observable observable2 = new Observable( context, ValueUtil.randomString() );
    final Observable observable3 = new Observable( context, ValueUtil.randomString() );
    final Observable observable4 = new Observable( context, ValueUtil.randomString() );

    observer.getDependencies().add( observable1 );
    observer.getDependencies().add( observable2 );
    observer.getDependencies().add( observable3 );
    observer.getDependencies().add( observable4 );

    setCurrentTransaction( context, observer );

    observable1.addObserver( observer );
    observable2.addObserver( observer );
    observable3.addObserver( observer );
    observable4.addObserver( observer );

    observable1.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observable2.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );
    observable3.setLeastStaleObserverState( ObserverState.STALE );
    observable4.setLeastStaleObserverState( ObserverState.INACTIVE );

    observer.markDependenciesLeastStaleObserverAsUpToDate();

    assertEquals( observable1.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observable3.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observable4.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    assertEquals( observable4.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
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
      assertEquals( context.isTransactionActive(), true );
      assertEquals( context.getTransaction().getName(), name );
      assertEquals( context.getTransaction().getMode(), mode );
      assertEquals( observer.getName(), name );
    };

    final Observer observer = new Observer( context, name, mode, reaction );

    observer.invokeReaction();

    assertEquals( callCount.get(), 1 );
    assertEquals( errorCount.get(), 0 );
  }

  @Test
  public void invokeReaction_whenObserverHasNoReaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Observer observer = newReadOnlyObserverWithNoReaction( context );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observer::invokeReaction );
    assertEquals( exception.getMessage(),
                  "invokeReaction called on observer named '" +
                  observer.getName() + "' but observer has no associated reaction." );
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

    final Observer observer = new Observer( context, name, mode, reaction );

    observer.invokeReaction();

    assertEquals( callCount.get(), 1 );
    assertEquals( errorCount.get(), 1 );
  }
}
