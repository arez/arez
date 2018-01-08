package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;

@ArezComponent
public class PreDisposeStaticModel
{
  @PreDispose
  static void doStuff()
  {
  }
}
