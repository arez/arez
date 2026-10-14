package arez.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import org.jspecify.annotations.Nullable;
import org.realityforge.proton.ProcessorException;

/**
 * Declaration of an inverse.
 */
final class InverseDescriptor {

    private final ComponentDescriptor _component;

    private final String _name;

    private final List<ExecutableElement> _postInverseAddHooks = new ArrayList<>();

    private final List<ExecutableElement> _preInverseRemoveHooks = new ArrayList<>();

    @Nullable
    private ObservableDescriptor _observable;

    @Nullable
    private String _referenceName;

    @Nullable
    private Multiplicity _multiplicity;

    @Nullable
    private TypeElement _targetType;

    @Nullable
    private String _otherName;

    InverseDescriptor(final ComponentDescriptor component, final String name) {
        _component = Objects.requireNonNull(component);
        _name = Objects.requireNonNull(name);
    }

    ComponentDescriptor getComponent() {
        return _component;
    }

    String getName() {
        return _name;
    }

    boolean hasObservable() {
        return null != _observable;
    }

    ObservableDescriptor getObservable() {
        assert null != _observable;
        return _observable;
    }

    String getReferenceName() {
        assert null != _referenceName;
        return _referenceName;
    }

    Multiplicity getMultiplicity() {
        assert null != _multiplicity;
        return _multiplicity;
    }

    TypeElement getTargetType() {
        assert null != _targetType;
        return _targetType;
    }

    String getOtherName() {
        assert null != _otherName;
        return _otherName;
    }

    void setInverse(
            final ObservableDescriptor observable,
            final String referenceName,
            final Multiplicity multiplicity,
            final TypeElement targetType,
            final String otherName) {
        assert null == _observable;
        _observable = Objects.requireNonNull(observable);
        _referenceName = Objects.requireNonNull(referenceName);
        _multiplicity = Objects.requireNonNull(multiplicity);
        _targetType = Objects.requireNonNull(targetType);
        _observable.setInverseDescriptor(this);
        _otherName = Objects.requireNonNull(otherName);
    }

    void addPostInverseAddHook(final ExecutableElement method) {
        _postInverseAddHooks.add(method);
    }

    void addPreInverseRemoveHook(final ExecutableElement method) {
        _preInverseRemoveHooks.add(method);
    }

    List<ExecutableElement> getPostInverseAddHooks() {
        return _postInverseAddHooks;
    }

    List<ExecutableElement> getPreInverseRemoveHooks() {
        return _preInverseRemoveHooks;
    }

    void validate(final ProcessingEnvironment processingEnv) {
        if (null == _observable && !getPreInverseRemoveHooks().isEmpty()) {
            throw new ProcessorException(
                    "@PreInverseRemove target with name '" + getName() + "' is not associated "
                            + "an @Inverse annotated method with the same name",
                    getPreInverseRemoveHooks().get(0));
        } else if (null == _observable && !getPostInverseAddHooks().isEmpty()) {
            throw new ProcessorException(
                    "@PostInverseAdd target with name '" + getName() + "' is not associated "
                            + "an @Inverse annotated method with the same name",
                    getPostInverseAddHooks().get(0));
        } else {
            final ObservableDescriptor observable = getObservable();
            if (observable.requireInitializer()) {
                throw new ProcessorException(
                        "@Inverse target also specifies @Observable(initializer=ENABLE) but "
                                + "it is not valid to define an initializer for an inverse.",
                        observable.getGetter());
            }
            for (final ExecutableElement hook : getPostInverseAddHooks()) {
                final TypeMirror paramType =
                        ((ExecutableType) hook.asType()).getParameterTypes().get(0);
                final TypeMirror targetType = getTargetType().asType();
                if (!processingEnv.getTypeUtils().isSameType(paramType, targetType)) {
                    throw new ProcessorException(
                            "@PostInverseAdd target has a parameter that is not the expected type. " + "Actual type: "
                                    + paramType + " Expected Type: " + targetType,
                            hook);
                }
            }
            for (final ExecutableElement hook : getPreInverseRemoveHooks()) {
                final TypeMirror paramType =
                        ((ExecutableType) hook.asType()).getParameterTypes().get(0);
                final TypeMirror targetType = getTargetType().asType();
                if (!processingEnv.getTypeUtils().isSameType(paramType, targetType)) {
                    throw new ProcessorException(
                            "@PreInverseRemove target has a parameter that is not the expected type. " + "Actual type: "
                                    + paramType + " Expected Type: " + targetType,
                            hook);
                }
            }
        }
    }
}
