package arez.component;

import arez.Observer;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Element that contains a reference to an entity and a monitor.
 * The monitor is Disposable representing a "when" observer that triggers removal of
 * entity from a container
 */
final class EntityEntry<E>
{
  /**
   * The underlying entity.
   */
  @Nonnull
  private final E _entity;
  /**
   * The monitor observer that will remove entity from the container when the entity is disposed.
   */
  @Nullable
  private Observer _monitor;

  /**
   * Create entry for entity.
   *
   * @param entity the entity placed into the container.
   */
  EntityEntry( @Nonnull final E entity )
  {
    _entity = Objects.requireNonNull( entity );
  }

  /**
   * Return the entity the entry represents.
   *
   * @return the entity the entry represents.
   */
  @Nonnull
  E getEntity()
  {
    return _entity;
  }

  /**
   * Set the monitor that will remove entity from the container when entity is disposed.
   *
   * @param monitor the monitor that will remove entity from the container when entity is disposed.
   */
  void setMonitor( @Nonnull final Observer monitor )
  {
    _monitor = Objects.requireNonNull( monitor );
  }

  /**
   * Return the monitor associated with the entry.
   *
   * @return the monitor associated with the entry.
   */
  @Nullable
  Observer getMonitor()
  {
    return _monitor;
  }
}
