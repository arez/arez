package arez;

import arez.spy.ComponentInfo;
import arez.spy.ComputedValueInfo;
import arez.spy.ObservableInfo;
import arez.spy.ObserverInfo;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A implementation of {@link ComputedValueInfo} that proxies to a {@link ComputedValue}.
 */
final class ComputedValueInfoImpl
  implements ComputedValueInfo
{
  private final Spy _spy;
  private final ComputedValue<?> _computedValue;

  ComputedValueInfoImpl( @Nonnull final Spy spy, @Nonnull final ComputedValue<?> computedValue )
  {
    _spy = Objects.requireNonNull( spy );
    _computedValue = Objects.requireNonNull( computedValue );
  }

  @Nonnull
  private static List<ComputedValueInfo> asInfos( @Nonnull final Spy spy,
                                                  @Nonnull final Collection<ComputedValue<?>> computedValues )
  {
    return computedValues
      .stream()
      .map( o -> new ComputedValueInfoImpl( spy, o ) )
      .collect( Collectors.toList() );
  }

  @Nonnull
  static List<ComputedValueInfo> asUnmodifiableInfos( @Nonnull final Spy spy,
                                                      @Nonnull final Collection<ComputedValue<?>> computedValues )
  {
    return Collections.unmodifiableList( asInfos( spy, computedValues ) );
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
  @Override
  public boolean isActive()
  {
    return _spy.isActive( _computedValue );
  }

  @Nonnull
  @Override
  public List<ObservableInfo> getDependencies()
  {
    return _spy.getDependencies( _computedValue );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public List<ObserverInfo> getObservers()
  {
    return _spy.getObservers( _computedValue );
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public ComponentInfo getComponent()
  {
    return _spy.getComponent( _computedValue );
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public Object getValue()
    throws Throwable
  {
    return _spy.getValue( _computedValue );
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
}
