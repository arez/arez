package arez.persist.processor;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

final class PropertyDescriptor {

    private final String _name;

    private final String _store;

    private final ExecutableElement _getter;

    private final ExecutableElement _setter;

    private final TypeMirror _type;

    PropertyDescriptor(
            final String name,
            final String store,
            final ExecutableElement getter,
            final ExecutableElement setter,
            final TypeMirror type) {
        _name = name;
        _store = store;
        _getter = getter;
        _setter = setter;
        _type = type;
    }

    String getName() {
        return _name;
    }

    String getConstantName() {
        return "PROPERTY_" + getName();
    }

    String getStore() {
        return _store;
    }

    TypeMirror getType() {
        return _type;
    }

    ExecutableElement getGetter() {
        return _getter;
    }

    ExecutableElement getSetter() {
        return _setter;
    }
}
