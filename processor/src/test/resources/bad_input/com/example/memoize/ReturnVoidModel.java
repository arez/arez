package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class ReturnVoidModel
{
  @Memoize
  public void getField()
  {
  }
}
