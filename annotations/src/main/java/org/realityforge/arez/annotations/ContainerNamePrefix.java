package org.realityforge.arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotate the method that will be overridden to return the value of Container.name().
 * This is only useful when a class potentially has multiple {@link Container} annotated subclasses.
 * This method is used when manually setting up debug context and you want it to align with the concrete
 * implementations. However often {@link ContainerName} is a better solution. If not specified Arez will
 * generate a private method if neede.
 *
 * <p>This annotation should appear at most once on a container. The annotation should be on a method that
 * can be overridden, accepts no parameters and returns a String.</p>
 *
 * <p>The method that is annotated with @ContainerNamePrefix must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with {@link Autorun}, {@link Observable}, {@link Computed}, {@link Action}, {@link javax.annotation.PostConstruct}, {@link PreDispose}, {@link PostDispose}, {@link OnActivate}, {@link OnDeactivate} or {@link OnStale}</li>
 * <li>Must have 0 parameters</li>
 * <li>Must return a String</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface ContainerNamePrefix
{
}
