package arez.component;

import arez.Arez;
import arez.annotations.Action;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Abstract base class for repositories that contain Arez components.
 * This class is used by the annotation processor as a base class from which to derive the actual
 * repositories for each type.
 *
 * <p>When multiple results are returned as a list, they are passed through {@link CollectionsUtil#asList(Stream)} or
 * {@link CollectionsUtil#wrap(List)} and this will convert the result set to an unmodifiable variant if
 * {@link Arez#areCollectionsPropertiesUnmodifiable()} returns true. Typically this means that in
 * development mode these will be made immutable but that the lists will be passed through as-is
 * in production mode for maximum performance.</p>
 */
public abstract class AbstractRepository<K, T, R extends AbstractRepository<K, T, R>>
  extends AbstractContainer<K, T>
{
  /**
   * {@inheritDoc}
   */
  public boolean contains( @Nonnull final T entity )
  {
    return super.contains( entity );
  }

  /**
   * {@inheritDoc}
   */
  @Action
  public void destroy( @Nonnull final T entity )
  {
    super.destroy( entity );
  }

  /**
   * Return all the entities.
   *
   * @return all the entities.
   */
  @Nonnull
  public final List<T> findAll()
  {
    return CollectionsUtil.asList( entities() );
  }

  /**
   * Return all entities sorted by supplied comparator.
   *
   * @param sorter the comparator used to sort entities.
   * @return the entity list result.
   */
  @Nonnull
  public final List<T> findAll( @Nonnull final Comparator<T> sorter )
  {
    return CollectionsUtil.asList( entities().sorted( sorter ) );
  }

  /**
   * Return all entities that match query.
   *
   * @param query the predicate used to select entities.
   * @return the entity list result.
   */
  @Nonnull
  public final List<T> findAllByQuery( @Nonnull final Predicate<T> query )
  {
    return CollectionsUtil.asList( entities().filter( query ) );
  }

  /**
   * Return all entities that match query sorted by supplied comparator.
   *
   * @param query  the predicate used to select entities.
   * @param sorter the comparator used to sort entities.
   * @return the entity list result.
   */
  @Nonnull
  public final List<T> findAllByQuery( @Nonnull final Predicate<T> query, @Nonnull final Comparator<T> sorter )
  {
    return CollectionsUtil.asList( entities().filter( query ).sorted( sorter ) );
  }

  /**
   * Return the entity that matches query or null if unable to locate matching entity.
   *
   * @param query the predicate used to select entity.
   * @return the entity or null if unable to locate matching entity.
   */
  @Nullable
  public final T findByQuery( @Nonnull final Predicate<T> query )
  {
    return entities().filter( query ).findFirst().orElse( null );
  }

  /**
   * Return the entity that matches query else throw an exception.
   *
   * @param query the predicate used to select entity.
   * @return the entity.
   * @throws NoResultException if unable to locate matching entity.
   */
  @Nonnull
  public final T getByQuery( @Nonnull final Predicate<T> query )
    throws NoResultException
  {
    final T entity = findByQuery( query );
    if ( null == entity )
    {
      throw new NoResultException();
    }
    return entity;
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  public final T findByArezId( @Nonnull final K arezId )
  {
    return super.findByArezId( arezId );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  public final T getByArezId( @Nonnull final K arezId )
    throws NoSuchEntityException
  {
    return super.getByArezId( arezId );
  }

  /**
   * Return the repository instance cast to typed subtype.
   *
   * @return the repository instance.
   */
  @SuppressWarnings( "unchecked" )
  @Nonnull
  public final R self()
  {
    return (R) this;
  }
}
