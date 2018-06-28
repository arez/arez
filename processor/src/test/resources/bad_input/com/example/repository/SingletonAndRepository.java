package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Repository;
import javax.inject.Singleton;

@Singleton
@Repository
@ArezComponent(disposeTrackable = Feature.DISABLE)
public abstract class SingletonAndRepository
{
  @Action
  public void doStuff()
  {
  }
}
