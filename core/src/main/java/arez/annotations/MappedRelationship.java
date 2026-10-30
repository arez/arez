package arez.annotations;

import arez.EqualityComparator;
import arez.component.ComponentObservable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Declares a memoized mapping from source objects to observable component instances.
 * The annotated method must be abstract, have no parameters and return a non-null
 * {@code List<Target>}, without method type parameters or declared exceptions.
 * It must not also declare {@link Memoize}.
 *
 * <p>For a property named {@code attributes} with target type {@code AttributeModel},
 * the processor discovers {@code attributesStream()} and
 * {@code createAttributeModel(Source)}. The source method returns a non-null stream;
 * its elements may be nullable when the factory accepts null. The factory returns
 * a non-null observable component. Companion annotations associate methods whose
 * names do not follow these conventions.</p>
 *
 * <p>Callbacks must be concrete instance methods accessible to the generated
 * subclass, without method type parameters or declared exceptions. Parameter and
 * return types, including their JSpecify contracts, must be compatible. Unrelated
 * conventional overloads are ignored; multiple compatible candidates are an error.
 * An explicitly annotated callback binds only its named property.</p>
 *
 * <p>Optional {@code filterAttributeModel(Target)}, {@code attributesComparator()}
 * and {@code retainAttributeModel(Target)} methods provide presentation filtering,
 * sorting and retention of absent-source targets. Filtered targets remain cached
 * and observed. Retained targets are appended after present-source targets before
 * filtering and sorting.</p>
 *
 * <p>Source objects are matched by reference identity unless {@link #useEqualsForKeys()}
 * is enabled. Targets remain observed while
 * their sources belong to the relationship. The mapping cache is cleared when the
 * computation deactivates. Target disposal follows the target's own lifecycle,
 * typically {@link ArezComponent#disposeOnDeactivate()}.</p>
 *
 * <p>Failures follow normal computed error handling. Evaluation can retry and
 * deactivation still releases cached references, but prior target identities and
 * rollback are not guaranteed.</p>
 *
 * @see MappedRelationshipSource
 * @see MappedRelationshipFactory
 * @see MappedRelationshipFilter
 * @see MappedRelationshipComparator
 * @see MappedRelationshipRetention
 */
@Documented
@Target(ElementType.METHOD)
public @interface MappedRelationship {
    /**
     * Matches source keys using {@link Object#equals(Object)} and {@link Object#hashCode()}
     * rather than reference identity. Equal keys must be interchangeable for target
     * construction, with stable equality and hash codes while cached or retained.
     * Reuse preserves the original target and its source; it does not rebind the target
     * to an equal replacement source. Repeated equal sources produce repeated references
     * to one target.
     *
     * @return true to match sources by value; false to match by reference identity.
     */
    boolean useEqualsForKeys() default false;

    /**
     * Permits runtime validation when the declared target type is component-like but
     * observability cannot be proven at compile time. Eligible types are annotated with
     * {@link ArezComponentLike} or an annotation marked with {@link ActAsArezComponent}.
     * Actual targets must implement {@link ComponentObservable}, including hidden and
     * retained targets. Unrelated declared types are rejected. Redundant opt-in produces
     * the suppressible warning {@code Arez:UnnecessaryRuntimeTypeValidation}.
     *
     * @return true to permit runtime validation of component-like targets.
     */
    boolean validateTypeAtRuntime() default false;

    /**
     * Returns the property name, or derives it from the getter when unspecified.
     *
     * @return the property name.
     * @see Memoize#name()
     */
    String name() default "<default>";

    /**
     * Returns whether the computation remains active until its component is disposed.
     *
     * @return whether to keep the computation active.
     * @see Memoize#keepAlive()
     */
    boolean keepAlive() default false;

    /**
     * Returns the computation priority, inheriting the component default when unspecified.
     *
     * @return the priority.
     * @see Memoize#priority()
     */
    Priority priority() default Priority.DEFAULT;

    /**
     * Returns whether lower-priority computed dependencies are permitted.
     *
     * @return whether priority inversion is permitted.
     * @see Memoize#observeLowerPriorityDependencies()
     */
    boolean observeLowerPriorityDependencies() default false;

    /**
     * Returns the permitted dependency types.
     *
     * @return the dependency types.
     * @see Memoize#depType()
     */
    DepType depType() default DepType.AREZ;

    /**
     * Returns whether the result is included in spy events.
     *
     * @return whether to report the result.
     * @see Memoize#reportResult()
     */
    boolean reportResult() default true;

    /**
     * Returns whether reads outside transactions are permitted.
     *
     * @return the read policy.
     * @see Memoize#readOutsideTransaction()
     */
    Feature readOutsideTransaction() default Feature.AUTODETECT;

    /**
     * Returns whether external invalidation creates an action outside a transaction.
     * Explicit enablement requires {@link DepType#AREZ_OR_EXTERNAL}.
     *
     * @return the external invalidation policy.
     * @see Memoize#writeOutsideTransaction()
     */
    Feature writeOutsideTransaction() default Feature.AUTODETECT;

    /**
     * Returns the additional result equality comparator. Target reference, order and
     * occurrence-count changes always propagate. The comparator is consulted only
     * when the ordered target references are unchanged, and may request additional
     * notifications by returning false.
     *
     * @return the additional comparator, or the default comparison sentinel.
     * @see Memoize#equalityComparator()
     */
    Class<? extends EqualityComparator> equalityComparator() default EqualityComparator.class;
}
