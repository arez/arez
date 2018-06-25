package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Methods marked with this annotation are dependencies of the component.
 * If the dependency is disposed then the component takes an action to cascade the dispose
 * or null the property referencing dependency. The dependency MUST implement the
 * {@link arez.component.DisposeTrackable} interface.
 *
 * <p>The method that is annotated with @Dependency must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation except {@link Observable}</li>
 * <li>Must have 0 parameters</li>
 * <li>Must return a value that implements {@link arez.component.DisposeTrackable} or is annotated with {@link ArezComponent}</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface Dependency
{
  /**
   * The action to take when dependency is disposed.
   */
  enum Action
  {
    /**
     * Remove the component.
     */
    CASCADE,
    /**
     * Set the {@link Observable} property that holds dependency to null.
     */
    SET_NULL
  }

  /**
   * Return the action to take when the dependency is disposed.
   * A {@link Action#CASCADE} value indicates that the component should be deleted, while a
   * {@link Action#SET_NULL} value indicates that the observable field should be set to null.
   * The {@link Action#SET_NULL} is only valid on {@link Observable} properties that have an
   * associated setter and are not annotated with {@link javax.annotation.Nonnull}.
   *
   * @return the action to take when the dependency is disposed.
   */
  @Nonnull
  Action action() default Action.CASCADE;
}
