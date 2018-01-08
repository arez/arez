package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;

@ArezComponent
public class PostDisposeDuplicateModel
{
  @PostDispose
  void foo()
  {
  }

  @PostDispose
  void doStuff()
  {
  }
}
