package com.example.dispose_trackable;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent( disposeTrackable = false )
public abstract class NoDisposeTrackableModel
{
  @Action
  public void someValue()
  {
  }
}
