package arez.processor;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.realityforge.proton.GeneratorUtil;
import org.realityforge.proton.ProcessorException;

/**
 * Declaration of a reference.
 */
final class ReferenceDescriptor {

    private final ComponentDescriptor _component;

    private final String _name;

    @Nullable
    private ExecutableElement _method;

    @Nullable
    private ExecutableType _methodType;

    @Nullable
    private String _linkType;

    @Nullable
    private ObservableDescriptor _observable;

    @Nullable
    private ExecutableElement _idMethod;

    @Nullable
    private TypeMirror _idType;

    @Nullable
    private String _inverseName;

    @Nullable
    private Multiplicity _inverseMultiplicity;

    @Nullable
    private CascadeDisposeDescriptor _cascadeDisposeDescriptor;

    @Nullable
    private AutoObserveDescriptor _autoObserveDescriptor;

    ReferenceDescriptor(final ComponentDescriptor component, final String name) {
        _component = component;
        _name = name;
    }

    void setIdMethod(final ExecutableElement method, final TypeMirror type) {
        assert null == _idMethod;
        _idMethod = method;
        _idType = type;
    }

    void setObservable(final ObservableDescriptor observable) {
        setIdMethod(observable.getGetter(), observable.getGetterType().getReturnType());
        assert null == _observable;
        _observable = observable;
        _observable.setReferenceDescriptor(this);
    }

    void setMethod(
            final ExecutableElement method,
            final ExecutableType methodType,
            final String linkType,
            @Nullable final String inverseName,
            @Nullable final Multiplicity inverseMultiplicity) {
        assert null == _method;
        assert null == _methodType;
        assert null == _linkType;
        assert null == _inverseName;
        assert null == _inverseMultiplicity;
        assert (null == inverseName && null == inverseMultiplicity)
                || (null != inverseName && null != inverseMultiplicity);
        _method = method;
        _methodType = methodType;
        _linkType = linkType;
        _inverseName = inverseName;
        _inverseMultiplicity = inverseMultiplicity;
    }

    String getLinkType() {
        assert null != _linkType;
        return _linkType;
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

    ComponentDescriptor getComponent() {
        return _component;
    }

    String getName() {
        return _name;
    }

    ExecutableType getMethodType() {
        assert null != _methodType;
        return _methodType;
    }

    @Nullable
    ObservableDescriptor getObservable() {
        return _observable;
    }

    boolean hasMethod() {
        return null != _method;
    }

    ExecutableElement getMethod() {
        assert null != _method;
        return _method;
    }

    ExecutableElement getIdMethod() {
        assert null != _idMethod;
        return _idMethod;
    }

    TypeMirror getIdType() {
        assert null != _idType;
        return _idType;
    }

    String getLinkMethodName() {
        return ComponentGenerator.getLinkMethodName(getName());
    }

    String getDelinkMethodName() {
        return ComponentGenerator.getDelinkMethodName(getName());
    }

    String getFieldName() {
        return ComponentGenerator.REFERENCE_FIELD_PREFIX + _name;
    }

    boolean hasInverse() {
        return null != _inverseName;
    }

    String getInverseName() {
        assert null != _inverseName;
        return _inverseName;
    }

    Multiplicity getInverseMultiplicity() {
        assert null != _inverseMultiplicity;
        return _inverseMultiplicity;
    }

    boolean isNullable() {
        return !getIdType().getKind().isPrimitive()
                && !Nullability.isNonNull(
                        getIdMethod(), getIdType(), getComponent().getElement());
    }

    ClassName getArezClassName() {
        final var other = (ClassName) TypeName.get(getMethod().getReturnType());
        return GeneratorUtil.getGeneratedClassName(other, "Arez_", "");
    }

    void validate() throws ProcessorException {
        if (null == _idMethod) {
            assert null != _method;
            throw new ProcessorException("@Reference exists but there is no corresponding @ReferenceId", _method);
        } else if (null == _method) {
            throw new ProcessorException("@ReferenceId exists but there is no corresponding @Reference", _idMethod);
        } else if (null != _observable && !_observable.hasSetter()) {
            throw new ProcessorException(
                    "@ReferenceId added to @Observable method but expectSetter = false on "
                            + "property which is not compatible with @ReferenceId",
                    _idMethod);
        }
    }
}
