package org.realityforge.arez.api2;

import java.util.concurrent.Callable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ArezContext
{
  /**
   * Id of last tracking created.
   * A running sequence used to create unique id for tracking within the context.
   *
   * This needs to start at 1 as {@link Observable#NOT_IN_CURRENT_TRACKING} is used
   * to optimize dependency tracking.
   */
  private int _lastTrackingId = 1;
  /**
   * All changes in a context must occur within the scope of a transaction.
   * This references the current transaction.
   */
  @Nullable
  private Transaction _transaction;
  /**
   * Next id of node created.
   */
  private int _nextNodeId = 1;

  private Transaction beginTransaction( @Nullable final String name, @Nullable final Observer tracker )
  {
    _transaction = new Transaction( this, _transaction, name, tracker );
    return _transaction;
  }

  private void commitTransaction( @Nonnull final Transaction transaction )
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
  }

  /**
   * Execute the supplied action and track observables that are accessed during execution of the action.
   * The observables are collected on the {@link Tracking} instance and the observer is updated on
   * completion of the tracking.
   */
  <T> T transaction( @Nullable final String name,
                     @Nullable final Observer tracker,
                     @Nonnull final Callable<T> action )
    throws Exception
  {
    final Transaction transaction = beginTransaction( name, tracker );
    try
    {
      return action.call();
    }
    finally
    {
      commitTransaction( transaction );
    }
  }

  public boolean isTransactionActive()
  {
    return null != _transaction;
  }

  @Nonnull
  Transaction getTransaction()
  {
    Guards.invariant( this::isTransactionActive,
                      () -> "Attempting to get current transaction but no transaction is active." );
    assert null != _transaction;
    return _transaction;
  }

  int nextLastTrackingId()
  {
    return _lastTrackingId++;
  }

  int nextNodeId()
  {
    return _nextNodeId++;
  }
}
