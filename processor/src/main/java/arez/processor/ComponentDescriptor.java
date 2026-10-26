package arez.processor;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import org.jspecify.annotations.Nullable;
import org.realityforge.proton.ElementsUtil;
import org.realityforge.proton.GeneratorUtil;

/**
 * The class that represents the parsed state of ArezComponent annotated class.
 */
final class ComponentDescriptor {

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

    private final TypeElement _element;

    private final List<ExecutableElement> _postConstructs = new ArrayList<>();

    private final List<ExecutableElement> _postDisposes = new ArrayList<>();

    private final List<ExecutableElement> _preDisposes = new ArrayList<>();

    @Nullable
    private ExecutableElement _componentId;

    @Nullable
    private ExecutableType _componentIdMethodType;

    private final List<ExecutableElement> _componentIdRefs = new ArrayList<>();

    private final List<ExecutableElement> _componentNameRefs = new ArrayList<>();

    private final List<ExecutableElement> _componentRefs = new ArrayList<>();

    private final List<ComponentStateRefDescriptor> _componentStateRefs = new ArrayList<>();

    private final List<ExecutableElement> _componentTypeNameRefs = new ArrayList<>();

    private final List<ExecutableElement> _contextRefs = new ArrayList<>();

    private final Map<String, List<CandidateMethod>> _observerRefs = new LinkedHashMap<>();

    private final Map<String, ObservableDescriptor> _observables = new LinkedHashMap<>();

    private final Map<String, ObservableInitialDescriptor> _observableInitials = new LinkedHashMap<>();

    private final Map<String, ActionDescriptor> _actions = new LinkedHashMap<>();

    private final List<RequiresTransactionDescriptor> _requiresTransactions = new ArrayList<>();

    private final Map<String, MemoizeContextParameterDescriptor> _memoizeContextParameters = new LinkedHashMap<>();

    private final Map<String, MemoizeDescriptor> _memoizes = new LinkedHashMap<>();

    private final Map<String, ObserveDescriptor> _observes = new LinkedHashMap<>();

    private final Map<Element, DependencyDescriptor> _dependencies = new LinkedHashMap<>();

    private final Map<Element, CascadeDisposeDescriptor> _cascadeDisposes = new LinkedHashMap<>();

    private final Map<Element, AutoObserveDescriptor> _autoObserves = new LinkedHashMap<>();

    private final Map<String, ReferenceDescriptor> _references = new LinkedHashMap<>();

    private final Map<String, InverseDescriptor> _inverses = new LinkedHashMap<>();

    @Nullable
    private Boolean _hasDeprecatedElements;

    @Nullable
    private final String _defaultReadOutsideTransaction;

    @Nullable
    private final String _defaultWriteOutsideTransaction;

    @Nullable
    private final String _defaultSkipIfDisposed;

    ComponentDescriptor(
            final String name,
            @Nullable final Priority defaultPriority,
            final boolean observable,
            final boolean disposeNotifier,
            final boolean disposeOnDeactivate,
            final boolean sting,
            final boolean requireEquals,
            final boolean verify,
            final boolean generateToString,
            final TypeElement element,
            @Nullable final String defaultReadOutsideTransaction,
            @Nullable final String defaultWriteOutsideTransaction,
            @Nullable final String defaultSkipIfDisposed) {
        _name = name;
        _defaultPriority = defaultPriority;
        _observable = observable;
        _disposeNotifier = disposeNotifier;
        _disposeOnDeactivate = disposeOnDeactivate;
        _sting = sting;
        _requireEquals = requireEquals;
        _verify = verify;
        _generateToString = generateToString;
        _element = element;
        _defaultReadOutsideTransaction = defaultReadOutsideTransaction;
        _defaultWriteOutsideTransaction = defaultWriteOutsideTransaction;
        _defaultSkipIfDisposed = defaultSkipIfDisposed;
    }

    ClassName getClassName() {
        return ClassName.get(getElement());
    }

