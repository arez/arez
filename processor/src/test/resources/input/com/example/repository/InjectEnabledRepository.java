package com.example.repository;

import arez.annotations.ArezComponent;
import arez.annotations.Injectible;
import arez.annotations.Observable;
import arez.annotations.Repository;
import javax.annotation.Nonnull;

@Repository( inject = Injectible.ENABLE )
@ArezComponent
public class InjectEnabledRepository
{
  @Nonnull
  private String _name;

  InjectEnabledRepository( @Nonnull final String name )
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
