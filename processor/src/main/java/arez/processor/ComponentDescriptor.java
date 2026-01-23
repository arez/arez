package arez.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import org.realityforge.proton.ElementsUtil;
import org.realityforge.proton.GeneratorUtil;

/**
 * The class that represents the parsed state of ArezComponent annotated class.
 */
final class ComponentDescriptor
{
  @Nonnull
  private final String _name;
  @Nullable
  private final Priority _defaultPriority;
  private final boolean _observable;
  private final boolean _disposeNotifier;
  private final boolean _disposeOnDeactivate;
  private final boolean _sting;
  private final boolean _requireEquals;
  /**
   * Flag indicating whether generated component should implement arez.component.Verifiable.
   */
  private final boolean _verify;
  private final boolean _generateToString;
  private boolean _idRequired;
  @Nonnull
  private final TypeElement _element;
  @Nonnull
  private final List<ExecutableElement> _postConstructs = new ArrayList<>();
  @Nonnull
  private final List<ExecutableElement> _postDisposes = new ArrayList<>();
  @Nonnull
  private final List<ExecutableElement> _preDisposes = new ArrayList<>();
  @Nullable
  private ExecutableElement _componentId;
  @Nullable
  private ExecutableType _componentIdMethodType;
  @Nonnull
  private final List<ExecutableElement> _componentIdRefs = new ArrayList<>();
  @Nonnull
  private final List<ExecutableElement> _componentNameRefs = new ArrayList<>();
  @Nonnull
  private final List<ExecutableElement> _componentRefs = new ArrayList<>();
  @Nonnull
  private final List<ComponentStateRefDescriptor> _componentStateRefs = new ArrayList<>();
  @Nonnull
  private final List<ExecutableElement> _componentTypeNameRefs = new ArrayList<>();
  @Nonnull
  private final List<ExecutableElement> _contextRefs = new ArrayList<>();
  @Nonnull
  private final Map<String, List<CandidateMethod>> _observerRefs = new LinkedHashMap<>();
  @Nonnull
  private final Map<String, ObservableDescriptor> _observables = new LinkedHashMap<>();
  @Nonnull
  private final Map<String, ObservableInitialDescriptor> _observableInitials = new LinkedHashMap<>();
  @Nonnull
  private final Map<String, ActionDescriptor> _actions = new LinkedHashMap<>();
  @Nonnull
  private final Map<String, MemoizeContextParameterDescriptor> _memoizeContextParameters = new LinkedHashMap<>();
  @Nonnull
  private final Map<String, MemoizeDescriptor> _memoizes = new LinkedHashMap<>();
  @Nonnull
  private final Map<String, ObserveDescriptor> _observes = new LinkedHashMap<>();
  @Nonnull
  private final Map<Element, DependencyDescriptor> _dependencies = new LinkedHashMap<>();
  @Nonnull
  private final Map<Element, CascadeDisposeDescriptor> _cascadeDisposes = new LinkedHashMap<>();
  @Nonnull
  private final Map<String, ReferenceDescriptor> _references = new LinkedHashMap<>();
  @Nonnull
  private final Map<String, InverseDescriptor> _inverses = new LinkedHashMap<>();
  @Nullable
  private Boolean _hasDeprecatedElements;
  @Nullable
  private final String _defaultReadOutsideTransaction;
  @Nullable
  private final String _defaultWriteOutsideTransaction;

  ComponentDescriptor( @Nonnull final String name,
                       @Nullable final Priority defaultPriority,
                       final boolean observable,
                       final boolean disposeNotifier,
                       final boolean disposeOnDeactivate,
                       final boolean sting,
                       final boolean requireEquals,
                       final boolean verify,
                       final boolean generateToString,
                       @Nonnull final TypeElement element,
                       @Nullable final String defaultReadOutsideTransaction,
                       @Nullable final String defaultWriteOutsideTransaction )
  {
    _name = Objects.requireNonNull( name );
    _defaultPriority = defaultPriority;
    _observable = observable;
    _disposeNotifier = disposeNotifier;
    _disposeOnDeactivate = disposeOnDeactivate;
    _sting = sting;
    _requireEquals = requireEquals;
    _verify = verify;
    _generateToString = generateToString;
    _element = Objects.requireNonNull( element );
    _defaultReadOutsideTransaction = defaultReadOutsideTransaction;
    _defaultWriteOutsideTransaction = defaultWriteOutsideTransaction;
  }

