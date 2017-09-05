package org.realityforge.arez.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks classes that contain Observable properties.
 *
 * <p>The annotation controls the way that contained observables are named. If names are
 * not enabled in the system, this annotation has no effect.</p>
 *
 * <p>The {@link #name()} value indicates the type name for all instances of this object.
 * If not specified it will default to the SimpleName of the class. i.e. The class
 * <tt>com.biz.models.MyModel</tt> will default to a name of "MyModel".</p>
 *
 * <p>The {@link #singleton()} indicates whether there are expected to be multiple
 * instances of this type. If the method returns true then the debug name of
 * contained observables will not include the "id" of the instance.</p>
 *
 * <p>The name of any observables contained within the container follows the pattern
 * "[Container.name].[Container.id].[Observable.name]" for non singletons and
 * "[Container.name].[Observable.name]" for singletons.</p>
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface Container
{
  /**
   * Return the name of the type.
   *
   * @return the name of the type.
   */
  String name() default "";

  /**
   * Return true if the container can only have a single instance, false otherwise.
   *
   * @return true if the container can only have a single instance, false otherwise.
   */
  boolean singleton() default false;
}
