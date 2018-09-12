package arez.doc.examples.repository2;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Observable;
import arez.annotations.Repository;
import java.util.Objects;
import javax.annotation.Nonnull;

@ArezComponent
@Repository( extensions = MyComponentRepositoryExtension.class )
public abstract class MyComponent
{
  private final int _id;
  @Nonnull
  private String _name;
  private boolean _active;

  public MyComponent( final int id, @Nonnull final String name )
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

  public boolean isActive()
  {
    return _active;
  }

  public void setActive( final boolean active )
  {
    _active = active;
  }
}
