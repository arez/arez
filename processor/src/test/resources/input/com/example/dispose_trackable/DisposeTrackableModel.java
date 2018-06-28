package com.example.dispose_trackable;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( disposeTrackable = Feature.ENABLE )
public abstract class DisposeTrackableModel
{
  @Action
  public void someValue()
  {
  }
}
