package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;

@ArezComponent( allowEmpty = true )
public abstract class MultiPreDisposeModel
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
