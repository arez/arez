package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.component.DisposeTrackable;

@ArezComponent
public abstract class CascadeDependencyModel
{
  @Dependency( action = Dependency.Action.CASCADE )
  public final DisposeTrackable getTime()
  {
    return null;
  }
}
