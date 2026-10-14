package arez.processor;

import java.util.Objects;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;

final class CandidateMethod {

    private final ExecutableElement _method;

    private final ExecutableType _methodType;

    CandidateMethod(final ExecutableElement method, final ExecutableType methodType) {
        _method = Objects.requireNonNull(method);
        _methodType = Objects.requireNonNull(methodType);
    }

    ExecutableElement getMethod() {
        return _method;
    }

    ExecutableType getMethodType() {
        return _methodType;
    }
}
