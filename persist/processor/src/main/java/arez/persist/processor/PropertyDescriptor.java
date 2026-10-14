package arez.persist.processor;

import java.util.Objects;
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
        _name = Objects.requireNonNull(name);
        _store = Objects.requireNonNull(store);
        _getter = Objects.requireNonNull(getter);
        _setter = Objects.requireNonNull(setter);
        _type = Objects.requireNonNull(type);
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
