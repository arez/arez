package org.realityforge.arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Annotation applied to methods that expose an Observable value in Arez.
 * Methods annotated with this either query state or mutate state. The query
 * method is expected to have 0 parameters and return a value and by default
 * is named with "get" or "is" prefixed to the property name. The mutation
 * method is expected to have a single parameter and return no value and by
 * default is named with "set" prefixed to property name. The setter or getter
 * can also be named matching the property name without the prefix.
 *
 * <p>Only one of the query or mutation method needs to be annotated with
 * this annotation if the other method follows the normal conventions. If
 * the other method does not conform to conventions, then you will need to
 * annotate the pair and specify a value for {@link #name()}.</p>
 *
 * <p>The method should only invoked within the scope of a transaction.
 * The mutation method requires that the transaction be READ_WRITE.</p>
 *
 * <p>The method that is annotated with @Observable must also comply with the following constraints:</p>
 * <ul>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must not be abstract</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface Observable
{
  /**
   * Return the name of the Observable relative to the container. If not specified
   * will default to the name of the property by convention as described above.
   * The value must conform to the requirements of a java identifier.
   * The name must also be unique across {@link Observable}s,
   * {@link Computed}s and {@link Action}s within the scope of the
   * {@link Container} annotated element.
   *
   * @return the name of the Observable relative to the container.
   */
  @Nonnull
  String name() default "<default>";
}
