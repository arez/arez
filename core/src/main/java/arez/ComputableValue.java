package arez;

import arez.spy.ComputableValueCreateEvent;
import arez.spy.ComputableValueDisposeEvent;
import arez.spy.ComputableValueInfo;
import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static arez.Guards.*;

/**
 * The ComputableValue represents an ObservableValue derived from other ObservableValues within
 * the Arez system. The value is calculated lazily. i.e. The ComputableValue will only
 * be calculated if the ComputableValue has observers.
 *
 * <p>It should be noted that the ComputableValue is backed by both an ObservableValue and
 * an Observer. The id's of each of these nodes differ but they share the name and
 * thus while debugging appear to be a single element.</p>
 */
public final class ComputableValue<T>
  extends Node
{
  /**
   * The component that this ComputableValue is contained within.
   * This should only be set if {@link Arez#areNativeComponentsEnabled()} is true but may also be null if
   * the ComputableValue is a "top-level" ComputableValue.
   */
  @Nullable
  private final Component _component;
  /**
   * The underlying observer that watches the dependencies are triggers the recomputation when required.
   */
  private final Observer _observer;
  /**
   * The function that recalculates the value.
   */
  private final SafeFunction<T> _function;
  /**
   * The associated observable value.
   */
  @Nonnull
  private final ObservableValue<T> _observableValue;
  /**
   * The cached value of the computation.
   */
  private T _value;
  /**
   * The error that was thrown the last time that this ComputableValue was derived.
   * If this value is non-null then {@link #_value} should be null. This exception
   * is rethrown every time {@link #get()} is called until the computable value is
   * recalculated.
   */
  private Throwable _error;
  /**
   * A flag indicating whether computation is active. Used when checking
   * invariants to detect when the derivation of the ComputableValue ultimately
   * causes a recalculation of the ComputableValue.
   */
  private boolean _computing;
  /**
   * Flag indicating whether dispose() method has been invoked.
   */
  private boolean _disposed;
  /**
   * The number of times that keepAlive has been called without being released.
   * If this is non-zero then the computedValue should not be deactivated.
   */
  private int _keepAliveRefCount;
  /**
   * Hook action called when the ComputableValue moves to observed state.
   */
  @Nullable
  private final Procedure _onActivate;
  /**
   * Hook action called when the ComputableValue moves to un-observed state from any other state.
   */
  @Nullable
  private final Procedure _onDeactivate;
  /**
   * Hook action called when the ComputableValue moves from the UP_TO_DATE state to STALE or POSSIBLY_STALE.
   */
  @Nullable
  private final Procedure _onStale;
  /**
   * Cached info object associated with element.
   * This should be null if {@link Arez#areSpiesEnabled()} is false;
   */
  @Nullable
  private ComputableValueInfo _info;
  /**
   * The number of times that the value from the ComputableValue has been read.
   * This should not be set unless {@link Arez#areSpiesEnabled()} returns true.
   */
  private int _readCount;
  /**
   * The number of times that the value from the ComputableValue has been computed.
   * This may different from the changeCount if the same value was recalculated.
   * This should not be set unless {@link Arez#areSpiesEnabled()} returns true.
   */
  private int _computeCount;
  /**
   * The number of times that the value from the ComputableValue has actually changed.
   * This is incremented each time the computedValue is activated and each time it is changed.
   * This should not be set unless {@link Arez#areSpiesEnabled()} returns true.
   */
  private int _changeCount;
  /**
   * The number of times that the ComputableValue has deactivated.
   * This helps identify problems where the ComputableValue is repeatedly accessed inside
   * actions without also being observed.
   * This should not be set unless {@link Arez#areSpiesEnabled()} returns true.
   */
  private int _deactivateCount;

  ComputableValue( @Nullable final ArezContext context,
                   @Nullable final Component component,
                   @Nullable final String name,
                   @Nonnull final SafeFunction<T> function,
                   @Nullable final Procedure onActivate,
                   @Nullable final Procedure onDeactivate,
                   @Nullable final Procedure onStale,
                   final int flags )
  {
    super( context, name );
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> Arez.areNativeComponentsEnabled() || null == component,
                 () -> "Arez-0048: ComputableValue named '" + getName() + "' has component specified but " +
                       "Arez.areNativeComponentsEnabled() is false." );
    }
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> Flags.KEEPALIVE != Flags.getScheduleType( flags ) || null == onActivate,
                    () -> "Arez-0039: ArezContext.computable() specified keepAlive = true and did not pass a null for onActivate." );
      apiInvariant( () -> Flags.KEEPALIVE != Flags.getScheduleType( flags ) || null == onDeactivate,
                    () -> "Arez-0045: ArezContext.computable() specified keepAlive = true and did not pass a null for onDeactivate." );
    }
    _component = Arez.areNativeComponentsEnabled() ? component : null;
    _function = Objects.requireNonNull( function );
    _onActivate = onActivate;
    _onDeactivate = onDeactivate;
    _onStale = onStale;
    _value = null;
    _computing = false;
    _observer = new Observer( this, flags );
    _observableValue =
      new ObservableValue<>( context,
                             null,
                             name,
                             _observer,
                             Arez.arePropertyIntrospectorsEnabled() ? this::getValue : null,
                             null );
    if ( Arez.areSpiesEnabled() )
    {
      _readCount = 0;
      _computeCount = 0;
      _changeCount = 0;
      _deactivateCount = 0;
    }
    if ( null != _component )
    {
      _component.addComputableValue( this );
    }
    else if ( Arez.areRegistriesEnabled() )
    {
      getContext().registerComputableValue( this );
    }
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ComputableValueCreateEvent( asInfo() ) );
    }
    if ( Flags.KEEPALIVE == Flags.getScheduleType( flags ) )
    {
      getObserver().initialSchedule();
    }
  }

  /**
   * Return the computable value, calculating the value if it is not up to date.
   * Before invoking this method, a transaction <b>MUST</b> be active but it may be read-only or read-write.
   *
   * @return the computable value.
   */
  public T get()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !_computing,
                    () -> "Arez-0049: Detected a cycle deriving ComputableValue named '" + getName() + "'." );
      apiInvariant( _observer::isNotDisposed,
                    () -> "Arez-0050: ComputableValue named '" + getName() + "' accessed after it has been disposed." );
    }
    getObservableValue().reportObserved();
    if ( Arez.areSpiesEnabled() )
    {
      _readCount++;
    }
    if ( _observer.shouldCompute() )
    {
      _observer.invokeReaction();
    }
    if ( null != _error )
    {
      if ( Arez.shouldCheckInvariants() )
      {
        invariant( () -> null == _value,
                   () -> "Arez-0051: ComputableValue generated a value during computation for ComputableValue named '" +
                         getName() + "' but still has a non-null value." );
      }
      if ( _error instanceof RuntimeException )
      {
        throw (RuntimeException) _error;
      }
      else
      {
        throw (Error) _error;
      }
    }
    return _value;
  }

  /**
   * Invoked when a non-arez dependency of the ComputableValue has changed. The ComputableValue
   * may or may not change as a result of the dependency change but Arez will recalculate
   * the ComputableValue during the normal reaction cycle or when next accessed and will propagate
   * the change at that time if required. This method must be explicitly invoked by the
   * developer if the ComputableValue is derived from non-arez data and that data changes.
   * Before invoking this method, a read-write transaction <b>MUST</b> be active.
   */
  public void reportPossiblyChanged()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( this::isNotDisposed,
                    () -> "Arez-0121: The method reportPossiblyChanged() was invoked on disposed " +
                          "ComputableValue named '" + getName() + "'." );
      apiInvariant( () -> getObserver().areExternalDependenciesAllowed(),
                    () -> "Arez-0085: The method reportPossiblyChanged() was invoked on ComputableValue named '" +
                          getName() + "' but the computable value has not specified the " +
                          "AREZ_OR_EXTERNAL_DEPENDENCIES flag." );
    }
    if ( Arez.shouldEnforceTransactionType() )
    {
      Transaction.current().verifyWriteAllowed( getObservableValue() );
    }
    if ( Arez.shouldCheckInvariants() )
    {
      Transaction.current().markTransactionAsUsed();
    }
    if ( Flags.STATE_UP_TO_DATE == getObserver().getState() )
    {
      getObserver().setState( Flags.STATE_POSSIBLY_STALE );
    }
  }

  /**
   * Dispose the ComputableValue so that it can no longer be used.
   */
  @Override
  public void dispose()
  {
    if ( isNotDisposed() )
    {
      if ( Arez.shouldCheckInvariants() )
      {
        // reportDispose only checks invariant and as we don't perform any other activity within it
        // we can elide this transaction if invariants are disabled
        getContext().safeAction( Arez.areNamesEnabled() ? getName() + ".dispose" : null,
                                 () -> getContext().getTransaction().reportDispose( this ),
                                 Flags.NO_VERIFY_ACTION_REQUIRED );
      }
      _disposed = true;
      _value = null;
      _error = null;
      if ( willPropagateSpyEvents() )
      {
        reportSpyEvent( new ComputableValueDisposeEvent( asInfo() ) );
      }
      if ( null != _component )
      {
        _component.removeComputableValue( this );
      }
      else if ( Arez.areRegistriesEnabled() )
      {
        getContext().deregisterComputableValue( this );
      }
      _observableValue.dispose();
      if ( !_observer.isDisposing() )
      {
        _observer.dispose();
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return _disposed;
  }

  /**
   * Invoke this method to ensure that the ComputableValue is activated and computing
   * a value even if there are no observers. This is used when there is a chance that
   * the value will be accessed multiple times, without being accessed from within a
   * tracking transaction (i.e. the value may only be accessed from actions or may have
   * observers come and go).
   *
   * <p>This method should not be called if the computable value was created with the
   * {@link Flags#KEEPALIVE} as it is never deactivated in that configuration.</p>
   *
   * <p>When the computable value no longer needs to be kept alive the return value
   * from this method should be disposed.</p>
   *
   * @return the object to dispose when no longer need to keep alive.
   */
  @Nonnull
  public Disposable keepAlive()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !getObserver().isKeepAlive(),
                    () -> "Arez-0223: ComputableValue.keepAlive() was invoked on computable value named '" +
                          getName() + "' but invoking this method when the computable value has been configured " +
                          "with the KEEPALIVE flag is invalid as the computable is always activated." );
    }
    incrementKeepAliveRefCount();
    return new Disposable()
    {
      private boolean _disposed;

      @Override
      public void dispose()
      {
        if ( !_disposed )
        {
          _disposed = true;
          decrementKeepAliveRefCount();
        }
      }

      @Override
      public boolean isDisposed()
      {
        return _disposed;
      }
    };
  }

  void incrementKeepAliveRefCount()
  {
    keepAliveInvariants();
    _keepAliveRefCount++;
    if ( 1 == _keepAliveRefCount )
    {
      final ObservableValue<T> observableValue = getObservableValue();
      if ( !observableValue.isActive() )
      {
        final ArezContext context = getContext();
        if ( context.isTransactionActive() )
        {
          get();
        }
        else
        {
          context.scheduleReaction( getObserver() );
          context.triggerScheduler();
        }
      }
    }
  }

  void decrementKeepAliveRefCount()
  {
    _keepAliveRefCount--;
    keepAliveInvariants();
    if ( 0 == _keepAliveRefCount )
    {
      final ObservableValue<T> observableValue = getObservableValue();
      final ArezContext context = getContext();
      if ( context.isTransactionActive() )
      {
        if ( !observableValue.isPendingDeactivation() )
        {
          context.getTransaction().queueForDeactivation( observableValue );
        }
      }
      else
      {
        getContext().safeAction( Arez.areNamesEnabled() ? getName() + ".deactivate" : null,
                                 observableValue::deactivate,
                                 Flags.NO_VERIFY_ACTION_REQUIRED );
      }
    }
  }

  private void keepAliveInvariants()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> _keepAliveRefCount >= 0,
                 () -> "Arez-0165: KeepAliveRefCount on ComputableValue named '" + getName() +
                       "' has an invalid value " + _keepAliveRefCount );
    }
  }

  int getKeepAliveRefCount()
  {
    return _keepAliveRefCount;
  }

  /**
   * Return true if the ComputableValue is currently being computable.
   *
   * @return true if the ComputableValue is currently being computable.
   */
  boolean isComputing()
  {
    return _computing;
  }

  /**
   * Return the Observer created to represent the ComputableValue.
   *
   * @return the Observer created to represent the ComputableValue.
   */
  @Nonnull
  Observer getObserver()
  {
    return _observer;
  }

  /**
   * Return the observable for computable value.
   *
   * @return the observable for the derived value.
   */
  @Nonnull
  ObservableValue<T> getObservableValue()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( this::isNotDisposed,
                 () -> "Arez-0084: Attempted to invoke getObservableValue on disposed ComputableValue " +
                       "named '" + getName() + "'." );
    }
    return _observableValue;
  }

  /**
   * Return the onActivate hook.
   *
   * @return the onActivate hook.
   */
  @Nullable
  Procedure getOnActivate()
  {
    return _onActivate;
  }

  /**
   * Return the onDeactivate hook.
   *
   * @return the onDeactivate hook.
   */
  @Nullable
  Procedure getOnDeactivate()
  {
    return _onDeactivate;
  }

  /**
   * Return the onStale hook.
   *
   * @return the onStale hook.
   */
  @Nullable
  Procedure getOnStale()
  {
    return _onStale;
  }

  /**
   * Compute the new value and compare it to the old value using equality comparator. If
   * the new value differs from the old value, cache the new value.
   */
  void compute()
  {
    if ( Arez.areSpiesEnabled() )
    {
      _computeCount++;
    }
    final T oldValue = _value;
    try
    {
      final T newValue = computeValue();
      if ( !Objects.equals( oldValue, newValue ) )
      {
        _value = newValue;
        _error = null;
        if ( Arez.areSpiesEnabled() )
        {
          _changeCount++;
        }
        getObservableValue().reportChangeConfirmed();
      }
      if ( Arez.shouldCheckApiInvariants() )
      {
        if ( getObserver().areArezDependenciesRequired() )
        {
          final ArrayList<ObservableValue<?>> observableValues = Transaction.current().getObservableValues();
          apiInvariant( () -> null != observableValues && !observableValues.isEmpty(),
                        () -> "Arez-0173: ComputableValue named '" + getName() + "' completed compute but is not " +
                              "observing any properties. As a result compute will never be rescheduled. " +
                              "This is not a ComputableValue candidate." );
        }
      }
    }
    catch ( final Exception e )
    {
      if ( null == _error )
      {
        if ( Arez.areSpiesEnabled() )
        {
          _changeCount++;
        }
        /*
         * This handles the scenario where the computable generates an exception. The observers should still be
         * marked as STALE. When they react to this the computable will throw the exception that was caught.
         */
        _value = null;
        _error = e;
        getObservableValue().reportChangeConfirmed();
      }
      throw e;
    }
  }

  /**
   * Actually invoke the function that calculates the value.
   *
   * @return the computable value.
   */
  T computeValue()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      _computing = true;
    }
    try
    {
      return _function.call();
    }
    finally
    {
      if ( Arez.shouldCheckInvariants() )
      {
        _computing = false;
      }
    }
  }

  /**
   * Return the info associated with this class.
   *
   * @return the info associated with this class.
   */
  @SuppressWarnings( "ConstantConditions" )
  @Nonnull
  ComputableValueInfo asInfo()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areSpiesEnabled,
                 () -> "Arez-0195: ComputableValue.asInfo() invoked but Arez.areSpiesEnabled() returned false." );
    }
    if ( Arez.areSpiesEnabled() && null == _info )
    {
      _info = new ComputableValueInfoImpl( this );
    }
    return Arez.areSpiesEnabled() ? _info : null;
  }

  void completeDeactivate()
  {
    _value = null;
    if ( Arez.areSpiesEnabled() )
    {
      _deactivateCount++;
    }
  }

  @Nullable
  Component getComponent()
  {
    return _component;
  }

  T getValue()
  {
    return _value;
  }

  void setValue( final T value )
  {
    _value = value;
  }

  Throwable getError()
  {
    return _error;
  }

  void setError( final Throwable error )
  {
    _error = error;
  }

  void setDisposed( final boolean disposed )
  {
    _disposed = disposed;
  }

  void setComputing( final boolean computing )
  {
    _computing = computing;
  }

  void setKeepAliveRefCount( final int keepAliveRefCount )
  {
    _keepAliveRefCount = keepAliveRefCount;
  }
}
