package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class TypeParamModel<T>
{
  @Memoize
  public T count( final String param )
  {
    return null;
  }
}
