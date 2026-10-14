package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class PreDisposeParametersModel
{
  @PreDispose
  void doStuff( int i )
  {
  }
}
