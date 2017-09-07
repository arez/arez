package org.realityforge.arez.processor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
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
  @Nonnull
  private final PackageElement _packageElement;
  @Nonnull
  private final TypeElement _element;
  private final Map<String, ObservableDescriptor> _observables = new HashMap<>();
  private final Map<String, ObservableDescriptor> _roObservables = Collections.unmodifiableMap( _observables );

  ContainerDescriptor( @Nonnull final String name,
                       final boolean singleton,
                       @Nonnull final PackageElement packageElement,
                       @Nonnull final TypeElement element )
  {
    _name = Objects.requireNonNull( name );
    _singleton = singleton;
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

  ObservableDescriptor getObservableByName( @Nonnull final String name )
  {
    return _observables.computeIfAbsent( name, n -> new ObservableDescriptor( this, n ) );
  }

  @Nonnull
  Map<String, ObservableDescriptor> getObservables()
  {
    return _roObservables;
  }

  boolean shouldStoreContext()
  {
    return true;
  }
}
