package arez.doc.examples.reference2;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Observable;
import java.util.Objects;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class User
{
  private final int _id;
  @Nonnull
  private String _name;
  private boolean _active;

  User( final int id, @Nonnull final String name )
  {
    _id = id;
    _name = Objects.requireNonNull( name );
  }

  @ComponentId
  public int getId()
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
