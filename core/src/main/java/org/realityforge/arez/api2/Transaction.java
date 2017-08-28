package org.realityforge.arez.api2;

import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.TestOnly;

final class Transaction
  extends Node
{
  /**
   * Determines which write operations are permitted within the scope of the transaction if any.
   */
  @Nonnull
  private final TransactionMode _mode;
  /**
   * A list of observables that have reached zero observers within the scope of the root transaction.
   * When the root transaction completes, these observers are passivated if they still have no observers.
   * It should be noted that this list is owned by the root transaction and a reference is copied to all
   * non-root transactions that attempt to passivate an observable.
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
   * The tracking observer if the transaction is trackable.
   */
  @Nullable
  private final Observer _tracker;
  /**
   * the list of observables that have been observed during tracking.
   * This list may contain duplicates but the duplicates will be eliminated when converting the list
   * of observables to dependencies to pass to the tracking observer.
   *
   * This should be null unless the _tracker is non null.
   */
  @Nullable
  private ArrayList<Observable> _observables;

  Transaction( @Nonnull final ArezContext context,
               @Nullable final Transaction previous,
               @Nullable final String name,
               @Nonnull final TransactionMode mode,
               @Nullable final Observer tracker )
  {
    super( context, name );
    _previous = previous;
    _mode = mode;
    _tracker = tracker;

    Guards.invariant( () -> TransactionMode.READ_WRITE_OWNED != mode || null != tracker,
                      () -> String.format(
                        "Attempted to create transaction named '%s' with mode READ_WRITE_OWNED but no tracker specified.",
                        getName() ) );
  }

  @Nonnull
  final TransactionMode getMode()
  {
    return _mode;
  }

  final void begin()
  {
    beginTracking();
  }

  final void beginTracking()
  {
    if ( null != _tracker )
    {
      _tracker.invariantDependenciesBackLink( "Pre beginTracking" );
      // Mark the tracker as up to date at the start of the transaction.
      // If it is made stale during the transaction then completeTracking() will fix the
      // state of the _tracker.
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
      // Only the root transactions performs passivations and reschedules reactions.
      passivatePendingPassivations();
      getContext().runPendingReactions();
    }
  }

  final int passivatePendingPassivations()
  {
    Guards.invariant( this::isRootTransaction,
                      () -> String.format(
                        "Invoked passivatePendingPassivations on transaction named '%s' which is not the root transaction.",
                        getName() ) );
    int count = 0;
    if ( null != _pendingPassivations )
    {
      //WARNING: Passivations can be enqueued during the passivation process
      // so we always need to call _pendingPassivations.size() through each iteration
      // of the loop to ensure that new pending passivations are passivated.
      //noinspection ForLoopReplaceableByForEach
      for ( int i = 0; i < _pendingPassivations.size(); i++ )
      {
        final Observable observable = _pendingPassivations.get( i );
        observable.resetPendingPassivation();
        if ( !observable.hasObservers() )
        {
          observable.passivate();
          count++;
        }
      }
    }
    return count;
  }

  final void queueForPassivation( @Nonnull final Observable observable )
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

  final void observe( @Nonnull final Observable observable )
  {
    if ( null != _tracker )
    {
    /*
     * This optimization attempts to stop the same observable being added multiple
     * times to the observables list by caching the transaction id on the observable.
     * This is optimization may be defeated if the same observable is observed in a
     * nested tracking transaction in which case the same observable may appear multiple.
     * times in the _observables list. However completeTracking will eliminate duplicates.
     */
      final int id = getId();
      if ( observable.getLastTrackerTransactionId() != id )
      {
        observable.setLastTrackerTransactionId( id );
        safeGetObservables().add( observable );
      }
    }
  }

  /**
   * Called to report that this observable has changed.
   * This is called when the observable has definitely changed and should
   * not be called for derived values that may have changed.
   */
  final void reportChanged( @Nonnull final Observable observable )
  {
    verifyWriteAllowed( observable );
    observable.invariantLeastStaleObserverState();

    if ( ObserverState.STALE != observable.getLeastStaleObserverState() )
    {
      observable.setLeastStaleObserverState( ObserverState.STALE );
      for ( final Observer observer : observable.getObservers() )
      {
        final ObserverState state = observer.getState();
        if ( ObserverState.UP_TO_DATE == state )
        {
          observer.setState( ObserverState.STALE );
        }
        else
        {
          Guards.invariant( () -> ObserverState.POSSIBLY_STALE != state,
                            () -> String.format( "Transaction named '%s' has attempted to explicitly change observable " +
                                                 "named '%s' but observable is in state POSSIBLY_STALE indicating it is " +
                                                 "derived and thus can not be explicitly changed.",
                                                 getName(),
                                                 observable.getName() ) );
          Guards.invariant( () -> ObserverState.STALE == state,
                            () -> String.format( "Transaction named '%s' has attempted to explicitly change observable " +
                                                 "named '%s' and observable is in unexpected state %s.",
                                                 getName(),
                                                 observable.getName(),
                                                 state.name() ) );
        }
      }
    }
    observable.invariantLeastStaleObserverState();
  }

  final void reportMaybeChanged( @Nonnull final Observable observable )
  {
    verifyWriteAllowed( observable );
    observable.invariantLeastStaleObserverState();
    if ( ObserverState.UP_TO_DATE == observable.getLeastStaleObserverState() )
    {
      observable.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );
      for ( final Observer observer : observable.getObservers() )
      {
        if ( ObserverState.UP_TO_DATE == observer.getState() )
        {
          observer.setState( ObserverState.POSSIBLY_STALE );
        }
      }
    }
    observable.invariantLeastStaleObserverState();
  }

  final void reportChangeConfirmed( @Nonnull final Observable observable )
  {
    verifyWriteAllowed( observable );
    observable.invariantLeastStaleObserverState();
    if ( ObserverState.STALE != observable.getLeastStaleObserverState() )
    {
      observable.setLeastStaleObserverState( ObserverState.STALE );

      for ( final Observer observer : observable.getObservers() )
      {
        if ( ObserverState.POSSIBLY_STALE == observer.getState() )
        {
          observer.setState( ObserverState.STALE );
        }
        else if ( ObserverState.UP_TO_DATE == observer.getState() )
        {
          // this happens during computing of `observer`, just keep _leastStaleObserverState up to date.
          observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
        }
      }
    }
    observable.invariantLeastStaleObserverState();
  }

  final void verifyWriteAllowed( @Nonnull final Observable observable )
  {
    if ( ArezConfig.enforceTransactionType() )
    {
      if ( TransactionMode.READ_ONLY == _mode )
      {
        Guards.fail( () -> String.format(
          "Transaction named '%s' attempted to change observable named '%s' but transaction is READ_ONLY.",
          getName(),
          observable.getName() ) );
      }
      else if ( TransactionMode.READ_WRITE_OWNED == _mode )
      {
        Guards.invariant( () -> !observable.hasObservers() || observable.getOwner() == _tracker,
                          () -> String.format(
                            "Transaction named '%s' attempted to change observable named '%s' and transaction is " +
                            "READ_WRITE_OWNED but the observable has not been created by the transaction.",
                            getName(),
                            observable.getName() ) );
      }
    }
  }

  /**
   * Completes the tracking by updating the dependencies on the observer to match the
   * observables that were observed during tracking. The _tracker is added or removed
   * as an observer on an observable if the observer is a new dependency or previously
   * was a dependency but no longer is, respectively.
   */
  final void completeTracking()
  {
    if ( null == _tracker )
    {
      Guards.invariant( () -> null == _observables,
                        () -> "No associated tracker so _observables should be null." );
      return;
    }
    _tracker.invariantDependenciesUnique( "Pre completeTracking" );
    Guards.invariant( () -> _tracker.getState() != ObserverState.NOT_TRACKING,
                      () -> "completeTracking expects derivation.dependenciesState != NOT_TRACKING" );

    ObserverState newDerivationState = ObserverState.UP_TO_DATE;

    boolean dependenciesChanged = false;
    int currentIndex = 0;
    if ( null != _observables )
    {
      /*
       * Iterate through the list of observables, flagging observables and "removing" duplicates.
       */
      final int size = _observables.size();
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

          final Observer owner = observable.getOwner();
          if ( null != owner )
          {
            final ObserverState dependenciesState = owner.getState();
            if ( dependenciesState.ordinal() > newDerivationState.ordinal() )
            {
              newDerivationState = dependenciesState;
            }
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
        // Old dependency was not part of current tracking and needs to be unobserved
        observable.removeObserver( _tracker );
        dependenciesChanged = true;
      }
      else
      {
        observable.removeFromCurrentTracking();
      }
    }

    if ( null != _observables )
    {
      // Look through the new observables and any that are still flagged must be
      // new dependencies and need to be observed by the observer
      for ( int i = currentIndex - 1; i >= 0; i-- )
      {
        final Observable observable = _observables.get( i );
        if ( observable.isInCurrentTracking() )
        {
          observable.removeFromCurrentTracking();
          //Observable was not a dependency so it needs to be observed
          observable.addObserver( _tracker );
          dependenciesChanged = true;
        }
      }
    }

    // Some newly observed derivation owned observables may have become stale during
    // tracking operation but they have had no chance to propagate staleness to this
    // observer so rectify this.
    if ( ObserverState.UP_TO_DATE != newDerivationState )
    {
      _tracker.setState( newDerivationState );
    }

    // Ugly hack to remove the elements from the end of the list that are no longer
    // required. We start from end of list and work back to avoid array copies.
    // We should replace _observables with a structure that works under both JS and Java
    // that avoids this by just allowing us to change current size
    if ( null != _observables )
    {
      for ( int i = _observables.size() - 1; i >= currentIndex; i-- )
      {
        _observables.remove( i );
      }

      if ( dependenciesChanged )
      {
        _tracker.replaceDependencies( _observables );
      }
    }
    else
    {
      if ( dependenciesChanged )
      {
        _tracker.replaceDependencies( new ArrayList<>() );
      }
    }

    /*
     * Check invariants. In both java and non-java code this will be compiled out.
     */
    if ( ArezConfig.checkInvariants() )
    {
      if ( null != _observables )
      {
        for ( final Observable observable : _observables )
        {
          observable.invariantLeastStaleObserverState();
          observable.invariantObserversLinked();
        }
      }
      _tracker.invariantDependenciesUnique( "Post completeTracking" );
      _tracker.invariantDependenciesBackLink( "Post completeTracking" );
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

  /**
   * Return the observables, initializing the array if necessary.
   */
  @Nonnull
  final ArrayList<Observable> safeGetObservables()
  {
    if ( null == _observables )
    {
      _observables = new ArrayList<>();
    }
    return _observables;
  }

  @TestOnly
  @Nullable
  final Observer getTracker()
  {
    return _tracker;
  }

  @TestOnly
  @Nullable
  final ArrayList<Observable> getPendingPassivations()
  {
    return _pendingPassivations;
  }

  @TestOnly
  @Nullable
  final ArrayList<Observable> getObservables()
  {
    return _observables;
  }
}
