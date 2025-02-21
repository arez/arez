package arez;

import arez.spy.ComponentInfo;
import arez.spy.ComputableValueInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserverInfo;
import arez.spy.Priority;
import arez.spy.PropertyAccessor;
import grim.annotations.OmitSymbol;
import grim.annotations.OmitType;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * A implementation of {@link ComputableValueInfo} that proxies to a {@link ComputableValue}.
 */
@OmitType( unless = "arez.enable_spies" )
final class ComputableValueInfoImpl
  implements ComputableValueInfo
{
  private final ComputableValue<?> _computableValue;

  ComputableValueInfoImpl( @Nonnull final ComputableValue<?> computableValue )
  {
    _computableValue = Objects.requireNonNull( computableValue );
  }

  @Nonnull
  private static List<ComputableValueInfo> asInfos( @Nonnull final Collection<ComputableValue<?>> computableValues )
  {
    return computableValues
      .stream()
      .map( ComputableValue::asInfo )
      .collect( Collectors.toList() );
  }

  @Nonnull
  static List<ComputableValueInfo> asUnmodifiableInfos( @Nonnull final Collection<ComputableValue<?>> computableValues )
  {
    return Collections.unmodifiableList( asInfos( computableValues ) );
  }

  @Nonnull
  @Override
  public String getName()
  {
    return _computableValue.getName();
  }

  @Override
  public boolean isComputing()
  {
    return _computableValue.isComputing();
  }

  @Nonnull
  @Override
  public Priority getPriority()
  {
    return _computableValue.getObserver().getTask().getPriority();
  }

  @Override
  public boolean isActive()
  {
    return _computableValue.getObserver().isActive();
  }

  @Nonnull
  @Override
  public List<ObservableValueInfo> getDependencies()
  {
    if ( _computableValue.isComputing() )
    {
      final Transaction transaction = getTransactionComputing();
      final FastList<ObservableValue<?>> observableValues = transaction.getObservableValues();
      if ( null == observableValues )
      {
        return Collections.emptyList();
      }
      else
      {
        // Copy the list removing any duplicates that may exist.
        final List<ObservableValue<?>> list =
          observableValues.stream().distinct().collect( Collectors.toList() );
        return ObservableValueInfoImpl.asUnmodifiableInfos( list );
      }
    }
    else
    {
      return ObservableValueInfoImpl.asUnmodifiableInfos( _computableValue
                                                            .getObserver()
                                                            .getDependencies()
                                                            .stream()
                                                            .collect( Collectors.toList() ) );
    }
  }

  @Nonnull
  @Override
  public List<ObserverInfo> getObservers()
  {
    return ObserverInfoImpl.asUnmodifiableInfos( _computableValue
                                                   .getObservableValue()
                                                   .getObservers()
                                                   .stream()
                                                   .collect( Collectors.toList() ) );
  }

  @Nullable
  @Override
  public ComponentInfo getComponent()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areNativeComponentsEnabled,
                 () -> "Arez-0109: Spy.getComponent invoked when Arez.areNativeComponentsEnabled() returns false." );
    }
    final Component component = _computableValue.getComponent();
    return null == component ? null : component.asInfo();
  }

  @OmitSymbol( unless = "arez.enable_property_introspection" )
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
    final PropertyAccessor<?> accessor = _computableValue.getObservableValue().getAccessor();
    assert null != accessor;
    return accessor.get();
  }

  @Override
  public boolean isDisposed()
  {
    return _computableValue.isDisposed();
  }

  @Override
  public String toString()
  {
    return _computableValue.toString();
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
      final ComputableValueInfoImpl that = (ComputableValueInfoImpl) o;
      return _computableValue.equals( that._computableValue );
    }
  }

  @Override
  public int hashCode()
  {
    return _computableValue.hashCode();
  }

  /**
   * Return the transaction that is computing specified ComputableValue.
   */
  @Nonnull
  Transaction getTransactionComputing()
  {
    assert _computableValue.isComputing();
    final Transaction transaction = getTrackerTransaction( _computableValue.getObserver() );
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> transaction != null,
                 () -> "Arez-0106: ComputableValue named '" + _computableValue.getName() + "' is marked as " +
                       "computing but unable to locate transaction responsible for computing ComputableValue" );
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
    Transaction t = _computableValue.getContext().getTransaction();
    while ( null != t && t.getTracker() != observer )
    {
      t = t.getPrevious();
    }
    return t;
  }
}
