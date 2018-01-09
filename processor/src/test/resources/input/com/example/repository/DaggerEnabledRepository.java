package com.example.repository;

import arez.annotations.ArezComponent;
import arez.annotations.Injectible;
import arez.annotations.Observable;
import arez.annotations.Repository;
import javax.annotation.Nonnull;

@Repository( dagger = Injectible.ENABLE )
@ArezComponent
public class DaggerEnabledRepository
{
  @Nonnull
  private String _name;

  DaggerEnabledRepository( @Nonnull final String name )
  {
    _name = name;
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
