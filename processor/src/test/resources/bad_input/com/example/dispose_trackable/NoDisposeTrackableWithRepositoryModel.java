package com.example.dispose_trackable;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Repository;

@Repository
@ArezComponent( disposeTrackable = Feature.DISABLE )
public abstract class NoDisposeTrackableWithRepositoryModel
{
  @Action
  public void someValue()
  {
  }
}
