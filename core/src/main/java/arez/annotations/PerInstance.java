package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation that can be applied to constructor parameters to indicate the must be passed in when
 * creating the instance. This annotation is only useful if using an injection framework to construct
 * the component and the the {@link ArezComponent#inject()} parameter is set to {@link InjectMode#CONSUME}.
 *
 * <p>If a parameter is annotated with this then the annotation processor will generate a static subclass
 * named <code>Factory</code> in the enhanced component class that is responsible for creating the component
 * instance that has a create method that accepts all parameters that are marked using this annotation.</p>
 */
@Documented
@Target( ElementType.PARAMETER )
public @interface PerInstance
{
}
