package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Computed;

@ArezComponent
public abstract class ComputedAndContainerIdMethodModel
{
  @ComponentId
  @Computed
  public long getField()
  {
    return 22;
  }
}
