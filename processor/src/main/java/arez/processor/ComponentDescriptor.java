package arez.processor;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * The class that represents the parsed state of ArezComponent annotated class.
 */
@SuppressWarnings( "Duplicates" )
final class ComponentDescriptor
{
  private static final Pattern OBSERVABLE_REF_PATTERN = Pattern.compile( "^get([A-Z].*)Observable$" );
  private static final Pattern COMPUTED_VALUE_REF_PATTERN = Pattern.compile( "^get([A-Z].*)ComputedValue$" );
  private static final Pattern OBSERVER_REF_PATTERN = Pattern.compile( "^get([A-Z].*)Observer$" );
  private static final Pattern SETTER_PATTERN = Pattern.compile( "^set([A-Z].*)$" );
  private static final Pattern GETTER_PATTERN = Pattern.compile( "^get([A-Z].*)$" );
  private static final Pattern ISSER_PATTERN = Pattern.compile( "^is([A-Z].*)$" );
  private static final List<String> OBJECT_METHODS =
    Arrays.asList( "hashCode", "equals", "clone", "toString", "finalize" );

  @Nullable
  private String _repositoryName;
  @Nullable
  private List<TypeElement> _repositoryExtensions;
  /**
   * Flag controlling whether dagger module is created for repository.
   */
  private String _repositoryDaggerConfig = "AUTODETECT";
  /**
   * Flag controlling whether Inject annotation is added to repository constructor.
   */
  private String _repositoryInjectConfig = "AUTODETECT";
  @Nonnull
  private final Elements _elements;
  @Nonnull
  private final String _type;
  private final boolean _nameIncludesId;
  private final boolean _allowEmpty;
  private final boolean _injectClassesPresent;
  private final boolean _inject;
  private final boolean _dagger;
  /**
   * Scope annotation that is declared on component and should be transferred to injection providers.
   */
  private final AnnotationMirror _scopeAnnotation;
  private final boolean _deferSchedule;
  private final boolean _generateToString;
  @Nonnull
  private final PackageElement _packageElement;
  @Nonnull
  private final TypeElement _element;
  @Nullable
  private ExecutableElement _postConstruct;
  @Nullable
  private ExecutableElement _componentId;
  @Nullable
  private ExecutableType _componentIdMethodType;
  @Nullable
  private ExecutableElement _componentRef;
  @Nullable
  private ExecutableElement _contextRef;
  @Nullable
  private ExecutableElement _componentTypeName;
  @Nullable
  private ExecutableElement _componentName;
  @Nullable
  private ExecutableElement _preDispose;
  @Nullable
  private ExecutableElement _postDispose;
  private final Map<String, CandidateMethod> _observerRefs = new HashMap<>();
  private final Map<String, ObservableDescriptor> _observables = new HashMap<>();
  private final Collection<ObservableDescriptor> _roObservables =
    Collections.unmodifiableCollection( _observables.values() );
  private final Map<String, ActionDescriptor> _actions = new HashMap<>();
  private final Collection<ActionDescriptor> _roActions =
    Collections.unmodifiableCollection( _actions.values() );
  private final Map<String, ComputedDescriptor> _computeds = new HashMap<>();
  private final Collection<ComputedDescriptor> _roComputeds =
    Collections.unmodifiableCollection( _computeds.values() );
  private final Map<String, MemoizeDescriptor> _memoizes = new HashMap<>();
  private final Collection<MemoizeDescriptor> _roMemoizes =
    Collections.unmodifiableCollection( _memoizes.values() );
  private final Map<String, AutorunDescriptor> _autoruns = new HashMap<>();
  private final Collection<AutorunDescriptor> _roAutoruns =
    Collections.unmodifiableCollection( _autoruns.values() );
  private final Map<String, TrackedDescriptor> _trackeds = new HashMap<>();
  private final Collection<TrackedDescriptor> _roTrackeds =
    Collections.unmodifiableCollection( _trackeds.values() );

  ComponentDescriptor( @Nonnull final Elements elements,
                       @Nonnull final String type,
                       final boolean nameIncludesId,
                       final boolean allowEmpty,
                       final boolean injectClassesPresent,
                       final boolean inject,
                       final boolean dagger,
                       @Nullable final AnnotationMirror scopeAnnotation,
                       final boolean deferSchedule,
                       final boolean generateToString,
                       @Nonnull final PackageElement packageElement,
                       @Nonnull final TypeElement element )
  {
    _elements = Objects.requireNonNull( elements );
    _type = Objects.requireNonNull( type );
    _nameIncludesId = nameIncludesId;
    _allowEmpty = allowEmpty;
    _injectClassesPresent = injectClassesPresent;
    _inject = inject;
    _dagger = dagger;
    _scopeAnnotation = scopeAnnotation;
    _deferSchedule = deferSchedule;
    _generateToString = generateToString;
    _packageElement = Objects.requireNonNull( packageElement );
    _element = Objects.requireNonNull( element );
  }

  private boolean hasDeprecatedElements()
  {
    return isDeprecated( _postConstruct ) ||
           _roObservables.stream().anyMatch( e -> ( e.hasSetter() && isDeprecated( e.getSetter() ) ) ||
                                                  ( e.hasGetter() && isDeprecated( e.getGetter() ) ) ) ||
           _roComputeds.stream().anyMatch( e -> ( e.hasComputed() && isDeprecated( e.getComputed() ) ) ||
                                                isDeprecated( e.getOnActivate() ) ||
                                                isDeprecated( e.getOnDeactivate() ) ||
                                                isDeprecated( e.getOnStale() ) ||
                                                isDeprecated( e.getOnDispose() ) ) ||
           _roActions.stream().anyMatch( e -> isDeprecated( e.getAction() ) ) ||
           _roAutoruns.stream().anyMatch( e -> isDeprecated( e.getAutorun() ) ) ||
           _roMemoizes.stream().anyMatch( e -> isDeprecated( e.getMemoize() ) ) ||
           _roTrackeds.stream().anyMatch( e -> ( e.hasTrackedMethod() && isDeprecated( e.getTrackedMethod() ) ) ||
                                               ( e.hasOnDepsChangedMethod() &&
                                                 isDeprecated( e.getOnDepsChangedMethod() ) ) );

  }

  private boolean isDeprecated( @Nullable final ExecutableElement element )
  {
    return null != element && null != element.getAnnotation( Deprecated.class );
  }

