package arez.processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import org.jspecify.annotations.Nullable;
import org.realityforge.proton.ProcessorException;

/**
 * Declaration of a method or field that must be cascade disposed.
 */
final class CascadeDisposeDescriptor {
    @Nullable
    private final ExecutableElement _method;

    @Nullable
    private final VariableElement _field;

    @Nullable
    private ObservableDescriptor _observable;

    @Nullable
    private ReferenceDescriptor _reference;

    CascadeDisposeDescriptor(final VariableElement field) {
        _method = null;
        _field = field;
    }

    CascadeDisposeDescriptor(final ExecutableElement method, @Nullable final ObservableDescriptor observable) {
        _method = method;
        _field = null;
        if (null != observable) {
            setObservable(observable);
        }
    }

    Element getElement() {
        final ExecutableElement method = getMethod();
        if (null != method) {
            return method;
        } else {
            final VariableElement field = getField();
            assert null != field;
            return field;
        }
    }

    void setObservable(final ObservableDescriptor observable) {
        _observable = observable;
        observable.setCascadeDisposeDescriptor(this);
    }

    void setReference(final ReferenceDescriptor reference) {
        _reference = reference;
        reference.setCascadeDisposeDescriptor(this);
    }

    void validate() {
        if (null != getMethod()) {
            if (null == getObservable() && null == getReference()) {
                if (getMethod().getModifiers().contains(Modifier.ABSTRACT)) {
                    throw new ProcessorException(
                            "@CascadeDispose target must not be abstract unless the method is "
                                    + "also annotated with the @Observable or @Reference annotation.",
                            getMethod());
                }
            } else {
                if (!getMethod().getModifiers().contains(Modifier.ABSTRACT)) {
                    throw new ProcessorException(
                            "@CascadeDispose target must be abstract if the method is "
                                    + "also annotated with the @Observable or @Reference annotation.",
                            getMethod());
                }
            }
        }
    }

    @Nullable
    ExecutableElement getMethod() {
        return _method;
    }

    @Nullable
    VariableElement getField() {
        return _field;
    }

    @Nullable
    ObservableDescriptor getObservable() {
        return _observable;
    }

    @Nullable
    ReferenceDescriptor getReference() {
        return _reference;
    }
}
