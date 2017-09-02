package org.realityforge.arez;

import java.util.concurrent.Callable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.TestOnly;

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
   *
   * This needs to start at 1 as {@link Observable#NOT_IN_CURRENT_TRACKING} is used
   * to optimize dependency tracking in transactions.
   */
  private int _nextNodeId = 1;
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

  public Observer createObserver( @Nullable final String name,
                                  @Nonnull final TransactionMode mode,
                                  @Nullable final Reaction reaction,
                                  final boolean runImmediately )
  {
    final Observer observer = new Observer( this, ArezConfig.enableNames() ? name : null, mode, reaction );
    if ( runImmediately )
    {
      if ( observer.hasReaction() )
      {
        invokeReaction( observer );
      }
      else
      {
        Guards.fail( () -> String.format(
          "Attempted to run observer named '%s' on creation but observer specified no reaction.",
          observer.getName() ) );

      }
    }
    return observer;
  }

  /**
   * Pass the supplied observer to the scheduler.
   * The observer should NOT be already pending execution.
   *
   * @param observer the reaction to schedule.
   */
  void scheduleReaction( @Nonnull final Observer observer )
  {
    _scheduler.scheduleReaction( observer );
  }

  /**
   * Create a new transaction.
   *
   * @param name    the name of the transaction. Should be non-null if {@link ArezConfig#enableNames()} is true, false otherwise.
   * @param mode    the transaciton mode.
   * @param tracker the observer that is tracking transaction if any.
   * @return the new transaction.
   */
  Transaction beginTransaction( @Nullable final String name,
                                @Nonnull final TransactionMode mode,
                                @Nullable final Observer tracker )
  {
    _transaction = new Transaction( this, _transaction, name, mode, tracker );
    _transaction.begin();
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
    Guards.invariant( () -> null != _transaction,
                      () -> String.format( "Attempting to commit transaction named '%s' but no transaction is active.",
                                           transaction.getName() ) );
    assert null != _transaction;
    Guards.invariant( () -> _transaction == transaction,
                      () -> String.format(
                        "Attempting to commit transaction named '%s' but this does not match existing transaction named '%s'.",
                        transaction.getName(),
                        _transaction.getName() ) );
    _transaction.commit();
    _transaction = _transaction.getPrevious();
    if ( null == _transaction )
    {
      _scheduler.runPendingObservers();
    }
  }

  /**
   * Execute the supplied action in a transaction.
   * The transaction is tracking if tracker is supplied and is named with specified name.
   *
   * @param <T>     the type of return value.
   * @param name    the name of the transaction. Should be non-null if {@link ArezConfig#enableNames()} is true, false otherwise.
   * @param mode    the transaciton mode.
   * @param tracker the observer that is tracking transaction if any.
   * @param action  the action to execute.
   * @return the value returned from the action.
   * @throws Exception if the action throws an an exception.
   */
  public <T> T transaction( @Nullable final String name,
                            @Nonnull final TransactionMode mode,
                            @Nullable final Observer tracker,
                            @Nonnull final Callable<T> action )
    throws Exception
  {
    final Transaction transaction = beginTransaction( name, mode, tracker );
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
   * Execute the supplied action in a transaction.
   * The transaction is tracking if tracker is supplied and is named with specified name.
   *
   * @param name    the name of the transaction. Should be non-null if {@link ArezConfig#enableNames()} is true, false otherwise.
   * @param mode    the transaction mode.
   * @param tracker the observer that is tracking transaction if any.
   * @param action  the action to execute.
   * @throws Exception if the action throws an an exception.
   */
  public void transaction( @Nullable final String name,
                           @Nonnull final TransactionMode mode,
                           @Nullable final Observer tracker,
                           @Nonnull final Action action )
    throws Exception
  {
    final Transaction transaction = beginTransaction( name, mode, tracker );
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
  public boolean isTransactionActive()
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
    Guards.invariant( this::isTransactionActive,
                      () -> "Attempting to get current transaction but no transaction is active." );
    assert null != _transaction;
    return _transaction;
  }

  /**
   * Return next node id and increment internal counter.
   * The id is a monotonically increasing number starting at 1.
   *
   * @return the next node id.
   */
  int nextNodeId()
  {
    return _nextNodeId++;
  }

  /**
   * Run a reaction for the supplied observer.
   * The reaction is executed in a transaction with the name and mode defined
   * by the observer. If the reaction throws an exception, the exception is reported
   * to the context global ObserverErrorHandlers
   *
   * @param observer the observer to run reaction for.
   */
  void invokeReaction( @Nonnull final Observer observer )
  {
    final String name = ArezConfig.enableNames() ? observer.getName() : null;
    final TransactionMode mode = observer.getMode();
    final Reaction reaction = observer.getReaction();
    Guards.invariant( () -> null != reaction,
                      () -> String.format(
                        "invokeReaction called for observer named '%s' but observer has no associated reaction.",
                        name ) );
    assert null != reaction;
    try
    {
      transaction( name, mode, observer, () -> reaction.react( observer ) );
    }
    catch ( final Throwable t )
    {
      getObserverErrorHandler().onObserverError( observer, ObserverError.REACTION_ERROR, t );
    }
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

  @Nonnull
  ObserverErrorHandler getObserverErrorHandler()
  {
    return _observerErrorHandlerSupport;
  }

  @TestOnly
  @Nonnull
  ObserverErrorHandlerSupport getObserverErrorHandlerSupport()
  {
    return _observerErrorHandlerSupport;
  }

  @TestOnly
  int currentNextNodeId()
  {
    return _nextNodeId;
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
}