    boolean hasDeprecatedElements() {
        if (null == _hasDeprecatedElements) {
            _hasDeprecatedElements = getPostConstructs().stream().anyMatch(this::isDeprecated)
                    || getPostDisposes().stream().anyMatch(this::isDeprecated)
                    || getPreDisposes().stream().anyMatch(this::isDeprecated)
                    || getComponentIdRefs().stream().anyMatch(this::isDeprecated)
                    || getComponentNameRefs().stream().anyMatch(this::isDeprecated)
                    || getComponentRefs().stream().anyMatch(this::isDeprecated)
                    || getComponentStateRefs().stream().anyMatch(e -> isDeprecated(e.getMethod()))
                    || getComponentTypeNameRefs().stream().anyMatch(this::isDeprecated)
                    || getContextRefs().stream().anyMatch(this::isDeprecated)
                    || isDeprecated(getComponentId())
                    || getObservables().values().stream()
                            .anyMatch(e -> (e.hasSetter() && isDeprecated(e.getSetter()))
                                    || (e.hasGetter() && isDeprecated(e.getGetter())))
                    || getMemoizes().values().stream()
                            .anyMatch(e -> (e.hasMemoize() && isDeprecated(e.getMethod()))
                                    || isDeprecated(e.getOnActivate())
                                    || isDeprecated(e.getOnDeactivate()))
                    || getObserverRefs().values().stream()
                            .flatMap(Collection::stream)
                            .anyMatch(e -> isDeprecated(e.getMethod()))
                    || getDependencies().values().stream()
                            .anyMatch(e -> (e.isMethodDependency() && isDeprecated(e.getMethod()))
                                    || (!e.isMethodDependency() && isDeprecated(e.getField())))
                    || getAutoObserves().values().stream()
                            .anyMatch(e -> (null != e.getMethod() && isDeprecated(e.getMethod()))
                                    || (null != e.getField() && isDeprecated(e.getField())))
                    || getActions().values().stream().anyMatch(e -> isDeprecated(e.getAction()))
                    || getRequiresTransactions().stream().anyMatch(e -> isDeprecated(e.getMethod()))
                    || getObserves().values().stream()
                            .anyMatch(e -> (e.hasObserve() && isDeprecated(e.getMethod()))
                                    || e.getOnDepsChanges().stream().anyMatch(this::isDeprecated));
        }
        return _hasDeprecatedElements;
    }

    void setIdRequired(final boolean idRequired) {
        _idRequired = idRequired;
    }

    boolean shouldVerify() {
        return _verify;
    }

    boolean isDisposeNotifier() {
        return _disposeNotifier;
    }

    private boolean isDeprecated(@Nullable final Element element) {
        return null != element && null != element.getAnnotation(Deprecated.class);
    }

    boolean defaultReadOutsideTransaction() {
        return "ENABLE".equals(_defaultReadOutsideTransaction);
    }

    boolean defaultWriteOutsideTransaction() {
        return "ENABLE".equals(_defaultWriteOutsideTransaction);
    }

    boolean defaultSkipIfDisposed() {
        return "ENABLE".equals(_defaultSkipIfDisposed);
    }

    @Nullable
    String getDeclaredDefaultReadOutsideTransaction() {
        return _defaultReadOutsideTransaction;
    }

    @Nullable
    String getDeclaredDefaultWriteOutsideTransaction() {
        return _defaultWriteOutsideTransaction;
    }

    @Nullable
    String getDeclaredDefaultSkipIfDisposed() {
        return _defaultSkipIfDisposed;
    }

    DeclaredType asDeclaredType() {
        return (DeclaredType) getElement().asType();
    }

    TypeElement getElement() {
        return _element;
    }

    String getName() {
        return _name;
    }

    Map<String, ReferenceDescriptor> getReferences() {
        return _references;
    }

    boolean hasReferenceWithInverseOutsidePackage(final ProcessingEnvironment processingEnv) {
        return getReferences().values().stream()
                .filter(ReferenceDescriptor::hasInverse)
                .anyMatch(reference -> {
                    final var typeElement = (TypeElement) processingEnv
                            .getTypeUtils()
                            .asElement(reference.getMethod().getReturnType());
                    return ElementsUtil.areTypesInDifferentPackage(typeElement, getElement());
                });
    }

    boolean hasInverseReferencedOutsidePackage() {
        return getInverses().values().stream()
                .anyMatch(inverse -> ElementsUtil.areTypesInDifferentPackage(inverse.getTargetType(), getElement()));
    }

    ReferenceDescriptor findOrCreateReference(final String name) {
        return getReferences().computeIfAbsent(name, n -> new ReferenceDescriptor(this, name));
    }

    ObservableDescriptor findOrCreateObservable(final String name) {
        return getObservables().computeIfAbsent(name, n -> new ObservableDescriptor(this, n));
    }

    Map<String, ObserveDescriptor> getObserves() {
        return _observes;
    }

    ObserveDescriptor findOrCreateObserve(final String name) {
        return getObserves().computeIfAbsent(name, n -> new ObserveDescriptor(this, n));
    }

    MemoizeContextParameterDescriptor findOrCreateMemoizeContextParameter(final String name) {
        return getMemoizeContextParameters().computeIfAbsent(name, n -> new MemoizeContextParameterDescriptor(this, n));
    }

    MemoizeDescriptor findOrCreateMemoize(final String name) {
        return getMemoizes().computeIfAbsent(name, n -> new MemoizeDescriptor(this, n));
    }

    List<ExecutableElement> getComponentIdRefs() {
        return _componentIdRefs;
    }

    boolean hasComponentIdMethod() {
        return null != getComponentId();
    }

    Map<String, ObservableDescriptor> getObservables() {
        return _observables;
    }

    Map<String, ObservableInitialDescriptor> getObservableInitials() {
        return _observableInitials;
    }

    boolean requiresSchedule() {
        return getObserves().values().stream().anyMatch(ObserveDescriptor::isInternalExecutor)
                || !getAutoObserves().isEmpty()
                || !getDependencies().isEmpty()
                || getMemoizes().values().stream().anyMatch(MemoizeDescriptor::isKeepAlive);
    }

