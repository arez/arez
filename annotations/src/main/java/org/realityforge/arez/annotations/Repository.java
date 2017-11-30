package org.realityforge.arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;
import org.realityforge.anodoc.Unsupported;

/**
 * Annotation that indicates that a repository must be generated for component.
 * A repository is the default mechanism for managing instances of classes
 * annotated with {@link ArezComponent}. The repository provides mechanisms to
 * create a component, lookup a component by id or using a query and dispose
 * the instance. These activities can be done by the user through other means
 * but the repositories provide an easy solution.
 *
 * <p>This annotation can only be added to classes that have been annotated with the
 * {@link ArezComponent} annotation.</p>
 *
 * <p>Annotating a class with this annotation will result in the following artifacts.</p>
 *
 * <ul>
 * <li>A repository named "[MyComponent]Repository"</li>
 * <li>An interface used to define extensions of the repository "[MyComponent]BaseRepositoryExtension"</li>
 * </ul>
 *
 * <p>The way to add custom queries or \@Computed queries is to define an interface
 * that extends the base extension interface. The extension interface defines a <code>self()</code>
 * method that you can use to get at the underlying repository. Using this combined with
 * default methods you can define as many new queries and mutations as is desired. The
 * extension class then needs to be registered by setting the appropriate parameter on this
 * annotation.</p>
 *
 * <p>An example of what an extension may look like for a <code>Todo</code> component. See below:</p>
 *
 * <pre>{@code
 * public interface MyTodoRepositoryExtension
 *   extends TodoBaseRepositoryExtension
 * {
 *   default Todo findByTitle( final String title )
 *   {
 *     return self().findByQuery( todo -> todo.getTitle().equals( title ) );
 *   }
 *
 *   \@Computed
 *   default List<Todo> findAllCompleted()
 *   {
 *     return self().findAllByQuery( Todo::isCompleted );
 *   }
 * }
 * }</pre>
 */
@Documented
@Target( ElementType.TYPE )
@Unsupported( "This may change in the future as it is incorporated with replicant and the wider ecosystem" )
public @interface Repository
{
  /**
   * Return the name of the repository.
   * The value must conform to the requirements of a java identifier.
   * The default value if not specified is the name of the associated ArezComponent suffixed with "Repository".
   *
   * @return the name of the repository.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * Return the list of extension interfaces that the repository will implement.
   * The extension interfaces should not define any non-default methods besides
   * self().
   *
   * @return the list of extension interfaces that the repository will implement.
   */
  Class[] extensions() default {};

  /**
   * Return true if a dagger module should be generated for repository.
   *
   * @return true if a dagger module should be generated for repository.
   */
  boolean dagger() default false;

  /**
   * Return true if an inject annotation should be added to the constructor of generated repository.
   * Note that this is effectively true if dagger is true.
   *
   * @return true if an inject annotation should be added to the constructor of generated repository.
   */
  boolean inject() default false;
}
