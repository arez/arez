package arez;

import arez.spy.ObserverInfo;
import arez.spy.TransactionCompletedEvent;
import arez.spy.TransactionInfo;
import arez.spy.TransactionStartedEvent;
import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
  @Nullable
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
  private ArrayList<ObservableValue> _pendingDeactivations;
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
  private ArrayList<ObservableValue<?>> _observableValues;
  /**
   * The flag set if transaction interacts with Arez resources.
   * This should only be accessed when {@link Arez#shouldCheckInvariants()} returns true.
   */
  private boolean _readOrWriteOccurred;
  /**
   * Cached info object associated with element.
   * This should be null if {@link Arez#areSpiesEnabled()} is false;
   */
  @Nullable
  private TransactionInfoImpl _info;

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
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> null != c_transaction,
                 () -> "Arez-0117: Attempting to get current transaction but no transaction is active." );
      invariant( () -> !c_suspended,
                 () -> "Arez-0118: Attempting to get current transaction but transaction is suspended." );
    }
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
    if ( Arez.shouldCheckApiInvariants() && Arez.shouldEnforceTransactionType() )
    {
      if ( null != c_transaction )
      {
        apiInvariant( () -> TransactionMode.READ_WRITE_OWNED != c_transaction.getMode() ||
                            TransactionMode.READ_WRITE_OWNED == mode,
                      () -> "Arez-0186: Attempting to create " + mode + " transaction named '" + name + "' " +
                            "nested in transaction named '" + c_transaction.getName() + "' with mode " +
                            "READ_WRITE_OWNED. ComputedValues must not invoke actions or track methods as " +
                            "they should derive values from other computeds and observables." );
        apiInvariant( () -> TransactionMode.READ_WRITE != mode || TransactionMode.READ_WRITE == c_transaction.getMode(),
                      () -> "Arez-0119: Attempting to create READ_WRITE transaction named '" + name + "' but it is " +
                            "nested in transaction named '" + c_transaction.getName() + "' with mode " +
                            c_transaction.getMode().name() + " which is not equal to READ_WRITE." );
        apiInvariant( () -> c_transaction.getContext() != context ||
                            TransactionMode.READ_WRITE_OWNED == mode ||
                            null == tracker,
                      () -> "Arez-0171: Attempting to create a tracking transaction named '" + name + "' for " +
                            "the observer named '" + Objects.requireNonNull( tracker ).getName() + "' but the " +
                            "transaction is not a top-level transaction when this is required. This may be a result " +
                            "of nesting a track() call inside an action or another observer function." );
      }
    }
    if ( Arez.shouldCheckInvariants() )
    {
      if ( !Arez.areZonesEnabled() && null != c_transaction )
      {
        invariant( () -> c_transaction.getContext() == context,
                   () -> "Arez-0120: Zones are not enabled but the transaction named '" + name + "' is " +
                         "nested in a transaction named '" + c_transaction.getName() + "' from a " +
                         "different context." );
      }
      invariant( () -> !c_suspended,
                 () -> "Arez-0121: Attempted to create transaction named '" + name + "' while " +
                       "nested in a suspended transaction named '" + c_transaction.getName() + "'." );
    }
    c_transaction = new Transaction( Arez.areZonesEnabled() ? context : null, c_transaction, name, mode, tracker );
    context.disableScheduler();
    c_transaction.begin();
    if ( context.willPropagateSpyEvents() )
    {
      assert null != name;
      final ObserverInfo trackerInfo = null != tracker ? tracker.asInfo() : null;
      final boolean mutation =
        !Arez.shouldEnforceTransactionType() || TransactionMode.READ_WRITE == c_transaction.getMode();
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
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> null != c_transaction,
                 () -> "Arez-0122: Attempting to commit transaction named '" + transaction.getName() +
                       "' but no transaction is active." );
      assert null != c_transaction;
      invariant( () -> c_transaction == transaction,
                 () -> "Arez-0123: Attempting to commit transaction named '" + transaction.getName() +
                       "' but this does not match existing transaction named '" + c_transaction.getName() + "'." );
      invariant( () -> !c_suspended,
                 () -> "Arez-0124: Attempting to commit transaction named '" + transaction.getName() +
                       "' transaction is suspended." );
    }
    assert null != c_transaction;
    try
    {
      c_transaction.commit();
      if ( c_transaction.getContext().willPropagateSpyEvents() )
      {
        final String name = c_transaction.getName();
        final boolean mutation =
          !Arez.shouldEnforceTransactionType() || TransactionMode.READ_WRITE == c_transaction.getMode();
        final Observer tracker = c_transaction.getTracker();
        final ObserverInfo trackerInfo = null != tracker ? tracker.asInfo() : null;
        final long duration = System.currentTimeMillis() - c_transaction.getStartedAt();
        c_transaction.getContext().getSpy().
          reportSpyEvent( new TransactionCompletedEvent( name, mutation, trackerInfo, duration ) );
      }
    }
    finally
    {
      // Finally block is required because if an exception occurs during transaction cleanup Arez
      // will be unable to recover as the transaction field will be wrong
      final Transaction previousInSameContext =
        Arez.areZonesEnabled() ? c_transaction.getPreviousInSameContext() : c_transaction.getPrevious();
      if ( null == previousInSameContext )
      {
        c_transaction.getContext().enableScheduler();
      }
      c_transaction = c_transaction.getPrevious();
    }
  }

  /**
   * Suspend specified transaction and stop it collecting dependencies.
   * It should be the current transaction.
   *
   * @param transaction the transaction.
   */
  static void suspend( @Nonnull final Transaction transaction )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> null != c_transaction,
                 () -> "Arez-0125: Attempting to suspend transaction named '" + transaction.getName() +
                       "' but no transaction is active." );
      assert null != c_transaction;
      invariant( () -> c_transaction == transaction,
                 () -> "Arez-0126: Attempting to suspend transaction named '" + transaction.getName() +
                       "' but this does not match existing transaction named '" + c_transaction.getName() + "'." );
      invariant( () -> !c_suspended,
                 () -> "Arez-0127: Attempting to suspend transaction named '" + transaction.getName() +
                       "' but transaction is already suspended." );
    }
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
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> null != c_transaction,
                 () -> "Arez-0128: Attempting to resume transaction named '" + transaction.getName() +
                       "' but no transaction is active." );
      assert null != c_transaction;
      invariant( () -> c_transaction == transaction,
                 () -> "Arez-0129: Attempting to resume transaction named '" + transaction.getName() +
                       "' but this does not match existing transaction named '" + c_transaction.getName() + "'." );
      invariant( () -> c_suspended,
                 () -> "Arez-0130: Attempting to resume transaction named '" + transaction.getName() +
                       "' but transaction is not suspended." );
    }
    c_suspended = false;
  }

  Transaction( @Nullable final ArezContext context,
               @Nullable final Transaction previous,
               @Nullable final String name,
               @Nullable final TransactionMode mode,
               @Nullable final Observer tracker )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> Arez.areNamesEnabled() || null == name,
                 () -> "Arez-0131: Transaction passed a name '" + name + "' but Arez.areNamesEnabled() is false" );
      invariant( () -> Arez.areZonesEnabled() || null == context,
                 () -> "Arez-184: Transaction passed a context but Arez.areZonesEnabled() is false" );
    }
    _context = Arez.areZonesEnabled() ? Objects.requireNonNull( context ) : null;
    _name = Arez.areNamesEnabled() ? Objects.requireNonNull( name ) : null;
    _id = getContext().nextTransactionId();
    _previous = previous;
    _previousInSameContext = Arez.areZonesEnabled() ? findPreviousTransactionInSameContext() : null;
    _mode = Arez.shouldEnforceTransactionType() ? Objects.requireNonNull( mode ) : null;
    _tracker = tracker;
    _startedAt = Arez.areSpiesEnabled() ? System.currentTimeMillis() : 0;

    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> TransactionMode.READ_WRITE_OWNED != mode || null != tracker,
                 () -> "Arez-0132: Attempted to create transaction named '" + getName() +
                       "' with mode READ_WRITE_OWNED but no tracker specified." );
    }
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
      if ( t.getContext() == getContext() )
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
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areNamesEnabled,
                 () -> "Arez-0133: Transaction.getName() invoked when Arez.areNamesEnabled() is false" );
    }
    assert null != _name;
    return _name;
  }

  @Nonnull
  ArezContext getContext()
  {
    return Arez.areZonesEnabled() ? Objects.requireNonNull( _context ) : Arez.context();
  }

  long getStartedAt()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areSpiesEnabled,
                 () -> "Arez-0134: Transaction.getStartedAt() invoked when Arez.areSpiesEnabled() is false" );
    }
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
      if ( Arez.shouldCheckInvariants() )
      {
        _tracker.invariantDependenciesBackLink( "Pre beginTracking" );
      }

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

  @Nullable
  Transaction getPreviousInSameContext()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areZonesEnabled,
                 () -> "Arez-0137: Attempted to invoke getPreviousInSameContext() on transaction named '" + getName() +
                       "' when zones are not enabled." );
    }
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

  boolean hasReadOrWriteOccurred()
  {
    return Arez.shouldCheckInvariants() && _readOrWriteOccurred;
  }

  int processPendingDeactivations()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( this::isRootTransaction,
                 () -> "Arez-0138: Invoked processPendingDeactivations on transaction named '" + getName() +
                       "' which is not the root transaction." );
    }
    int count = 0;
    if ( null != _pendingDeactivations )
    {
      // Deactivations can be enqueued during the deactivation process so we always pop the last
      while ( !_pendingDeactivations.isEmpty() )
      {
        final ObservableValue observableValue = _pendingDeactivations.remove( _pendingDeactivations.size() - 1 );
        observableValue.resetPendingDeactivation();
        if ( Arez.shouldCheckInvariants() )
        {
          if ( observableValue.isDisposed() )
          {
            invariant( () -> !observableValue.hasObservers(),
                       () -> "Arez-0139: Attempting to deactivate disposed observableValue named '" +
                             observableValue.getName() + "' in transaction named '" + getName() +
                             "' but the observableValue still has observers." );
          }
        }
        if ( !observableValue.hasObservers() )
        {
          observableValue.deactivate();
          count++;
        }
      }
    }
    return count;
  }

  void queueForDeactivation( @Nonnull final ObservableValue observableValue )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( observableValue::canDeactivate,
                 () -> "Arez-0140: Invoked queueForDeactivation on transaction named '" +
                       getName() + "' for observableValue named '" + observableValue.getName() + "' when " +
                       "observableValue can not be deactivated." );
    }
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
      if ( Arez.shouldCheckInvariants() )
      {
        invariant( () -> !_pendingDeactivations.contains( observableValue ),
                   () -> "Arez-0141: Invoked queueForDeactivation on transaction named '" + getName() + "' for " +
                         "observableValue named '" + observableValue.getName() + "' when pending deactivation " +
                         "already exists for observableValue." );
      }
    }
    observableValue.markAsPendingDeactivation();
    _pendingDeactivations.add( Objects.requireNonNull( observableValue ) );
  }

  void observe( @Nonnull final ObservableValue observableValue )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      _readOrWriteOccurred = true;
      invariant( observableValue::isNotDisposed,
                 () -> "Arez-0142: Invoked observe on transaction named '" +
                       getName() +
                       "' for observableValue named '" +
                       observableValue.getName() +
                       "' where the observableValue is disposed." );
    }
    if ( null != _tracker )
    {
      /*
       * This invariant is in place as owned observables are generated by the tracker and thus should not be
       * observed during own generation process.
       */
      if ( Arez.shouldCheckInvariants() )
      {
        invariant( () -> !observableValue.hasOwner() || _tracker != observableValue.getOwner(),
                   () -> "Arez-0143: Invoked observe on transaction named '" +
                         getName() +
                         "' for observableValue named '" +
                         observableValue.getName() +
                         "' where the observableValue is owned by the tracker." );
        observableValue.invariantOwner();
      }
      /*
       * This optimization attempts to stop the same observableValue being added multiple
       * times to the observables list by caching the transaction id on the observableValue.
       * This is optimization may be defeated if the same observableValue is observed in a
       * nested tracking transaction in which case the same observableValue may appear multiple.
       * times in the _observableValues list. However completeTracking will eliminate duplicates.
       */
      final int id = getId();
      if ( observableValue.getLastTrackerTransactionId() != id )
      {
        observableValue.setLastTrackerTransactionId( id );
        safeGetObservables().add( observableValue );
      }
    }
  }

  /**
   * Called when disposing a arez node.
   * This will check transaction mode.
   *
   * @param disposable the element being disposed.
   */
  void reportDispose( @Nonnull final Disposable disposable )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( disposable::isNotDisposed,
                 () -> "Arez-0176: Invoked reportDispose on transaction named '" + getName() +
                       "' where the element is disposed." );
      invariant( () -> !Arez.shouldEnforceTransactionType() || TransactionMode.READ_WRITE == getMode(),
                 () -> "Arez-0177: Invoked reportDispose on transaction named '" + getName() +
                       "' but the transaction mode is not READ_WRITE but is " + getMode() + "." );
    }
  }

  /**
   * Called before making change to observableValue.
   * This will check preconditions such as verifying observableValue is not disposed and observableValue
   * is writeable in transaction.
   */
  void preReportChanged( @Nonnull final ObservableValue<?> observableValue )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      _readOrWriteOccurred = true;
      invariant( observableValue::isNotDisposed,
                 () -> "Arez-0144: Invoked reportChanged on transaction named '" + getName() +
                       "' for ObservableValue named '" + observableValue.getName() +
                       "' where the ObservableValue is disposed." );
    }
    verifyWriteAllowed( observableValue );
  }

  /**
   * Called to report that this observableValue has changed.
   * This is called when the observableValue has definitely changed and should
   * not be called for derived values that may have changed.
   */
  void reportChanged( @Nonnull final ObservableValue<?> observableValue )
  {
    preReportChanged( observableValue );
    if ( Arez.shouldCheckInvariants() )
    {
      _readOrWriteOccurred = true;
      observableValue.invariantLeastStaleObserverState();
    }

    if ( observableValue.hasObservers() && ObserverState.STALE != observableValue.getLeastStaleObserverState() )
    {
      observableValue.setLeastStaleObserverState( ObserverState.STALE );
      final ArrayList<Observer> observers = observableValue.getObservers();
      for ( final Observer observer : observers )
      {
        final ObserverState state = observer.getState();
        if ( Arez.shouldCheckInvariants() )
        {
          invariant( () -> ObserverState.INACTIVE != state,
                     () -> "Arez-0145: Transaction named '" + getName() + "' has attempted to explicitly " +
                           "change observableValue named '" + observableValue.getName() + "' and observableValue " +
                           "is in unexpected state " + state.name() + "." );
        }
        if ( ObserverState.STALE != state )
        {
          observer.setState( ObserverState.STALE );
        }
      }
    }
    if ( Arez.shouldCheckInvariants() )
    {
      observableValue.invariantLeastStaleObserverState();
    }
  }

  /**
   * Invoked with a derived observableValue when a dependency of the observableValue has
   * changed. The observableValue may or may not have changed but the framework will
   * recalculate the value during normal reaction cycle or when accessed within
   * transaction scope and will update the state of the observableValue at that time.
   */
  void reportPossiblyChanged( @Nonnull final ObservableValue<?> observableValue )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      _readOrWriteOccurred = true;
      invariant( observableValue::isNotDisposed,
                 () -> "Arez-0146: Invoked reportPossiblyChanged on transaction named '" + getName() + "' for " +
                       "ObservableValue named '" + observableValue.getName() + "' where the ObservableValue" +
                       " is disposed." );
      invariant( observableValue::hasOwner,
                 () -> "Arez-0147: Transaction named '" + getName() + "' has attempted to mark " +
                       "ObservableValue named '" + observableValue.getName() + "' as potentially changed but " +
                       "ObservableValue is not a derived value." );
      invariant( () -> !Arez.shouldEnforceTransactionType() || TransactionMode.READ_ONLY != getMode(),
                 () -> "Arez-0148: Transaction named '" + getName() + "' attempted to call reportPossiblyChanged in " +
                       "read-only transaction." );
      observableValue.invariantLeastStaleObserverState();
    }

    if ( observableValue.hasObservers() && ObserverState.UP_TO_DATE == observableValue.getLeastStaleObserverState() )
    {
      observableValue.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );
      for ( final Observer observer : observableValue.getObservers() )
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
    if ( Arez.shouldCheckInvariants() )
    {
      observableValue.invariantLeastStaleObserverState();
    }
  }

  /**
   * Invoked with a derived observableValue when the derived observableValue is actually
   * changed. This is determined after the value is recalculated and converts
   * a UPTODATE or POSSIBLY_STALE state to STALE.
   */
  void reportChangeConfirmed( @Nonnull final ObservableValue<?> observableValue )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( observableValue::isNotDisposed,
                 () -> "Arez-0149: Invoked reportChangeConfirmed on transaction named '" +
                       getName() + "' for ObservableValue named '" +
                       observableValue.getName() + "' where the ObservableValue is disposed." );
      invariant( observableValue::hasOwner,
                 () -> "Arez-0150: Transaction named '" + getName() + "' has attempted to mark " +
                       "ObservableValue named '" + observableValue.getName() + "' as potentially changed " +
                       "but ObservableValue is not a derived value." );
      observableValue.invariantLeastStaleObserverState();
    }
    verifyWriteAllowed( observableValue );
    if ( observableValue.hasObservers() && ObserverState.STALE != observableValue.getLeastStaleObserverState() )
    {
      observableValue.setLeastStaleObserverState( ObserverState.STALE );

      for ( final Observer observer : observableValue.getObservers() )
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
          if ( Arez.shouldCheckInvariants() )
          {
            invariantObserverIsTracker( observableValue, observer );
          }
          observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
        }
      }
    }
    if ( Arez.shouldCheckInvariants() )
    {
      observableValue.invariantLeastStaleObserverState();
    }
  }

  /**
   * Verifies that the specified observer is a tracker for the current
   * transaction or one of the parent transactions.
   *
   * @param observableValue the observableValue which the observer is observing. Used when constructing invariant message.
   * @param observer        the observer.
   */
  void invariantObserverIsTracker( @Nonnull final ObservableValue observableValue, @Nonnull final Observer observer )
  {
    if ( Arez.shouldCheckInvariants() )
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
                 () -> "Arez-0151: Transaction named '" + getName() + "' attempted to call reportChangeConfirmed " +
                       "for ObservableValue named '" + observableValue.getName() + "' and found a dependency named '" +
                       observer.getName() + "' that is UP_TO_DATE but is not the tracker of any " +
                       "transactions in the hierarchy: " + names + "." );
    }
  }

  void verifyWriteAllowed( @Nonnull final ObservableValue observableValue )
  {
    if ( Arez.shouldEnforceTransactionType() )
    {
      if ( TransactionMode.READ_ONLY == _mode )
      {
        if ( Arez.shouldCheckInvariants() )
        {
          fail( () -> "Arez-0152: Transaction named '" + getName() + "' attempted to change ObservableValue named '" +
                      observableValue.getName() + "' but the transaction mode is READ_ONLY." );
        }
      }
      else if ( TransactionMode.READ_WRITE_OWNED == _mode )
      {
        if ( Arez.shouldCheckInvariants() )
        {
          invariant( () -> observableValue.hasOwner() && observableValue.getOwner() == _tracker,
                     () -> "Arez-0153: Transaction named '" + getName() + "' attempted to change" +
                           " ObservableValue named '" + observableValue.getName() + "' and the transaction mode is " +
                           "READ_WRITE_OWNED but the ObservableValue has not been created by the transaction." );
        }
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
      if ( Arez.shouldCheckInvariants() )
      {
        invariant( () -> null == _observableValues,
                   () -> "Arez-0154: Transaction named '" + getName() + "' has no associated tracker so " +
                         "_observableValues should be null but are not." );
      }
      return;
    }
    if ( Arez.shouldCheckInvariants() )
    {
      _tracker.invariantDependenciesUnique( "Pre completeTracking" );
      invariant( () -> _tracker.getState() != ObserverState.INACTIVE || _tracker.isDisposed(),
                 () -> "Arez-0155: Transaction named '" + getName() + "' called completeTracking but _tracker state " +
                       "of INACTIVE is not expected when tracker has not been disposed." );
    }

    // the newDerivation state should be ObserverState.UP_TO_DATE in most cases
    // as that is what it was set to in beginTracking. However if an observer adds a
    // new observable, the tracker itself is stale and the observable has a LeastStaleObserverState
    // of STALE due to another observer, the newDerivationState value can be incorrect
    // The state tracker can also be disposed within the scope of the transaction which will lead to
    // DISPOSED or DISPOSING state.
    ObserverState newDerivationState = ObserverState.getLeastStaleObserverState( _tracker.getState() );

    boolean dependenciesChanged = false;
    int currentIndex = 0;
    if ( null != _observableValues && !_tracker.isDisposed() )
    {
      /*
       * Iterate through the list of observables, flagging observables and "removing" duplicates.
       */
      final int size = _observableValues.size();
      for ( int i = 0; i < size; i++ )
      {
        final ObservableValue observableValue = _observableValues.get( i );
        if ( !observableValue.isInCurrentTracking() && observableValue.isNotDisposed() )
        {
          observableValue.putInCurrentTracking();
          if ( i != currentIndex )
          {
            _observableValues.set( currentIndex, observableValue );
          }
          currentIndex++;

          if ( observableValue.hasOwner() )
          {
            final Observer owner = observableValue.getOwner();
            final ObserverState dependenciesState = owner.getState();
            if ( dependenciesState == ObserverState.STALE )
            {
              newDerivationState = dependenciesState;
            }
          }
        }
      }
    }

    // Look through the old dependencies and any that are no longer tracked
    // should no longer be observed.
    final ArrayList<ObservableValue<?>> dependencies = _tracker.getDependencies();
    for ( int i = dependencies.size() - 1; i >= 0; i-- )
    {
      final ObservableValue observableValue = dependencies.get( i );
      if ( !observableValue.isInCurrentTracking() )
      {
        // Old dependency was not part of current tracking and needs to be unobserved
        observableValue.removeObserver( _tracker );
        dependenciesChanged = true;
      }
      else
      {
        observableValue.removeFromCurrentTracking();
      }
    }

    // Some newly observed derivation owned observables may have become stale during
    // tracking operation but they have had no chance to propagate staleness to this
    // observer so rectify this. This should NOT reschedule tracker.
    // NOTE: This must occur before subsequent observable.addObserver() calls
    if ( !_tracker.isDisposedOrDisposing() && ObserverState.UP_TO_DATE != newDerivationState )
    {
      if ( _tracker.getState().ordinal() < newDerivationState.ordinal() )
      {
        _tracker.setState( newDerivationState, false );
      }
    }

    if ( null != _observableValues )
    {
      // Look through the new observables and any that are still flagged must be
      // new dependencies and need to be observed by the observer
      for ( int i = currentIndex - 1; i >= 0; i-- )
      {
        final ObservableValue observableValue = _observableValues.get( i );
        if ( observableValue.isInCurrentTracking() )
        {
          observableValue.removeFromCurrentTracking();
          //ObservableValue was not a dependency so it needs to be observed
          observableValue.addObserver( _tracker );
          dependenciesChanged = true;
          if ( Arez.shouldCheckInvariants() )
          {
            final ObserverState leastStaleObserverState = observableValue.getLeastStaleObserverState();
            assert !( ObserverState.isNotActive( leastStaleObserverState ) ||
                      leastStaleObserverState.ordinal() > newDerivationState.ordinal() );
          }
        }
      }
    }

    // Ugly hack to remove the elements from the end of the list that are no longer
    // required. We start from end of list and work back to avoid array copies.
    // We should replace _observableValues with a structure that works under both JS and Java
    // that avoids this by just allowing us to change current size
    if ( null != _observableValues )
    {
      for ( int i = _observableValues.size() - 1; i >= currentIndex; i-- )
      {
        _observableValues.remove( i );
      }

      if ( dependenciesChanged )
      {
        _tracker.replaceDependencies( _observableValues );
      }
    }
    else
    {
      if ( dependenciesChanged )
      {
        _tracker.replaceDependencies( new ArrayList<>() );
      }
    }

    if ( Disposable.isNotDisposed( _tracker ) &&
         _tracker.isComputedValue() &&
         !_tracker.getComputedValue().getObservableValue().hasObservers() &&
         !_tracker.getComputedValue().isKeepAlive() )
    {
      queueForDeactivation( _tracker.getComputedValue().getObservableValue() );
    }

    /*
     * Check invariants. In both java and non-java code this will be compiled out.
     */
    if ( Arez.shouldCheckInvariants() )
    {
      if ( null != _observableValues )
      {
        for ( final ObservableValue observableValue : _observableValues )
        {
          observableValue.invariantLeastStaleObserverState();
          observableValue.invariantObserversLinked();
        }
      }
      _tracker.invariantDependenciesUnique( "Post completeTracking" );
      _tracker.invariantDependenciesBackLink( "Post completeTracking" );
      _tracker.invariantDependenciesNotDisposed();
    }
  }

  @Nullable
  ArrayList<ObservableValue<?>> getObservableValues()
  {
    return _observableValues;
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
  ArrayList<ObservableValue<?>> safeGetObservables()
  {
    if ( null == _observableValues )
    {
      _observableValues = new ArrayList<>();
    }
    return _observableValues;
  }

  @Nullable
  Observer getTracker()
  {
    return _tracker;
  }

  @Nullable
  ArrayList<ObservableValue> getPendingDeactivations()
  {
    return _pendingDeactivations;
  }

  /**
   * Return the info associated with this class.
   *
   * @return the info associated with this class.
   */
  @SuppressWarnings( "ConstantConditions" )
  @Nonnull
  TransactionInfo asInfo()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areSpiesEnabled,
                 () -> "Arez-0198: TransactionInfo.asInfo() invoked but Arez.areSpiesEnabled() returned false." );
    }
    if ( Arez.areSpiesEnabled() && null == _info )
    {
      _info = new TransactionInfoImpl( this );
    }
    return Arez.areSpiesEnabled() ? _info : null;
  }

  static void setTransaction( @Nullable final Transaction transaction )
  {
    c_transaction = transaction;
  }

  static boolean isSuspended()
  {
    return c_suspended;
  }

  static void markAsSuspended()
  {
    c_suspended = true;
  }

  static void resetSuspendedFlag()
  {
    c_suspended = false;
  }
}
