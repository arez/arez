package arez;

import arez.spy.ComponentCreateCompleteEvent;
import arez.spy.ComponentDisposeCompleteEvent;
import arez.spy.ComponentDisposeStartEvent;
import arez.spy.ComponentInfo;
import grim.annotations.OmitSymbol;
import grim.annotations.OmitType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * The component is an abstraction representation of a reactive component within Arez.
 * Each component is made up of one or more of the core Arez reactive elements: {@link ObservableValue}s,
 * {@link Observer}s or {@link ComputableValue}s.
 */
@OmitType( unless = "arez.enable_native_components" )
public final class Component
  implements Disposable
{
  /**
   * Reference to the system to which this node belongs.
   */
  @OmitSymbol( unless = "arez.enable_zones" )
  @Nullable
  private final ArezContext _context;
  /**
   * A opaque string describing the type of the component.
   * It corresponds to @ArezComponent.name parameter if this component was built using the annotation processor.
   */
  @Nonnull
  private final String _type;
  /**
   * The id of the component.
   */
  @Nonnull
  private final Object _id;
  /**
   * A human consumable name for node. It should be non-null if {@link Arez#areNamesEnabled()} returns
   * true and <code>null</code> otherwise.
   */
  @Nullable
  @OmitSymbol( unless = "arez.enable_names" )
  private final String _name;
  @Nonnull
  private final List<ObservableValue<?>> _observableValues = new ArrayList<>();
  @Nonnull
  private final List<Observer> _observers = new ArrayList<>();
  @Nonnull
  private final List<ComputableValue<?>> _computableValues = new ArrayList<>();
  /**
   * Hook action called just before the Component is disposed.
   * Occurs inside the dispose transaction.
   */
  @Nullable
  private final SafeProcedure _preDispose;
  /**
   * Hook action called just after the Component is disposed.
   */
  @Nullable
  private final SafeProcedure _postDispose;
  private boolean _complete;
  private boolean _disposed;
  /**
   * Cached info object associated with element.
   * This should be null if {@link Arez#areSpiesEnabled()} is false;
   */
  @OmitSymbol( unless = "arez.enable_spies" )
  @Nullable
  private ComponentInfo _info;

  Component( @Nullable final ArezContext context,
             @Nonnull final String type,
             @Nonnull final Object id,
             @Nullable final String name,
             @Nullable final SafeProcedure preDispose,
             @Nullable final SafeProcedure postDispose )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> Arez.areNamesEnabled() || null == name,
                    () -> "Arez-0037: Component passed a name '" + name + "' but Arez.areNamesEnabled() is false" );
      invariant( () -> Arez.areZonesEnabled() || null == context,
                 () -> "Arez-0175: Component passed a context but Arez.areZonesEnabled() is false" );
    }
    _context = Arez.areZonesEnabled() ? Objects.requireNonNull( context ) : null;
    _type = Objects.requireNonNull( type );
    _id = Objects.requireNonNull( id );
    _name = Arez.areNamesEnabled() ? Objects.requireNonNull( name ) : null;
    _preDispose = preDispose;
    _postDispose = postDispose;
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
  @Nonnull
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
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( Arez::areNamesEnabled,
                    () -> "Arez-0038: Component.getName() invoked when Arez.areNamesEnabled() is false" );
    }
    assert null != _name;
    return _name;
  }

  @Nonnull
  ArezContext getContext()
  {
    return Arez.areZonesEnabled() ? Objects.requireNonNull( _context ) : Arez.context();
  }

  @Override
  public void dispose()
  {
    if ( !_disposed )
    {
      _disposed = true;
      if ( Arez.areSpiesEnabled() && getContext().getSpy().willPropagateSpyEvents() )
      {
        final ComponentInfo info = getContext().getSpy().asComponentInfo( this );
        getContext().getSpy().reportSpyEvent( new ComponentDisposeStartEvent( info ) );
      }
      getContext().safeAction( Arez.areNamesEnabled() ? getName() + ".dispose" : null, () -> {
        if ( null != _preDispose )
        {
          _preDispose.call();
        }
        getContext().deregisterComponent( this );
        /*
         * Create a new list and perform dispose on each list to avoid concurrent mutation exceptions.
         * This can probably be significantly optimized when translated to javascript. However native
         * components are not typically used in production mode so no effort has been made to optimize
         * the next steps.
         */
        new ArrayList<>( _observers ).forEach( o -> Disposable.dispose( o ) );
        new ArrayList<>( _computableValues ).forEach( v -> Disposable.dispose( v ) );
        new ArrayList<>( _observableValues ).forEach( o -> Disposable.dispose( o ) );
        if ( null != _postDispose )
        {
          _postDispose.call();
        }
      }, ActionFlags.NO_VERIFY_ACTION_REQUIRED );
      if ( Arez.areSpiesEnabled() && getContext().getSpy().willPropagateSpyEvents() )
      {
        final ComponentInfo info = getContext().getSpy().asComponentInfo( this );
        getContext().getSpy().reportSpyEvent( new ComponentDisposeCompleteEvent( info ) );
      }
    }
  }

  @Override
  public boolean isDisposed()
  {
    return _disposed;
  }

  @Nonnull
  @Override
  public String toString()
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
   * Return true if the creation of this component is complete.
   *
   * @return true if the creation of this component is complete, false otherwise.
   */
  public boolean isComplete()
  {
    return _complete;
  }

  /**
   * The toolkit user should call this method when the component is complete.
   * After this method has been invoked the user should not attempt to define any more {@link ObservableValue}s,
   * {@link Observer}s or {@link ComputableValue}s on the component.
   */
  public void complete()
  {
    if ( !_complete )
    {
      _complete = true;
      if ( Arez.areSpiesEnabled() && getContext().getSpy().willPropagateSpyEvents() )
      {
        final ComponentInfo component = getContext().getSpy().asComponentInfo( this );
        getContext().getSpy().reportSpyEvent( new ComponentCreateCompleteEvent( component ) );
      }
    }
  }

  /**
   * Return the info associated with this class.
   *
   * @return the info associated with this class.
   */
  @SuppressWarnings( "ConstantConditions" )
  @OmitSymbol( unless = "arez.enable_spies" )
  @Nonnull
  ComponentInfo asInfo()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areSpiesEnabled,
                 () -> "Arez-0194: Component.asInfo() invoked but Arez.areSpiesEnabled() returned false." );
    }
    if ( Arez.areSpiesEnabled() && null == _info )
    {
      _info = new ComponentInfoImpl( this );
    }
    return Arez.areSpiesEnabled() ? _info : null;
  }

  /**
   * Return the observers associated with the component.
   *
   * @return the observers associated with the component.
   */
  @Nonnull
  List<Observer> getObservers()
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
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !_observers.contains( observer ),
                    () -> "Arez-0040: Component.addObserver invoked on component '" + getName() + "' specifying " +
                          "observer named '" + observer.getName() + "' when observer already exists for component." );
    }
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
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> _observers.contains( observer ),
                    () -> "Arez-0041: Component.removeObserver invoked on component '" + getName() + "' specifying " +
                          "observer named '" + observer.getName() + "' when observer does not exist for component." );
    }
    _observers.remove( observer );
  }

  /**
   * Return the observables associated with the component.
   *
   * @return the observables associated with the component.
   */
  @Nonnull
  List<ObservableValue<?>> getObservableValues()
  {
    return _observableValues;
  }

  /**
   * Add observableValue to component.
   * ObservableValue should not be part of component.
   *
   * @param observableValue the observableValue.
   */
  void addObservableValue( @Nonnull final ObservableValue<?> observableValue )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !_complete,
                    () -> "Arez-0042: Component.addObservableValue invoked on component '" +
                          getName() +
                          "' " +
                          "specifying ObservableValue named '" +
                          observableValue.getName() +
                          "' when component.complete() " +
                          "has already been called." );
      apiInvariant( () -> !_observableValues.contains( observableValue ),
                    () -> "Arez-0043: Component.addObservableValue invoked on component '" +
                          getName() +
                          "' " +
                          "specifying ObservableValue named '" +
                          observableValue.getName() +
                          "' when ObservableValue already " +
                          "exists for component." );
    }
    _observableValues.add( observableValue );
  }

  /**
   * Remove observableValue from the component.
   * ObservableValue should be part of component.
   *
   * @param observableValue the observableValue.
   */
  void removeObservableValue( @Nonnull final ObservableValue<?> observableValue )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> _observableValues.contains( observableValue ),
                    () -> "Arez-0044: Component.removeObservableValue invoked on component '" +
                          getName() +
                          "' " +
                          "specifying ObservableValue named '" +
                          observableValue.getName() +
                          "' when ObservableValue does not " +
                          "exist for component." );
    }
    _observableValues.remove( observableValue );
  }

  /**
   * Return the {@link ComputableValue} instances associated with the component.
   *
   * @return the {@link ComputableValue} instances associated with the component.
   */
  @Nonnull
  List<ComputableValue<?>> getComputableValues()
  {
    return _computableValues;
  }

  /**
   * Add computableValue to component.
   * ComputableValue should not be part of component.
   *
   * @param computableValue the computableValue.
   */
  void addComputableValue( @Nonnull final ComputableValue<?> computableValue )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !_computableValues.contains( computableValue ),
                    () -> "Arez-0046: Component.addComputableValue invoked on component '" + getName() + "' " +
                          "specifying ComputableValue named '" + computableValue.getName() + "' when " +
                          "ComputableValue already exists for component." );
    }
    _computableValues.add( computableValue );
  }

  /**
   * Remove computableValue from the component.
   * ComputableValue should be part of component.
   *
   * @param computableValue the computableValue.
   */
  void removeComputableValue( @Nonnull final ComputableValue<?> computableValue )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> _computableValues.contains( computableValue ),
                    () -> "Arez-0047: Component.removeComputableValue invoked on component '" + getName() + "' " +
                          "specifying ComputableValue named '" + computableValue.getName() + "' when " +
                          "ComputableValue does not exist for component." );
    }
    _computableValues.remove( computableValue );
  }

  @OmitSymbol
  @Nullable
  SafeProcedure getPreDispose()
  {
    return _preDispose;
  }

  @OmitSymbol
  @Nullable
  SafeProcedure getPostDispose()
  {
    return _postDispose;
  }
}
