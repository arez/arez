package org.realityforge.arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Methods marked with this annotation are run in tracking transactions.
 * Any Observables or ComputedValues accessed within the scope of the method will be added
 * as a dependency of the tracker. If any of these dependencies are changed then class will
 * be notified by way of a "OnDepsUpdated" method. The method can be explicitly marked with the
 * {@link OnDepsUpdated} annotation or the framework will attempt to use the method named according
 * to naming conventions. i.e. If the Tracked method is named "render" then the OnDepsUpdated method
 * will expect to be named "onRenderDepsUpdate".
 *
 * <p>The method that is annotated with @Tracked must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with {@link ContainerId}, {@link Action}, {@link Observable}, {@link Computed}, {@link javax.annotation.PostConstruct}, {@link PreDispose}, {@link PostDispose}, {@link OnActivate}, {@link OnDeactivate} or {@link OnStale}</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must not be abstract</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface Tracked
{
  /**
   * Return the name of the Tracked relative to the container.
   * The value must conform to the requirements of a java identifier.
   * The name must also be unique across {@link Observable}s,
   * {@link Computed}s and {@link Action}s within the scope of the
   * {@link Container} annotated element.
   *
   * @return the name of the Tracked relative to the container.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * Does the tracker mutate state or not.
   *
   * @return true if the tracker should run in a READ_WRITE transaction, false if it should run in a READ_ONLY transaction.
   */
  boolean mutation() default true;
}
