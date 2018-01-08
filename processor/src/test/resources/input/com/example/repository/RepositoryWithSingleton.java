package com.example.repository;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.Repository;
import javax.annotation.Nonnull;

@Repository
@ArezComponent( nameIncludesId = false )
public class RepositoryWithSingleton
{
  @Nonnull
  private String _name;

  RepositoryWithSingleton( @Nonnull final String name )
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
