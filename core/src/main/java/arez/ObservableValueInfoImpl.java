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
 * A implementation of {@link ObservableValueInfo} that proxies to a {@link ObservableValue}.
 */
final class ObservableValueInfoImpl
  implements ObservableValueInfo
{
  private final Spy _spy;
  private final ObservableValue<?> _observableValue;

  ObservableValueInfoImpl( @Nonnull final Spy spy, @Nonnull final ObservableValue<?> observableValue )
  {
    _spy = Objects.requireNonNull( spy );
    _observableValue = Objects.requireNonNull( observableValue );
  }

  @Nonnull
  private static List<ObservableValueInfo> asInfos( @Nonnull final Spy spy,
                                                    @Nonnull final Collection<ObservableValue<?>> observableValues )
  {
    return observableValues
      .stream()
      .map( ObservableValue::asInfo )
      .collect( Collectors.toList() );
  }

  @Nonnull
  static List<ObservableValueInfo> asUnmodifiableInfos( @Nonnull final Spy spy,
                                                        @Nonnull final Collection<ObservableValue<?>> observableValues )
  {
    return Collections.unmodifiableList( asInfos( spy, observableValues ) );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public String getName()
  {
    return _observableValue.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isComputedValue()
  {
    return _spy.isComputedValue( _observableValue );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComputedValueInfo asComputedValue()
  {
    return _spy.asComputedValue( _observableValue );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public List<ObserverInfo> getObservers()
  {
    return _spy.getObservers( _observableValue );
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public ComponentInfo getComponent()
  {
    return _spy.getComponent( _observableValue );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAccessor()
  {
    return _spy.hasAccessor( _observableValue );
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public Object getValue()
    throws Throwable
  {
    return _spy.getValue( _observableValue );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasMutator()
  {
    return _spy.hasMutator( _observableValue );
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings( "unchecked" )
  @Override
  public void setValue( @Nullable final Object value )
    throws Throwable
  {
    _spy.setValue( (ObservableValue<Object>) _observableValue, value );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return _observableValue.isDisposed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return _observableValue.toString();
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
      final ObservableValueInfoImpl that = (ObservableValueInfoImpl) o;
      return _observableValue.equals( that._observableValue );
    }
  }

  @Override
  public int hashCode()
  {
    return _observableValue.hashCode();
  }
}