  @Nonnull
  ClassName getClassName()
  {
    return ClassName.get( getElement() );
  }

  boolean hasDeprecatedElements()
  {
    if ( null == _hasDeprecatedElements )
    {
      _hasDeprecatedElements = getPostConstructs().stream().anyMatch( this::isDeprecated ) ||
                               getPostDisposes().stream().anyMatch( this::isDeprecated ) ||
                               getPreDisposes().stream().anyMatch( this::isDeprecated ) ||
                               getComponentIdRefs().stream().anyMatch( this::isDeprecated ) ||
                               getComponentNameRefs().stream().anyMatch( this::isDeprecated ) ||
                               getComponentRefs().stream().anyMatch( this::isDeprecated ) ||
                               getComponentStateRefs().stream().anyMatch( e -> isDeprecated( e.getMethod() ) ) ||
                               getComponentTypeNameRefs().stream().anyMatch( this::isDeprecated ) ||
                               getContextRefs().stream().anyMatch( this::isDeprecated ) ||
                               isDeprecated( getComponentId() ) ||
                               getObservables().values()
                                 .stream()
                                 .anyMatch( e -> ( e.hasSetter() && isDeprecated( e.getSetter() ) ) ||
                                                 ( e.hasGetter() && isDeprecated( e.getGetter() ) ) ) ||
                               getMemoizes().values()
                                 .stream()
                                 .anyMatch( e -> ( e.hasMemoize() && isDeprecated( e.getMethod() ) ) ||
                                                 isDeprecated( e.getOnActivate() ) ||
                                                 isDeprecated( e.getOnDeactivate() ) ) ||
                               getObserverRefs().values()
                                 .stream()
                                 .flatMap( Collection::stream )
                                 .anyMatch( e -> isDeprecated( e.getMethod() ) ) ||
                               getDependencies().values()
                                 .stream()
                                 .anyMatch( e -> ( e.isMethodDependency() && isDeprecated( e.getMethod() ) ) ||
                                                 ( !e.isMethodDependency() &&
                                                   isDeprecated( e.getField() ) ) ) ||
                               getActions().values().stream().anyMatch( e -> isDeprecated( e.getAction() ) ) ||
                               getObserves().values()
                                 .stream()
                                 .anyMatch( e -> ( e.hasObserve() && isDeprecated( e.getMethod() ) ) ||
                                                 ( e.hasOnDepsChange() &&
                                                   isDeprecated( e.getOnDepsChange() ) ) );
    }
    return _hasDeprecatedElements;
  }

  void setIdRequired( final boolean idRequired )
  {
    _idRequired = idRequired;
  }

  boolean shouldVerify()
  {
    return _verify;
  }

  boolean isDisposeNotifier()
  {
    return _disposeNotifier;
  }

  private boolean isDeprecated( @Nullable final Element element )
  {
    return null != element && null != element.getAnnotation( Deprecated.class );
  }

  boolean defaultReadOutsideTransaction()
  {
    return "ENABLE".equals( _defaultReadOutsideTransaction );
  }

  boolean defaultWriteOutsideTransaction()
  {
    return "ENABLE".equals( _defaultWriteOutsideTransaction );
  }

  @Nullable
  String getDeclaredDefaultReadOutsideTransaction()
  {
    return _defaultReadOutsideTransaction;
  }

  @Nullable
  String getDeclaredDefaultWriteOutsideTransaction()
  {
    return _defaultWriteOutsideTransaction;
  }

  @Nonnull
  DeclaredType asDeclaredType()
  {
    return (DeclaredType) getElement().asType();
  }

