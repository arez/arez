package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class MissingComputedValueRefModel
{
  @Computed( arezOnlyDependencies = false )
  public Integer getValue()
  {
    return null;
  }
}
