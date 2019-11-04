package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class TypeParametersModel
{
  @Memoize
  public <T extends Integer> T getTime()
  {
    return null;
  }
}
