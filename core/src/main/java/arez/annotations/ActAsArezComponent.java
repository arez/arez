package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation applied to a framework-defined type annotation to indicate that the annotated type should be treated
 * as an Arez-compatible container.
 *
 * <p>Normally a type that contains {@link Action @Action} or most other Arez processor annotations must be
 * annotated with either {@link ArezComponent @ArezComponent} or {@link ArezComponentLike @ArezComponentLike}.
 * If neither annotation is present then the Arez processor reports the Arez annotation usage as invalid.</p>
 *
 * <p>Applying {@code @ActAsArezComponent} to another annotation, such as a framework-specific {@code @View}
 * annotation, tells the Arez processor that types annotated with that framework annotation are valid locations
 * for Arez annotations. This allows another framework to define its own component model and process those Arez
 * annotations itself or generate an Arez-compatible subtype.</p>
 *
 * <p>The preferred integration pattern is for the framework annotation to depend directly on this annotation, but
 * the Arez processor also recognizes any meta-annotation named {@code ActAsArezComponent}. This fallback exists so
 * downstream frameworks can declare a package-access compatibility annotation without taking a direct code
 * dependency on Arez.</p>
 */
@Documented
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.ANNOTATION_TYPE )
public @interface ActAsArezComponent
{
}
