package arez;

import arez.spy.ComponentInfo;
import arez.spy.ComputableValueInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserverInfo;
import grim.annotations.OmitType;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * A implementation of {@link ComponentInfo} that proxies to a {@link Component}.
 */
@OmitType( unless = "arez.enable_spies" )
@OmitType( unless = "arez.enable_native_components" )
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

  @Nonnull
  @Override
  public String getType()
  {
    return _component.getType();
  }

  @Nonnull
  @Override
  public Object getId()
  {
    return _component.getId();
  }

  @Nonnull
  @Override
  public String getName()
  {
    return _component.getName();
  }

  @Override
  public List<ObservableValueInfo> getObservableValues()
  {
    return ObservableValueInfoImpl.asUnmodifiableInfos( _observableValues );
  }

  @Override
  public List<ObserverInfo> getObservers()
  {
    return ObserverInfoImpl.asUnmodifiableInfos( _component.getObservers() );
  }

  @Override
  public List<ComputableValueInfo> getComputableValues()
  {
    return ComputableValueInfoImpl.asUnmodifiableInfos( _computableValues );
  }

  @Override
  public boolean isDisposed()
  {
    return _component.isDisposed();
  }

  @Override
  public String toString()
  {
    return _component.toString();
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
      final ComponentInfoImpl that = (ComponentInfoImpl) o;
      return _component.equals( that._component );
    }
  }

  @Override
  public int hashCode()
  {
    return _component.hashCode();
  }
}
