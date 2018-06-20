package arez.entity;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.component.AbstractContainer;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

/**
 * Model for a many multiplicity inverse relationship between two entities.
 *
 * @param <T> the type of the entity.
 */
@ArezComponent( observable = false )
public abstract class HasManyInverseRelationship<T>
  extends AbstractContainer<Object, T>
{
  @Nonnull
  public static <T> HasManyInverseRelationship<T> create()
  {
    return new Arez_HasManyInverseRelationship<>();
  }

  @Override
  protected final boolean shouldDisposeEntryOnDispose()
  {
    return false;
  }

  @Computed( name = "relationship" )
  @Nonnull
  public List<T> getEntities()
  {
    return entities().collect( Collectors.toList() );
  }

  public void link( @Nonnull final T entity )
  {
    attach( entity );
  }

  public void delink( @Nonnull final T entity )
  {
    detach( entity );
  }
}
