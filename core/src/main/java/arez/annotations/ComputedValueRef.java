package arez.annotations;

import arez.ComputableValue;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Marks a template method that returns the {@link ComputableValue} instance for
 * the {@link Computed} annotated property. Each property marked with the {@link Computed} annotation is backed
 * by an {@link ComputableValue} instance and some frameworks make use of this value to implement
 * advanced functionality.
 *
 * <p>The method that is annotated with this annotation must also comply with the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must be abstract</li>
 * <li>Must not throw any exceptions</li>
 * <li>Must return an instance of {@link ComputableValue}.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface ComputedValueRef
{
  /**
   * Return the name of the associated Computed property that this ref relates to.
   * This value will be derived if the method name matches the pattern "get[Name]ComputableValue",
   * otherwise it must be specified.
   *
   * @return the name of the associated ComputableValue.
   */
  @Nonnull
  String name() default "<default>";
}
