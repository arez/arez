package arez;

import arez.spy.ComputedValueDisposedEvent;
import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * The ComputedValue represents an Observable derived from other observables within
 * the Arez system. The value is calculated lazily. i.e. The ComputedValue will only
 * be calculated if there is current observers on the calculated value.
 *
 * <p>It should be notes that the ComputedValue is backed by both an Observable and
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
   * Flag indicating whether the ComputedValue should be "kept alive". This essentially means it is activated on
   * creation and never deactivates.
   */
  private final boolean _keepAlive;
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

  ComputedValue( @Nullable final ArezContext context,
                 @Nullable final Component component,
                 @Nullable final String name,
                 @Nonnull final SafeFunction<T> function,
                 @Nonnull final Priority priority,
                 final boolean keepAlive,
                 final boolean observeLowerPriorityDependencies )
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
    _value = null;
    _computing = false;
    _keepAlive = keepAlive;
    _observer = new Observer( Arez.areZonesEnabled() ? context : null,
                              null,
                              Arez.areNamesEnabled() ? getName() : null,
                              this,
                              Arez.shouldEnforceTransactionType() ? TransactionMode.READ_WRITE_OWNED : null,
                              o -> o.getContext().action( Arez.areNamesEnabled() ? o.getName() : null,
                                                          Arez.shouldEnforceTransactionType() ? o.getMode() : null,
                                                          false,
                                                          this::compute,
                                                          false,
                                                          o ),
                              priority,
                              false,
                              observeLowerPriorityDependencies );
    if ( null != _component )
    {
      _component.addComputedValue( this );
    }
    else if ( Arez.areRegistriesEnabled() )
    {
      getContext().registerComputedValue( this );
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
    getObservable().reportObserved();
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
        reportSpyEvent( new ComputedValueDisposedEvent( new ComputedValueInfoImpl( getSpy(), this ) ) );
      }
      if ( null != _component )
      {
        _component.removeComputedValue( this );
      }
      else if ( Arez.areRegistriesEnabled() )
      {
        getContext().deregisterComputedValue( this );
      }
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
  Observable<T> getObservable()
  {
    return (Observable<T>) getObserver().getDerivedValue();
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
        getObservable().reportChangeConfirmed();
      }
      if ( Arez.shouldCheckInvariants() )
      {
        final ArrayList<Observable<?>> observables = Transaction.current().getObservables();
        invariant( () -> null != observables && !observables.isEmpty(),
                   () -> "Arez-0173: ComputedValue named '" + getName() + "' completed compute but is not " +
                         "observing any observables and thus will never be rescheduled. " +
                         "This is not a ComputedValue candidate." );
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
        getObservable().reportChangeConfirmed();
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

  @Nullable
  Component getComponent()
  {
    return _component;
  }

  T getValue()
  {
    return _value;
  }

  boolean isKeepAlive()
  {
    return _keepAlive;
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

  void setComputing( final boolean computing )
  {
    _computing = computing;
  }
}
