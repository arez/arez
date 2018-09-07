package arez;

import arez.spy.ComponentInfo;
import arez.spy.ComputedValueInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserverInfo;
import arez.spy.Spy;
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
 * A implementation of {@link ObserverInfo} that proxies to a {@link Observer}.
 */
final class ObserverInfoImpl
  implements ObserverInfo
{
  private final Spy _spy;
  private final Observer _observer;

  ObserverInfoImpl( @Nonnull final Spy spy, @Nonnull final Observer observer )
  {
    _spy = Objects.requireNonNull( spy );
    _observer = Objects.requireNonNull( observer );
  }

  @Nonnull
  private static List<ObserverInfo> asInfos( @Nonnull final Collection<Observer> observers )
  {
    return observers
      .stream()
      .map( Observer::asInfo )
      .collect( Collectors.toList() );
  }

  @Nonnull
  static List<ObserverInfo> asUnmodifiableInfos( @Nonnull final Collection<Observer> observers )
  {
    return Collections.unmodifiableList( asInfos( observers ) );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public String getName()
  {
    return _observer.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isActive()
  {
    return _observer.isActive();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRunning()
  {
    return _spy.isTransactionActive() && null != getTrackerTransaction();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isScheduled()
  {
    return _observer.isScheduled();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isComputedValue()
  {
    return _observer.isComputedValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isReadOnly()
  {
    return Arez.shouldEnforceTransactionType() && !_observer.isMutation();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public Priority getPriority()
  {
    return _observer.getPriority();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComputedValueInfo asComputedValue()
  {
    return _observer.getComputedValue().asInfo();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public List<ObservableValueInfo> getDependencies()
  {
    final Transaction transaction = _spy.isTransactionActive() ? getTrackerTransaction() : null;
    if ( null != transaction )
    {
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
      return ObservableValueInfoImpl.asUnmodifiableInfos( _observer.getDependencies() );
    }
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
                 () -> "Arez-0108: Spy.getComponent invoked when Arez.areNativeComponentsEnabled() returns false." );
    }
    final Component component = _observer.getComponent();
    return null == component ? null : component.asInfo();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return _observer.isDisposed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return _observer.toString();
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
      final ObserverInfoImpl that = (ObserverInfoImpl) o;
      return _observer.equals( that._observer );
    }
  }

  @Override
  public int hashCode()
  {
    return _observer.hashCode();
  }

  /**
   * Get transaction with specified observer as tracker.
   */
  @Nullable
  private Transaction getTrackerTransaction()
  {
    Transaction t = _observer.getContext().getTransaction();
    while ( null != t && t.getTracker() != _observer )
    {
      t = t.getPrevious();
    }
    return t;
  }
}
