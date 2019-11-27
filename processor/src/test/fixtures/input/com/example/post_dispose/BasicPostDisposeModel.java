package com.example.post_dispose;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;

@ArezComponent
abstract class BasicPostDisposeModel
{
  @PostDispose
  void postDispose()
  {
  }

  @Action
  public int someValue()
  {
    return 0;
  }
}
