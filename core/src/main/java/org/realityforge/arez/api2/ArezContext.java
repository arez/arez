package org.realityforge.arez.api2;

import java.util.concurrent.Callable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.TestOnly;

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
   * Execute the supplied action in a transaction.
   * The transaction is tracking if tracker is supplied and is named with specified name.
   */
  public <T> T transaction( @Nullable final String name,
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

  /**
   * Execute the supplied action in a transaction.
   * The transaction is tracking if tracker is supplied and is named with specified name.
   */
  public void transaction( @Nullable final String name,
                           @Nullable final Observer tracker,
                           @Nonnull final Action action )
    throws Exception
  {
    final Transaction transaction = beginTransaction( name, tracker );
    try
    {
      action.call();
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

  int nextNodeId()
  {
    return _nextNodeId++;
  }

  @TestOnly
  final int currentNextNodeId()
  {
    return _nextNodeId;
  }
}
