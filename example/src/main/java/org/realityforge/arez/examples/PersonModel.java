package org.realityforge.arez.examples;

import javax.annotation.Nonnull;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Observable;

@SuppressWarnings( "WeakerAccess" )
@ArezComponent
public class PersonModel
{
  @Nonnull
  private String _firstName;
  @Nonnull
  private String _lastName;

  @Nonnull
  public static PersonModel create( @Nonnull final String firstName, @Nonnull final String lastName )
  {
    return new Arez_PersonModel( firstName, lastName );
  }

  PersonModel( @Nonnull final String firstName, @Nonnull final String lastName )
  {
    _firstName = firstName;
    _lastName = lastName;
  }

  @Observable
  @Nonnull
  public String getFirstName()
  {
    return _firstName;
  }

  public void setFirstName( @Nonnull final String firstName )
  {
    _firstName = firstName;
  }

  @Observable
  @Nonnull
  public String getLastName()
  {
    return _lastName;
  }

  public void setLastName( @Nonnull final String lastName )
  {
    _lastName = lastName;
  }

  @Computed
  @Nonnull
  public String getFullName()
  {
    return getFirstName() + " " + getLastName();
  }
}
