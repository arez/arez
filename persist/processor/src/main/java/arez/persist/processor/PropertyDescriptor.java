package arez.persist.processor;

import java.util.Objects;
import javax.lang.model.element.ExecutableElement;

final class PropertyDescriptor {

    private final String _name;

    private final String _store;

    private final ExecutableElement _getter;

    private final ExecutableElement _setter;

    PropertyDescriptor(
            final String name, final String store, final ExecutableElement getter, final ExecutableElement setter) {
        _name = Objects.requireNonNull(name);
        _store = Objects.requireNonNull(store);
        _getter = Objects.requireNonNull(getter);
        _setter = Objects.requireNonNull(setter);
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

    ExecutableElement getGetter() {
        return _getter;
    }

    ExecutableElement getSetter() {
        return _setter;
    }
}
