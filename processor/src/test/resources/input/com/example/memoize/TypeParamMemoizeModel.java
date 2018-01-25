package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class TypeParamMemoizeModel<T>
{
  @Memoize
  public T count( final String param )
  {
    return null;
  }
}