  @Nonnull
  TypeElement getElement()
  {
    return _element;
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  @Nonnull
  Map<String, ReferenceDescriptor> getReferences()
  {
    return _references;
  }

  boolean hasReferenceWithInverseOutsidePackage( @Nonnull final ProcessingEnvironment processingEnv )
  {
    return getReferences()
      .values()
      .stream()
      .filter( ReferenceDescriptor::hasInverse )
      .anyMatch( reference -> {
        final TypeElement typeElement =
          (TypeElement) processingEnv.getTypeUtils().asElement( reference.getMethod().getReturnType() );
        return ElementsUtil.areTypesInDifferentPackage( typeElement, getElement() );
      } );
  }

  boolean hasInverseReferencedOutsidePackage()
  {
    return getInverses()
      .values()
      .stream()
      .anyMatch( inverse -> ElementsUtil.areTypesInDifferentPackage( inverse.getTargetType(), getElement() ) );
  }

  @Nonnull
  ReferenceDescriptor findOrCreateReference( @Nonnull final String name )
  {
    return getReferences().computeIfAbsent( name, n -> new ReferenceDescriptor( this, name ) );
  }

  @Nonnull
  ObservableDescriptor findOrCreateObservable( @Nonnull final String name )
  {
    return getObservables().computeIfAbsent( name, n -> new ObservableDescriptor( this, n ) );
  }

  @Nonnull
  Map<String, ObserveDescriptor> getObserves()
  {
    return _observes;
  }

  @Nonnull
  ObserveDescriptor findOrCreateObserve( @Nonnull final String name )
  {
    return getObserves().computeIfAbsent( name, n -> new ObserveDescriptor( this, n ) );
  }

  @Nonnull
  MemoizeContextParameterDescriptor findOrCreateMemoizeContextParameter( @Nonnull final String name )
  {
    return getMemoizeContextParameters().computeIfAbsent( name, n -> new MemoizeContextParameterDescriptor( this, n ) );
  }

  @Nonnull
  MemoizeDescriptor findOrCreateMemoize( @Nonnull final String name )
  {
    return getMemoizes().computeIfAbsent( name, n -> new MemoizeDescriptor( this, n ) );
  }

  @Nonnull
  List<ExecutableElement> getComponentIdRefs()
  {
    return _componentIdRefs;
  }

  boolean hasComponentIdMethod()
  {
    return null != getComponentId();
  }

  @Nonnull
  Map<String, ObservableDescriptor> getObservables()
  {
    return _observables;
  }

  @Nonnull
  Map<String, ObservableInitialDescriptor> getObservableInitials()
  {
    return _observableInitials;
  }

  boolean requiresSchedule()
  {
    return getObserves().values().stream().anyMatch( ObserveDescriptor::isInternalExecutor ) ||
           !getDependencies().isEmpty() ||
           getMemoizes().values().stream().anyMatch( MemoizeDescriptor::isKeepAlive );
  }

  void addCascadeDispose( @Nonnull final CascadeDisposeDescriptor descriptor )
  {
    getCascadeDisposes().put( descriptor.getElement(), descriptor );
  }

  @Nonnull
  Map<Element, CascadeDisposeDescriptor> getCascadeDisposes()
  {
    return _cascadeDisposes;
  }

  @SuppressWarnings( "BooleanMethodIsAlwaysInverted" )
  boolean isCascadeDisposeDefined( @Nonnull final Element element )
  {
    return getCascadeDisposes().containsKey( element );
  }

  @Nonnull
  Map<String, InverseDescriptor> getInverses()
  {
    return _inverses;
  }

  @Nonnull
  Map<Element, DependencyDescriptor> getDependencies()
  {
    return _dependencies;
  }

  void addDependency( @Nonnull final DependencyDescriptor dependencyDescriptor )
  {
    getDependencies().put( dependencyDescriptor.getElement(), dependencyDescriptor );
  }

  @SuppressWarnings( "BooleanMethodIsAlwaysInverted" )
  boolean isDependencyDefined( @Nonnull final Element element )
  {
    return getDependencies().containsKey( element );
  }

  boolean isClassType()
  {
    return ElementKind.CLASS == getElement().getKind();
  }

  boolean isInterfaceType()
  {
    return !isClassType();
  }

  boolean hasInverses()
  {
    return !getInverses().isEmpty();
  }

  boolean needsExplicitLink()
  {
    return getReferences().values().stream().anyMatch( r -> r.getLinkType().equals( "EXPLICIT" ) );
  }

  boolean hasInternalPostDispose()
  {
    return getPostDisposes().size() > 1;
  }

  boolean needsInternalDispose()
  {
    return !getObserves().isEmpty() || !getMemoizes().isEmpty() || !getObservables().isEmpty();
  }

  @Nonnull
  List<ObservableDescriptor> getInitializers()
  {
    return
      getObservables()
        .values()
        .stream()
        .filter( ObservableDescriptor::requireInitializer )
        .collect( Collectors.toList() );
  }

  boolean isStingEnabled()
  {
    return _sting;
  }

  boolean shouldGeneratedClassBePublic( @Nonnull final ProcessingEnvironment processingEnv )
  {
    return hasReferenceWithInverseOutsidePackage( processingEnv ) || hasInverseReferencedOutsidePackage();
  }

  @Nonnull
  ClassName getEnhancedClassName()
  {
    return GeneratorUtil.getGeneratedClassName( getElement(), "Arez_", "" );
  }

  @Nonnull
  String getPackageName()
  {
    return GeneratorUtil.getQualifiedPackageName( getElement() );
  }

  @Nonnull
  String getIdMethodName()
  {
    /*
     * Note that it is a deliberate choice to not use getArezId() as that will box Id which for the
     * "normal" case involves converting a long to a Long and it was decided that the slight increase in
     * code size was worth the slightly reduced memory pressure.
     */
    return null != getComponentId() ? getComponentId().getSimpleName().toString() : ComponentGenerator.ID_FIELD_NAME;
  }

  @Nonnull
  TypeName getIdType()
  {
    return null == _componentIdMethodType ?
           ComponentGenerator.DEFAULT_ID_TYPE :
           TypeName.get( _componentIdMethodType.getReturnType() );
  }

  @Nullable
  Priority getDefaultPriority()
  {
    return _defaultPriority;
  }

  @Nonnull
  List<ExecutableElement> getPostConstructs()
  {
    return _postConstructs;
  }

  @Nonnull
  List<ExecutableElement> getPostDisposes()
  {
    return _postDisposes;
  }

  @Nonnull
  List<ExecutableElement> getPreDisposes()
  {
    return _preDisposes;
  }

  @Nonnull
  List<ExecutableElement> getComponentNameRefs()
  {
    return _componentNameRefs;
  }

  @Nonnull
  List<ExecutableElement> getComponentRefs()
  {
    return _componentRefs;
  }

  @Nonnull
  List<ComponentStateRefDescriptor> getComponentStateRefs()
  {
    return _componentStateRefs;
  }

  @Nonnull
  List<ExecutableElement> getComponentTypeNameRefs()
  {
    return _componentTypeNameRefs;
  }

  @Nonnull
  List<ExecutableElement> getContextRefs()
  {
    return _contextRefs;
  }

  @Nonnull
  Map<String, List<CandidateMethod>> getObserverRefs()
  {
    return _observerRefs;
  }

  @Nonnull
  Map<String, ActionDescriptor> getActions()
  {
    return _actions;
  }

  @Nonnull
  Map<String, MemoizeContextParameterDescriptor> getMemoizeContextParameters()
  {
    return _memoizeContextParameters;
  }

  @Nonnull
  Map<String, MemoizeDescriptor> getMemoizes()
  {
    return _memoizes;
  }

  @Nullable
  public ExecutableElement getComponentId()
  {
    return _componentId;
  }

  public void setComponentId( @Nullable ExecutableElement componentId )
  {
    _componentId = componentId;
  }

  public void setComponentIdMethodType( @Nullable ExecutableType componentIdMethodType )
  {
    _componentIdMethodType = componentIdMethodType;
  }

  /**
   * Annotation that indicates whether equals/hashCode should be implemented. See arez.annotations.ArezComponent.requireEquals()
   */
  boolean isRequireEquals()
  {
    return _requireEquals;
  }

  boolean isGenerateToString()
  {
    return _generateToString;
  }

  boolean isIdRequired()
  {
    return _idRequired;
  }

  boolean isObservable()
  {
    return _observable;
  }

  boolean isDisposeOnDeactivate()
  {
    return _disposeOnDeactivate;
  }
}
