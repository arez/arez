package org.realityforge.arez.processor;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.ContainerId;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.OnActivate;
import org.realityforge.arez.annotations.OnDeactivate;
import org.realityforge.arez.annotations.OnDispose;
import org.realityforge.arez.annotations.OnStale;
import org.realityforge.arez.annotations.PostDispose;
import org.realityforge.arez.annotations.PreDispose;

/**
 * The class that represents the parsed state of Container annotated class.
 */
final class ContainerDescriptor
{
  private static final Pattern SETTER_PATTERN = Pattern.compile( "^set([A-Z].*)$" );
  private static final Pattern GETTER_PATTERN = Pattern.compile( "^get([A-Z].*)$" );
  private static final Pattern ISSER_PATTERN = Pattern.compile( "^is([A-Z].*)$" );

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
  private ObservableDescriptor findOrCreateObservable( @Nonnull final String name )
  {
    return _observables.computeIfAbsent( name, n -> new ObservableDescriptor( this, n ) );
  }

  private void addObservable( @Nonnull final Observable annotation, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( Observable.class, method );

    final TypeMirror returnType = method.getReturnType();
    final String methodName = method.getSimpleName().toString();
    String name;
    final boolean setter;
    if ( TypeKind.VOID == returnType.getKind() )
    {
      setter = true;
      //Should be a setter
      if ( 1 != method.getParameters().size() )
      {
        throw new ArezProcessorException( "Method annotated with @Observable should be a setter or getter", method );
      }

      name = ProcessorUtil.deriveName( method, SETTER_PATTERN, annotation.name() );
      if ( null == name )
      {
        name = methodName;
      }
    }
    else
    {
      setter = false;
      //Must be a getter
      if ( 0 != method.getParameters().size() )
      {
        throw new ArezProcessorException( "Method annotated with @Observable should be a setter or getter", method );
      }
      name = getPropertyAccessorName( method, annotation.name() );
    }
    // Override name if supplied by user
    if ( !ProcessorUtil.isSentinelName( annotation.name() ) )
    {
      name = annotation.name();
      if ( !name.isEmpty() )
      {
        if ( !ProcessorUtil.isJavaIdentifier( name ) )
        {
          throw new ArezProcessorException( "Method annotated with @Observable specified invalid name " + name,
                                            method );
        }
      }
    }
    checkNameUnique( name, method, Observable.class );
    final ObservableDescriptor observable = findOrCreateObservable( name );
    if ( setter )
    {
      if ( observable.hasSetter() )
      {
        throw new ArezProcessorException( "Method annotated with @Observable defines duplicate setter for " +
                                          "observable named " + name, method );
      }
      observable.setSetter( method );
    }
    else
    {
      if ( observable.hasGetter() )
      {
        throw new ArezProcessorException( "Method annotated with @Observable defines duplicate getter for " +
                                          "observable named " + name, method );
      }
      observable.setGetter( method );
    }
  }

  @Nonnull
  Collection<ObservableDescriptor> getObservables()
  {
    return _roObservables;
  }

  private void addAction( @Nonnull final Action annotation, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( Action.class, method );

    final String name = deriveActionName( method, annotation );
    checkNameUnique( name, method, Action.class );
    final ActionDescriptor action = new ActionDescriptor( name, annotation.mutation(), method );
    _actions.put( action.getName(), action );
  }

  @Nonnull
  private String deriveActionName( @Nonnull final ExecutableElement method, @Nonnull final Action annotation )
    throws ArezProcessorException
  {
    if ( ProcessorUtil.isSentinelName( annotation.name() ) )
    {
      return method.getSimpleName().toString();
    }
    else
    {
      final String name = annotation.name();
      if ( name.isEmpty() || !ProcessorUtil.isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @Action specified invalid name " + name, method );
      }
      return name;
    }
  }

  @Nonnull
  Collection<ActionDescriptor> getActions()
  {
    return _roActions;
  }

  private void addAutorun( @Nonnull final Autorun annotation, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( Autorun.class, method );
    MethodChecks.mustNotHaveAnyParameters( Autorun.class, method );
    MethodChecks.mustNotThrowAnyExceptions( Autorun.class, method );
    MethodChecks.mustNotReturnAnyValue( Autorun.class, method );

    final String name = deriveAutorunName( method, annotation );
    checkNameUnique( name, method, Autorun.class );
    final AutorunDescriptor autorun = new AutorunDescriptor( name, annotation.mutation(), method );
    _autoruns.put( autorun.getName(), autorun );
  }

