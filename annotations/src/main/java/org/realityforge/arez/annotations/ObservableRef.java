package org.realityforge.arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;
import org.realityforge.anodoc.Unsupported;

/**
 * Marks a template method that returns the {@link org.realityforge.arez.Observable} instance for
 * the {@link Observable} annotated property. Each property marked with the {@link Observable} annotation is backed
 * by an {@link org.realityforge.arez.Observable} instance and some frameworks make use of this value to implement
 * advanced functionality.
 *
 * <p>The method that is annotated with @ObservableRef must also comply with the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw any exceptions</li>
 * <li>Must return an instance of {@link org.realityforge.arez.Observable}.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
@Unsupported( "This is largely experimental and intended for framework users rather than casual users of the framework" )
public @interface ObservableRef
{
  /**
   * Return the name of the associated Observable property that this ref relates to.
   * This value will be derived if the method name matches the pattern "get[Name]Observable",
   * otherwise it must be specified.
   *
   * @return the name of the associated Observable.
   */
  @Nonnull
  String name() default "<default>";
}
