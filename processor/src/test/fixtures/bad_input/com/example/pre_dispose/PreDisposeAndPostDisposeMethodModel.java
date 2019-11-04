package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;
import arez.annotations.PreDispose;

@ArezComponent
public abstract class PreDisposeAndPostDisposeMethodModel
{
  @PreDispose
  @PostDispose
  public long doStuff()
  {
    return 22;
  }
}
