package org.realityforge.arez.processor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

/**
 * The class that represents the parsed state of Container annotated class.
 */
final class ContainerDescriptor
{
  @Nonnull
  private final String _name;
  private final boolean _singleton;
  private final boolean _disposable;
  @Nonnull
  private final PackageElement _packageElement;
  @Nonnull
  private final TypeElement _element;
  @Nullable
  private ExecutableElement _containerId;
  private final Map<String, ObservableDescriptor> _observables = new HashMap<>();
  private final Collection<ObservableDescriptor> _roObservables =
    Collections.unmodifiableCollection( _observables.values() );
  private final Map<String, ActionDescriptor> _actions = new HashMap<>();
  private final Collection<ActionDescriptor> _roActions =
    Collections.unmodifiableCollection( _actions.values() );
  private final Map<String, ComputedDescriptor> _computeds = new HashMap<>();
  private final Collection<ComputedDescriptor> _roComputeds =
    Collections.unmodifiableCollection( _computeds.values() );

  ContainerDescriptor( @Nonnull final String name,
                       final boolean singleton,
                       final boolean disposable,
                       @Nonnull final PackageElement packageElement,
                       @Nonnull final TypeElement element )
  {
    _name = Objects.requireNonNull( name );
    _singleton = singleton;
    _disposable = disposable;
    _packageElement = Objects.requireNonNull( packageElement );
    _element = Objects.requireNonNull( element );
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  boolean isSingleton()
  {
    return _singleton;
  }

  boolean isDisposable()
  {
    return _disposable && !getObservables().isEmpty() && !getComputeds().isEmpty();
  }

  @Nonnull
  DeclaredType asDeclaredType()
  {
    return (DeclaredType) _element.asType();
  }

  @Nonnull
  PackageElement getPackageElement()
  {
    return _packageElement;
  }

  @Nonnull
  TypeElement getElement()
  {
    return _element;
  }

  @Nonnull
  ObservableDescriptor findOrCreateObservable( @Nonnull final String name )
  {
    return _observables.computeIfAbsent( name, n -> new ObservableDescriptor( this, n ) );
  }

  @Nullable
  ObservableDescriptor getObservable( @Nonnull final String name )
  {
    return _observables.get( name );
  }

  @Nonnull
  Collection<ObservableDescriptor> getObservables()
  {
    return _roObservables;
  }

  void addAction( @Nonnull final ActionDescriptor action )
  {
    _actions.put( action.getName(), action );
  }

  @Nullable
  ActionDescriptor getAction( @Nonnull final String name )
  {
    return _actions.get( name );
  }

  @Nonnull
  Collection<ActionDescriptor> getActions()
  {
    return _roActions;
  }

  void addComputed( @Nonnull final ComputedDescriptor computed )
  {
    _computeds.put( computed.getName(), computed );
  }

  @Nullable
  ComputedDescriptor getComputed( @Nonnull final String name )
  {
    return _computeds.get( name );
  }

  @Nonnull
  Collection<ComputedDescriptor> getComputeds()
  {
    return _roComputeds;
  }

  @Nullable
  ExecutableElement getContainerId()
  {
    return _containerId;
  }

  void setContainerId( @Nonnull final ExecutableElement containerId )
  {
    _containerId = containerId;
  }
}
