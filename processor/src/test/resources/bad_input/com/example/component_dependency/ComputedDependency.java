package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Computed;
import arez.component.DisposeTrackable;

@ArezComponent
public abstract class ComputedDependency
{
  @Computed
  @ComponentDependency
  public DisposeTrackable getValue()
  {
    return null;
  }
}
