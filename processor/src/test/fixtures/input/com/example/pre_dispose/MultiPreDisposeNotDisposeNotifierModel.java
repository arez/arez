package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.PreDispose;

@ArezComponent( allowEmpty = true, disposeNotifier = Feature.DISABLE )
public abstract class MultiPreDisposeNotDisposeNotifierModel
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
