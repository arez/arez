package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Methods marked with this annotation are ComputedValues within Arez.
 *
 * <p>The return value should be derived from other Observables within the Arez system
 * and the value returned by the method should not change unless the state of the other
 * {@link Observable}s change. The method is wrapped in a READ_ONLY transaction and
 * thus can not modify other state in the system.</p>
 *
 * <p>The method that is annotated with @Computed must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must have 0 parameters</li>
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
public @interface Computed
{
  /**
   * Return the name of the ComputedValue relative to the component.
   * The value must conform to the requirements of a java identifier.
   * The name must also be unique across {@link Observable}s,
   * {@link Computed}s and {@link Action}s within the scope of the
   * {@link ArezComponent} annotated element.
   *
   * @return the name of the ComputedValue relative to the component.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * A flag indicating whether the computed should be "kept alive". A computed that is kept alive
   * is activated on creation and never deactivates. This is useful if the computed property is only
   * accessed from within actions but should be kept up to date and not recomputed on each access.
   *
   * @return true to keep computed alive.
   */
  boolean keepAlive() default false;

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
