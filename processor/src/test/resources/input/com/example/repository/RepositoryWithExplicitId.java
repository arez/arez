package com.example.repository;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Observable;
import arez.annotations.Repository;
import javax.annotation.Nonnull;

@Repository
@ArezComponent
public abstract class RepositoryWithExplicitId
{
  private final int _id;
  @Nonnull
  private String _name;

  RepositoryWithExplicitId( @Nonnull final String packageName, @Nonnull final String name )
  {
    _id = 22;
    _name = name;
  }

  @ComponentId
  final int getId()
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
