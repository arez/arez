package arez;

import arez.spy.TransactionCompletedEvent;
import arez.spy.TransactionStartedEvent;
import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.anodoc.TestOnly;
import org.realityforge.anodoc.VisibleForTesting;
import org.realityforge.braincheck.BrainCheckConfig;
import static org.realityforge.braincheck.Guards.*;

/**
 * Representation of transactions within an Arez system.
 */
final class Transaction
{
  /**
   * All changes in a context must occur within the scope of a transaction.
   * This references the current active transaction.
   */
  @Nullable
  private static Transaction c_transaction;
  /**
   * Flag indicating whether this transaction is currently suspended or not.
   * Suspended transactions are transactions that will not collect dependencies and will not allow
   * the current thread to access or mutate observable data. Suspended transactions can also not
   * have other transactions begin until it has been resumed.
   */
  private static boolean c_suspended;
  /**
   * Reference to the system to which this transaction belongs.
   */
  @Nonnull
  private final ArezContext _context;
  /**
   * A human consumable name for transaction. It should be non-null if {@link Arez#areNamesEnabled()} returns
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
   * It is only set if {@link ArezConfig#enforceTransactionType()} returns true.
   */
  @Nullable
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
   * Reference to a previous transaction in the same context.
   * To determine this transaction, the code walks back the chain of _previous transactions looking for
   * one with the same context.
   */
  @Nullable
  private final Transaction _previousInSameContext;
  /**
   * Time at which transaction started. Is only set if {@link Arez#areSpiesEnabled()} return true.
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
  private ArrayList<Observable<?>> _observables;
  /**
   * Flag set to true when the current tracker should be disposed at the end.
   */
  private boolean _disposeTracker;

  /**
   * Return true if there is a transaction for speciffied context in progress.
   *
   * @return true if there is a transaction for speciffied context in progress.
   */
  static boolean isTransactionActive( @Nonnull final ArezContext context )
  {
    return null != c_transaction &&
           !c_suspended &&
           ( !Arez.areZonesEnabled() || c_transaction.getContext() == context );
  }

  /**
   * Return the current transaction.
   * This method should not be invoked unless a transaction active and will throw an
   * exception if invariant checks are enabled.
   *
   * @return the current transaction.
   */
  @Nonnull
  static Transaction current()
  {
    invariant( () -> null != c_transaction,
               () -> "Attempting to get current transaction but no transaction is active." );
    invariant( () -> !c_suspended,
               () -> "Attempting to get current transaction but transaction is suspended." );
    assert null != c_transaction;
    return c_transaction;
  }

  /**
   * Create a new transaction.
   *
   * @param context the associated context.
   * @param name    the name of the transaction. Should be non-null if {@link Arez#areNamesEnabled()} is true, false otherwise.
   * @param mode    the transaction mode.
   * @param tracker the observer that is tracking transaction if any.
   * @return the new transaction.
   */
  static Transaction begin( @Nonnull final ArezContext context,
                            @Nullable final String name,
                            @Nullable final TransactionMode mode,
                            @Nullable final Observer tracker )
  {
    if ( ArezConfig.enforceTransactionType() && TransactionMode.READ_WRITE == mode && null != c_transaction )
    {
      apiInvariant( () -> TransactionMode.READ_WRITE == c_transaction.getMode(),
                    () -> "Attempting to create READ_WRITE transaction named '" + name + "' but it is " +
                          "nested in transaction named '" + c_transaction.getName() + "' with mode " +
                          c_transaction.getMode().name() + " which is not equal to READ_WRITE." );
    }
    if ( null != c_transaction && !Arez.areZonesEnabled() )
    {
      invariant( () -> c_transaction.getContext() == context,
                 () -> "Zones are not enabled but the transaction named '" + name + "' is " +
                       "nested in a transaction named '" + c_transaction.getName() + "' from a " +
                       "different context." );
    }
    invariant( () -> !c_suspended,
               () -> "Attempted to create transaction named '" + name + "' while " +
                     "nested in a suspended transaction named '" + c_transaction.getName() + "'." );

    c_transaction = new Transaction( context, c_transaction, name, mode, tracker );
    context.disableScheduler();
    c_transaction.begin();
    if ( context.willPropagateSpyEvents() )
    {
      assert null != name;
      final ObserverInfoImpl trackerInfo =
        null != tracker ? new ObserverInfoImpl( c_transaction.getContext().getSpy(), tracker ) : null;
      final boolean mutation =
        !ArezConfig.enforceTransactionType() || TransactionMode.READ_WRITE == c_transaction.getMode();
      context.getSpy().reportSpyEvent( new TransactionStartedEvent( name, mutation, trackerInfo ) );
    }
    return c_transaction;
  }

