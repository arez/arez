package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Methods and fields annotated by this annotation should be disposed when the component is disposed.
 * The dispose occurs after the {@link PreDispose} method.
 *
 * <p>If the element annotated is a method then the method must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must be final</li>
 * <li>Must have 0 parameters</li>
 * <li>The type of the field must implement {@link arez.Disposable} or must be annotated by {@link ArezComponent}</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * <li>The field must be accessible to the component subclass which means it must not be package access unless it is in the same package as the arez component.</li>
 * </ul>
 *
 * <p>If the element annotated is a field then the field must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>The type of the field must implement {@link arez.Disposable} or must be annotated by {@link ArezComponent}</li>
 * <li>The field must be accessible to the component subclass which means it must be protected or package access if it is in the same package as the arez component.</li>
 * </ul>
 */
@Documented
@Target( { ElementType.METHOD, ElementType.FIELD } )
public @interface CascadeDispose
{
}
