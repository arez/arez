package arez;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The interface used to look up components by type and id.
 * This is primarily used by components that represent entities that relate to other entities.
 */
@FunctionalInterface
public interface Locator
{
  /**
   * Lookup the entity with the specified type and the specified id, returning null if not present.
   *
   * @param <T>  the entity type.
   * @param type the type of the entity.
   * @param id   the id of the entity.
   * @return the entity or null if no such entity.
   */
  @Nullable
  <T> T findById( @Nonnull Class<T> type, @Nonnull Object id );
}
