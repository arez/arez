package org.realityforge.arez;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.spy.ComponentInfo;
import org.realityforge.arez.spy.ObserverInfo;

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
  private static Collection<ObserverInfo> asInfos( @Nonnull final Spy spy,
                                                   @Nonnull final Collection<Observer> observers )
  {
    return observers
      .stream()
      .map( o -> new ObserverInfoImpl( spy, o ) )
      .collect( Collectors.toList() );
  }

  @Nonnull
  static Collection<ObserverInfo> asUnmodifiableInfos( @Nonnull final Spy spy,
                                                       @Nonnull final Collection<Observer> observers )
  {
    return Collections.unmodifiableCollection( asInfos( spy, observers ) );
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

  @Override
  public boolean isRunning()
  {
    return _spy.isRunning( _observer );
  }

  @Override
  public boolean isScheduled()
  {
    return _spy.isScheduled( _observer );
  }

  @Override
  public boolean isComputedValue()
  {
    return _spy.isComputedValue( _observer );
  }

  @Override
  public boolean isReadOnly()
  {
    return _spy.isReadOnly( _observer );
  }

  @Override
  public ComputedValue<?> asComputedValue()
  {
    return _spy.asComputedValue( _observer );
  }

  @Nonnull
  @Override
  public List<Observable<?>> getDependencies()
  {
    return _spy.getDependencies( _observer );
  }

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
}
