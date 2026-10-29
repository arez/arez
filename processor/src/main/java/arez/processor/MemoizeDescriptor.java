package arez.processor;

import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.WildcardTypeName;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import org.jspecify.annotations.Nullable;
import org.realityforge.proton.GeneratorUtil;
import org.realityforge.proton.MemberChecks;
import org.realityforge.proton.ProcessorException;

/**
 * The class that represents the parsed state of @Memoize methods on a @ArezComponent annotated class.
 */
final class MemoizeDescriptor {

    private final ComponentDescriptor _component;

    private final String _name;

    @Nullable
    private ExecutableElement _method;

    @Nullable
    private ExecutableType _methodType;

    private boolean _keepAlive;

    @Nullable
    private Priority _priority;

    private boolean _reportResult;
    private boolean _observeLowerPriorityDependencies;

    @Nullable
    private String _readOutsideTransaction;

    @Nullable
    private String _writeOutsideTransaction;

    @Nullable
    private String _depType;

    private String _equalityComparator = Constants.OBJECTS_EQUALS_COMPARATOR_CLASSNAME;

    @Nullable
    private MappedRelationshipDescriptor _mappedRelationship;

    @Nullable
    private ExecutableElement _onActivate;

    @Nullable
    private ExecutableElement _onDeactivate;

    private final List<CandidateMethod> _refMethods = new ArrayList<>();

    private final List<MemoizeContextParameterDescriptor> _contextParameters = new ArrayList<>();

    MemoizeDescriptor(final ComponentDescriptor component, final String name) {
        _component = component;
        _name = name;
    }

    ComponentDescriptor getComponent() {
        return _component;
    }

    String getName() {
        return _name;
    }

    boolean hasMemoize() {
        return null != _method;
    }

    boolean isKeepAlive() {
        return _keepAlive;
    }

    Priority getPriority() {
        assert null != _priority;
        return _priority;
    }

    ExecutableElement getMethod() {
        assert null != _method;
        return _method;
    }

    ExecutableType getMethodType() {
        assert null != _methodType;
        return _methodType;
    }

    @Nullable
    MappedRelationshipDescriptor getMappedRelationship() {
        return _mappedRelationship;
    }

    void setMappedRelationship(final MappedRelationshipDescriptor mappedRelationship) {
        _mappedRelationship = mappedRelationship;
    }

    private String annotationName() {
        return null == _mappedRelationship ? "@Memoize" : "@MappedRelationship";
    }

    @Nullable
    ExecutableElement getOnActivate() {
        return _onActivate;
    }

    @Nullable
    ExecutableElement getOnDeactivate() {
        return _onDeactivate;
    }

    void setMemoize(
            final ExecutableElement method,
            final ExecutableType methodType,
            final boolean keepAlive,
            final Priority priority,
            final boolean reportResult,
            final boolean observeLowerPriorityDependencies,
            final String readOutsideTransaction,
            final String writeOutsideTransaction,
            final String depType,
            final String equalityComparator)
            throws ProcessorException {
        // The caller already verified that no duplicate computable have been defined
        assert null == _method;
        if (null == _mappedRelationship) {
            MemberChecks.mustBeWrappable(
                    _component.getElement(), Constants.COMPONENT_CLASSNAME, Constants.MEMOIZE_CLASSNAME, method);
        }
        MemberChecks.mustReturnAValue(Constants.MEMOIZE_CLASSNAME, method);
        MemberChecks.mustNotThrowAnyExceptions(Constants.MEMOIZE_CLASSNAME, method);

        _method = method;
        _methodType = methodType;
        _keepAlive = keepAlive;
        _priority = priority;
        _reportResult = reportResult;
        _observeLowerPriorityDependencies = observeLowerPriorityDependencies;
        _readOutsideTransaction = readOutsideTransaction;
        _writeOutsideTransaction = writeOutsideTransaction;
        _depType = depType;
        _equalityComparator = equalityComparator;

        if ("ENABLE".equals(writeOutsideTransaction) && !"AREZ_OR_EXTERNAL".equals(depType)) {
            throw new ProcessorException(
                    annotationName()
                            + " target specified writeOutsideTransaction = ENABLE but depType is not AREZ_OR_EXTERNAL.",
                    method);
        }

        if (ComponentGenerator.isMethodReturnType(getMethod(), Stream.class)) {
            throw new ProcessorException(
                    "@Memoize target must not return a value of type java.util.stream.Stream "
                            + "as the type is single use and thus does not make sense to cache as a "
                            + "computable value",
                    method);
        }
    }

    void addRefMethod(final ExecutableElement method, final ExecutableType methodType) throws ProcessorException {
        _refMethods.add(new CandidateMethod(method, methodType));
    }

    List<CandidateMethod> getRefMethods() {
        return _refMethods;
    }

