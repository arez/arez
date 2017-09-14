package org.realityforge.arez.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that marks classes to be processed by Arez annotation processor.
 * Classes with this annotation can contain {@link Observable} properties,
 * {@link Computed} properties and {@link Action} methods.
 *
 * <p>The annotation controls the way that contained actions and observables are
 * named (if names are enabled in the system.</p>
 * <ul>
 * <li>The value returned by {@link #name()} indicates the type name for instances
 * of this object. If not specified it will default to the SimpleName of the class.
 * i.e. The class <tt>com.biz.models.MyModel</tt> will default to a name of
 * "MyModel".</li>
 * <li>The {@link #singleton()} method indicates whether there are expected to be
 * multiple instances of this type or just one. If the method returns true then the
 * debug name of contained observables will not include the "id" of the instance.</li>
 * </ul>
 * <p>The name of any elements contained within the container follows the pattern
 * "<tt>[Container.name].[Container.id].[Element.name]</tt>". If the value of {@link #name()}
 * is the empty string then the "<tt>[Container.name].</tt>" element of name will be elided.
 * If the value of {@link #singleton()} is true then the "<tt>[Container.id].</tt>" element
 * of the name will be elided.</p>
 *
 * <p>The type that is annotated with @Container must comply with the additional constraints:</p>
 * <ul>
 * <li>Must be a class, not an interface or enum</li>
 * <li>Must be concrete, not abstract</li>
 * <li>Must not be final</li>
 * <li>Must not be a non-static nested class</li>
 * <li>Must have at least one method annotated with {@link Action}, {@link Computed} or {@link Observable}</li>
 * </ul>
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface Container
{
  /**
   * Return the name of the type.
   * The value must conform to the requirements of a java identifier.
   * This can be set to the empty string in which case the {@link Observable}, {@link Computed} and
   * {@link Action} sub-elements will have no type prefix.
   *
   * @return the name of the type.
   */
  String name() default "<default>";

  /**
   * Return true if the container can only have a single instance, false otherwise.
   *
   * @return true if the container can only have a single instance, false otherwise.
   */
  boolean singleton() default false;

  /**
   * Return true if the generated container should implement Disposable interface.
   *
   * @return true if the generated container should implement Disposable interface, false otherwise.
   */
  boolean disposable() default true;
}
