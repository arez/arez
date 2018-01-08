package com.example.repository;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.PostDispose;
import arez.annotations.PreDispose;
import arez.annotations.Repository;
import javax.annotation.Nonnull;

@Repository
@ArezComponent
public class RepositoryPreDisposeHook
{
  @Nonnull
  private String _name;

  RepositoryPreDisposeHook( @Nonnull final String name )
  {
    _name = name;
  }

  @PreDispose
  void myPreDispose()
  {
  }

  @PostDispose
  void myPostDispose()
  {
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
