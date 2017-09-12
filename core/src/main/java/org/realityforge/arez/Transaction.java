package org.realityforge.arez;

import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.TestOnly;

final class Transaction
{
  /**
   * Reference to the system to which this transaction belongs.
   */
  @Nonnull
  private final ArezContext _context;
  /**
   * A human consumable name for transaction. It should be non-null if {@link ArezConfig#enableNames()} returns
   * true and <tt>null</tt> otherwise.
   */
  @Nullable
  private final String _name;
  /**
   * Uniquely identifies the transaction within the system.
   * It is used to optimize state tracking.
   */
  private final int _id;
  /**
   * Determines which write operations are permitted within the scope of the transaction if any.
   */
  @Nonnull
  private final TransactionMode _mode;
  /**
   * A list of observables that have reached zero observers within the scope of the root transaction.
   * When the root transaction completes, these observers are deactivated if they still have no observers.
   * It should be noted that this list is owned by the root transaction and a reference is copied to all
   * non-root transactions that attempt to deactivate an observable.
   */
  @Nullable
  private ArrayList<Observable> _pendingDeactivations;
  /**
   * Reference to the transaction that was active when this transaction began. When this
   * transaction commits, the previous transaction will be restored.
   */
  @Nullable
  private final Transaction _previous;
  /**
   * Time at which transaction started. Is only set if {@link ArezConfig#enableSpy()} return true.
   */
  private final long _startedAt;
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
  /**
   * Flag set to true when the current tracker should be disposed at the end.
   */
  private boolean _disposeTracker;

  Transaction( @Nonnull final ArezContext context,
               @Nullable final Transaction previous,
               @Nullable final String name,
               @Nonnull final TransactionMode mode,
               @Nullable final Observer tracker )
  {
    Guards.invariant( () -> ArezConfig.enableNames() || null == name,
                      () -> String.format( "Node passed a name '%s' but ArezConfig.enableNames() is false", name ) );
    _context = Objects.requireNonNull( context );
    _name = ArezConfig.enableNames() ? Objects.requireNonNull( name ) : null;
    _id = context.nextTransactionId();
    _previous = previous;
    _mode = Objects.requireNonNull( mode );
    _tracker = tracker;
    _startedAt = ArezConfig.enableSpy() ? System.currentTimeMillis() : 0;

    Guards.invariant( () -> TransactionMode.READ_WRITE_OWNED != mode || null != tracker,
                      () -> String.format(
                        "Attempted to create transaction named '%s' with mode READ_WRITE_OWNED but no tracker specified.",
                        getName() ) );
  }

  /**
   * Return the name of the transaction.
   * This method should NOT be invoked unless {@link ArezConfig#enableNames()} returns true and will throw an
   * exception if invariant checking is enabled.
   *
   * @return the name of the transaction.
   */
  @Nonnull
  String getName()
  {
    Guards.invariant( ArezConfig::enableNames,
                      () -> "Transaction.getName() invoked when ArezConfig.enableNames() is false" );
    assert null != _name;
    return _name;
  }

  @Nonnull
  ArezContext getContext()
  {
    return _context;
  }

