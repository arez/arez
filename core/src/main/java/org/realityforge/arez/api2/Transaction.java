package org.realityforge.arez.api2;

import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class Transaction
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
   * The tracker if transaction is trackable.
   */
  @Nullable
  private final Observer _tracker;
  /**
   * the list of observables that have been observed during tracking.
   * This list can contain duplicates and the duplicates will be skipped when converting the list
   * of observables to dependencies in the derivation.
   */
  private ArrayList<Observable> _observables;

  Transaction( @Nonnull final ArezContext context,
               @Nullable final Transaction previous,
               @Nullable final String name,
               @Nullable final Observer tracker )
  {
    super( context, name );
    _previous = previous;
    _tracker = tracker;
  }

  final void begin()
  {
    startTracking();
  }

  private void startTracking()
  {
    if ( null != _tracker )
    {
      // Mark the tracker/observer as uptodate at the start of the transaction.
      // If it is outdated during the transaction then completeTracking() will fix the state.
      _tracker.setState( ObserverState.UP_TO_DATE );
    }
  }

  @Nullable
  final Transaction getPrevious()
  {
    return _previous;
  }

  final void commit()
  {
    completeTracking();
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

  void observe( @Nonnull final Observable observable )
  {
    /*
     * This optimization attempts to stop the same observable being added multiple
     * times to the observables list by caching the transaction id on the observable.
     * This is purely an optimization but it is not perfect and may be defeated if
     * the same observable is observed in a nested tracking transaction.
     */
    final int id = getId();
    if ( observable.getLastTrackerTransactionId() != id )
    {
      observable.setLastTrackerTransactionId( id );
      if ( null == _observables )
      {
        _observables = new ArrayList<>();
      }
      _observables.add( observable );
    }
  }

  /**
   * Completes the tracking by updating the dependencies on the derivation to match the
   * observables that were observed during tracking.
   */
  private void completeTracking()
  {
    if ( null == _tracker )
    {
      return;
    }
    _tracker.invariantDependenciesUnique( "Pre completeTracking" );
    Guards.invariant( () -> _tracker.getState() != ObserverState.NOT_TRACKING,
                      () -> "completeTracking expects derivation.dependenciesState != NOT_TRACKING" );

    ObserverState newDerivationState = ObserverState.UP_TO_DATE;

    if ( null == _observables )
    {
      return;
    }

    /*
     * Iterate through the list of observables, flagging observables and removing duplicates.
     */
    final int size = _observables.size();
    int currentIndex = 0;
    for ( int i = 0; i < size; i++ )
    {
      final Observable observable = _observables.get( i );
      if ( !observable.isInCurrentTracking() )
      {
        observable.putInCurrentTracking();
        if ( i != currentIndex )
        {
          _observables.set( currentIndex, observable );
        }
        currentIndex++;

        final Observer observer = observable.getObserver();
        if ( null != observer )
        {
          final ObserverState dependenciesState = observer.getState();
          if ( dependenciesState.ordinal() < newDerivationState.ordinal() )
          {
            newDerivationState = dependenciesState;
          }
        }
      }
    }

    // Look through the old dependencies and any that are no longer tracked
    // should no longer be observed.
    final ArrayList<Observable> dependencies = _tracker.getDependencies();
    for ( int i = dependencies.size() - 1; i >= 0; i-- )
    {
      final Observable observable = dependencies.get( i );
      if ( !observable.isInCurrentTracking() )
      {
        // Old dependency was not part of tracking and needs to be unobserved
        observable.removeObserver( _tracker );
      }
    }

    // Look through the new observables and any that are still flagged must be
    // new dependencies and need to be observed by the derivation
    for ( int i = currentIndex - 1; i >= 0; i-- )
    {
      final Observable observable = _observables.get( i );
      if ( observable.isInCurrentTracking() )
      {
        observable.removeFromCurrentTracking();
        //Observable was not a dependency so it needs to be observed
        observable.addObserver( _tracker );
      }
    }

    // Some new observed derivations may become stale during this derivation computation
    // so they have had no chance to propagate staleness
    if ( ObserverState.UP_TO_DATE != newDerivationState )
    {
      _tracker.setState( newDerivationState );
    }
  }

  final boolean isRootTransaction()
  {
    return null == _previous;
  }

  @Nonnull
  final Transaction getRootTransaction()
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
