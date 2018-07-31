package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Methods marked with this annotation are <a href="https://en.wikipedia.org/wiki/Memoization">memoized</a> while there is an observer.
 *
 * <p>The return value should be derived from the parameters and any other Observables or ComputedValues
 * within the Arez system. The value returned by the method should not change unless the parameters or the
 * state of the other {@link Observable}s change. The method is wrapped in a READ_ONLY transaction and
 * thus can not modify other state in the system.</p>
 *
 * <p>This is implemented by creating a separate ComputedValue instance for each unique combination of
 * parameters. When the ComputedValue is deactivated, a hook triggers that removes the ComputedValue
 * from the local cache.</p>
 *
 * <p>The method that is annotated with @Memoize must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must have 1 or more parameters</li>
 * <li>Must return a value</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface Memoize
{
  /**
   * Return the root name of the Memoized value relative to the component. This
   * will be used in combination with a sequence when naming the synthesized
   * ComputedValue instances. The value must conform to the requirements of a
   * java identifier. The name must also be unique across {@link Observable}s,
   * {@link Computed}s and {@link Action}s within the scope of the
   * {@link ArezComponent} annotated element.
   *
   * @return the root name of the Memoized value relative to the component.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * The priority of the underlying ComputedValue observer
   *
   * @return the priority of the ComputedValue observer.
   */
  Priority priority() default Priority.NORMAL;

  /**
   * Flag controlling whether the underlying observer can observe ComputedValue instances with lower priorities.
   * The default value of false will result in an invariant failure (in development mode) if a lower priority
   * dependency is observed by the observer. This is to prevent priority inversion when scheduling a higher
   * priority observer is dependent upon a lower priority computed value. If the value is true then the no
   * invariant failure is triggered and the component relies on the component author to handle possible priority
   * inversion.
   *
   * @return false if observing lower priority dependencies should result in invariant failure in development mode.
   */
  boolean observeLowerPriorityDependencies() default false;
}
