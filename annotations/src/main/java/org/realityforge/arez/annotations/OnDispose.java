package org.realityforge.arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Identifies method that is called when the ComputedValue is disposed.
 *
 * <p>The method must also conform to the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with {@link Autorun}, {@link ComponentId}, {@link Observable}, {@link Computed}, {@link Action}, {@link javax.annotation.PostConstruct}, {@link PreDispose}, {@link PostDispose}, {@link OnActivate} or {@link OnStale}</li>
 * <li>Must have 0 parameters</li>
 * <li>Must not return a value</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface OnDispose
{
  /**
   * Return the name of the ComputedValue that this method is associated with.
   * This value of the name will be derived if the method name matches the pattern "on[Name]Dispose",
   * otherwise it must be specified.
   *
   * @return the name of the ComputedValue.
   */
  @Nonnull
  String name() default "<default>";
}
