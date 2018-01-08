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
}
