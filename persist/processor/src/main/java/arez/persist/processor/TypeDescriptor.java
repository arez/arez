package arez.persist.processor;

import java.util.List;
import java.util.stream.Collectors;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.jspecify.annotations.Nullable;

final class TypeDescriptor {

    private final String _name;

    private final boolean _persistOnDispose;

    private final TypeElement _element;

    @Nullable
    private final ExecutableElement _idMethod;

    private final List<PropertyDescriptor> _properties;

    TypeDescriptor(
            final String name,
            final boolean persistOnDispose,
            final TypeElement element,
            @Nullable final ExecutableElement idMethod,
            final List<PropertyDescriptor> properties) {
        _name = name;
        _persistOnDispose = persistOnDispose;
        _element = element;
        _idMethod = idMethod;
        _properties = properties;
    }

    String getName() {
        return _name;
    }

    boolean isPersistOnDispose() {
        return _persistOnDispose;
    }

    TypeElement getElement() {
        return _element;
    }

    @Nullable
    ExecutableElement getIdMethod() {
        return _idMethod;
    }

    List<PropertyDescriptor> getProperties() {
        return _properties;
    }

    List<PropertyDescriptor> getPropertiesByStore(final String storeName) {
        return _properties.stream().filter(p -> p.getStore().equals(storeName)).collect(Collectors.toList());
    }

    List<String> getStoreNames() {
        return getProperties().stream()
                .map(PropertyDescriptor::getStore)
                .sorted()
                .distinct()
                .collect(Collectors.toList());
    }
}
