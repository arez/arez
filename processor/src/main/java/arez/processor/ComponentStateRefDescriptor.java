package arez.processor;

import javax.lang.model.element.ExecutableElement;

/**
 * Declaration of a method that used to access component state.
 */
final class ComponentStateRefDescriptor {
    enum State {
        CONSTRUCTED,
        COMPLETE,
        READY,
        DISPOSING
    }

    private final ExecutableElement _method;

    private final State _state;

    ComponentStateRefDescriptor(final ExecutableElement method, final State state) {
        _method = method;
        _state = state;
    }

    ExecutableElement getMethod() {
        return _method;
    }

    State getState() {
        return _state;
    }
}
