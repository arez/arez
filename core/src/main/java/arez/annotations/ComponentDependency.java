package arez.annotations;

import arez.component.DisposeNotifier;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Methods and fields marked with this annotation are dependencies of the component.
 * If the dependency is disposed then the component takes an action to cascade the dispose
 * or null the property referencing the dependency. The dependency MUST implement the
 * {@link DisposeNotifier} interface.
 *
 * <p>It should be noted that it is preferable for the field that defines the dependency is marked
 * with this annotation rather than the method accessor. The reason is that the annotation processor
 * will issue a warning if a field that the processor identifies as a potential dependency if it is
 * not annotated with {@link ComponentDependency} or {@link CascadeDispose}.</p>
 *
 * <p>If the element annotated is a method then the method must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation except {@link Observable}</li>
 * <li>If not annotated with {@link Observable} then must be final</li>
 * <li>Must have 0 parameters</li>
 * <li>
 *   Must return a value that implements {@link DisposeNotifier} or is annotated with {@link ArezComponent}.
 *   This will be checked at compile-time unless {@link #validateTypeAtRuntime()} set to <code>true</code>.
 * </li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>If not annotated with {@link Observable} then must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * </ul>
 *
 * <p>If the element annotated is a field then the field must comply with the additional constraints:</p>
 * <ul>
 * <li>Must be final</li>
 * <li>Must be a type that implements {@link DisposeNotifier} or is annotated with {@link ArezComponent}</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * </ul>
 */
@Documented
@Target( { ElementType.METHOD, ElementType.FIELD } )
public @interface ComponentDependency
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

  /**
   * Return true and the value of the annotated field will be validated at runtime rather than at compile time.
   * This is useful when the field is defined by an interface but the underlying field is guaranteed to implement
   * {@link DisposeNotifier} at runtime.
   *
   * @return true to defer validation of types until runtime.
   */
  boolean validateTypeAtRuntime() default false;
}