    void addContextParameter(final MemoizeContextParameterDescriptor parameter) throws ProcessorException {
        _contextParameters.add(parameter);
    }

    boolean shouldGenerateMemoizeWrapper() {
        return !getContextParameters().isEmpty() || hasHooks() || shouldGenerateDeactivateWrapperHook();
    }

    boolean shouldGenerateActivateWrapperHook() {
        return hasOnActivateWithComputableValueParameter() || (isCollectionType() && hasNoParameters());
    }

    boolean shouldGenerateDeactivateWrapperHook() {
        return isCollectionType() && hasNoParameters();
    }

    boolean isCollectionType() {
        return ComponentGenerator.isCollectionType(getMethod());
    }

    /**
     * Return true if the Memoize has either explicit or implicit (aka context) parameters.
     *
     * @return true if the Memoize has either explicit or implicit (aka context) parameters.
     */
    boolean hasNoParameters() {
        return getMethod().getParameters().isEmpty() && getContextParameters().isEmpty();
    }

    List<MemoizeContextParameterDescriptor> getContextParameters() {
        return _contextParameters;
    }

    void setOnActivate(final ExecutableElement method) throws ProcessorException {
        if (null != _onActivate) {
            throw new ProcessorException(
                    "@OnActivate target duplicates existing method named " + _onActivate.getSimpleName(), method);
        } else {
            _onActivate = method;
        }
    }

    void setOnDeactivate(final ExecutableElement method) throws ProcessorException {
        if (null != _onDeactivate) {
            throw new ProcessorException(
                    "@OnDeactivate target duplicates existing method named " + _onDeactivate.getSimpleName(), method);
        } else {
            _onDeactivate = method;
        }
    }

    void validate(final ProcessingEnvironment processingEnv) throws ProcessorException {
        if (null != _mappedRelationship && !getContextParameters().isEmpty()) {
            throw new ProcessorException("@MappedRelationship does not support context parameters", getMethod());
        }
        if (null == _method) {
            if (null != _onActivate) {
                throw new ProcessorException("@OnActivate exists but there is no corresponding @Memoize", _onActivate);
            } else if (null != _onDeactivate) {
                throw new ProcessorException(
                        "@OnDeactivate exists but there is no corresponding @Memoize", _onDeactivate);
            } else {
                throw new ProcessorException(
                        "@ComputableValueRef exists but there is no corresponding @Memoize",
                        _refMethods.get(0).getMethod());
            }
        }
        if (_keepAlive) {
            if (!_method.getParameters().isEmpty()) {
                throw new ProcessorException(
                        "@Memoize target specified parameter keepAlive as true but has parameters.", _method);
            } else if (!getContextParameters().isEmpty()) {
                throw new ProcessorException(
                        "@Memoize target specified parameter keepAlive as true but has "
                                + "matching context parameters.",
                        _method);
            } else if (null != _onActivate) {
                throw new ProcessorException(
                        "@OnActivate exists for " + annotationName()
                                + " property that specified parameter keepAlive as true.",
                        _onActivate);
            } else if (null != _onDeactivate) {
                throw new ProcessorException(
                        "@OnDeactivate exists for " + annotationName()
                                + " property that specified parameter keepAlive as true.",
                        _onDeactivate);
            }
        }
        // One day we may want to support parameters here.
        // But rather than put the effort into correct code generation when there is no
        // current use case, let's throw an exception.
        // When a use case pops up, then we can implement the functionality.
        if (!_method.getParameters().isEmpty()) {
            if (null != _onActivate) {
                throw new ProcessorException(
                        "@OnActivate target associated with @Memoize method that has parameters.", _onActivate);
            } else if (null != _onDeactivate) {
                throw new ProcessorException(
                        "@OnDeactivate target associated with @Memoize method that has parameters.", _onDeactivate);
            }
        }
        // One day we may want to support context parameters here.
        // But rather than put the effort into correct code generation when there is no
        // current use case, let's throw an exception.
        // When a use case pops up, then we can implement the functionality.
        if (!getContextParameters().isEmpty()) {
            if (null != _onActivate) {
                throw new ProcessorException(
                        "@OnActivate target associated with @Memoize method that has " + "matching context parameters.",
                        _onActivate);
            } else if (null != _onDeactivate) {
                throw new ProcessorException(
                        "@OnDeactivate target associated with @Memoize method that has "
                                + "matching context parameters.",
                        _onDeactivate);
            }
        }

        if (null != _onActivate && null != _method) {
            final var hookType = (ExecutableType) processingEnv
                    .getTypeUtils()
                    .asMemberOf((DeclaredType) _component.getElement().asType(), _onActivate);
            final List<? extends TypeMirror> parameters = hookType.getParameterTypes();
            if (1 == parameters.size()) {
                final TypeName typeName = GeneratorUtil.getTypeName(parameters.get(0));
                if (typeName instanceof final ParameterizedTypeName parameterizedTypeName) {
                    final TypeName paramType =
                            parameterizedTypeName.typeArguments().get(0);
                    if (!(paramType instanceof WildcardTypeName)) {
                        final TypeName actual =
                                GeneratorUtil.getTypeName(getMethodType().getReturnType());
                        if (!actual.box().toString().equals(paramType.toString())) {
                            throw new ProcessorException(
                                    "@OnActivate target has a parameter of type ComputableValue with a "
                                            + "type parameter of "
                                            + paramType + " but the " + annotationName() + " method returns a type of "
                                            + actual,
                                    _onActivate);
                        }
                    }
                }
            }
        }

        for (final CandidateMethod refMethod : _refMethods) {
            final TypeName typeName =
                    GeneratorUtil.getTypeName(refMethod.getMethodType().getReturnType());
            if (typeName instanceof final ParameterizedTypeName parameterizedTypeName) {
                final TypeName expectedType =
                        parameterizedTypeName.typeArguments().get(0);
                if (!(expectedType instanceof WildcardTypeName)) {
                    final TypeName actual =
                            GeneratorUtil.getTypeName(getMethodType().getReturnType());
                    if (!actual.box().toString().equals(expectedType.toString())) {
                        throw new ProcessorException(
                                "@ComputableValueRef target has a type parameter of " + expectedType + " but "
                                        + annotationName() + " method returns type of " + actual,
                                refMethod.getMethod());
                    }
                }
            }

            assert null != _methodType;
            final List<? extends TypeMirror> parameterTypes = _methodType.getParameterTypes();
            final List<? extends TypeMirror> refParameterTypes =
                    refMethod.getMethodType().getParameterTypes();

            final boolean sizeMatch = parameterTypes.size() == refParameterTypes.size();
            boolean typesMatch = true;
            if (sizeMatch) {
                for (int i = 0; i < parameterTypes.size(); i++) {
                    final TypeMirror typeMirror = parameterTypes.get(i);
                    final TypeMirror typeMirror2 = refParameterTypes.get(i);
                    if (!processingEnv.getTypeUtils().isSameType(typeMirror, typeMirror2)) {
                        typesMatch = false;
                        break;
                    }
                }
            }
            if (!sizeMatch || !typesMatch) {
                throw new ProcessorException(
                        "@ComputableValueRef target and the associated @Memoize "
                                + "target do not have the same parameters.",
                        _method);
            }
        }
        if (_refMethods.isEmpty()
                && !hasOnActivateWithComputableValueParameter()
                && getDepType().equals("AREZ_OR_EXTERNAL")) {
            assert null != _method;
            throw new ProcessorException(
                    annotationName() + " target specified depType = AREZ_OR_EXTERNAL but "
                            + "there is no associated @ComputableValueRef method.",
                    _method);
        }
    }

