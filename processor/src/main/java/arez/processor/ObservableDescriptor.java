package arez.processor;

import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.WildcardTypeName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.realityforge.proton.ProcessorException;

/**
 * The class that represents the parsed state of ObservableValue properties on a @ArezComponent annotated class.
 */
final class ObservableDescriptor {

    private final ComponentDescriptor _component;

    private final String _name;

    private boolean _expectSetter;

    private String _readOutsideTransaction = "AUTODETECT";

    private String _writeOutsideTransaction = "AUTODETECT";

    private String _equalityComparator = Constants.EQUALITY_COMPARATOR_CLASSNAME;

    private String _getterDeclaredEqualityComparator = Constants.EQUALITY_COMPARATOR_CLASSNAME;

    private String _setterDeclaredEqualityComparator = Constants.EQUALITY_COMPARATOR_CLASSNAME;

    private boolean _setterAlwaysMutates;

    @Nullable
    private Boolean _initializer;

    @Nullable
    private ObservableInitialDescriptor _observableInitial;

    @Nullable
    private ExecutableElement _getter;

    @Nullable
    private ExecutableType _getterType;

    @Nullable
    private ExecutableElement _setter;

    @Nullable
    private ExecutableType _setterType;

    private final List<CandidateMethod> _refMethods = new ArrayList<>();

    @Nullable
    private DependencyDescriptor _dependencyDescriptor;

    @Nullable
    private ReferenceDescriptor _referenceDescriptor;

    @Nullable
    private InverseDescriptor _inverseDescriptor;

    @Nullable
    private CascadeDisposeDescriptor _cascadeDisposeDescriptor;

    @Nullable
    private AutoObserveDescriptor _autoObserveDescriptor;

    ObservableDescriptor(final ComponentDescriptor component, final String name) {
        _component = component;
        _name = name;
        setExpectSetter(true);
        setSetterAlwaysMutates(true);
    }

    ComponentDescriptor getComponent() {
        return _component;
    }

    String getName() {
        return _name;
    }

    boolean requireInitializer() {
        assert null != _initializer;
        return _initializer;
    }

    @Nullable
    Boolean getInitializer() {
        return _initializer;
    }

    void setInitializer(@Nullable final Boolean initializer) {
        _initializer = initializer;
    }

    boolean hasObservableInitial() {
        return null != _observableInitial;
    }

    void setObservableInitial(final ObservableInitialDescriptor observableInitial) throws ProcessorException {
        if (null != _observableInitial) {
            throw new ProcessorException(
                    "@ObservableInitial target duplicates existing initializer for observable " + "named " + getName(),
                    observableInitial.getElement());
        }
        _observableInitial = observableInitial;
    }

    ObservableInitialDescriptor getObservableInitial() {
        assert null != _observableInitial;
        return _observableInitial;
    }

    void setSetterAlwaysMutates(final boolean setterAlwaysMutates) {
        _setterAlwaysMutates = setterAlwaysMutates;
    }

    void setReadOutsideTransaction(final String readOutsideTransaction) {
        _readOutsideTransaction = readOutsideTransaction;
    }

    boolean canReadOutsideTransaction() {
        return "ENABLE".equals(_readOutsideTransaction)
                || ("AUTODETECT".equals(_readOutsideTransaction)
                        && getComponent().defaultReadOutsideTransaction());
    }

    boolean canWriteOutsideTransaction() {
        return "ENABLE".equals(_writeOutsideTransaction)
                || ("AUTODETECT".equals(_writeOutsideTransaction)
                        && getComponent().defaultWriteOutsideTransaction());
    }

    void setWriteOutsideTransaction(final String writeOutsideTransaction) {
        _writeOutsideTransaction = writeOutsideTransaction;
    }

    void setEqualityComparator(final String equalityComparator) {
        _equalityComparator = equalityComparator;
    }

    void setGetterDeclaredEqualityComparator(final String equalityComparator) {
        _getterDeclaredEqualityComparator = equalityComparator;
    }

    String getGetterDeclaredEqualityComparator() {
        return _getterDeclaredEqualityComparator;
    }

    void setSetterDeclaredEqualityComparator(final String equalityComparator) {
        _setterDeclaredEqualityComparator = equalityComparator;
    }

    String getSetterDeclaredEqualityComparator() {
        return _setterDeclaredEqualityComparator;
    }

    String getEqualityComparator() {
        return _equalityComparator;
    }

