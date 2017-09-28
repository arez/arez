package com.example.repository;

import javax.annotation.Nonnull;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.Repository;

@Repository
@ArezComponent
public class RepositoryWithMultipleCtors
{
  @Nonnull
  private String _name;

  RepositoryWithMultipleCtors( @Nonnull final String packageName, @Nonnull final String name )
  {
    _name = name;
  }

  RepositoryWithMultipleCtors( @Nonnull final String name )
  {
    _name = name;
  }

  RepositoryWithMultipleCtors()
  {
    _name = "";
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
