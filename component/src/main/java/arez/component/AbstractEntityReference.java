package arez.component;

import arez.Disposable;
import arez.annotations.Observable;
import arez.annotations.PreDispose;
import javax.annotation.Nullable;

/**
 * Abstract base class for reference to a component where reference should be cleared when the component is disposed.
 */
public abstract class AbstractEntityReference<T>
  extends AbstractEntryContainer<T>
{
  @Nullable
  private EntityEntry<T> _entry;

  /**
   * Dispose or detach the associated entity if any.
   */
  @PreDispose
  protected void preDispose()
  {
    if ( null != _entry )
    {
      detachEntry( _entry, false );
      _entry = null;
    }
  }

  /**
   * Return the entity that this reference points to.
   *
   * @return the associated entity.
   */
  @Nullable
  protected T getEntity()
  {
    return null != _entry && Disposable.isNotDisposed( _entry ) ? _entry.getEntity() : null;
  }

  /**
   * Set the entity that this reference points to.
   *
   * @param entity the associated entity.
   */
  @Observable
  protected void setEntity( @Nullable final T entity )
  {
    if ( null != _entry )
    {
      detachEntry( _entry, false );
    }
    if ( null == entity )
    {
      _entry = null;
    }
    else
    {
      _entry = createEntityEntry( entity, reference -> detachEntry( reference, false ) );
    }
  }
}
