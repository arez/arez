package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Dependency;
import arez.component.DisposeTrackable;

@ArezComponent
public abstract class ComputedDependency
{
  @Computed
  @Dependency
  public DisposeTrackable getValue()
  {
    return null;
  }
}
