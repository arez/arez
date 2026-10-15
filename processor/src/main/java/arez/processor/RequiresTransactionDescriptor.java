package arez.processor;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;

/**
 * The class that represents the parsed state of @RequiresTransaction methods on a
 * @ArezComponent annotated class.
 */
final class RequiresTransactionDescriptor {

    private final ComponentDescriptor _component;

    private final String _mode;

    private final String _tracking;

    private final ExecutableElement _method;

    private final ExecutableType _methodType;

    RequiresTransactionDescriptor(
            final ComponentDescriptor component,
            final String mode,
            final String tracking,
            final ExecutableElement method,
            final ExecutableType methodType) {
        _component = component;
        _mode = mode;
        _tracking = tracking;
        _method = method;
        _methodType = methodType;
    }

    ComponentDescriptor getComponent() {
        return _component;
    }

    String getMode() {
        return _mode;
    }

    boolean isAnyMode() {
        return "ANY".equals(_mode);
    }

    boolean isReadOnlyMode() {
        return "READ_ONLY".equals(_mode);
    }

    String getTracking() {
        return _tracking;
    }

    boolean isAnyTracking() {
        return "ANY".equals(_tracking);
    }

    boolean isTrackingTransactionRequired() {
        return "TRACKING".equals(_tracking);
    }

    ExecutableElement getMethod() {
        return _method;
    }

    ExecutableType getMethodType() {
        return _methodType;
    }
}
