package arez.doc.examples.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Observable;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class PersonModel
{
  private String _name;

  @Nonnull
  public static PersonModel create( @Nonnull final String name )
  {
    return new Arez_PersonModel( name );
  }

  PersonModel( @Nonnull final String name )
  {
    _name = name;
  }

  @Observable
  public String getName()
  {
    return _name;
  }

  public void setName( @Nonnull final String name )
  {
    _name = name;
  }

  @Computed
  public boolean doesSearchMatch( @Nonnull final String value )
  {
    return getName().contains( value );
  }
}