  @Nonnull
  private String deriveAutorunName( @Nonnull final ExecutableElement method, @Nonnull final Autorun annotation )
    throws ArezProcessorException
  {
    if ( ProcessorUtil.isSentinelName( annotation.name() ) )
    {
      return method.getSimpleName().toString();
    }
    else
    {
      final String name = annotation.name();
      if ( name.isEmpty() || !ProcessorUtil.isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @Autorun specified invalid name " + name, method );
      }
      return name;
    }
  }

  @Nonnull
  Collection<AutorunDescriptor> getAutoruns()
  {
    return _roAutoruns;
  }

  @Nonnull
  private ComputedDescriptor findOrCreateComputed( @Nonnull final String name )
  {
    return _computeds.computeIfAbsent( name, ComputedDescriptor::new );
  }

  private void addComputed( @Nonnull final Computed annotation, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final String name = deriveComputedName( method, annotation );
    checkNameUnique( name, method, Computed.class );
    findOrCreateComputed( name ).setComputed( method );
  }

  @Nonnull
  private String deriveComputedName( @Nonnull final ExecutableElement method, @Nonnull final Computed annotation )
    throws ArezProcessorException
  {
    if ( ProcessorUtil.isSentinelName( annotation.name() ) )
    {
      return getPropertyAccessorName( method, annotation.name() );
    }
    else
    {
      final String name = annotation.name();
      if ( name.isEmpty() || !ProcessorUtil.isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @Computed specified invalid name " + name, method );
      }
      return name;
    }
  }

  private void addOnActivate( @Nonnull final OnActivate annotation, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final String name = deriveHookName( method, ComputedDescriptor.ON_ACTIVATE_PATTERN, "Activate", annotation.name() );
    findOrCreateComputed( name ).setOnActivate( method );
  }

  private void addOnDeactivate( @Nonnull final OnDeactivate annotation, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final String name =
      deriveHookName( method, ComputedDescriptor.ON_DEACTIVATE_PATTERN, "Deactivate", annotation.name() );
    findOrCreateComputed( name ).setOnDeactivate( method );
  }

  private void addOnStale( @Nonnull final OnStale annotation, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final String name = deriveHookName( method, ComputedDescriptor.ON_STALE_PATTERN, "Stale", annotation.name() );
    findOrCreateComputed( name ).setOnStale( method );
  }

  private void addOnDispose( @Nonnull final OnDispose annotation, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final String name = deriveHookName( method, ComputedDescriptor.ON_DISPOSE_PATTERN, "Dispose", annotation.name() );
    findOrCreateComputed( name ).setOnDispose( method );
  }

