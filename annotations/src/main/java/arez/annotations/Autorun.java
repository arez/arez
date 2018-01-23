package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Methods marked with this annotation are converted to autoruns.
 *
 * <p>The method that is annotated with @Autorun must comply with the additional constraints:</p>
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
public @interface Autorun
{
  /**
   * Return the name of the Autorun relative to the component.
   * The value must conform to the requirements of a java identifier.
   * The name must also be unique across {@link Observable}s,
   * {@link Computed}s and {@link Action}s within the scope of the
   * {@link ArezComponent} annotated element.
   *
   * @return the name of the Autorun relative to the component.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * Does the autorun mutate state or not.
   * The default value us false thus making the transaction mode read-only. The intention is that autorun observers
   * should primarily be reflecting Arez state to external systems. (i.e. views, network layers etc.)
   *
   * @return true if the autorun should run in a READ_WRITE transaction, false if it should run in a READ_ONLY transaction.
   */
  boolean mutation() default false;
}
