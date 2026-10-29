package arez.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import org.jspecify.annotations.Nullable;
import org.realityforge.proton.AnnotationsUtil;

/** A callback type use with its declaration scope and inherited generic bindings. */
final class MappedRelationshipType {
    private final Element _scope;
    private final TypeMirror _type;
    private final Map<Element, MappedRelationshipType> _bindings;
    private final boolean _projection;

    private MappedRelationshipType(
            final Element scope, final TypeMirror type, final Map<Element, MappedRelationshipType> bindings) {
        this(scope, type, bindings, false);
    }

    private MappedRelationshipType(
            final Element scope,
            final TypeMirror type,
            final Map<Element, MappedRelationshipType> bindings,
            final boolean projection) {
        _scope = scope;
        _type = type;
        _bindings = bindings;
        _projection = projection;
    }

    static MappedRelationshipType of(final Element declaration, final TypeMirror type, final TypeElement context) {
        Element owner = declaration;
        while (!(owner instanceof TypeElement)) {
            final Element parent = owner.getEnclosingElement();
            assert null != parent;
            owner = parent;
        }
        final Map<Element, MappedRelationshipType> bindings = findBindings(context, owner, Map.of());
        assert null != bindings;
        return new MappedRelationshipType(declaration, type, bindings);
    }

    Nullability.Nullness nullness() {
        if (_type instanceof TypeVariable variable
                && (_projection || Nullability.isMarked(_scope))
                && !AnnotationsUtil.hasAnnotationOfType(_type, "org.jspecify.annotations.Nullable")
                && !AnnotationsUtil.hasAnnotationOfType(_type, "org.jspecify.annotations.NonNull")) {
            final MappedRelationshipType binding = _bindings.get(variable.asElement());
            if (null != binding) {
                return binding.nullness();
            }
        }
        return Nullability.resolveTypeUse(_scope, _type);
    }

    MappedRelationshipType argument(final int index) {
        final MappedRelationshipType type = expand();
        return type.child(((DeclaredType) type._type).getTypeArguments().get(index));
    }

    int argumentCount() {
        return ((DeclaredType) expand()._type).getTypeArguments().size();
    }

    MappedRelationshipType comparatorInput() {
        final MappedRelationshipType type = expand();
        if (type._type instanceof WildcardType wildcard) {
            final TypeMirror bound = wildcard.getSuperBound();
            assert null != bound;
            return type.child(bound);
        }
        return type;
    }

    /** Checks nullness after Java's nominal assignability check has succeeded. */
    boolean canPassTo(final MappedRelationshipType expected) {
        final Nullability.Nullness actualNullness = nullness();
        final Nullability.Nullness expectedNullness = expected.nullness();
        if (actualNullness != expectedNullness
                && expectedNullness != Nullability.Nullness.NULLABLE
                && !(actualNullness == Nullability.Nullness.NONNULL
                        && expectedNullness == Nullability.Nullness.PARAMETRIC)) {
            return false;
        }
        final MappedRelationshipType actual = expand();
        final MappedRelationshipType target = expected.expand();
        if (target._type instanceof ArrayType array) {
            return actual.child(((ArrayType) actual._type).getComponentType())
                    .canPassTo(target.child(array.getComponentType()));
        }
        if (target._type instanceof DeclaredType declared) {
            if (declared.getTypeArguments().isEmpty() && !(declared.getEnclosingType() instanceof DeclaredType)) {
                return true;
            }
            final MappedRelationshipType projected = actual.asSuper((TypeElement) declared.asElement());
            assert null != projected;
            final var projectedType = (DeclaredType) projected._type;
            if (projected.argumentCount() != declared.getTypeArguments().size()) {
                return false;
            }
            for (int i = 0; i < declared.getTypeArguments().size(); i++) {
                if (!projected.argument(i).matchesArgument(target.argument(i))) {
                    return false;
                }
            }
            if (declared.getEnclosingType() instanceof DeclaredType enclosing
                    && !projected.child(projectedType.getEnclosingType()).matchesArgument(target.child(enclosing))) {
                return false;
            }
        }
        return true;
    }

