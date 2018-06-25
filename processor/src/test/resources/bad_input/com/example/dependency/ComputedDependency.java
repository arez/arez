package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Dependency;

@ArezComponent
public abstract class ComputedDependency
{
  @Computed
  @Dependency
  public Object getValue()
  {
    return null;
  }
}
