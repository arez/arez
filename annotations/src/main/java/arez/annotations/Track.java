package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Methods marked with this annotation are run in tracking transactions.
 * Any Observables or ComputedValues accessed within the scope of the method will be added
 * as a dependency of the tracker. If any of these dependencies are changed then class will
 * be notified by way of a "OnDepsChanged" method. The method can be explicitly marked with the
 * {@link OnDepsChanged} annotation or the framework will attempt to use the method named according
 * to naming conventions. i.e. If the @Track annotated method is named "render" then the @OnDepsChanged
 * annotated method will have default to being named "onRenderDepsUpdate".
 *
 * <p>The method that is annotated with @Track must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must not be abstract</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface Track
{
  /**
   * Return the name of the Tracked relative to the component.
   * The value must conform to the requirements of a java identifier.
   * The name must also be unique across {@link Observable}s,
   * {@link Computed}s and {@link Action}s within the scope of the
   * {@link ArezComponent} annotated element.
   *
   * @return the name of the Tracked relative to the component.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * Does the tracker mutate state or not.
   * The default value us false thus making the transaction mode read-only. The intention is that tracker observers
   * should primarily be reflecting Arez state to external systems. (i.e. views, network layers etc.)
   *
   * @return true if the tracker should run in a READ_WRITE transaction, false if it should run in a READ_ONLY transaction.
   */
  boolean mutation() default false;

  /**
   * Return true if the parameters should be reported to the core Arez runtime.
   * It is useful to disable reporting for large, circular or just uninteresting parameters to the spy infrastructure.
   *
   * @return true to report the parameters, false otherwise.
   */
  boolean reportParameters() default true;

  /**
   * Is the observer scheduled at a high-priority.
   * A high priority observer is placed at the start of the scheduler queue when is scheduled, meaning it will
   * react next unless another high priority observer or ComputedValue needs rescheduling.
   *
   * <p>A user should be very careful when marking a @Track as a high priority as it is possible that
   * the method will be scheduled part way through the process of disposing one-or-more components (in
   * most environments dispose reactions are the only high priority observer and thus a partially disposed
   * state is never exposed to user code). In most cases this may mean invoking
   * <code>Disposable.isDisposed(component)</code> before accessing arez components. A high priority @Track
   * should only access high priority @Computed annotated methods to ensure consistency.</p>
   *
   * @return true if the observer is scheduled at a high-priority, false otherwise.
   */
  boolean highPriority() default false;
}
