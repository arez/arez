package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Memoize;

@ArezComponent
public abstract class MemoizeAndContainerIdMethodModel
{
  @ComponentId
  @Memoize
  public long getField()
  {
    return 22;
  }
}
