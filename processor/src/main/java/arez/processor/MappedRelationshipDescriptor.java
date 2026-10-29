package arez.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.jspecify.annotations.Nullable;
import org.realityforge.proton.AnnotationsUtil;
import org.realityforge.proton.MemberChecks;
import org.realityforge.proton.ProcessorException;

/** Resolves typed callbacks for a generated mapped property. */
final class MappedRelationshipDescriptor {
    enum Role {
        SOURCE(Constants.MAPPED_RELATIONSHIP_SOURCE_CLASSNAME),
        FACTORY(Constants.MAPPED_RELATIONSHIP_FACTORY_CLASSNAME),
        FILTER(Constants.MAPPED_RELATIONSHIP_FILTER_CLASSNAME),
        COMPARATOR(Constants.MAPPED_RELATIONSHIP_COMPARATOR_CLASSNAME),
        RETENTION(Constants.MAPPED_RELATIONSHIP_RETENTION_CLASSNAME);

        private final String _annotation;

        Role(final String annotation) {
            _annotation = annotation;
        }

        String annotation() {
            return _annotation;
        }
    }

    private final ComponentDescriptor _component;
    private final String _name;
    private final ExecutableElement _method;
    private final DeclaredType _targetType;
    private final MappedRelationshipType _targetContract;
    private @Nullable CandidateMethod _source;
    private @Nullable CandidateMethod _factory;
    private @Nullable CandidateMethod _filter;
    private @Nullable CandidateMethod _comparator;
    private @Nullable CandidateMethod _retention;
    private @Nullable TypeMirror _sourceType;
    private @Nullable MappedRelationshipType _sourceContract;
    private Nullability.Nullness _sourceNullness = Nullability.Nullness.UNSPECIFIED;

    MappedRelationshipDescriptor(
            final ComponentDescriptor component,
            final String name,
            final ExecutableElement method,
            final ExecutableType methodType) {
        _component = component;
        _name = name;
        _method = method;
        MemberChecks.mustBeSubclassCallable(
                component.getElement(), Constants.COMPONENT_CLASSNAME, Constants.MAPPED_RELATIONSHIP_CLASSNAME, method);
        MemberChecks.mustNotHaveAnyParameters(Constants.MAPPED_RELATIONSHIP_CLASSNAME, method);
        MemberChecks.mustNotThrowAnyExceptions(Constants.MAPPED_RELATIONSHIP_CLASSNAME, method);
        if (!method.getModifiers().contains(Modifier.ABSTRACT)
                || !method.getTypeParameters().isEmpty()) {
            throw new ProcessorException(
                    "@MappedRelationship target must be abstract and have no type parameters", method);
        }
        if (!(methodType.getReturnType() instanceof DeclaredType list)
                || !rawName(list).equals("java.util.List")
                || list.getTypeArguments().size() != 1
                || !(list.getTypeArguments().get(0) instanceof DeclaredType target)) {
            throw new ProcessorException(
                    "@MappedRelationship target must return List<Target> with a declared target type", method);
        }
        _targetType = target;
        if (!Nullability.isNonNull(method, list, component.getElement())) {
            throw new ProcessorException("@MappedRelationship target must return a non-null list", method);
        }
        _targetContract = MappedRelationshipType.of(method, method.getReturnType(), component.getElement())
                .argument(0);
        if (_targetContract.nullness() != Nullability.Nullness.NONNULL) {
            throw new ProcessorException("@MappedRelationship target elements must be non-null", method);
        }
    }

    DeclaredType getTargetType() {
        return _targetType;
    }

    TypeMirror getSourceType() {
        assert null != _sourceType;
        return _sourceType;
    }

    boolean isSourceNullable() {
        return _sourceNullness == Nullability.Nullness.NULLABLE;
    }

    CandidateMethod getSource() {
        assert null != _source;
        return _source;
    }

    CandidateMethod getFactory() {
        assert null != _factory;
        return _factory;
    }

    @Nullable
    CandidateMethod getFilter() {
        return _filter;
    }

    @Nullable
    CandidateMethod getComparator() {
        return _comparator;
    }

    @Nullable
    CandidateMethod getRetention() {
        return _retention;
    }

