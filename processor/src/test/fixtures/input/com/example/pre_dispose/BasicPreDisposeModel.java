package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;

@ArezComponent( allowEmpty = true )
abstract class BasicPreDisposeModel
{
  @PreDispose
  void preDispose()
  {
  }
}
