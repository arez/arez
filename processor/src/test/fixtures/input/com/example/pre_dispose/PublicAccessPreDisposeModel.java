package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;

@ArezComponent( allowEmpty = true )
abstract class PublicAccessPreDisposeModel
{
  @PreDispose
  public void preDispose()
  {
  }
}