    void resolve(final ProcessingEnvironment environment, final List<CandidateMethod> methods) {
        _source = find(environment, methods, Role.SOURCE);
        final CandidateMethod source = getSource();
        _sourceType = ((DeclaredType) source.getMethodType().getReturnType())
                .getTypeArguments()
                .get(0);
        _sourceContract = MappedRelationshipType.of(
                        source.getMethod(), source.getMethod().getReturnType(), _component.getElement())
                .argument(0);
        _sourceNullness = _sourceContract.nullness();
        if (_sourceNullness == Nullability.Nullness.PARAMETRIC) {
            throw new ProcessorException(
                    "Mapped source elements need a definite JSpecify nullness contract", source.getMethod());
        }
        _factory = find(environment, methods, Role.FACTORY);
        _filter = find(environment, methods, Role.FILTER);
        _comparator = find(environment, methods, Role.COMPARATOR);
        _retention = find(environment, methods, Role.RETENTION);
    }

    private @Nullable CandidateMethod find(
            final ProcessingEnvironment environment, final List<CandidateMethod> methods, final Role role) {
        final String targetName =
                ((TypeElement) _targetType.asElement()).getSimpleName().toString();
        final String conventionalName =
                switch (role) {
                    case SOURCE -> _name + "Stream";
                    case FACTORY -> "create" + targetName;
                    case FILTER -> "filter" + targetName;
                    case COMPARATOR -> _name + "Comparator";
                    case RETENTION -> "retain" + targetName;
                };
        final List<CandidateMethod> matches = new ArrayList<>();
        final List<String> rejected = new ArrayList<>();
        for (final CandidateMethod candidate : methods) {
            final ExecutableElement method = candidate.getMethod();
            final AnnotationMirror annotation = AnnotationsUtil.findAnnotationByType(method, role.annotation());
            final boolean explicit = null != annotation
                    && _name.equals(AnnotationsUtil.<String>getAnnotationValueValue(annotation, "name"));
            if (!explicit && (hasCompanion(method) || !method.getSimpleName().contentEquals(conventionalName))) {
                continue;
            }
            final String problem = problem(environment, role, candidate);
            if (null == problem) {
                matches.add(candidate);
            } else if (explicit) {
                throw new ProcessorException(
                        "Invalid @"
                                + role.annotation().substring(role.annotation().lastIndexOf('.') + 1)
                                + " callback for '" + _name + "': " + problem,
                        method);
            } else {
                rejected.add(method.getSimpleName() + candidate.getMethodType().toString() + ": " + problem);
            }
        }
        if (matches.size() > 1) {
            throw new ProcessorException(
                    "@MappedRelationship '" + _name + "' has multiple compatible "
                            + role.name().toLowerCase(Locale.ROOT) + " callbacks: "
                            + matches.stream()
                                    .map(c -> c.getMethod().toString())
                                    .toList(),
                    _method);
        } else if (matches.isEmpty()) {
            if (role != Role.SOURCE && role != Role.FACTORY) {
                return null;
            }
            throw new ProcessorException(
                    "@MappedRelationship '" + _name + "' requires " + expected(role, conventionalName)
                            + (rejected.isEmpty() ? "" : ". Nonmatching methods: " + String.join("; ", rejected)),
                    _method);
        }
        return matches.get(0);
    }

