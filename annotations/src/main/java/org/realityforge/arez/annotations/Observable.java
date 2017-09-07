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
   *
   * @return the name of the Observable relative to the container.
   */
  String name() default "<default>";
}
