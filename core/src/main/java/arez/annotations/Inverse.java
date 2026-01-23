package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * The Inverse annotation is used to annotate the reverse direction of the relationship annotated
 * by the {@link Reference} annotation. The <code>Inverse</code> annotation MUST be paired with a
 * reference. The method annotated with this annotation must return a class annotated with
 * the {@link ArezComponent} annotation or a {@link java.util.Collection}, a {@link java.util.List} or a
 * {@link java.util.Set} where the type parameter refers to a class annotated with the
 * {@link ArezComponent} annotation. If the method returns a non-collection type then the type
 * must also be annotated with either {@link javax.annotation.Nonnull} or {@link javax.annotation.Nullable}.
 *
 * <p>The method must also conform to the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must have 0 parameters</li>
 * <li>Must return a value.</li>
 * <li>Must be abstract</li>
 * <li>Must not throw exceptions</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface Inverse
{
  /**
   * Return the name of the inverse relative to the component. The value must conform
   * to the requirements of a java identifier. If not specified, the name will be derived by assuming
   * the naming convention "get[Name]" or failing that the name will be the method name.
   *
   * @return the name of the inverse relative to the component.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * Return the name of the reference that the inverse is associated with. The value must conform
   * to the requirements of a java identifier. If not specified, the name will be derived by assuming
   * that the reference name is the camelCase name of the class on which the {@code Inverse} annotation
   * appears unless the {@code Inverse} appears on an interface in which case it is the class annotated
   * with {@link ArezComponent}.
   *
   * @return the name of the reference relative to the component.
   */
  @Nonnull
  String referenceName() default "<default>";
}
