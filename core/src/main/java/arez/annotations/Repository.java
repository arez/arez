package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

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
 * <p>Annotating a class with this annotation will result in a class named "[MyComponent]Repository"</p>
 *
 * <p>The way to add custom queries or {@link Computed} queries is to define an interface that
 * defines an abstract <code>self()</code> that returns the underlying repository. Using this
 * combined with default methods, you can define as many new queries and mutations as is desired.
 * The extension class then needs to be registered by setting the appropriate parameter on this
 * annotation.</p>
 *
 * <p>An example of what an extension may look like for a <code>Issue</code> component. See below:</p>
 *
 * <pre>{@code
 * public interface MyIssueRepositoryExtension
 * {
 *   default Issue findByTitle( final String title )
 *   {
 *     return self().findByQuery( issue -> issue.getTitle().equals( title ) );
 *   }
 *
 *   \@Computed
 *   default List<Issue> findAllCompleted()
 *   {
 *     return self().findAllByQuery( Issue::isCompleted );
 *   }
 *
 *   MyIssueRepository self();
 * }
 * }</pre>
 */
@Documented
@Target( ElementType.TYPE )
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
   * Indicate whether a dagger module should be generated for repository.
   * {@link Feature#ENABLE} will force the generation of the module, {@link Feature#DISABLE}
   * will result in no dagger module and {@link Feature#AUTODETECT} will add a dagger
   * module if the <code>dagger.Module</code> class is present on the classpath.
   *
   * @return enum controlling whether a dagger module should be generated for repository.
   */
  Feature dagger() default Feature.AUTODETECT;

  /**
   * Indicate whether an @Inject annotation should be added to constructor of the generated repository.
   * {@link Feature#ENABLE} will force the addition of an @Inject annotation, {@link Feature#DISABLE}
   * will result in no @Inject annotation and {@link Feature#AUTODETECT} will add an @Inject
   * if any it is present on the classpath. Note that this is effectively {@link Feature#ENABLE} if
   * dagger parameter is true.
   *
   * @return enum controlling present of Inject annotation on constructor of repository.
   */
  Feature inject() default Feature.AUTODETECT;

  /**
   * Indicate the strategy for attaching entities to the repository.
   *
   * @return the strategy for attaching entities to the repository.
   */
  AttachType attach() default AttachType.CREATE_ONLY;

  /**
   * Indicate the strategy for detaching entities from the repository.
   *
   * @return the strategy for detaching entities from the repository.
   */
  DetachType detach() default DetachType.DESTROY_ONLY;

  /**
   * Enum to control how entities can be attached to the repository.
   */
  enum AttachType
  {
    /**
     * Entities are created by create methods on the repository.
     * The create methods accept the same parameters as the constructors on the entities.
     */
    CREATE_ONLY,
    /**
     * Entities are created outside repository but can be manually attached to the repository.
     */
    ATTACH_ONLY,
    /**
     * Entities can be created or manually attached as required.
     */
    CREATE_OR_ATTACH
  }

  /**
   * Enum to control how entities can be detached from the repository.
   */
  enum DetachType
  {
    /**
     * Entities are disposed by detach methods on repository.
     */
    DESTROY_ONLY,
    /**
     * Entities can be detached from the repository manually and the lifecycle of the entity managed externally.
     */
    DETACH_ONLY,
    /**
     * Entities can be destroyed or manually detached as required.
     */
    DESTROY_OR_DETACH,
    /**
     * Entities can only be detached from the repository by directly disposing entity.
     */
    NONE
  }
}
