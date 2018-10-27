package arez;

import arez.spy.ComponentInfo;
import arez.spy.ComputableValueInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserverInfo;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * A implementation of {@link ComponentInfo} that proxies to a {@link Component}.
 */
final class ComponentInfoImpl
  implements ComponentInfo
{
  private final Component _component;
  private final List<ObservableValue<?>> _observableValues;
  private final List<ComputableValue<?>> _computableValues;

  ComponentInfoImpl( @Nonnull final Component component )
  {
    _component = Objects.requireNonNull( component );
    _observableValues = Collections.unmodifiableList( _component.getObservableValues() );
    _computableValues = Collections.unmodifiableList( _component.getComputableValues() );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public String getType()
  {
    return _component.getType();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public Object getId()
  {
    return _component.getId();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public String getName()
  {
    return _component.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ObservableValueInfo> getObservableValues()
  {
    return ObservableValueInfoImpl.asUnmodifiableInfos( _observableValues );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ObserverInfo> getObservers()
  {
    return ObserverInfoImpl.asUnmodifiableInfos( _component.getObservers() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ComputableValueInfo> getComputableValues()
  {
    return ComputableValueInfoImpl.asUnmodifiableInfos( _computableValues );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return _component.isDisposed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return _component.toString();
  }

  /**
   * {@inheritDoc}
   */
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
      final ComponentInfoImpl that = (ComponentInfoImpl) o;
      return _component.equals( that._component );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return _component.hashCode();
  }
}