    private boolean hasOnActivateWithComputableValueParameter() {
        if (null == _onActivate) {
            return false;
        } else {
            final var parameters = _onActivate.getParameters();
            return 1 == parameters.size()
                    && parameters.get(0).asType() instanceof DeclaredType declared
                    && ((TypeElement) declared.asElement())
                            .getQualifiedName()
                            .contentEquals(Constants.COMPUTABLE_VALUE_CLASSNAME);
        }
    }

    boolean hasHooks() {
        return null != _mappedRelationship || null != _onActivate || null != _onDeactivate;
    }

    boolean isReportResult() {
        return _reportResult;
    }

    boolean isObserveLowerPriorityDependencies() {
        return _observeLowerPriorityDependencies;
    }

    boolean canReadOutsideTransaction() {
        return "ENABLE".equals(_readOutsideTransaction)
                || ("AUTODETECT".equals(_readOutsideTransaction)
                        && getComponent().defaultReadOutsideTransaction());
    }

    String getDepType() {
        assert null != _depType;
        return _depType;
    }

    boolean canWriteOutsideTransaction() {
        return "AREZ_OR_EXTERNAL".equals(getDepType())
                && ("ENABLE".equals(_writeOutsideTransaction)
                        || ("AUTODETECT".equals(_writeOutsideTransaction)
                                && getComponent().defaultWriteOutsideTransaction()));
    }

    String getEqualityComparator() {
        return _equalityComparator;
    }

    boolean hasObjectsEqualsComparator() {
        return Constants.OBJECTS_EQUALS_COMPARATOR_CLASSNAME.equals(_equalityComparator);
    }

    boolean hasObjectsDeepEqualsComparator() {
        return Constants.OBJECTS_DEEP_EQUALS_COMPARATOR_CLASSNAME.equals(_equalityComparator);
    }
}
