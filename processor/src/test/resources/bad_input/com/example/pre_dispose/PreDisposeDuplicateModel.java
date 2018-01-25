package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;

@ArezComponent
public abstract class PreDisposeDuplicateModel
{
  @PreDispose
  void foo()
  {
  }

  @PreDispose
  void doStuff()
  {
  }
}
