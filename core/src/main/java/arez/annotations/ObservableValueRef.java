package arez.annotations;

import arez.ObservableValue;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Marks a template method that returns the {@link ObservableValue} instance for
 * the {@link Observable} annotated property. Each property marked with the {@link Observable} annotation is backed
 * by an {@link ObservableValue} instance and some frameworks make use of this value to implement
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
 * <li>Must return an instance of {@link ObservableValue}.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface ObservableValueRef
{
  /**
   * Return the name of the associated ObservableValue property that this ref relates to.
   * This value will be derived if the method name matches the pattern "get[Name]ObservableValue",
   * otherwise it must be specified.
   *
   * @return the name of the associated ObservableValue.
   */
  @Nonnull
  String name() default "<default>";
}