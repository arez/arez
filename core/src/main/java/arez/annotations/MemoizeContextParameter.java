package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;
import org.intellij.lang.annotations.Language;

/**
 * Annotation applied to methods that define a single "contextual" parameter to one or more {@link Memoize}
 * annotated methods. A "contextual" parameter is one that is used within the {@link Memoize} annotated methods,
 * but is not passed in but derived from the calling context. These are the conceptual equivalent of thread-local
 * values accessed from within the function.
 *
 * <p>There are expected to be a three methods for each context parameter: one to get the value from the
 * calling context (optionally prefixed with "capture" that returns the type of the context parameter), one
 * to push the value to the calling context (prefixed with "push" with a single parameter that has the
 * type of the context parameter), and one to pop the value from the calling context (prefixed with "pop"
 * with a single parameter that has the type of the context parameter).</p>
 *
 * <h2>Capture Method</h2>
 *
 * <p>The method to capture the context parameter is invoked prior to the invocation of the {@link Memoize}
 * annotated method when non-arez-framework code invokes the method. It may be invoked outside of an arez
 * transaction if the associated method has {@link Memoize#readOutsideTransaction()} resolve to
 * {@link Feature#DISABLE}. The method must also comply with the following additional constraints:</p>
 *
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must return a value</li>
 * <li>Must not have any parameters</li>
 * <li>Must not specify type parameters</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * </ul>
 *
 * <h2>Push Method</h2>
 *
 * <p>The method to push the context parameter into the current context is invoked prior to the invocation
 * of the {@link Memoize} annotated method when the arez framework invokes the method to determine whether
 * the result has changed. It is invoked outside of an arez transaction. The method must also comply
 * with the following additional constraints:</p>
 *
 * <p>The method that is annotated with this annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>The method name must start with "push"</li>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not return a value</li>
 * <li>Must have one parameter</li>
 * <li>Must not specify type parameters</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * </ul>
 *
 * <h2>Pop Method</h2>
 *
 * <p>The method to pop the context parameter from the current context is invoked after to the invocation
 * of the {@link Memoize} annotated method when the arez framework invokes the method to determine whether
 * the result has changed. It is invoked outside of an arez transaction. The method must also comply
 * with the following additional constraints:</p>
 *
 * <p>The method that is annotated with this annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>The method name must start with "pop"</li>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not return a value</li>
 * <li>Must have one parameter</li>
 * <li>Must not specify type parameters</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface MemoizeContextParameter
{
  /**
   * Return the name of the context parameter.
   * If not specified, then the name will be derived from the name of the method.
   * <ul>
   *   <li>To derive the name from a push method then remove the "push" prefix.</li>
   *   <li>To derive the name from a pop method then remove the "pop" prefix.</li>
   *   <li>To derive the name from a capture method then remove the optional "capture" prefix else just use the method name if no such prefix.</li>
   * </ul>
   *
   * @return the name of the context parameter.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * Return a regular expression for matching the arez names of memoized methods where this context parameter is tracked.
   *
   * @return a regular expression for matching arez name of memoized methods.
   */
  @Nonnull
  @Language( "RegExp" )
  String pattern() default ".*";

  /**
   * Return true if the component does not need to have {@link Memoize} annotated methods match.
   * Otherwise, if no {@link Memoize} annotated methods match the {@link #pattern()} then the annotation
   * processor will generate an error.
   *
   * @return true if the memoized methods must match annotation, false otherwise.
   */
  boolean allowEmpty() default false;
}
