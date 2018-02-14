package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class LocalTypeParamMemoizeModel
{
  @Memoize
  public <T> T count( final String param )
  {
    return null;
  }
}