  /**
   * Commit the supplied transaction.
   *
   * This method verifies that the transaction active is the supplied transaction before committing
   * the transaction and restoring the prior transaction if any.
   *
   * @param transaction the transaction.
   */
  static void commit( @Nonnull final Transaction transaction )
  {
    invariant( () -> null != c_transaction,
               () -> "Attempting to commit transaction named '" + transaction.getName() +
                     "' but no transaction is active." );
    assert null != c_transaction;
    invariant( () -> c_transaction == transaction,
               () -> "Attempting to commit transaction named '" + transaction.getName() + "' but this does " +
                     "not match existing transaction named '" + c_transaction.getName() + "'." );
    invariant( () -> !c_suspended,
               () -> "Attempting to commit transaction named '" + transaction.getName() +
                     "' transaction is suspended." );
    c_transaction.commit();
    if ( c_transaction.getContext().willPropagateSpyEvents() )
    {
      final String name = c_transaction.getName();
      final boolean mutation =
        !ArezConfig.enforceTransactionType() || TransactionMode.READ_WRITE == c_transaction.getMode();
      final Observer tracker = c_transaction.getTracker();
      final ObserverInfoImpl trackerInfo =
        null != tracker ? new ObserverInfoImpl( c_transaction.getContext().getSpy(), tracker ) : null;
      final long duration = System.currentTimeMillis() - c_transaction.getStartedAt();
      c_transaction.getContext().getSpy().
        reportSpyEvent( new TransactionCompletedEvent( name, mutation, trackerInfo, duration ) );
    }
    final Transaction previousInSameContext =
      Arez.areZonesEnabled() ? c_transaction.getPreviousInSameContext() : c_transaction.getPrevious();
    if ( null == previousInSameContext )
    {
      c_transaction.getContext().enableScheduler();
    }
    c_transaction = c_transaction.getPrevious();
  }

  /**
   * Suspend specified transaction and stop it collecting dependencies.
   * It should be the current transaction.
   *
   * @param transaction the transaction.
   */
  static void suspend( @Nonnull final Transaction transaction )
  {
    invariant( () -> null != c_transaction,
               () -> "Attempting to suspend transaction named '" + transaction.getName() +
                     "' but no transaction is active." );
    assert null != c_transaction;
    invariant( () -> c_transaction == transaction,
               () -> "Attempting to suspend transaction named '" + transaction.getName() + "' but this does " +
                     "not match existing transaction named '" + c_transaction.getName() + "'." );
    invariant( () -> !c_suspended,
               () -> "Attempting to suspend transaction named '" + transaction.getName() +
                     "' but transaction is already suspended." );
    c_suspended = true;
  }

  /**
   * Resume specified transaction and start it collecting dependencies again.
   * It should be the current transaction.
   *
   * @param transaction the transaction.
   */
  static void resume( @Nonnull final Transaction transaction )
  {
    invariant( () -> null != c_transaction,
               () -> "Attempting to resume transaction named '" + transaction.getName() +
                     "' but no transaction is active." );
    assert null != c_transaction;
    invariant( () -> c_transaction == transaction,
               () -> "Attempting to resume transaction named '" + transaction.getName() + "' but this does " +
                     "not match existing transaction named '" + c_transaction.getName() + "'." );
    invariant( () -> c_suspended,
               () -> "Attempting to resume transaction named '" + transaction.getName() +
                     "' but transaction is not suspended." );
    c_suspended = false;
  }

  Transaction( @Nonnull final ArezContext context,
               @Nullable final Transaction previous,
               @Nullable final String name,
               @Nullable final TransactionMode mode,
               @Nullable final Observer tracker )
  {
    invariant( () -> Arez.areNamesEnabled() || null == name,
               () -> "Transaction passed a name '" + name + "' but Arez.areNamesEnabled() is false" );
    _context = Objects.requireNonNull( context );
    _name = Arez.areNamesEnabled() ? Objects.requireNonNull( name ) : null;
    _id = context.nextTransactionId();
    _previous = previous;
    _previousInSameContext = Arez.areZonesEnabled() ? findPreviousTransactionInSameContext() : null;
    _mode = ArezConfig.enforceTransactionType() ? Objects.requireNonNull( mode ) : null;
    _tracker = tracker;
    _startedAt = Arez.areSpiesEnabled() ? System.currentTimeMillis() : 0;

    invariant( () -> TransactionMode.READ_WRITE_OWNED != mode || null != tracker,
               () -> "Attempted to create transaction named '" + getName() +
                     "' with mode READ_WRITE_OWNED but no tracker specified." );
  }

  /**
   * Walk the tree looking for transaction in the same context.
   */
  @Nullable
  private Transaction findPreviousTransactionInSameContext()
  {
    Transaction t = _previous;
    while ( null != t )
    {
      if ( t.getContext() == _context )
      {
        return t;
      }
      t = t.getPrevious();
    }
    return null;
  }

