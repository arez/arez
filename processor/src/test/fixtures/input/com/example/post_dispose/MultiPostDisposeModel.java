package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;

@ArezComponent( allowEmpty = true )
abstract class MultiPostDisposeModel
{
  @PostDispose
  void postDispose1()
  {
  }

  @PostDispose
  void postDispose2()
  {
  }
}