  @Nonnull
  private String deriveHookName( @Nonnull final ExecutableElement method,
                                 @Nonnull final Pattern pattern,
                                 @Nonnull final String type,
                                 @Nonnull final String name )
    throws ArezProcessorException
  {
    final String value = ProcessorUtil.deriveName( method, pattern, name );
    if ( null == value )
    {
      throw new ArezProcessorException( "Unable to derive name for @On" + type + " as does not match " +
                                        "on[Name]" + type + " pattern. Please specify name.", method );
    }
    else if ( value.isEmpty() || !ProcessorUtil.isJavaIdentifier( value ) )
    {
      throw new ArezProcessorException( "Method annotated with @On" + type + " specified invalid name " + value,
                                        method );
    }
    else
    {
      return value;
    }
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

  private void setContainerId( @Nonnull final ExecutableElement containerId )
    throws ArezProcessorException
  {
    if ( isSingleton() )
    {
      throw new ArezProcessorException( "@ContainerId must not exist if @Container is a singleton", containerId );
    }

    MethodChecks.mustBeSubclassCallable( ContainerId.class, containerId );
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
    MethodChecks.mustBeLifecycleHook( PostConstruct.class, postConstruct );

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

  private void setPreDispose( @Nonnull final ExecutableElement preDispose )
    throws ArezProcessorException
  {
    if ( !isDisposable() )
    {
      throw new ArezProcessorException( "@PreDispose must not exist if @Container set disposable to false",
                                        preDispose );
    }
    MethodChecks.mustBeLifecycleHook( PreDispose.class, preDispose );

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

  private void setPostDispose( @Nonnull final ExecutableElement postDispose )
    throws ArezProcessorException
  {
    if ( !isDisposable() )
    {
      throw new ArezProcessorException( "@PostDispose must not exist if @Container set disposable to false",
                                        postDispose );
    }

    MethodChecks.mustBeLifecycleHook( PostDispose.class, postDispose );

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

  void validate()
    throws ArezProcessorException
  {
    validateComputeds();

    if ( getObservables().isEmpty() &&
         getActions().isEmpty() &&
         getComputeds().isEmpty() &&
         getAutoruns().isEmpty() )
    {
      throw new ArezProcessorException( "@Container target has no methods annotated with @Action, " +
                                        "@Computed, @Observable or @Autorun", _element );
    }
  }

  private void validateComputeds()
    throws ArezProcessorException
  {
    for ( final ComputedDescriptor computed : getComputeds() )
    {
      computed.validate();
    }
  }

  private void checkNameUnique( @Nonnull final String name,
                                @Nonnull final ExecutableElement sourceMethod,
                                @Nonnull final Class<? extends Annotation> sourceType )
    throws ArezProcessorException
  {
    final ActionDescriptor action = _actions.get( name );
    if ( null != action )
    {
      throw toException( name, sourceType, sourceMethod, Action.class, action.getAction() );
    }
    final ComputedDescriptor computed = _computeds.get( name );
    if ( null != computed )
    {
      throw toException( name, sourceType, sourceMethod, Computed.class, computed.getComputed() );
    }
    final AutorunDescriptor autorun = _autoruns.get( name );
    if ( null != autorun )
    {
      throw toException( name, sourceType, sourceMethod, Autorun.class, autorun.getAutorun() );
    }
    // Observables have pairs so let the caller determine whether a duplicate occurs in that scenario
    if ( Observable.class != sourceType )
    {
      final ObservableDescriptor observable = _observables.get( name );
      if ( null != observable )
      {
        throw toException( name, sourceType, sourceMethod, Observable.class, observable.getDefiner() );
      }
    }
  }

  @Nonnull
  private ArezProcessorException toException( @Nonnull final String name,
                                              @Nonnull final Class<? extends Annotation> source,
                                              @Nonnull final ExecutableElement sourceMethod,
                                              @Nonnull final Class<? extends Annotation> target,
                                              @Nonnull final ExecutableElement targetElement )
  {
    return new ArezProcessorException( "Method annotated with @" + source.getSimpleName() + " specified name " +
                                       name + " that duplicates @" + target.getSimpleName() + " defined by " +
                                       "method " + targetElement.getSimpleName(), sourceMethod );
  }

  void analyzeCandidateMethods( @Nonnull final List<ExecutableElement> methods )
    throws ArezProcessorException
  {
    final Map<String, ExecutableElement> getters = new HashMap<>();
    final Map<String, ExecutableElement> setters = new HashMap<>();
    for ( final ExecutableElement method : methods )
    {
      if ( !analyzeMethod( method ) )
      {
        /*
         * If we get here the method was not annotated so we can try to detect if it is a
         * candidate @Observable in case some @Observables are not fully specified.
         */
        if ( method.getModifiers().contains( Modifier.FINAL ) )
        {
          continue;
        }
        else if ( method.getModifiers().contains( Modifier.STATIC ) )
        {
          continue;
        }

        final boolean voidReturn = method.getReturnType().getKind() == TypeKind.VOID;
        final int parameterCount = method.getParameters().size();
        final String methodName = method.getSimpleName().toString();
        if ( voidReturn &&
             1 == parameterCount &&
             methodName.startsWith( "set" ) &&
             ( methodName.length() > 3 && Character.isUpperCase( methodName.charAt( 3 ) ) ) )
        {
          final String observableName = Character.toLowerCase( methodName.charAt( 3 ) ) + methodName.substring( 4 );
          setters.put( observableName, method );
        }
        else if ( !voidReturn &&
                  0 == parameterCount &&
                  methodName.startsWith( "get" ) &&
                  ( methodName.length() > 3 && Character.isUpperCase( methodName.charAt( 3 ) ) ) )
        {
          final String observableName = Character.toLowerCase( methodName.charAt( 3 ) ) + methodName.substring( 4 );
          getters.put( observableName, method );
        }
        else if ( !voidReturn &&
                  0 == parameterCount &&
                  methodName.startsWith( "is" ) &&
                  ( methodName.length() > 2 && Character.isUpperCase( methodName.charAt( 2 ) ) ) )
        {
          final String observableName = Character.toLowerCase( methodName.charAt( 2 ) ) + methodName.substring( 3 );
          getters.put( observableName, method );
        }
      }
    }

    linkUnAnnotatedObservables( getters, setters );
  }

  private void linkUnAnnotatedObservables( @Nonnull final Map<String, ExecutableElement> getters,
                                           @Nonnull final Map<String, ExecutableElement> setters )
    throws ArezProcessorException
  {
    for ( final ObservableDescriptor observable : getObservables() )
    {
      if ( !observable.hasSetter() )
      {
        final ExecutableElement element = setters.get( observable.getName() );
        if ( null != element )
        {
          observable.setSetter( element );
        }
        else
        {
          throw new ArezProcessorException( "@Observable target defined getter but no setter was defined and no " +
                                            "setter could be automatically determined", observable.getGetter() );
        }
      }
      else if ( !observable.hasGetter() )
      {
        final ExecutableElement element = getters.get( observable.getName() );
        if ( null != element )
        {
          observable.setGetter( element );
        }
        else
        {
          throw new ArezProcessorException( "@Observable target defined setter but no getter was defined and no " +
                                            "getter could be automatically determined", observable.getSetter() );
        }
      }
    }
  }

  private boolean analyzeMethod( @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    verifyNoDuplicateAnnotations( method );

    final Action action = method.getAnnotation( Action.class );
    final Autorun autorun = method.getAnnotation( Autorun.class );
    final Observable observable = method.getAnnotation( Observable.class );
    final Computed computed = method.getAnnotation( Computed.class );
    final ContainerId containerId = method.getAnnotation( ContainerId.class );
    final PostConstruct postConstruct = method.getAnnotation( PostConstruct.class );
    final PreDispose preDispose = method.getAnnotation( PreDispose.class );
    final PostDispose postDispose = method.getAnnotation( PostDispose.class );
    final OnActivate onActivate = method.getAnnotation( OnActivate.class );
    final OnDeactivate onDeactivate = method.getAnnotation( OnDeactivate.class );
    final OnStale onStale = method.getAnnotation( OnStale.class );
    final OnDispose onDispose = method.getAnnotation( OnDispose.class );

    if ( null != observable )
    {
      addObservable( observable, method );
      return true;
    }
    else if ( null != action )
    {
      addAction( action, method );
      return true;
    }
    else if ( null != autorun )
    {
      addAutorun( autorun, method );
      return true;
    }
    else if ( null != computed )
    {
      addComputed( computed, method );
      return true;
    }
    else if ( null != containerId )
    {
      setContainerId( method );
      return true;
    }
    else if ( null != postConstruct )
    {
      setPostConstruct( method );
      return true;
    }
    else if ( null != preDispose )
    {
      setPreDispose( method );
      return true;
    }
    else if ( null != postDispose )
    {
      setPostDispose( method );
      return true;
    }
    else if ( null != onActivate )
    {
      addOnActivate( onActivate, method );
      return true;
    }
    else if ( null != onDeactivate )
    {
      addOnDeactivate( onDeactivate, method );
      return true;
    }
    else if ( null != onStale )
    {
      addOnStale( onStale, method );
      return true;
    }
    else if ( null != onDispose )
    {
      addOnDispose( onDispose, method );
      return true;
    }
    else
    {
      return false;
    }
  }

  private void verifyNoDuplicateAnnotations( @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    @SuppressWarnings( "unchecked" )
    final Class<? extends Annotation>[] annotationTypes =
      new Class[]{ Action.class,
                   Autorun.class,
                   Observable.class,
                   Computed.class,
                   ContainerId.class,
                   PostConstruct.class,
                   PreDispose.class,
                   PostDispose.class,
                   OnActivate.class,
                   OnDeactivate.class,
                   OnStale.class };
    for ( int i = 0; i < annotationTypes.length; i++ )
    {
      final Class<? extends Annotation> type1 = annotationTypes[ i ];
      final Object annotation1 = method.getAnnotation( type1 );
      if ( null != annotation1 )
      {
        for ( int j = i + 1; j < annotationTypes.length; j++ )
        {
          final Class<? extends Annotation> type2 = annotationTypes[ j ];
          final Object annotation2 = method.getAnnotation( type2 );
          if ( null != annotation2 )
          {
            final String message =
              "Method can not be annotated with both @" + type1.getSimpleName() + " and @" + type2.getSimpleName();
            throw new ArezProcessorException( message, method );
          }
        }
      }
    }
  }

  @Nonnull
  private String getPropertyAccessorName( @Nonnull final ExecutableElement method, @Nonnull final String specifiedName )
    throws ArezProcessorException
  {
    String name = ProcessorUtil.deriveName( method, GETTER_PATTERN, specifiedName );
    if ( null != name )
    {
      return name;
    }
    if ( method.getReturnType().getKind() == TypeKind.BOOLEAN )
    {
      name = ProcessorUtil.deriveName( method, ISSER_PATTERN, specifiedName );
      if ( null != name )
      {
        return name;
      }
    }
    return method.getSimpleName().toString();
  }
}
