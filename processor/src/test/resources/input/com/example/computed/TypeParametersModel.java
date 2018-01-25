package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class TypeParametersModel
{
  @Computed
  public <T extends Integer> T getTime()
  {
    return null;
  }
}
