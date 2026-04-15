package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Methods and fields annotated by this annotation should be disposed when the component is disposed.
 * The dispose occurs after the {@link PreDispose} method.
 *
 * <p>It should be noted that it is preferable for the field that defines the contained component is marked
 * with this annotation rather than the method accessor. The reason is that the annotation processor
 * will issue a warning if a field that the processor identifies as a potential contained component if it is
 * not annotated with {@link AutoObserve}, {@link CascadeDispose} or {@link ComponentDependency}.</p>
 *
 * <p>If the element annotated is a method then the method must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation other than {@link Reference} or {@link Observable}</li>
 * <li>Must have 0 parameters</li>
 * <li>The type of the field must implement {@link arez.Disposable} or must be annotated by {@link ArezComponent}</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract unless the method is annotated with {@link Reference} or {@link Observable} in which case it MUST be abstract</li>
 * <li>Must not throw exceptions</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * </ul>
 *
 * <p>If the element annotated is a field then the field must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>The type of the field must implement {@link arez.Disposable} or must be annotated by {@link ArezComponent}</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * <li>Should not be public. A warning will be generated but can be suppressed by the {@link SuppressWarnings} or {@link SuppressArezWarnings} annotations with a key "Arez:PublicField".</li>
 * <li>
 *   Should not be protected if in the class annotated with the {@link ArezComponent} annotation as the field is not
 *   expected to be accessed outside the component. A warning will be generated but can be suppressed by the
 *   {@link SuppressWarnings} or {@link SuppressArezWarnings} annotations with a key "Arez:ProtectedField".
 * </li>
 * </ul>
 *
 * <p>This annotation is only supported on elements contained within a type annotated by
 * {@link ArezComponent} or {@link ArezComponentLike}. Other usages will fail compilation.</p>
 */
@Documented
@Target( { ElementType.METHOD, ElementType.FIELD } )
public @interface CascadeDispose
{
}
