package arez.component.internal;

import arez.Arez;
import arez.Disposable;
import arez.ObservableValue;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import arez.annotations.PreDispose;
import arez.component.ComponentObservable;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.NoResultException;
import arez.component.NoSuchEntityException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

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
{
  /**
   * A map of all the entities ArezId to entity.
   */
  @Nonnull
  private final Map<K, T> _entities = new HashMap<>();

  protected final boolean shouldDisposeEntryOnDispose()
  {
    return true;
  }

  public boolean contains( @Nonnull final T entity )
  {
    if ( reportRead() )
    {
      getEntitiesObservableValue().reportObserved();
    }
    return _entities.containsKey( Identifiable.<K>getArezId( entity ) );
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

  @Nullable
  public final T findByArezId( @Nonnull final K arezId )
  {
    final T entity = _entities.get( arezId );
    if ( null != entity )
    {
      if ( reportRead() )
      {
        ComponentObservable.observe( entity );
      }
      return entity;
    }
    if ( reportRead() )
    {
      getEntitiesObservableValue().reportObserved();
    }
    return null;
  }

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
   * Attach specified entity to the set of entities managed by the container.
   * This should not be invoked if the entity is already attached to the repository.
   *
   * @param entity the entity to register.
   */
  @SuppressWarnings( "SuspiciousMethodCalls" )
  protected void attach( @Nonnull final T entity )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> Disposable.isNotDisposed( entity ),
                    () -> "Arez-0168: Called attach() passing an entity that is disposed. Entity: " + entity );
      apiInvariant( () -> !_entities.containsKey( Identifiable.getArezId( entity ) ),
                    () -> "Arez-0136: Called attach() passing an entity that is already attached " +
                          "to the container. Entity: " + entity );
    }
    getEntitiesObservableValue().preReportChanged();
    attachEntity( entity );
    _entities.put( Identifiable.getArezId( entity ), entity );
    getEntitiesObservableValue().reportChanged();
  }

  /**
   * Dispose or detach all the entities associated with the container.
   */
  @PreDispose
  protected void preDispose()
  {
    _entities.values().forEach( entry -> detachEntity( entry, shouldDisposeEntryOnDispose() ) );
    _entities.clear();
  }

  /**
   * Detach the entity from the container and dispose the entity.
   * The entity must be attached to the container.
   *
   * @param entity the entity to destroy.
   */
  protected void destroy( @Nonnull final T entity )
  {
    detach( entity, true );
  }

  /**
   * Detach entity from container without disposing entity.
   * The entity must be attached to the container.
   *
   * @param entity the entity to detach.
   */
  protected void detach( @Nonnull final T entity )
  {
    detach( entity, false );
  }

  /**
   * Detach entity from container without disposing entity.
   * The entity must be attached to the container.
   *
   * @param entity the entity to detach.
   */
  private void detach( @Nonnull final T entity, final boolean disposeEntity )
  {
    // This method has been extracted to try and avoid GWT inlining into invoker
    final T removed = _entities.remove( Identifiable.<K>getArezId( entity ) );
    if ( null != removed )
    {
      getEntitiesObservableValue().preReportChanged();
      detachEntity( entity, disposeEntity );
      getEntitiesObservableValue().reportChanged();
    }
    else
    {
      fail( () -> "Arez-0157: Called detach() passing an entity that was not attached to the container. Entity: " +
                  entity );
    }
  }

  protected boolean reportRead()
  {
    return true;
  }

  /**
   * Return the observable associated with entities.
   * This template method is implemented by the Arez annotation processor and is used internally
   * to container. It should not be invoked by extensions.
   *
   * @return the Arez observable associated with entities observable property.
   */
  @ObservableValueRef
  @Nonnull
  protected abstract ObservableValue<?> getEntitiesObservableValue();

  /**
   * Return a stream of all entities in the container.
   *
   * @return the underlying entities.
   */
  @Nonnull
  public Stream<T> entities()
  {
    if ( reportRead() )
    {
      getEntitiesObservableValue().reportObserved();
    }
    return entityStream();
  }

  /**
   * Return a stream of all entities in the container.
   *
   * @return the underlying entities.
   */
  @Observable( name = "entities", expectSetter = false )
  @Nonnull
  protected Stream<T> entitiesValue()
  {
    return entityStream();
  }

  @Nonnull
  private Stream<T> entityStream()
  {
    return _entities.values().stream();
  }

  private void attachEntity( @Nonnull final T entity )
  {
    DisposeNotifier
      .asDisposeNotifier( entity )
      .addOnDisposeListener( this, () -> {
        getEntitiesObservableValue().preReportChanged();
        detach( entity, false );
        getEntitiesObservableValue().reportChanged();
      } );
  }

  private void detachEntity( @Nonnull final T entity, final boolean disposeOnDetach )
  {
    DisposeNotifier.asDisposeNotifier( entity ).removeOnDisposeListener( this );
    if ( disposeOnDetach )
    {
      Disposable.dispose( entity );
    }
  }
}
