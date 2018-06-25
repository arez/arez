package arez.component;

import arez.annotations.Observable;
import arez.annotations.ObservableRef;
import arez.annotations.PreDispose;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Abstract base class for reference to a component where reference should be cleared when the component is disposed.
 */
public abstract class AbstractEntityReference<T>
{
  @Nullable
  private T _entity;

  /**
   * Dispose or detach the associated entity if any.
   */
  @PreDispose
  protected void preDispose()
  {
    setEntity( null );
  }

  /**
   * Return the entity that this reference points to.
   *
   * @return the associated entity.
   */
  @Nullable
  protected T getEntity()
  {
    return _entity;
  }

  /**
   * Return true if reference for entity exists.
   *
   * @return true if reference for entity exists.
   */
  protected boolean hasEntity()
  {
    getEntityObservable().reportObserved();
    return null != _entity;
  }

  /**
   * Return the observable associated with entity.
   *
   * @return the Arez observable associated with entities observable property.
   */
  @ObservableRef
  @Nonnull
  protected abstract arez.Observable getEntityObservable();

  /**
   * Set the entity that this reference points to.
   *
   * @param entity the associated entity.
   */
  @Observable
  protected void setEntity( @Nullable final T entity )
  {
    if ( null != _entity )
    {
      detachEntity( _entity );
    }
    _entity = entity;
    if ( null != _entity )
    {
      attachEntity( _entity );
    }
  }

  private void attachEntity( @Nonnull final T entity )
  {
    DisposeTrackable
      .asDisposeTrackable( entity )
      .getNotifier()
      .addOnDisposeListener( this, () -> {
        getEntityObservable().preReportChanged();
        detachEntity( entity );
        _entity = null;
        getEntityObservable().reportChanged();
      } );
  }

  private void detachEntity( @Nonnull final T entity )
  {
    DisposeTrackable
      .asDisposeTrackable( entity )
      .getNotifier()
      .removeOnDisposeListener( this );
  }
}
