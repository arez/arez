package org.realityforge.arez;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.spy.ComponentInfo;

/**
 * A implementation of {@link ComponentInfo} that proxies to a {@link Component}.
 */
final class ComponentInfoImpl
  implements ComponentInfo
{
  private final Component _component;
  private final List<Observable<?>> _observables;
  private final List<Observer> _observers;
  private final List<ComputedValue<?>> _computedValues;

  ComponentInfoImpl( @Nonnull final Component component )
  {
    _component = Objects.requireNonNull( component );
    _observables = Collections.unmodifiableList( _component.getObservables() );
    _observers = Collections.unmodifiableList( _component.getObservers() );
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
  @Nullable
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
  public Collection<Observable<?>> getObservables()
  {
    return _observables;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<Observer> getObservers()
  {
    return _observers;
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
  public void dispose()
  {
    _component.dispose();
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
}
