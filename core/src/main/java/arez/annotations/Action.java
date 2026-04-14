package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Methods marked with this annotation are invoked in an Arez transaction.
 *
 * <p>The method that is annotated with this annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must not be abstract</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
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
   * {@link Memoize}s and {@link Action}s within the scope of the
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
   * Return true if the parameters should be reported to the Arez spy subsystem.
   * It is useful to disable reporting for large, circular or just uninteresting parameters to the spy infrastructure.
   *
   * @return true to report the parameters, false otherwise.
   */
  boolean reportParameters() default true;

  /**
   * Return true if the return value of the action (if any) should be reported to the Arez spy subsystem.
   * It is useful to disable reporting for large, circular or just uninteresting parameters to the spy infrastructure.
   *
   * @return true to report the return value, false otherwise.
   */
  boolean reportResult() default true;

  /**
   * True if the action should always start a new transaction. A false value indicates that the action will
   * use the invoking transaction if present, otherwise will create a new transaction to invoke action.
   *
   * @return true if the action will create a new transaction, false if it will use the existing transaction if present.
   */
  boolean requireNewTransaction() default false;

  /**
   * Flag indicating whether the code should verify that at least one read or write occurs within
   * the scope of the action.
   *
   * @return true to verify action reads or writes observable data.
   */
  boolean verifyRequired() default true;

  /**
   * True if the action will be skipped if the owning component is disposed. The default is false, in which case an
   * invariant will verify that the action is not called on a disposed component. If this parameter is true, then
   * the action must be a void method.
   *
   * @return true if the action will be skipped if the owning component is disposed, false if the component must not
   * be disposed.
   */
  boolean skipIfDisposed() default false;
}
