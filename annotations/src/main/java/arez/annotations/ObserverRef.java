package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;
import org.realityforge.anodoc.Unsupported;

/**
 * Marks a template method that returns the {@link arez.Observer} instance for
 * the associated {@link Track} or {@link Autorun} annotated method.
 *
 * <p>The method that is annotated with @ObserverRef must also comply with the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must be abstract</li>
 * <li>Must not throw any exceptions</li>
 * <li>Must return an instance of {@link arez.Observer}.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
@Unsupported( "This is largely experimental and intended for framework users rather than casual users of the framework" )
public @interface ObserverRef
{
  /**
   * Return the name of the associated Observer that this ref relates to.
   * This value will be derived if the method name matches the pattern "get[Name]Observer",
   * otherwise it must be specified.
   *
   * @return the name of the associated Observer.
   */
  @Nonnull
  String name() default "<default>";
}
