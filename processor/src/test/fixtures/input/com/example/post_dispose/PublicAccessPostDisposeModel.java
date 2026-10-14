package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class PublicAccessPostDisposeModel
{
  @PostDispose
  public void postDispose()
  {
  }
}
