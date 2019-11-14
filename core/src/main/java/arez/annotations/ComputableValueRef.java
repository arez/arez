package arez.annotations;

import arez.ComputableValue;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Marks a template method that returns the {@link ComputableValue} instance for
 * the {@link Memoize} annotated property. Each property marked with the {@link Memoize} annotation is backed
 * by an {@link ComputableValue} instance and some frameworks make use of this value to implement
 * advanced functionality.
 *
 * <p>The method that is annotated with this annotation must also comply with the following constraints:</p>
 * <ul>
 * <li>Must have the exact same parameter types as the associated {@link Memoize} annotated method</li>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must be abstract</li>
 * <li>Must not throw any exceptions</li>
 * <li>Must return an instance of {@link ComputableValue}.</li>
 * <li>The method must be accessible to the component subclass which means it must not be package access unless it is in the same package as the arez component.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface ComputableValueRef
{
  /**
   * Return the name of the associated Memoize property that this ref relates to.
   * This value will be derived if the method name matches the pattern "get[Name]ComputableValue",
   * otherwise it must be specified.
   *
   * @return the name of the associated ComputableValue.
   */
  @Nonnull
  String name() default "<default>";
}
