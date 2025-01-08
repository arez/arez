package arez;

import arez.spy.ComputableValueCreateEvent;
import arez.spy.ComputableValueDisposeEvent;
import arez.spy.ComputableValueInfo;
import grim.annotations.OmitSymbol;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * The ComputableValue represents an ObservableValue derived from other ObservableValues within
 * the Arez system. The value is calculated lazily. i.e., The ComputableValue will only
 * be calculated if the ComputableValue has observers.
 *
 * <p>It should be noted that the ComputableValue is backed by both an ObservableValue and
 * an Observer. The id's of each of these nodes differ, but they share the name and
 * thus while debugging appears to be a single element.</p>
 */
public final class ComputableValue<T>
  extends Node
{
  /**
   * The component that this ComputableValue is contained within.
   * This should only be set if {@link Arez#areNativeComponentsEnabled()} is true but may also be null if
   * the ComputableValue is a "top-level" ComputableValue.
   */
  @OmitSymbol( unless = "arez.enable_native_components" )
  @Nullable
  private final Component _component;
  /**
   * The underlying observer that watches the dependencies triggers the re-computation when required.
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
   * Flag set to true if computable value can be read outside a transaction.
   */
  private final boolean _readOutsideTransaction;
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
   * Hook action called when the ComputableValue moves to unobserved state from any other state.
   */
  @Nullable
  private final Procedure _onDeactivate;
  /**
   * Cached info object associated with the element.
   * This should be null if {@link Arez#areSpiesEnabled()} is false;
   */
  @OmitSymbol( unless = "arez.enable_spies" )
  @Nullable
  private ComputableValueInfo _info;

  ComputableValue( @Nullable final ArezContext context,
                   @Nullable final Component component,
                   @Nullable final String name,
                   @Nonnull final SafeFunction<T> function,
                   @Nullable final Procedure onActivate,
                   @Nullable final Procedure onDeactivate,
                   final int flags )
  {
    super( context, name );
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> Arez.areNativeComponentsEnabled() || null == component,
                 () -> "Arez-0048: ComputableValue named '" + getName() + "' has component specified but " +
                       "Arez.areNativeComponentsEnabled() is false." );
    }
    _component = Arez.areNativeComponentsEnabled() ? component : null;
    _function = Objects.requireNonNull( function );
    _onActivate = onActivate;
    _onDeactivate = onDeactivate;
    _value = null;
    _computing = false;
    _readOutsideTransaction = Flags.READ_OUTSIDE_TRANSACTION == ( flags & Flags.READ_OUTSIDE_TRANSACTION );
    _observer = new Observer( this, flags & ~Flags.READ_OUTSIDE_TRANSACTION );
    _observableValue =
      new ObservableValue<>( context,
                             null,
                             name,
                             _observer,
                             Arez.arePropertyIntrospectorsEnabled() ? this::getValue : null,
                             null );
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
    if ( Flags.KEEPALIVE == Observer.Flags.getScheduleType( flags ) )
    {
      getObserver().initialSchedule();
    }
  }

  /**
   * Return the computable value, calculating the value if it is not up to date.
   * Before invoking this method, a transaction <b>MUST</b> be active, but it may be read-only or read-write.
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
    if ( _readOutsideTransaction )
    {
      getObservableValue().reportObservedIfTrackingTransactionActive();
    }
    else
    {
      getObservableValue().reportObserved();
    }
    if ( _observer.shouldCompute() )
    {
      if ( _readOutsideTransaction && !getContext().isTrackingTransactionActive() )
      {
        return getContext().rawCompute( this, () -> {
          _observer.invokeReaction();
          return returnResult();
        } );
      }
      else
      {
        _observer.invokeReaction();
      }
    }
    return returnResult();
  }

  private T returnResult()
  {
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
   * may or may not change as a result of the dependency change, but Arez will recalculate
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
    if ( Observer.Flags.STATE_UP_TO_DATE == getObserver().getState() )
    {
      getObserver().setState( Observer.Flags.STATE_POSSIBLY_STALE );
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
                                 ActionFlags.NO_VERIFY_ACTION_REQUIRED );
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

  @Override
  public boolean isDisposed()
  {
    return _disposed;
  }

  /**
   * Invoke this method to ensure that the ComputableValue is activated and computing
   * a value even if there are no observers. This is used when there is a chance that
   * the value will be accessed multiple times, without being accessed from within a
   * tracking transaction (i.e., the value may only be accessed from actions or may have
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
                                 ActionFlags.NO_VERIFY_ACTION_REQUIRED );
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
   * Compute the new value and compare it to the old value using equality comparator. If
   * the new value differs from the old value, cache the new value.
   */
  void compute()
  {
    final Procedure onDeactivate = getOnDeactivate();
    if ( null != onDeactivate )
    {
      Transaction.current().registerOnDeactivationHook( onDeactivate );
    }
    final T oldValue = _value;
    try
    {
      final T newValue = computeValue();
      if ( !Objects.equals( oldValue, newValue ) )
      {
        _value = newValue;
        _error = null;
        getObservableValue().reportChangeConfirmed();
      }
      if ( Arez.shouldCheckApiInvariants() )
      {
        if ( getObserver().areArezDependenciesRequired() )
        {
          final List<ObservableValue<?>> observableValues = Transaction.current().getObservableValues();
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
        /*
         * This handles the scenario where the computable generates an exception. The observers should still be
         * marked as STALE. When they react to this, the computable will throw the exception that was caught.
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
  @OmitSymbol( unless = "arez.enable_spies" )
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
  }

  @OmitSymbol
  @Nullable
  Component getComponent()
  {
    return _component;
  }

  T getValue()
  {
    return _value;
  }

  @OmitSymbol
  void setValue( final T value )
  {
    _value = value;
  }

  Throwable getError()
  {
    return _error;
  }

  @OmitSymbol
  void setError( final Throwable error )
  {
    _error = error;
  }

  @OmitSymbol
  void setDisposed( final boolean disposed )
  {
    _disposed = disposed;
  }

  @OmitSymbol
  void setComputing( final boolean computing )
  {
    _computing = computing;
  }

  @OmitSymbol
  void setKeepAliveRefCount( final int keepAliveRefCount )
  {
    _keepAliveRefCount = keepAliveRefCount;
  }

  /**
   * Flags that can configure ComputableValue instances during creation.
   */
  public static final class Flags
  {
    /**
     * The scheduler will be triggered when the ComputableValue is created to immediately invoke the
     * compute function. This should not be specified if {@link #RUN_LATER} is specified.
     *
     * @see Task.Flags#RUN_NOW
     */
    public static final int RUN_NOW = 1 << 22;
    /**
     * The scheduler will not be triggered when the ComputableValue is created. The ComputableValue
     * is responsible for ensuring that {@link ArezContext#triggerScheduler()} is invoked at a later
     * time. This should not be specified if {@link #RUN_NOW} is specified.
     *
     * @see Task.Flags#RUN_LATER
     */
    public static final int RUN_LATER = 1 << 21;
    /**
     * If passed, then the computable value should not report result to the spy infrastructure.
     *
     * @see Observer.Flags#NO_REPORT_RESULT
     */
    public static final int NO_REPORT_RESULT = 1 << 12;
    /**
     * Indicates that application code cannot invoke {@link ComputableValue#reportPossiblyChanged()}
     * and the {@link ComputableValue} is only recalculated if a dependency is updated.
     *
     * @see arez.annotations.DepType#AREZ
     * @see Observer.Flags#AREZ_DEPENDENCIES
     */
    public static final int AREZ_DEPENDENCIES = 1 << 27;
    /**
     * Flag set if the application code cannot invoke {@link ComputableValue#reportPossiblyChanged()} to
     * indicate that a dependency has changed.
     *
     * @see arez.annotations.DepType#AREZ_OR_NONE
     * @see Observer.Flags#AREZ_OR_NO_DEPENDENCIES
     */
    public static final int AREZ_OR_NO_DEPENDENCIES = 1 << 26;
    /**
     * Indicates that application code can invoke {@link ComputableValue#reportPossiblyChanged()} to
     * indicate some dependency has changed and that the {@link ComputableValue} should recompute.
     *
     * @see arez.annotations.DepType#AREZ_OR_EXTERNAL
     * @see Observer.Flags#AREZ_OR_EXTERNAL_DEPENDENCIES
     */
    public static final int AREZ_OR_EXTERNAL_DEPENDENCIES = 1 << 25;
    /**
     * Flag indicating that the ComputableValue is allowed to observe {@link ComputableValue} instances with a lower priority.
     *
     * @see Observer.Flags#AREZ_OR_EXTERNAL_DEPENDENCIES
     */
    public static final int OBSERVE_LOWER_PRIORITY_DEPENDENCIES = 1 << 30;
    /**
     * The runtime will keep the ComputableValue reacting to dependencies until disposed.
     *
     * @see Observer.Flags#KEEPALIVE
     */
    public static final int KEEPALIVE = 1 << 20;
    /**
     * Highest priority.
     * This priority should be used when the ComputableValue will dispose or release other reactive elements
     * (and thus remove elements from being scheduled).
     * <p>Only one of the PRIORITY_* flags should be applied to ComputableValue.</p>
     *
     * @see arez.annotations.Priority#HIGHEST
     * @see arez.spy.Priority#HIGHEST
     * @see Task.Flags#PRIORITY_HIGHEST
     */
    public static final int PRIORITY_HIGHEST = 0b001 << 15;
    /**
     * High priority.
     * To reduce the chance that downstream elements will react multiple times within a single
     * reaction round, this priority should be used when the ComputableValue may trigger many downstream
     * tasks.
     * <p>Only one of the PRIORITY_* flags should be applied to ComputableValue.</p>
     *
     * @see arez.annotations.Priority#HIGH
     * @see arez.spy.Priority#HIGH
     * @see Task.Flags#PRIORITY_HIGH
     */
    public static final int PRIORITY_HIGH = 0b010 << 15;
    /**
     * Normal priority if no other priority otherwise specified.
     * <p>Only one of the PRIORITY_* flags should be applied to ComputableValue.</p>
     *
     * @see arez.annotations.Priority#NORMAL
     * @see arez.spy.Priority#NORMAL
     * @see Task.Flags#PRIORITY_NORMAL
     */
    public static final int PRIORITY_NORMAL = 0b011 << 15;
    /**
     * Low priority.
     * Usually used to schedule ComputableValues that support reflecting state onto non-reactive
     * application components. i.e., ComputableValue that are used to build html views,
     * perform network operations, etc. These ComputableValues are often at low priority
     * to avoid recalculation of dependencies (i.e. {@link ComputableValue}s) triggering
     * this ComputableValue multiple times within a single reaction round.
     * <p>Only one of the PRIORITY_* flags should be applied to ComputableValue.</p>
     *
     * @see arez.annotations.Priority#LOW
     * @see arez.spy.Priority#LOW
     * @see Task.Flags#PRIORITY_LOW
     */
    public static final int PRIORITY_LOW = 0b100 << 15;
    /**
     * Lowest priority. Use this priority if the ComputableValue may be unobserved when
     * a {@link #PRIORITY_LOW} element reacts. This is used to avoid recomputing state, that is
     * likely to either be unobserved or recomputed as part of another element's reaction.
     * <p>Only one of the PRIORITY_* flags should be applied to ComputableValue.</p>
     *
     * @see arez.annotations.Priority#LOWEST
     * @see arez.spy.Priority#LOWEST
     * @see Task.Flags#PRIORITY_LOWEST
     */
    public static final int PRIORITY_LOWEST = 0b101 << 15;
    /**
     * Mask used to extract priority bits.
     */
    public static final int PRIORITY_MASK = 0b111 << 15;
    /**
     * Flag indicating that the ComputableValue be accessed outside a transaction.
     */
    public static final int READ_OUTSIDE_TRANSACTION = 1 << 14;
    /**
     * Mask that identifies the bits associated with configuration of ComputableValue instances.
     */
    static final int CONFIG_FLAGS_MASK =
      READ_OUTSIDE_TRANSACTION |
      PRIORITY_MASK |
      ( RUN_NOW | RUN_LATER ) |
      OBSERVE_LOWER_PRIORITY_DEPENDENCIES |
      ( AREZ_DEPENDENCIES | AREZ_OR_NO_DEPENDENCIES | AREZ_OR_EXTERNAL_DEPENDENCIES ) |
      KEEPALIVE |
      NO_REPORT_RESULT;
  }
}
