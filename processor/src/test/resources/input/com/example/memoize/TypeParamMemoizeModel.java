package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class TypeParamMemoizeModel<T>
{
  @Computed
  public T count( final String param )
  {
    return null;
  }
}
