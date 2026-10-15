package arez.processor;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;

final class CandidateMethod {

    private final ExecutableElement _method;

    private final ExecutableType _methodType;

    CandidateMethod(final ExecutableElement method, final ExecutableType methodType) {
        _method = method;
        _methodType = methodType;
    }

    ExecutableElement getMethod() {
        return _method;
    }

    ExecutableType getMethodType() {
        return _methodType;
    }
}
