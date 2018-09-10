package arez.processor;

import com.google.auto.common.GeneratedAnnotationSpecs;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
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
import static arez.processor.ProcessorUtil.*;

/**
 * The class that represents the parsed state of ArezComponent annotated class.
 */
@SuppressWarnings( "Duplicates" )
final class ComponentDescriptor
{
  private static final Pattern OBSERVABLE_REF_PATTERN = Pattern.compile( "^get([A-Z].*)ObservableValue$" );
  private static final Pattern COMPUTED_VALUE_REF_PATTERN = Pattern.compile( "^get([A-Z].*)ComputedValue$" );
  private static final Pattern OBSERVER_REF_PATTERN = Pattern.compile( "^get([A-Z].*)Observer$" );
  private static final Pattern SETTER_PATTERN = Pattern.compile( "^set([A-Z].*)$" );
  private static final Pattern GETTER_PATTERN = Pattern.compile( "^get([A-Z].*)$" );
  private static final Pattern ID_GETTER_PATTERN = Pattern.compile( "^get([A-Z].*)Id$" );
  private static final Pattern RAW_ID_GETTER_PATTERN = Pattern.compile( "^(.*)Id$" );
  private static final Pattern ISSER_PATTERN = Pattern.compile( "^is([A-Z].*)$" );
  private static final List<String> OBJECT_METHODS =
    Arrays.asList( "hashCode", "equals", "clone", "toString", "finalize", "getClass", "wait", "notifyAll", "notify" );
  private static final List<String> AREZ_SPECIAL_METHODS =
    Arrays.asList( "observe", "dispose", "isDisposed", "getArezId" );
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
  private final SourceVersion _sourceVersion;
  @Nonnull
  private final Elements _elements;
  @Nonnull
  private final Types _typeUtils;
  @Nonnull
  private final String _type;
  private final boolean _nameIncludesId;
  private final boolean _allowEmpty;
  private final boolean _observable;
  private final boolean _disposeTrackable;
  private final boolean _disposeOnDeactivate;
  private final boolean _injectClassesPresent;
  private final boolean _inject;
  private final boolean _dagger;
  /**
   * Annotation that indicates whether equals/hashCode should be implemented. See arez.annotations.ArezComponent.requireEquals()
   */
  private final boolean _requireEquals;
  /**
   * Flag indicating whether generated component should implement arez.component.Verifiable.
   */
  private final boolean _verify;
  /**
   * Scope annotation that is declared on component and should be transferred to injection providers.
   */
  private final AnnotationMirror _scopeAnnotation;
  private final boolean _deferSchedule;
  private final boolean _generateToString;
  private boolean _idRequired;
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
  private ExecutableElement _componentTypeNameRef;
  @Nullable
  private ExecutableElement _componentNameRef;
  @Nullable
  private ExecutableElement _preDispose;
  @Nullable
  private ExecutableElement _postDispose;
  private final Map<String, CandidateMethod> _observerRefs = new LinkedHashMap<>();
  private final Map<String, ObservableDescriptor> _observables = new LinkedHashMap<>();
  private final Collection<ObservableDescriptor> _roObservables =
    Collections.unmodifiableCollection( _observables.values() );
  private final Map<String, ActionDescriptor> _actions = new LinkedHashMap<>();
  private final Collection<ActionDescriptor> _roActions =
    Collections.unmodifiableCollection( _actions.values() );
  private final Map<String, ComputedDescriptor> _computeds = new LinkedHashMap<>();
  private final Collection<ComputedDescriptor> _roComputeds =
    Collections.unmodifiableCollection( _computeds.values() );
  private final Map<String, MemoizeDescriptor> _memoizes = new LinkedHashMap<>();
  private final Collection<MemoizeDescriptor> _roMemoizes =
    Collections.unmodifiableCollection( _memoizes.values() );
  private final Map<String, ObservedDescriptor> _observeds = new LinkedHashMap<>();
  private final Collection<ObservedDescriptor> _roObserveds =
    Collections.unmodifiableCollection( _observeds.values() );
  private final Map<String, TrackedDescriptor> _trackeds = new LinkedHashMap<>();
  private final Collection<TrackedDescriptor> _roTrackeds =
    Collections.unmodifiableCollection( _trackeds.values() );
  private final Map<ExecutableElement, DependencyDescriptor> _dependencies = new LinkedHashMap<>();
  private final Collection<DependencyDescriptor> _roDependencies =
    Collections.unmodifiableCollection( _dependencies.values() );
  private final Map<VariableElement, CascadeDisposableDescriptor> _cascadeDisposes = new LinkedHashMap<>();
  private final Collection<CascadeDisposableDescriptor> _roCascadeDisposes =
    Collections.unmodifiableCollection( _cascadeDisposes.values() );
  private final Map<String, ReferenceDescriptor> _references = new LinkedHashMap<>();
  private final Collection<ReferenceDescriptor> _roReferences =
    Collections.unmodifiableCollection( _references.values() );
  private final Map<String, InverseDescriptor> _inverses = new LinkedHashMap<>();
  private final Collection<InverseDescriptor> _roInverses =
    Collections.unmodifiableCollection( _inverses.values() );

  ComponentDescriptor( @Nonnull final SourceVersion sourceVersion,
                       @Nonnull final Elements elements,
                       @Nonnull final Types typeUtils,
                       @Nonnull final String type,
                       final boolean nameIncludesId,
                       final boolean allowEmpty,
                       final boolean observable,
                       final boolean disposeTrackable,
                       final boolean disposeOnDeactivate,
                       final boolean injectClassesPresent,
                       final boolean inject,
                       final boolean dagger,
                       final boolean requireEquals,
                       final boolean verify,
                       @Nullable final AnnotationMirror scopeAnnotation,
                       final boolean deferSchedule,
                       final boolean generateToString,
                       @Nonnull final PackageElement packageElement,
                       @Nonnull final TypeElement element )
  {
    _sourceVersion = Objects.requireNonNull( sourceVersion );
    _elements = Objects.requireNonNull( elements );
    _typeUtils = Objects.requireNonNull( typeUtils );
    _type = Objects.requireNonNull( type );
    _nameIncludesId = nameIncludesId;
    _allowEmpty = allowEmpty;
    _observable = observable;
    _disposeTrackable = disposeTrackable;
    _disposeOnDeactivate = disposeOnDeactivate;
    _injectClassesPresent = injectClassesPresent;
    _inject = inject;
    _dagger = dagger;
    _requireEquals = requireEquals;
    _verify = verify;
    _scopeAnnotation = scopeAnnotation;
    _deferSchedule = deferSchedule;
    _generateToString = generateToString;
    _packageElement = Objects.requireNonNull( packageElement );
    _element = Objects.requireNonNull( element );
  }

  @Nonnull
  Types getTypeUtils()
  {
    return _typeUtils;
  }

  private boolean hasDeprecatedElements()
  {
    return isDeprecated( _postConstruct ) ||
           isDeprecated( _componentId ) ||
           isDeprecated( _componentRef ) ||
           isDeprecated( _contextRef ) ||
           isDeprecated( _componentTypeNameRef ) ||
           isDeprecated( _componentNameRef ) ||
           isDeprecated( _preDispose ) ||
           isDeprecated( _postDispose ) ||
           _roObservables.stream().anyMatch( e -> ( e.hasSetter() && isDeprecated( e.getSetter() ) ) ||
                                                  ( e.hasGetter() && isDeprecated( e.getGetter() ) ) ) ||
           _roComputeds.stream().anyMatch( e -> ( e.hasComputed() && isDeprecated( e.getComputed() ) ) ||
                                                isDeprecated( e.getOnActivate() ) ||
                                                isDeprecated( e.getOnDeactivate() ) ||
                                                isDeprecated( e.getOnStale() ) ) ||
           _observerRefs.values().stream().anyMatch( e -> isDeprecated( e.getMethod() ) ) ||
           _roDependencies.stream().anyMatch( e -> isDeprecated( e.getMethod() ) ) ||
           _roActions.stream().anyMatch( e -> isDeprecated( e.getAction() ) ) ||
           _roObserveds.stream().anyMatch( e -> isDeprecated( e.getObserved() ) ) ||
           _roMemoizes.stream().anyMatch( e -> isDeprecated( e.getMemoize() ) ) ||
           _roTrackeds.stream().anyMatch( e -> ( e.hasTrackedMethod() && isDeprecated( e.getTrackedMethod() ) ) ||
                                               ( e.hasOnDepsChangedMethod() &&
                                                 isDeprecated( e.getOnDepsChangedMethod() ) ) );

  }

  void setIdRequired( final boolean idRequired )
  {
    _idRequired = idRequired;
  }

  boolean shouldVerify()
  {
    return _verify;
  }

