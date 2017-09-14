package org.realityforge.arez.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods marked with this annotation are observable values within Arez.
 *
 * <p>The method should only invoked within the scope of a transaction.</p>
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface Observable
{
  /**
   * Return the name of the Observable relative to the container.
   * The value must conform to the requirements of a java identifier.
   * The name must also be unique across {@link Observable}s,
   * {@link Computed}s and {@link Action}s within the scope of the
   * {@link Container} annotated element.
   *
   * @return the name of the Observable relative to the container.
   */
  String name() default "<default>";
}