  @Nonnull
  private DeclaredType asDeclaredType()
  {
    return (DeclaredType) _element.asType();
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

  @Nonnull
  private TrackedDescriptor findOrCreateTracked( @Nonnull final String name )
  {
    return _trackeds.computeIfAbsent( name, n -> new TrackedDescriptor( this, n ) );
  }

  private void addObservable( @Nonnull final AnnotationMirror annotation,
                              @Nonnull final ExecutableElement method,
                              @Nonnull final ExecutableType methodType )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( Constants.OBSERVABLE_ANNOTATION_CLASSNAME, method );

    final String declaredName = getAnnotationParameter( annotation, "name" );
    final boolean expectSetter = getAnnotationParameter( annotation, "expectSetter" );

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

      name = ProcessorUtil.deriveName( method, SETTER_PATTERN, declaredName );
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
      name = getPropertyAccessorName( method, declaredName );
    }
    // Override name if supplied by user
    if ( !ProcessorUtil.isSentinelName( declaredName ) )
    {
      name = declaredName;
      if ( !ProcessorUtil.isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @Observable specified invalid name " + name,
                                          method );
      }
    }
    checkNameUnique( name, method, Constants.OBSERVABLE_ANNOTATION_CLASSNAME );

    if ( setter && !expectSetter )
    {
      throw new ArezProcessorException( "Method annotated with @Observable is a setter but defines " +
                                        "expectSetter = false for observable named " + name, method );
    }

    final ObservableDescriptor observable = findOrCreateObservable( name );
    if ( !expectSetter )
    {
      observable.setExpectSetter( false );
    }
    if ( !observable.expectSetter() )
    {
      if ( observable.hasSetter() )
      {
        throw new ArezProcessorException( "Method annotated with @Observable defines expectSetter = false but a " +
                                          "setter exists named " + observable.getSetter().getSimpleName() +
                                          "for observable named " + name, method );
      }
    }
    if ( setter )
    {
      if ( observable.hasSetter() )
      {
        throw new ArezProcessorException( "Method annotated with @Observable defines duplicate setter for " +
                                          "observable named " + name, method );
      }
      if ( !observable.expectSetter() )
      {
        throw new ArezProcessorException( "Method annotated with @Observable defines expectSetter = false but a " +
                                          "setter exists for observable named " + name, method );
      }
      observable.setSetter( method, methodType );
    }
    else
    {
      if ( observable.hasGetter() )
      {
        throw new ArezProcessorException( "Method annotated with @Observable defines duplicate getter for " +
                                          "observable named " + name, method );
      }
      observable.setGetter( method, methodType );
    }
  }

  private void addObservableRef( @Nonnull final AnnotationMirror annotation,
                                 @Nonnull final ExecutableElement method,
                                 @Nonnull final ExecutableType methodType )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( Constants.OBSERVABLE_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustBeAbstract( Constants.OBSERVABLE_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotHaveAnyParameters( Constants.OBSERVABLE_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotThrowAnyExceptions( Constants.OBSERVABLE_REF_ANNOTATION_CLASSNAME, method );

    final TypeMirror returnType = methodType.getReturnType();
    if ( TypeKind.DECLARED != returnType.getKind() ||
         !toRawType( returnType ).toString().equals( "arez.Observable" ) )
    {
      throw new ArezProcessorException( "Method annotated with @ObservableRef must return an instance of " +
                                        "arez.Observable", method );
    }

    final String declaredName = getAnnotationParameter( annotation, "name" );
    final String name;
    if ( ProcessorUtil.isSentinelName( declaredName ) )
    {
      name = ProcessorUtil.deriveName( method, OBSERVABLE_REF_PATTERN, declaredName );
      if ( null == name )
      {
        throw new ArezProcessorException( "Method annotated with @ObservableRef should specify name or be " +
                                          "named according to the convention get[Name]Observable", method );
      }
    }
    else
    {
      name = declaredName;
      if ( !ProcessorUtil.isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @ObservableRef specified invalid name " + name,
                                          method );
      }
    }

    final ObservableDescriptor observable = findOrCreateObservable( name );

    if ( observable.hasRefMethod() )
    {
      throw new ArezProcessorException( "Method annotated with @ObservableRef defines duplicate ref accessor for " +
                                        "observable named " + name, method );
    }
    observable.setRefMethod( method, methodType );
  }

  @Nonnull
  private TypeName toRawType( @Nonnull final TypeMirror type )
  {
    final TypeName typeName = TypeName.get( type );
    if ( typeName instanceof ParameterizedTypeName )
    {
      return ( (ParameterizedTypeName) typeName ).rawType;
    }
    else
    {
      return typeName;
    }
  }

  private void addAction( @Nonnull final AnnotationMirror annotation,
                          @Nonnull final ExecutableElement method,
                          @Nonnull final ExecutableType methodType )
    throws ArezProcessorException
  {
    MethodChecks.mustBeWrappable( Constants.ACTION_ANNOTATION_CLASSNAME, method );

    final String name = deriveActionName( method, annotation );
    checkNameUnique( name, method, Constants.ACTION_ANNOTATION_CLASSNAME );
    final boolean mutation = getAnnotationParameter( annotation, "mutation" );
    final boolean reportParameters = getAnnotationParameter( annotation, "reportParameters" );
    final ActionDescriptor action = new ActionDescriptor( this, name, mutation, reportParameters, method, methodType );
    _actions.put( action.getName(), action );
  }

  @Nonnull
  private String deriveActionName( @Nonnull final ExecutableElement method, @Nonnull final AnnotationMirror annotation )
    throws ArezProcessorException
  {
    final String name = getAnnotationParameter( annotation, "name" );
    if ( ProcessorUtil.isSentinelName( name ) )
    {
      return method.getSimpleName().toString();
    }
    else
    {
      if ( !ProcessorUtil.isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @Action specified invalid name " + name, method );
      }
      return name;
    }
  }

  private void addAutorun( @Nonnull final AnnotationMirror annotation,
                           @Nonnull final ExecutableElement method,
                           @Nonnull final ExecutableType methodType )
    throws ArezProcessorException
  {
    MethodChecks.mustBeWrappable( Constants.AUTORUN_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotHaveAnyParameters( Constants.AUTORUN_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotThrowAnyExceptions( Constants.AUTORUN_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotReturnAnyValue( Constants.AUTORUN_ANNOTATION_CLASSNAME, method );

    final String name = deriveAutorunName( method, annotation );
    checkNameUnique( name, method, Constants.AUTORUN_ANNOTATION_CLASSNAME );
    final boolean mutation = getAnnotationParameter( annotation, "mutation" );
    final AutorunDescriptor autorun = new AutorunDescriptor( this, name, mutation, method, methodType );
    _autoruns.put( autorun.getName(), autorun );
  }

  @Nonnull
  private String deriveAutorunName( @Nonnull final ExecutableElement method,
                                    @Nonnull final AnnotationMirror annotation )
    throws ArezProcessorException
  {
    final String name = getAnnotationParameter( annotation, "name" );
    if ( ProcessorUtil.isSentinelName( name ) )
    {
      return method.getSimpleName().toString();
    }
    else
    {
      if ( !ProcessorUtil.isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @Autorun specified invalid name " + name, method );
      }
      return name;
    }
  }

  private void addOnDepsChanged( @Nonnull final AnnotationMirror annotation, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final String name =
      deriveHookName( method,
                      TrackedDescriptor.ON_DEPS_CHANGED_PATTERN,
                      "DepsChanged",
                      getAnnotationParameter( annotation, "name" ) );
    findOrCreateTracked( name ).setOnDepsChangedMethod( method );
  }

  private void addTracked( @Nonnull final AnnotationMirror annotation,
                           @Nonnull final ExecutableElement method,
                           @Nonnull final ExecutableType methodType )
    throws ArezProcessorException
  {
    final String name = deriveTrackedName( method, annotation );
    checkNameUnique( name, method, Constants.TRACK_ANNOTATION_CLASSNAME );
    final boolean mutation = getAnnotationParameter( annotation, "mutation" );
    final boolean reportParameters = getAnnotationParameter( annotation, "reportParameters" );
    final TrackedDescriptor tracked = findOrCreateTracked( name );
    tracked.setTrackedMethod( mutation, reportParameters, method, methodType );
  }

  @Nonnull
  private String deriveTrackedName( @Nonnull final ExecutableElement method,
                                    @Nonnull final AnnotationMirror annotation )
    throws ArezProcessorException
  {
    final String name = getAnnotationParameter( annotation, "name" );
    if ( ProcessorUtil.isSentinelName( name ) )
    {
      return method.getSimpleName().toString();
    }
    else
    {
      if ( !ProcessorUtil.isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @Track specified invalid name " + name, method );
      }
      return name;
    }
  }

  private void addObserverRef( @Nonnull final AnnotationMirror annotation,
                               @Nonnull final ExecutableElement method,
                               @Nonnull final ExecutableType methodType )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( Constants.OBSERVER_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustBeAbstract( Constants.OBSERVER_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotHaveAnyParameters( Constants.OBSERVER_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotThrowAnyExceptions( Constants.OBSERVER_REF_ANNOTATION_CLASSNAME, method );

    final TypeMirror returnType = method.getReturnType();
    if ( TypeKind.DECLARED != returnType.getKind() ||
         !returnType.toString().equals( "arez.Observer" ) )
    {
      throw new ArezProcessorException( "Method annotated with @ObserverRef must return an instance of " +
                                        "arez.Observer", method );
    }

    final String declaredName = getAnnotationParameter( annotation, "name" );
    final String name;
    if ( ProcessorUtil.isSentinelName( declaredName ) )
    {
      name = ProcessorUtil.deriveName( method, OBSERVER_REF_PATTERN, declaredName );
      if ( null == name )
      {
        throw new ArezProcessorException( "Method annotated with @ObserverRef should specify name or be " +
                                          "named according to the convention get[Name]Observer", method );
      }
    }
    else
    {
      name = declaredName;
      if ( !ProcessorUtil.isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @ObserverRef specified invalid name " + name,
                                          method );
      }
    }
    if ( _observerRefs.containsKey( name ) )
    {
      throw new ArezProcessorException( "Method annotated with @ObserverRef defines duplicate ref accessor for " +
                                        "observer named " + name, method );
    }
    _observerRefs.put( name, new CandidateMethod( method, methodType ) );
  }

  @Nonnull
  private ComputedDescriptor findOrCreateComputed( @Nonnull final String name )
  {
    return _computeds.computeIfAbsent( name, n -> new ComputedDescriptor( this, n ) );
  }

  private void addComputed( @Nonnull final AnnotationMirror annotation,
                            @Nonnull final ExecutableElement method,
                            @Nonnull final ExecutableType computedType )
    throws ArezProcessorException
  {
    final String name = deriveComputedName( method, annotation );
    checkNameUnique( name, method, Constants.COMPUTED_ANNOTATION_CLASSNAME );
    findOrCreateComputed( name ).setComputed( method, computedType );
  }

  private void addComputedValueRef( @Nonnull final AnnotationMirror annotation,
                                    @Nonnull final ExecutableElement method,
                                    @Nonnull final ExecutableType methodType )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( Constants.COMPUTED_VALUE_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustBeAbstract( Constants.COMPUTED_VALUE_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotHaveAnyParameters( Constants.COMPUTED_VALUE_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotThrowAnyExceptions( Constants.COMPUTED_VALUE_REF_ANNOTATION_CLASSNAME, method );

    final TypeMirror returnType = methodType.getReturnType();
    if ( TypeKind.DECLARED != returnType.getKind() ||
         !toRawType( returnType ).toString().equals( "arez.ComputedValue" ) )
    {
      throw new ArezProcessorException( "Method annotated with @ComputedValueRef must return an instance of " +
                                        "arez.ComputedValue", method );
    }

    final String declaredName = getAnnotationParameter( annotation, "name" );
    final String name;
    if ( ProcessorUtil.isSentinelName( declaredName ) )
    {
      name = ProcessorUtil.deriveName( method, COMPUTED_VALUE_REF_PATTERN, declaredName );
      if ( null == name )
      {
        throw new ArezProcessorException( "Method annotated with @ComputedValueRef should specify name or be " +
                                          "named according to the convention get[Name]ComputedValue", method );
      }
    }
    else
    {
      name = declaredName;
      if ( !ProcessorUtil.isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @ComputedValueRef specified invalid name " + name,
                                          method );
      }
    }

    findOrCreateComputed( name ).setRefMethod( method, methodType );
  }

  @Nonnull
  private String deriveComputedName( @Nonnull final ExecutableElement method,
                                     @Nonnull final AnnotationMirror annotation )
    throws ArezProcessorException
  {
    final String name = getAnnotationParameter( annotation, "name" );
    if ( ProcessorUtil.isSentinelName( name ) )
    {
      return getPropertyAccessorName( method, name );
    }
    else
    {
      if ( !ProcessorUtil.isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @Computed specified invalid name " + name, method );
      }
      return name;
    }
  }

  private void addMemoize( @Nonnull final AnnotationMirror annotation,
                           @Nonnull final ExecutableElement method,
                           @Nonnull final ExecutableType methodType )
    throws ArezProcessorException
  {
    final String name = deriveMemoizeName( method, annotation );
    checkNameUnique( name, method, Constants.MEMOIZE_ANNOTATION_CLASSNAME );
    _memoizes.computeIfAbsent( name, n -> new MemoizeDescriptor( this, n ) ).setMemoize( method, methodType );
  }

  @Nonnull
  private String deriveMemoizeName( @Nonnull final ExecutableElement method,
                                    @Nonnull final AnnotationMirror annotation )
    throws ArezProcessorException
  {
    final String name = getAnnotationParameter( annotation, "name" );
    if ( ProcessorUtil.isSentinelName( name ) )
    {
      return method.getSimpleName().toString();
    }
    else
    {
      if ( !ProcessorUtil.isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Method annotated with @Memoize specified invalid name " + name, method );
      }
      return name;
    }
  }

  private void addOnActivate( @Nonnull final AnnotationMirror annotation, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final String name = deriveHookName( method,
                                        ComputedDescriptor.ON_ACTIVATE_PATTERN,
                                        "Activate",
                                        getAnnotationParameter( annotation, "name" ) );
    findOrCreateComputed( name ).setOnActivate( method );
  }

  private void addOnDeactivate( @Nonnull final AnnotationMirror annotation, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final String name =
      deriveHookName( method,
                      ComputedDescriptor.ON_DEACTIVATE_PATTERN,
                      "Deactivate",
                      getAnnotationParameter( annotation, "name" ) );
    findOrCreateComputed( name ).setOnDeactivate( method );
  }

  private void addOnStale( @Nonnull final AnnotationMirror annotation, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final String name =
      deriveHookName( method,
                      ComputedDescriptor.ON_STALE_PATTERN,
                      "Stale",
                      getAnnotationParameter( annotation, "name" ) );
    findOrCreateComputed( name ).setOnStale( method );
  }

  private void addOnDispose( @Nonnull final AnnotationMirror annotation, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final String name = deriveHookName( method,
                                        ComputedDescriptor.ON_DISPOSE_PATTERN,
                                        "Dispose",
                                        getAnnotationParameter( annotation, "name" ) );
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
    else if ( !ProcessorUtil.isJavaIdentifier( value ) )
    {
      throw new ArezProcessorException( "Method annotated with @On" + type + " specified invalid name " + value,
                                        method );
    }
    else
    {
      return value;
    }
  }

  private void setContextRef( @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( Constants.CONTEXT_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustBeAbstract( Constants.CONTEXT_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotHaveAnyParameters( Constants.CONTEXT_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustReturnAValue( Constants.CONTEXT_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotThrowAnyExceptions( Constants.CONTEXT_REF_ANNOTATION_CLASSNAME, method );

    final TypeMirror returnType = method.getReturnType();
    if ( TypeKind.DECLARED != returnType.getKind() ||
         !returnType.toString().equals( "arez.ArezContext" ) )
    {
      throw new ArezProcessorException( "Method annotated with @ContextRef must return an instance of " +
                                        "arez.ArezContext", method );
    }

    if ( null != _contextRef )
    {
      throw new ArezProcessorException( "@ContextRef target duplicates existing method named " +
                                        _contextRef.getSimpleName(), method );
    }
    else
    {
      _contextRef = Objects.requireNonNull( method );
    }
  }

  private void setComponentRef( @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( Constants.COMPONENT_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustBeAbstract( Constants.COMPONENT_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotHaveAnyParameters( Constants.COMPONENT_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustReturnAValue( Constants.COMPONENT_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotThrowAnyExceptions( Constants.COMPONENT_REF_ANNOTATION_CLASSNAME, method );

    final TypeMirror returnType = method.getReturnType();
    if ( TypeKind.DECLARED != returnType.getKind() ||
         !returnType.toString().equals( "arez.Component" ) )
    {
      throw new ArezProcessorException( "Method annotated with @ComponentRef must return an instance of " +
                                        "arez.Component", method );
    }

    if ( null != _componentRef )
    {
      throw new ArezProcessorException( "@ComponentRef target duplicates existing method named " +
                                        _componentRef.getSimpleName(), method );
    }
    else
    {
      _componentRef = Objects.requireNonNull( method );
    }
  }

  private void setComponentId( @Nonnull final ExecutableElement componentId,
                               @Nonnull final ExecutableType componentIdMethodType )
    throws ArezProcessorException
  {
    MethodChecks.mustNotBeAbstract( Constants.COMPONENT_ID_ANNOTATION_CLASSNAME, componentId );
    MethodChecks.mustBeSubclassCallable( Constants.COMPONENT_ID_ANNOTATION_CLASSNAME, componentId );
    MethodChecks.mustBeFinal( Constants.COMPONENT_ID_ANNOTATION_CLASSNAME, componentId );
    MethodChecks.mustNotHaveAnyParameters( Constants.COMPONENT_ID_ANNOTATION_CLASSNAME, componentId );
    MethodChecks.mustReturnAValue( Constants.COMPONENT_ID_ANNOTATION_CLASSNAME, componentId );
    MethodChecks.mustNotThrowAnyExceptions( Constants.COMPONENT_ID_ANNOTATION_CLASSNAME, componentId );

    if ( null != _componentId )
    {
      throw new ArezProcessorException( "@ComponentId target duplicates existing method named " +
                                        _componentId.getSimpleName(), componentId );
    }
    else
    {
      _componentId = Objects.requireNonNull( componentId );
      _componentIdMethodType = componentIdMethodType;
    }
  }

  private void setComponentTypeNameRef( @Nonnull final ExecutableElement componentTypeName )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( Constants.COMPONENT_TYPE_NAME_REF_ANNOTATION_CLASSNAME, componentTypeName );
    MethodChecks.mustBeAbstract( Constants.COMPONENT_TYPE_NAME_REF_ANNOTATION_CLASSNAME, componentTypeName );
    MethodChecks.mustNotHaveAnyParameters( Constants.COMPONENT_TYPE_NAME_REF_ANNOTATION_CLASSNAME, componentTypeName );
    MethodChecks.mustReturnAValue( Constants.COMPONENT_TYPE_NAME_REF_ANNOTATION_CLASSNAME, componentTypeName );
    MethodChecks.mustNotThrowAnyExceptions( Constants.COMPONENT_TYPE_NAME_REF_ANNOTATION_CLASSNAME, componentTypeName );

    final TypeMirror returnType = componentTypeName.getReturnType();
    if ( !( TypeKind.DECLARED == returnType.getKind() &&
            returnType.toString().equals( String.class.getName() ) ) )
    {
      throw new ArezProcessorException( "@ComponentTypeNameRef target must return a String", componentTypeName );
    }

    if ( null != _componentTypeName )
    {
      throw new ArezProcessorException( "@ComponentTypeNameRef target duplicates existing method named " +
                                        _componentTypeName.getSimpleName(), componentTypeName );
    }
    else
    {
      _componentTypeName = Objects.requireNonNull( componentTypeName );
    }
  }

  private void setComponentNameRef( @Nonnull final ExecutableElement componentName )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( Constants.COMPONENT_NAME_REF_ANNOTATION_CLASSNAME, componentName );
    MethodChecks.mustBeAbstract( Constants.COMPONENT_NAME_REF_ANNOTATION_CLASSNAME, componentName );
    MethodChecks.mustNotHaveAnyParameters( Constants.COMPONENT_NAME_REF_ANNOTATION_CLASSNAME, componentName );
    MethodChecks.mustReturnAValue( Constants.COMPONENT_NAME_REF_ANNOTATION_CLASSNAME, componentName );
    MethodChecks.mustNotThrowAnyExceptions( Constants.COMPONENT_NAME_REF_ANNOTATION_CLASSNAME, componentName );

    if ( null != _componentName )
    {
      throw new ArezProcessorException( "@ComponentNameRef target duplicates existing method named " +
                                        _componentName.getSimpleName(), componentName );
    }
    else
    {
      _componentName = Objects.requireNonNull( componentName );
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
    MethodChecks.mustBeLifecycleHook( Constants.POST_CONSTRUCT_ANNOTATION_CLASSNAME, postConstruct );

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

  private void setPreDispose( @Nonnull final ExecutableElement preDispose )
    throws ArezProcessorException
  {
    MethodChecks.mustBeLifecycleHook( Constants.PRE_DISPOSE_ANNOTATION_CLASSNAME, preDispose );

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

  private void setPostDispose( @Nonnull final ExecutableElement postDispose )
    throws ArezProcessorException
  {
    MethodChecks.mustBeLifecycleHook( Constants.POST_DISPOSE_ANNOTATION_CLASSNAME, postDispose );

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

  @Nonnull
  Collection<ObservableDescriptor> getObservables()
  {
    return _roObservables;
  }

  void validate()
    throws ArezProcessorException
  {
    validateObservables();
    validateComputeds();

    if ( !_allowEmpty &&
         _roObservables.isEmpty() &&
         _roActions.isEmpty() &&
         _roComputeds.isEmpty() &&
         _roMemoizes.isEmpty() &&
         _roTrackeds.isEmpty() &&
         _roAutoruns.isEmpty() )
    {
      throw new ArezProcessorException( "@ArezComponent target has no methods annotated with @Action, " +
                                        "@Computed, @Memoize, @Observable, @Track or @Autorun", _element );
    }
  }

  private void validateObservables()
    throws ArezProcessorException
  {
    for ( final ObservableDescriptor observable : _roObservables )
    {
      observable.validate();
    }
  }

  private void validateComputeds()
    throws ArezProcessorException
  {
    for ( final ComputedDescriptor computed : _roComputeds )
    {
      computed.validate();
    }
  }

  private void checkNameUnique( @Nonnull final String name,
                                @Nonnull final ExecutableElement sourceMethod,
                                @Nonnull final String sourceAnnotationName )
    throws ArezProcessorException
  {
    final ActionDescriptor action = _actions.get( name );
    if ( null != action )
    {
      throw toException( name,
                         sourceAnnotationName,
                         sourceMethod,
                         Constants.ACTION_ANNOTATION_CLASSNAME,
                         action.getAction() );
    }
    final ComputedDescriptor computed = _computeds.get( name );
    if ( null != computed && computed.hasComputed() )
    {
      throw toException( name,
                         sourceAnnotationName,
                         sourceMethod,
                         Constants.COMPUTED_ANNOTATION_CLASSNAME,
                         computed.getComputed() );
    }
    final MemoizeDescriptor memoize = _memoizes.get( name );
    if ( null != memoize )
    {
      throw toException( name,
                         sourceAnnotationName,
                         sourceMethod,
                         Constants.MEMOIZE_ANNOTATION_CLASSNAME,
                         memoize.getMemoize() );
    }
    final AutorunDescriptor autorun = _autoruns.get( name );
    if ( null != autorun )
    {
      throw toException( name,
                         sourceAnnotationName,
                         sourceMethod,
                         Constants.AUTORUN_ANNOTATION_CLASSNAME,
                         autorun.getAutorun() );
    }
    // Track have pairs so let the caller determine whether a duplicate occurs in that scenario
    if ( !sourceAnnotationName.equals( Constants.TRACK_ANNOTATION_CLASSNAME ) )
    {
      final TrackedDescriptor tracked = _trackeds.get( name );
      if ( null != tracked )
      {
        throw toException( name,
                           sourceAnnotationName,
                           sourceMethod,
                           Constants.TRACK_ANNOTATION_CLASSNAME,
                           tracked.getTrackedMethod() );
      }
    }
    // Observables have pairs so let the caller determine whether a duplicate occurs in that scenario
    if ( !sourceAnnotationName.equals( Constants.OBSERVABLE_ANNOTATION_CLASSNAME ) )
    {
      final ObservableDescriptor observable = _observables.get( name );
      if ( null != observable )
      {
        throw toException( name,
                           sourceAnnotationName,
                           sourceMethod,
                           Constants.OBSERVABLE_ANNOTATION_CLASSNAME,
                           observable.getDefiner() );
      }
    }
  }

  @Nonnull
  private ArezProcessorException toException( @Nonnull final String name,
                                              @Nonnull final String sourceAnnotationName,
                                              @Nonnull final ExecutableElement sourceMethod,
                                              @Nonnull final String targetAnnotationName,
                                              @Nonnull final ExecutableElement targetElement )
  {
    return new ArezProcessorException( "Method annotated with @" + ProcessorUtil.toSimpleName( sourceAnnotationName ) +
                                       " specified name " + name + " that duplicates @" +
                                       ProcessorUtil.toSimpleName( targetAnnotationName ) + " defined by method " +
                                       targetElement.getSimpleName(), sourceMethod );
  }

  void analyzeCandidateMethods( @Nonnull final List<ExecutableElement> methods,
                                @Nonnull final Types typeUtils )
    throws ArezProcessorException
  {
    final Map<String, CandidateMethod> getters = new HashMap<>();
    final Map<String, CandidateMethod> setters = new HashMap<>();
    final Map<String, CandidateMethod> trackeds = new HashMap<>();
    final Map<String, CandidateMethod> onDepsChangeds = new HashMap<>();
    for ( final ExecutableElement method : methods )
    {
      final ExecutableType methodType =
        (ExecutableType) typeUtils.asMemberOf( (DeclaredType) _element.asType(), method );
      if ( !analyzeMethod( method, methodType ) )
      {
        /*
         * If we get here the method was not annotated so we can try to detect if it is a
         * candidate arez method in case some arez annotations are implied via naming conventions.
         */
        if ( method.getModifiers().contains( Modifier.STATIC ) )
        {
          continue;
        }

        final CandidateMethod candidateMethod = new CandidateMethod( method, methodType );
        final boolean voidReturn = method.getReturnType().getKind() == TypeKind.VOID;
        final int parameterCount = method.getParameters().size();
        String name;

        if ( !method.getModifiers().contains( Modifier.FINAL ) )
        {
          name = ProcessorUtil.deriveName( method, SETTER_PATTERN, ProcessorUtil.SENTINEL_NAME );
          if ( voidReturn && 1 == parameterCount && null != name )
          {
            setters.put( name, candidateMethod );
            continue;
          }
          name = ProcessorUtil.deriveName( method, ISSER_PATTERN, ProcessorUtil.SENTINEL_NAME );
          if ( !voidReturn && 0 == parameterCount && null != name )
          {
            getters.put( name, candidateMethod );
            continue;
          }
          name = ProcessorUtil.deriveName( method, GETTER_PATTERN, ProcessorUtil.SENTINEL_NAME );
          if ( !voidReturn && 0 == parameterCount && null != name )
          {
            getters.put( name, candidateMethod );
            continue;
          }
        }
        name =
          ProcessorUtil.deriveName( method, TrackedDescriptor.ON_DEPS_CHANGED_PATTERN, ProcessorUtil.SENTINEL_NAME );
        if ( voidReturn && 0 == parameterCount && null != name )
        {
          onDepsChangeds.put( name, candidateMethod );
          continue;
        }

        final String methodName = method.getSimpleName().toString();
        if ( !OBJECT_METHODS.contains( methodName ) )
        {
          trackeds.put( methodName, candidateMethod );
        }
      }
    }

    linkUnAnnotatedObservables( getters, setters );
    linkUnAnnotatedTracked( trackeds, onDepsChangeds );
    linkObserverRefs();

    /*
     * ALl of the maps will have called remove() for all matching candidates.
     * Thus any left are the non-arez methods.
     */

    ensureNoAbstractMethods( getters.values() );
    ensureNoAbstractMethods( setters.values() );
    ensureNoAbstractMethods( trackeds.values() );
    ensureNoAbstractMethods( onDepsChangeds.values() );
  }

  private void ensureNoAbstractMethods( @Nonnull final Collection<CandidateMethod> candidateMethods )
  {
    candidateMethods
      .stream()
      .map( CandidateMethod::getMethod )
      .filter( m -> m.getModifiers().contains( Modifier.ABSTRACT ) )
      .forEach( m -> {
        throw new ArezProcessorException( "@ArezComponent target has an abstract method not implemented by framework",
                                          m );
      } );
  }

  private void linkObserverRefs()
  {
    for ( final Map.Entry<String, CandidateMethod> entry : _observerRefs.entrySet() )
    {
      final String key = entry.getKey();
      final CandidateMethod method = entry.getValue();
      final AutorunDescriptor autorunDescriptor = _autoruns.get( key );
      if ( null != autorunDescriptor )
      {
        autorunDescriptor.setRefMethod( method.getMethod(), method.getMethodType() );
      }
      else
      {
        final TrackedDescriptor trackedDescriptor = _trackeds.get( key );
        if ( null != trackedDescriptor )
        {
          trackedDescriptor.setRefMethod( method.getMethod(), method.getMethodType() );
        }
        else
        {
          throw new ArezProcessorException( "@ObserverRef target defined observer named '" + key + "' but no " +
                                            "@Autorun or @Track method with that name exists", method.getMethod() );
        }
      }
    }
  }

  private void linkUnAnnotatedObservables( @Nonnull final Map<String, CandidateMethod> getters,
                                           @Nonnull final Map<String, CandidateMethod> setters )
    throws ArezProcessorException
  {
    for ( final ObservableDescriptor observable : _roObservables )
    {
      if ( !observable.hasSetter() && !observable.hasGetter() )
      {
        throw new ArezProcessorException( "@ObservableRef target unable to associated with an Observable property",
                                          observable.getRefMethod() );
      }
      else if ( !observable.hasSetter() && observable.expectSetter() )
      {
        final CandidateMethod candidate = setters.remove( observable.getName() );
        if ( null != candidate )
        {
          observable.setSetter( candidate.getMethod(), candidate.getMethodType() );
        }
        else if ( observable.hasGetter() )
        {
          throw new ArezProcessorException( "@Observable target defined getter but no setter was defined and no " +
                                            "setter could be automatically determined", observable.getGetter() );
        }
      }
      else if ( !observable.hasGetter() )
      {
        final CandidateMethod candidate = getters.remove( observable.getName() );
        if ( null != candidate )
        {
          observable.setGetter( candidate.getMethod(), candidate.getMethodType() );
        }
        else
        {
          throw new ArezProcessorException( "@Observable target defined setter but no getter was defined and no " +
                                            "getter could be automatically determined", observable.getSetter() );
        }
      }
    }
  }

  private void linkUnAnnotatedTracked( @Nonnull final Map<String, CandidateMethod> trackeds,
                                       @Nonnull final Map<String, CandidateMethod> onDepsChangeds )
    throws ArezProcessorException
  {
    for ( final TrackedDescriptor tracked : _roTrackeds )
    {
      if ( !tracked.hasTrackedMethod() )
      {
        final CandidateMethod candidate = trackeds.remove( tracked.getName() );
        if ( null != candidate )
        {
          tracked.setTrackedMethod( false, true, candidate.getMethod(), candidate.getMethodType() );
        }
        else
        {
          throw new ArezProcessorException( "@OnDepsChanged target has no corresponding @Track that could " +
                                            "be automatically determined", tracked.getOnDepsChangedMethod() );
        }
      }
      else if ( !tracked.hasOnDepsChangedMethod() )
      {
        final CandidateMethod candidate = onDepsChangeds.remove( tracked.getName() );
        if ( null != candidate )
        {
          tracked.setOnDepsChangedMethod( candidate.getMethod() );
        }
        else
        {
          throw new ArezProcessorException( "@Track target has no corresponding @OnDepsChanged that could " +
                                            "be automatically determined", tracked.getTrackedMethod() );
        }
      }
    }
  }

  private boolean analyzeMethod( @Nonnull final ExecutableElement method,
                                 @Nonnull final ExecutableType methodType )
    throws ArezProcessorException
  {
    verifyNoDuplicateAnnotations( method );

    final AnnotationMirror action =
      ProcessorUtil.findAnnotationByType( method, Constants.ACTION_ANNOTATION_CLASSNAME );
    final AnnotationMirror autorun =
      ProcessorUtil.findAnnotationByType( method, Constants.AUTORUN_ANNOTATION_CLASSNAME );
    final AnnotationMirror observable =
      ProcessorUtil.findAnnotationByType( method, Constants.OBSERVABLE_ANNOTATION_CLASSNAME );
    final AnnotationMirror observableRef =
      ProcessorUtil.findAnnotationByType( method, Constants.OBSERVABLE_REF_ANNOTATION_CLASSNAME );
    final AnnotationMirror computed =
      ProcessorUtil.findAnnotationByType( method, Constants.COMPUTED_ANNOTATION_CLASSNAME );
    final AnnotationMirror computedValueRef =
      ProcessorUtil.findAnnotationByType( method, Constants.COMPUTED_VALUE_REF_ANNOTATION_CLASSNAME );
    final AnnotationMirror contextRef =
      ProcessorUtil.findAnnotationByType( method, Constants.CONTEXT_REF_ANNOTATION_CLASSNAME );
    final AnnotationMirror componentRef =
      ProcessorUtil.findAnnotationByType( method, Constants.COMPONENT_REF_ANNOTATION_CLASSNAME );
    final AnnotationMirror componentId =
      ProcessorUtil.findAnnotationByType( method, Constants.COMPONENT_ID_ANNOTATION_CLASSNAME );
    final AnnotationMirror componentTypeName =
      ProcessorUtil.findAnnotationByType( method, Constants.COMPONENT_TYPE_NAME_REF_ANNOTATION_CLASSNAME );
    final AnnotationMirror componentName =
      ProcessorUtil.findAnnotationByType( method, Constants.COMPONENT_NAME_REF_ANNOTATION_CLASSNAME );
    final AnnotationMirror postConstruct =
      ProcessorUtil.findAnnotationByType( method, Constants.POST_CONSTRUCT_ANNOTATION_CLASSNAME );
    final AnnotationMirror ejbPostConstruct =
      ProcessorUtil.findAnnotationByType( method, Constants.EJB_POST_CONSTRUCT_ANNOTATION_CLASSNAME );
    final AnnotationMirror preDispose =
      ProcessorUtil.findAnnotationByType( method, Constants.PRE_DISPOSE_ANNOTATION_CLASSNAME );
    final AnnotationMirror postDispose =
      ProcessorUtil.findAnnotationByType( method, Constants.POST_DISPOSE_ANNOTATION_CLASSNAME );
    final AnnotationMirror onActivate =
      ProcessorUtil.findAnnotationByType( method, Constants.ON_ACTIVATE_ANNOTATION_CLASSNAME );
    final AnnotationMirror onDeactivate =
      ProcessorUtil.findAnnotationByType( method, Constants.ON_DEACTIVATE_ANNOTATION_CLASSNAME );
    final AnnotationMirror onStale =
      ProcessorUtil.findAnnotationByType( method, Constants.ON_STALE_ANNOTATION_CLASSNAME );
    final AnnotationMirror onDispose =
      ProcessorUtil.findAnnotationByType( method, Constants.ON_DISPOSE_ANNOTATION_CLASSNAME );
    final AnnotationMirror track =
      ProcessorUtil.findAnnotationByType( method, Constants.TRACK_ANNOTATION_CLASSNAME );
    final AnnotationMirror onDepsChanged =
      ProcessorUtil.findAnnotationByType( method, Constants.ON_DEPS_CHANGED_ANNOTATION_CLASSNAME );
    final AnnotationMirror observerRef =
      ProcessorUtil.findAnnotationByType( method, Constants.OBSERVER_REF_ANNOTATION_CLASSNAME );
    final AnnotationMirror memoize =
      ProcessorUtil.findAnnotationByType( method, Constants.MEMOIZE_ANNOTATION_CLASSNAME );

    if ( null != observable )
    {
      addObservable( observable, method, methodType );
      return true;
    }
    else if ( null != observableRef )
    {
      addObservableRef( observableRef, method, methodType );
      return true;
    }
    else if ( null != action )
    {
      addAction( action, method, methodType );
      return true;
    }
    else if ( null != autorun )
    {
      addAutorun( autorun, method, methodType );
      return true;
    }
    else if ( null != track )
    {
      addTracked( track, method, methodType );
      return true;
    }
    else if ( null != onDepsChanged )
    {
      addOnDepsChanged( onDepsChanged, method );
      return true;
    }
    else if ( null != observerRef )
    {
      addObserverRef( observerRef, method, methodType );
      return true;
    }
    else if ( null != contextRef )
    {
      setContextRef( method );
      return true;
    }
    else if ( null != computed )
    {
      addComputed( computed, method, methodType );
      return true;
    }
    else if ( null != computedValueRef )
    {
      addComputedValueRef( computedValueRef, method, methodType );
      return true;
    }
    else if ( null != memoize )
    {
      addMemoize( memoize, method, methodType );
      return true;
    }
    else if ( null != componentRef )
    {
      setComponentRef( method );
      return true;
    }
    else if ( null != componentId )
    {
      setComponentId( method, methodType );
      return true;
    }
    else if ( null != componentName )
    {
      setComponentNameRef( method );
      return true;
    }
    else if ( null != componentTypeName )
    {
      setComponentTypeNameRef( method );
      return true;
    }
    else if ( null != ejbPostConstruct )
    {
      throw new ArezProcessorException( "@" + Constants.EJB_POST_CONSTRUCT_ANNOTATION_CLASSNAME + " annotation " +
                                        "not supported in components annotated with @ArezComponent, use the @" +
                                        Constants.POST_CONSTRUCT_ANNOTATION_CLASSNAME + " annotation instead.",
                                        method );
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
    final String[] annotationTypes =
      new String[]{ Constants.ACTION_ANNOTATION_CLASSNAME,
                    Constants.AUTORUN_ANNOTATION_CLASSNAME,
                    Constants.TRACK_ANNOTATION_CLASSNAME,
                    Constants.ON_DEPS_CHANGED_ANNOTATION_CLASSNAME,
                    Constants.OBSERVER_REF_ANNOTATION_CLASSNAME,
                    Constants.OBSERVABLE_ANNOTATION_CLASSNAME,
                    Constants.OBSERVABLE_REF_ANNOTATION_CLASSNAME,
                    Constants.COMPUTED_ANNOTATION_CLASSNAME,
                    Constants.COMPUTED_VALUE_REF_ANNOTATION_CLASSNAME,
                    Constants.MEMOIZE_ANNOTATION_CLASSNAME,
                    Constants.COMPONENT_REF_ANNOTATION_CLASSNAME,
                    Constants.COMPONENT_ID_ANNOTATION_CLASSNAME,
                    Constants.COMPONENT_NAME_REF_ANNOTATION_CLASSNAME,
                    Constants.COMPONENT_TYPE_NAME_REF_ANNOTATION_CLASSNAME,
                    Constants.CONTEXT_REF_ANNOTATION_CLASSNAME,
                    Constants.POST_CONSTRUCT_ANNOTATION_CLASSNAME,
                    Constants.PRE_DISPOSE_ANNOTATION_CLASSNAME,
                    Constants.POST_DISPOSE_ANNOTATION_CLASSNAME,
                    Constants.ON_ACTIVATE_ANNOTATION_CLASSNAME,
                    Constants.ON_DEACTIVATE_ANNOTATION_CLASSNAME,
                    Constants.ON_DISPOSE_ANNOTATION_CLASSNAME,
                    Constants.ON_STALE_ANNOTATION_CLASSNAME };
    for ( int i = 0; i < annotationTypes.length; i++ )
    {
      final String type1 = annotationTypes[ i ];
      final Object annotation1 = ProcessorUtil.findAnnotationByType( method, type1 );
      if ( null != annotation1 )
      {
        for ( int j = i + 1; j < annotationTypes.length; j++ )
        {
          final String type2 = annotationTypes[ j ];
          final Object annotation2 = ProcessorUtil.findAnnotationByType( method, type2 );
          if ( null != annotation2 )
          {
            final String message =
              "Method can not be annotated with both @" + ProcessorUtil.toSimpleName( type1 ) +
              " and @" + ProcessorUtil.toSimpleName( type2 );
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

  @Nonnull
  private String getNestedClassPrefix()
  {
    final StringBuilder name = new StringBuilder();
    TypeElement t = getElement();
    while ( NestingKind.TOP_LEVEL != t.getNestingKind() )
    {
      t = (TypeElement) t.getEnclosingElement();
      name.insert( 0, t.getSimpleName() + "_" );
    }
    return name.toString();
  }

  /**
   * Build the enhanced class for the component.
   */
  @Nonnull
  TypeSpec buildType( @Nonnull final Types typeUtils )
    throws ArezProcessorException
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( getArezClassName() ).
      superclass( TypeName.get( getElement().asType() ) ).
      addTypeVariables( ProcessorUtil.getTypeArgumentsAsNames( asDeclaredType() ) ).
      addModifiers( Modifier.FINAL );

    builder.addAnnotation( AnnotationSpec.builder( Generated.class ).
      addMember( "value", "$S", ArezProcessor.class.getName() ).
      build() );
    if ( !_roComputeds.isEmpty() )
    {
      builder.addAnnotation( AnnotationSpec.builder( SuppressWarnings.class ).
        addMember( "value", "$S", "unchecked" ).
        build() );
    }
    final boolean publicType =
      ProcessorUtil.getConstructors( getElement() ).
        stream().
        anyMatch( c -> c.getModifiers().contains( Modifier.PUBLIC ) ) &&
      getElement().getModifiers().contains( Modifier.PUBLIC );
    if ( publicType )
    {
      builder.addModifiers( Modifier.PUBLIC );
    }
    if ( null != _scopeAnnotation )
    {
      final DeclaredType annotationType = _scopeAnnotation.getAnnotationType();
      final TypeElement typeElement = (TypeElement) annotationType.asElement();
      builder.addAnnotation( ClassName.get( typeElement ) );
    }

    builder.addSuperinterface( GeneratorUtil.DISPOSABLE_CLASSNAME );
    builder.addSuperinterface( ParameterizedTypeName.get( GeneratorUtil.IDENTIFIABLE_CLASSNAME, getIdType().box() ) );

    buildFields( builder );

    buildConstructors( builder, typeUtils );

    builder.addMethod( buildContextRefMethod() );
    if ( null != _componentRef )
    {
      builder.addMethod( buildComponentRefMethod() );
    }
    if ( null == _componentId )
    {
      builder.addMethod( buildComponentIdMethod() );
    }
    builder.addMethod( buildArezIdMethod() );
    builder.addMethod( buildComponentNameMethod() );
    final MethodSpec method = buildComponentTypeNameMethod();
    if ( null != method )
    {
      builder.addMethod( method );
    }

    builder.addMethod( buildIsDisposed() );
    builder.addMethod( buildDispose() );

    _roObservables.forEach( e -> e.buildMethods( builder ) );
    _roAutoruns.forEach( e -> e.buildMethods( builder ) );
    _roActions.forEach( e -> e.buildMethods( builder ) );
    _roComputeds.forEach( e -> e.buildMethods( builder ) );
    _roMemoizes.forEach( e -> e.buildMethods( builder ) );
    _roTrackeds.forEach( e -> e.buildMethods( builder ) );

    builder.addMethod( buildHashcodeMethod() );
    builder.addMethod( buildEqualsMethod() );

    if ( _generateToString )
    {
      builder.addMethod( buildToStringMethod() );
    }

    return builder.build();
  }

  @Nonnull
  private MethodSpec buildToStringMethod()
    throws ArezProcessorException
  {
    assert _generateToString;

    final MethodSpec.Builder method =
      MethodSpec.methodBuilder( "toString" ).
        addModifiers( Modifier.PUBLIC, Modifier.FINAL ).
        addAnnotation( Override.class ).
        returns( TypeName.get( String.class ) );

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    codeBlock.beginControlFlow( "if ( $T.areNamesEnabled() )", GeneratorUtil.AREZ_CLASSNAME );
    codeBlock.addStatement( "return $S + $N() + $S", "ArezComponent[", getComponentNameMethodName(), "]" );
    codeBlock.nextControlFlow( "else" );
    codeBlock.addStatement( "return super.toString()" );
    codeBlock.endControlFlow();
    method.addCode( codeBlock.build() );
    return method.build();
  }

  @Nonnull
  private MethodSpec buildEqualsMethod()
    throws ArezProcessorException
  {
    final String idMethod = getIdMethodName();

    final MethodSpec.Builder method =
      MethodSpec.methodBuilder( "equals" ).
        addModifiers( Modifier.PUBLIC, Modifier.FINAL ).
        addAnnotation( Override.class ).
        addParameter( Object.class, "o", Modifier.FINAL ).
        returns( TypeName.BOOLEAN );

    final ClassName generatedClass = ClassName.get( getPackageName(), getArezClassName() );

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    codeBlock.beginControlFlow( "if ( this == o )" );
    codeBlock.addStatement( "return true" );
    codeBlock.nextControlFlow( "else if ( null == o || !(o instanceof $T) )", generatedClass );
    codeBlock.addStatement( "return false" );
    codeBlock.nextControlFlow( "else" );
    codeBlock.addStatement( "final $T that = ($T) o;", generatedClass, generatedClass );
    final TypeKind kind = null != _componentId ? _componentId.getReturnType().getKind() : TypeKind.LONG;
    if ( kind == TypeKind.DECLARED || kind == TypeKind.TYPEVAR )
    {
      codeBlock.addStatement( "return null != $N() && $N().equals( that.$N() )", idMethod, idMethod, idMethod );
    }
    else
    {
      codeBlock.addStatement( "return $N() == that.$N()", idMethod, idMethod );
    }
    codeBlock.endControlFlow();
    method.addCode( codeBlock.build() );
    return method.build();
  }

  @Nonnull
  private MethodSpec buildHashcodeMethod()
    throws ArezProcessorException
  {
    final String idMethod = getIdMethodName();

    final MethodSpec.Builder method =
      MethodSpec.methodBuilder( "hashCode" ).
        addModifiers( Modifier.PUBLIC, Modifier.FINAL ).
        addAnnotation( Override.class ).
        returns( TypeName.INT );
    final TypeKind kind = null != _componentId ? _componentId.getReturnType().getKind() : TypeKind.LONG;
    if ( kind == TypeKind.DECLARED || kind == TypeKind.TYPEVAR )
    {
      method.addStatement( "return null != $N() ? $N().hashCode() : $T.identityHashCode( this )",
                           idMethod,
                           idMethod,
                           System.class );
    }
    else if ( kind == TypeKind.BYTE )
    {
      method.addStatement( "return $T.hashCode( $N() )", Byte.class, idMethod );
    }
    else if ( kind == TypeKind.CHAR )
    {
      method.addStatement( "return $T.hashCode( $N() )", Character.class, idMethod );
    }
    else if ( kind == TypeKind.SHORT )
    {
      method.addStatement( "return $T.hashCode( $N() )", Short.class, idMethod );
    }
    else if ( kind == TypeKind.INT )
    {
      method.addStatement( "return $T.hashCode( $N() )", Integer.class, idMethod );
    }
    else if ( kind == TypeKind.LONG )
    {
      method.addStatement( "return $T.hashCode( $N() )", Long.class, idMethod );
    }
    else if ( kind == TypeKind.FLOAT )
    {
      method.addStatement( "return $T.hashCode( $N() )", Float.class, idMethod );
    }
    else if ( kind == TypeKind.DOUBLE )
    {
      method.addStatement( "return $T.hashCode( $N() )", Double.class, idMethod );
    }
    else
    {
      // So very unlikely but will cover it for completeness
      assert kind == TypeKind.BOOLEAN;
      method.addStatement( "return $T.hashCode( $N() )", Boolean.class, idMethod );
    }
    return method.build();
  }

  @Nonnull
  String getComponentNameMethodName()
  {
    return null == _componentName ? GeneratorUtil.NAME_METHOD_NAME : _componentName.getSimpleName().toString();
  }

  @Nonnull
  private MethodSpec buildContextRefMethod()
    throws ArezProcessorException
  {
    final MethodSpec.Builder method = MethodSpec.methodBuilder( getContextMethodName() ).
      addModifiers( Modifier.FINAL ).
      returns( GeneratorUtil.AREZ_CONTEXT_CLASSNAME ).
      addStatement( "return $T.areZonesEnabled() ? this.$N : $T.context()",
                    GeneratorUtil.AREZ_CLASSNAME,
                    GeneratorUtil.CONTEXT_FIELD_NAME,
                    GeneratorUtil.AREZ_CLASSNAME );
    if ( null != _contextRef )
    {
      method.addAnnotation( Override.class );
      ProcessorUtil.copyDocumentedAnnotations( _contextRef, method );
      ProcessorUtil.copyAccessModifiers( _contextRef, method );
    }
    return method.build();
  }

  @Nonnull
  String getContextMethodName()
  {
    return null != _contextRef ? _contextRef.getSimpleName().toString() : GeneratorUtil.CONTEXT_FIELD_NAME;
  }

  @Nonnull
  private MethodSpec buildComponentRefMethod()
    throws ArezProcessorException
  {
    assert null != _componentRef;

    final MethodSpec.Builder method = MethodSpec.methodBuilder( _componentRef.getSimpleName().toString() ).
      addModifiers( Modifier.FINAL ).
      returns( GeneratorUtil.COMPONENT_CLASSNAME ).
      addStatement( "$T.invariant( () -> $T.areNativeComponentsEnabled(), () -> \"Invoked @ComponentRef method '$N' " +
                    "but Arez.areNativeComponentsEnabled() returned false.\" )",
                    GeneratorUtil.GUARDS_CLASSNAME,
                    GeneratorUtil.AREZ_CLASSNAME,
                    _componentRef.getSimpleName().toString() ).
      addStatement( "return this.$N", GeneratorUtil.COMPONENT_FIELD_NAME );
    ProcessorUtil.copyDocumentedAnnotations( _componentRef, method );
    ProcessorUtil.copyAccessModifiers( _componentRef, method );
    return method.build();
  }

  @Nonnull
  private MethodSpec buildArezIdMethod()
    throws ArezProcessorException
  {
    return MethodSpec.methodBuilder( "getArezId" ).
      addAnnotation( Override.class ).
      addAnnotation( GeneratorUtil.NONNULL_CLASSNAME ).
      addModifiers( Modifier.PUBLIC ).
      addModifiers( Modifier.FINAL ).
      returns( getIdType().box() ).
      addStatement( "return $N()", getIdMethodName() ).build();
  }

  @Nonnull
  private MethodSpec buildComponentIdMethod()
    throws ArezProcessorException
  {
    assert null == _componentId;

    return MethodSpec.methodBuilder( GeneratorUtil.ID_FIELD_NAME ).
      addModifiers( Modifier.FINAL ).
      returns( TypeName.LONG ).
      addStatement( "return this.$N", GeneratorUtil.ID_FIELD_NAME ).build();
  }

  /**
   * Generate the getter for component name.
   */
  @Nonnull
  private MethodSpec buildComponentNameMethod()
    throws ArezProcessorException
  {
    final MethodSpec.Builder builder;
    if ( null == _componentName )
    {
      builder = MethodSpec.methodBuilder( GeneratorUtil.NAME_METHOD_NAME );
    }
    else
    {
      builder = MethodSpec.methodBuilder( _componentName.getSimpleName().toString() );
      ProcessorUtil.copyAccessModifiers( _componentName, builder );
      builder.addModifiers( Modifier.FINAL );
    }

    builder.returns( TypeName.get( String.class ) );
    if ( _nameIncludesId )
    {
      builder.addStatement( "return $S + $N()", _type.isEmpty() ? "" : _type + ".", getIdMethodName() );
    }
    else
    {
      builder.addStatement( "return $S", _type );
    }
    return builder.build();
  }

  @Nullable
  private MethodSpec buildComponentTypeNameMethod()
    throws ArezProcessorException
  {
    if ( null == _componentTypeName )
    {
      return null;
    }

    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( _componentTypeName.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( _componentTypeName, builder );
    builder.addModifiers( Modifier.FINAL );
    builder.addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );

    builder.returns( TypeName.get( String.class ) );
    builder.addStatement( "return $S", _type );
    return builder.build();
  }

  /**
   * Generate the dispose method.
   */
  @Nonnull
  private MethodSpec buildDispose()
    throws ArezProcessorException
  {
    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( "dispose" ).
        addModifiers( Modifier.PUBLIC ).
        addAnnotation( Override.class );

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    codeBlock.beginControlFlow( "if ( !isDisposed() )" );
    codeBlock.addStatement( "this.$N = true", GeneratorUtil.DISPOSED_FIELD_NAME );
    final CodeBlock.Builder nativeComponentBlock = CodeBlock.builder();
    nativeComponentBlock.beginControlFlow( "if ( $T.areNativeComponentsEnabled() )", GeneratorUtil.AREZ_CLASSNAME );
    nativeComponentBlock.addStatement( "this.$N.dispose()", GeneratorUtil.COMPONENT_FIELD_NAME );
    nativeComponentBlock.nextControlFlow( "else" );

    final CodeBlock.Builder actionBlock = CodeBlock.builder();
    actionBlock.beginControlFlow( "$N().safeAction( $T.areNamesEnabled() ? $N() + \".dispose\" : null, () -> {",
                                  getContextMethodName(),
                                  GeneratorUtil.AREZ_CLASSNAME,
                                  getComponentNameMethodName() );

    if ( null != _preDispose )
    {
      actionBlock.addStatement( "super.$N()", _preDispose.getSimpleName() );
    }
    actionBlock.addStatement( "this.$N.dispose()", GeneratorUtil.DISPOSED_OBSERVABLE_FIELD_NAME );
    _roAutoruns.forEach( autorun -> autorun.buildDisposer( actionBlock ) );
    _roTrackeds.forEach( tracked -> tracked.buildDisposer( actionBlock ) );
    _roComputeds.forEach( computed -> computed.buildDisposer( actionBlock ) );
    _roMemoizes.forEach( computed -> computed.buildDisposer( actionBlock ) );
    _roObservables.forEach( observable -> observable.buildDisposer( actionBlock ) );
    if ( null != _postDispose )
    {
      actionBlock.addStatement( "super.$N()", _postDispose.getSimpleName() );
    }
    actionBlock.endControlFlow( "} )" );
    nativeComponentBlock.add( actionBlock.build() );
    nativeComponentBlock.endControlFlow();
    codeBlock.add( nativeComponentBlock.build() );
    codeBlock.endControlFlow();

    builder.addCode( codeBlock.build() );

    return builder.build();
  }

  /**
   * Generate the isDisposed method.
   */
  @Nonnull
  private MethodSpec buildIsDisposed()
    throws ArezProcessorException
  {
    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( "isDisposed" ).
        addModifiers( Modifier.PUBLIC ).
        addAnnotation( Override.class ).
        returns( TypeName.BOOLEAN );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $N().isTransactionActive() && !this.$N.isDisposed() ) ",
                            getContextMethodName(),
                            GeneratorUtil.DISPOSED_OBSERVABLE_FIELD_NAME );
    block.addStatement( "this.$N.reportObserved()", GeneratorUtil.DISPOSED_OBSERVABLE_FIELD_NAME );
    block.addStatement( "return this.$N", GeneratorUtil.DISPOSED_FIELD_NAME );
    block.nextControlFlow( "else" );
    block.addStatement( "return this.$N", GeneratorUtil.DISPOSED_FIELD_NAME );
    block.endControlFlow();
    builder.addCode( block.build() );

    return builder.build();
  }

  /**
   * Build the fields required to make class Observable. This involves;
   * <ul>
   * <li>the context field if there is any @Action methods.</li>
   * <li>the observable object for every @Observable.</li>
   * <li>the ComputedValue object for every @Computed method.</li>
   * </ul>
   */
  private void buildFields( @Nonnull final TypeSpec.Builder builder )
  {
    // If we don't have a method for object id but we need one then synthesize it
    if ( null == _componentId )
    {
      final FieldSpec.Builder nextIdField =
        FieldSpec.builder( TypeName.LONG,
                           GeneratorUtil.NEXT_ID_FIELD_NAME,
                           Modifier.VOLATILE,
                           Modifier.STATIC,
                           Modifier.PRIVATE );
      builder.addField( nextIdField.build() );

      final FieldSpec.Builder idField =
        FieldSpec.builder( TypeName.LONG, GeneratorUtil.ID_FIELD_NAME, Modifier.FINAL, Modifier.PRIVATE );
      builder.addField( idField.build() );
    }

    final FieldSpec.Builder disposableField =
      FieldSpec.builder( TypeName.BOOLEAN, GeneratorUtil.DISPOSED_FIELD_NAME, Modifier.PRIVATE );
    builder.addField( disposableField.build() );

    // Create the field that contains the context
    {
      final FieldSpec.Builder field =
        FieldSpec.builder( GeneratorUtil.AREZ_CONTEXT_CLASSNAME,
                           GeneratorUtil.CONTEXT_FIELD_NAME,
                           Modifier.FINAL,
                           Modifier.PRIVATE ).
          addAnnotation( GeneratorUtil.NULLABLE_CLASSNAME );
      builder.addField( field.build() );
    }

    //Create the field that contains the component
    {
      final FieldSpec.Builder field =
        FieldSpec.builder( GeneratorUtil.COMPONENT_CLASSNAME,
                           GeneratorUtil.COMPONENT_FIELD_NAME,
                           Modifier.FINAL,
                           Modifier.PRIVATE );
      builder.addField( field.build() );

    }
    {
      final ParameterizedTypeName typeName =
        ParameterizedTypeName.get( GeneratorUtil.OBSERVABLE_CLASSNAME, TypeName.BOOLEAN.box() );
      final FieldSpec.Builder field =
        FieldSpec.builder( typeName,
                           GeneratorUtil.DISPOSED_OBSERVABLE_FIELD_NAME,
                           Modifier.FINAL,
                           Modifier.PRIVATE );
      builder.addField( field.build() );

    }
    _roObservables.forEach( observable -> observable.buildFields( builder ) );
    _roComputeds.forEach( computed -> computed.buildFields( builder ) );
    _roMemoizes.forEach( e -> e.buildFields( builder ) );
    _roAutoruns.forEach( autorun -> autorun.buildFields( builder ) );
    _roTrackeds.forEach( tracked -> tracked.buildFields( builder ) );
  }

  /**
   * Build all constructors as they appear on the ArezComponent class.
   * Arez Observable fields are populated as required and parameters are passed up to superclass.
   */
  private void buildConstructors( @Nonnull final TypeSpec.Builder builder,
                                  @Nonnull final Types typeUtils )
  {
    final boolean requiresDeprecatedSuppress = hasDeprecatedElements();
    for ( final ExecutableElement constructor : ProcessorUtil.getConstructors( getElement() ) )
    {
      final ExecutableType methodType =
        (ExecutableType) typeUtils.asMemberOf( (DeclaredType) _element.asType(), constructor );
      builder.addMethod( buildConstructor( constructor, methodType, requiresDeprecatedSuppress ) );
    }
  }

  /**
   * Build a constructor based on the supplied constructor
   */
  @Nonnull
  private MethodSpec buildConstructor( @Nonnull final ExecutableElement constructor,
                                       @Nonnull final ExecutableType constructorType,
                                       final boolean requiresDeprecatedSuppress )
  {
    final MethodSpec.Builder builder = MethodSpec.constructorBuilder();
    ProcessorUtil.copyAccessModifiers( constructor, builder );
    ProcessorUtil.copyExceptions( constructorType, builder );
    ProcessorUtil.copyTypeParameters( constructorType, builder );

    if ( requiresDeprecatedSuppress )
    {
      builder.addAnnotation( AnnotationSpec.builder( SuppressWarnings.class )
                               .addMember( "value", "$S", "deprecation" )
                               .build() );
    }

    if ( _inject )
    {
      builder.addAnnotation( GeneratorUtil.INJECT_CLASSNAME );
    }

    final StringBuilder superCall = new StringBuilder();
    superCall.append( "super(" );
    final ArrayList<String> parameterNames = new ArrayList<>();

    boolean firstParam = true;
    for ( final VariableElement element : constructor.getParameters() )
    {
      final ParameterSpec.Builder param =
        ParameterSpec.builder( TypeName.get( element.asType() ), element.getSimpleName().toString(), Modifier.FINAL );
      ProcessorUtil.copyDocumentedAnnotations( element, param );
      builder.addParameter( param.build() );
      parameterNames.add( element.getSimpleName().toString() );
      if ( !firstParam )
      {
        superCall.append( "," );
      }
      firstParam = false;
      superCall.append( "$N" );
    }

    superCall.append( ")" );
    builder.addStatement( superCall.toString(), parameterNames.toArray() );

    builder.addStatement( "this.$N = $T.areZonesEnabled() ? $T.context() : null",
                          GeneratorUtil.CONTEXT_FIELD_NAME,
                          GeneratorUtil.AREZ_CLASSNAME,
                          GeneratorUtil.AREZ_CLASSNAME );

    // Synthesize Id if required
    if ( null == _componentId )
    {
      builder.addStatement( "this.$N = $N++", GeneratorUtil.ID_FIELD_NAME, GeneratorUtil.NEXT_ID_FIELD_NAME );
    }

    // Create component representation if required
    {
      final StringBuilder sb = new StringBuilder();
      final ArrayList<Object> params = new ArrayList<>();
      sb.append( "this.$N = $T.areNativeComponentsEnabled() ? $N().createComponent( $S, $N(), $N(), " );
      params.add( GeneratorUtil.COMPONENT_FIELD_NAME );
      params.add( GeneratorUtil.AREZ_CLASSNAME );
      params.add( getContextMethodName() );
      params.add( _type );
      params.add( getIdMethodName() );
      params.add( getComponentNameMethodName() );
      if ( null != _preDispose )
      {
        sb.append( "() -> super.$N(), " );
        params.add( _preDispose.getSimpleName().toString() );
      }
      else
      {
        sb.append( "null, " );
      }
      if ( null != _postDispose )
      {
        sb.append( "() -> super.$N() " );
        params.add( _postDispose.getSimpleName().toString() );
      }
      else
      {
        sb.append( "null " );
      }
      sb.append( ") : null" );
      builder.addStatement( sb.toString(), params.toArray() );
    }
    {
      builder.addStatement( "this.$N = $N().createObservable( " +
                            "$T.areNativeComponentsEnabled() ? this.$N : null, " +
                            "$T.areNamesEnabled() ? $N() + $S : null, " +
                            "$T.arePropertyIntrospectorsEnabled() ? () -> this.$N : null, null )",
                            GeneratorUtil.DISPOSED_OBSERVABLE_FIELD_NAME,
                            getContextMethodName(),
                            GeneratorUtil.AREZ_CLASSNAME,
                            GeneratorUtil.COMPONENT_FIELD_NAME,
                            GeneratorUtil.AREZ_CLASSNAME,
                            getComponentNameMethodName(),
                            ".isDisposed",
                            GeneratorUtil.AREZ_CLASSNAME,
                            GeneratorUtil.DISPOSED_FIELD_NAME );
    }

    _roObservables.forEach( observable -> observable.buildInitializer( builder ) );
    _roComputeds.forEach( computed -> computed.buildInitializer( builder ) );
    _roMemoizes.forEach( e -> e.buildInitializer( builder ) );
    _roAutoruns.forEach( autorun -> autorun.buildInitializer( builder ) );
    _roTrackeds.forEach( tracked -> tracked.buildInitializer( builder ) );

    final ExecutableElement postConstruct = getPostConstruct();
    if ( null != postConstruct )
    {
      builder.addStatement( "super.$N()", postConstruct.getSimpleName().toString() );
    }

    final CodeBlock.Builder componentEnabledBlock = CodeBlock.builder();
    componentEnabledBlock.beginControlFlow( "if ( $T.areNativeComponentsEnabled() )",
                                            GeneratorUtil.AREZ_CLASSNAME );
    componentEnabledBlock.addStatement( "this.$N.complete()", GeneratorUtil.COMPONENT_FIELD_NAME );
    componentEnabledBlock.endControlFlow();
    builder.addCode( componentEnabledBlock.build() );

    if ( !_deferSchedule && !_roAutoruns.isEmpty() )
    {
      builder.addStatement( "$N().triggerScheduler()", getContextMethodName() );
    }

    return builder.build();
  }

  boolean shouldGenerateComponentDaggerModule()
  {
    return _dagger;
  }

  @Nonnull
  TypeSpec buildComponentDaggerModule()
    throws ArezProcessorException
  {
    assert shouldGenerateComponentDaggerModule();

    final TypeSpec.Builder builder = TypeSpec.interfaceBuilder( getComponentDaggerModuleName() ).
      addTypeVariables( ProcessorUtil.getTypeArgumentsAsNames( asDeclaredType() ) );

    builder.addAnnotation( AnnotationSpec.builder( Generated.class ).
      addMember( "value", "$S", ArezProcessor.class.getName() ).
      build() );
    builder.addAnnotation( GeneratorUtil.DAGGER_MODULE_CLASSNAME );
    builder.addModifiers( Modifier.PUBLIC );

    final MethodSpec.Builder method = MethodSpec.methodBuilder( "provideComponent" ).
      addAnnotation( GeneratorUtil.NONNULL_CLASSNAME ).
      addAnnotation( GeneratorUtil.DAGGER_PROVIDES_CLASSNAME ).
      addModifiers( Modifier.STATIC, Modifier.PUBLIC ).
      addParameter( ClassName.get( getPackageName(), getArezClassName() ), "component", Modifier.FINAL ).
      addStatement( "return component" ).
      returns( ClassName.get( getElement() ) );
    if ( null != _scopeAnnotation )
    {
      final DeclaredType annotationType = _scopeAnnotation.getAnnotationType();
      final TypeElement typeElement = (TypeElement) annotationType.asElement();
      method.addAnnotation( ClassName.get( typeElement ) );
    }

    builder.addMethod( method.build() );
    return builder.build();
  }

  boolean hasRepository()
  {
    return null != _repositoryName;
  }

  @SuppressWarnings( "ConstantConditions" )
  void configureRepository( @Nonnull final String name,
                            @Nonnull final List<TypeElement> extensions,
                            @Nonnull final String repositoryInjectConfig,
                            @Nonnull final String repositoryDaggerConfig )
  {
    assert null != name;
    assert null != extensions;
    _repositoryInjectConfig = repositoryInjectConfig;
    _repositoryDaggerConfig = repositoryDaggerConfig;
    if ( ProcessorUtil.isSentinelName( name ) )
    {
      _repositoryName = _type + "Repository";
    }
    else
    {
      if ( !ProcessorUtil.isJavaIdentifier( name ) )
      {
        throw new ArezProcessorException( "Class annotated with @Repository specified an invalid name " + name,
                                          _element );
      }
      _repositoryName = name;
    }
    for ( final TypeElement extension : extensions )
    {
      if ( ElementKind.INTERFACE != extension.getKind() )
      {
        throw new ArezProcessorException( "Class annotated with @Repository defined an extension that is " +
                                          "not an interface. Extension: " + extension.getQualifiedName(),
                                          getElement() );
      }

      for ( final Element enclosedElement : extension.getEnclosedElements() )
      {
        if ( ElementKind.METHOD == enclosedElement.getKind() )
        {
          final ExecutableElement method = (ExecutableElement) enclosedElement;
          if ( !method.isDefault() &&
               !( method.getSimpleName().toString().equals( "self" ) && 0 == method.getParameters().size() ) )
          {
            throw new ArezProcessorException( "Class annotated with @Repository defined an extension that has " +
                                              "a non default method. Extension: " + extension.getQualifiedName() +
                                              " Method: " + method, getElement() );
          }
        }
      }
    }
    _repositoryExtensions = extensions;
  }

  /**
   * Build the enhanced class for the component.
   */
  @Nonnull
  TypeSpec buildRepository( @Nonnull final Types typeUtils )
    throws ArezProcessorException
  {
    assert null != _repositoryExtensions;
    final TypeElement element = getElement();

    final ClassName arezType = ClassName.get( getPackageName(), getArezClassName() );

    final TypeSpec.Builder builder = TypeSpec.classBuilder( getRepositoryName() ).
      addTypeVariables( ProcessorUtil.getTypeArgumentsAsNames( asDeclaredType() ) );

    builder.addAnnotation( AnnotationSpec.builder( Generated.class ).
      addMember( "value", "$S", ArezProcessor.class.getName() ).
      build() );
    final AnnotationSpec.Builder arezComponent =
      AnnotationSpec.builder( ClassName.bestGuess( Constants.COMPONENT_ANNOTATION_CLASSNAME ) ).
        addMember( "nameIncludesId", "false" );
    if ( !"AUTODETECT".equals( _repositoryInjectConfig ) )
    {
      arezComponent.addMember( "inject", "$T.$N", GeneratorUtil.INJECTIBLE_CLASSNAME, _repositoryInjectConfig );
    }
    if ( !"AUTODETECT".equals( _repositoryDaggerConfig ) )
    {
      arezComponent.addMember( "dagger", "$T.$N", GeneratorUtil.INJECTIBLE_CLASSNAME, _repositoryDaggerConfig );
    }
    builder.addAnnotation( arezComponent.build() );
    if ( "ENABLE".equals( _repositoryInjectConfig ) ||
         ( "AUTODETECT".equals( _repositoryInjectConfig ) && _injectClassesPresent ) )
    {
      builder.addAnnotation( GeneratorUtil.SINGLETON_CLASSNAME );
    }

    builder.superclass( ParameterizedTypeName.get( GeneratorUtil.ABSTRACT_REPOSITORY_CLASSNAME,
                                                   getIdType().box(),
                                                   ClassName.get( getElement() ),
                                                   ClassName.get( getPackageName(), getRepositoryName() ) ) );

    _repositoryExtensions.forEach( e -> builder.addSuperinterface( TypeName.get( e.asType() ) ) );

    ProcessorUtil.copyAccessModifiers( element, builder );

    builder.addModifiers( Modifier.ABSTRACT );

    //Add the default access, no-args constructor
    builder.addMethod( MethodSpec.constructorBuilder().build() );

    // Add the factory method
    builder.addMethod( buildFactoryMethod() );

    for ( final ExecutableElement constructor : ProcessorUtil.getConstructors( getElement() ) )
    {
      final ExecutableType methodType =
        (ExecutableType) typeUtils.asMemberOf( (DeclaredType) _element.asType(), constructor );
      builder.addMethod( buildRepositoryCreate( constructor, methodType, arezType ) );
    }

    if ( null != _componentId )
    {
      builder.addMethod( buildFindByIdMethod() );
      builder.addMethod( buildGetByIdMethod() );
    }

    return builder.build();
  }

  @Nonnull
  private String getArezClassName()
  {
    return getNestedClassPrefix() + "Arez_" + getElement().getSimpleName();
  }

  @Nonnull
  private String getComponentDaggerModuleName()
  {
    return getNestedClassPrefix() + getElement().getSimpleName() + "DaggerModule";
  }

  @Nonnull
  private String getArezRepositoryName()
  {
    return "Arez_" + getNestedClassPrefix() + getElement().getSimpleName() + "Repository";
  }

  @Nonnull
  private String getRepositoryName()
  {
    return getNestedClassPrefix() + getElement().getSimpleName() + "Repository";
  }

  @Nonnull
  private MethodSpec buildFindByIdMethod()
  {
    assert null != _componentId;

    return MethodSpec.methodBuilder( "findBy" + getIdName() ).
      addModifiers( Modifier.PUBLIC ).
      addParameter( ParameterSpec.builder( getIdType(), "id", Modifier.FINAL ).build() ).
      addAnnotation( GeneratorUtil.NULLABLE_CLASSNAME ).
      returns( TypeName.get( getElement().asType() ) ).
      addStatement( "return findByArezId( id )" ).build();
  }

  @Nonnull
  private MethodSpec buildGetByIdMethod()
  {
    final TypeName entityType = TypeName.get( getElement().asType() );
    return MethodSpec.methodBuilder( "getBy" + getIdName() ).
      addModifiers( Modifier.PUBLIC, Modifier.FINAL ).
      addAnnotation( GeneratorUtil.NONNULL_CLASSNAME ).
      addParameter( ParameterSpec.builder( getIdType(), "id", Modifier.FINAL ).build() ).
      returns( entityType ).
      addStatement( "return getByArezId( id )" ).
      build();
  }

  @Nonnull
  private MethodSpec buildFactoryMethod()
  {
    return MethodSpec.methodBuilder( "newRepository" ).
      addModifiers( Modifier.PUBLIC, Modifier.STATIC ).
      addAnnotation( GeneratorUtil.NONNULL_CLASSNAME ).
      returns( ClassName.get( getPackageName(), getRepositoryName() ) ).
      addStatement( "return new $T()", ClassName.get( getPackageName(), getArezRepositoryName() ) ).build();
  }

  @Nonnull
  private MethodSpec buildRepositoryCreate( @Nonnull final ExecutableElement constructor,
                                            @Nonnull final ExecutableType methodType,
                                            @Nonnull final ClassName arezType )
  {
    final String suffix = constructor.getParameters().stream().
      map( p -> p.getSimpleName().toString() ).collect( Collectors.joining( "_" ) );
    final String actionName = "create" + ( suffix.isEmpty() ? "" : "_" + suffix );
    final AnnotationSpec annotationSpec =
      AnnotationSpec.builder( ClassName.bestGuess( Constants.ACTION_ANNOTATION_CLASSNAME ) ).
        addMember( "name", "$S", actionName ).build();
    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( "create" ).
        addAnnotation( annotationSpec ).
        addAnnotation( GeneratorUtil.NONNULL_CLASSNAME ).
        returns( TypeName.get( asDeclaredType() ) );

    ProcessorUtil.copyAccessModifiers( constructor, builder );
    ProcessorUtil.copyExceptions( methodType, builder );
    ProcessorUtil.copyTypeParameters( methodType, builder );

    final StringBuilder newCall = new StringBuilder();
    newCall.append( "final $T entity = new $T(" );
    final ArrayList<Object> parameters = new ArrayList<>();
    parameters.add( arezType );
    parameters.add( arezType );

    boolean firstParam = true;
    for ( final VariableElement element : constructor.getParameters() )
    {
      final ParameterSpec.Builder param =
        ParameterSpec.builder( TypeName.get( element.asType() ), element.getSimpleName().toString(), Modifier.FINAL );
      ProcessorUtil.copyDocumentedAnnotations( element, param );
      builder.addParameter( param.build() );
      parameters.add( element.getSimpleName().toString() );
      if ( !firstParam )
      {
        newCall.append( "," );
      }
      firstParam = false;
      newCall.append( "$N" );
    }

    newCall.append( ")" );
    builder.addStatement( newCall.toString(), parameters.toArray() );
    builder.addStatement( "registerEntity( entity )" );
    builder.addStatement( "return entity" );
    return builder.build();
  }

  @Nonnull
  String getPackageName()
  {
    return _packageElement.getQualifiedName().toString();
  }

  @Nonnull
  private String getIdMethodName()
  {
    /*
     * Note that it is a deliberate choice to not use getArezId() as that will box Id which for the
     * "normal" case involves converting a long to a Long and it was decided that the slight increase in
     * code size was worth the slightly reduced memory pressure.
     */
    return null != _componentId ? _componentId.getSimpleName().toString() : GeneratorUtil.ID_FIELD_NAME;
  }

  @Nonnull
  private String getIdName()
  {
    if ( null != _componentId )
    {
      final String name = ProcessorUtil.deriveName( _componentId, GETTER_PATTERN, ProcessorUtil.SENTINEL_NAME );
      if ( null != name )
      {
        return Character.toUpperCase( name.charAt( 0 ) ) + ( name.length() > 1 ? name.substring( 1 ) : "" );
      }
    }
    return "Id";
  }

  @Nonnull
  private TypeName getIdType()
  {
    return null == _componentIdMethodType ?
           TypeName.LONG :
           TypeName.get( _componentIdMethodType.getReturnType() );
  }

  @SuppressWarnings( "unchecked" )
  private <T> T getAnnotationParameter( @Nonnull final AnnotationMirror annotation,
                                        @Nonnull final String parameterName )
  {
    return (T) ProcessorUtil.getAnnotationValue( _elements,
                                                 annotation,
                                                 parameterName ).getValue();
  }
}
