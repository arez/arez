package com.example.dispose_trackable;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( disposeTrackable = Feature.DISABLE )
public abstract class NoDisposeTrackableModel
{
  @Action
  public void someValue()
  {
  }
}