    boolean hasObjectsEqualsComparator() {
        return Constants.OBJECTS_EQUALS_COMPARATOR_CLASSNAME.equals(_equalityComparator);
    }

    boolean hasObjectsDeepEqualsComparator() {
        return Constants.OBJECTS_DEEP_EQUALS_COMPARATOR_CLASSNAME.equals(_equalityComparator);
    }

    boolean hasCustomEqualityComparator() {
        return !hasObjectsEqualsComparator() && !hasObjectsDeepEqualsComparator();
    }

    void setExpectSetter(final boolean expectSetter) {
        _expectSetter = expectSetter;
    }

    boolean expectSetter() {
        return _expectSetter;
    }

    List<CandidateMethod> getRefMethods() {
        return _refMethods;
    }

    void addRefMethod(final ExecutableElement method, final ExecutableType methodType) {
        _refMethods.add(new CandidateMethod(method, methodType));
    }

    boolean hasGetter() {
        return null != _getter;
    }

    ExecutableElement getGetter() throws ProcessorException {
        assert null != _getter;
        return _getter;
    }

    ExecutableType getGetterType() {
        assert null != _getterType;
        return _getterType;
    }

    void setGetter(final ExecutableElement getter, final ExecutableType methodType) {
        _getter = getter;
        _getterType = methodType;
    }

    boolean hasSetter() {
        return null != _setter;
    }

    ExecutableElement getSetter() throws ProcessorException {
        assert null != _setter;
        return _setter;
    }

    ExecutableType getSetterType() {
        assert null != _setterType;
        return _setterType;
    }

    void setSetter(final ExecutableElement setter, final ExecutableType methodType) {
        assert _expectSetter;
        _setter = setter;
        _setterType = methodType;
    }

    ExecutableElement getDefiner() {
        if (null != _getter) {
            return _getter;
        } else {
            assert null != _setter;
            return _setter;
        }
    }

    void setDependencyDescriptor(@Nullable final DependencyDescriptor dependencyDescriptor) {
        _dependencyDescriptor = dependencyDescriptor;
    }

    void setReferenceDescriptor(@Nullable final ReferenceDescriptor referenceDescriptor) {
        assert null == _inverseDescriptor;
        _referenceDescriptor = referenceDescriptor;
    }

    void setInverseDescriptor(@Nullable final InverseDescriptor inverseDescriptor) {
        assert null == getReferenceDescriptor();
        _inverseDescriptor = inverseDescriptor;
        setExpectSetter(false);
    }

    boolean isManyInverse() {
        return null != _inverseDescriptor && Multiplicity.MANY == _inverseDescriptor.getMultiplicity();
    }

    boolean isInverse() {
        return null != _inverseDescriptor;
    }

    @Nullable
    CascadeDisposeDescriptor getCascadeDisposeDescriptor() {
        return _cascadeDisposeDescriptor;
    }

    void setCascadeDisposeDescriptor(@NonNull final CascadeDisposeDescriptor cascadeDisposeDescriptor) {
        _cascadeDisposeDescriptor = cascadeDisposeDescriptor;
    }

    @Nullable
    AutoObserveDescriptor getAutoObserveDescriptor() {
        return _autoObserveDescriptor;
    }

    void setAutoObserveDescriptor(@NonNull final AutoObserveDescriptor autoObserveDescriptor) {
        _autoObserveDescriptor = autoObserveDescriptor;
    }

    String getDataFieldName() {
        return ComponentGenerator.OBSERVABLE_DATA_FIELD_PREFIX + getName();
    }

    String getCollectionCacheDataFieldName() {
        return ComponentGenerator.OBSERVABLE_DATA_FIELD_PREFIX + "$$cache$$_" + getName();
    }

    String getFieldName() {
        return ComponentGenerator.FIELD_PREFIX + getName();
    }

    String getEqualityComparatorFieldName() {
        return ComponentGenerator.FIELD_PREFIX + "equalityComparator_" + getName();
    }

    boolean isAbstract() {
        return getGetter().getModifiers().contains(Modifier.ABSTRACT);
    }

    boolean shouldGenerateUnmodifiableCollectionVariant() {
        return (hasSetter() || null != _inverseDescriptor) && isCollectionType();
    }

    private boolean isCollectionType() {
        return isGetterUnparameterizedReturnType(Collection.class)
                || isGetterUnparameterizedReturnType(Set.class)
                || isGetterUnparameterizedReturnType(List.class)
                || isGetterUnparameterizedReturnType(Map.class);
    }

