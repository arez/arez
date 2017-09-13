package org.realityforge.arez;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.realityforge.arez.spy.ComputedValueDisposedEvent;

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
                 @Nullable final String name,
                 @Nonnull final SafeFunction<T> function,
                 @Nonnull final EqualityComparator<T> equalityComparator )
  {
    super( context, name );
    _function = Objects.requireNonNull( function );
    _equalityComparator = Objects.requireNonNull( equalityComparator );
    _value = null;
    _computing = false;
    _observer = new Observer( this );
  }

  /**
   * Return the computed value, calculating the value if it is not up to date.
   *
   * @return the computed value.
   */
  public T get()
  {
    Guards.invariant( () -> !_computing,
                      () -> String.format( "Detected a cycle deriving ComputedValue named '%s'.", getName() ) );
    Guards.invariant( () -> !_observer.isDisposed(),
                      () -> String.format( "ComputedValue named '%s' accessed after it has been disposed.",
                                           getName() ) );
    getObservable().reportObserved();
    if ( _observer.shouldCompute() )
    {
      _observer.invokeReaction();
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
      _observer.dispose();
      _value = null;
      if ( willPropagateSpyEvents() )
      {
        reportSpyEvent( new ComputedValueDisposedEvent( this ) );
      }
      _disposed = true;
    }
  }

  /**
   * Return true if dispose() has been called.
   */
  @Override
  boolean isDisposed()
  {
    return _disposed;
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
  @Nonnull
  Observable getObservable()
  {
    return getObserver().getDerivedValue();
  }

  /**
   * Compute the new value and compare it to the old value using equality comparator. If
   * the new value differs from the old value, cache the new value.
   */
  void compute()
  {
    final T oldValue = _value;
    final T newValue = computeValue();
    if ( !_equalityComparator.equals( oldValue, newValue ) )
    {
      _value = newValue;
      getObservable().reportChangeConfirmed();
    }
  }

  /**
   * Actually invoke the function that calculates the value.
   *
   * @return the computed value.
   */
  T computeValue()
  {
    if ( ArezConfig.checkInvariants() )
    {
      _computing = true;
    }
    try
    {
      return _function.call();
    }
    finally
    {
      if ( ArezConfig.checkInvariants() )
      {
        _computing = false;
      }
    }
  }

  @TestOnly
  void setValue( final T value )
  {
    _value = value;
  }

  @TestOnly
  T getValue()
  {
    return _value;
  }

  @TestOnly
  void setComputing( final boolean computing )
  {
    _computing = computing;
  }
}
