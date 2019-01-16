package com.example.repository;

import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.Observable;
import arez.annotations.Repository;
import javax.annotation.Nonnull;

@Repository( inject = InjectMode.NONE )
@ArezComponent
public abstract class InjectDisabledRepository
{
  @Nonnull
  private String _name;

  InjectDisabledRepository( @Nonnull final String name )
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
