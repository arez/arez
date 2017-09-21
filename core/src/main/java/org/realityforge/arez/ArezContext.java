package org.realityforge.arez;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.realityforge.arez.spy.ComputedValueCreatedEvent;
import org.realityforge.arez.spy.ObservableCreatedEvent;
import org.realityforge.arez.spy.ObserverCreatedEvent;
import org.realityforge.arez.spy.ObserverErrorEvent;
import org.realityforge.arez.spy.ReactionScheduledEvent;
import org.realityforge.arez.spy.TransactionCompletedEvent;
import org.realityforge.arez.spy.TransactionStartedEvent;
import static org.realityforge.braincheck.Guards.*;

/**
 * The ArezContext defines the top level container of interconnected observables and observers.
 * The context also provides the mechanism for creating transactions to read and write state
 * within the system.
 */
public final class ArezContext
{
  /**
   * All changes in a context must occur within the scope of a transaction.
   * This references the current transaction.
   */
  @Nullable
  private Transaction _transaction;
  /**
   * Id of next node to be created.
   * This is only used if {@link #areNamesEnabled()} returns true but no name has been supplied.
   */
  private int _nextNodeId = 1;
  /**
   * Id of next transaction to be created.
   *
   * This needs to start at 1 as {@link Observable#NOT_IN_CURRENT_TRACKING} is used
   * to optimize dependency tracking in transactions.
   */
  private int _nextTransactionId = 1;
  /**
   * Reaction Scheduler.
   * Currently hard-coded, in the future potentially configurable.
   */
  private final ReactionScheduler _scheduler = new ReactionScheduler( this );
  /**
   * Support infrastructure for propagating observer errors.
   */
  @Nonnull
  private final ObserverErrorHandlerSupport _observerErrorHandlerSupport = new ObserverErrorHandlerSupport();
  /**
   * Support infrastructure for supporting spy events.
   */
  @Nullable
  private final SpyImpl _spy = ArezConfig.enableSpy() ? new SpyImpl( this ) : null;

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>      the type of the computed value.
   * @param function the function that computes the value.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> createComputedValue( @Nonnull final SafeFunction<T> function )
  {
    return createComputedValue( null, function );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>      the type of the computed value.
   * @param name     the name of the ComputedValue. Should be non-null if {@link #areNamesEnabled()} returns true, null otherwise.
   * @param function the function that computes the value.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> createComputedValue( @Nullable final String name,
                                                   @Nonnull final SafeFunction<T> function )
  {
    return createComputedValue( name, function, Objects::equals );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>                the type of the computed value.
   * @param name               the name of the ComputedValue. Should be non-null if {@link #areNamesEnabled()} returns true, null otherwise.
   * @param function           the function that computes the value.
   * @param equalityComparator the comparator that determines whether the newly computed value differs from existing value.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> createComputedValue( @Nullable final String name,
                                                   @Nonnull final SafeFunction<T> function,
                                                   @Nonnull final EqualityComparator<T> equalityComparator )
  {
    return createComputedValue( name, function, equalityComparator, null, null, null );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>                the type of the computed value.
   * @param name               the name of the ComputedValue.
   * @param function           the function that computes the value.
   * @param equalityComparator the comparator that determines whether the newly computed value differs from existing value.
   * @param onActivate         the procedure to invoke when ComputedValue changes from the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onDeactivate       the procedure to invoke when ComputedValue changes to the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onStale            the procedure to invoke when ComputedValue changes changes from the UP_TO_DATE state to STALE or POSSIBLY_STALE. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> createComputedValue( @Nullable final String name,
                                                   @Nonnull final SafeFunction<T> function,
                                                   @Nonnull final EqualityComparator<T> equalityComparator,
                                                   @Nullable final Procedure onActivate,
                                                   @Nullable final Procedure onDeactivate,
                                                   @Nullable final Procedure onStale )
  {
    final ComputedValue<T> computedValue =
      new ComputedValue<>( this, toName( "ComputedValue", name ), function, equalityComparator );
    final Observer observer = computedValue.getObserver();
    observer.setOnActivate( onActivate );
    observer.setOnDeactivate( onDeactivate );
    observer.setOnStale( onStale );
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ComputedValueCreatedEvent( computedValue ) );
    }
    return computedValue;
  }

  /**
   * Build name for node.
   * If {@link #areNamesEnabled()} returns false then this method will return null, otherwise the specified
   * name will be returned or a name synthesized from the prefix and a running number if no name is specified.
   *
   * @param prefix the prefix used if this method needs to generate name.
   * @param name   the name specified by the user.
   * @return the name.
   */
  @Nullable
  String toName( @Nonnull final String prefix, @Nullable final String name )
  {
    return ArezConfig.enableNames() ?
           null != name ? name : prefix + "@" + _nextNodeId++ :
           null;
  }

  /**
   * Create a read-only autorun observer and run immediately.
   *
   * @param action the action defining the observer.
   * @return the new Observer.
   */
  @Nonnull
  public Observer autorun( @Nonnull final Procedure action )
  {
    return autorun( null, action );
  }

  /**
   * Create a read-only autorun observer and run immediately.
   *
   * @param name   the name of the observer.
   * @param action the action defining the observer.
   * @return the new Observer.
   */
  @Nonnull
  public Observer autorun( @Nullable final String name, @Nonnull final Procedure action )
  {
    return autorun( name, false, action );
  }

  /**
   * Create an autorun observer and run immediately.
   *
   * @param mutation true if the action may modify state, false otherwise.
   * @param action   the action defining the observer.
   * @return the new Observer.
   */
  @Nonnull
  public Observer autorun( final boolean mutation, @Nonnull final Procedure action )
  {
    return autorun( null, mutation, action );
  }

  /**
   * Create an autorun observer and run immediately.
   *
   * @param name     the name of the observer.
   * @param mutation true if the action may modify state, false otherwise.
   * @param action   the action defining the observer.
   * @return the new Observer.
   */
  @Nonnull
  public Observer autorun( @Nullable final String name,
                           final boolean mutation,
                           @Nonnull final Procedure action )
  {
    return autorun( name, mutation, action, true );
  }

  /**
   * Create an autorun observer.
   *
   * @param name           the name of the observer.
   * @param mutation       true if the action may modify state, false otherwise.
   * @param action         the action defining the observer.
   * @param runImmediately true to invoke action immediately, false to schedule reaction for next reaction cycle.
   * @return the new Observer.
   */
  @Nonnull
  public Observer autorun( @Nullable final String name,
                           final boolean mutation,
                           @Nonnull final Procedure action,
                           final boolean runImmediately )
  {
    final Observer observer =
      createObserver( name, mutation, o -> procedure( name, o.getMode(), action, o ), false );
    if ( runImmediately )
    {
      observer.invokeReaction();
    }
    else
    {
      scheduleReaction( observer );
    }
    return observer;
  }

  /**
   * Create an observer with specified parameters.
   *
   * @param name     the name of the observer.
   * @param mutation true if the reaction may modify state, false otherwise.
   * @param reaction the reaction defining observer.
   * @return the new Observer.
   */
  @Nonnull
  Observer createObserver( @Nullable final String name,
                           final boolean mutation,
                           @Nonnull final Reaction reaction,
                           final boolean canTrackExplicitly )
  {
    final TransactionMode mode = mutationToTransactionMode( mutation );
    final Observer observer = new Observer( this, toName( "Observer", name ), mode, reaction, canTrackExplicitly );
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ObserverCreatedEvent( observer ) );
    }
    return observer;
  }

  /**
   * Create a non-computed Observer with specified name.
   *
   * @param name the name of the observer. Should be non null if {@link #areNamesEnabled()} returns true, null otherwise.
   * @return the new Observer.
   */
  @Nonnull
  public Observable createObservable( @Nullable final String name )
  {
    final Observable observable = new Observable( this, ArezConfig.enableNames() ? name : null );
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ObservableCreatedEvent( observable ) );
    }
    return observable;
  }

  /**
   * Pass the supplied observer to the scheduler.
   * The observer should NOT be already pending execution.
   *
   * @param observer the reaction to schedule.
   */
  void scheduleReaction( @Nonnull final Observer observer )
  {
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ReactionScheduledEvent( observer ) );
    }
    _scheduler.scheduleReaction( observer );
  }

  /**
   * Create a new transaction.
   *
   * @param name    the name of the transaction. Should be non-null if {@link #areNamesEnabled()} is true, false otherwise.
   * @param mode    the transaction mode.
   * @param tracker the observer that is tracking transaction if any.
   * @return the new transaction.
   */
  Transaction beginTransaction( @Nullable final String name,
                                @Nonnull final TransactionMode mode,
                                @Nullable final Observer tracker )
  {
    if ( TransactionMode.READ_WRITE == mode && null != _transaction )
    {
      invariant( () -> TransactionMode.READ_WRITE == _transaction.getMode(),
                 () -> "Attempting to create READ_WRITE transaction named '" + name + "' but it is " +
                       "nested in transaction named '" + _transaction.getName() + "' with mode " +
                       _transaction.getMode().name() + " which is not equal to READ_WRITE." );
    }
    _transaction = new Transaction( this, _transaction, name, mode, tracker );
    _transaction.begin();
    if ( willPropagateSpyEvents() )
    {
      assert null != name;
      final boolean mutation = TransactionMode.READ_WRITE == _transaction.getMode();
      getSpy().reportSpyEvent( new TransactionStartedEvent( name, mutation, tracker ) );
    }
    return _transaction;
  }

  /**
   * Commit the supplied transaction.
   *
   * This method verifies that the transaction active is the supplied transaction before committing
   * the transaction and restoring the prior transaction if any.
   *
   * @param transaction the transaction.
   */
  void commitTransaction( @Nonnull final Transaction transaction )
  {
    invariant( () -> null != _transaction,
               () -> "Attempting to commit transaction named '" + transaction.getName() +
                     "' but no transaction is active." );
    assert null != _transaction;
    invariant( () -> _transaction == transaction,
               () -> "Attempting to commit transaction named '" + transaction.getName() + "' but this does " +
                     "not match existing transaction named '" + _transaction.getName() + "'." );
    _transaction.commit();
    if ( willPropagateSpyEvents() )
    {
      final String name = _transaction.getName();
      final boolean mutation = TransactionMode.READ_WRITE == _transaction.getMode();
      final Observer tracker = _transaction.getTracker();
      final long duration = System.currentTimeMillis() - _transaction.getStartedAt();
      getSpy().reportSpyEvent( new TransactionCompletedEvent( name, mutation, tracker, duration ) );
    }
    _transaction = _transaction.getPrevious();
    triggerScheduler();
  }

  /**
   * Method invoked to trigger the scheduler to run any pending reactions. The scheduler will only be
   * triggered if there is no transaction active. This method is typically used after one or more Observers
   * have been created outside a transaction with the runImmediately flag set to false and the caller wants
   * to force the observers to react. Otherwise the Observers will not be schedule until the next top-level
   * transaction completes.
   */
  public void triggerScheduler()
  {
    if ( null == _transaction )
    {
      _scheduler.runPendingObservers();
    }
  }

  /**
   * Return true if user should pass names into API methods, false if should pass null.
   *
   * @return true if user should pass names into API methods, false if should pass null.
   */
  public boolean areNamesEnabled()
  {
    return ArezConfig.enableNames();
  }

  /**
   * Return true if spies are enabled.
   *
   * @return true if spies are enabled, false otherwise.
   */
  public boolean areSpiesEnabled()
  {
    return ArezConfig.enableSpy();
  }

  /**
   * Execute the supplied function in a read-write transaction.
   * The name is synthesized if {@link #areNamesEnabled()} returns true.
   * The action may throw an exception.
   *
   * @param <T>    the type of return value.
   * @param action the action to execute.
   * @return the value returned from the action.
   * @throws Exception if the action throws an an exception.
   */
  public <T> T function( @Nonnull final Function<T> action )
    throws Throwable
  {
    return function( true, action );
  }

  /**
   * Execute the supplied function in a transaction.
   * The name is synthesized if {@link #areNamesEnabled()} returns true.
   * The action may throw an exception.
   *
   * @param <T>      the type of return value.
   * @param mutation true if the action may modify state, false otherwise.
   * @param action   the action to execute.
   * @return the value returned from the action.
   * @throws Exception if the action throws an an exception.
   */
  public <T> T function( final boolean mutation, @Nonnull final Function<T> action )
    throws Throwable
  {
    return function( null, mutation, action );
  }

  /**
   * Execute the supplied function in a transaction.
   * The action may throw an exception.
   *
   * @param <T>      the type of return value.
   * @param name     the name of the transaction.
   * @param mutation true if the action may modify state, false otherwise.
   * @param action   the action to execute.
   * @return the value returned from the action.
   * @throws Exception if the action throws an an exception.
   */
  public <T> T function( @Nullable final String name, final boolean mutation, @Nonnull final Function<T> action )
    throws Throwable
  {
    return function( name, mutationToTransactionMode( mutation ), action, null );
  }

  private <T> T function( @Nullable final String name,
                          @Nonnull final TransactionMode mode,
                          @Nonnull final Function<T> action,
                          @Nullable final Observer tracker )
    throws Throwable
  {
    final Transaction transaction = beginTransaction( toName( "Transaction", name ), mode, tracker );
    try
    {
      return action.call();
    }
    finally
    {
      commitTransaction( transaction );
    }
  }

  /**
   * Execute the supplied function in a read-write transaction.
   * The action is expected to not throw an exception.
   *
   * @param <T>    the type of return value.
   * @param action the action to execute.
   * @return the value returned from the action.
   */
  public <T> T safeFunction( @Nonnull final SafeFunction<T> action )
  {
    return safeFunction( true, action );
  }

  /**
   * Execute the supplied function in a transaction.
   * The action is expected to not throw an exception.
   *
   * @param <T>      the type of return value.
   * @param mutation true if the action may modify state, false otherwise.
   * @param action   the action to execute.
   * @return the value returned from the action.
   */
  public <T> T safeFunction( final boolean mutation, @Nonnull final SafeFunction<T> action )
  {
    return safeFunction( null, mutation, action );
  }

  /**
   * Execute the supplied function in a transaction.
   * The action is expected to not throw an exception.
   *
   * @param <T>      the type of return value.
   * @param name     the name of the transaction.
   * @param mutation true if the action may modify state, false otherwise.
   * @param action   the action to execute.
   * @return the value returned from the action.
   */
  public <T> T safeFunction( @Nullable final String name,
                             final boolean mutation,
                             @Nonnull final SafeFunction<T> action )
  {
    return safeFunction( name, mutationToTransactionMode( mutation ), action, null );
  }

  private <T> T safeFunction( @Nullable final String name,
                              @Nonnull final TransactionMode mode,
                              @Nonnull final SafeFunction<T> action,
                              @Nullable final Observer tracker )
  {
    final Transaction transaction = beginTransaction( toName( "Transaction", name ), mode, tracker );
    try
    {
      return action.call();
    }
    finally
    {
      commitTransaction( transaction );
    }
  }

  /**
   * Execute the supplied function in a read-write transaction.
   * The procedure may throw an exception.
   *
   * @param procedure the procedure to execute.
   * @throws Throwable if the procedure throws an an exception.
   */
  public void procedure( @Nonnull final Procedure procedure )
    throws Throwable
  {
    procedure( true, procedure );
  }

  /**
   * Execute the supplied procedure in a transaction.
   * The procedure may throw an exception.
   *
   * @param mutation  true if the action may modify state, false otherwise.
   * @param procedure the procedure to execute.
   * @throws Throwable if the procedure throws an an exception.
   */
  public void procedure( final boolean mutation, @Nonnull final Procedure procedure )
    throws Throwable
  {
    procedure( null, mutation, procedure );
  }

  /**
   * Execute the supplied procedure in a transaction.
   * The procedure may throw an exception.
   *
   * @param name      the name of the transaction.
   * @param mutation  true if the action may modify state, false otherwise.
   * @param procedure the procedure to execute.
   * @throws Throwable if the procedure throws an an exception.
   */
  public void procedure( @Nullable final String name, final boolean mutation, @Nonnull final Procedure procedure )
    throws Throwable
  {
    procedure( name, mutationToTransactionMode( mutation ), procedure, null );
  }

  void procedure( @Nullable final String name,
                  @Nonnull final TransactionMode mode,
                  @Nonnull final Procedure procedure,
                  @Nullable final Observer tracker )
    throws Throwable
  {
    final Transaction transaction = beginTransaction( toName( "Transaction", name ), mode, tracker );
    try
    {
      procedure.call();
    }
    finally
    {
      commitTransaction( transaction );
    }
  }

  /**
   * Execute the supplied procedure in a read-write transaction.
   * The action is expected to not throw an exception.
   *
   * @param action the action to execute.
   */
  public void safeProcedure( @Nonnull final SafeProcedure action )
  {
    safeProcedure( true, action );
  }

  /**
   * Execute the supplied procedure in a transaction.
   * The action is expected to not throw an exception.
   *
   * @param mutation true if the action may modify state, false otherwise.
   * @param action   the action to execute.
   */
  public void safeProcedure( final boolean mutation, @Nonnull final SafeProcedure action )
  {
    safeProcedure( null, mutation, action );
  }

  /**
   * Execute the supplied procedure in a transaction.
   * The action is expected to not throw an exception.
   *
   * @param name     the name of the transaction.
   * @param mutation true if the action may modify state, false otherwise.
   * @param action   the action to execute.
   */
  public void safeProcedure( @Nullable final String name,
                             final boolean mutation,
                             @Nonnull final SafeProcedure action )
  {
    safeProcedure( name, mutationToTransactionMode( mutation ), action, null );
  }

  void safeProcedure( @Nullable final String name,
                      @Nonnull final TransactionMode mode,
                      @Nonnull final SafeProcedure action,
                      @Nullable final Observer tracker )
  {
    final Transaction transaction = beginTransaction( toName( "Transaction", name ), mode, tracker );
    try
    {
      action.call();
    }
    finally
    {
      commitTransaction( transaction );
    }
  }

  /**
   * Return true if there is a transaction in progress.
   *
   * @return true if there is a transaction in progress.
   */
  boolean isTransactionActive()
  {
    return null != _transaction;
  }

  /**
   * Return the current transaction.
   * This method should not be invoked unless a transaction active and will throw an
   * exception if invariant checks are enabled.
   *
   * @return the current transaction.
   */
  @Nonnull
  Transaction getTransaction()
  {
    invariant( this::isTransactionActive, () -> "Attempting to get current transaction but no transaction is active." );
    assert null != _transaction;
    return _transaction;
  }

  /**
   * Return next transaction id and increment internal counter.
   * The id is a monotonically increasing number starting at 1.
   *
   * @return the next transaction id.
   */
  int nextTransactionId()
  {
    return _nextTransactionId++;
  }

  /**
   * Add error handler to the list of error handlers called.
   * The handler should not already be in the list.
   *
   * @param handler the error handler.
   */
  public void addObserverErrorHandler( @Nonnull final ObserverErrorHandler handler )
  {
    _observerErrorHandlerSupport.addObserverErrorHandler( handler );
  }

  /**
   * Remove error handler from list of existing error handlers.
   * The handler should already be in the list.
   *
   * @param handler the error handler.
   */
  public void removeObserverErrorHandler( @Nonnull final ObserverErrorHandler handler )
  {
    _observerErrorHandlerSupport.removeObserverErrorHandler( handler );
  }

  /**
   * Report an error in observer.
   *
   * @param observer  the observer that generated error.
   * @param error     the type of the error.
   * @param throwable the exception that caused error if any.
   */
  void reportObserverError( @Nonnull final Observer observer,
                            @Nonnull final ObserverError error,
                            @Nullable final Throwable throwable )
  {
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ObserverErrorEvent( observer, error, throwable ) );
    }
    _observerErrorHandlerSupport.onObserverError( observer, error, throwable );
  }

  /**
   * Return true if spy events will be propagated.
   * This means spies are enabled and there is at least one spy event handler present.
   *
   * @return true if spy events will be propagated, false otherwise.
   */
  private boolean willPropagateSpyEvents()
  {
    return areSpiesEnabled() && getSpy().willPropagateSpyEvents();
  }

  /**
   * Return the spy associated with context.
   * This method should not be invoked unless {@link #areSpiesEnabled()} returns true.
   *
   * @return the spy associated with context.
   */
  @Nonnull
  public Spy getSpy()
  {
    invariant( this::areSpiesEnabled, () -> "Attempting to get Spy but spies are not enabled." );
    assert null != _spy;
    return _spy;
  }

  /**
   * Convert flag to appropriate transaction mode.
   *
   * @param mutation true if the transaction may modify state, false otherwise.
   * @return the READ_WRITE transaction mode if mutation is true else READ_ONLY.
   */
  @Nonnull
  private TransactionMode mutationToTransactionMode( final boolean mutation )
  {
    return mutation ? TransactionMode.READ_WRITE : TransactionMode.READ_ONLY;
  }

  @TestOnly
  @Nonnull
  ObserverErrorHandlerSupport getObserverErrorHandlerSupport()
  {
    return _observerErrorHandlerSupport;
  }

  @TestOnly
  int currentNextTransactionId()
  {
    return _nextTransactionId;
  }

  @TestOnly
  @Nonnull
  ReactionScheduler getScheduler()
  {
    return _scheduler;
  }

  @TestOnly
  void setTransaction( @Nullable final Transaction transaction )
  {
    _transaction = transaction;
  }

  @TestOnly
  void setNextNodeId( final int nextNodeId )
  {
    _nextNodeId = nextNodeId;
  }

  @TestOnly
  int getNextNodeId()
  {
    return _nextNodeId;
  }
}
