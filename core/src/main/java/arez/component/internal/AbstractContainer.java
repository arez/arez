package arez.component.internal;

import arez.Arez;
import arez.Disposable;
import arez.ObservableValue;
import arez.annotations.ObservableValueRef;
import arez.annotations.PreDispose;
import arez.component.CollectionsUtil;
import arez.component.ComponentObservable;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import arez.component.NoSuchEntityException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;
import static org.realityforge.braincheck.Guards.*;

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
  private final HashMap<K, T> _entities = new HashMap<>();

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
   * Return true if disposing the container should result in disposing contained entities or just detaching the entities.
   *
   * @return true to dispose entities on container dispose, false to detach entities on container disposed.
   */
  protected abstract boolean shouldDisposeEntryOnDispose();

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
   * Return true if the specified entity is contained in the container.
   *
   * @param entity the entity.
   * @return true if the specified entity is contained in the container, false otherwise.
   */
  protected boolean contains( @Nonnull final T entity )
  {
    getEntitiesObservableValue().reportObserved();
    return _entities.containsKey( Identifiable.<K>getArezId( entity ) );
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
      Guards.fail( () -> "Arez-0157: Called detach() passing an entity that was not attached to the container. Entity: " +
                         entity );
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
    final T entity = _entities.get( arezId );
    if ( null != entity )
    {
      ComponentObservable.observe( entity );
      return entity;
    }
    getEntitiesObservableValue().reportObserved();
    return null;
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
   * Return the observable associated with entities.
   * This template method is implemented by the Arez annotation processor and is used internally
   * to container. It should not be invoked by extensions.
   *
   * @return the Arez observable associated with entities observable property.
   */
  @ObservableValueRef
  @Nonnull
  protected abstract ObservableValue getEntitiesObservableValue();

  /**
   * Return a stream of all entities in the container.
   *
   * @return the underlying entities.
   */
  @arez.annotations.Observable( expectSetter = false )
  @Nonnull
  public Stream<T> entities()
  {
    return _entities.values().stream();
  }

  private void attachEntity( @Nonnull final T entity )
  {
    DisposeTrackable
      .asDisposeTrackable( entity )
      .addOnDisposeListener( this, () -> {
        getEntitiesObservableValue().preReportChanged();
        detach( entity, false );
        getEntitiesObservableValue().reportChanged();
      } );
  }

  private void detachEntity( @Nonnull final T entity, final boolean disposeOnDetach )
  {
    detachEntity( entity );
    if ( disposeOnDetach )
    {
      Disposable.dispose( entity );
    }
  }

  private void detachEntity( @Nonnull final T entity )
  {
    DisposeTrackable.asDisposeTrackable( entity ).removeOnDisposeListener( this );
  }

  final HashMap<K, T> entityMap()
  {
    return _entities;
  }
}