    private boolean matchesArgument(final MappedRelationshipType expected) {
        final MappedRelationshipType target = expected.expand();
        if (target._type instanceof WildcardType wildcard) {
            final TypeMirror lower = wildcard.getSuperBound();
            if (null != lower) {
                return target.child(lower).canPassTo(comparatorInput());
            }
            final TypeMirror upper = wildcard.getExtendsBound();
            if (null == upper) {
                return true;
            }
            final MappedRelationshipType actual = expand();
            if (actual._type instanceof WildcardType actualWildcard && null == actualWildcard.getExtendsBound()) {
                return target.child(upper).nullness() == Nullability.Nullness.NULLABLE;
            }
            return readable().canPassTo(target.child(upper));
        }
        return canPassTo(expected) && expected.canPassTo(this);
    }

    private MappedRelationshipType readable() {
        final MappedRelationshipType type = expand();
        if (type._type instanceof WildcardType wildcard) {
            final TypeMirror bound = wildcard.getExtendsBound();
            assert null != bound;
            return type.child(bound);
        }
        return type;
    }

    private MappedRelationshipType expand() {
        if (_type instanceof TypeVariable variable) {
            final MappedRelationshipType binding = _bindings.get(variable.asElement());
            if (null != binding) {
                return binding.expand();
            }
        }
        return this;
    }

    @Nullable
    MappedRelationshipType asSuper(final TypeElement expected) {
        final MappedRelationshipType type = expand();
        if (type._type instanceof TypeVariable variable) {
            return type.child(variable.getUpperBound()).asSuper(expected);
        }
        if (type._type instanceof IntersectionType intersection) {
            for (final TypeMirror bound : intersection.getBounds()) {
                final MappedRelationshipType result = type.child(bound).asSuper(expected);
                if (null != result) {
                    return result;
                }
            }
            return null;
        }
        final var declared = (DeclaredType) type._type;
        final var element = (TypeElement) declared.asElement();
        if (element.equals(expected)) {
            return type;
        }
        final Map<Element, MappedRelationshipType> bindings = type.bindArguments(element, declared);
        for (final TypeMirror parent : parents(element)) {
            if (parent instanceof DeclaredType) {
                // Preserve supplied arguments through nominal supertype edges, including unannotated JDK collections.
                final MappedRelationshipType result =
                        new MappedRelationshipType(element, parent, bindings, true).asSuper(expected);
                if (null != result) {
                    return result;
                }
            }
        }
        return null;
    }

    private MappedRelationshipType child(final TypeMirror type) {
        return new MappedRelationshipType(_scope, type, _bindings, _projection);
    }

    private Map<Element, MappedRelationshipType> bindArguments(final TypeElement element, final DeclaredType declared) {
        final Map<Element, MappedRelationshipType> bindings = new HashMap<>(_bindings);
        if (declared.getEnclosingType() instanceof DeclaredType enclosing) {
            bindings.putAll(child(enclosing).bindArguments((TypeElement) enclosing.asElement(), enclosing));
        }
        for (int i = 0; i < declared.getTypeArguments().size(); i++) {
            bindings.put(
                    element.getTypeParameters().get(i),
                    child(declared.getTypeArguments().get(i)));
        }
        return bindings;
    }

    private static @Nullable Map<Element, MappedRelationshipType> findBindings(
            final TypeElement type, final Element owner, final Map<Element, MappedRelationshipType> bindings) {
        if (type.equals(owner)) {
            return bindings;
        }
        for (final TypeMirror parent : parents(type)) {
            if (parent instanceof DeclaredType declared) {
                final var element = (TypeElement) declared.asElement();
                final Map<Element, MappedRelationshipType> result = findBindings(
                        element,
                        owner,
                        new MappedRelationshipType(type, parent, bindings).bindArguments(element, declared));
                if (null != result) {
                    return result;
                }
            }
        }
        return null;
    }

    private static List<? extends TypeMirror> parents(final TypeElement type) {
        final var parents = new ArrayList<TypeMirror>(type.getInterfaces());
        parents.add(type.getSuperclass());
        return parents;
    }
}