  boolean isDisposeTrackable()
  {
    return _disposeTrackable;
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
  String getType()
  {
    return _type;
  }

  @Nonnull
  private ReferenceDescriptor findOrCreateReference( @Nonnull final String name )
  {
    return _references.computeIfAbsent( name, n -> new ReferenceDescriptor( this, name ) );
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

  @Nonnull
  private ObservableDescriptor addObservable( @Nonnull final AnnotationMirror annotation,
                                              @Nonnull final ExecutableElement method,
                                              @Nonnull final ExecutableType methodType )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( getElement(), Constants.OBSERVABLE_ANNOTATION_CLASSNAME, method );

    final String declaredName = getAnnotationParameter( annotation, "name" );
    final boolean expectSetter = getAnnotationParameter( annotation, "expectSetter" );
    final boolean readOutsideTransaction = getAnnotationParameter( annotation, "readOutsideTransaction" );
    final boolean setterAlwaysMutates = getAnnotationParameter( annotation, "setterAlwaysMutates" );
    final Boolean requireInitializer = isInitializerRequired( method );

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
        throw new ArezProcessorException( "@Observable target should be a setter or getter", method );
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
        throw new ArezProcessorException( "@Observable target should be a setter or getter", method );
      }
      name = getPropertyAccessorName( method, declaredName );
    }
    // Override name if supplied by user
    if ( !ProcessorUtil.isSentinelName( declaredName ) )
    {
      name = declaredName;
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ArezProcessorException( "@Observable target specified an invalid name '" + name + "'. The " +
                                          "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ArezProcessorException( "@Observable target specified an invalid name '" + name + "'. The " +
                                          "name must not be a java keyword.", method );
      }
    }
    checkNameUnique( name, method, Constants.OBSERVABLE_ANNOTATION_CLASSNAME );

    if ( setter && !expectSetter )
    {
      throw new ArezProcessorException( "Method annotated with @Observable is a setter but defines " +
                                        "expectSetter = false for observable named " + name, method );
    }

    final ObservableDescriptor observable = findOrCreateObservable( name );
    if ( readOutsideTransaction )
    {
      observable.setReadOutsideTransaction( true );
    }
    if ( !setterAlwaysMutates )
    {
      observable.setSetterAlwaysMutates( false );
    }
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
    if ( null != requireInitializer )
    {
      if ( !method.getModifiers().contains( Modifier.ABSTRACT ) )
      {
        throw new ArezProcessorException( "@Observable target set initializer parameter to ENABLED but " +
                                          "method is not abstract.", method );
      }
      final Boolean existing = observable.getInitializer();
      if ( null == existing )
      {
        observable.setInitializer( requireInitializer );
      }
      else if ( existing != requireInitializer )
      {
        throw new ArezProcessorException( "@Observable target set initializer parameter to value that differs from " +
                                          "the paired observable method.", method );
      }
    }
    return observable;
  }

  private void addObservableValueRef( @Nonnull final AnnotationMirror annotation,
                                      @Nonnull final ExecutableElement method,
                                      @Nonnull final ExecutableType methodType )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( getElement(), Constants.OBSERVABLE_VALUE_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustBeAbstract( Constants.OBSERVABLE_VALUE_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotHaveAnyParameters( Constants.OBSERVABLE_VALUE_REF_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotThrowAnyExceptions( Constants.OBSERVABLE_VALUE_REF_ANNOTATION_CLASSNAME, method );

    final TypeMirror returnType = methodType.getReturnType();
    if ( TypeKind.DECLARED != returnType.getKind() ||
         !toRawType( returnType ).toString().equals( "arez.ObservableValue" ) )
    {
      throw new ArezProcessorException( "Method annotated with @ObservableValueRef must return an instance of " +
                                        "arez.ObservableValue", method );
    }

    final String declaredName = getAnnotationParameter( annotation, "name" );
    final String name;
    if ( ProcessorUtil.isSentinelName( declaredName ) )
    {
      name = ProcessorUtil.deriveName( method, OBSERVABLE_REF_PATTERN, declaredName );
      if ( null == name )
      {
        throw new ArezProcessorException( "Method annotated with @ObservableValueRef should specify name or be " +
                                          "named according to the convention get[Name]ObservableValue", method );
      }
    }
    else
    {
      name = declaredName;
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ArezProcessorException( "@ObservableValueRef target specified an invalid name '" + name + "'. The " +
                                          "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ArezProcessorException( "@ObservableValueRef target specified an invalid name '" + name + "'. The " +
                                          "name must not be a java keyword.", method );
      }
    }

    final ObservableDescriptor observable = findOrCreateObservable( name );

    if ( observable.hasRefMethod() )
    {
      throw new ArezProcessorException( "Method annotated with @ObservableValueRef defines duplicate ref " +
                                        "accessor for observable named " + name, method );
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
    MethodChecks.mustBeWrappable( getElement(), Constants.ACTION_ANNOTATION_CLASSNAME, method );

    final String name = deriveActionName( method, annotation );
    checkNameUnique( name, method, Constants.ACTION_ANNOTATION_CLASSNAME );
    final boolean mutation = getAnnotationParameter( annotation, "mutation" );
    final boolean requireNewTransaction = getAnnotationParameter( annotation, "requireNewTransaction" );
    final boolean reportParameters = getAnnotationParameter( annotation, "reportParameters" );
    final boolean verifyRequired = getAnnotationParameter( annotation, "verifyRequired" );
    final ActionDescriptor action =
      new ActionDescriptor( this,
                            name,
                            requireNewTransaction,
                            mutation,
                            verifyRequired,
                            reportParameters,
                            method,
                            methodType );
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
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ArezProcessorException( "@Action target specified an invalid name '" + name + "'. The " +
                                          "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ArezProcessorException( "@Action target specified an invalid name '" + name + "'. The " +
                                          "name must not be a java keyword.", method );
      }
      return name;
    }
  }

  private void addObserved( @Nonnull final AnnotationMirror annotation,
                            @Nonnull final ExecutableElement method,
                            @Nonnull final ExecutableType methodType )
    throws ArezProcessorException
  {
    MethodChecks.mustBeWrappable( getElement(), Constants.OBSERVED_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotHaveAnyParameters( Constants.OBSERVED_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotThrowAnyExceptions( Constants.OBSERVED_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotReturnAnyValue( Constants.OBSERVED_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotBePublic( Constants.OBSERVED_ANNOTATION_CLASSNAME, method );

    final String name = deriveObservedName( method, annotation );
    checkNameUnique( name, method, Constants.OBSERVED_ANNOTATION_CLASSNAME );
    final boolean mutation = getAnnotationParameter( annotation, "mutation" );
    final boolean observeLowerPriorityDependencies =
      getAnnotationParameter( annotation, "observeLowerPriorityDependencies" );
    final boolean nestedActionsAllowed = getAnnotationParameter( annotation, "nestedActionsAllowed" );
    final VariableElement priority = getAnnotationParameter( annotation, "priority" );
    final ObservedDescriptor observed =
      new ObservedDescriptor( this,
                              name,
                              mutation,
                              priority.getSimpleName().toString(),
                              observeLowerPriorityDependencies,
                              nestedActionsAllowed,
                              method,
                              methodType );
    _observeds.put( observed.getName(), observed );
  }

  @Nonnull
  private String deriveObservedName( @Nonnull final ExecutableElement method,
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
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ArezProcessorException( "@Observed target specified an invalid name '" + name + "'. The " +
                                          "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ArezProcessorException( "@Observed target specified an invalid name '" + name + "'. The " +
                                          "name must not be a java keyword.", method );
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
    final boolean observeLowerPriorityDependencies =
      getAnnotationParameter( annotation, "observeLowerPriorityDependencies" );
    final boolean nestedActionsAllowed = getAnnotationParameter( annotation, "nestedActionsAllowed" );
    final VariableElement priority = getAnnotationParameter( annotation, "priority" );
    final boolean reportParameters = getAnnotationParameter( annotation, "reportParameters" );
    final TrackedDescriptor tracked = findOrCreateTracked( name );
    tracked.setTrackedMethod( mutation,
                              priority.getSimpleName().toString(),
                              reportParameters,
                              observeLowerPriorityDependencies,
                              nestedActionsAllowed,
                              method,
                              methodType );
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
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ArezProcessorException( "@Track target specified an invalid name '" + name + "'. The " +
                                          "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ArezProcessorException( "@Track target specified an invalid name '" + name + "'. The " +
                                          "name must not be a java keyword.", method );
      }
      return name;
    }
  }

  private void addObserverRef( @Nonnull final AnnotationMirror annotation,
                               @Nonnull final ExecutableElement method,
                               @Nonnull final ExecutableType methodType )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( getElement(), Constants.OBSERVER_REF_ANNOTATION_CLASSNAME, method );
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
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ArezProcessorException( "@ObserverRef target specified an invalid name '" + name + "'. The " +
                                          "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ArezProcessorException( "@ObserverRef target specified an invalid name '" + name + "'. The " +
                                          "name must not be a java keyword.", method );
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
    final boolean keepAlive = getAnnotationParameter( annotation, "keepAlive" );
    final boolean observeLowerPriorityDependencies =
      getAnnotationParameter( annotation, "observeLowerPriorityDependencies" );
    final VariableElement priority = getAnnotationParameter( annotation, "priority" );
    final boolean arezOnlyDependencies = getAnnotationParameter( annotation, "arezOnlyDependencies" );
    findOrCreateComputed( name ).setComputed( method,
                                              computedType,
                                              keepAlive,
                                              priority.getSimpleName().toString(),
                                              observeLowerPriorityDependencies,
                                              arezOnlyDependencies );
  }

  private void addComputedValueRef( @Nonnull final AnnotationMirror annotation,
                                    @Nonnull final ExecutableElement method,
                                    @Nonnull final ExecutableType methodType )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( getElement(), Constants.COMPUTED_VALUE_REF_ANNOTATION_CLASSNAME, method );
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
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ArezProcessorException( "@ComputedValueRef target specified an invalid name '" + name + "'. The " +
                                          "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ArezProcessorException( "@ComputedValueRef target specified an invalid name '" + name + "'. The " +
                                          "name must not be a java keyword.", method );
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
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ArezProcessorException( "@Computed target specified an invalid name '" + name + "'. The " +
                                          "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ArezProcessorException( "@Computed target specified an invalid name '" + name + "'. The " +
                                          "name must not be a java keyword.", method );
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
    final boolean observeLowerPriorityDependencies =
      getAnnotationParameter( annotation, "observeLowerPriorityDependencies" );
    final VariableElement priorityElement = getAnnotationParameter( annotation, "priority" );
    final String priority = priorityElement.getSimpleName().toString();
    checkNameUnique( name, method, Constants.MEMOIZE_ANNOTATION_CLASSNAME );
    _memoizes.put( name,
                   new MemoizeDescriptor( this,
                                          name,
                                          priority,
                                          observeLowerPriorityDependencies,
                                          method,
                                          methodType ) );
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
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ArezProcessorException( "@Memoize target specified an invalid name '" + name + "'. The " +
                                          "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ArezProcessorException( "@Memoize target specified an invalid name '" + name + "'. The " +
                                          "name must not be a java keyword.", method );
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
    else if ( !SourceVersion.isIdentifier( value ) )
    {
      throw new ArezProcessorException( "@On" + type + " target specified an invalid name '" + value + "'. The " +
                                        "name must be a valid java identifier.", _element );
    }
    else if ( SourceVersion.isKeyword( value ) )
    {
      throw new ArezProcessorException( "@On" + type + " target specified an invalid name '" + value + "'. The " +
                                        "name must not be a java keyword.", _element );
    }
    else
    {
      return value;
    }
  }

  private void setContextRef( @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( getElement(), Constants.CONTEXT_REF_ANNOTATION_CLASSNAME, method );
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
    MethodChecks.mustBeOverridable( getElement(), Constants.COMPONENT_REF_ANNOTATION_CLASSNAME, method );
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

  boolean hasComponentIdMethod()
  {
    return null != _componentId;
  }

  private void setComponentId( @Nonnull final ExecutableElement componentId,
                               @Nonnull final ExecutableType componentIdMethodType )
    throws ArezProcessorException
  {
    MethodChecks.mustNotBeAbstract( Constants.COMPONENT_ID_ANNOTATION_CLASSNAME, componentId );
    MethodChecks.mustBeSubclassCallable( getElement(), Constants.COMPONENT_ID_ANNOTATION_CLASSNAME, componentId );
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
    MethodChecks.mustBeOverridable( getElement(),
                                    Constants.COMPONENT_TYPE_NAME_REF_ANNOTATION_CLASSNAME,
                                    componentTypeName );
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

    if ( null != _componentTypeNameRef )
    {
      throw new ArezProcessorException( "@ComponentTypeNameRef target duplicates existing method named " +
                                        _componentTypeNameRef.getSimpleName(), componentTypeName );
    }
    else
    {
      _componentTypeNameRef = Objects.requireNonNull( componentTypeName );
    }
  }

  private void setComponentNameRef( @Nonnull final ExecutableElement componentName )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( getElement(), Constants.COMPONENT_NAME_REF_ANNOTATION_CLASSNAME, componentName );
    MethodChecks.mustBeAbstract( Constants.COMPONENT_NAME_REF_ANNOTATION_CLASSNAME, componentName );
    MethodChecks.mustNotHaveAnyParameters( Constants.COMPONENT_NAME_REF_ANNOTATION_CLASSNAME, componentName );
    MethodChecks.mustReturnAValue( Constants.COMPONENT_NAME_REF_ANNOTATION_CLASSNAME, componentName );
    MethodChecks.mustNotThrowAnyExceptions( Constants.COMPONENT_NAME_REF_ANNOTATION_CLASSNAME, componentName );

    if ( null != _componentNameRef )
    {
      throw new ArezProcessorException( "@ComponentNameRef target duplicates existing method named " +
                                        _componentNameRef.getSimpleName(), componentName );
    }
    else
    {
      _componentNameRef = Objects.requireNonNull( componentName );
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
    MethodChecks.mustBeLifecycleHook( getElement(), Constants.POST_CONSTRUCT_ANNOTATION_CLASSNAME, postConstruct );

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
    MethodChecks.mustBeLifecycleHook( getElement(), Constants.PRE_DISPOSE_ANNOTATION_CLASSNAME, preDispose );

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
    MethodChecks.mustBeLifecycleHook( getElement(), Constants.POST_DISPOSE_ANNOTATION_CLASSNAME, postDispose );

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
    _roObservables.forEach( ObservableDescriptor::validate );
    _roComputeds.forEach( ComputedDescriptor::validate );
    _roDependencies.forEach( DependencyDescriptor::validate );
    _roReferences.forEach( ReferenceDescriptor::validate );
    _roInverses.forEach( InverseDescriptor::validate );

    final boolean hasReactiveElements =
      _roObservables.isEmpty() &&
      _roActions.isEmpty() &&
      _roComputeds.isEmpty() &&
      _roMemoizes.isEmpty() &&
      _roTrackeds.isEmpty() &&
      _roDependencies.isEmpty() &&
      _roCascadeDisposes.isEmpty() &&
      _roReferences.isEmpty() &&
      _roInverses.isEmpty() &&
      _roObserveds.isEmpty();

    if ( !_allowEmpty && hasReactiveElements )
    {
      throw new ArezProcessorException( "@ArezComponent target has no methods annotated with @Action, " +
                                        "@CascadeDispose, @Computed, @Memoize, @Observable, @Inverse, " +
                                        "@Reference, @Dependency, @Track or @Observed", _element );
    }
    else if ( _allowEmpty && !hasReactiveElements )
    {
      throw new ArezProcessorException( "@ArezComponent target has specified allowEmpty = true but has methods " +
                                        "annotated with @Action, @CascadeDispose, @Computed, @Memoize, @Observable, @Inverse, " +
                                        "@Reference, @Dependency, @Track or @Observed", _element );
    }

    if ( _deferSchedule && !requiresSchedule() )
    {
      throw new ArezProcessorException( "@ArezComponent target has specified the deferSchedule = true " +
                                        "annotation parameter but has no methods annotated with @Observed, " +
                                        "@Dependency or @Computed(keepAlive=true)", _element );
    }
  }

  private boolean requiresSchedule()
  {
    return !_roObserveds.isEmpty() ||
           !_roDependencies.isEmpty() ||
           _computeds.values().stream().anyMatch( ComputedDescriptor::isKeepAlive );
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
    final ObservedDescriptor observed = _observeds.get( name );
    if ( null != observed )
    {
      throw toException( name,
                         sourceAnnotationName,
                         sourceMethod,
                         Constants.OBSERVED_ANNOTATION_CLASSNAME,
                         observed.getObserved() );
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
    for ( final ExecutableElement method : methods )
    {
      final String methodName = method.getSimpleName().toString();
      if ( AREZ_SPECIAL_METHODS.contains( methodName ) && method.getParameters().isEmpty() )
      {
        throw new ArezProcessorException( "Method defined on a class annotated by @ArezComponent uses a name " +
                                          "reserved by Arez", method );
      }
      else if ( methodName.startsWith( GeneratorUtil.FIELD_PREFIX ) ||
                methodName.startsWith( GeneratorUtil.OBSERVABLE_DATA_FIELD_PREFIX ) ||
                methodName.startsWith( GeneratorUtil.REFERENCE_FIELD_PREFIX ) ||
                methodName.startsWith( GeneratorUtil.FRAMEWORK_PREFIX ) )
      {
        throw new ArezProcessorException( "Method defined on a class annotated by @ArezComponent uses a name " +
                                          "with a prefix reserved by Arez", method );
      }
    }
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

    linkDependencies( getters.values() );

    autodetectObservableInitializers();

    /*
     * ALl of the maps will have called remove() for all matching candidates.
     * Thus any left are the non-arez methods.
     */

    ensureNoAbstractMethods( getters.values() );
    ensureNoAbstractMethods( setters.values() );
    ensureNoAbstractMethods( trackeds.values() );
    ensureNoAbstractMethods( onDepsChangeds.values() );

    processCascadeDisposeFields();
  }

  private void processCascadeDisposeFields()
  {
    ProcessorUtil.getFieldElements( _element )
      .stream()
      .filter( f -> null != ProcessorUtil.findAnnotationByType( f, Constants.CASCADE_DISPOSE_ANNOTATION_CLASSNAME ) )
      .forEach( this::processCascadeDisposeField );
  }

  private void processCascadeDisposeField( @Nonnull final VariableElement f )
  {
    MethodChecks.mustBeSubclassCallable( _element, Constants.CASCADE_DISPOSE_ANNOTATION_CLASSNAME, f );
    mustBeCascadeDisposeTypeCompatible( f );
    _cascadeDisposes.put( f, new CascadeDisposableDescriptor( f ) );
  }

  private void mustBeCascadeDisposeTypeCompatible( final @Nonnull VariableElement f )
  {
    final TypeElement disposable = _elements.getTypeElement( Constants.DISPOSABLE_CLASSNAME );
    assert null != disposable;
    final TypeMirror typeMirror = f.asType();
    if ( !_typeUtils.isAssignable( typeMirror, disposable.asType() ) )
    {
      final TypeElement typeElement = (TypeElement) _typeUtils.asElement( typeMirror );
      final AnnotationMirror value =
        null != typeElement ?
        ProcessorUtil.findAnnotationByType( typeElement, Constants.COMPONENT_ANNOTATION_CLASSNAME ) :
        null;
      if ( null == value || !ProcessorUtil.isDisposableTrackableRequired( _elements, typeElement ) )
      {
        //The type of the field must implement {@link arez.Disposable} or must be annotated by {@link ArezComponent}
        throw new ArezProcessorException( "@CascadeDispose target must be assignable to " +
                                          Constants.DISPOSABLE_CLASSNAME + " or a type annotated with @ArezComponent",
                                          f );
      }
    }
  }

  private void autodetectObservableInitializers()
  {
    for ( final ObservableDescriptor observable : getObservables() )
    {
      if ( null == observable.getInitializer() && observable.hasGetter() )
      {
        if ( observable.hasSetter() )
        {
          final boolean initializer =
            autodetectInitializer( observable.getGetter() ) && autodetectInitializer( observable.getSetter() );
          observable.setInitializer( initializer );
        }
        else
        {
          final boolean initializer = autodetectInitializer( observable.getGetter() );
          observable.setInitializer( initializer );
        }
      }
    }
  }

  private void linkDependencies( @Nonnull final Collection<CandidateMethod> candidates )
  {
    _roObservables
      .stream()
      .filter( ObservableDescriptor::hasGetter )
      .filter( o -> hasDependencyAnnotation( o.getGetter() ) )
      .forEach( o -> addOrUpdateDependency( o.getGetter(), o ) );

    _roComputeds
      .stream()
      .filter( ComputedDescriptor::hasComputed )
      .map( ComputedDescriptor::getComputed )
      .filter( this::hasDependencyAnnotation )
      .forEach( this::addDependency );

    candidates
      .stream()
      .map( CandidateMethod::getMethod )
      .filter( this::hasDependencyAnnotation )
      .forEach( this::addDependency );
  }

  private boolean hasDependencyAnnotation( @Nonnull final ExecutableElement method )
  {
    return null != ProcessorUtil.findAnnotationByType( method, Constants.DEPENDENCY_ANNOTATION_CLASSNAME );
  }

  private void addOrUpdateDependency( @Nonnull final ExecutableElement method,
                                      @Nonnull final ObservableDescriptor observable )
  {
    final DependencyDescriptor dependencyDescriptor =
      _dependencies.computeIfAbsent( method, this::createDependencyDescriptor );
    dependencyDescriptor.setObservable( observable );
  }

  private void addReferenceId( @Nonnull final AnnotationMirror annotation,
                               @Nonnull final ObservableDescriptor observable,
                               @Nonnull final ExecutableElement method )
  {
    MethodChecks.mustNotHaveAnyParameters( Constants.REFERENCE_ID_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustBeSubclassCallable( getElement(), Constants.REFERENCE_ID_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotThrowAnyExceptions( Constants.REFERENCE_ID_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustReturnAValue( Constants.REFERENCE_ID_ANNOTATION_CLASSNAME, method );

    findOrCreateReference( getReferenceIdName( annotation, method ) ).setObservable( observable );
  }

  private void addReferenceId( @Nonnull final AnnotationMirror annotation,
                               @Nonnull final ExecutableElement method,
                               @Nonnull final ExecutableType methodType )
  {
    MethodChecks.mustNotHaveAnyParameters( Constants.REFERENCE_ID_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustBeSubclassCallable( getElement(), Constants.REFERENCE_ID_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotThrowAnyExceptions( Constants.REFERENCE_ID_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustReturnAValue( Constants.REFERENCE_ID_ANNOTATION_CLASSNAME, method );

    final String name = getReferenceIdName( annotation, method );
    findOrCreateReference( name ).setIdMethod( method, methodType );
  }

  @Nonnull
  private String getReferenceIdName( @Nonnull final AnnotationMirror annotation,
                                     @Nonnull final ExecutableElement method )
  {
    final String declaredName = getAnnotationParameter( annotation, "name" );
    final String name;
    if ( ProcessorUtil.isSentinelName( declaredName ) )
    {
      final String candidate = ProcessorUtil.deriveName( method, ID_GETTER_PATTERN, declaredName );
      if ( null == candidate )
      {
        final String candidate2 = ProcessorUtil.deriveName( method, RAW_ID_GETTER_PATTERN, declaredName );
        if ( null == candidate2 )
        {
          throw new ArezProcessorException( "@ReferenceId target has not specified a name and does not follow " +
                                            "the convention \"get[Name]Id\" or \"[name]Id\"", method );
        }
        else
        {
          name = candidate2;
        }
      }
      else
      {
        name = candidate;
      }
    }
    else
    {
      name = declaredName;
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ArezProcessorException( "@ReferenceId target specified an invalid name '" + name + "'. The " +
                                          "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ArezProcessorException( "@ReferenceId target specified an invalid name '" + name + "'. The " +
                                          "name must not be a java keyword.", method );
      }
    }
    return name;
  }

  private void addInverse( @Nonnull final AnnotationMirror annotation,
                           @Nonnull final ExecutableElement method,
                           @Nonnull final ExecutableType methodType )
  {
    MethodChecks.mustNotHaveAnyParameters( Constants.INVERSE_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustBeSubclassCallable( getElement(), Constants.INVERSE_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotThrowAnyExceptions( Constants.INVERSE_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustReturnAValue( Constants.INVERSE_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustBeAbstract( Constants.INVERSE_ANNOTATION_CLASSNAME, method );

    final String name = getInverseName( annotation, method );
    final ObservableDescriptor observable = findOrCreateObservable( name );
    observable.setGetter( method, methodType );

    addInverse( annotation, observable, method );
  }

  private void addInverse( @Nonnull final AnnotationMirror annotation,
                           @Nonnull final ObservableDescriptor observable,
                           @Nonnull final ExecutableElement method )
  {
    MethodChecks.mustNotHaveAnyParameters( Constants.INVERSE_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustBeSubclassCallable( getElement(), Constants.INVERSE_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotThrowAnyExceptions( Constants.INVERSE_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustReturnAValue( Constants.INVERSE_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustBeAbstract( Constants.INVERSE_ANNOTATION_CLASSNAME, method );

    final String name = getInverseName( annotation, method );
    final InverseDescriptor existing = _inverses.get( name );
    if ( null != existing )
    {
      throw new ArezProcessorException( "@Inverse target defines duplicate inverse for name '" + name +
                                        "'. The other inverse is " + existing.getObservable().getGetter(),
                                        method );
    }
    else
    {
      final TypeMirror type = method.getReturnType();

      final Multiplicity multiplicity;
      TypeElement targetType = getInverseManyTypeTarget( method );
      if ( null != targetType )
      {
        multiplicity = Multiplicity.MANY;
      }
      else
      {
        if ( !( type instanceof DeclaredType ) ||
             null == ProcessorUtil.findAnnotationByType( ( (DeclaredType) type ).asElement(),
                                                         Constants.COMPONENT_ANNOTATION_CLASSNAME ) )
        {
          throw new ArezProcessorException( "@Inverse target expected to return a type annotated with " +
                                            Constants.COMPONENT_ANNOTATION_CLASSNAME, method );
        }
        targetType = (TypeElement) ( (DeclaredType) type ).asElement();
        if ( null != ProcessorUtil.findAnnotationByType( method, Constants.NONNULL_ANNOTATION_CLASSNAME ) )
        {
          multiplicity = Multiplicity.ONE;
        }
        else if ( null != ProcessorUtil.findAnnotationByType( method, Constants.NULLABLE_ANNOTATION_CLASSNAME ) )
        {
          multiplicity = Multiplicity.ZERO_OR_ONE;
        }
        else
        {
          throw new ArezProcessorException( "@Inverse target expected to be annotated with either " +
                                            Constants.NULLABLE_ANNOTATION_CLASSNAME + " or " +
                                            Constants.NONNULL_ANNOTATION_CLASSNAME, method );
        }
      }
      final String referenceName = getInverseReferenceNameParameter( method );
      final InverseDescriptor descriptor =
        new InverseDescriptor( this, observable, referenceName, multiplicity, targetType );
      _inverses.put( name, descriptor );
      verifyMultiplicityOfAssociatedReferenceMethod( descriptor );
    }
  }

  private void verifyMultiplicityOfAssociatedReferenceMethod( @Nonnull final InverseDescriptor descriptor )
  {
    final Multiplicity multiplicity =
      ProcessorUtil
        .getMethods( descriptor.getTargetType(), _elements, _typeUtils )
        .stream()
        .map( m -> {
          final AnnotationMirror a = ProcessorUtil.findAnnotationByType( m, Constants.REFERENCE_ANNOTATION_CLASSNAME );
          if ( null != a && getReferenceName( a, m ).equals( descriptor.getReferenceName() ) )
          {
            if ( null == ProcessorUtil.findAnnotationValueNoDefaults( a, "inverse" ) &&
                 null == ProcessorUtil.findAnnotationValueNoDefaults( a, "inverseName" ) &&
                 null == ProcessorUtil.findAnnotationValueNoDefaults( a, "inverseMultiplicity" ) )
            {
              throw new ArezProcessorException( "@Inverse target found an associated @Reference on the method '" +
                                                m.getSimpleName() + "' on type '" +
                                                descriptor.getTargetType().getQualifiedName() + "' but the " +
                                                "annotation has not configured an inverse.",
                                                descriptor.getObservable().getGetter() );
            }
            ensureTargetTypeAligns( descriptor, m.getReturnType() );
            return getReferenceInverseMultiplicity( a );
          }
          else
          {
            return null;
          }
        } )
        .filter( Objects::nonNull )
        .findAny()
        .orElse( null );
    if ( null == multiplicity )
    {
      throw new ArezProcessorException( "@Inverse target expected to find an associated @Reference annotation with " +
                                        "a name parameter equal to '" + descriptor.getReferenceName() + "' on class " +
                                        descriptor.getTargetType().getQualifiedName() + " but is unable to " +
                                        "locate a matching method.", descriptor.getObservable().getGetter() );
    }

    if ( descriptor.getMultiplicity() != multiplicity )
    {
      throw new ArezProcessorException( "@Inverse target has a multiplicity of " + descriptor.getMultiplicity() +
                                        " but that associated @Reference has a multiplicity of " + multiplicity +
                                        ". The multiplicity must align.", descriptor.getObservable().getGetter() );
    }
  }

  private void ensureTargetTypeAligns( @Nonnull final InverseDescriptor descriptor, @Nonnull final TypeMirror target )
  {
    if ( !_typeUtils.isSameType( target, getElement().asType() ) )
    {
      throw new ArezProcessorException( "@Inverse target expected to find an associated @Reference annotation with " +
                                        "a target type equal to " + descriptor.getTargetType() + " but the actual " +
                                        "target type is " + target, descriptor.getObservable().getGetter() );
    }
  }

  @Nullable
  private TypeElement getInverseManyTypeTarget( @Nonnull final ExecutableElement method )
  {
    final TypeName typeName = TypeName.get( method.getReturnType() );
    if ( typeName instanceof ParameterizedTypeName )
    {
      final ParameterizedTypeName type = (ParameterizedTypeName) typeName;
      if ( isSupportedInverseCollectionType( type.rawType.toString() ) && !type.typeArguments.isEmpty() )
      {
        final TypeElement typeElement = _elements.getTypeElement( type.typeArguments.get( 0 ).toString() );
        if ( null != ProcessorUtil.findAnnotationByType( typeElement, Constants.COMPONENT_ANNOTATION_CLASSNAME ) )
        {
          return typeElement;
        }
        else
        {
          throw new ArezProcessorException( "@Inverse target expected to return a type annotated with " +
                                            Constants.COMPONENT_ANNOTATION_CLASSNAME, method );
        }
      }
    }
    return null;
  }

  private boolean isSupportedInverseCollectionType( @Nonnull final String typeClassname )
  {
    return Collection.class.getName().equals( typeClassname ) ||
           Set.class.getName().equals( typeClassname ) ||
           List.class.getName().equals( typeClassname );
  }

  @Nonnull
  private String getInverseReferenceNameParameter( @Nonnull final ExecutableElement method )
  {
    final String declaredName =
      (String) ProcessorUtil.getAnnotationValue( _elements,
                                                 method,
                                                 Constants.INVERSE_ANNOTATION_CLASSNAME,
                                                 "referenceName" ).getValue();
    final String name;
    if ( ProcessorUtil.isSentinelName( declaredName ) )
    {
      name = ProcessorUtil.firstCharacterToLowerCase( getElement().getSimpleName().toString() );
    }
    else
    {
      name = declaredName;
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ArezProcessorException( "@Inverse target specified an invalid referenceName '" + name + "'. The " +
                                          "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ArezProcessorException( "@Inverse target specified an invalid referenceName '" + name + "'. The " +
                                          "name must not be a java keyword.", method );
      }
    }
    return name;
  }

  @Nonnull
  private String getInverseName( @Nonnull final AnnotationMirror annotation,
                                 @Nonnull final ExecutableElement method )
  {
    final String declaredName = getAnnotationParameter( annotation, "name" );
    final String name;
    if ( ProcessorUtil.isSentinelName( declaredName ) )
    {
      final String candidate = ProcessorUtil.deriveName( method, GETTER_PATTERN, declaredName );
      name = null == candidate ? method.getSimpleName().toString() : candidate;
    }
    else
    {
      name = declaredName;
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ArezProcessorException( "@Inverse target specified an invalid name '" + name + "'. The " +
                                          "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ArezProcessorException( "@Inverse target specified an invalid name '" + name + "'. The " +
                                          "name must not be a java keyword.", method );
      }
    }
    return name;
  }

  private void addReference( @Nonnull final AnnotationMirror annotation,
                             @Nonnull final ExecutableElement method,
                             @Nonnull final ExecutableType methodType )
  {
    MethodChecks.mustNotHaveAnyParameters( Constants.REFERENCE_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustBeSubclassCallable( getElement(), Constants.REFERENCE_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotThrowAnyExceptions( Constants.REFERENCE_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustReturnAValue( Constants.REFERENCE_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustBeAbstract( Constants.REFERENCE_ANNOTATION_CLASSNAME, method );

    final String name = getReferenceName( annotation, method );
    final String linkType = getLinkType( method );
    final String inverseName;
    final Multiplicity inverseMultiplicity;
    if ( hasInverse( annotation ) )
    {
      inverseMultiplicity = getReferenceInverseMultiplicity( annotation );
      inverseName = getReferenceInverseName( annotation, method, inverseMultiplicity );
      final TypeMirror returnType = method.getReturnType();
      if ( !( returnType instanceof DeclaredType ) ||
           null == ProcessorUtil.findAnnotationByType( ( (DeclaredType) returnType ).asElement(),
                                                       Constants.COMPONENT_ANNOTATION_CLASSNAME ) )
      {
        throw new ArezProcessorException( "@Reference target expected to return a type annotated with " +
                                          Constants.COMPONENT_ANNOTATION_CLASSNAME + " if there is an " +
                                          "inverse reference.", method );
      }
    }
    else
    {
      inverseName = null;
      inverseMultiplicity = null;
    }
    final ReferenceDescriptor descriptor = findOrCreateReference( name );
    descriptor.setMethod( method, methodType, linkType, inverseName, inverseMultiplicity );
    verifyMultiplicityOfAssociatedInverseMethod( descriptor );
  }

  private void verifyMultiplicityOfAssociatedInverseMethod( @Nonnull final ReferenceDescriptor descriptor )
  {
    final TypeElement element = (TypeElement) _typeUtils.asElement( descriptor.getMethod().getReturnType() );
    final String defaultInverseName =
      descriptor.hasInverse() ?
      null :
      ProcessorUtil.firstCharacterToLowerCase( getElement().getSimpleName().toString() ) + "s";
    final Multiplicity multiplicity =
      ProcessorUtil
        .getMethods( element, _elements, _typeUtils )
        .stream()
        .map( m -> {
          final AnnotationMirror a = ProcessorUtil.findAnnotationByType( m, Constants.INVERSE_ANNOTATION_CLASSNAME );
          if ( null == a )
          {
            return null;
          }
          final String inverseName = getInverseName( a, m );
          if ( !descriptor.hasInverse() && inverseName.equals( defaultInverseName ) )
          {
            throw new ArezProcessorException( "@Reference target has not configured an inverse but there is an " +
                                              "associated @Inverse annotated method named '" + m.getSimpleName() +
                                              "' on type '" + element.getQualifiedName() + "'.",
                                              descriptor.getMethod() );
          }
          if ( descriptor.hasInverse() && inverseName.equals( descriptor.getInverseName() ) )
          {
            final TypeElement target = getInverseManyTypeTarget( m );
            if ( null != target )
            {
              ensureTargetTypeAligns( descriptor, target.asType() );
              return Multiplicity.MANY;
            }
            else
            {
              ensureTargetTypeAligns( descriptor, m.getReturnType() );
              if ( null != ProcessorUtil.findAnnotationByType( m, Constants.NONNULL_ANNOTATION_CLASSNAME ) )
              {
                return Multiplicity.ONE;
              }
              else
              {
                return Multiplicity.ZERO_OR_ONE;
              }
            }
          }
          else
          {
            return null;
          }
        } )
        .filter( Objects::nonNull )
        .findAny()
        .orElse( null );

    if ( descriptor.hasInverse() )
    {
      if ( null == multiplicity )
      {
        throw new ArezProcessorException( "@Reference target expected to find an associated @Inverse annotation " +
                                          "with a name parameter equal to '" + descriptor.getInverseName() + "' on " +
                                          "class " + descriptor.getMethod().getReturnType() + " but is unable to " +
                                          "locate a matching method.", descriptor.getMethod() );
      }

      final Multiplicity inverseMultiplicity = descriptor.getInverseMultiplicity();
      if ( inverseMultiplicity != multiplicity )
      {
        throw new ArezProcessorException( "@Reference target has an inverseMultiplicity of " + inverseMultiplicity +
                                          " but that associated @Inverse has a multiplicity of " + multiplicity +
                                          ". The multiplicity must align.", descriptor.getMethod() );
      }
    }
  }

  private void ensureTargetTypeAligns( @Nonnull final ReferenceDescriptor descriptor, @Nonnull final TypeMirror target )
  {
    if ( !_typeUtils.isSameType( target, getElement().asType() ) )
    {
      throw new ArezProcessorException( "@Reference target expected to find an associated @Inverse annotation with " +
                                        "a target type equal to " + getElement().getQualifiedName() + " but " +
                                        "the actual target type is " + target, descriptor.getMethod() );
    }
  }

  private boolean hasInverse( @Nonnull final AnnotationMirror annotation )
  {
    final VariableElement variableElement = ProcessorUtil.getAnnotationValue( _elements, annotation, "inverse" );
    switch ( variableElement.getSimpleName().toString() )
    {
      case "ENABLE":
        return true;
      case "DISABLE":
        return false;
      default:
        return null != ProcessorUtil.findAnnotationValueNoDefaults( annotation, "inverseName" ) ||
               null != ProcessorUtil.findAnnotationValueNoDefaults( annotation, "inverseMultiplicity" );
    }
  }

  @Nonnull
  private String getReferenceInverseName( @Nonnull final AnnotationMirror annotation,
                                          @Nonnull final ExecutableElement method,
                                          @Nonnull final Multiplicity multiplicity )
  {
    final String declaredName =
      ProcessorUtil.getAnnotationValue( _elements, annotation, "inverseName" );
    final String name;
    if ( ProcessorUtil.isSentinelName( declaredName ) )
    {
      final String baseName = getElement().getSimpleName().toString();
      return ProcessorUtil.firstCharacterToLowerCase( baseName ) + ( Multiplicity.MANY == multiplicity ? "s" : "" );
    }
    else
    {
      name = declaredName;
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ArezProcessorException( "@Reference target specified an invalid inverseName '" + name + "'. The " +
                                          "inverseName must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ArezProcessorException( "@Reference target specified an invalid inverseName '" + name + "'. The " +
                                          "inverseName must not be a java keyword.", method );
      }
    }
    return name;
  }

  @Nonnull
  private Multiplicity getReferenceInverseMultiplicity( @Nonnull final AnnotationMirror annotation )
  {
    final VariableElement variableElement =
      ProcessorUtil.getAnnotationValue( _elements, annotation, "inverseMultiplicity" );
    switch ( variableElement.getSimpleName().toString() )
    {
      case "MANY":
        return Multiplicity.MANY;
      case "ONE":
        return Multiplicity.ONE;
      default:
        return Multiplicity.ZERO_OR_ONE;
    }
  }

  @Nonnull
  private String getReferenceName( @Nonnull final AnnotationMirror annotation, @Nonnull final ExecutableElement method )
  {
    final String declaredName = getAnnotationParameter( annotation, "name" );
    final String name;
    if ( ProcessorUtil.isSentinelName( declaredName ) )
    {
      final String candidate = ProcessorUtil.deriveName( method, GETTER_PATTERN, declaredName );
      if ( null == candidate )
      {
        name = method.getSimpleName().toString();
      }
      else
      {
        name = candidate;
      }
    }
    else
    {
      name = declaredName;
      if ( !SourceVersion.isIdentifier( name ) )
      {
        throw new ArezProcessorException( "@Reference target specified an invalid name '" + name + "'. The " +
                                          "name must be a valid java identifier.", method );
      }
      else if ( SourceVersion.isKeyword( name ) )
      {
        throw new ArezProcessorException( "@Reference target specified an invalid name '" + name + "'. The " +
                                          "name must not be a java keyword.", method );
      }
    }
    return name;
  }

  @Nonnull
  private String getLinkType( @Nonnull final ExecutableElement method )
  {
    final VariableElement injectParameter = (VariableElement)
      ProcessorUtil.getAnnotationValue( _elements,
                                        method,
                                        Constants.REFERENCE_ANNOTATION_CLASSNAME,
                                        "load" ).getValue();
    return injectParameter.getSimpleName().toString();
  }

  private void addDependency( @Nonnull final ExecutableElement method )
  {
    _dependencies.put( method, createDependencyDescriptor( method ) );
  }

  @Nonnull
  private DependencyDescriptor createDependencyDescriptor( @Nonnull final ExecutableElement method )
  {
    MethodChecks.mustNotHaveAnyParameters( Constants.DEPENDENCY_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustBeSubclassCallable( getElement(), Constants.DEPENDENCY_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotThrowAnyExceptions( Constants.DEPENDENCY_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustReturnAValue( Constants.DEPENDENCY_ANNOTATION_CLASSNAME, method );

    if ( TypeKind.DECLARED != method.getReturnType().getKind() )
    {
      throw new ArezProcessorException( "@Dependency target must return a non-primitive value", method );
    }
    final TypeElement disposeTrackable = _elements.getTypeElement( Constants.DISPOSE_TRACKABLE_CLASSNAME );
    assert null != disposeTrackable;
    if ( !_typeUtils.isAssignable( method.getReturnType(), disposeTrackable.asType() ) )
    {
      final TypeElement typeElement = (TypeElement) _typeUtils.asElement( method.getReturnType() );
      final AnnotationMirror value =
        ProcessorUtil.findAnnotationByType( typeElement, Constants.COMPONENT_ANNOTATION_CLASSNAME );
      if ( null == value || !ProcessorUtil.isDisposableTrackableRequired( _elements, typeElement ) )
      {
        throw new ArezProcessorException( "@Dependency target must return an instance compatible with " +
                                          Constants.DISPOSE_TRACKABLE_CLASSNAME + " or a type annotated " +
                                          "with @ArezComponent(disposeTrackable=ENABLE)", method );
      }
    }

    final boolean cascade = isActionCascade( method );
    return new DependencyDescriptor( method, cascade );
  }

  private boolean isActionCascade( @Nonnull final ExecutableElement method )
  {
    final VariableElement injectParameter = (VariableElement)
      ProcessorUtil.getAnnotationValue( _elements,
                                        method,
                                        Constants.DEPENDENCY_ANNOTATION_CLASSNAME,
                                        "action" ).getValue();
    switch ( injectParameter.getSimpleName().toString() )
    {
      case "CASCADE":
        return true;
      case "SET_NULL":
      default:
        return false;
    }
  }

  private void ensureNoAbstractMethods( @Nonnull final Collection<CandidateMethod> candidateMethods )
  {
    candidateMethods
      .stream()
      .map( CandidateMethod::getMethod )
      .filter( m -> m.getModifiers().contains( Modifier.ABSTRACT ) )
      .forEach( m -> {
        throw new ArezProcessorException( "@ArezComponent target has an abstract method not implemented by " +
                                          "framework. The method is named " + m.getSimpleName(), getElement() );
      } );
  }

  private void linkObserverRefs()
  {
    for ( final Map.Entry<String, CandidateMethod> entry : _observerRefs.entrySet() )
    {
      final String key = entry.getKey();
      final CandidateMethod method = entry.getValue();
      final ObservedDescriptor observed = _observeds.get( key );
      if ( null != observed )
      {
        observed.setRefMethod( method.getMethod(), method.getMethodType() );
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
                                            "@Observed or @Track method with that name exists", method.getMethod() );
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
        throw new ArezProcessorException( "@ObservableValueRef target unable to be associated with an " +
                                          "Observable property", observable.getRefMethod() );
      }
      else if ( !observable.hasSetter() && observable.expectSetter() )
      {
        final CandidateMethod candidate = setters.remove( observable.getName() );
        if ( null != candidate )
        {
          MethodChecks.mustBeOverridable( getElement(),
                                          Constants.OBSERVABLE_ANNOTATION_CLASSNAME,
                                          candidate.getMethod() );
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
          MethodChecks.mustBeOverridable( getElement(),
                                          Constants.OBSERVABLE_ANNOTATION_CLASSNAME,
                                          candidate.getMethod() );
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
          tracked.setTrackedMethod( false,
                                    "NORMAL",
                                    true,
                                    false,
                                    false,
                                    candidate.getMethod(),
                                    candidate.getMethodType() );
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
    final AnnotationMirror observed =
      ProcessorUtil.findAnnotationByType( method, Constants.OBSERVED_ANNOTATION_CLASSNAME );
    final AnnotationMirror observable =
      ProcessorUtil.findAnnotationByType( method, Constants.OBSERVABLE_ANNOTATION_CLASSNAME );
    final AnnotationMirror observableValueRef =
      ProcessorUtil.findAnnotationByType( method, Constants.OBSERVABLE_VALUE_REF_ANNOTATION_CLASSNAME );
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
    final AnnotationMirror track =
      ProcessorUtil.findAnnotationByType( method, Constants.TRACK_ANNOTATION_CLASSNAME );
    final AnnotationMirror onDepsChanged =
      ProcessorUtil.findAnnotationByType( method, Constants.ON_DEPS_CHANGED_ANNOTATION_CLASSNAME );
    final AnnotationMirror observerRef =
      ProcessorUtil.findAnnotationByType( method, Constants.OBSERVER_REF_ANNOTATION_CLASSNAME );
    final AnnotationMirror memoize =
      ProcessorUtil.findAnnotationByType( method, Constants.MEMOIZE_ANNOTATION_CLASSNAME );
    final AnnotationMirror dependency =
      ProcessorUtil.findAnnotationByType( method, Constants.DEPENDENCY_ANNOTATION_CLASSNAME );
    final AnnotationMirror reference =
      ProcessorUtil.findAnnotationByType( method, Constants.REFERENCE_ANNOTATION_CLASSNAME );
    final AnnotationMirror referenceId =
      ProcessorUtil.findAnnotationByType( method, Constants.REFERENCE_ID_ANNOTATION_CLASSNAME );
    final AnnotationMirror inverse =
      ProcessorUtil.findAnnotationByType( method, Constants.INVERSE_ANNOTATION_CLASSNAME );

    if ( null != observable )
    {
      final ObservableDescriptor descriptor = addObservable( observable, method, methodType );
      if ( null != referenceId )
      {
        addReferenceId( referenceId, descriptor, method );
      }
      if ( null != inverse )
      {
        addInverse( inverse, descriptor, method );
      }
      return true;
    }
    else if ( null != observableValueRef )
    {
      addObservableValueRef( observableValueRef, method, methodType );
      return true;
    }
    else if ( null != action )
    {
      addAction( action, method, methodType );
      return true;
    }
    else if ( null != observed )
    {
      addObserved( observed, method, methodType );
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
    else if ( null != dependency )
    {
      addDependency( method );
      return false;
    }
    else if ( null != reference )
    {
      addReference( reference, method, methodType );
      return true;
    }
    else if ( null != referenceId )
    {
      addReferenceId( referenceId, method, methodType );
      return true;
    }
    else if ( null != inverse )
    {
      addInverse( inverse, method, methodType );
      return true;
    }
    else
    {
      return false;
    }
  }

  @Nullable
  private Boolean isInitializerRequired( @Nonnull final ExecutableElement element )
  {
    final AnnotationMirror annotation =
      ProcessorUtil.findAnnotationByType( element, Constants.OBSERVABLE_ANNOTATION_CLASSNAME );
    final AnnotationValue v =
      null == annotation ? null : ProcessorUtil.findAnnotationValueNoDefaults( annotation, "initializer" );
    final String value = null == v ? "AUTODETECT" : ( (VariableElement) v.getValue() ).getSimpleName().toString();
    switch ( value )
    {
      case "ENABLE":
        return Boolean.TRUE;
      case "DISABLE":
        return Boolean.FALSE;
      default:
        return null;
    }
  }

  private boolean autodetectInitializer( @Nonnull final ExecutableElement element )
  {
    return element.getModifiers().contains( Modifier.ABSTRACT ) &&
           (
             (
               // Getter
               element.getReturnType().getKind() != TypeKind.VOID &&
               null != ProcessorUtil.findAnnotationByType( element, Constants.NONNULL_ANNOTATION_CLASSNAME ) &&
               null == ProcessorUtil.findAnnotationByType( element, Constants.INVERSE_ANNOTATION_CLASSNAME )
             ) ||
             (
               // Setter
               1 == element.getParameters().size() &&
               null != ProcessorUtil.findAnnotationByType( element.getParameters().get( 0 ),
                                                           Constants.NONNULL_ANNOTATION_CLASSNAME )
             )
           );
  }

  private void verifyNoDuplicateAnnotations( @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final String[] annotationTypes =
      new String[]{ Constants.ACTION_ANNOTATION_CLASSNAME,
                    Constants.OBSERVED_ANNOTATION_CLASSNAME,
                    Constants.TRACK_ANNOTATION_CLASSNAME,
                    Constants.ON_DEPS_CHANGED_ANNOTATION_CLASSNAME,
                    Constants.OBSERVER_REF_ANNOTATION_CLASSNAME,
                    Constants.OBSERVABLE_ANNOTATION_CLASSNAME,
                    Constants.OBSERVABLE_VALUE_REF_ANNOTATION_CLASSNAME,
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
                    Constants.REFERENCE_ANNOTATION_CLASSNAME,
                    Constants.REFERENCE_ID_ANNOTATION_CLASSNAME,
                    Constants.ON_ACTIVATE_ANNOTATION_CLASSNAME,
                    Constants.ON_DEACTIVATE_ANNOTATION_CLASSNAME,
                    Constants.ON_STALE_ANNOTATION_CLASSNAME,
                    Constants.DEPENDENCY_ANNOTATION_CLASSNAME };
    for ( int i = 0; i < annotationTypes.length; i++ )
    {
      final String type1 = annotationTypes[ i ];
      final Object annotation1 = ProcessorUtil.findAnnotationByType( method, type1 );
      if ( null != annotation1 )
      {
        for ( int j = i + 1; j < annotationTypes.length; j++ )
        {
          final String type2 = annotationTypes[ j ];
          final boolean observableDependency =
            type1.equals( Constants.OBSERVABLE_ANNOTATION_CLASSNAME ) &&
            type2.equals( Constants.DEPENDENCY_ANNOTATION_CLASSNAME );
          final boolean observableReferenceId =
            type1.equals( Constants.OBSERVABLE_ANNOTATION_CLASSNAME ) &&
            type2.equals( Constants.REFERENCE_ID_ANNOTATION_CLASSNAME );
          if ( !observableDependency && !observableReferenceId )
          {
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
    addOriginatingTypes( getElement(), builder );

    addGeneratedAnnotation( builder );
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
    final boolean hasInverseReferencedOutsideClass =
      _roInverses.stream().anyMatch( inverse -> {
        final PackageElement targetPackageElement = ProcessorUtil.getPackageElement( inverse.getTargetType() );
        final PackageElement selfPackageElement = getPackageElement( getElement() );
        return !Objects.equals( targetPackageElement.getQualifiedName(), selfPackageElement.getQualifiedName() );
      } );
    final boolean hasReferenceWithInverseOutsidePackage =
      _roReferences
        .stream()
        .filter( ReferenceDescriptor::hasInverse )
        .anyMatch( reference -> {
          final TypeElement typeElement =
            (TypeElement) _typeUtils.asElement( reference.getMethod().getReturnType() );

          final PackageElement targetPackageElement = ProcessorUtil.getPackageElement( typeElement );
          final PackageElement selfPackageElement = getPackageElement( getElement() );
          return !Objects.equals( targetPackageElement.getQualifiedName(), selfPackageElement.getQualifiedName() );
        } );
    if ( publicType || hasInverseReferencedOutsideClass || hasReferenceWithInverseOutsidePackage )
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
    if ( _observable )
    {
      builder.addSuperinterface( GeneratorUtil.COMPONENT_OBSERVABLE_CLASSNAME );
    }
    if ( _verify )
    {
      builder.addSuperinterface( GeneratorUtil.VERIFIABLE_CLASSNAME );
    }
    if ( _disposeTrackable )
    {
      builder.addSuperinterface( GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME );
    }
    if ( needsExplicitLink() )
    {
      builder.addSuperinterface( GeneratorUtil.LINKABLE_CLASSNAME );
    }

    buildFields( builder );

    buildConstructors( builder, typeUtils );

    builder.addMethod( buildContextRefMethod() );
    if ( null != _componentRef )
    {
      builder.addMethod( buildComponentRefMethod() );
    }
    if ( !_references.isEmpty() || !_inverses.isEmpty() )
    {
      builder.addMethod( buildLocatorRefMethod() );
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

    if ( _observable )
    {
      builder.addMethod( buildInternalObserve() );
      builder.addMethod( buildObserve() );
    }
    if ( _disposeTrackable || !_roReferences.isEmpty() || !_roInverses.isEmpty() || !_roCascadeDisposes.isEmpty() )
    {
      builder.addMethod( buildInternalPreDispose() );
    }
    if ( _disposeTrackable )
    {
      builder.addMethod( buildNotifierAccessor() );
    }
    builder.addMethod( buildIsDisposed() );
    builder.addMethod( buildDispose() );
    if ( _verify )
    {
      builder.addMethod( buildVerify() );
    }

    if ( needsExplicitLink() )
    {
      builder.addMethod( buildLink() );
    }

    _roObservables.forEach( e -> e.buildMethods( builder ) );
    _roObserveds.forEach( e -> e.buildMethods( builder ) );
    _roActions.forEach( e -> e.buildMethods( builder ) );
    _roComputeds.forEach( e -> e.buildMethods( builder ) );
    _roMemoizes.forEach( e -> e.buildMethods( builder ) );
    _roTrackeds.forEach( e -> e.buildMethods( builder ) );
    _roReferences.forEach( e -> e.buildMethods( builder ) );
    _roInverses.forEach( e -> e.buildMethods( builder ) );

    builder.addMethod( buildHashcodeMethod() );
    builder.addMethod( buildEqualsMethod() );

    if ( _generateToString )
    {
      builder.addMethod( buildToStringMethod() );
    }

    return builder.build();
  }

  private boolean needsExplicitLink()
  {
    return _roReferences.stream().anyMatch( r -> r.getLinkType().equals( "EXPLICIT" ) );
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
    if ( null != _componentId )
    {
      codeBlock.nextControlFlow( "else if ( $T.isDisposed( this ) != $T.isDisposed( o ) )",
                                 GeneratorUtil.DISPOSABLE_CLASSNAME,
                                 GeneratorUtil.DISPOSABLE_CLASSNAME );
      codeBlock.addStatement( "return false" );
    }
    codeBlock.nextControlFlow( "else" );
    codeBlock.addStatement( "final $T that = ($T) o", generatedClass, generatedClass );
    final TypeKind kind = null != _componentId ? _componentId.getReturnType().getKind() : GeneratorUtil.DEFAULT_ID_KIND;
    if ( kind == TypeKind.DECLARED || kind == TypeKind.TYPEVAR )
    {
      codeBlock.addStatement( "return null != $N() && $N().equals( that.$N() )", idMethod, idMethod, idMethod );
    }
    else
    {
      codeBlock.addStatement( "return $N() == that.$N()", idMethod, idMethod );
    }
    codeBlock.endControlFlow();

    if ( _requireEquals )
    {
      method.addCode( codeBlock.build() );
    }
    else
    {
      final CodeBlock.Builder guardBlock = CodeBlock.builder();
      guardBlock.beginControlFlow( "if ( $T.areNativeComponentsEnabled() )", GeneratorUtil.AREZ_CLASSNAME );
      guardBlock.add( codeBlock.build() );
      guardBlock.nextControlFlow( "else" );
      guardBlock.addStatement( "return super.equals( o )" );
      guardBlock.endControlFlow();
      method.addCode( guardBlock.build() );
    }
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
    final TypeKind kind = null != _componentId ? _componentId.getReturnType().getKind() : GeneratorUtil.DEFAULT_ID_KIND;
    if ( _requireEquals )
    {
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
    }
    else
    {
      final CodeBlock.Builder guardBlock = CodeBlock.builder();
      guardBlock.beginControlFlow( "if ( $T.areNativeComponentsEnabled() )", GeneratorUtil.AREZ_CLASSNAME );
      if ( kind == TypeKind.DECLARED || kind == TypeKind.TYPEVAR )
      {
        guardBlock.addStatement( "return null != $N() ? $N().hashCode() : $T.identityHashCode( this )",
                                 idMethod,
                                 idMethod,
                                 System.class );
      }
      else if ( kind == TypeKind.BYTE )
      {
        guardBlock.addStatement( "return $T.hashCode( $N() )", Byte.class, idMethod );
      }
      else if ( kind == TypeKind.CHAR )
      {
        guardBlock.addStatement( "return $T.hashCode( $N() )", Character.class, idMethod );
      }
      else if ( kind == TypeKind.SHORT )
      {
        guardBlock.addStatement( "return $T.hashCode( $N() )", Short.class, idMethod );
      }
      else if ( kind == TypeKind.INT )
      {
        guardBlock.addStatement( "return $T.hashCode( $N() )", Integer.class, idMethod );
      }
      else if ( kind == TypeKind.LONG )
      {
        guardBlock.addStatement( "return $T.hashCode( $N() )", Long.class, idMethod );
      }
      else if ( kind == TypeKind.FLOAT )
      {
        guardBlock.addStatement( "return $T.hashCode( $N() )", Float.class, idMethod );
      }
      else if ( kind == TypeKind.DOUBLE )
      {
        guardBlock.addStatement( "return $T.hashCode( $N() )", Double.class, idMethod );
      }
      else
      {
        // So very unlikely but will cover it for completeness
        assert kind == TypeKind.BOOLEAN;
        guardBlock.addStatement( "return $T.hashCode( $N() )", Boolean.class, idMethod );
      }
      guardBlock.nextControlFlow( "else" );
      guardBlock.addStatement( "return super.hashCode()" );
      guardBlock.endControlFlow();
      method.addCode( guardBlock.build() );
    }

    return method.build();
  }

  @Nonnull
  String getComponentNameMethodName()
  {
    return null == _componentNameRef ? GeneratorUtil.NAME_METHOD_NAME : _componentNameRef.getSimpleName().toString();
  }

  @Nonnull
  private MethodSpec buildContextRefMethod()
    throws ArezProcessorException
  {
    final String methodName = getContextMethodName();
    final MethodSpec.Builder method = MethodSpec.methodBuilder( methodName ).
      addModifiers( Modifier.FINAL ).
      returns( GeneratorUtil.AREZ_CONTEXT_CLASSNAME );

    GeneratorUtil.generateNotInitializedInvariant( this, method, methodName );

    method.addStatement( "return $T.areZonesEnabled() ? this.$N : $T.context()",
                         GeneratorUtil.AREZ_CLASSNAME,
                         GeneratorUtil.CONTEXT_FIELD_NAME,
                         GeneratorUtil.AREZ_CLASSNAME );
    if ( null != _contextRef )
    {
      method.addAnnotation( Override.class );
      ProcessorUtil.copyWhitelistedAnnotations( _contextRef, method );
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
  private MethodSpec buildLocatorRefMethod()
    throws ArezProcessorException
  {
    final String methodName = GeneratorUtil.LOCATOR_METHOD_NAME;
    final MethodSpec.Builder method = MethodSpec.methodBuilder( methodName ).
      addModifiers( Modifier.FINAL ).
      returns( GeneratorUtil.LOCATOR_CLASSNAME );

    GeneratorUtil.generateNotInitializedInvariant( this, method, methodName );

    method.addStatement( "return $N().locator()", getContextMethodName() );
    return method.build();
  }

  @Nonnull
  private MethodSpec buildComponentRefMethod()
    throws ArezProcessorException
  {
    assert null != _componentRef;

    final String methodName = _componentRef.getSimpleName().toString();
    final MethodSpec.Builder method = MethodSpec.methodBuilder( methodName ).
      addModifiers( Modifier.FINAL ).
      returns( GeneratorUtil.COMPONENT_CLASSNAME );

    GeneratorUtil.generateNotInitializedInvariant( this, method, methodName );
    GeneratorUtil.generateNotConstructedInvariant( this, method, methodName );
    GeneratorUtil.generateNotCompleteInvariant( this, method, methodName );
    GeneratorUtil.generateNotDisposedInvariant( this, method, methodName );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckInvariants() )", GeneratorUtil.AREZ_CLASSNAME );
    block.addStatement( "$T.invariant( () -> $T.areNativeComponentsEnabled(), () -> \"Invoked @ComponentRef " +
                        "method '$N' but Arez.areNativeComponentsEnabled() returned false.\" )",
                        GeneratorUtil.GUARDS_CLASSNAME,
                        GeneratorUtil.AREZ_CLASSNAME,
                        methodName );
    block.endControlFlow();

    method.addCode( block.build() );

    method.addStatement( "return this.$N", GeneratorUtil.COMPONENT_FIELD_NAME );
    ProcessorUtil.copyWhitelistedAnnotations( _componentRef, method );
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

    final MethodSpec.Builder method = MethodSpec.methodBuilder( GeneratorUtil.ID_FIELD_NAME ).
      addModifiers( Modifier.FINAL ).
      returns( GeneratorUtil.DEFAULT_ID_TYPE );

    if ( !_idRequired )
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      if ( _nameIncludesId )
      {
        block.beginControlFlow(
          "if ( $T.shouldCheckInvariants() && !$T.areNamesEnabled() && !$T.areRegistriesEnabled() && !$T.areNativeComponentsEnabled() )",
          GeneratorUtil.AREZ_CLASSNAME,
          GeneratorUtil.AREZ_CLASSNAME,
          GeneratorUtil.AREZ_CLASSNAME,
          GeneratorUtil.AREZ_CLASSNAME );
      }
      else
      {
        block.beginControlFlow(
          "if ( $T.shouldCheckInvariants() && !$T.areRegistriesEnabled() && !$T.areNativeComponentsEnabled() )",
          GeneratorUtil.AREZ_CLASSNAME,
          GeneratorUtil.AREZ_CLASSNAME,
          GeneratorUtil.AREZ_CLASSNAME );
      }
      block.addStatement(
        "$T.fail( () -> \"Method invoked to access id when id not expected on component named '\" + $N() + \"'.\" )",
        GeneratorUtil.GUARDS_CLASSNAME,
        getComponentNameMethodName() );
      block.endControlFlow();

      method.addCode( block.build() );
    }

    return method.addStatement( "return this.$N", GeneratorUtil.ID_FIELD_NAME ).build();
  }

  /**
   * Generate the getter for component name.
   */
  @Nonnull
  private MethodSpec buildComponentNameMethod()
    throws ArezProcessorException
  {
    final MethodSpec.Builder builder;
    final String methodName;
    if ( null == _componentNameRef )
    {
      methodName = GeneratorUtil.NAME_METHOD_NAME;
      builder = MethodSpec.methodBuilder( methodName );
    }
    else
    {
      methodName = _componentNameRef.getSimpleName().toString();
      builder = MethodSpec.methodBuilder( methodName );
      ProcessorUtil.copyWhitelistedAnnotations( _componentNameRef, builder );
      ProcessorUtil.copyAccessModifiers( _componentNameRef, builder );
      builder.addModifiers( Modifier.FINAL );
    }

    builder.returns( TypeName.get( String.class ) );
    GeneratorUtil.generateNotInitializedInvariant( this, builder, methodName );
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
    if ( null == _componentTypeNameRef )
    {
      return null;
    }

    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( _componentTypeNameRef.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( _componentTypeNameRef, builder );
    builder.addModifiers( Modifier.FINAL );
    builder.addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );

    builder.returns( TypeName.get( String.class ) );
    builder.addStatement( "return $S", _type );
    return builder.build();
  }

  @Nonnull
  private MethodSpec buildVerify()
  {
    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( "verify" ).
        addModifiers( Modifier.PUBLIC ).
        addAnnotation( Override.class );

    GeneratorUtil.generateNotDisposedInvariant( this, builder, "verify" );

    if ( !_roReferences.isEmpty() || !_roInverses.isEmpty() )
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() && $T.isVerifyEnabled() )",
                              GeneratorUtil.AREZ_CLASSNAME,
                              GeneratorUtil.AREZ_CLASSNAME );

      block.addStatement( "$T.apiInvariant( () -> this == $N().findById( $T.class, $N() ), () -> \"Attempted to " +
                          "lookup self in Locator with type $T and id '\" + $N() + \"' but unable to locate " +
                          "self. Actual value: \" + $N().findById( $T.class, $N() ) )",
                          GeneratorUtil.GUARDS_CLASSNAME,
                          GeneratorUtil.LOCATOR_METHOD_NAME,
                          getElement(),
                          getIdMethodName(),
                          getElement(),
                          getIdMethodName(),
                          GeneratorUtil.LOCATOR_METHOD_NAME,
                          getElement(),
                          getIdMethodName() );
      for ( final ReferenceDescriptor reference : _roReferences )
      {
        reference.buildVerify( block );
      }

      for ( final InverseDescriptor inverse : _roInverses )
      {
        inverse.buildVerify( block );
      }

      block.endControlFlow();
      builder.addCode( block.build() );
    }
    return builder.build();
  }

  @Nonnull
  private MethodSpec buildLink()
  {
    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( "link" ).
        addModifiers( Modifier.PUBLIC ).
        addAnnotation( Override.class );

    GeneratorUtil.generateNotDisposedInvariant( this, builder, "link" );

    final List<ReferenceDescriptor> explicitReferences =
      _roReferences.stream().filter( r -> r.getLinkType().equals( "EXPLICIT" ) ).collect( Collectors.toList() );
    for ( final ReferenceDescriptor reference : explicitReferences )
    {
      builder.addStatement( "this.$N()", reference.getLinkMethodName() );
    }
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
    codeBlock.beginControlFlow( "if ( !$T.isDisposingOrDisposed( this.$N ) )",
                                GeneratorUtil.COMPONENT_STATE_CLASSNAME,
                                GeneratorUtil.STATE_FIELD_NAME );
    codeBlock.addStatement( "this.$N = $T.COMPONENT_DISPOSING",
                            GeneratorUtil.STATE_FIELD_NAME,
                            GeneratorUtil.COMPONENT_STATE_CLASSNAME );
    final CodeBlock.Builder nativeComponentBlock = CodeBlock.builder();
    nativeComponentBlock.beginControlFlow( "if ( $T.areNativeComponentsEnabled() )", GeneratorUtil.AREZ_CLASSNAME );
    nativeComponentBlock.addStatement( "this.$N.dispose()", GeneratorUtil.COMPONENT_FIELD_NAME );
    nativeComponentBlock.nextControlFlow( "else" );

    final CodeBlock.Builder actionBlock = CodeBlock.builder();

    actionBlock.beginControlFlow( "$N().safeAction( $T.areNamesEnabled() ? $N() + $S : null, true, false, () -> {",
                                  getContextMethodName(),
                                  GeneratorUtil.AREZ_CLASSNAME,
                                  getComponentNameMethodName(),
                                  ".dispose" );

    if ( _disposeTrackable || !_roReferences.isEmpty() || !_roInverses.isEmpty() || !_roCascadeDisposes.isEmpty() )
    {
      actionBlock.addStatement( "this.$N()", GeneratorUtil.INTERNAL_PRE_DISPOSE_METHOD_NAME );
    }
    else if ( null != _preDispose )
    {
      actionBlock.addStatement( "super.$N()", _preDispose.getSimpleName() );
    }
    if ( _observable )
    {
      actionBlock.addStatement( "this.$N.dispose()", GeneratorUtil.DISPOSED_OBSERVABLE_FIELD_NAME );
    }
    _roObserveds.forEach( observed -> observed.buildDisposer( actionBlock ) );
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
    GeneratorUtil.setStateForInvariantChecking( codeBlock, "COMPONENT_DISPOSED" );
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

    builder.addStatement( "return $T.isDisposingOrDisposed( this.$N )",
                          GeneratorUtil.COMPONENT_STATE_CLASSNAME,
                          GeneratorUtil.STATE_FIELD_NAME );
    return builder.build();
  }

  /**
   * Generate the observe method.
   */
  @Nonnull
  private MethodSpec buildInternalObserve()
    throws ArezProcessorException
  {
    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( GeneratorUtil.INTERNAL_OBSERVE_METHOD_NAME ).
        addModifiers( Modifier.PRIVATE ).
        returns( TypeName.BOOLEAN );

    builder.addStatement( "final boolean isNotDisposed = isNotDisposed()" );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( isNotDisposed ) ", getContextMethodName() );
    block.addStatement( "this.$N.reportObserved()", GeneratorUtil.DISPOSED_OBSERVABLE_FIELD_NAME );
    block.endControlFlow();
    builder.addCode( block.build() );
    builder.addStatement( "return isNotDisposed" );
    return builder.build();
  }

  /**
   * Generate the observe method.
   */
  @Nonnull
  private MethodSpec buildObserve()
    throws ArezProcessorException
  {
    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( "observe" ).
        addModifiers( Modifier.PUBLIC ).
        addAnnotation( Override.class ).
        returns( TypeName.BOOLEAN );

    if ( _disposeOnDeactivate )
    {
      builder.addStatement( "return $N.get()", GeneratorUtil.DISPOSE_ON_DEACTIVATE_FIELD_NAME );
    }
    else
    {
      builder.addStatement( "return $N()", GeneratorUtil.INTERNAL_OBSERVE_METHOD_NAME );
    }
    return builder.build();
  }

  /**
   * Generate the preDispose method.
   */
  @Nonnull
  private MethodSpec buildInternalPreDispose()
    throws ArezProcessorException
  {
    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( GeneratorUtil.INTERNAL_PRE_DISPOSE_METHOD_NAME ).
        addModifiers( Modifier.PRIVATE );

    if ( null != _preDispose )
    {
      builder.addStatement( "super.$N()", _preDispose.getSimpleName() );
    }
    _roCascadeDisposes.forEach( r -> r.buildDisposer( builder ) );
    _roReferences.forEach( r -> r.buildDisposer( builder ) );
    _roInverses.forEach( r -> r.buildDisposer( builder ) );
    if ( _disposeTrackable )
    {
      builder.addStatement( "$N.dispose()", GeneratorUtil.DISPOSE_NOTIFIER_FIELD_NAME );
      for ( final DependencyDescriptor dependency : _roDependencies )
      {
        final ExecutableElement method = dependency.getMethod();
        final String methodName = method.getSimpleName().toString();
        final boolean isNonnull =
          null != ProcessorUtil.findAnnotationByType( method, Constants.NONNULL_ANNOTATION_CLASSNAME );
        if ( isNonnull )
        {
          builder.addStatement( "$T.asDisposeTrackable( $N() ).getNotifier().removeOnDisposeListener( this )",
                                GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
                                methodName );
        }
        else
        {
          final String varName = GeneratorUtil.VARIABLE_PREFIX + methodName + "_dependency";
          final boolean abstractObservables = method.getModifiers().contains( Modifier.ABSTRACT );
          if ( abstractObservables )
          {
            builder.addStatement( "final $T $N = this.$N",
                                  method.getReturnType(),
                                  varName,
                                  dependency.getObservable().getDataFieldName() );
          }
          else
          {
            builder.addStatement( "final $T $N = super.$N()", method.getReturnType(), varName, methodName );
          }
          final CodeBlock.Builder listenerBlock = CodeBlock.builder();
          listenerBlock.beginControlFlow( "if ( null != $N )", varName );
          listenerBlock.addStatement( "$T.asDisposeTrackable( $N ).getNotifier().removeOnDisposeListener( this )",
                                      GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
                                      varName );
          listenerBlock.endControlFlow();
          builder.addCode( listenerBlock.build() );
        }
      }
    }

    return builder.build();
  }

  /**
   * Generate the observe method.
   */
  @Nonnull
  private MethodSpec buildNotifierAccessor()
    throws ArezProcessorException
  {
    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( "getNotifier" ).
        addModifiers( Modifier.PUBLIC ).
        addAnnotation( Override.class ).
        addAnnotation( GeneratorUtil.NONNULL_CLASSNAME ).
        returns( GeneratorUtil.DISPOSE_NOTIFIER_CLASSNAME );

    builder.addStatement( "return $N", GeneratorUtil.DISPOSE_NOTIFIER_FIELD_NAME );
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
        FieldSpec.builder( GeneratorUtil.DEFAULT_ID_TYPE,
                           GeneratorUtil.NEXT_ID_FIELD_NAME,
                           Modifier.VOLATILE,
                           Modifier.STATIC,
                           Modifier.PRIVATE );
      builder.addField( nextIdField.build() );

      final FieldSpec.Builder idField =
        FieldSpec.builder( GeneratorUtil.DEFAULT_ID_TYPE,
                           GeneratorUtil.ID_FIELD_NAME,
                           Modifier.FINAL,
                           Modifier.PRIVATE );
      builder.addField( idField.build() );
    }

    final FieldSpec.Builder disposableField =
      FieldSpec.builder( TypeName.BYTE, GeneratorUtil.STATE_FIELD_NAME, Modifier.PRIVATE );
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
    if ( _observable )
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
    if ( _disposeTrackable )
    {
      final FieldSpec.Builder field =
        FieldSpec.builder( GeneratorUtil.DISPOSE_NOTIFIER_CLASSNAME,
                           GeneratorUtil.DISPOSE_NOTIFIER_FIELD_NAME,
                           Modifier.FINAL,
                           Modifier.PRIVATE );
      builder.addField( field.build() );
    }
    _roObservables.forEach( observable -> observable.buildFields( builder ) );
    _roComputeds.forEach( computed -> computed.buildFields( builder ) );
    _roMemoizes.forEach( e -> e.buildFields( builder ) );
    _roObserveds.forEach( observed -> observed.buildFields( builder ) );
    _roTrackeds.forEach( tracked -> tracked.buildFields( builder ) );
    _roReferences.forEach( r -> r.buildFields( builder ) );
    if ( _disposeOnDeactivate )
    {
      final FieldSpec.Builder field =
        FieldSpec.builder( ParameterizedTypeName.get( GeneratorUtil.COMPUTED_VALUE_CLASSNAME, TypeName.BOOLEAN.box() ),
                           GeneratorUtil.DISPOSE_ON_DEACTIVATE_FIELD_NAME,
                           Modifier.FINAL,
                           Modifier.PRIVATE ).
          addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );
      builder.addField( field.build() );
    }
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
    if ( constructor.getModifiers().contains( Modifier.PUBLIC ) &&
         getElement().getModifiers().contains( Modifier.PUBLIC ) )
    {
      /*
       * The constructor MUST be public if annotated class is public as that implies that we expect
       * that code outside the package may construct the component.
       */
      builder.addModifiers( Modifier.PUBLIC );
    }

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

    final List<ObservableDescriptor> initializers = getInitializers();

    final StringBuilder superCall = new StringBuilder();
    superCall.append( "super(" );
    final ArrayList<String> parameterNames = new ArrayList<>();

    boolean firstParam = true;
    for ( final VariableElement element : constructor.getParameters() )
    {
      final ParameterSpec.Builder param =
        ParameterSpec.builder( TypeName.get( element.asType() ), element.getSimpleName().toString(), Modifier.FINAL );
      ProcessorUtil.copyWhitelistedAnnotations( element, param );
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
    if ( !_references.isEmpty() )
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", GeneratorUtil.AREZ_CLASSNAME );
      block.addStatement( "$T.apiInvariant( () -> $T.areReferencesEnabled(), () -> \"Attempted to create instance " +
                          "of component of type '$N' that contains references but Arez.areReferencesEnabled() " +
                          "returns false. References need to be enabled to use this component\" )",
                          GeneratorUtil.GUARDS_CLASSNAME,
                          GeneratorUtil.AREZ_CLASSNAME,
                          getType() );
      block.endControlFlow();
      builder.addCode( block.build() );
    }

    for ( final ObservableDescriptor observable : initializers )
    {
      final String candidateName = observable.getName();
      final String name = isNameCollision( constructor, Collections.emptyList(), candidateName ) ?
                          GeneratorUtil.INITIALIZER_PREFIX + candidateName :
                          candidateName;
      final ParameterSpec.Builder param =
        ParameterSpec.builder( TypeName.get( observable.getGetterType().getReturnType() ),
                               name,
                               Modifier.FINAL );
      ProcessorUtil.copyWhitelistedAnnotations( observable.getGetter(), param );
      builder.addParameter( param.build() );
      final boolean isPrimitive = TypeName.get( observable.getGetterType().getReturnType() ).isPrimitive();
      if ( isPrimitive )
      {
        builder.addStatement( "this.$N = $N", observable.getDataFieldName(), name );
      }
      else if ( observable.isGetterNonnull() )
      {
        builder.addStatement( "this.$N = $T.requireNonNull( $N )",
                              observable.getDataFieldName(),
                              Objects.class,
                              name );
      }
      else
      {
        builder.addStatement( "this.$N = $N", observable.getDataFieldName(), name );
      }
    }

    builder.addStatement( "this.$N = $T.areZonesEnabled() ? $T.context() : null",
                          GeneratorUtil.CONTEXT_FIELD_NAME,
                          GeneratorUtil.AREZ_CLASSNAME,
                          GeneratorUtil.AREZ_CLASSNAME );

    // Synthesize Id if required
    if ( null == _componentId )
    {
      if ( _nameIncludesId )
      {
        if ( _idRequired )
        {
          builder.addStatement( "this.$N = $N++", GeneratorUtil.ID_FIELD_NAME, GeneratorUtil.NEXT_ID_FIELD_NAME );
        }
        else
        {
          builder.addStatement(
            "this.$N = ( $T.areNamesEnabled() || $T.areRegistriesEnabled() || $T.areNativeComponentsEnabled() ) ? $N++ : 0",
            GeneratorUtil.ID_FIELD_NAME,
            GeneratorUtil.AREZ_CLASSNAME,
            GeneratorUtil.AREZ_CLASSNAME,
            GeneratorUtil.AREZ_CLASSNAME,
            GeneratorUtil.NEXT_ID_FIELD_NAME );
        }
      }
      else
      {
        builder.addStatement( "this.$N = ( $T.areRegistriesEnabled() || $T.areNativeComponentsEnabled() ) ? $N++ : 0",
                              GeneratorUtil.ID_FIELD_NAME,
                              GeneratorUtil.AREZ_CLASSNAME,
                              GeneratorUtil.AREZ_CLASSNAME,
                              GeneratorUtil.NEXT_ID_FIELD_NAME );
      }
    }
    GeneratorUtil.setStateForInvariantChecking( builder, "COMPONENT_INITIALIZED" );

    // Create component representation if required
    {
      final StringBuilder sb = new StringBuilder();
      final ArrayList<Object> params = new ArrayList<>();
      sb.append( "this.$N = $T.areNativeComponentsEnabled() ? " +
                 "$N().component( $S, $N(), $T.areNamesEnabled() ? $N() :" +
                 " null" );
      params.add( GeneratorUtil.COMPONENT_FIELD_NAME );
      params.add( GeneratorUtil.AREZ_CLASSNAME );
      params.add( getContextMethodName() );
      params.add( _type );
      params.add( getIdMethodName() );
      params.add( GeneratorUtil.AREZ_CLASSNAME );
      params.add( getComponentNameMethodName() );
      if ( _disposeTrackable || null != _preDispose || null != _postDispose )
      {
        sb.append( ", " );
        if ( _disposeTrackable )
        {
          sb.append( "() -> $N()" );
          params.add( GeneratorUtil.INTERNAL_PRE_DISPOSE_METHOD_NAME );
        }
        else if ( null != _preDispose )
        {
          sb.append( "() -> super.$N()" );
          params.add( _preDispose.getSimpleName().toString() );
        }

        if ( null != _postDispose )
        {
          sb.append( ",  () -> super.$N()" );
          params.add( _postDispose.getSimpleName().toString() );
        }
      }
      sb.append( " ) : null" );
      builder.addStatement( sb.toString(), params.toArray() );
    }
    if ( _observable )
    {
      builder.addStatement( "this.$N = $N().observable( " +
                            "$T.areNativeComponentsEnabled() ? this.$N : null, " +
                            "$T.areNamesEnabled() ? $N() + $S : null, " +
                            "$T.arePropertyIntrospectorsEnabled() ? () -> this.$N >= 0 : null )",
                            GeneratorUtil.DISPOSED_OBSERVABLE_FIELD_NAME,
                            getContextMethodName(),
                            GeneratorUtil.AREZ_CLASSNAME,
                            GeneratorUtil.COMPONENT_FIELD_NAME,
                            GeneratorUtil.AREZ_CLASSNAME,
                            getComponentNameMethodName(),
                            ".isDisposed",
                            GeneratorUtil.AREZ_CLASSNAME,
                            GeneratorUtil.STATE_FIELD_NAME );
    }
    if ( _disposeTrackable )
    {
      builder.addStatement( "this.$N = new $T()",
                            GeneratorUtil.DISPOSE_NOTIFIER_FIELD_NAME,
                            GeneratorUtil.DISPOSE_NOTIFIER_CLASSNAME );

    }
    if ( _disposeOnDeactivate )
    {
      builder.addStatement( "this.$N = $N().computed( " +
                            "$T.areNativeComponentsEnabled() ? this.$N : null, " +
                            "$T.areNamesEnabled() ? $N() + $S : null, " +
                            "() -> $N(), null, () -> $N().scheduleDispose( this ), null, $T.PRIORITY_HIGHEST )",
                            GeneratorUtil.DISPOSE_ON_DEACTIVATE_FIELD_NAME,
                            getContextMethodName(),
                            GeneratorUtil.AREZ_CLASSNAME,
                            GeneratorUtil.COMPONENT_FIELD_NAME,
                            GeneratorUtil.AREZ_CLASSNAME,
                            getComponentNameMethodName(),
                            ".disposeOnDeactivate",
                            GeneratorUtil.INTERNAL_OBSERVE_METHOD_NAME,
                            getContextMethodName(),
                            GeneratorUtil.FLAGS_CLASSNAME );
    }

    _roObservables.forEach( observable -> observable.buildInitializer( builder ) );
    _roComputeds.forEach( computed -> computed.buildInitializer( builder ) );
    _roMemoizes.forEach( e -> e.buildInitializer( builder ) );
    _roObserveds.forEach( observed -> observed.buildInitializer( builder ) );
    _roTrackeds.forEach( tracked -> tracked.buildInitializer( builder ) );
    _roInverses.forEach( e -> e.buildInitializer( builder ) );

    for ( final DependencyDescriptor dep : _roDependencies )
    {
      final ExecutableElement method = dep.getMethod();
      final String methodName = method.getSimpleName().toString();
      final boolean abstractObservables = method.getModifiers().contains( Modifier.ABSTRACT );
      final boolean isNonnull =
        null != ProcessorUtil.findAnnotationByType( method, Constants.NONNULL_ANNOTATION_CLASSNAME );
      if ( abstractObservables )
      {
        if ( isNonnull )
        {
          assert dep.shouldCascadeDispose();
          builder.addStatement( "$T.asDisposeTrackable( $N ).getNotifier().addOnDisposeListener( this, this::dispose )",
                                GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
                                dep.getObservable().getDataFieldName() );
        }
        else
        {
          final String varName = GeneratorUtil.VARIABLE_PREFIX + methodName + "_dependency";
          builder.addStatement( "final $T $N = this.$N",
                                dep.getMethod().getReturnType(),
                                varName,
                                dep.getObservable().getDataFieldName() );
          final CodeBlock.Builder listenerBlock = CodeBlock.builder();
          listenerBlock.beginControlFlow( "if ( null != $N )", varName );
          if ( dep.shouldCascadeDispose() )
          {
            listenerBlock.addStatement(
              "$T.asDisposeTrackable( $N ).getNotifier().addOnDisposeListener( this, this::dispose )",
              GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
              dep.getObservable().getDataFieldName() );
          }
          else
          {
            listenerBlock.addStatement(
              "$T.asDisposeTrackable( $N ).getNotifier().addOnDisposeListener( this, () -> $N( null ) )",
              GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
              dep.getObservable().getDataFieldName(),
              dep.getObservable().getSetter().getSimpleName().toString() );
          }
          listenerBlock.endControlFlow();
          builder.addCode( listenerBlock.build() );
        }
      }
      else
      {
        if ( isNonnull )
        {
          assert dep.shouldCascadeDispose();
          builder.addStatement(
            "$T.asDisposeTrackable( super.$N() ).getNotifier().addOnDisposeListener( this, this::dispose )",
            GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
            method.getSimpleName().toString() );
        }
        else
        {
          final String varName = GeneratorUtil.VARIABLE_PREFIX + methodName + "_dependency";
          builder.addStatement( "final $T $N = super.$N()",
                                dep.getMethod().getReturnType(),
                                varName,
                                method.getSimpleName().toString() );
          final CodeBlock.Builder listenerBlock = CodeBlock.builder();
          listenerBlock.beginControlFlow( "if ( null != $N )", varName );
          if ( dep.shouldCascadeDispose() )
          {
            listenerBlock.addStatement(
              "$T.asDisposeTrackable( super.$N() ).getNotifier().addOnDisposeListener( this, this::dispose )",
              GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
              method.getSimpleName() );
          }
          else
          {
            listenerBlock.addStatement(
              "$T.asDisposeTrackable( super.$N() ).getNotifier().addOnDisposeListener( this, () -> $N( null ) )",
              GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
              method.getSimpleName(),
              dep.getObservable().getSetter().getSimpleName().toString() );
          }
          listenerBlock.endControlFlow();
          builder.addCode( listenerBlock.build() );
        }
      }
    }

    GeneratorUtil.setStateForInvariantChecking( builder, "COMPONENT_CONSTRUCTED" );

    final List<ReferenceDescriptor> eagerReferences =
      _roReferences.stream().filter( r -> r.getLinkType().equals( "EAGER" ) ).collect( Collectors.toList() );
    for ( final ReferenceDescriptor reference : eagerReferences )
    {
      builder.addStatement( "this.$N()", reference.getLinkMethodName() );
    }

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

    if ( !_deferSchedule && requiresSchedule() )
    {
      GeneratorUtil.setStateForInvariantChecking( builder, "COMPONENT_COMPLETE" );

      builder.addStatement( "$N().triggerScheduler()", getContextMethodName() );
    }

    GeneratorUtil.setStateForInvariantChecking( builder, "COMPONENT_READY" );
    return builder.build();
  }

  @Nonnull
  private List<ObservableDescriptor> getInitializers()
  {
    return getObservables()
      .stream()
      .filter( ObservableDescriptor::requireInitializer )
      .collect( Collectors.toList() );
  }

  private boolean isNameCollision( @Nonnull final ExecutableElement constructor,
                                   @Nonnull final List<ObservableDescriptor> initializers,
                                   @Nonnull final String name )
  {
    return constructor.getParameters().stream().anyMatch( p -> p.getSimpleName().toString().equals( name ) ) ||
           initializers.stream().anyMatch( o -> o.getName().equals( name ) );
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
    addOriginatingTypes( getElement(), builder );

    addGeneratedAnnotation( builder );
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
    return null != _repositoryExtensions;
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
    addOriginatingTypes( element, builder );

    addGeneratedAnnotation( builder );

    final boolean addSingletonAnnotation =
      "ENABLE".equals( _repositoryInjectConfig ) ||
      ( "AUTODETECT".equals( _repositoryInjectConfig ) && _injectClassesPresent );

    final AnnotationSpec.Builder arezComponent =
      AnnotationSpec.builder( ClassName.bestGuess( Constants.COMPONENT_ANNOTATION_CLASSNAME ) );
    if ( !addSingletonAnnotation )
    {
      arezComponent.addMember( "nameIncludesId", "false" );
    }
    if ( !"AUTODETECT".equals( _repositoryInjectConfig ) )
    {
      arezComponent.addMember( "inject", "$T.$N", GeneratorUtil.INJECTIBLE_CLASSNAME, _repositoryInjectConfig );
    }
    if ( !"AUTODETECT".equals( _repositoryDaggerConfig ) )
    {
      arezComponent.addMember( "dagger", "$T.$N", GeneratorUtil.INJECTIBLE_CLASSNAME, _repositoryDaggerConfig );
    }
    builder.addAnnotation( arezComponent.build() );
    if ( addSingletonAnnotation )
    {
      builder.addAnnotation( GeneratorUtil.SINGLETON_CLASSNAME );
    }

    builder.superclass( ParameterizedTypeName.get( GeneratorUtil.ABSTRACT_REPOSITORY_CLASSNAME,
                                                   getIdType().box(),
                                                   ClassName.get( element ),
                                                   ClassName.get( getPackageName(), getRepositoryName() ) ) );

    _repositoryExtensions.forEach( e -> builder.addSuperinterface( TypeName.get( e.asType() ) ) );

    ProcessorUtil.copyAccessModifiers( element, builder );

    builder.addModifiers( Modifier.ABSTRACT );

    //Add the default access, no-args constructor
    builder.addMethod( MethodSpec.constructorBuilder().build() );

    // Add the factory method
    builder.addMethod( buildFactoryMethod() );

    if ( shouldRepositoryDefineCreate() )
    {
      for ( final ExecutableElement constructor : ProcessorUtil.getConstructors( element ) )
      {
        final ExecutableType methodType =
          (ExecutableType) typeUtils.asMemberOf( (DeclaredType) _element.asType(), constructor );
        builder.addMethod( buildRepositoryCreate( constructor, methodType, arezType ) );
      }
    }
    if ( shouldRepositoryDefineAttach() )
    {
      builder.addMethod( buildRepositoryAttach() );
    }

    if ( null != _componentId )
    {
      builder.addMethod( buildFindByIdMethod() );
      builder.addMethod( buildGetByIdMethod() );
    }

    if ( shouldRepositoryDefineDestroy() )
    {
      builder.addMethod( buildRepositoryDestroy() );
    }
    if ( shouldRepositoryDefineDetach() )
    {
      builder.addMethod( buildRepositoryDetach() );
    }
    return builder.build();
  }

  private void addOriginatingTypes( @Nonnull final TypeElement element, @Nonnull final TypeSpec.Builder builder )
  {
    builder.addOriginatingElement( element );
    ProcessorUtil.getSuperTypes( element ).forEach( builder::addOriginatingElement );
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
  private MethodSpec buildRepositoryAttach()
  {
    final TypeName entityType = TypeName.get( getElement().asType() );
    final MethodSpec.Builder method = MethodSpec.methodBuilder( "attach" ).
      addAnnotation( Override.class ).
      addAnnotation( AnnotationSpec.builder( GeneratorUtil.ACTION_CLASSNAME )
                       .addMember( "reportParameters", "false" )
                       .build() ).
      addParameter( ParameterSpec.builder( entityType, "entity", Modifier.FINAL )
                      .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                      .build() ).
      addStatement( "super.attach( entity )" );
    ProcessorUtil.copyAccessModifiers( getElement(), method );
    return method.build();
  }

  @Nonnull
  private MethodSpec buildRepositoryDetach()
  {
    final TypeName entityType = TypeName.get( getElement().asType() );
    final MethodSpec.Builder method = MethodSpec.methodBuilder( "detach" ).
      addAnnotation( Override.class ).
      addAnnotation( AnnotationSpec.builder( GeneratorUtil.ACTION_CLASSNAME )
                       .addMember( "reportParameters", "false" )
                       .build() ).
      addParameter( ParameterSpec.builder( entityType, "entity", Modifier.FINAL )
                      .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                      .build() ).
      addStatement( "super.detach( entity )" );
    ProcessorUtil.copyAccessModifiers( getElement(), method );
    return method.build();
  }

  @Nonnull
  private MethodSpec buildRepositoryDestroy()
  {
    final TypeName entityType = TypeName.get( getElement().asType() );
    final MethodSpec.Builder method = MethodSpec.methodBuilder( "destroy" ).
      addAnnotation( Override.class ).
      addAnnotation( AnnotationSpec.builder( GeneratorUtil.ACTION_CLASSNAME )
                       .addMember( "reportParameters", "false" )
                       .build() ).
      addParameter( ParameterSpec.builder( entityType, "entity", Modifier.FINAL )
                      .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                      .build() ).
      addStatement( "super.destroy( entity )" );
    ProcessorUtil.copyAccessModifiers( getElement(), method );
    final Set<Modifier> modifiers = getElement().getModifiers();
    if ( !modifiers.contains( Modifier.PUBLIC ) && !modifiers.contains( Modifier.PROTECTED ) )
    {
      /*
       * The destroy method inherited from AbstractContainer is protected and the override
       * must be at least the same access level.
       */
      method.addModifiers( Modifier.PROTECTED );
    }
    return method.build();
  }

  @Nonnull
  private MethodSpec buildFindByIdMethod()
  {
    assert null != _componentId;

    final MethodSpec.Builder method = MethodSpec.methodBuilder( "findBy" + getIdName() ).
      addModifiers( Modifier.FINAL ).
      addParameter( ParameterSpec.builder( getIdType(), "id", Modifier.FINAL ).build() ).
      addAnnotation( GeneratorUtil.NULLABLE_CLASSNAME ).
      returns( TypeName.get( getElement().asType() ) ).
      addStatement( "return findByArezId( id )" );
    ProcessorUtil.copyAccessModifiers( getElement(), method );
    return method.build();
  }

  @Nonnull
  private MethodSpec buildGetByIdMethod()
  {
    final TypeName entityType = TypeName.get( getElement().asType() );
    final MethodSpec.Builder method = MethodSpec.methodBuilder( "getBy" + getIdName() ).
      addModifiers( Modifier.FINAL ).
      addAnnotation( GeneratorUtil.NONNULL_CLASSNAME ).
      addParameter( ParameterSpec.builder( getIdType(), "id", Modifier.FINAL ).build() ).
      returns( entityType ).
      addStatement( "return getByArezId( id )" );
    ProcessorUtil.copyAccessModifiers( getElement(), method );
    return method.build();
  }

  @Nonnull
  private MethodSpec buildFactoryMethod()
  {
    final MethodSpec.Builder method = MethodSpec.methodBuilder( "newRepository" ).
      addModifiers( Modifier.STATIC ).
      addAnnotation( GeneratorUtil.NONNULL_CLASSNAME ).
      returns( ClassName.get( getPackageName(), getRepositoryName() ) ).
      addStatement( "return new $T()", ClassName.get( getPackageName(), getArezRepositoryName() ) );
    ProcessorUtil.copyAccessModifiers( getElement(), method );
    return method.build();
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

    ProcessorUtil.copyAccessModifiers( getElement(), builder );
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
      ProcessorUtil.copyWhitelistedAnnotations( element, param );
      builder.addParameter( param.build() );
      parameters.add( element.getSimpleName().toString() );
      if ( !firstParam )
      {
        newCall.append( "," );
      }
      firstParam = false;
      newCall.append( "$N" );
    }

    for ( final ObservableDescriptor observable : getInitializers() )
    {
      final String candidateName = observable.getName();
      final String name = isNameCollision( constructor, Collections.emptyList(), candidateName ) ?
                          GeneratorUtil.INITIALIZER_PREFIX + candidateName :
                          candidateName;
      final ParameterSpec.Builder param =
        ParameterSpec.builder( TypeName.get( observable.getGetterType().getReturnType() ),
                               name,
                               Modifier.FINAL );
      ProcessorUtil.copyWhitelistedAnnotations( observable.getGetter(), param );
      builder.addParameter( param.build() );
      parameters.add( name );
      if ( !firstParam )
      {
        newCall.append( "," );
      }
      firstParam = false;
      newCall.append( "$N" );
    }

    newCall.append( ")" );
    builder.addStatement( newCall.toString(), parameters.toArray() );
    builder.addStatement( "attach( entity )" );
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
    assert null != _componentId;
    final String name = ProcessorUtil.deriveName( _componentId, GETTER_PATTERN, ProcessorUtil.SENTINEL_NAME );
    if ( null != name )
    {
      return Character.toUpperCase( name.charAt( 0 ) ) + ( name.length() > 1 ? name.substring( 1 ) : "" );
    }
    else
    {
      return "Id";
    }
  }

  @Nonnull
  private TypeName getIdType()
  {
    return null == _componentIdMethodType ?
           GeneratorUtil.DEFAULT_ID_TYPE :
           TypeName.get( _componentIdMethodType.getReturnType() );
  }

  private <T> T getAnnotationParameter( @Nonnull final AnnotationMirror annotation,
                                        @Nonnull final String parameterName )
  {
    return ProcessorUtil.getAnnotationValue( _elements, annotation, parameterName );
  }

  private void addGeneratedAnnotation( @Nonnull final TypeSpec.Builder builder )
  {
    GeneratedAnnotationSpecs
      .generatedAnnotationSpec( _elements, _sourceVersion, ArezProcessor.class )
      .ifPresent( builder::addAnnotation );
  }

  private boolean shouldRepositoryDefineCreate()
  {
    final VariableElement injectParameter = (VariableElement)
      ProcessorUtil.getAnnotationValue( _elements,
                                        getElement(),
                                        Constants.REPOSITORY_ANNOTATION_CLASSNAME,
                                        "attach" ).getValue();
    switch ( injectParameter.getSimpleName().toString() )
    {
      case "CREATE_ONLY":
      case "CREATE_OR_ATTACH":
        return true;
      default:
        return false;
    }
  }

  private boolean shouldRepositoryDefineAttach()
  {
    final VariableElement injectParameter = (VariableElement)
      ProcessorUtil.getAnnotationValue( _elements,
                                        getElement(),
                                        Constants.REPOSITORY_ANNOTATION_CLASSNAME,
                                        "attach" ).getValue();
    switch ( injectParameter.getSimpleName().toString() )
    {
      case "ATTACH_ONLY":
      case "CREATE_OR_ATTACH":
        return true;
      default:
        return false;
    }
  }

  private boolean shouldRepositoryDefineDestroy()
  {
    final VariableElement injectParameter = (VariableElement)
      ProcessorUtil.getAnnotationValue( _elements,
                                        getElement(),
                                        Constants.REPOSITORY_ANNOTATION_CLASSNAME,
                                        "detach" ).getValue();
    switch ( injectParameter.getSimpleName().toString() )
    {
      case "DESTROY_ONLY":
      case "DESTROY_OR_DETACH":
        return true;
      default:
        return false;
    }
  }

  private boolean shouldRepositoryDefineDetach()
  {
    final VariableElement injectParameter = (VariableElement)
      ProcessorUtil.getAnnotationValue( _elements,
                                        getElement(),
                                        Constants.REPOSITORY_ANNOTATION_CLASSNAME,
                                        "detach" ).getValue();
    switch ( injectParameter.getSimpleName().toString() )
    {
      case "DETACH_ONLY":
      case "DESTROY_OR_DETACH":
        return true;
      default:
        return false;
    }
  }
}
