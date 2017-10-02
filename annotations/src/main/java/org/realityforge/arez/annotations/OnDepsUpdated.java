package org.realityforge.arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Identifies method that will be invoked when the dependencies of the paired @Track annotated method are updated.
 *
 * <p>The method that is annotated with OnDepsUpdated must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with {@link ComponentId}, {@link Action}, {@link Observable}, {@link Computed}, {@link javax.annotation.PostConstruct}, {@link PreDispose}, {@link PostDispose}, {@link OnActivate}, {@link OnDeactivate} or {@link OnStale}</li>
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
public @interface OnDepsUpdated
{
  /**
   * Return the name of the paired Tracked relative to the component.
   * The value must conform to the requirements of a java identifier.
   * The name need not be specified. If the @Track annotated method is
   * named "render" then this will default to being named "onRenderDepsUpdate".
   *
   * @return the name of the paired @Track annotated method relative to the component.
   */
  @Nonnull
  String name() default "<default>";
}
