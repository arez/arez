package arez.doc.examples.reference2;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Inverse;
import arez.annotations.Observable;
import arez.annotations.Repository;
import java.util.Collection;
import java.util.Objects;
import javax.annotation.Nonnull;

@ArezComponent
@Repository
public abstract class Group
{
  //DOC ELIDE START
  private final int _id;
  @Nonnull
  private String _name;

  public Group( final int id, @Nonnull final String name )
  {
    _id = id;
    _name = Objects.requireNonNull( name );
  }

  @ComponentId
  public final int getId()
  {
    return _id;
  }

  @Observable
  @Nonnull
  public String getName()
  {
    return _name;
  }

  public void setName( @Nonnull final String name )
  {
    _name = name;
  }

  //DOC ELIDE END
  @Inverse
  public abstract Collection<Permission> getPermissions();
}