    private boolean isGetterUnparameterizedReturnType(final Class<?> type) {
        final TypeMirror returnType = getGetterType().getReturnType();
        final TypeKind kind = returnType.getKind();
        if (TypeKind.DECLARED != kind) {
            return false;
        } else {
            final var declaredType = (DeclaredType) returnType;
            final var element = (TypeElement) declaredType.asElement();
            return element.getQualifiedName().toString().equals(type.getName());
        }
    }

    boolean isGetterNonnull() {
        return Nullability.isNonNull(
                getGetter(), getGetterType().getReturnType(), getComponent().getElement());
    }

    boolean isSetterNonnull() {
        return hasSetter()
                && Nullability.isNonNull(
                        getSetter().getParameters().get(0),
                        getSetterType().getParameterTypes().get(0),
                        getComponent().getElement());
    }

    void validate() {
        if (!expectSetter()) {
            if (!doesSetterAlwaysMutate()) {
                throw new ProcessorException(
                        "@Observable target defines expectSetter = false "
                                + "setterAlwaysMutates = false but this is an invalid configuration.",
                        getGetter());
            }
            if (_refMethods.isEmpty() && null == _inverseDescriptor) {
                throw new ProcessorException(
                        "@Observable target defines expectSetter = false but there is no ref "
                                + "method for observable and thus never possible to report it as changed "
                                + "and thus should not be observable.",
                        getGetter());
            }
        }

        for (final CandidateMethod refMethod : _refMethods) {
            final TypeName typeName = TypeName.get(refMethod.getMethodType().getReturnType());
            if (typeName instanceof final ParameterizedTypeName parameterizedTypeName) {
                final TypeName expectedType =
                        parameterizedTypeName.typeArguments().get(0);
                if (!(expectedType instanceof WildcardTypeName)) {
                    assert null != _getterType;
                    final TypeName actual = TypeName.get(_getterType.getReturnType());
                    if (!actual.box().toString().equals(expectedType.toString())) {
                        throw new ProcessorException(
                                "@ObservableValueRef target has a type parameter of " + expectedType
                                        + " but @Observable method returns type of " + actual,
                                refMethod.getMethod());
                    }
                }
            }
        }
        if (isAbstract()) {
            if (!getGetter().getThrownTypes().isEmpty()) {
                throw new ProcessorException(
                        "@Observable property is abstract but the getter declares an exception.", getSetter());
            }
            if (!hasSetter()) {
                if (null == _inverseDescriptor) {
                    throw new ProcessorException(
                            "@Observable target defines expectSetter = false but is abstract. This "
                                    + "is not compatible as there is no opportunity for the processor to "
                                    + "generate the setter.",
                            getGetter());
                }
            } else {
                final ExecutableElement setter = getSetter();
                if (!setter.getModifiers().contains(Modifier.ABSTRACT)) {
                    throw new ProcessorException(
                            "@Observable property defines an abstract getter but a concrete setter. "
                                    + "Both getter and setter must be concrete or both must be abstract.",
                            getGetter());
                }

                if (!setter.getThrownTypes().isEmpty()) {
                    throw new ProcessorException(
                            "@Observable property is abstract but the setter declares an exception.", getSetter());
                }
            }
            if (!doesSetterAlwaysMutate()) {
                throw new ProcessorException(
                        "@Observable target defines setterAlwaysMutates = false but but has "
                                + "defined abstract getters and setters.",
                        getGetter());
            }
        } else if (hasSetter() && getSetter().getModifiers().contains(Modifier.ABSTRACT)) {
            throw new ProcessorException(
                    "@Observable property defines an abstract setter but a concrete getter. "
                            + "Both getter and setter must be concrete or both must be abstract.",
                    getSetter());
        }
        if (expectSetter() && !getSetterType().getTypeVariables().isEmpty()) {
            throw new ProcessorException(
                    "@Observable target defines type variables. Method level type parameters "
                            + "are not supported for observable values.",
                    getSetter());
        }
        if (!getGetterType().getTypeVariables().isEmpty()) {
            throw new ProcessorException(
                    "@Observable target defines type variables. Method level type parameters "
                            + "are not supported for observable values.",
                    getGetter());
        }
    }

    @Nullable
    DependencyDescriptor getDependencyDescriptor() {
        return _dependencyDescriptor;
    }

    @Nullable
    ReferenceDescriptor getReferenceDescriptor() {
        return _referenceDescriptor;
    }

    boolean doesSetterAlwaysMutate() {
        return _setterAlwaysMutates;
    }
}
