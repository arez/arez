package org.realityforge.arez;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.spy.ComponentInfo;
import org.realityforge.arez.spy.ObservableInfo;
import org.realityforge.arez.spy.ObserverInfo;

/**
 * A implementation of {@link ObservableInfo} that proxies to a {@link Observable}.
 */
final class ObservableInfoImpl
  implements ObservableInfo
{
  private final Spy _spy;
  private final Observable<?> _observable;

  ObservableInfoImpl( @Nonnull final Spy spy, @Nonnull final Observable<?> observable )
  {
    _spy = Objects.requireNonNull( spy );
    _observable = Objects.requireNonNull( observable );
  }

  @Nonnull
  private static List<ObservableInfo> asInfos( @Nonnull final Spy spy,
                                               @Nonnull final Collection<Observable<?>> observables )
  {
    return observables
      .stream()
      .map( o -> new ObservableInfoImpl( spy, o ) )
      .collect( Collectors.toList() );
  }

  @Nonnull
  static List<ObservableInfo> asUnmodifiableInfos( @Nonnull final Spy spy,
                                                   @Nonnull final Collection<Observable<?>> observables )
  {
    return Collections.unmodifiableList( asInfos( spy, observables ) );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public String getName()
  {
    return _observable.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isComputedValue()
  {
    return _spy.isComputedValue( _observable );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComputedValue<?> asComputedValue()
  {
    return _spy.asComputedValue( _observable );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public List<ObserverInfo> getObservers()
  {
    return ObserverInfoImpl.asUnmodifiableInfos( _spy, _spy.getObservers( _observable ) );
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public ComponentInfo getComponent()
  {
    return _spy.getComponent( _observable );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAccessor()
  {
    return _spy.hasAccessor( _observable );
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public Object getValue()
    throws Throwable
  {
    return _spy.getValue( _observable );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasMutator()
  {
    return _spy.hasMutator( _observable );
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings( "unchecked" )
  @Override
  public void setValue( @Nullable final Object value )
    throws Throwable
  {
    _spy.setValue( (Observable<Object>) _observable, value );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return _observable.isDisposed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return _observable.toString();
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
      final ObservableInfoImpl that = (ObservableInfoImpl) o;
      return _observable.equals( that._observable );
    }
  }

  @Override
  public int hashCode()
  {
    return _observable.hashCode();
  }
}
