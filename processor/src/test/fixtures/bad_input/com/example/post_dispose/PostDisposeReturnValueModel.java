package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class PostDisposeReturnValueModel
{
  @PostDispose
  int doStuff()
  {
    return 0;
  }
}
