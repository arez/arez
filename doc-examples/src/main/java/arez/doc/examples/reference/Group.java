package arez.doc.examples.reference;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Observable;
import java.util.Objects;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class Group
{
  private final int _id;
  @Nonnull
  private String _name;

  Group( final int id, @Nonnull final String name )
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
}
