package arez.component;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Observable;
import arez.Observer;
import arez.annotations.Action;
import arez.annotations.ComponentNameRef;
import arez.annotations.ComponentRef;
import arez.annotations.ContextRef;
import arez.annotations.ObservableRef;
import arez.annotations.PreDispose;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

/**
 * Abstract base class for observable Arez components that contain other components.
 *
 * <p>When multiple results are returned as a list, they are passed through {@link RepositoryUtil#asList(Stream)} or
 * {@link RepositoryUtil#toResults(List)} and this will convert the result set to an unmodifiable list if
 * {@link Arez#areRepositoryResultsModifiable()} returns true. Typically this means that in
 * development mode these will be made immutable but that the lists will be passed through as-is
 * in production mode for maximum performance.</p>
 */
public abstract class AbstractContainer<K, T>
{
  /**
   * A map of all the entities ArezId to entity.
   */
  private final HashMap<K, RepositoryEntry<T>> _entities = new HashMap<>();

  /**
   * Return the component associated with component if native components enabled.
   *
   * @return the component associated with component if native components enabled.
   */
  @ComponentRef
  @Nonnull
  protected abstract Component component();

  /**
   * Register specified entity in list of entities managed by the container.
   * The expectation that this is invoked after the entity has been created but before it is returned
   * to the container user.
   *
   * @param entity the entity to register.
   */
  protected final void registerEntity( @Nonnull final T entity )
  {
    getEntitiesObservable().preReportChanged();
    final RepositoryEntry<T> entry = new RepositoryEntry<>( entity );
    final K arezId = Identifiable.getArezId( entity );
    _entities.put( arezId, entry );
    final Observer monitor =
      getContext().when( Arez.areNativeComponentsEnabled() ? component() : null,
                         Arez.areNamesEnabled() ? getContainerName() + ".Watcher." + arezId : null,
                         true,
                         () -> Disposable.isDisposed( entity ),
                         () -> destroy( entity ),
                         true,
                         true );
    entry.setMonitor( monitor );
    getEntitiesObservable().reportChanged();
  }

  /**
   * Dispose all the entities associated with the container.
   */
  @PreDispose
  protected void preDispose()
  {
    getEntitiesObservable().preReportChanged();
    _entities.values().forEach( e -> Disposable.dispose( e ) );
    _entities.clear();
    getEntitiesObservable().reportChanged();
  }

  /**
   * Return true if the specified entity is contained in the container.
   *
   * @param entity the entity.
   * @return true if the specified entity is contained in the container, false otherwise.
   */
  protected boolean contains( @Nonnull final T entity )
  {
    getEntitiesObservable().reportObserved();
    return !Disposable.isDisposed( entity ) && _entities.containsKey( Identifiable.<K>getArezId( entity ) );
  }

  /**
   * Destroy the supplied entity.
   * The entity must have been created in this container and must not have already been destroyed.
   * The entity will be removed from the map and will be disposed.
   *
   * @param entity the entity to destroy.
   */
  @Action
  protected void destroy( @Nonnull final T entity )
  {
    final RepositoryEntry<T> entry = _entities.remove( Identifiable.<K>getArezId( entity ) );
    if ( null != entry )
    {
      getEntitiesObservable().preReportChanged();
      Disposable.dispose( entry );
      getEntitiesObservable().reportChanged();
    }
    else
    {
      Guards.fail( () -> "Called destroy() passing an entity that was not in the container. Entity: " + entity );
    }
  }

  /**
   * Return the entity with the specified ArezId or null if no such entity.
   *
   * @param arezId the id of entity.
   * @return the entity or null if no such entity.
   */
  @Nullable
  protected T findByArezId( @Nonnull final K arezId )
  {
    final RepositoryEntry<T> entry = _entities.get( arezId );
    if ( null != entry && !Disposable.isDisposed( entry ) )
    {
      final T entity = entry.getEntity();
      ComponentObservable.observe( entity );
      return entity;
    }
    else
    {
      getEntitiesObservable().reportObserved();
      return null;
    }
  }

  /**
   * Return the entity with the specified ArezId else throw an exception.
   *
   * @param arezId the id of entity.
   * @return the entity.
   * @throws NoSuchEntityException if unable to locate entity with specified ArezId.
   */
  @Nonnull
  protected T getByArezId( @Nonnull final K arezId )
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
   * Return the context associated with the container.
   *
   * @return the context associated with the container.
   */
  @ContextRef
  @Nonnull
  protected abstract ArezContext getContext();

  /**
   * Return the name associated with the container.
   *
   * @return the name associated with the container.
   */
  @ComponentNameRef
  @Nonnull
  protected abstract String getContainerName();

  /**
   * Return the observable associated with entities.
   * This template method is implemented by the Arez annotation processor and is used internally
   * to container. It should not be invoked by extensions.
   *
   * @return the Arez observable associated with entities observable property.
   */
  @ObservableRef
  @Nonnull
  protected abstract Observable getEntitiesObservable();

  /**
   * Return a stream of all entities in the container.
   *
   * @return the underlying entities.
   */
  @arez.annotations.Observable( expectSetter = false )
  @Nonnull
  public Stream<T> entities()
  {
    return _entities.values().stream().filter( this::notDisposed ).map( RepositoryEntry::getEntity );
  }

  /**
   * Return true if entry is not disposed.
   *
   * @return true if entry is not disposed.
   */
  protected final boolean notDisposed( @Nonnull final RepositoryEntry<T> entry )
  {
    return !Disposable.isDisposed( entry );
  }
}