  long getStartedAt()
  {
    Guards.invariant( ArezConfig::enableSpy,
                      () -> "Transaction.getStartedAt() invoked when ArezConfig.enableSpy() is false" );
    return _startedAt;
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public String toString()
  {
    return ArezConfig.enableNames() ? getName() : super.toString();
  }

  void markTrackerAsDisposed()
  {
    Guards.invariant( () -> null != _tracker,
                      () -> String.format( "Attempted to invoke markTrackerAsDisposed on transaction named " +
                                           "'%s' when there is no tracker associated with the transaction.",
                                           getName() ) );
    Guards.invariant( () -> TransactionMode.READ_WRITE == getMode(),
                      () -> String.format( "Attempted to invoke markTrackerAsDisposed on transaction named " +
                                           "'%s' when the transaction mode is %s and not READ_WRITE.",
                                           getName(),
                                           getMode().name() ) );
    assert null != _tracker;
    _tracker.setDisposed( true );
    _disposeTracker = true;
  }

  int getId()
  {
    return _id;
  }

  @Nonnull
  TransactionMode getMode()
  {
    return _mode;
  }

  void begin()
  {
    beginTracking();
  }

  void beginTracking()
  {
    if ( null != _tracker )
    {
      _tracker.invariantDependenciesBackLink( "Pre beginTracking" );

      /*
       * In the scenario where we are disposing the tracker, the tracker will
       * already be marked as disposed so we should not try to set the state to
       * up to date and will instead rely on completeTracking to force the state
       * to INACTIVE.
       */
      if ( !_tracker.isDisposed() )
      {
        // Mark the tracker as up to date at the start of the transaction.
        // If it is made stale during the transaction then completeTracking() will fix the
        // state of the _tracker.
        _tracker.setState( ObserverState.UP_TO_DATE );
      }
      // Ensure dependencies "LeastStaleObserverState" state is kept up to date.
      _tracker.markDependenciesLeastStaleObserverAsUpToDate();
    }
  }

  @Nullable
  Transaction getPrevious()
  {
    return _previous;
  }

  void commit()
  {
    completeTracking();
    if ( isRootTransaction() )
    {
      // Only the root transactions performs deactivations.
      processPendingDeactivations();
    }
  }

  int processPendingDeactivations()
  {
    Guards.invariant( this::isRootTransaction,
                      () -> String.format(
                        "Invoked processPendingDeactivations on transaction named '%s' which is not the root transaction.",
                        getName() ) );
    int count = 0;
    if ( null != _pendingDeactivations )
    {
      // WARNING: Deactivationss can be enqueued during the deactivation process
      // so we always need to call _pendingDeactivations.size() through each iteration
      // of the loop to ensure that new pending deactivations are deactivated.
      //noinspection ForLoopReplaceableByForEach
      for ( int i = 0; i < _pendingDeactivations.size(); i++ )
      {
        final Observable observable = _pendingDeactivations.get( i );
        observable.resetPendingDeactivation();
        if ( observable.isDisposed() )
        {
          Guards.invariant( () -> !observable.hasObservers(),
                            () -> String.format( "Attempting to deactivate disposed observable named '%s' in " +
                                                 "transaction named '%s' but the observable still has observers.",
                                                 observable.getName(), getName() ) );
        }
        if ( !observable.hasObservers() )
        {
          observable.deactivate();
          count++;
        }
      }
    }
    return count;
  }

  void queueForDeactivation( @Nonnull final Observable observable )
  {
    Guards.invariant( observable::canDeactivate,
                      () -> String.format(
                        "Invoked queueForDeactivation on transaction named '%s' for observable named '%s' when observable can not be deactivated.",
                        getName(),
                        observable.getName() ) );
    if ( null == _pendingDeactivations )
    {
      final Transaction rootTransaction = getRootTransaction();
      if ( null == rootTransaction._pendingDeactivations )
      {
        rootTransaction._pendingDeactivations = new ArrayList<>();
      }
      _pendingDeactivations = rootTransaction._pendingDeactivations;
    }
    else
    {
      Guards.invariant( () -> !_pendingDeactivations.contains( observable ),
                        () -> String.format(
                          "Invoked queueForDeactivation on transaction named '%s' for observable named '%s' when pending deactivation already exists for observable.",
                          getName(),
                          observable.getName() ) );
    }
    _pendingDeactivations.add( Objects.requireNonNull( observable ) );
  }

  void observe( @Nonnull final Observable observable )
  {
    Guards.invariant( () -> !observable.isDisposed(),
                      () -> String.format( "Invoked observe on transaction named '%s' for observable named '%s' " +
                                           "where the observable is disposed.", getName(), observable.getName() ) );
    if ( null != _tracker )
    {
      /*
       * This invariant is in place as owned observables are generated by the tracker and thus should not be
       * observed during own generation process.
       */
      Guards.invariant( () -> _tracker != observable.getOwner(),
                        () -> String.format(
                          "Invoked observe on transaction named '%s' for observable named '%s' where the observable is owned by the tracker.",
                          getName(),
                          observable.getName() ) );
      observable.invariantOwner();
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
  void reportChanged( @Nonnull final Observable observable )
  {
    Guards.invariant( () -> !observable.isDisposed(),
                      () -> String.format( "Invoked reportChanged on transaction named '%s' for observable " +
                                           "named '%s' where the observable is disposed.",
                                           getName(),
                                           observable.getName() ) );
    verifyWriteAllowed( observable );
    observable.invariantLeastStaleObserverState();

    if ( observable.hasObservers() && ObserverState.STALE != observable.getLeastStaleObserverState() )
    {
      observable.setLeastStaleObserverState( ObserverState.STALE );
      final ArrayList<Observer> observers = observable.getObservers();
      for ( final Observer observer : observers )
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

  /**
   * Invoked with a derived observable when a dependency of the observable has
   * changed. The observable may or may not have changed but the framework will
   * recalculate the value during normal reaction cycle or when accessed within
   * transaction scope and will update the state of the observable at that time.
   */
  void reportPossiblyChanged( @Nonnull final Observable observable )
  {
    Guards.invariant( () -> !observable.isDisposed(),
                      () -> String.format( "Invoked reportPossiblyChanged on transaction named '%s' for observable " +
                                           "named '%s' where the observable is disposed.",
                                           getName(),
                                           observable.getName() ) );
    Guards.invariant( () -> null != observable.getOwner(),
                      () -> String.format( "Transaction named '%s' has attempted to mark observable " +
                                           "named '%s' as potentially changed but observable is not a derived value.",
                                           getName(),
                                           observable.getName() ) );
    verifyWriteAllowed( observable );
    observable.invariantLeastStaleObserverState();
    if ( observable.hasObservers() && ObserverState.UP_TO_DATE == observable.getLeastStaleObserverState() )
    {
      observable.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );
      for ( final Observer observer : observable.getObservers() )
      {
        final ObserverState state = observer.getState();
        if ( ObserverState.UP_TO_DATE == state )
        {
          observer.setState( ObserverState.POSSIBLY_STALE );
        }
        else
        {
          assert ObserverState.STALE == state || ObserverState.POSSIBLY_STALE == state;
        }
      }
    }
    observable.invariantLeastStaleObserverState();
  }

  /**
   * Invoked with a derived observable when the derived observable is actually
   * changed. This is determined after the value is recalculated and converts
   * a UPTODATE or POSSIBLY_STALE state to STALE.
   */
  void reportChangeConfirmed( @Nonnull final Observable observable )
  {
    Guards.invariant( () -> !observable.isDisposed(),
                      () -> String.format( "Invoked reportChangeConfirmed on transaction named '%s' for observable " +
                                           "named '%s' where the observable is disposed.",
                                           getName(),
                                           observable.getName() ) );
    Guards.invariant( () -> null != observable.getOwner(),
                      () -> String.format( "Transaction named '%s' has attempted to mark observable " +
                                           "named '%s' as potentially changed but observable is not a derived value.",
                                           getName(),
                                           observable.getName() ) );

    verifyWriteAllowed( observable );
    observable.invariantLeastStaleObserverState();
    if ( observable.hasObservers() && ObserverState.STALE != observable.getLeastStaleObserverState() )
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
          /*
           * This happens when the observer is reacting to the change and this
           * has a ComputedValue dependency has recalculated as part of the reaction.
           * So make sure we keep _leastStaleObserverState up to date.
           */
          invariantObserverIsTracker( observable, observer );
          observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
        }
      }
    }
    observable.invariantLeastStaleObserverState();
  }

  /**
   * Verifies that the specified observer is a tracker for the current
   * transaction or one of the parent transactions.
   *
   * @param observable the observable which the observer is observing. Used when constructing invariant message.
   * @param observer   the observer.
   */
  void invariantObserverIsTracker( @Nonnull final Observable observable, @Nonnull final Observer observer )
  {
    // The ArezConfig.checkInvariants() is not needed as the optimizing compilers will eventually
    // eliminate this as dead code but this top level check short-cuts this and ensures that GWT compiler
    // eliminates it in first pass.
    if ( ArezConfig.checkInvariants() )
    {
      boolean found = false;
      Transaction t = this;
      final ArrayList<String> names = new ArrayList<>();
      while ( null != t )
      {
        if ( t.getTracker() == observer )
        {
          found = true;
          break;
        }
        names.add( ArezConfig.enableNames() ? t.getName() : String.valueOf( t.getId() ) );
        t = t.getPrevious();
      }
      final boolean check = found;
      Guards.invariant( () -> check,
                        () -> String.format(
                          "Transaction named '%s' attempted to call reportChangeConfirmed for observable " +
                          "named '%s' and found a dependency named '%s' that is UP_TO_DATE but is not the " +
                          "tracker of any transactions in the hierarchy: %s.",
                          getName(),
                          observable.getName(),
                          observer.getName(),
                          String.valueOf( names ) ) );
    }
  }

  void verifyWriteAllowed( @Nonnull final Observable observable )
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
        Guards.invariant( () -> observable.getOwner() == _tracker,
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
  void completeTracking()
  {
    if ( null == _tracker )
    {
      Guards.invariant( () -> null == _observables,
                        () -> String.format(
                          "Transaction named '%s' has no associated tracker so _observables should be null but are not.",
                          getName() ) );
      return;
    }
    _tracker.invariantDependenciesUnique( "Pre completeTracking" );
    Guards.invariant( () -> _tracker.getState() != ObserverState.INACTIVE || _tracker.isDisposed(),
                      () -> String.format(
                        "Transaction named '%s' called completeTracking but _tracker state of INACTIVE is " +
                        "not expected when tracker has not been disposed.", getName() ) );

    ObserverState newDerivationState = ObserverState.UP_TO_DATE;

    boolean dependenciesChanged = false;
    int currentIndex = 0;
    if ( null != _observables && !_tracker.isDisposed() )
    {
      /*
       * Iterate through the list of observables, flagging observables and "removing" duplicates.
       */
      final int size = _observables.size();
      for ( int i = 0; i < size; i++ )
      {
        final Observable observable = _observables.get( i );
        if ( !observable.isInCurrentTracking() && !observable.isDisposed() )
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

    // Some newly observed derivation owned observables may have become stale during
    // tracking operation but they have had no chance to propagate staleness to this
    // observer so rectify this.
    // NOTE: This must occur before subsequent observable.addObserver() calls
    if ( !_tracker.isDisposed() && ObserverState.UP_TO_DATE != newDerivationState )
    {
      _tracker.setState( newDerivationState );
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
          final ObserverState leastStaleObserverState = observable.getLeastStaleObserverState();
          if ( leastStaleObserverState == ObserverState.INACTIVE ||
               leastStaleObserverState.ordinal() > newDerivationState.ordinal() )
          {
            observable.setLeastStaleObserverState( newDerivationState );
          }
        }
      }
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

    if ( _disposeTracker )
    {
      _tracker.setState( ObserverState.INACTIVE );
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
      _tracker.invariantDependenciesNotDisposed();
    }
  }

  boolean isRootTransaction()
  {
    return null == _previous;
  }

  @Nonnull
  Transaction getRootTransaction()
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
  ArrayList<Observable> safeGetObservables()
  {
    if ( null == _observables )
    {
      _observables = new ArrayList<>();
    }
    return _observables;
  }

  @TestOnly
  @Nullable
  Observer getTracker()
  {
    return _tracker;
  }

  @TestOnly
  @Nullable
  ArrayList<Observable> getPendingDeactivations()
  {
    return _pendingDeactivations;
  }

  @TestOnly
  boolean shouldDisposeTracker()
  {
    return _disposeTracker;
  }

  @TestOnly
  @Nullable
  ArrayList<Observable> getObservables()
  {
    return _observables;
  }
}
