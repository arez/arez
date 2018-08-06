package arez.annotations;

import arez.component.Locator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * A Reference annotation is placed on an abstract method that will resolve to a referenced object.
 * The reference annotation is paired with a {@link ReferenceId} method that returns the <code>id</code>
 * of the referenced object. The <code>type</code> of the referenced object is the return type of the method
 * annotated with <code>@Reference</code>. The <code>type</code> and <code>id</code> are passed to the
 * {@link Locator#findById(Class, Object)} method when the reference is resolved.
 *
 * * <p>The reference can be resolved either eagerly (during a linking phase or when it is modified) or
 * lazily (when accessed).</p>
 *
 * <p>The method must also conform to the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must have 0 parameters</li>
 * <li>Must return a value</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must be abstract</li>
 * <li>Must not throw exceptions</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface Reference
{
  /**
   * Return the name of the reference relative to the component. The value must conform
   * to the requirements of a java identifier. If not specified, the name will be derived by assuming
   * the naming convention "get[Name]" or failing that the name will be the method name.
   *
   * @return the name of the reference relative to the component.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * Return the strategy for resolving reference.
   *
   * @return the strategy for resolving reference.
   */
  @Nonnull
  LinkType load() default LinkType.EAGER;
}