  /**
   * Return the name of the transaction.
   * This method should NOT be invoked unless {@link Arez#areNamesEnabled()} returns true and will throw an
   * exception if invariant checking is enabled.
   *
   * @return the name of the transaction.
   */
  @Nonnull
  String getName()
  {
    invariant( Arez::areNamesEnabled,
               () -> "Transaction.getName() invoked when Arez.areNamesEnabled() is false" );
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
    invariant( Arez::areSpiesEnabled,
               () -> "Transaction.getStartedAt() invoked when Arez.areSpiesEnabled() is false" );
    return _startedAt;
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public String toString()
  {
    return Arez.areNamesEnabled() ? getName() : super.toString();
  }

  void markTrackerAsDisposed()
  {
    invariant( () -> null != _tracker,
               () -> "Attempted to invoke markTrackerAsDisposed on transaction named '" + getName() +
                     "' when there is no tracker associated with the transaction." );
    invariant( () -> !ArezConfig.enforceTransactionType() || TransactionMode.READ_WRITE == getMode(),
               () -> "Attempted to invoke markTrackerAsDisposed on transaction named '" + getName() +
                     "' when the transaction mode is " + getMode().name() + " and not READ_WRITE." );
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
    assert null != _mode;
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
      if ( !_tracker.isDisposing() )
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

  @VisibleForTesting
  @Nullable
  Transaction getPreviousInSameContext()
  {
    invariant( Arez::areZonesEnabled,
               () -> "Attempted to invoke getPreviousInSameContext() on transaction named '" + getName() +
                     "' when zones are not enabled." );
    return _previousInSameContext;
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
    invariant( this::isRootTransaction,
               () -> "Invoked processPendingDeactivations on transaction named '" + getName() +
                     "' which is not the root transaction." );
    int count = 0;
    if ( null != _pendingDeactivations )
    {
      // Deactivations can be enqueued during the deactivation process so we always pop the last
      while ( !_pendingDeactivations.isEmpty() )
      {
        final Observable observable = _pendingDeactivations.remove( _pendingDeactivations.size() - 1 );
        observable.resetPendingDeactivation();
        if ( observable.isDisposed() )
        {
          invariant( () -> !observable.hasObservers(),
                     () -> "Attempting to deactivate disposed observable named '" + observable.getName() + "' " +
                           "in transaction named '" + getName() + "' but the observable still has observers." );
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
    invariant( observable::canDeactivate,
               () -> "Invoked queueForDeactivation on transaction named '" + getName() + "' for observable " +
                     "named '" + observable.getName() + "' when observable can not be deactivated." );
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
      invariant( () -> !_pendingDeactivations.contains( observable ),
                 () -> "Invoked queueForDeactivation on transaction named '" + getName() + "' for " +
                       "observable named '" + observable.getName() + "' when pending deactivation " +
                       "already exists for observable." );
    }
    observable.markAsPendingDeactivation();
    _pendingDeactivations.add( Objects.requireNonNull( observable ) );
  }

  void observe( @Nonnull final Observable observable )
  {
    invariant( () -> !observable.isDisposed(),
               () -> "Invoked observe on transaction named '" + getName() + "' for observable named '" +
                     observable.getName() + "' where the observable is disposed." );
    if ( null != _tracker )
    {
      /*
       * This invariant is in place as owned observables are generated by the tracker and thus should not be
       * observed during own generation process.
       */
      invariant( () -> !observable.hasOwner() || _tracker != observable.getOwner(),
                 () -> "Invoked observe on transaction named '" + getName() + "' for observable named '" +
                       observable.getName() + "' where the observable is owned by the tracker." );
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
   * Called before making change to observable.
   * This will check preconditions such as verifying observable is not disposed and observable
   * is writeable in transaction.
   */
  void preReportChanged( @Nonnull final Observable<?> observable )
  {
    invariant( () -> !observable.isDisposed(),
               () -> "Invoked reportChanged on transaction named '" + getName() + "' for observable " +
                     "named '" + observable.getName() + "' where the observable is disposed." );
    verifyWriteAllowed( observable );
  }

  /**
   * Called to report that this observable has changed.
   * This is called when the observable has definitely changed and should
   * not be called for derived values that may have changed.
   */
  void reportChanged( @Nonnull final Observable<?> observable )
  {
    preReportChanged( observable );
    observable.invariantLeastStaleObserverState();

    if ( observable.hasObservers() && ObserverState.STALE != observable.getLeastStaleObserverState() )
    {
      observable.setLeastStaleObserverState( ObserverState.STALE );
      final ArrayList<Observer> observers = observable.getObservers();
      for ( final Observer observer : observers )
      {
        final ObserverState state = observer.getState();
        invariant( () -> ObserverState.INACTIVE != state,
                   () -> "Transaction named '" + getName() + "' has attempted to explicitly " +
                         "change observable named '" + observable.getName() + "' and observable " +
                         "is in unexpected state " + state.name() + "." );
        if ( ObserverState.STALE != state )
        {
          observer.setState( ObserverState.STALE );
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
  void reportPossiblyChanged( @Nonnull final Observable<?> observable )
  {
    invariant( () -> !observable.isDisposed(),
               () -> "Invoked reportPossiblyChanged on transaction named '" + getName() + "' for " +
                     "observable named '" + observable.getName() + "' where the observable is disposed." );
    invariant( observable::hasOwner,
               () -> "Transaction named '" + getName() + "' has attempted to mark observable named '" +
                     observable.getName() + "' as potentially changed but observable is not a derived value." );
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
  void reportChangeConfirmed( @Nonnull final Observable<?> observable )
  {
    invariant( () -> !observable.isDisposed(),
               () -> "Invoked reportChangeConfirmed on transaction named '" +
                     getName() + "' for observable named '" +
                     observable.getName() + "' where the observable is disposed." );
    invariant( observable::hasOwner,
               () -> "Transaction named '" + getName() + "' has attempted to mark observable named '" +
                     observable.getName() + "' as potentially changed but observable is not a derived value." );

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
    if ( BrainCheckConfig.checkInvariants() )
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
        names.add( Arez.areNamesEnabled() ? t.getName() : String.valueOf( t.getId() ) );
        t = t.getPrevious();
      }
      final boolean check = found;
      invariant( () -> check,
                 () -> "Transaction named '" + getName() + "' attempted to call reportChangeConfirmed " +
                       "for observable named '" + observable.getName() + "' and found a dependency named '" +
                       observer.getName() + "' that is UP_TO_DATE but is not the tracker of any " +
                       "transactions in the hierarchy: " + names + "." );
    }
  }

  void verifyWriteAllowed( @Nonnull final Observable observable )
  {
    if ( ArezConfig.enforceTransactionType() )
    {
      if ( TransactionMode.READ_ONLY == _mode )
      {
        fail( () -> "Transaction named '" + getName() + "' attempted to change observable named '" +
                    observable.getName() + "' but transaction is READ_ONLY." );
      }
      else if ( TransactionMode.READ_WRITE_OWNED == _mode )
      {
        apiInvariant( () -> observable.hasOwner() && observable.getOwner() == _tracker,
                      () -> "Transaction named '" + getName() + "' attempted to change observable named '" +
                            observable.getName() + "' and transaction is READ_WRITE_OWNED but the observable " +
                            "has not been created by the transaction." );
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
      invariant( () -> null == _observables,
                 () -> "Transaction named '" + getName() + "' has no associated tracker so " +
                       "_observables should be null but are not." );
      return;
    }
    _tracker.invariantDependenciesUnique( "Pre completeTracking" );
    invariant( () -> _tracker.getState() != ObserverState.INACTIVE || _tracker.isDisposed(),
               () -> "Transaction named '" + getName() + "' called completeTracking but _tracker state " +
                     "of INACTIVE is not expected when tracker has not been disposed." );

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

          if ( observable.hasOwner() )
          {
            final Observer owner = observable.getOwner();
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
    final ArrayList<Observable<?>> dependencies = _tracker.getDependencies();
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
    else if ( _tracker.isDerivation() && !_tracker.getDerivedValue().hasObservers() )
    {
      queueForDeactivation( _tracker.getDerivedValue() );
    }

    /*
     * Check invariants. In both java and non-java code this will be compiled out.
     */
    if ( BrainCheckConfig.checkInvariants() )
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

  @Nullable
  ArrayList<Observable<?>> getObservables()
  {
    return _observables;
  }

  boolean isRootTransaction()
  {
    return Arez.areZonesEnabled() ? null == _previousInSameContext : null == _previous;
  }

  @Nonnull
  Transaction getRootTransaction()
  {
    if ( isRootTransaction() )
    {
      return this;
    }
    else if ( Arez.areZonesEnabled() )
    {
      assert null != _previousInSameContext;
      return _previousInSameContext.getRootTransaction();
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
  ArrayList<Observable<?>> safeGetObservables()
  {
    if ( null == _observables )
    {
      _observables = new ArrayList<>();
    }
    return _observables;
  }

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
  static void setTransaction( @Nullable final Transaction transaction )
  {
    c_transaction = transaction;
  }

  @TestOnly
  static boolean isSuspended()
  {
    return c_suspended;
  }

  @TestOnly
  static void markAsSuspended()
  {
    c_suspended = true;
  }

  @TestOnly
  static void resetSuspendedFlag()
  {
    c_suspended = false;
  }
}
