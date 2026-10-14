package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class PublicAccessPreDisposeModel
{
  @PreDispose
  public void preDispose()
  {
  }
}
