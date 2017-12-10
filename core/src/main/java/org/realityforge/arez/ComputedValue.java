package org.realityforge.arez;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.anodoc.TestOnly;
import org.realityforge.arez.spy.ComputedValueDisposedEvent;
import org.realityforge.braincheck.BrainCheckConfig;
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
   * The comparator that determines if two values are equal and thus whether the new value is a change or not.
   */
  private final EqualityComparator<T> _equalityComparator;
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
   * FLag indicating whether dispose() method has been invoked.
   */
  private boolean _disposed;

  ComputedValue( @Nonnull final ArezContext context,
                 @Nullable final Component component,
                 @Nullable final String name,
                 @Nonnull final SafeFunction<T> function,
                 @Nonnull final EqualityComparator<T> equalityComparator )
  {
    super( context, name );
    invariant( () -> Arez.areNativeComponentsEnabled() || null == component,
               () -> "ComputedValue named '" + getName() + "' has component specified but " +
                     "Arez.areNativeComponentsEnabled() is false." );
    _component = component;
    _function = Objects.requireNonNull( function );
    _equalityComparator = Objects.requireNonNull( equalityComparator );
    _value = null;
    _computing = false;
    _observer = new Observer( context,
                              null,
                              Arez.areNamesEnabled() ? getName() : null,
                              this,
                              ArezConfig.enforceTransactionType() ? TransactionMode.READ_WRITE_OWNED : null,
                              o -> o.getContext().action( Arez.areNamesEnabled() ? o.getName() : null,
                                                          ArezConfig.enforceTransactionType() ? o.getMode() : null,
                                                          this::compute,
                                                          false,
                                                          o ),
                              false );
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
    apiInvariant( () -> !_computing,
                  () -> "Detected a cycle deriving ComputedValue named '" + getName() + "'." );
    apiInvariant( _observer::isLive,
                  () -> "ComputedValue named '" + getName() + "' accessed after it has been disposed." );
    getObservable().reportObserved();
    if ( _observer.shouldCompute() )
    {
      _observer.invokeReaction();
    }
    if ( null != _error )
    {
      invariant( () -> null == _value,
                 () -> "ComputedValue generated a value during computation for ComputedValue named '" + getName() +
                       "' but still has a non-null value." );
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
    if ( !isDisposed() )
    {
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
      _observer.dispose();
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
      if ( !_equalityComparator.equals( oldValue, newValue ) )
      {
        _value = newValue;
        _error = null;
        getObservable().reportChangeConfirmed();
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
    if ( BrainCheckConfig.checkInvariants() )
    {
      _computing = true;
    }
    try
    {
      return _function.call();
    }
    finally
    {
      if ( BrainCheckConfig.checkInvariants() )
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

  @TestOnly
  void setValue( final T value )
  {
    _value = value;
  }

  @TestOnly
  Throwable getError()
  {
    return _error;
  }

  @TestOnly
  void setError( final Throwable error )
  {
    _error = error;
  }

  @TestOnly
  void setComputing( final boolean computing )
  {
    _computing = computing;
  }
}
