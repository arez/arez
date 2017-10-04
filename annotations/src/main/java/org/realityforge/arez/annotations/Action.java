package org.realityforge.arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Methods marked with this annotation are invoked in an Arez transaction.
 *
 * <p>The method that is annotated with @Action must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with {@link Autorun}, {@link ComponentId}, {@link Observable}, {@link Computed}, {@link javax.annotation.PostConstruct}, {@link PreDispose}, {@link PostDispose}, {@link OnActivate}, {@link OnDeactivate} or {@link OnStale}</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must not be abstract</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface Action
{
  /**
   * Return the name of the Action relative to the component.
   * The value must conform to the requirements of a java identifier.
   * The name must also be unique across {@link Observable}s,
   * {@link Computed}s and {@link Action}s within the scope of the
   * {@link ArezComponent} annotated element.
   *
   * @return the name of the Action relative to the component.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * Does the action mutate state or not.
   *
   * @return true if method should be wrapped in READ_WRITE transaction, false if it should it should be wrapped in READ_ONLY transaction.
   */
  boolean mutation() default true;

  /**
   * Return true if the generated action wrapper should pass the parameters to the core Arez runtime.
   * Sometimes it is useful to disable this to avoid passing large, circular or just uninteresting
   * parameters to the spy infrastructure.
   *
   * @return true if the generated action should pass action parameters to core runtime.
   */
  boolean parameters() default true;
}
