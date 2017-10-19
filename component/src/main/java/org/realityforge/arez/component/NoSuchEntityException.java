package org.realityforge.arez.component;

import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Exception thrown by repository when the query for a specific entity failed.
 */
@SuppressWarnings( "GwtInconsistentSerializableClass" )
public class NoSuchEntityException
  extends NoResultException
{
  @Nonnull
  private final Class<?> _type;
  @Nonnull
  private final Object _id;

  /**
   * Create the exception
   *
   * @param type the type of the entity that was queried.
   * @param id   the id of the entity that was queried.
   */
  public NoSuchEntityException( @Nonnull final Class<?> type, @Nonnull final Object id )
  {
    _type = Objects.requireNonNull( type );
    _id = Objects.requireNonNull( id );
  }

  /**
   * Return the type of the entity that was not found.
   *
   * @return the type of the entity that was not found.
   */
  @Nonnull
  public Class<?> getType()
  {
    return _type;
  }

  /**
   * Return the id of the entity that was not found.
   *
   * @return the id of the entity that was not found.
   */
  @Nonnull
  public Object getId()
  {
    return _id;
  }

  @Override
  public String toString()
  {
    return "NoSuchEntityException[type=" + _type + ", id=" + _id + ']';
  }
}
