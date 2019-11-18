package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Identifies method that will be invoked when the dependencies of the paired {@link Observe} annotated method are changed.
 *
 * <p>The method that is annotated with this annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract</li>
 * <li>Must have either no parameters or a single parameter of type {@link arez.Observer}</li>
 * <li>Must not return a value</li>
 * <li>Must not throw an exception</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * </ul>
 *
 * <p>If the annotated method has a parameter of type {@link arez.Observer} then the underlying {@link arez.Observer}
 * instance associated with the {@link Observe}/{@link OnDepsChange} annotated method is passed to the method when
 * dependencies change. This is extremely useful when implementing asynchronous callbacks.</p>
 */
@Documented
@Target( ElementType.METHOD )
public @interface OnDepsChange
{
  /**
   * Return the name of the paired Tracked relative to the component.
   * The value must conform to the requirements of a java identifier.
   * The name need not be specified. If the {@link Observe} annotated method is
   * named "render" then this will default to being named "onRenderDepsChange".
   *
   * @return the name of the paired {@link Observe} annotated method relative to the component.
   */
  @Nonnull
  String name() default "<default>";
}
