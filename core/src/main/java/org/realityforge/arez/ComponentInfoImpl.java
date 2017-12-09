package org.realityforge.arez;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.spy.ComponentInfo;
import org.realityforge.arez.spy.ObservableInfo;
import org.realityforge.arez.spy.ObserverInfo;

/**
 * A implementation of {@link ComponentInfo} that proxies to a {@link Component}.
 */
final class ComponentInfoImpl
  implements ComponentInfo
{
  private final Spy _spy;
  private final Component _component;
  private final List<Observable<?>> _observables;
  private final List<ComputedValue<?>> _computedValues;

  ComponentInfoImpl( @Nonnull final Spy spy, @Nonnull final Component component )
  {
    _spy = Objects.requireNonNull( spy );
    _component = Objects.requireNonNull( component );
    _observables = Collections.unmodifiableList( _component.getObservables() );
    _computedValues = Collections.unmodifiableList( _component.getComputedValues() );
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
  public List<ObservableInfo> getObservables()
  {
    return ObservableInfoImpl.asUnmodifiableInfos( _spy, _observables );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ObserverInfo> getObservers()
  {
    return ObserverInfoImpl.asUnmodifiableInfos( _spy, _component.getObservers() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<ComputedValue<?>> getComputedValues()
  {
    return _computedValues;
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
