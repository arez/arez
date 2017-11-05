package org.realityforge.arez;

import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * The component is an abstraction representation of a reactive component within Arez.
 * Each component is made up of one or more of the core Arez reactive elements: {@link Observable}s,
 * {@link Observer}s or {@link ComputedValue}s.
 */
public final class Component
  implements Disposable
{
  /**
   * Reference to the system to which this node belongs.
   */
  @Nonnull
  private final ArezContext _context;
  /**
   * A opaque string describing the type of the component.
   * It corresponds to @ArezComponent.name parameter if this component was built using the annotation processor.
   */
  @Nonnull
  private final String _type;
  /**
   * The if of the component. This should be null for singletons and non-null for non-singletons.
   */
  @Nullable
  private final Object _id;
  /**
   * A human consumable name for node. It should be non-null if {@link Arez#areNamesEnabled()} returns
   * true and <tt>null</tt> otherwise.
   */
  @Nullable
  private final String _name;
  private final ArrayList<Observable<?>> _observables = new ArrayList<>();
  private final ArrayList<Observer> _observers = new ArrayList<>();
  private final ArrayList<ComputedValue<?>> _computedValues = new ArrayList<>();
  private boolean _disposed;

  Component( @Nonnull final ArezContext context,
             @Nonnull final String type,
             @Nullable final Object id,
             @Nullable final String name )
  {
    apiInvariant( () -> Arez.areNamesEnabled() || null == name,
                  () -> "Component passed a name '" + name + "' but Arez.areNamesEnabled() is false" );
    _context = context;
    _type = Objects.requireNonNull( type );
    _id = id;
    _name = Arez.areNamesEnabled() ? Objects.requireNonNull( name ) : null;
  }

  /**
   * Return the component type.
   * This is an opaque string specified by the user.
   *
   * @return the component type.
   */
  @Nonnull
  public String getType()
  {
    return _type;
  }

  /**
   * Return the unique id of the component.
   * This will return null for singletons.
   *
   * @return the unique id of the component.
   */
  @Nullable
  public Object getId()
  {
    return _id;
  }

  /**
   * Return the unique name of the component.
   *
   * @return the name of the component.
   */
  @Nonnull
  public String getName()
  {
    apiInvariant( Arez::areNamesEnabled, () -> "Component.getName() invoked when Arez.areNamesEnabled() is false" );
    assert null != _name;
    return _name;
  }

  @Nonnull
  final ArezContext getContext()
  {
    return _context;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose()
  {
    if ( !_disposed )
    {
      _disposed = true;
      _context.safeAction( Arez.areNamesEnabled() ? getName() + ".dispose" : null, () -> {
        _context.componentDisposed( this );
        _observers.forEach( o -> Disposable.dispose( o ) );
        _observers.clear();
        _computedValues.forEach( v -> Disposable.dispose( v ) );
        _computedValues.clear();
        _observables.forEach( o -> Disposable.dispose( o ) );
        _observables.clear();
      } );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return _disposed;
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public final String toString()
  {
    if ( Arez.areNamesEnabled() )
    {
      return getName();
    }
    else
    {
      return super.toString();
    }
  }

  /**
   * Return the observers associated with the component.
   *
   * @return the observers associated with the component.
   */
  @Nonnull
  ArrayList<Observer> getObservers()
  {
    return _observers;
  }

  /**
   * Add observer to component.
   * Observer should not be part of observer.
   *
   * @param observer the observer.
   */
  void addObserver( @Nonnull final Observer observer )
  {
    apiInvariant( () -> !_observers.contains( observer ),
                  () -> "Component.addObserver invoked on component '" + getName() + "' specifying observer " +
                        "named '" + observer.getName() + "' when observer already exists for component." );
    _observers.add( observer );
  }

  /**
   * Remove observer from the component.
   * Observer should be part of component.
   *
   * @param observer the observer.
   */
  void removeObserver( @Nonnull final Observer observer )
  {
    apiInvariant( () -> _observers.contains( observer ),
                  () -> "Component.removeObserver invoked on component '" + getName() + "' specifying observer " +
                        "named '" + observer.getName() + "' when observer does not exist for component." );
    _observers.remove( observer );
  }

  /**
   * Return the observables associated with the component.
   *
   * @return the observables associated with the component.
   */
  @Nonnull
  ArrayList<Observable<?>> getObservables()
  {
    return _observables;
  }

  /**
   * Add observable to component.
   * Observable should not be part of component.
   *
   * @param observable the observable.
   */
  void addObservable( @Nonnull final Observable observable )
  {
    apiInvariant( () -> !_observables.contains( observable ),
                  () -> "Component.addObservable invoked on component '" + getName() + "' specifying observable " +
                        "named '" + observable.getName() + "' when observable already exists for component." );
    _observables.add( observable );
  }

  /**
   * Remove observable from the component.
   * Observable should be part of component.
   *
   * @param observable the observable.
   */
  void removeObservable( @Nonnull final Observable observable )
  {
    apiInvariant( () -> _observables.contains( observable ),
                  () -> "Component.removeObservable invoked on component '" + getName() + "' specifying observable " +
                        "named '" + observable.getName() + "' when observable does not exist for component." );
    _observables.remove( observable );
  }

  /**
   * Return the computedValues associated with the component.
   *
   * @return the computedValues associated with the component.
   */
  @Nonnull
  ArrayList<ComputedValue<?>> getComputedValues()
  {
    return _computedValues;
  }

  /**
   * Add computedValue to component.
   * ComputedValue should not be part of component.
   *
   * @param computedValue the computedValue.
   */
  void addComputedValue( @Nonnull final ComputedValue computedValue )
  {
    apiInvariant( () -> !_computedValues.contains( computedValue ),
                  () -> "Component.addComputedValue invoked on component '" + getName() + "' specifying " +
                        "computedValue named '" + computedValue.getName() + "' when computedValue already exists " +
                        "for component." );
    _computedValues.add( computedValue );
  }

  /**
   * Remove computedValue from the component.
   * ComputedValue should be part of component.
   *
   * @param computedValue the computedValue.
   */
  void removeComputedValue( @Nonnull final ComputedValue computedValue )
  {
    apiInvariant( () -> _computedValues.contains( computedValue ),
                  () -> "Component.removeComputedValue invoked on component '" + getName() + "' specifying " +
                        "computedValue named '" + computedValue.getName() + "' when computedValue does not exist " +
                        "for component." );
    _computedValues.remove( computedValue );
  }
}
