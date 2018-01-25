package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnDeactivate;

@ArezComponent
public abstract class ComputedAndOnDeactivateMethodModel
{
  @Computed
  @OnDeactivate
  public long getField()
  {
    return 22;
  }
}
