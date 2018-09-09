package arez;

import arez.spy.ComponentInfo;
import arez.spy.ComputedValueInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserverInfo;
import arez.spy.Priority;
import arez.spy.PropertyAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * A implementation of {@link ComputedValueInfo} that proxies to a {@link ComputedValue}.
 */
final class ComputedValueInfoImpl
  implements ComputedValueInfo
{
  private final ComputedValue<?> _computedValue;

  ComputedValueInfoImpl( @Nonnull final ComputedValue<?> computedValue )
  {
    _computedValue = Objects.requireNonNull( computedValue );
  }

  @Nonnull
  private static List<ComputedValueInfo> asInfos( @Nonnull final Collection<ComputedValue<?>> computedValues )
  {
    return computedValues
      .stream()
      .map( ComputedValue::asInfo )
      .collect( Collectors.toList() );
  }

  @Nonnull
  static List<ComputedValueInfo> asUnmodifiableInfos( @Nonnull final Collection<ComputedValue<?>> computedValues )
  {
    return Collections.unmodifiableList( asInfos( computedValues ) );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public String getName()
  {
    return _computedValue.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isComputing()
  {
    return _computedValue.isComputing();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public Priority getPriority()
  {
    return _computedValue.getObserver().getPriority();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isActive()
  {
    return _computedValue.getObserver().isActive();
  }

  @Nonnull
  @Override
  public List<ObservableValueInfo> getDependencies()
  {
    if ( _computedValue.isComputing() )
    {
      final Transaction transaction = getTransactionComputing();
      final ArrayList<ObservableValue<?>> observableValues = transaction.getObservableValues();
      if ( null == observableValues )
      {
        return Collections.emptyList();
      }
      else
      {
        // Copy the list removing any duplicates that may exist.
        final List<ObservableValue<?>> list = observableValues.stream().distinct().collect( Collectors.toList() );
        return ObservableValueInfoImpl.asUnmodifiableInfos( list );
      }
    }
    else
    {
      return ObservableValueInfoImpl.asUnmodifiableInfos( _computedValue.getObserver().getDependencies() );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public List<ObserverInfo> getObservers()
  {
    return ObserverInfoImpl.asUnmodifiableInfos( _computedValue.getObservableValue().getObservers() );
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public ComponentInfo getComponent()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areNativeComponentsEnabled,
                 () -> "Arez-0109: Spy.getComponent invoked when Arez.areNativeComponentsEnabled() returns false." );
    }
    final Component component = _computedValue.getComponent();
    return null == component ? null : component.asInfo();
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public Object getValue()
    throws Throwable
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::arePropertyIntrospectorsEnabled,
                 () -> "Arez-0116: Spy.getValue invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
    }
    final PropertyAccessor<?> accessor = _computedValue.getObservableValue().getAccessor();
    assert null != accessor;
    return accessor.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return _computedValue.isDisposed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return _computedValue.toString();
  }

  @Override
  public boolean equals( final Object o )
  {
    if ( this == o )
    {
      return true;
    }
    else if ( o == null || getClass() != o.getClass() )
    {
      return false;
    }
    else
    {
      final ComputedValueInfoImpl that = (ComputedValueInfoImpl) o;
      return _computedValue.equals( that._computedValue );
    }
  }

  @Override
  public int hashCode()
  {
    return _computedValue.hashCode();
  }

  /**
   * Return the transaction that is computing specified ComputedValue.
   */
  @Nonnull
  Transaction getTransactionComputing()
  {
    assert _computedValue.isComputing();
    final Transaction transaction = getTrackerTransaction( _computedValue.getObserver() );
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> transaction != null,
                 () -> "Arez-0106: ComputedValue named '" + _computedValue.getName() + "' is marked as computing but " +
                       "unable to locate transaction responsible for computing ComputedValue" );
    }
    assert null != transaction;
    return transaction;
  }

  /**
   * Get transaction with specified observer as tracker.
   */
  @Nullable
  private Transaction getTrackerTransaction( @Nonnull final Observer observer )
  {
    Transaction t = _computedValue.getContext().getTransaction();
    while ( null != t && t.getTracker() != observer )
    {
      t = t.getPrevious();
    }
    return t;
  }
}
