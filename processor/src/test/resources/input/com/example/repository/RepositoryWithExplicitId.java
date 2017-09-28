package com.example.repository;

import javax.annotation.Nonnull;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentId;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.Repository;

@Repository
@ArezComponent
public class RepositoryWithExplicitId
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
