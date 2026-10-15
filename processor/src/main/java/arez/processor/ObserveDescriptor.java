package arez.processor;

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import org.jspecify.annotations.Nullable;
import org.realityforge.proton.MemberChecks;
import org.realityforge.proton.ProcessorException;

/**
 * The class that represents the parsed state of @Observe methods on a @ArezComponent annotated class.
 */
final class ObserveDescriptor {

    private final ComponentDescriptor _component;

    private final String _name;

    private boolean _mutation;

    @Nullable
    private Priority _priority;

    private boolean _internalExecutor;
    private boolean _reportParameters;
    private boolean _reportResult;

    @Nullable
    private String _depType;

    private boolean _observeLowerPriorityDependencies;
    private boolean _nestedActionsAllowed;

    @Nullable
    private ExecutableElement _method;

    @Nullable
    private ExecutableType _methodType;

    private final List<ExecutableElement> _onDepsChanges = new ArrayList<>();

    private final List<ExecutableElement> _refMethods = new ArrayList<>();

    ObserveDescriptor(final ComponentDescriptor component, final String name) {
        _component = component;
        _name = name;
    }

    ComponentDescriptor getComponent() {
        return _component;
    }

    String getName() {
        return _name;
    }

    void addRefMethod(final ExecutableElement method) {
        getRefMethods().add(method);
    }

    List<ExecutableElement> getRefMethods() {
        return _refMethods;
    }

    ExecutableElement getMethod() {
        assert null != _method;
        return _method;
    }

    ExecutableType getMethodType() {
        assert null != _methodType;
        return _methodType;
    }

    boolean hasObserve() {
        return null != _method;
    }

    void setObserveMethod(
            final boolean mutation,
            final Priority priority,
            final boolean internalExecutor,
            final boolean reportParameters,
            final boolean reportResult,
            final String depType,
            final boolean observeLowerPriorityDependencies,
            final boolean nestedActionsAllowed,
            final ExecutableElement method,
            final ExecutableType trackedMethodType) {
        MemberChecks.mustBeWrappable(
                _component.getElement(), Constants.COMPONENT_CLASSNAME, Constants.OBSERVE_CLASSNAME, method);

        if (internalExecutor) {
            if (!method.getParameters().isEmpty()) {
                throw new ProcessorException(
                        "@Observe target must not have any parameters when executor=INTERNAL", method);
            }
            if (!method.getThrownTypes().isEmpty()) {
                throw new ProcessorException(
                        "@Observe target must not throw any exceptions when executor=INTERNAL", method);
            }
            if (TypeKind.VOID != method.getReturnType().getKind()) {
                throw new ProcessorException("@Observe target must not return a value when executor=INTERNAL", method);
            }
            if (_component.isClassType() && method.getModifiers().contains(Modifier.PUBLIC)) {
                throw new ProcessorException("@Observe target must not be public when executor=INTERNAL", method);
            }
            if (!reportParameters) {
                throw new ProcessorException(
                        "@Observe target must not specify reportParameters parameter " + "when executor=INTERNAL",
                        method);
            }
            if (!reportResult) {
                throw new ProcessorException(
                        "@Observe target must not specify reportResult parameter " + "when executor=INTERNAL", method);
            }
        }

        if (hasObserve()) {
            throw new ProcessorException(
                    "@Observe target duplicates existing method named "
                            + getMethod().getSimpleName(),
                    method);
        } else {
            _mutation = mutation;
            _priority = priority;
            _internalExecutor = internalExecutor;
            _reportParameters = reportParameters;
            _reportResult = reportResult;
            _depType = depType;
            _observeLowerPriorityDependencies = observeLowerPriorityDependencies;
            _nestedActionsAllowed = nestedActionsAllowed;
            _method = method;
            _methodType = trackedMethodType;
        }
    }

    boolean isInternalExecutor() {
        return _internalExecutor;
    }

    ExecutableElement getOnDepsChange() {
        assert 1 == _onDepsChanges.size();
        return getFirstOnDepsChange();
    }

    ExecutableElement getFirstOnDepsChange() {
        assert hasOnDepsChange();
        return _onDepsChanges.get(0);
    }

    List<ExecutableElement> getOnDepsChanges() {
        return _onDepsChanges;
    }

    boolean hasOnDepsChange() {
        return !_onDepsChanges.isEmpty();
    }

    boolean hasMultipleOnDepsChanges() {
        return _onDepsChanges.size() > 1;
    }

    boolean anyOnDepsChangeParameterIsObserver() {
        return _onDepsChanges.stream()
                .anyMatch(method -> !method.getParameters().isEmpty());
    }

    void setOnDepsChange(final ExecutableElement method) {
        _onDepsChanges.add(method);
    }

    String getFieldName() {
        return ComponentGenerator.FIELD_PREFIX + getName();
    }

    void validate() {
        if (isInternalExecutor()
                && hasOnDepsChange()
                && getRefMethods().isEmpty()
                && !anyOnDepsChangeParameterIsObserver()) {
            assert hasObserve();
            throw new ProcessorException(
                    "@Observe target with parameter executor=INTERNAL defined an @OnDepsChange "
                            + "method but has not defined an @ObserverRef method nor does the "
                            + "@OnDepsChange annotated method have an arez.Observer parameter. This results "
                            + "in an impossible to schedule observer.",
                    getMethod());
        }
        if (!isInternalExecutor() && !hasOnDepsChange()) {
            assert hasObserve();
            throw new ProcessorException(
                    "@Observe target defined parameter executor=EXTERNAL but does not "
                            + "specify an @OnDepsChange method.",
                    getMethod());
        }
        if ("AREZ_OR_EXTERNAL".equals(getDepType()) && getRefMethods().isEmpty()) {
            assert hasObserve();
            throw new ProcessorException(
                    "@Observe target with parameter depType=AREZ_OR_EXTERNAL has not "
                            + "defined an @ObserverRef method and thus can not invoke reportStale().",
                    getMethod());
        }
    }

    boolean isMutation() {
        return _mutation;
    }

    Priority getPriority() {
        assert null != _priority;
        return _priority;
    }

    boolean isReportParameters() {
        return _reportParameters;
    }

    boolean isReportResult() {
        return _reportResult;
    }

    String getDepType() {
        assert null != _depType;
        return _depType;
    }

    boolean isObserveLowerPriorityDependencies() {
        return _observeLowerPriorityDependencies;
    }

    boolean isNestedActionsAllowed() {
        return _nestedActionsAllowed;
    }
}
