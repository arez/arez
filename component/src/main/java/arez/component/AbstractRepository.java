package arez.component;

import arez.Arez;
import arez.Disposable;
import arez.Observable;
import arez.annotations.Action;
import arez.annotations.ObservableRef;
import arez.annotations.PreDispose;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.anodoc.Unsupported;
import org.realityforge.braincheck.Guards;

/**
 * Abstract base class for repositories that contain Arez components.
 * This class is used by the annotation processor as a base class from which to derive the actual
 * repositories for each type.
 *
 * <p>When multiple results are returned as a list, they are passed through {@link #asList(Stream)} or
 * {@link #wrap(List)} and this will convert the result set to an unmodifiable list if
 * {@link Arez#areRepositoryResultsModifiable()} returns true. Typically this means that in
 * development mode these will be made immutable but that the lists will be passed through as-is
 * in production mode for maximum performance.</p>
 */
public abstract class AbstractRepository<K, T, R extends AbstractRepository<K, T, R>>
{
  /**
   * A map of all the entities ArezId to entity.
   */
  private final HashMap<K, T> _entities = new HashMap<>();

  /**
   * Register specified entity in list of entities managed by the repository.
   * The expectation that this is invoked after the entity has been created but before it is returned
   * to the repository user.
   *
   * @param entity the entity to register.
   */
  protected final void registerEntity( @Nonnull final T entity )
  {
    getEntitiesObservable().preReportChanged();
    _entities.put( Identifiable.getArezId( entity ), entity );
    getEntitiesObservable().reportChanged();
  }

  /**
   * Dispose all the entities associated with the repository.
   */
  @PreDispose
  protected void preDispose()
  {
    getEntitiesObservable().preReportChanged();
    _entities.values().forEach( Disposable::dispose );
    _entities.clear();
    getEntitiesObservable().reportChanged();
  }

  /**
   * Return true if the specified entity is contained in the repository.
   *
   * @param entity the entity.
   * @return true if the specified entity is contained in the repository, false otherwise.
   */
  public boolean contains( @Nonnull final T entity )
  {
    getEntitiesObservable().reportObserved();
    return !Disposable.isDisposed( entity ) && _entities.containsKey( Identifiable.<K>getArezId( entity ) );
  }

  /**
   * Destroy the supplied entity.
   * The entity must have been created in this repository and must not have already been destroyed.
   * The entity will be removed from the map and will be disposed.
   *
   * @param entity the entity to destroy.
   */
  @Action
  public void destroy( @Nonnull final T entity )
  {
    if ( null != _entities.remove( Identifiable.<K>getArezId( entity ) ) )
    {
      getEntitiesObservable().preReportChanged();
      preDisposeEntity( entity );
      Disposable.dispose( entity );
      getEntitiesObservable().reportChanged();
    }
    else
    {
      Guards.fail( () -> "Called destroy() passing an entity that was not in the repository. Entity: " + entity );
    }
  }

  /**
   * A hook method invoked prior to disposing specified entity.
   * This method may disappear in the future, as it is currently used to support an optimization hack
   * that may not have any performance benefit.
   *
   * @param entity the entity that is about to be disposed.
   */
  @Unsupported
  protected abstract void preDisposeEntity( @Nonnull T entity );

  /**
   * Return all the entities.
   *
   * @return all the entities.
   */
  @Nonnull
  public final List<T> findAll()
  {
    return asList( entities() );
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
    return asList( entities().sorted( sorter ) );
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
    return asList( entities().filter( query ) );
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
    return asList( entities().filter( query ).sorted( sorter ) );
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
   * Return the entity with the specified ArezId or null if no such entity.
   *
   * @param arezId the id of entity.
   * @return the entity or null if no such entity.
   */
  @Nullable
  public T findByArezId( @Nonnull final K arezId )
  {
    getEntitiesObservable().reportObserved();
    final T entity = _entities.get( arezId );
    return null != entity && !Disposable.isDisposed( entity ) ? entity : null;
  }

  /**
   * Return the entity with the specified ArezId else throw an exception.
   *
   * @param arezId the id of entity.
   * @return the entity.
   * @throws NoSuchEntityException if unable to locate entity with specified ArezId.
   */
  @Nonnull
  public final T getByArezId( @Nonnull final K arezId )
    throws NoSuchEntityException
  {
    final T entity = findByArezId( arezId );
    if ( null == entity )
    {
      throw new NoSuchEntityException( arezId );
    }
    return entity;
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

  /**
   * Return the observable associated with entities.
   * This template method is implemented by the Arez annotation processor and is used internally
   * to repository. It should not be invoked by extensions.
   *
   * @return the Arez observable associated with entities observable property.
   */
  @ObservableRef
  protected abstract Observable getEntitiesObservable();

  /**
   * Return the raw collection of entities in the repository.
   * This collection should not be exposed to the repository user but may be used be repository extensions when
   * they define custom queries. NOTE: use of this method marks the list as observed.
   *
   * @return the underlying entities.
   */
  @arez.annotations.Observable( expectSetter = false )
  @Nonnull
  protected Stream<T> entities()
  {
    return _entities.values().stream().filter( e -> !Disposable.isDisposed( e ) );
  }

  /**
   * If config option enabled, wrap the specified list in an immutable list and return it.
   * This method should be called by repository extensions when returning list results when not using {@link #asList(Stream)}.
   *
   * @param list the input list.
   * @return the output list
   */
  @Nonnull
  protected final List<T> wrap( @Nonnull final List<T> list )
  {
    return Arez.areRepositoryResultsModifiable() ? list : Collections.unmodifiableList( list );
  }

  /**
   * Convert specified stream to a list, wrapping as an immutable list if required.
   * This method should be called by repository extensions when returning list results.
   *
   * @param stream the input stream.
   * @return the output list
   */
  @Nonnull
  protected final List<T> asList( @Nonnull final Stream<T> stream )
  {
    return wrap( stream.collect( Collectors.toList() ) );
  }
}
