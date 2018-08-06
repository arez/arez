package com.example.repository;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.annotations.Repository;

@Repository
@ArezComponent
public abstract class RepositoryWithInitializerNameCollisionModel
{
  RepositoryWithInitializerNameCollisionModel( int time )
  {
  }

  @Observable( initializer = Feature.ENABLE )
  public abstract long getTime();

  public abstract void setTime( long value );
}
