package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;

@ArezComponent
public abstract class PreDisposeStaticModel
{
  @PreDispose
  static void doStuff()
  {
  }
}
