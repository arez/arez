package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Identifies method that is called when the {@link arez.ComputableValue} changes from the UP_TO_DATE state to STALE or POSSIBLY_STALE.
 *
 * <p>This method can only be associated with a {@link Memoize} annotated method that has 0 parameters.
 * This limitation is in place to limit implementation complexity and because no use case for this
 * functionality has been found.</p>
 *
 * <p>The method must also conform to the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must have 0 parameters</li>
 * <li>Must not return a value</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * <li>The method must be accessible to the component subclass which means it must not be package access unless it is in the same package as the arez component.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface OnStale
{
  /**
   * Return the name of the ComputableValue that this method is associated with.
   * This value will be derived if the method name matches the pattern "on[Name]Stale",
   * otherwise it must be specified.
   *
   * @return the name of the ComputableValue.
   */
  @Nonnull
  String name() default "<default>";
}
