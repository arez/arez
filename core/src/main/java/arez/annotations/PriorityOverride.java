package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Marks a method that can dynamically override the priority of the associated {@link Observe} annotated
 * method or the associated {@link Memoize} annotated method.
 *
 * <p>The method that is annotated with this annotation must also comply with the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be be abstract</li>
 * <li>Must not throw any exceptions</li>
 * <li>Must return an int priority.</li>
 * <li>May accept zero parameters or a single int parameter that indicates default priority.</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface PriorityOverride
{
  /**
   * Return the name of the associated Observer or Memoize that this override relates to.
   * This value will be derived if the method name matches the pattern "[name]Priority",
   * otherwise it must be specified.
   *
   * @return the name of the associated Observer or Memoize.
   */
  @Nonnull
  String name() default "<default>";
}
