package arez.entity;

import arez.annotations.ArezComponent;
import arez.component.AbstractEntityReference;
import javax.annotation.Nonnull;

/**
 * A representation of a HasOne inverse relationship.
 */
@ArezComponent
public abstract class HasOneInverseRelationship<T>
  extends AbstractEntityReference<T>
{
  @Nonnull
  public static <T> HasOneInverseRelationship<T> create()
  {
    return new Arez_HasOneInverseRelationship<>();
  }

  public void link( @Nonnull final T entity )
  {
    setEntity( entity );
  }

  public void delink()
  {
    setEntity( null );
  }

  @Nonnull
  public final T getReference()
  {
    final T entity = getEntity();
    assert null != entity;
    return entity;
  }

  public final boolean hasReference()
  {
    return hasEntity();
  }
}
