package org.realityforge.arez.doc.examples.repository;

import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.annotations.Observable;

public class MyComponent
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
