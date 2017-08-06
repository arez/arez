package org.realityforge.arez.api2;

import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;

public final class Transaction
  extends ArezElement
{
  /**
   * List of observables that reached zero observers within the scope of the transaction.
   * When the transaction completes, these observers are passivated
   */
  private final ArrayList<Observable> _pendingPassivations = new ArrayList<>();
  @Nonnull
  private int _batchDepth;

  boolean inBatch()
  {
    return _batchDepth > 0;
  }

  public void batch( @Nonnull final Runnable action )
  {
    startBatch();
    try
    {
      action.run();
    }
    finally
    {
      endBatch();
    }
  }

  public void startBatch()
  {
    _batchDepth++;
  }

  public void endBatch()
  {
    _batchDepth--;
    Guards.invariant( () -> _batchDepth >= 0,
                      () -> String.format( "Invoked endBatch on transaction named '%s' when no batch is active.",
                                           _name ) );
  }

  void commit()
  {
    Guards.invariant( () -> 0 == _batchDepth,
                      () -> String.format( "Invoked commit on transaction named '%s' when batch is active.",
                                           _name ) );
    passivatePendingPassivations();
  }

  private void passivatePendingPassivations()
  {
    //WARNING: Passivations can be enqueued during the passivation process
    // so always need to call _pendingPassivations.size() through each iteration
    // of loop to ensure new passivations are collected.
    //noinspection ForLoopReplaceableByForEach
    for( int i = 0; i < _pendingPassivations.size(); i++ )
    {
      final Observable observable = _pendingPassivations.get( i );
      observable.resetPendingPassivation();
      if( !observable.hasObservers() )
      {
        observable.passivate();
      }
    }
  }

  void queueForPassivation( @Nonnull final Observable observable )
  {
    Guards.invariant( observable::canPassivate,
                      () -> String.format(
                        "Invoked queueForPassivation on transaction named '%s' for observable named '%s' when observable can not be passivated.",
                        _name,
                        observable.getName() ) );
    Guards.invariant( () -> !_pendingPassivations.contains( observable ),
                      () -> String.format(
                        "Invoked queueForPassivation on transaction named '%s' for observable named '%s' when pending passivation already exists for observable.",
                        _name,
                        observable.getName() ) );
    _pendingPassivations.add( Objects.requireNonNull( observable ) );
  }
}
