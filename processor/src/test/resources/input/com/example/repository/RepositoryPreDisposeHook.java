package com.example.repository;

import javax.annotation.Nonnull;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.PostDispose;
import org.realityforge.arez.annotations.PreDispose;
import org.realityforge.arez.annotations.Repository;

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
