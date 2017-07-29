package org.realityforge.arez.api2;

import java.util.concurrent.Callable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ArezContext
{
  /**
   * The current tracking.
   */
  @Nullable
  private Tracking _tracking;
  /**
   * Id of last tracking created.
   * A running sequence used to create unique id for tracking within the context.
   *
   * This needs to start at 1 as {@link Observable#NOT_IN_CURRENT_TRACKING} is used
   * to optimize depndency tracking.
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

  public void beginTransaction( @Nonnull final String name )
  {
    //noinspection ConstantConditions
    Guards.invariant( () -> !isTransactionActive(),
                      () -> String.format(
                        "Attempting to begin transaction named '%s' when existing transaction named '%s' already active.",
                        name,
                        _transaction.getName() ) );
    _transaction = new Transaction( name );
  }

  public void commitTransaction()
  {
    Guards.invariant( this::isTransactionActive,
                      () -> "Attempting to commit transaction but no transaction is active." );
    assert null != _transaction;
    _transaction.commit();
    _transaction = null;
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

  @Nonnull
  Tracking getTracking()
  {
    Guards.invariant( () -> null != _tracking,
                      () -> "Attempting to get current tracking but no tracking is active." );
    assert null != _tracking;
    return _tracking;
  }

  @Nonnull
  private Tracking beginTracking( @Nonnull final Derivation derivation )
  {
    final Tracking tracking = new Tracking( derivation, ++_lastTrackingId, _tracking );
    _tracking = tracking;
    return tracking;
  }

  private void completeTracking( @Nonnull final Tracking tracking )
  {
    Guards.invariant( () -> tracking == _tracking,
                      () -> String.format(
                        "Attempting to complete tracking '%s' but the active tracking is '%s'.",
                        String.valueOf( tracking ),
                        String.valueOf( _tracking ) ) );
    tracking.completeTracking();
    _tracking = tracking.getPrevious();
  }

  int nextNodeId()
  {
    return _nextNodeId++;
  }

  /**
   * Execute the supplied action and track observables that are accessed during execution of the action.
   * The observables are collected on the {@link Tracking} instance and the derivation is updated on
   * completion of the tracking.
   */
  <T> T track( @Nonnull final Derivation derivation, @Nonnull final Callable<T> action )
    throws Exception
  {
    final Tracking tracking = beginTracking( derivation );
    try
    {
      return action.call();
    }
    finally
    {
      completeTracking( tracking );
    }
  }
}
