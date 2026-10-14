package arez.processor;

import java.util.Objects;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.jspecify.annotations.Nullable;
import org.realityforge.proton.ProcessorException;

/**
 * Declaration of a component's dependency.
 * This dependency can be an <code>@Observable</code> method, a un-annotated getter method or a field.
 */
final class DependencyDescriptor {

    private final ComponentDescriptor _component;

    @Nullable
    private final ExecutableElement _method;

    @Nullable
    private final VariableElement _field;

    private final boolean _cascade;

    @Nullable
    private ObservableDescriptor _observable;

    DependencyDescriptor(final ComponentDescriptor component, final ExecutableElement method, final boolean cascade) {
        _component = Objects.requireNonNull(component);
        _method = Objects.requireNonNull(method);
        _field = null;
        _cascade = cascade;
    }

    DependencyDescriptor(final ComponentDescriptor component, final VariableElement field) {
        _component = Objects.requireNonNull(component);
        _method = null;
        _field = Objects.requireNonNull(field);
        _cascade = true;
    }

    ComponentDescriptor getComponent() {
        return _component;
    }

    boolean needsKey() {
        return _component.getDependencies().size() > 1 && hasNoObservable();
    }

    String getKeyName() {
        return getElement().getSimpleName().toString();
    }

    boolean shouldCascadeDispose() {
        return _cascade;
    }

    boolean isMethodDependency() {
        return null != _method;
    }

    Element getElement() {
        return null != _method ? _method : Objects.requireNonNull(_field);
    }

    ExecutableElement getMethod() {
        assert null != _method;
        return _method;
    }

    VariableElement getField() {
        assert null != _field;
        return _field;
    }

    void setObservable(final ObservableDescriptor observable) {
        _observable = Objects.requireNonNull(observable);
        _observable.setDependencyDescriptor(this);
    }

    boolean hasNoObservable() {
        return null == _observable;
    }

    ObservableDescriptor getObservable() {
        assert null != _observable;
        return _observable;
    }

    void validate() {
        assert hasNoObservable() || isMethodDependency();
        if (!shouldCascadeDispose() && isMethodDependency()) {
            if (hasNoObservable()) {
                throw new ProcessorException(
                        "@ComponentDependency target defined an action of 'SET_NULL' but the "
                                + "dependency is not an observable so the annotation processor does not "
                                + "know how to set the value to null.",
                        getMethod());
            } else if (!getObservable().hasSetter()) {
                throw new ProcessorException(
                        "@ComponentDependency target defined an action of 'SET_NULL' but the "
                                + "dependency is an observable with no setter defined so the annotation "
                                + "processor does not know how to set the value to null.",
                        getMethod());
            } else if (Nullability.isNonNull(
                    getObservable().getSetter().getParameters().get(0))) {
                throw new ProcessorException(
                        "@ComponentDependency target defined an action of 'SET_NULL' but the "
                                + "setter has a non-null JSpecify contract.",
                        getMethod());
            }
        }
    }
}
