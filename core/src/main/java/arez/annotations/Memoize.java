package arez.annotations;

import arez.ComputableValue;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Methods marked with this annotation are <a href="https://en.wikipedia.org/wiki/Memoization">memoized</a> while
 * activated which typically means they have an observer. These methods are typically backed with one or more
 * {@link ComputableValue} instances.
 *
 * <p>The return value should be derived from the method parameters and any other {@link Observable} properties
 * or {@link Memoize} properties accessed within the scope of the method. The he value returned by the method
 * should not change unless the state of the {@link Observable} properties or {@link Memoize} properties change.
 * If the return value can change outside of the above scenarios it is important to set the {@link #depType()}
 * to {@link DepType#AREZ_OR_EXTERNAL} and explicitly report possible changes to the derived value by invoking
 * the {@link ComputableValue#reportPossiblyChanged()} on the {@link ComputableValue} returned from a method
 * annotated by the {@link ComputableValueRef} that is linked to the method marked with this annotation.</p>
 *
 * <p>The method is wrapped in a READ_ONLY transaction and thus can not modify other state in the system.</p>
 *
 * <p>The enhanced method is implemented by creating a separate {@link ComputableValue} instance for each unique
 * combination of parameters that are passed to the method. When the {@link ComputableValue} is deactivated, a hook
 * triggers that removes the {@link ComputableValue} from the local cache. If the method has zero parameter then
 * the method is backed by a single {@link ComputableValue} instance.</p>
 *
 * <p>The method that is annotated with this annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must return a value</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface Memoize
{
  /**
   * Return the root name of the element value relative to the component.
   * If the method has parameters then the name will be used in combination with a sequence
   * when naming the synthesized {@link ComputableValue} instances. The value must conform to
   * the requirements of a java identifier. The name must also be unique across {@link Observable}s,
   * {@code Memoize}s and {@link Action}s within the scope of the {@link ArezComponent} annotated element.
   *
   * @return the root name of the element relative to the component.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * A flag indicating whether the computable should be "kept alive". A computable that is kept alive
   * is activated on creation and never deactivates. This is useful if the computable property is only
   * accessed from within actions but should be kept up to date and not recomputed on each access.
   * This MUST not be set if the target method has any parameters as can not keep computed value active
   * if parameter values are unknown.
   *
   * @return true to keep computable alive.
   */
  boolean keepAlive() default false;

  /**
   * The priority of the underlying ComputableValue observer.
   *
   * @return the priority of the ComputableValue observer.
   */
  Priority priority() default Priority.DEFAULT;

  /**
   * Flag controlling whether the underlying observer can observe ComputableValue instances with lower priorities.
   * The default value of false will result in an invariant failure (in development mode) if a lower priority
   * dependency is observed by the observer. This is to prevent priority inversion when scheduling a higher
   * priority observer is dependent upon a lower priority computable value. If the value is true then the no
   * invariant failure is triggered and the component relies on the component author to handle possible priority
   * inversion.
   *
   * @return false if observing lower priority dependencies should result in invariant failure in development mode.
   */
  boolean observeLowerPriorityDependencies() default false;

  /**
   * Enum indicating whether the value of the computable is derived from arez elements and/or external dependencies.
   * If set to {@link DepType#AREZ} then Arez will verify that the method annotated by this annotation accesses arez
   * elements (i.e. instances of {@link arez.ObservableValue} or instances of {@link ComputableValue}). If set to
   * {@link DepType#AREZ_OR_NONE} then the runtime will allow computable to exist with no dependencies. If set
   * to {@link DepType#AREZ_OR_EXTERNAL} then the component must define a {@link ComputableValueRef} method and should invoke
   * {@link ComputableValue#reportPossiblyChanged()} when the non-arez dependencies are changed.
   *
   * @return the types of dependencies allowed on the computable.
   */
  @Nonnull
  DepType depType() default DepType.AREZ;

  /**
   * Return true if the return value of the memoized value should be reported to the Arez spy subsystem.
   * It is useful to disable reporting for large, circular or just uninteresting parameters to the spy infrastructure.
   *
   * @return true to report the return value, false otherwise.
   */
  boolean reportResult() default true;

  /**
   * Indicate whether the memoized value can be read outside a transaction.
   * If the value is {@link Feature#AUTODETECT} then the value will be derived from the
   * {@link ArezComponent#defaultReadOutsideTransaction()} parameter on the {@link ArezComponent} annotation. If
   * the value is set to {@link Feature#ENABLE} then the memoized value can be read outside a transaction. It should
   * be noted that in this scenario the memoized value will be recalculated each time it is accessed.
   *
   * @return flag that determines whether the memoized value allows reads outside a transaction, false to require a transaction to read the memoized value.
   */
  Feature readOutsideTransaction() default Feature.AUTODETECT;
}
