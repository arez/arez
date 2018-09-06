package arez;

import arez.spy.ComputedValueCreatedEvent;
import arez.spy.ComputedValueDisposedEvent;
import arez.spy.ComputedValueInfo;
import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * The ComputedValue represents an ObservableValue derived from other ObservableValues within
 * the Arez system. The value is calculated lazily. i.e. The ComputedValue will only
 * be calculated if there is current observers on the calculated value.
 *
 * <p>It should be noted that the ComputedValue is backed by both an ObservableValue and
 * an Observer. The id's of each of these nodes differ but they share the name and
 * thus while debugging appear to be a single element.</p>
 */
public final class ComputedValue<T>
  extends Node
{
  /**
   * The component that this ComputedValue is contained within.
   * This should only be set if {@link Arez#areNativeComponentsEnabled()} is true but may also be null if
   * the ComputedValue is a "top-level" ComputedValue.
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
   * The error that was thrown the last time that this ComputedValue was derived.
   * If this value is non-null then {@link #_value} should be null. This exception
   * is rethrown every time {@link #get()} is called until the computed value is
   * recalculated.
   */
  private Throwable _error;
  /**
   * A flag indicating whether computation is active. Used when checking
   * invariants to detect when the derivation of the ComputedValue ultimately
   * causes a recalculation of the ComputedValue.
   */
  private boolean _computing;
  /**
   * Flag indicating whether dispose() method has been invoked.
   */
  private boolean _disposed;
  /**
   * Hook action called when the ComputedValue moves to observed state.
   */
  @Nullable
  private final Procedure _onActivate;
  /**
   * Hook action called when the ComputedValue moves to un-observed state from any other state.
   */
  @Nullable
  private final Procedure _onDeactivate;
  /**
   * Hook action called when the ComputedValue moves from the UP_TO_DATE state to STALE or POSSIBLY_STALE.
   */
  @Nullable
  private final Procedure _onStale;
  /**
   * Cached info object associated with element.
   * This should be null if {@link Arez#areSpiesEnabled()} is false;
   */
  @Nullable
  private ComputedValueInfo _info;

  ComputedValue( @Nullable final ArezContext context,
                 @Nullable final Component component,
                 @Nullable final String name,
                 @Nonnull final SafeFunction<T> function,
                 @Nullable final Procedure onActivate,
                 @Nullable final Procedure onDeactivate,
                 @Nullable final Procedure onStale,
                 @Nonnull final Priority priority,
                 final boolean keepAlive,
                 final boolean runImmediately,
                 final boolean observeLowerPriorityDependencies,
                 final boolean arezOnlyDependencies )
  {
    super( context, name );
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> Arez.areNativeComponentsEnabled() || null == component,
                 () -> "Arez-0048: ComputedValue named '" + getName() + "' has component specified but " +
                       "Arez.areNativeComponentsEnabled() is false." );
    }
    _component = Arez.areNativeComponentsEnabled() ? component : null;
    _function = Objects.requireNonNull( function );
    _onActivate = onActivate;
    _onDeactivate = onDeactivate;
    _onStale = onStale;
    _value = null;
    _computing = false;
    final int flags =
      Flags.priorityToFlag( priority ) |
      ( runImmediately ? Flags.REACT_IMMEDIATELY : Flags.DEFER_REACT ) |
      ( keepAlive ? Flags.KEEPALIVE : Flags.DEACTIVATE_ON_UNOBSERVE ) |
      ( Arez.shouldCheckApiInvariants() && !arezOnlyDependencies ? Flags.MANUAL_REPORT_STALE_ALLOWED : 0 ) |
      ( Arez.shouldCheckApiInvariants() && observeLowerPriorityDependencies ?
        Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES :
        0 );
    _observer = new Observer( this, flags );
    _observableValue =
      new ObservableValue<>( context,
                             null,
                             name,
                             _observer,
                             Arez.arePropertyIntrospectorsEnabled() ? this::getValue : null,
                             null );
    if ( null != _component )
    {
      _component.addComputedValue( this );
    }
    else if ( Arez.areRegistriesEnabled() )
    {
      getContext().registerComputedValue( this );
    }
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ComputedValueCreatedEvent( asInfo() ) );
    }
    if ( keepAlive )
    {
      getObserver().initialSchedule();
    }
  }

  /**
   * Return the computed value, calculating the value if it is not up to date.
   *
   * @return the computed value.
   */
  public T get()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !_computing,
                    () -> "Arez-0049: Detected a cycle deriving ComputedValue named '" + getName() + "'." );
      apiInvariant( _observer::isNotDisposed,
                    () -> "Arez-0050: ComputedValue named '" + getName() + "' accessed after it has been disposed." );
    }
    getObservableValue().reportObserved();
    if ( _observer.shouldCompute() )
    {
      _observer.invokeReaction();
    }
    if ( null != _error )
    {
      if ( Arez.shouldCheckInvariants() )
      {
        invariant( () -> null == _value,
                   () -> "Arez-0051: ComputedValue generated a value during computation for ComputedValue named '" +
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
   * Invoked when a non-arez dependency of the ComputedValue has changed. The ComputedValue
   * may or may not change as a result of the dependency change but Arez will recalculate
   * the ComputedValue during the normal reaction cycle or when next accessed and will propagate
   * the change at that time if required. This method must be explicitly invoked by the
   * developer if the ComputedValue is derived from non-arez data and that data changes.
   */
  public void reportPossiblyChanged()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !getObserver().arezOnlyDependencies(),
                    () -> "Arez-0085: The method reportPossiblyChanged() was invoked on ComputedValue named '" +
                          getName() + "' but the computed value has arezOnlyDependencies = true." );
    }
    Transaction.current().verifyWriteAllowed( getObservableValue() );
    if ( Flags.STATE_UP_TO_DATE == getObserver().getState() )
    {
      getObserver().setState( Flags.STATE_POSSIBLY_STALE );
    }
  }

  /**
   * Dispose the ComputedValue so that it can no longer be used.
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
                                 true,
                                 false,
                                 () -> getContext().getTransaction().reportDispose( this ) );
      }
      _disposed = true;
      _value = null;
      _error = null;
      if ( willPropagateSpyEvents() )
      {
        reportSpyEvent( new ComputedValueDisposedEvent( asInfo() ) );
      }
      if ( null != _component )
      {
        _component.removeComputedValue( this );
      }
      else if ( Arez.areRegistriesEnabled() )
      {
        getContext().deregisterComputedValue( this );
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
   * Return true if the ComputedValue is currently being computed.
   *
   * @return true if the ComputedValue is currently being computed.
   */
  boolean isComputing()
  {
    return _computing;
  }

  /**
   * Return the Observer created to represent the ComputedValue.
   *
   * @return the Observer created to represent the ComputedValue.
   */
  @Nonnull
  Observer getObserver()
  {
    return _observer;
  }

  /**
   * Return the observable for computed value.
   *
   * @return the observable for the derived value.
   */
  @SuppressWarnings( "unchecked" )
  @Nonnull
  ObservableValue<T> getObservableValue()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( this::isNotDisposed,
                 () -> "Arez-0084: Attempted to invoke getObservableValue on disposed ComputedValue " +
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
        if ( getObserver().arezOnlyDependencies() )
        {
          final ArrayList<ObservableValue<?>> observableValues = Transaction.current().getObservableValues();
          apiInvariant( () -> null != observableValues && !observableValues.isEmpty(),
                        () -> "Arez-0173: ComputedValue named '" + getName() + "' completed compute but is not " +
                              "observing any properties. As a result compute will never be rescheduled. " +
                              "This is not a ComputedValue candidate." );
        }
      }
    }
    catch ( final Exception e )
    {
      if ( null == _error )
      {
        /*
         * This handles the scenario where the computed generates an exception. The observers should still be
         * marked as STALE. When they react to this the computed will throw the exception that was caught.
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
   * @return the computed value.
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
  ComputedValueInfo asInfo()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areSpiesEnabled,
                 () -> "Arez-0195: ComputedValue.asInfo() invoked but Arez.areSpiesEnabled() returned false." );
    }
    if ( Arez.areSpiesEnabled() && null == _info )
    {
      _info = new ComputedValueInfoImpl( this );
    }
    return Arez.areSpiesEnabled() ? _info : null;
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
}
