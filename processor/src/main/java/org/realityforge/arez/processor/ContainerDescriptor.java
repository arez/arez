package org.realityforge.arez.processor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import org.realityforge.arez.annotations.ContainerId;
import org.realityforge.arez.annotations.PostDispose;
import org.realityforge.arez.annotations.PreDispose;

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
  private ExecutableElement _postConstruct;
  @Nullable
  private ExecutableElement _containerId;
  @Nullable
  private ExecutableElement _preDispose;
  @Nullable
  private ExecutableElement _postDispose;
  private final Map<String, ObservableDescriptor> _observables = new HashMap<>();
  private final Collection<ObservableDescriptor> _roObservables =
    Collections.unmodifiableCollection( _observables.values() );
  private final Map<String, ActionDescriptor> _actions = new HashMap<>();
  private final Collection<ActionDescriptor> _roActions =
    Collections.unmodifiableCollection( _actions.values() );
  private final Map<String, ComputedDescriptor> _computeds = new HashMap<>();
  private final Collection<ComputedDescriptor> _roComputeds =
    Collections.unmodifiableCollection( _computeds.values() );
  private final Map<String, AutorunDescriptor> _autoruns = new HashMap<>();
  private final Collection<AutorunDescriptor> _roAutoruns =
    Collections.unmodifiableCollection( _autoruns.values() );

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
    return _disposable;
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

  void addAutorun( @Nonnull final AutorunDescriptor autorun )
  {
    _autoruns.put( autorun.getName(), autorun );
  }

  @Nullable
  AutorunDescriptor getAutorun( @Nonnull final String name )
  {
    return _autoruns.get( name );
  }

  @Nonnull
  Collection<AutorunDescriptor> getAutoruns()
  {
    return _roAutoruns;
  }

  @Nullable
  ComputedDescriptor getComputed( @Nonnull final String name )
  {
    return _computeds.get( name );
  }

  @Nonnull
  ComputedDescriptor findOrCreateComputed( @Nonnull final String name )
  {
    ComputedDescriptor descriptor = _computeds.get( name );
    if ( null == descriptor )
    {
      descriptor = new ComputedDescriptor( name );
      _computeds.put( name, descriptor );
    }
    return descriptor;
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
    throws ArezProcessorException
  {
    if ( isSingleton() )
    {
      throw new ArezProcessorException( "@ContainerId must not exist if @Container is a singleton", containerId );
    }

    MethodChecks.mustNotBeStatic( ContainerId.class, containerId );
    MethodChecks.mustNotBeAbstract( ContainerId.class, containerId );
    MethodChecks.mustNotBePrivate( ContainerId.class, containerId );
    MethodChecks.mustBeFinal( ContainerId.class, containerId );
    MethodChecks.mustNotHaveAnyParameters( ContainerId.class, containerId );
    MethodChecks.mustReturnAValue( ContainerId.class, containerId );
    MethodChecks.mustNotThrowAnyExceptions( ContainerId.class, containerId );

    if ( null != _containerId )
    {
      throw new ArezProcessorException( "@ContainerId target duplicates existing method named " +
                                        _containerId.getSimpleName(), containerId );
    }
    else
    {
      _containerId = containerId;
    }
  }

  @Nullable
  ExecutableElement getPostConstruct()
  {
    return _postConstruct;
  }

  void setPostConstruct( @Nonnull final ExecutableElement postConstruct )
    throws ArezProcessorException
  {
    MethodChecks.mustNotBeStatic( PostConstruct.class, postConstruct );
    MethodChecks.mustNotBeAbstract( PostConstruct.class, postConstruct );
    MethodChecks.mustNotBePrivate( PostConstruct.class, postConstruct );
    MethodChecks.mustNotHaveAnyParameters( PostConstruct.class, postConstruct );
    MethodChecks.mustNotReturnAnyValue( PostConstruct.class, postConstruct );
    MethodChecks.mustNotThrowAnyExceptions( PostConstruct.class, postConstruct );

    if ( null != _postConstruct )
    {
      throw new ArezProcessorException( "@PostConstruct target duplicates existing method named " +
                                        _postConstruct.getSimpleName(), postConstruct );
    }
    else
    {
      _postConstruct = postConstruct;
    }
  }

  @Nullable
  ExecutableElement getPreDispose()
  {
    return _preDispose;
  }

  void setPreDispose( @Nonnull final ExecutableElement preDispose )
    throws ArezProcessorException
  {
    if ( !isDisposable() )
    {
      throw new ArezProcessorException( "@PreDispose must not exist if @Container set disposable to false",
                                        preDispose );
    }
    MethodChecks.mustNotBeStatic( PreDispose.class, preDispose );
    MethodChecks.mustNotBeAbstract( PreDispose.class, preDispose );
    MethodChecks.mustNotBePrivate( PreDispose.class, preDispose );
    MethodChecks.mustNotHaveAnyParameters( PreDispose.class, preDispose );
    MethodChecks.mustNotReturnAnyValue( PreDispose.class, preDispose );
    MethodChecks.mustNotThrowAnyExceptions( PreDispose.class, preDispose );

    if ( null != _preDispose )
    {
      throw new ArezProcessorException( "@PreDispose target duplicates existing method named " +
                                        _preDispose.getSimpleName(), preDispose );
    }
    else
    {
      _preDispose = preDispose;
    }
  }

  @Nullable
  ExecutableElement getPostDispose()
  {
    return _postDispose;
  }

  void setPostDispose( @Nonnull final ExecutableElement postDispose )
    throws ArezProcessorException
  {
    if ( !isDisposable() )
    {
      throw new ArezProcessorException( "@PostDispose must not exist if @Container set disposable to false",
                                        postDispose );
    }

    MethodChecks.mustNotBeStatic( PostDispose.class, postDispose );
    MethodChecks.mustNotBeAbstract( PostDispose.class, postDispose );
    MethodChecks.mustNotBePrivate( PostDispose.class, postDispose );
    MethodChecks.mustNotHaveAnyParameters( PostDispose.class, postDispose );
    MethodChecks.mustNotReturnAnyValue( PostDispose.class, postDispose );
    MethodChecks.mustNotThrowAnyExceptions( PostDispose.class, postDispose );

    if ( null != _postDispose )
    {
      throw new ArezProcessorException( "@PostDispose target duplicates existing method named " +
                                        _postDispose.getSimpleName(), postDispose );
    }
    else
    {
      _postDispose = postDispose;
    }
  }
}
