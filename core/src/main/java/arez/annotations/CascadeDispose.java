package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation added to a field to indicate that the field should be disposed when the component is disposed.
 * The field will be disposed after the {@link PreDispose} method.
 *
 * <p>The field that is annotated with the annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>The type of the field must implement {@link arez.Disposable} or must be annotated by {@link ArezComponent}</li>
 * <li>The field must be accessible to the component subclass which means it must be protected of package access if it is in the same package as the arez component.</li>
 * </ul>
 */
@Documented
@Target( { ElementType.FIELD } )
public @interface CascadeDispose
{
}
