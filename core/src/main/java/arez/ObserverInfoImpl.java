package arez;

import arez.spy.ComponentInfo;
import arez.spy.ComputedValueInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserverInfo;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    return _spy.isComputedValue( _observer );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isReadOnly()
  {
    return Arez.shouldEnforceTransactionType() && TransactionMode.READ_WRITE != _observer.getMode();
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
    return _spy.asComputedValue( _observer );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public List<ObservableValueInfo> getDependencies()
  {
    return _spy.getDependencies( _observer );
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public ComponentInfo getComponent()
  {
    return _spy.getComponent( _observer );
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
