package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Identifies method that will be invoked when the dependencies of the paired @Track annotated method are changed.
 *
 * <p>The method that is annotated with OnDepsChanged must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract</li>
 * <li>Must have no parameters</li>
 * <li>Must not return a value</li>
 * <li>Must not throw an exception</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface OnDepsChanged
{
  /**
   * Return the name of the paired Tracked relative to the component.
   * The value must conform to the requirements of a java identifier.
   * The name need not be specified. If the @Track annotated method is
   * named "render" then this will default to being named "onRenderDepsChanged".
   *
   * @return the name of the paired @Track annotated method relative to the component.
   */
  @Nonnull
  String name() default "<default>";
}
