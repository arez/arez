package arez.entity;

import arez.annotations.ArezComponent;
import arez.component.AbstractEntityReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ArezComponent( observable = false )
public abstract class HasZeroOrOneInverseRelationship<T>
  extends AbstractEntityReference<T>
{
  @Nonnull
  public static <T> HasZeroOrOneInverseRelationship<T> create()
  {
    return new Arez_HasZeroOrOneInverseRelationship<>();
  }

  public void link( @Nonnull final T entity )
  {
    setEntity( entity );
  }

  public void delink()
  {
    setEntity( null );
  }

  @Nullable
  public final T getReference()
  {
    return getEntity();
  }

  public final boolean hasReference()
  {
    return hasEntity();
  }
}
