package org.realityforge.arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Methods marked with this annotation are invoked in an Arez transaction.
 *
 * <p>The method that is annotated with @Action must comply with the additional constraints:</p>
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
public @interface Action
{
  /**
   * Return the name of the Action relative to the component.
   * The value must conform to the requirements of a java identifier.
   * The name must also be unique across {@link Observable}s,
   * {@link Computed}s and {@link Action}s within the scope of the
   * {@link ArezComponent} annotated element.
   *
   * @return the name of the Action relative to the component.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * Does the action mutate state or not.
   *
   * @return true if method should be wrapped in READ_WRITE transaction, false if it should it should be wrapped in READ_ONLY transaction.
   */
  boolean mutation() default true;

  /**
   * Return true if the parameters should be reported to the core Arez runtime.
   * It is useful to disable reporting for large, circular or just uninteresting parameters to the spy infrastructure.
   *
   * @return true to report the parameters, false otherwise.
   */
  boolean reportParameters() default true;
}
