package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;

@ArezComponent( allowEmpty = true )
abstract class ProtectedAccessPostDisposeModel
{
  @PostDispose
  protected void postDispose()
  {
  }
}
