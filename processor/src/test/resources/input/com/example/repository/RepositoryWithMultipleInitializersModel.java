package com.example.repository;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.annotations.Repository;

@Repository
@ArezComponent
public abstract class RepositoryWithMultipleInitializersModel
{
  @Observable( initializer = Feature.ENABLE )
  public abstract long getTime();

  public abstract void setTime( long value );

  @Observable( initializer = Feature.ENABLE )
  public abstract long getValue();

  public abstract void setValue( long value );
}
