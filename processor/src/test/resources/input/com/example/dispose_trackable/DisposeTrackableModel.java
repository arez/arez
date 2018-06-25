package com.example.dispose_trackable;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent( disposeTrackable = true )
public abstract class DisposeTrackableModel
{
  @Action
  public void someValue()
  {
  }
}
