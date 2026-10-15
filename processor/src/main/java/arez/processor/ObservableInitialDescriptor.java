package arez.processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import org.jspecify.annotations.Nullable;

/**
 * The class that represents the parsed state of @ObservableInitial annotated elements.
 */
final class ObservableInitialDescriptor {

    private final String _name;

    private final Element _element;

    @Nullable
    private final VariableElement _field;

    @Nullable
    private final ExecutableElement _method;

    @Nullable
    private final ExecutableType _methodType;

    ObservableInitialDescriptor(final String name, final VariableElement field) {
        _name = name;
        _element = field;
        _field = field;
        _method = null;
        _methodType = null;
    }

    ObservableInitialDescriptor(final String name, final ExecutableElement method, final ExecutableType methodType) {
        _name = name;
        _element = method;
        _field = null;
        _method = method;
        _methodType = methodType;
    }

    String getName() {
        return _name;
    }

    boolean isField() {
        return null != _field;
    }

    VariableElement getField() {
        assert null != _field;
        return _field;
    }

    boolean isMethod() {
        return null != _method;
    }

    ExecutableElement getMethod() {
        assert null != _method;
        return _method;
    }

    TypeMirror getType() {
        return isField() ? getField().asType() : getMethodType().getReturnType();
    }

    ExecutableType getMethodType() {
        assert null != _methodType;
        return _methodType;
    }

    Element getElement() {
        return _element;
    }
}
