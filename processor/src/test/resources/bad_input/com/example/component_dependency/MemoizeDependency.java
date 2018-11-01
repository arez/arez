package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Memoize;
import arez.component.DisposeTrackable;

@ArezComponent
public abstract class MemoizeDependency
{
  @Memoize
  @ComponentDependency
  public DisposeTrackable getValue()
  {
    return null;
  }
}