    private @Nullable String problem(
            final ProcessingEnvironment environment, final Role role, final CandidateMethod candidate) {
        final ExecutableElement method = candidate.getMethod();
        final ExecutableType type = candidate.getMethodType();
        if (method.getModifiers().contains(Modifier.ABSTRACT)
                || method.getModifiers().contains(Modifier.STATIC)
                || !method.getTypeParameters().isEmpty()
                || !type.getThrownTypes().isEmpty()) {
            return "must be a concrete instance method without type parameters or declared exceptions";
        }
        if (role == Role.SOURCE) {
            if (!type.getParameterTypes().isEmpty()
                    || !(type.getReturnType() instanceof DeclaredType stream)
                    || !rawName(stream).equals("java.util.stream.Stream")
                    || stream.getTypeArguments().size() != 1
                    || stream.getTypeArguments().get(0).getKind() == TypeKind.WILDCARD) {
                return "must have no parameters and return Stream<Source>";
            }
        } else if (role == Role.FACTORY
                && (type.getParameterTypes().size() != 1
                        || !environment
                                .getTypeUtils()
                                .isAssignable(
                                        getSourceType(),
                                        type.getParameterTypes().get(0))
                        || !environment.getTypeUtils().isAssignable(type.getReturnType(), _targetType))) {
            return "must accept " + getSourceType() + " and return " + _targetType;
        } else if ((role == Role.FILTER || role == Role.RETENTION)
                && (type.getParameterTypes().size() != 1
                        || !environment
                                .getTypeUtils()
                                .isAssignable(
                                        _targetType, type.getParameterTypes().get(0))
                        || type.getReturnType().getKind() != TypeKind.BOOLEAN)) {
            return "must accept " + _targetType + " and return boolean";
        } else if (role == Role.COMPARATOR) {
            final DeclaredType comparatorType = environment
                    .getTypeUtils()
                    .getDeclaredType(
                            environment.getElementUtils().getTypeElement("java.util.Comparator"),
                            environment.getTypeUtils().getWildcardType(null, _targetType));
            if (!type.getParameterTypes().isEmpty()
                    || !(type.getReturnType() instanceof DeclaredType declared)
                    || (rawName(declared).equals("java.util.Comparator")
                            && declared.getTypeArguments().isEmpty())
                    || !environment.getTypeUtils().isAssignable(declared, comparatorType)) {
                return "must have no parameters and return Comparator<? super " + _targetType + ">";
            }
        }
        try {
            MemberChecks.mustBeSubclassCallable(
                    _component.getElement(), Constants.COMPONENT_CLASSNAME, role.annotation(), method);
            if (!Nullability.isNonNull(method, type.getReturnType(), _component.getElement())) {
                return "must return a non-null value";
            }
            if (role == Role.FACTORY
                    && _sourceNullness == Nullability.Nullness.NULLABLE
                    && !Nullability.isNullable(
                            method.getParameters().get(0),
                            type.getParameterTypes().get(0),
                            _component.getElement())) {
                return "must accept nullable source elements";
            }
            final var result = MappedRelationshipType.of(method, method.getReturnType(), _component.getElement());
            if (role == Role.FACTORY || role == Role.FILTER || role == Role.RETENTION) {
                final var parameter = method.getParameters().get(0);
                final var input = MappedRelationshipType.of(parameter, parameter.asType(), _component.getElement());
                assert null != _sourceContract;
                if (!(role == Role.FACTORY ? _sourceContract : _targetContract).canPassTo(input)
                        || (role == Role.FACTORY && !result.canPassTo(_targetContract))) {
                    return "has incompatible JSpecify parameter or result contracts";
                }
            } else if (role == Role.COMPARATOR) {
                final MappedRelationshipType comparator =
                        result.asSuper(environment.getElementUtils().getTypeElement("java.util.Comparator"));
                assert null != comparator;
                if (comparator.argumentCount() != 1
                        || !_targetContract.canPassTo(comparator.argument(0).comparatorInput())) {
                    return "has incompatible JSpecify comparator input contract";
                }
            }
        } catch (final ProcessorException exception) {
            return exception.getMessage();
        }
        return null;
    }

    private String expected(final Role role, final String methodName) {
        return role == Role.SOURCE
                ? "a compatible source callback " + methodName + "(): Stream<Source>"
                : "a compatible factory callback " + methodName + "(" + getSourceType() + "): " + _targetType;
    }

    static boolean hasCompanion(final ExecutableElement method) {
        for (final Role role : Role.values()) {
            if (AnnotationsUtil.hasAnnotationOfType(method, role.annotation())) {
                return true;
            }
        }
        return false;
    }

    static void validateExplicitAssociations(final ComponentDescriptor component, final List<CandidateMethod> methods) {
        for (final CandidateMethod method : methods) {
            for (final Role role : Role.values()) {
                final AnnotationMirror annotation =
                        AnnotationsUtil.findAnnotationByType(method.getMethod(), role.annotation());
                if (null != annotation) {
                    final String name = AnnotationsUtil.getAnnotationValueValue(annotation, "name");
                    final MemoizeDescriptor memoize = component.getMemoizes().get(name);
                    if (!SourceVersion.isIdentifier(name)
                            || SourceVersion.isKeyword(name)
                            || null == memoize
                            || null == memoize.getMappedRelationship()) {
                        throw new ProcessorException(
                                "Mapped relationship callback refers to unknown relationship '" + name + "'",
                                method.getMethod());
                    }
                }
            }
        }
    }

    private static String rawName(final DeclaredType type) {
        return ((TypeElement) type.asElement()).getQualifiedName().toString();
    }
}