    void addCascadeDispose(final CascadeDisposeDescriptor descriptor) {
        getCascadeDisposes().put(descriptor.getElement(), descriptor);
    }

    Map<Element, CascadeDisposeDescriptor> getCascadeDisposes() {
        return _cascadeDisposes;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isCascadeDisposeDefined(final Element element) {
        return getCascadeDisposes().containsKey(element);
    }

    void addAutoObserve(final AutoObserveDescriptor descriptor) {
        getAutoObserves().put(descriptor.getElement(), descriptor);
    }

    Map<Element, AutoObserveDescriptor> getAutoObserves() {
        return _autoObserves;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isAutoObserveDefined(final Element element) {
        return getAutoObserves().containsKey(element);
    }

    Map<String, InverseDescriptor> getInverses() {
        return _inverses;
    }

    Map<Element, DependencyDescriptor> getDependencies() {
        return _dependencies;
    }

    void addDependency(final DependencyDescriptor dependencyDescriptor) {
        getDependencies().put(dependencyDescriptor.getElement(), dependencyDescriptor);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isDependencyDefined(final Element element) {
        return getDependencies().containsKey(element);
    }

    boolean isClassType() {
        return ElementKind.CLASS == getElement().getKind();
    }

    boolean isInterfaceType() {
        return !isClassType();
    }

    boolean hasInverses() {
        return !getInverses().isEmpty();
    }

    boolean needsExplicitLink() {
        return getReferences().values().stream().anyMatch(r -> r.getLinkType().equals("EXPLICIT"));
    }

    boolean hasInternalPostDispose() {
        return getPostDisposes().size() > 1;
    }

    boolean needsInternalDispose() {
        return !getAutoObserves().isEmpty()
                || !getObserves().isEmpty()
                || !getMemoizes().isEmpty()
                || !getObservables().isEmpty();
    }

    List<ObservableDescriptor> getInitializers() {
        return getObservables().values().stream()
                .filter(ObservableDescriptor::requireInitializer)
                .collect(Collectors.toList());
    }

    boolean isStingEnabled() {
        return _sting;
    }

    ClassName getEnhancedClassName() {
        return GeneratorUtil.getGeneratedClassName(getElement(), "Arez_", "");
    }

    String getPackageName() {
        return GeneratorUtil.getQualifiedPackageName(getElement());
    }

    String getIdMethodName() {
        /*
         * Note that it is a deliberate choice to not use getArezId() as that will box Id which for the
         * "normal" case involves converting a long to a Long and it was decided that the slight increase in
         * code size was worth the slightly reduced memory pressure.
         */
        return null != getComponentId()
                ? getComponentId().getSimpleName().toString()
                : ComponentGenerator.ID_FIELD_NAME;
    }

    TypeName getIdType() {
        return null == _componentIdMethodType
                ? ComponentGenerator.DEFAULT_ID_TYPE
                : GeneratorUtil.getTypeName(_componentIdMethodType.getReturnType());
    }

    @Nullable
    Priority getDefaultPriority() {
        return _defaultPriority;
    }

    List<ExecutableElement> getPostConstructs() {
        return _postConstructs;
    }

    List<ExecutableElement> getPostDisposes() {
        return _postDisposes;
    }

    List<ExecutableElement> getPreDisposes() {
        return _preDisposes;
    }

    List<ExecutableElement> getComponentNameRefs() {
        return _componentNameRefs;
    }

    List<ExecutableElement> getComponentRefs() {
        return _componentRefs;
    }

    List<ComponentStateRefDescriptor> getComponentStateRefs() {
        return _componentStateRefs;
    }

    List<ExecutableElement> getComponentTypeNameRefs() {
        return _componentTypeNameRefs;
    }

    List<ExecutableElement> getContextRefs() {
        return _contextRefs;
    }

    Map<String, List<CandidateMethod>> getObserverRefs() {
        return _observerRefs;
    }

    Map<String, ActionDescriptor> getActions() {
        return _actions;
    }

    List<RequiresTransactionDescriptor> getRequiresTransactions() {
        return _requiresTransactions;
    }

    Map<String, MemoizeContextParameterDescriptor> getMemoizeContextParameters() {
        return _memoizeContextParameters;
    }

    Map<String, MemoizeDescriptor> getMemoizes() {
        return _memoizes;
    }

    @Nullable
    public ExecutableElement getComponentId() {
        return _componentId;
    }

    public void setComponentId(@Nullable ExecutableElement componentId) {
        _componentId = componentId;
    }

    public void setComponentIdMethodType(@Nullable ExecutableType componentIdMethodType) {
        _componentIdMethodType = componentIdMethodType;
    }

    /**
     * Annotation that indicates whether equals/hashCode should be implemented. See arez.annotations.ArezComponent.requireEquals()
     */
    boolean isRequireEquals() {
        return _requireEquals;
    }

    boolean isGenerateToString() {
        return _generateToString;
    }

    boolean isIdRequired() {
        return _idRequired;
    }

    boolean isObservable() {
        return _observable;
    }

    boolean isDisposeOnDeactivate() {
        return _disposeOnDeactivate;
    }
}
