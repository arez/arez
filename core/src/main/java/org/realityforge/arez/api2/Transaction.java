package org.realityforge.arez.api2;

import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Transaction
  extends Node
{
  /**
   * List of observables that reached zero observers within the scope of the transaction.
   * When the transaction completes, these observers are passivated
   */
  @Nullable
  private ArrayList<Observable> _pendingPassivations;
  /**
   * Reference to the transaction that was active when this transaction began. When this
   * transaction commits, the previous transaction will be restored.
   */
  @Nullable
  private final Transaction _previous;
  /**
   * The state associated with tracking transaction if transaction is trackable.
   */
  @Nullable
  private final Tracking _tracking;

  Transaction( @Nonnull final ArezContext context,
               @Nullable final Transaction previous,
               @Nullable final String name,
               @Nullable final Observer tracker )
  {
    super( context, name );
    _previous = previous;
    _tracking = null != tracker ? new Tracking( tracker, context.nextLastTrackingId() ) : null;
  }

  @Nullable
  final Transaction getPrevious()
  {
    return _previous;
  }

  @Nonnull
  final Tracking getTracking()
  {
    Guards.invariant( () -> null != _tracking,
                      () -> String.format( "Attempting to get current tracking for transaction named '%s' but no tracking is active.", getName() ) );
    assert null != _tracking;
    return _tracking;
  }

  final void commit()
  {
    if( null != _tracking )
    {
      _tracking.completeTracking();
    }
    if ( isRootTransaction() )
    {
      //If you are the root transaction
      passivatePendingPassivations();
      //TODO: schedule notifications here
    }
  }

  private void passivatePendingPassivations()
  {
    assert isRootTransaction();
    if ( null != _pendingPassivations )
    {
      //WARNING: Passivations can be enqueued during the passivation process
      // so always need to call _pendingPassivations.size() through each iteration
      // of loop to ensure new passivations are collected.
      //noinspection ForLoopReplaceableByForEach
      for ( int i = 0; i < _pendingPassivations.size(); i++ )
      {
        final Observable observable = _pendingPassivations.get( i );
        observable.resetPendingPassivation();
        if ( !observable.hasObservers() )
        {
          observable.passivate();
        }
      }
    }
  }

  void queueForPassivation( @Nonnull final Observable observable )
  {
    Guards.invariant( observable::canPassivate,
                      () -> String.format(
                        "Invoked queueForPassivation on transaction named '%s' for observable named '%s' when observable can not be passivated.",
                        getName(),
                        observable.getName() ) );
    if ( null == _pendingPassivations )
    {
      final Transaction rootTransaction = getRootTransaction();
      if ( null == rootTransaction._pendingPassivations )
      {
        rootTransaction._pendingPassivations = new ArrayList<>();
      }
      _pendingPassivations = rootTransaction._pendingPassivations;
    }
    else
    {
      Guards.invariant( () -> !_pendingPassivations.contains( observable ),
                        () -> String.format(
                          "Invoked queueForPassivation on transaction named '%s' for observable named '%s' when pending passivation already exists for observable.",
                          getName(),
                          observable.getName() ) );
    }
    _pendingPassivations.add( Objects.requireNonNull( observable ) );
  }

  private boolean isRootTransaction()
  {
    return null == _previous;
  }

  @Nonnull
  private Transaction getRootTransaction()
  {
    if ( isRootTransaction() )
    {
      return this;
    }
    else
    {
      assert null != _previous;
      return _previous.getRootTransaction();
    }
  }
}
