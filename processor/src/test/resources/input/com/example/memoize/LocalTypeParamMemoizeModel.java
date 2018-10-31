package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class LocalTypeParamMemoizeModel
{
  @Computed
  public <T> T count( final String param )
  {
    return null;
  }
}
