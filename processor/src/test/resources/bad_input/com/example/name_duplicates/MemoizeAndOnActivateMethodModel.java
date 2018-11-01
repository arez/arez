package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;

@ArezComponent
public abstract class MemoizeAndOnActivateMethodModel
{
  @Memoize
  @OnActivate
  public long getField()
  {
    return 22;
  }
}
