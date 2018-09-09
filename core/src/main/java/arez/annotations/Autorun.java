package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Methods marked with this annotation are converted to autoruns.
 * Autorun methods are invoked by the Arez runtime and should never be invoked directly by user code.
 *
 * <p>The method that is annotated with @Autorun must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be public</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must not be abstract</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface Autorun
{
  /**
   * Return the name of the Autorun relative to the component.
   * The value must conform to the requirements of a java identifier.
   * The name must also be unique across {@link Observable}s,
   * {@link Computed}s and {@link Action}s within the scope of the
   * {@link ArezComponent} annotated element.
   *
   * @return the name of the Autorun relative to the component.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * Does the autorun mutate state or not.
   * The default value us false thus making the transaction mode read-only. The intention is that autorun observers
   * should primarily be reflecting Arez state to external systems. (i.e. views, network layers etc.)
   *
   * @return true if the autorun should run in a READ_WRITE transaction, false if it should run in a READ_ONLY transaction.
   */
  boolean mutation() default false;

  /**
   * The priority of the underlying autorun observer
   *
   * @return the priority of the autorun observer.
   */
  Priority priority() default Priority.NORMAL;

  /**
   * Flag controlling whether the underlying observer can observe ComputedValue instances with lower priorities.
   * The default value of false will result in an invariant failure (in development mode) if a lower priority
   * dependency is observed by the observer. This is to prevent priority inversion when scheduling a higher
   * priority observer is dependent upon a lower priority computed value. If the value is true then the no
   * invariant failure is triggered and the component relies on the component author to handle possible priority
   * inversion.
   *
   * @return false if observing lower priority dependencies should result in invariant failure in development mode.
   */
  boolean observeLowerPriorityDependencies() default false;

  /**
   * Flag controlling whether the observer can invoke actions.
   * An action that specifies {@link Action#requireNewTransaction()} as true will start a new transaction
   * and any observables accessed within the action will not be dependencies of the observer. Sometimes this
   * behaviour is desired. Sometimes an action that specifies {@link Action#requireNewTransaction()} as false
   * will be used instead and any observable accessed within the scope of the action will be a dependency of
   * the observer and thus changes in the observable will reschedule the observer. Sometimes this
   * behaviour is desired. Either way the developer must be conscious of these decisions and thus must explicitly
   * set this flag to true to invoke any actions within the scope of the observers reaction.
   *
   * @return true if the observer can invoke actions, false otherwise.
   */
  boolean canNestActions() default false;
}
