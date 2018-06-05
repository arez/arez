package com.example.repository;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.Repository;
import javax.annotation.Nonnull;

@Repository( detach = Repository.DetachType.DESTROY_OR_DETACH )
@ArezComponent
public abstract class RepositoryWithDestroyAndDetach
{
  @Nonnull
  private String _name;

  RepositoryWithDestroyAndDetach( @Nonnull final String name )
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
