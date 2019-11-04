package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;

@ArezComponent
public abstract class PreDisposeReturnValueModel
{
  @PreDispose
  int doStuff()
  {
    return 0;
  }
}
