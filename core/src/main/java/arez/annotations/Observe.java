package arez.annotations;

import arez.ComputableValue;
import arez.Observer;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Annotation that marks a method as observed.
 * Any {@link arez.ObservableValue} instances or {@link ComputableValue} instances accessed within the
 * scope of the method will be added as a dependency of the observer. If any of these dependencies are changed
 * then the runtime will invoke the associated {@link OnDepsChange} method if present or re-schedule the observed
 * method for execution if there is no {@link OnDepsChange} method present. Note that Arez will attempt to detect
 * the {@link OnDepsChange} method using naming conventions even if there is no method explicitly annotated.
 *
 * <p>The method that is annotated with this annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must not be abstract</li>
 * </ul>
 *
 * <p>If the {@link #executor()} is set to {@link Executor#INTERNAL} then the method must also
 * comply with the following additional constraints:</p>
 * <ul>
 * <li>Must not be public</li>
 * <li>Must have 0 parameters</li>
 * <li>Must not return a value</li>
 * <li>Must not throw exceptions</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface Observe
{
  /**
   * Return the name of the Observer relative to the component.
   * The value must conform to the requirements of a java identifier.
   * The name must also be unique across {@link Observable}s,
   * {@link Memoize}s and {@link Action}s within the scope of the
   * {@link ArezComponent} annotated element.
   *
   * @return the name of the Observer relative to the component.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * Does the observer's tracking method change arez state or not.
   * Observers are primarily used to reflect Arez state onto external systems (i.e. views, network layers etc.)
   * and thus the default value is false thus making the transaction mode read-only.
   *
   * @return true if the observer should use a read-write transaction, false if it should use a read-only transaction.
   */
  boolean mutation() default false;

  /**
   * The priority of the underlying observer
   *
   * @return the priority of the observer.
   */
  Priority priority() default Priority.DEFAULT;

  /**
   * The actor responsible for calling the observed method.
   *
   * @return the actor responsible for calling the observed method.
   */
  Executor executor() default Executor.INTERNAL;

  /**
   * Flag controlling whether the observer can observe ComputableValue instances with lower priorities.
   * The default value of false will result in an invariant failure (in development mode) if a lower priority
   * dependency is observed by the observer. This is to prevent priority inversion when scheduling a higher
   * priority observer that is dependent upon a lower priority computable value. If the value is true then the no
   * invariant failure is triggered and the component relies on the component author to handle possible priority
   * inversion.
   *
   * @return false if observing lower priority dependencies should result in invariant failure in development mode.
   */
  boolean observeLowerPriorityDependencies() default false;

  /**
   * Can the observer invoke actions.
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
  boolean nestedActionsAllowed() default false;

  /**
   * Return true if the parameters should be reported to the core Arez runtime.
   * It is useful to disable reporting for large, circular or just uninteresting parameters to the spy infrastructure.
   *
   * @return true to report the parameters, false otherwise.
   */
  boolean reportParameters() default true;

  /**
   * Return true if the return value of the observed function (if any) should be reported to the Arez spy subsystem.
   * It is useful to disable reporting for large, circular or just uninteresting parameters to the spy infrastructure.
   * This is only useful if the value of {@link #executor()} is set to {@link Executor#EXTERNAL} as otherwise the
   * result is not reported anyway.
   *
   * @return true to report the return value, false otherwise.
   */
  boolean reportResult() default true;

  /**
   * Enum indicating whether the Observer is derived from arez elements and/or external dependencies.
   * If set to {@link DepType#AREZ} then the arez runtime will verify that the method annotated by this
   * annotation accesses arez elements (i.e. instances of {@link arez.ObservableValue} or instances of
   * {@link ComputableValue}). If set to {@link DepType#AREZ_OR_NONE} then the runtime will allow
   * observed to exist with no dependencies. If set to {@link DepType#AREZ_OR_EXTERNAL} then the component
   * must define a {@link ObserverRef} method and should invoke {@link Observer#reportStale()} when the
   * non-arez dependencies are changed.
   *
   * @return the type of dependencies allowed in the observed method.
   */
  @Nonnull
  DepType depType() default DepType.AREZ;
}
