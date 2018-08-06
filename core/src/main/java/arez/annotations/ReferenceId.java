package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * This annotation designates the method used to retrieve the id of the reference. It can be placed
 * on the getter of an {@link Observable} property or on a normal getter method if the id is never
 * expected to change. See the {@link Reference} docs for how the method is used.
 *
 * <p>The method must also conform to the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any arez annotations other than @Observable</li>
 * <li>Must have 0 parameters</li>
 * <li>Must return a value</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not throw exceptions</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface ReferenceId
{
  /**
   * Return the name of the reference relative to the component. The value must conform
   * to the requirements of a java identifier. If not specified, the name will be derived by assuming
   * the naming convention "get[Name]Id" or "[name]Id" otherwise a compile failure will generated.
   *
   * @return the name of the reference relative to the component.
   */
  @Nonnull
  String name() default "<default>";
}
