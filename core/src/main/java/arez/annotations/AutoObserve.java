package arez.annotations;

import arez.component.ComponentObservable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Methods and fields annotated by this annotation should be observed for the alive lifetime of the component.
 *
 * <p>This annotation is typically used when a field references an {@link ArezComponent} annotated class that has
 * {@link ArezComponent#disposeOnDeactivate()} set to <code>true</code>. This results in the current component
 * observing the referenced component and thus preventing it from being disposed for the lifetime of the
 * current component.</p>
 *
 * <p>It should be noted that it is preferable for the field that defines the observed component to be marked
 * with this annotation rather than the method accessor. The reason is that the annotation processor will issue
 * a warning if a field that the processor identifies as a potential dependency if it is not annotated with
 * {@link AutoObserve}, {@link CascadeDispose} or {@link ComponentDependency}.</p>
 *
 * <p>If the element annotated is a method then the method must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation other than {@link Reference} or {@link Observable}</li>
 * <li>Must have 0 parameters</li>
 * <li>Must return a value compatible with {@link ComponentObservable}, unless
 *     {@link #validateTypeAtRuntime()} is <code>true</code></li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract unless the method is annotated with {@link Reference} or {@link Observable} in which
 *     case it MUST be abstract</li>
 * <li>Must not throw exceptions</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * </ul>
 *
 * <p>If the element annotated is a field then the field must comply with the additional constraints:</p>
 * <ul>
 * <li>Must be final</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must be compatible with {@link ComponentObservable}, unless {@link #validateTypeAtRuntime()} is
 *     <code>true</code></li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * <li>Should not be public. A warning will be generated but can be suppressed by the {@link SuppressWarnings} or {@link SuppressArezWarnings} annotations with a key "Arez:PublicField".</li>
 * <li>
 *   Should not be protected if in the class annotated with the {@link ArezComponent} annotation as the field is not
 *   expected to be accessed outside the component. A warning will be generated but can be suppressed by the
 *   {@link SuppressWarnings} or {@link SuppressArezWarnings} annotations with a key "Arez:ProtectedField".
 * </li>
 * </ul>
 *
 * <p>If {@link #validateTypeAtRuntime()} is set to <code>true</code> then the declared type of the field or method
 * must be annotated with {@link ActAsComponent}. In this mode, type compatibility is validated at runtime
 * rather than compile time.</p>
 */
@Documented
@Target( { ElementType.METHOD, ElementType.FIELD } )
public @interface AutoObserve
{
  /**
   * Return true and the value of the annotated field or method will be validated at runtime rather than compile time.
   * This is useful when the declared type is annotated with {@link ActAsComponent} but the runtime value is known
   * to implement {@link ComponentObservable}.
   *
   * @return true to defer validation of types until runtime.
   */
  boolean validateTypeAtRuntime() default false;
}
