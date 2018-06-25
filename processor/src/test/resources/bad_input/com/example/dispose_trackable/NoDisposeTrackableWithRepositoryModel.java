package com.example.dispose_trackable;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Repository;

@Repository
@ArezComponent( disposeTrackable = false )
public abstract class NoDisposeTrackableWithRepositoryModel
{
  @Action
  public void someValue()
  {
  }
}
