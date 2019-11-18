package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Identifies method that is called when the {@link arez.ComputableValue} changes from the INACTIVE state to any other state.
 *
 * <p>This method can only be associated with a {@link Memoize} annotated method that has 0 parameters.
 * This limitation is in place to limit implementation complexity and because no use case for this
 * functionality has been found.</p>
 *
 * <p>The method must also conform to the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must have either 0 parameters or a single parameter of type {@link arez.ComputableValue}</li>
 * <li>Must not return a value</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * <li>Must not be for a {@link Memoize} method that has the {@link Memoize#keepAlive} parameter set to true</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface OnActivate
{
  /**
   * Return the name of the ComputableValue that this method is associated with.
   * This value will be derived if the method name matches the pattern "on[Name]Activate",
   * otherwise it must be specified.
   *
   * @return the name of the ComputableValue.
   */
  @Nonnull
  String name() default "<default>";
}
