package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class PostDisposeParametersModel
{
  @PostDispose
  void doStuff( int i )
  {
  }
}
