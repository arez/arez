package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Annotation applied to a static field or static method that supplies the initial value for an
 * {@link Observable} property.
 *
 * <p>The annotation should be paired with an abstract {@link Observable} property on the same component.
 * The annotated element must be a static, final field or a static method. The initial value will be used
 * when constructing the generated component and removes the need to pass the value via the constructor.</p>
 *
 * <p>If the associated {@link Observable} getter is annotated with {@link javax.annotation.Nonnull} then
 * the annotated field or method should also be annotated with {@link javax.annotation.Nonnull}.</p>
 */
@Documented
@Target( { ElementType.METHOD, ElementType.FIELD } )
public @interface ObservableInitial
{
  /**
   * Return the name of the associated Observable.
   *
   * <p>If the annotation is applied to a method, this value will be derived if the method name matches
   * the pattern "getInitial[Name]", otherwise it must be specified.</p>
   *
   * <p>If the annotation is applied to a field, this value will be derived if the field name matches
   * the pattern "INITIAL_[NAME]", otherwise it must be specified.</p>
   *
   * @return the name of the Observable.
   */
  @Nonnull
  String name() default "<default>";
}
