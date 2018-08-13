package arez.annotations;

import arez.Locator;
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

  /**
   * Return the enum controlling whether there is an inverse for reference.
   * {@link Feature#ENABLE} tells the annotation processor to expect an inverse and add code
   * to maintain the inverse. {@link Feature#DISABLE} will generate no code to maintain
   * inverse module. {@link Feature#AUTODETECT} will be treated as {@link Feature#ENABLE}
   * if either the {@link #inverseName} or {@link #inverseMultiplicity} is specified.
   *
   * @return the enum controlling whether there is an inverse for reference
   */
  @Nonnull
  Feature inverse() default Feature.AUTODETECT;

  /**
   * Return the name of the inverse associated with the reference. The value must conform
   * to the requirements of a java identifier. If not specified, the name will be derived
   * by camelCasing the simple name of the class on which the {@link Reference} annotation
   * is placed and then adding an s if {@link #inverseMultiplicity} is {@link Multiplicity#MANY}.
   *
   * @return the name of the reference relative to the component.
   */
  @Nonnull
  String inverseName() default "<default>";

  /**
   * Define the expected multiplicity of the inverse associated with the reference.
   *
   * @return the expected multiplicity of the inverse associated with the reference.
   */
  @Nonnull
  Multiplicity inverseMultiplicity() default Multiplicity.MANY;
}
