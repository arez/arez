package arez.component;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Observable;
import arez.Observer;
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
import org.realityforge.anodoc.TestOnly;
import org.realityforge.braincheck.Guards;

/**
 * Abstract base class for observable Arez components that contain other components.
 *
 * <p>When multiple results are returned as a list, they are passed through {@link CollectionsUtil#asList(Stream)} or
 * {@link CollectionsUtil#wrap(List)} and this will convert the result set to an unmodifiable variant if
 * {@link Arez#areCollectionsPropertiesUnmodifiable()} returns true. Typically this means that in
 * development mode these will be made immutable but that the lists will be passed through as-is
 * in production mode for maximum performance.</p>
 */
public abstract class AbstractContainer<K, T>
{
  /**
   * A map of all the entities ArezId to entity.
   */
  private final HashMap<K, EntityReference<T>> _entities = new HashMap<>();

  /**
   * Attach specified entity to the set of entities managed by the container.
   * This should not be invoked if the entity is already attached to the repository.
   *
   * @param entity the entity to register.
   */
  protected void attach( @Nonnull final T entity )
  {
    getEntitiesObservable().preReportChanged();
    final EntityReference<T> entry = new EntityReference<>( entity );
    final K arezId = Identifiable.getArezId( entity );
    _entities.put( arezId, entry );
    final Observer monitor =
      getContext().when( Arez.areNativeComponentsEnabled() ? component() : null,
                         Arez.areNamesEnabled() ? getContainerName() + ".EntityWatcher." + arezId : null,
                         true,
                         () -> ComponentObservable.notObserved( entity ),
                         () -> detach( entity ),
                         true,
                         true );
    entry.setMonitor( monitor );
    getEntitiesObservable().reportChanged();
  }

  /**
   * Return true if disposing the container should result in disposing contained entities or just detaching the entities.
   *
   * @return true to dispose entities on container dispose, false to detach entities on container disposed.
   */
  protected abstract boolean shouldDisposeEntryOnDispose();

  /**
   * Dispose all the entities associated with the container.
   */
  @PreDispose
  protected void preDispose()
  {
    _entities.values().forEach( entry -> detachEntry( entry, shouldDisposeEntryOnDispose() ) );
    _entities.clear();
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
    return Disposable.isNotDisposed( entity ) && _entities.containsKey( Identifiable.<K>getArezId( entity ) );
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
    final EntityReference<T> entry = _entities.remove( Identifiable.<K>getArezId( entity ) );
    if ( null != entry )
    {
      getEntitiesObservable().preReportChanged();
      detachEntry( entry, disposeEntity );
      getEntitiesObservable().reportChanged();
    }
    else
    {
      Guards.fail( () -> "Arez-0157: Called detach() passing an entity that was not attached to the container. Entity: " +
                         entity );
    }
  }

  private void detachEntry( @Nonnull final EntityReference<T> entry, final boolean shouldDisposeEntity )
  {
    if ( shouldDisposeEntity )
    {
      Disposable.dispose( entry );
    }
    else
    {
      final Observer monitor = entry.getMonitor();
      if ( null != monitor )
      {
        Disposable.dispose( monitor );
      }
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
    final EntityReference<T> entry = _entities.get( arezId );
    if ( null != entry && Disposable.isNotDisposed( entry ) )
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
   * Return the component associated with the container if native components enabled.
   *
   * @return the component associated with the container if native components enabled.
   */
  @ComponentRef
  @Nonnull
  protected abstract Component component();

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
    return _entities.values().stream().filter( Disposable::isNotDisposed ).map( EntityReference::getEntity );
  }

  @TestOnly
  final HashMap<K, EntityReference<T>> getEntities()
  {
    return _entities;
  }
}
